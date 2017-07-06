package spring.mqtt.broker.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.PooledByteBufAllocator;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import spring.mqtt.broker.server.Handler.ChildChannelHandler;

import javax.annotation.PostConstruct;
import java.util.concurrent.ThreadFactory;

/**
 * Created by Administrator on 2017/7/6.
 * 应用启动类
 */
@Service
public class ApplicationService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationService.class);

    private EventLoopGroup bossGroup;

    private EventLoopGroup workerGroup;

    protected volatile ByteBufAllocator allocator;

    //cpu数
    private static final int TASK_GROUP = Runtime.getRuntime().availableProcessors();
    //工作组数量
    private int workers = 0;

    @Autowired
    private ChildChannelHandler childChannelHandler;


    public ApplicationService (){
        this(TASK_GROUP << 1);
    }
    public ApplicationService (final int workers){

        this.workers = workers;
    }

    @PostConstruct
    public void runServie(int port,String host,boolean sslEnable,int sslPort,String sslCertPath,String sslkeyPath,String sslKeyPassword){
        LOGGER.info("启动netty服务器");
        ThreadFactory boss = new DefaultThreadFactory("netty.accpet.boss");
        ThreadFactory work = new DefaultThreadFactory("netty.accpet.work");
        bossGroup = new NioEventLoopGroup(1,boss);
        workerGroup = new NioEventLoopGroup(workers,work);
        try {
            ServerBootstrap b = new ServerBootstrap();
            //使用池化的directBuffer
            allocator = new PooledByteBufAllocator(PlatformDependent.directBufferPreferred());
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 32768)
                    .option(ChannelOption.SO_REUSEADDR, true)
                    .childOption(ChannelOption.SO_REUSEADDR, true)
                    .childOption(ChannelOption.SO_KEEPALIVE, true) //此选项的目的是检测对端主机是否崩溃, 仅对TCP套接字有效.
                    .childOption(ChannelOption.TCP_NODELAY, true) //禁用 Nagle 算法.
                    .childOption(ChannelOption.ALLOW_HALF_CLOSURE, false)  //禁用掉半关闭的状态的链接状态
                    .childOption(ChannelOption.ALLOCATOR, allocator)
                    .handler(new LoggingHandler(LogLevel.INFO)) //启动日志
                    .childHandler(childChannelHandler);
            //绑定端口，同步等待成功
            ChannelFuture f = b.bind(host,port).sync();
            LOGGER.info("netty服务器启动成功");
            //等待服务端监听端口关闭
            f.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            LOGGER.error("服务中断: "+e.getMessage());
            e.printStackTrace();
        }finally{
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
