package spring.mqtt.api.cluster;


import spring.mqtt.api.message.Message;

/**
 * Cluster Event Listener
 */
public interface ClusterListener {

    /**
     * Received Connect Message Event
     *
     * @param msg Connect Message
     */
    void onConnect(Message<Object, Object> msg);

    /**
     * Received Subscribe Message Event
     *
     * @param msg Subscribe Message
     */
    void onSubscribe(Message<Object, Object> msg);

    /**
     * Received Unsubscribe Message Event
     *
     * @param msg Unsubscribe Message
     */
    void onUnsubscribe(Message<Object, Object> msg);

    /**
     * Received Publish Message Event
     *
     * @param msg Publish Message
     */
    void onPublish(Message<Object, Object> msg);

    /**
     * Received Disconnect Message Event
     *
     * @param msg Disconnect Message
     */
    void onDisconnect(Message<Void, Void> msg);
}
