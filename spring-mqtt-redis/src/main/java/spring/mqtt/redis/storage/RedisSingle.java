package spring.mqtt.redis.storage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import spring.mqtt.api.storage.SyncStorage;
import spring.mqtt.redis.bean.RedisConfig;

@Service
public class RedisSingle {

    @Autowired
    RedisConfig redisConfig;
    private  SyncStorage storage;

    public RedisSingle() {
        if(redisConfig.getREDIS_TYPE().equals("cluster")){
            storage=new RedisSyncClusterStorageImpl();
            storage.init();
        }else if(redisConfig.getREDIS_TYPE().equals("single")){
            storage.init();
        }else if (redisConfig.getREDIS_TYPE().equals("sentinel")){
            storage.init();
        }
    }
}
