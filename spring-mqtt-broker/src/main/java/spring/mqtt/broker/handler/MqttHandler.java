package spring.mqtt.broker.handler;

import io.netty.channel.ChannelPipeline;
import io.netty.channel.DefaultEventLoopGroup;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.mqtt.MqttDecoder;
import io.netty.handler.codec.mqtt.MqttEncoder;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.timeout.IdleStateHandler;
import spring.mqtt.api.ChannelHandler;
import spring.mqtt.api.Context;

import javax.net.ssl.SSLException;
import java.io.File;

/**
 * Created by Micheal-Bigmac on 2017/7/14.
 */
public class MqttHandler extends ChannelHandler<SocketChannel> {
    final boolean ssl;
    final SslContext sslContext;
    final int keepAlive;

    public MqttHandler(Context context) throws SSLException {
        super(context);
        ssl = context.getBoolean("mqtt.ssl.enabled");
        final String sslCretPath = context.getString("mqtt.ssl.certPath");
        final String keyPath = context.getString("mqtt.ssl.keyPath");
        keepAlive = context.getInt("mqtt.keepalive.default", 50);

        sslContext = ssl ? SslContextBuilder.forServer(new File(sslCretPath), new File(keyPath)).build() : null;
    }

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {

        EventLoopGroup bussinessGroup=new DefaultEventLoopGroup(10, r -> {
            Thread thread=new Thread(r);
            thread.setName("业务线程 Bussiness Thread ");
            return thread;
        });

        ChannelPipeline pipeline = socketChannel.pipeline();
        if (ssl) {
            pipeline.addLast("ssl", sslContext.newHandler(socketChannel.alloc()));
        }
        pipeline.addLast("idleStateHandler", new IdleStateHandler(0, 0, keepAlive));
        pipeline.addLast("encoder", MqttEncoder.INSTANCE);
        pipeline.addLast("decoder",new MqttDecoder());
//        pipeline.addLast(bussinessGroup,"Bussiness",new );
    }
}
