package spring.mqtt.api.cluster;


import spring.mqtt.api.message.Message;

/**
 * Cluster
 */
public interface Cluster {

    /**
     * Init the cluster
     *
     * @param config  Configuration
     * @param factory Cluster Listener Factory
     * @throws ClusterException if there is a exception when trying to init cluster.
     */
    void init( ClusterListenerFactory factory) throws ClusterException;

    /**
     * Destroy the cluster
     */
    void destroy();

    /**
     * Send message to broker
     *
     * @param brokerId Broker Id
     * @param message  Message
     */
    void sendToBroker(String brokerId, Message message);

    /**
     * Send message to outside
     * Other application can pick up the message from there
     *
     * @param message Message
     */
    void sendToApplication(Message message);
}
