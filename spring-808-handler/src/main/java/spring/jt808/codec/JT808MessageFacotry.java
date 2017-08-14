package spring.jt808.codec;

import io.netty.buffer.ByteBuf;

/**
 * Created by Micheal on 2017/8/5.
 */
public class JT808MessageFacotry {
    public static JT808Message newMessage(JT808Header header,ByteBuf body,int checkSum) throws Exception {
        switch (header.getMsgId()){
            case RESPON:
                
                return null;
            case HEART_BEAT:

                return null;
            case REGISTER:

                return null;
            case LOG_OUT:

                return null;
            case AUTHENTICATION:

                return null;
            case LOCATION_INFO_UPLOAD:

                return null;
            case TRANSMISSION_TYRE_PRESSURE:

                return null;
            case PARAM_QUERY_RESP:

                return null;
            case CMD_COMMON_RESP:

                return null;
            case CMD_TERMINAL_REGISTER_RESP:

                return null;
            case CMD_TERMINAL_PARAM_SETTINGS:

                return null;
            case CMD_TERMINAL_PARAM_QUERY:

                return null;
            default:
                throw new Exception("Unknown messageType");
        }

    }
}
