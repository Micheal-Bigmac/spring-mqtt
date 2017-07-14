package spring.mqtt.broker.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.PooledByteBufAllocator;
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
public class NettyApplication implements NettyServer {
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

    @Override
    public void connect() {
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
                .handler(new LoggingHandler(LogLevel.INFO));
                // deal with Handler By kinds of protocol
        if(handler!=null){
            bootstarp.childHandler(handler);
        }
        try {
            bootstarp.bind(host,port).sync();
            LOGGER.info("服务启动成功");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void stop() {

    }
}
