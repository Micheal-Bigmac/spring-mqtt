package spring.mqtt.api;

/**
 * Created by Micheal-Bigmac on 2017/7/13.
 */
public interface NettyServer {

     void configure(Context context) ;
     void connect();
     void stop();
}
