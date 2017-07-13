package spring.mqtt.redis.bean;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Created by admin on 2017/7/7.
 */

@Component
public class RedisConfig {

    @Value("${mqtt.inflight.queue.size}")
    private int MQTT_INFLIGHT_QUEUE_SIZE;

    @Value("${mqtt.qos2.queue.size}")
    private int MQTT_QOS2_QUEUE_SIZE;

    @Value("${mqtt.retain.queue.size}")
    private int MQTT_RETAIN_QUEUE_SIZE;

    @Value("${redis.type}")
    private String REDIS_TYPE;

    @Value("${redis.address}")
    private String REDIS_ADDRESS;

    @Value("${redis.database}")
    private int  REDIS_DATABASE;

    @Value("${redis.password}")
    private String REDIS_PASSWORD;

    @Value("${redis.master}")
    private  String REDIS_MASTER;

    @Value("${redis.read}")
    private String REDIS_READ;

    @Value("${redis.cluster.periodicRefreshEnabled }")
    private boolean REDIS_CLUSTER_PERIODICREFRESHENABLED;

    @Value("${redis.cluster.refreshPeriod}")
    private int REDIS_CLUSTER_REFRESHPERIOD;

    @Value("${redis.cluster.closeStaleConnections}")
    private boolean REDIS_CLUSTER_CLOSESTALECONNECTIONS;

    @Value("${redis.cluster.validateClusterNodeMembership}")
    private boolean REDIS_CLUSTER_VALIDATECLUSTERNODEMEMBERSHIP;

    @Value("${redis.cluster.maxRedirects}")
    private int REDIS_CLUSTER_MAXREDIRECTS;

    public int getMQTT_INFLIGHT_QUEUE_SIZE() {
        return MQTT_INFLIGHT_QUEUE_SIZE;
    }

    public void setMQTT_INFLIGHT_QUEUE_SIZE(int MQTT_INFLIGHT_QUEUE_SIZE) {
        this.MQTT_INFLIGHT_QUEUE_SIZE = MQTT_INFLIGHT_QUEUE_SIZE;
    }

    public int getMQTT_QOS2_QUEUE_SIZE() {
        return MQTT_QOS2_QUEUE_SIZE;
    }

    public void setMQTT_QOS2_QUEUE_SIZE(int MQTT_QOS2_QUEUE_SIZE) {
        this.MQTT_QOS2_QUEUE_SIZE = MQTT_QOS2_QUEUE_SIZE;
    }

    public int getMQTT_RETAIN_QUEUE_SIZE() {
        return MQTT_RETAIN_QUEUE_SIZE;
    }

    public void setMQTT_RETAIN_QUEUE_SIZE(int MQTT_RETAIN_QUEUE_SIZE) {
        this.MQTT_RETAIN_QUEUE_SIZE = MQTT_RETAIN_QUEUE_SIZE;
    }

    public String getREDIS_TYPE() {
        return REDIS_TYPE;
    }

    public void setREDIS_TYPE(String REDIS_TYPE) {
        this.REDIS_TYPE = REDIS_TYPE;
    }

    public String getREDIS_ADDRESS() {
        return REDIS_ADDRESS;
    }

    public void setREDIS_ADDRESS(String REDIS_ADDRESS) {
        this.REDIS_ADDRESS = REDIS_ADDRESS;
    }

    public int getREDIS_DATABASE() {
        return REDIS_DATABASE;
    }

    public void setREDIS_DATABASE(int REDIS_DATABASE) {
        this.REDIS_DATABASE = REDIS_DATABASE;
    }

    public String getREDIS_PASSWORD() {
        return REDIS_PASSWORD;
    }

    public void setREDIS_PASSWORD(String REDIS_PASSWORD) {
        this.REDIS_PASSWORD = REDIS_PASSWORD;
    }

    public String getREDIS_MASTER() {
        return REDIS_MASTER;
    }

    public void setREDIS_MASTER(String REDIS_MASTER) {
        this.REDIS_MASTER = REDIS_MASTER;
    }

    public String getREDIS_READ() {
        return REDIS_READ;
    }

    public void setREDIS_READ(String REDIS_READ) {
        this.REDIS_READ = REDIS_READ;
    }

    public boolean isREDIS_CLUSTER_PERIODICREFRESHENABLED() {
        return REDIS_CLUSTER_PERIODICREFRESHENABLED;
    }

    public void setREDIS_CLUSTER_PERIODICREFRESHENABLED(boolean REDIS_CLUSTER_PERIODICREFRESHENABLED) {
        this.REDIS_CLUSTER_PERIODICREFRESHENABLED = REDIS_CLUSTER_PERIODICREFRESHENABLED;
    }

    public int getREDIS_CLUSTER_REFRESHPERIOD() {
        return REDIS_CLUSTER_REFRESHPERIOD;
    }

    public void setREDIS_CLUSTER_REFRESHPERIOD(int REDIS_CLUSTER_REFRESHPERIOD) {
        this.REDIS_CLUSTER_REFRESHPERIOD = REDIS_CLUSTER_REFRESHPERIOD;
    }

    public boolean isREDIS_CLUSTER_CLOSESTALECONNECTIONS() {
        return REDIS_CLUSTER_CLOSESTALECONNECTIONS;
    }

    public void setREDIS_CLUSTER_CLOSESTALECONNECTIONS(boolean REDIS_CLUSTER_CLOSESTALECONNECTIONS) {
        this.REDIS_CLUSTER_CLOSESTALECONNECTIONS = REDIS_CLUSTER_CLOSESTALECONNECTIONS;
    }

    public boolean isREDIS_CLUSTER_VALIDATECLUSTERNODEMEMBERSHIP() {
        return REDIS_CLUSTER_VALIDATECLUSTERNODEMEMBERSHIP;
    }

    public void setREDIS_CLUSTER_VALIDATECLUSTERNODEMEMBERSHIP(boolean REDIS_CLUSTER_VALIDATECLUSTERNODEMEMBERSHIP) {
        this.REDIS_CLUSTER_VALIDATECLUSTERNODEMEMBERSHIP = REDIS_CLUSTER_VALIDATECLUSTERNODEMEMBERSHIP;
    }

    public int getREDIS_CLUSTER_MAXREDIRECTS() {
        return REDIS_CLUSTER_MAXREDIRECTS;
    }

    public void setREDIS_CLUSTER_MAXREDIRECTS(int REDIS_CLUSTER_MAXREDIRECTS) {
        this.REDIS_CLUSTER_MAXREDIRECTS = REDIS_CLUSTER_MAXREDIRECTS;
    }
}
