package spring.mqtt.api;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;

/**
 * Created by Micheal-Bigmac on 2017/7/13.
 */
public abstract class ChannelHandler<T extends Channel> extends ChannelInitializer<T>{

     Context context;

    public ChannelHandler(Context context) {
        this.context = context;
    }
}
