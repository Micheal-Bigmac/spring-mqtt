package spring.mqtt.redis.storage;

import com.lambdaworks.redis.ReadFrom;
import com.lambdaworks.redis.RedisClient;
import com.lambdaworks.redis.RedisURI;
import com.lambdaworks.redis.api.sync.*;
import com.lambdaworks.redis.codec.Utf8StringCodec;
import com.lambdaworks.redis.masterslave.MasterSlave;
import com.lambdaworks.redis.masterslave.StatefulRedisMasterSlaveConnection;
import org.apache.commons.lang3.StringUtils;
import spring.mqtt.api.Context;

import java.util.List;

/**
 * Synchronized Storage for Master Slave Redis setup
 */
@SuppressWarnings("unused")
public class RedisSyncSentinelStorageImpl extends RedisSyncSingleStorageImpl {

    // A scalable thread-safe Redis client. Multiple threads may share one connection if they avoid
    // blocking and transactional operations such as BLPOP and MULTI/EXEC.
    private RedisClient lettuceSentinel;
    // A thread-safe connection to a redis server. Multiple threads may share one StatefulRedisConnection
    private StatefulRedisMasterSlaveConnection<String, String> lettuceSentinelConn;
    // Main infrastructure class allows to get access to all Redisson objects on top of Redis server

    protected RedisHashCommands<String, String> hash() {
        return this.lettuceSentinelConn.sync();
    }

    protected RedisKeyCommands<String, String> key() {
        return this.lettuceSentinelConn.sync();
    }

    protected RedisStringCommands<String, String> string() {
        return this.lettuceSentinelConn.sync();
    }

    protected RedisListCommands<String, String> list() {
        return this.lettuceSentinelConn.sync();
    }

    protected RedisSetCommands<String, String> set() {
        return this.lettuceSentinelConn.sync();
    }

    protected RedisSortedSetCommands<String, String> sortedSet() {
        return this.lettuceSentinelConn.sync();
    }

    protected RedisScriptingCommands<String, String> script() {
        return this.lettuceSentinelConn.sync();
    }

    protected RedisServerCommands<String, String> server() {
        return this.lettuceSentinelConn.sync();
    }

    protected RedisHLLCommands<String, String> hll() {
        return this.lettuceSentinelConn.sync();
    }

    protected RedisGeoCommands<String, String> geo() {
        return this.lettuceSentinelConn.sync();
    }

    @Override
    public void init(Context config) {
//        AbstractConfiguration config
        if (!config.getString("redis.type").equals("sentinel")) {
            throw new IllegalStateException("RedisSyncSingleStorageImpl class can only be used with sentinel redis setup, but redis.type value is " + config.getString("redis.type"));
        }

        List<String> address = parseRedisAddress(config.getString("redis.address"), 26379);
        int databaseNumber = config.getInt("redis.database", 0);
        String password = StringUtils.isNotEmpty(config.getString("redis.password")) ? config.getString("redis.password") + "@" : "";
        String masterId = config.getString("redis.master");

        // lettuce
        RedisURI lettuceURI = RedisURI.create("redis-sentinel://" + password + String.join(",", address) + "/" + databaseNumber + "#" + masterId);
        this.lettuceSentinel = RedisClient.create(lettuceURI);
        this.lettuceSentinelConn = MasterSlave.connect(this.lettuceSentinel, new Utf8StringCodec(), lettuceURI);
        this.lettuceSentinelConn.setReadFrom(ReadFrom.valueOf(config.getString("redis.read")));

        // params
        initParams(config);
    }

    @Override
    public void destroy() {
        // shutdown this client and close all open connections
        if (this.lettuceSentinelConn != null) this.lettuceSentinelConn.close();
        if (this.lettuceSentinel != null) this.lettuceSentinel.shutdown();
    }
}
