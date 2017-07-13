package spring.mqtt.redis.storage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import spring.mqtt.api.Context;
import spring.mqtt.api.storage.SyncStorage;


@Service
public class RedisFactory {


    private static final Logger logger = LoggerFactory
            .getLogger(RedisFactory.class);

    public static SyncStorage getRedisObjectByType(Context context) throws Exception{

        String redis_type = context.getString("redis.type");
        SyncStorage storage=null;
        if("single".equals(redis_type)){
           storage = new RedisSyncSingleStorageImpl();
            storage.init(context);
        }else if("cluster".equals(redis_type)){
            storage=new RedisSyncClusterStorageImpl();
            storage.init(context);
        }else if("sentinel".equals(redis_type)){
            storage=new RedisSyncSentinelStorageImpl();
            storage.init(context);
        }else{
            throw new Exception("Redis Config is not Right");
        }
        return storage;
    }
}
