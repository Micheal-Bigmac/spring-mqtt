package spring.mqtt.broker.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.concurrent.DefaultThreadFactory;
import io.netty.util.internal.PlatformDependent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import spring.mqtt.broker.config.NettyConfig;

import javax.annotation.PostConstruct;
import javax.net.ssl.SSLException;
import java.io.File;
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
    private NettyConfig nettyConfig;


    public ApplicationService (){
        this(TASK_GROUP << 1);
    }
    public ApplicationService (final int workers){
        this.workers = workers;
    }

    @PostConstruct
    public void runServie() throws SSLException {
        final boolean ssl = nettyConfig.isSslEnable();
        final SslContext sslContext = ssl ? SslContextBuilder.forServer(new File(nettyConfig.getSslCertPath()), new File(nettyConfig.getSslkeyPath()), nettyConfig.getSslKeyPassword()).build() : null;
        final String host = nettyConfig.getHost();
        final int port = ssl ? nettyConfig.getSslPort() : nettyConfig.getPort();
        final int keepAlive = nettyConfig.getKeepAlive();
        final int keepAliveMax = nettyConfig.getKeepAliveMax();
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
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel sch) throws Exception {
                            ChannelPipeline pipeline = sch.pipeline();
                            // ssl
                            if (ssl) {
                                pipeline.addLast("ssl", sslContext.newHandler(sch.alloc()));
                            }
                            pipeline.addLast("idleStateHandler", new IdleStateHandler(0, 0, keepAlive));

                        }
                    });
            //绑定端口，同步等待成功
            ChannelFuture f = b.bind(host,port).sync();
            LOGGER.info("服务启动成功");
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
