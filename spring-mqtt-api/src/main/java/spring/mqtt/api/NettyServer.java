package spring.mqtt.api;

/**
 * Created by Micheal-Bigmac on 2017/7/13.
 */
public abstract class NettyServer {

     public abstract void configure(Context context) ;
     synchronized void  connect(){};
     synchronized void  stop(){};
}
