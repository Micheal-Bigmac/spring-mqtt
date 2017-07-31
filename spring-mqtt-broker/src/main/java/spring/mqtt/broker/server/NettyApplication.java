package spring.mqtt.broker.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.AdaptiveRecvByteBufAllocator;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.util.concurrent.DefaultThreadFactory;
import io.netty.util.internal.PlatformDependent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spring.mqtt.api.ChannelHandler;
import spring.mqtt.api.Context;
import spring.mqtt.api.NettyServer;
import spring.mqtt.broker.handler.HandlerFactory;

import java.util.concurrent.ThreadFactory;

/**
 * Created by Micheal-Bigmac on 2017/7/13.
 */
public class NettyApplication extends NettyServer {
    private static final Logger LOGGER = LoggerFactory.getLogger(NettyApplication.class);
    private static final int Task_Group = Runtime.getRuntime().availableProcessors(); //CPU Core

    private int workers = 0;  // 工作组 数量


    private EventLoopGroup boosGroup;
    private EventLoopGroup workerGroup;
    protected volatile ByteBufAllocator allocator;

    private Context context;


    private String host;
    private int port;

    private int keepAliveMax;
    private ChannelHandler handler;

    @Override
    public void configure(Context context) {

        final boolean ssl = context.getBoolean("mqtt.ssl.enabled");
        host = context.getString("mqtt.host");
        port = ssl ? context.getInt("mqtt.ssl.port") : context.getInt("mqtt.port");
        keepAliveMax = context.getInt("mqtt.keepalive.max",50);
        handler= HandlerFactory.getInstance(context);
    }

    /***
     * FixedRecvByteBufAllocator：固定长度的接收缓冲区分配器，由它分配的ByteBuf长度都是固定大小的，并不会根据实际数据报的大小动态收缩。但是，如果容量不足，支持动态扩展。动态扩展是Netty ByteBuf的一项基本功能，与ByteBuf分配器的实现没有关系；
     * AdaptiveRecvByteBufAllocator：容量动态调整的接收缓冲区分配器，它会根据之前Channel接收到的数据报大小进行计算，如果连续填充满接收缓冲区的可写空间，则动态扩展容量。如果连续2次接收到的数据报都小于指定值，则收缩当前的容量，以节约内存。
     */

    /**
     * 实ChannelOutboundBuffer虽然无界，但是可以给它配置一个高水位线和低水位线，
     * 当buffer的大小超过高水位线的时候对应channel的isWritable就会变成false，当buffer的大小低于低水位线的时候，isWritable就会变成true。
     * 所以应用应该判断isWritable，
     * 如果是false就不要再写数据了。高水位线和低水位线是字节数，默认高水位是64K，低水位是32K，
     */

    /***
     *  设置AutoRead 调整 client  Server 两端的 TCP 窗口大小
     *
         int highReadWaterMarker = 900;
         int lowReadWaterMarker = 600;
         ThreadPoolExecutor executor = new ThreadPoolExecutor(8, 8, 1, TimeUnit.MINUTES, new ArrayBlockingQueue<Runnable>(1000), namedFactory, new ThreadPoolExecutor.DiscardOldestPolicy());
         int queued = executor.getQueue().size();
         if(queued > highReadWaterMarker){
         channel.config().setAutoRead(false);
         }
         if(queued < lowReadWaterMarker){
         channel.config().setAutoRead(true);
         }
     *
     */

//  TCP层面的接收和发送缓冲区大小设置  应ChannelOption的SO_SNDBUF和SO_RCVBUF，需要根据推送消息的大小，合理设置
    public synchronized void connect() {
        LOGGER.info("启动netty服务器");
        ThreadFactory bossFactory = new DefaultThreadFactory("netty.accept.boss");
        ThreadFactory workFactory = new DefaultThreadFactory("netty.accept.work");
        boosGroup = new NioEventLoopGroup(1, bossFactory);
        workerGroup = new NioEventLoopGroup(workers, workFactory);
        ServerBootstrap bootstarp = new ServerBootstrap();
        allocator = new PooledByteBufAllocator(PlatformDependent.directBufferPreferred()); //使用池化的directBuffer
        bootstarp.group(boosGroup, workerGroup).channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG, 32768)
                .option(ChannelOption.SO_REUSEADDR, true)
                .option(ChannelOption.SO_KEEPALIVE,true)//此选项的目的是检测对端主机是否崩溃, 仅对TCP套接字有效
                .option(ChannelOption.TCP_NODELAY,true)//禁用 Nagle 算法.
                .option(ChannelOption.ALLOW_HALF_CLOSURE,false) //禁用掉半关闭的状态的链接状态
                .option(ChannelOption.ALLOCATOR,allocator)
                .option(ChannelOption.RCVBUF_ALLOCATOR, AdaptiveRecvByteBufAllocator.DEFAULT)  // 默认的 bytebuf  缓冲区  如果默认没有设置，则使用AdaptiveRecvByteBufAllocator。
                .option(ChannelOption.WRITE_BUFFER_HIGH_WATER_MARK, 64 * 1024)   //netty  ChannelOutBoundBuffer 最高水位线
                .option(ChannelOption.WRITE_BUFFER_LOW_WATER_MARK, 32 * 1024)    //netty  ChannelOutBoundBuffer 最低水位线  当netty 程序 向channel write 时 会把ByteBuff 添加到缓存中 如果 处理TCP 包 速度太慢 造成Buffer 过高 系统Swap 会变高  根据系统内核内存分配设置 （1，2，3） 直接杀死该进程
                .handler(new LoggingHandler(LogLevel.INFO));
                // deal with Handler By kinds of protocol
        if(handler!=null){
            bootstarp.childHandler(handler);
        }
        try {
            ChannelFuture future = bootstarp.bind(host, port).sync();
            LOGGER.info("服务启动成功");
            future.channel().closeFuture().sync(); // 阻塞等待
        } catch (InterruptedException e) {
            LOGGER.error("服务中断: "+e.getMessage());
            e.printStackTrace();
        }
    }

    public synchronized void stop() {
        if(!boosGroup.isShutdown()){
            boosGroup.shutdownGracefully();
        }
        if(!workerGroup.isShutdown()){
            workerGroup.shutdownGracefully();
        }


    }
}
