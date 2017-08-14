package spring.jt808.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spring.broker.session.SessionRegistry;
import spring.jt808.codec.JT808Header;
import spring.jt808.codec.JT808Message;

/**
 * Created by Micheal on 2017/8/5.
 */
public class JT808BussinessHandler extends SimpleChannelInboundHandler<JT808Message>{
    private static final Logger logger = LoggerFactory.getLogger(JT808BussinessHandler.class);
    private final SessionRegistry registry;


    public JT808BussinessHandler(SessionRegistry registry){
        this.registry=registry;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, JT808Message jt808Message) throws Exception {
       final JT808Header msgHeader = jt808Message.getMsgHeader();
        switch (msgHeader.getMsgId()){
            case RESPON:

                break;
            case HEART_BEAT:
                logger.info(">>>>>[终端心跳],phone={},flowid={}", msgHeader.getTerminalPhone(), msgHeader.getFlowId());
//                this.registry.sendMessage();  // 给 终端发送相应消息 Handler 链 执行完毕后 进入Encode
                break;
            case REGISTER:

                break;
            case LOG_OUT:

                break;
            case AUTHENTICATION:

                break;
            case LOCATION_INFO_UPLOAD:

                break;
            case TRANSMISSION_TYRE_PRESSURE:

                break;
            case PARAM_QUERY_RESP:

                break;
            case CMD_COMMON_RESP:

                break;
            case CMD_TERMINAL_REGISTER_RESP:

                break;
            case CMD_TERMINAL_PARAM_SETTINGS:

                break;
            case CMD_TERMINAL_PARAM_QUERY:

                break;
        }

    }
}
