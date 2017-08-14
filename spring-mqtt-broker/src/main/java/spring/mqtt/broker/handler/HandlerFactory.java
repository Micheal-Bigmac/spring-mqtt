package spring.mqtt.broker.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spring.jt808.handler.JT808ChannelHandler;
import spring.mqtt.api.ChannelHandler;
import spring.mqtt.api.Context;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Locale;

/**
 * Created by Micheal-Bigmac on 2017/7/14.
 */
public class HandlerFactory {
    private static final Logger LOGGER = LoggerFactory.getLogger(HandlerFactory.class);

    public static ChannelHandler getInstance(Context context)  {
        String protocolType = context.getString("protocol.type");
        LOGGER.info("Current Channel Protocol is "+protocolType);
        if (protocolType == null || protocolType.isEmpty()) {
            protocolType = ProtoColType.OTHER.getProtoCol();
        }
        Class<? extends ChannelHandler> handler = null;
        Constructor<? extends ChannelHandler> constructor=null;
        try {
            ProtoColType type= ProtoColType.valueOf(protocolType.toUpperCase(Locale.ENGLISH));
            String className=type.getProtoCol();
            LOGGER.info(" Class：+"+className+" initialize ChnnelHandler .......");
            handler = (Class<? extends ChannelHandler>) Class.forName(className);
            constructor = handler.getConstructor(Context.class);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        try {
            return constructor.newInstance(context);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }finally {
            LOGGER.info("initialize  ChannelHandler finished ");
        }
        return null;
    }

    static enum ProtoColType {
        OTHER(null),
        DEFAULT("Default"),
        PRO_808(JT808ChannelHandler.class.getCanonicalName()) ,  // 有待完善交通部 808 协议
        PRO_809("809"),   // 有待完善 交通部809 协议
        PRO_MQTT(MqttHandler.class.getCanonicalName());

        private final String ProtoCol;

        ProtoColType(String s) {
            this.ProtoCol = s;
        }

        protected String getProtoCol() {
            return this.ProtoCol;
        }
    }
}
