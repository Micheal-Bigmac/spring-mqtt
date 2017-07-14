package spring.mqtt.broker;

import spring.mqtt.api.ChannelHandler;
import spring.mqtt.api.Context;
import spring.mqtt.broker.handler.HandlerFactory;

/**
 * Created by Micheal-Bigmac on 2017/7/14.
 */
public class TestHandlerFactory {

    public static void main(String[] args){
        Context context=new Context();
        context.put("protocol.type","PRO_MQTT");
        context.put("mqtt.ssl.enabled","false");
        ChannelHandler instance = HandlerFactory.getInstance(context);
        System.out.println("123456");
    }
}
