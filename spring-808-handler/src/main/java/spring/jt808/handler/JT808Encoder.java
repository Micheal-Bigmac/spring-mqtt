package spring.jt808.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import spring.jt808.codec.JT808Message;

import java.util.List;

/**
 * Created by Micheal-Bigmac on 2017/7/31.
 */
public class JT808Encoder extends MessageToMessageEncoder<JT808Message> {
    public JT808Encoder() {
    }

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, JT808Message jt808Message, List<Object> list) throws Exception {

    }
}
