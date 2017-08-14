package spring.jt808.handler;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.DefaultEventLoopGroup;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.timeout.IdleStateHandler;
import spring.mqtt.api.ChannelHandler;
import spring.mqtt.api.Context;

import javax.net.ssl.SSLException;
import java.io.File;

/**
 * Created by Micheal on 2017/8/5.
 */
public class JT808ChannelHandler extends ChannelHandler<SocketChannel> {

    final boolean ssl;
    final SslContext sslContext;
    final int keepAlive;

    public JT808ChannelHandler(Context context) throws SSLException {
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
        if (sslContext !=null) {
            pipeline.addLast("ssl", sslContext.newHandler(socketChannel.alloc()));
        }
        pipeline.addLast("idleStateHandler", new IdleStateHandler(0, 0, keepAlive));
       pipeline.addLast(new DelimiterBasedFrameDecoder(1024, Unpooled.copiedBuffer(new byte[]{0x7e}), Unpooled.copiedBuffer(new byte[]{0x7e, 0x7e})));
        pipeline.addLast("encoder",new JT808Encoder());
        pipeline.addLast("decoder",new JT808Decoder());
//        pipeline.addLast("bussiness",new JT808BussinessHandler());
//        pipeline.addLast(bussinessGroup,"Bussiness",new );
    }
}
