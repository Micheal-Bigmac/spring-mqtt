package spring.jt808.common;

/**
 * Created by Micheal on 2017/8/5.
 */
public enum JT808MessageType {
    RESPON(0x0001),
    HEART_BEAT(0x0002),
    REGISTER(0x0100),
    LOG_OUT(0x0003),
    AUTHENTICATION(0x0102),
    LOCATION_INFO_UPLOAD(0x0200),
    TRANSMISSION_TYRE_PRESSURE(0x0600),
    PARAM_QUERY_RESP(0x0104),
    CMD_COMMON_RESP(0x8001),
    CMD_TERMINAL_REGISTER_RESP(0x8100),
    CMD_TERMINAL_PARAM_SETTINGS(0X8103),
    CMD_TERMINAL_PARAM_QUERY(0x8104);

    private final  int value;
    JT808MessageType(int i) {
        this.value=i;
    }
    public static JT808MessageType valueOf(int type){
        JT808MessageType[] values = values();
        for(JT808MessageType type1 : values){
            if(type1.value==type){
                return type1;
            }
        }
        throw  new IllegalArgumentException("UnCorrect type value ");
    }


}
