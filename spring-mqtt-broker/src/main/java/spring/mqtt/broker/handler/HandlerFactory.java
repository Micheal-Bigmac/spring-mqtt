package spring.mqtt.broker.handler;

import spring.mqtt.api.ChannelHandler;
import spring.mqtt.api.Context;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Locale;

/**
 * Created by Micheal-Bigmac on 2017/7/14.
 */
public class HandlerFactory {


    public static ChannelHandler getInstance(Context context)  {
        String protocolType = context.getString("protocol.type");
        if (protocolType == null || protocolType.isEmpty()) {
            protocolType = ProtoColType.OTHER.getProtoCol();
        }
        Class<? extends ChannelHandler> handler = null;
        Constructor<? extends ChannelHandler> constructor=null;
        try {
            ProtoColType type= ProtoColType.valueOf(protocolType.toUpperCase(Locale.ENGLISH));
            String className=type.getProtoCol();
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
        }
        return null;
    }

    static enum ProtoColType {
        OTHER(null),
        DEFAULT("Default"),
        PRO_808("808"),   // 有待完善交通部 808 协议
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
