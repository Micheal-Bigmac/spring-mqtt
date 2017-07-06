package spring.mqtt.api.cluster;

/**
 * Cluster Listener Factory
 */
public interface ClusterListenerFactory {

    /**
     * Create a new ClusterListener
     *
     * @return ClusterListener
     */
    ClusterListener newListener();
}
