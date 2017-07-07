package spring.mqtt.nats.cluster;

import io.nats.client.Connection;
import io.nats.client.ConnectionFactory;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import spring.mqtt.api.cluster.Cluster;
import spring.mqtt.api.cluster.ClusterException;
import spring.mqtt.api.cluster.ClusterListener;
import spring.mqtt.api.cluster.ClusterListenerFactory;
import spring.mqtt.api.message.Message;
import spring.mqtt.api.util.JSONs;
import spring.mqtt.nats.bean.NatsBean;

import java.io.IOException;
import java.util.concurrent.TimeoutException;


/**
 * NATS Server based Cluster
 */

@Service
public class NATSClusterImpl implements Cluster {

    private static final Logger logger = LoggerFactory.getLogger(NATSClusterImpl.class);

    @Autowired
    private NatsBean natsBean;

    // topics


    // active connection to NATS Server
    private Connection conn;

    @SuppressWarnings("unchecked")
    @Override
    public void init( ClusterListenerFactory factory) throws ClusterException {
        logger.trace("Loading cluster configurations ...");
//        AbstractConfiguration config;
//
//        // Topics for broker and application
//        LISTENER_TOPIC = config.getString("listener.topic");
//        BROKER_TOPIC_PREFIX = config.getString("broker.topic.prefix");
//        APPLICATION_TOPIC = config.getString("application.topic");

        // Setup options to include all servers in the cluster
        ConnectionFactory cf = new ConnectionFactory();
        cf.setServers(natsBean.getNATS_SERVERS().split(","));

        // Set NATS configurations
        cf.setMaxReconnect( natsBean.getNats_maxReconnect());
        cf.setReconnectWait(natsBean.getNats_reconnectWait());
        cf.setReconnectBufSize(natsBean.getNats_reconnectBufSize());
        cf.setConnectionTimeout(natsBean.getNats_connectionTimeout());
        cf.setPingInterval(natsBean.getNats_pingInterval());
        cf.setMaxPingsOut(natsBean.getNats_maxPingsOut());
        cf.setMaxPendingMsgs(natsBean.getNats_maxPendingMsgs());
        cf.setMaxPendingBytes(natsBean.getNats_maxPendingMsgs());

        // Optionally disable randomization of the server pool
        cf.setNoRandomize(natsBean.isNats_noRandomize());

        logger.trace("Creating connection with NATS servers ...");

        // Create connection to the NATS servers
        try {
            this.conn = cf.createConnection();
        } catch (IOException | TimeoutException e) {
            throw new ClusterException(e);
        }

        String listener_topic = natsBean.getLISTENER_TOPIC();
        if (StringUtils.isNotBlank(listener_topic) && factory != null) {
            logger.trace("Subscribe to topic {} ...", listener_topic);

            this.conn.subscribeAsync(listener_topic, msg -> {
            try {
                logger.trace("Received message from NATS topic {}", msg.getSubject());

                // event listener
                ClusterListener listener = factory.newListener();

                // decode message
                Message m = JSONs.decodeMessage(msg.getData());

                // handle message
                if (m != null) {
                    logger.debug("Cluster received: Received {} message for client {}", m.fixedHeader().messageType(), m.additionalHeader().clientId());
                    switch (m.fixedHeader().messageType()) {
                        case CONNECT:
                            listener.onConnect(m);
                            break;
                        case SUBSCRIBE:
                            listener.onSubscribe(m);
                            break;
                        case UNSUBSCRIBE:
                            listener.onUnsubscribe(m);
                            break;
                        case PUBLISH:
                            listener.onPublish(m);
                            break;
                        case DISCONNECT:
                            listener.onDisconnect(m);
                            break;
                        default:
                            logger.warn("Cluster Error: Received message with unknown type {}", m.fixedHeader().messageType());
                    }
                }
            } catch (IOException e) {
                logger.warn("Cluster Error: Error when decoding or handling the message", e);
            }
        });
    }
    }

    @Override
    public void destroy() {
        logger.trace("Closing connection with NATS servers ...");

        if (this.conn != null) this.conn.close();
    }

    @Override
    public void sendToBroker(String brokerId, Message message) {
        String brokerTopic =natsBean.getBROKER_TOPIC_PREFIX() + "." + brokerId;
        try {
            this.conn.publish(brokerTopic, JSONs.Mapper.writeValueAsBytes(message));
        } catch (IOException e) {
            logger.warn("Cluster Error: Failed to send message {} to topic {}: ", message.fixedHeader().messageType(), brokerTopic, e);
        }
    }

    @Override
    public void sendToApplication(Message message) {
        String application_topic = natsBean.getAPPLICATION_TOPIC();
        try {
            this.conn.publish(application_topic, JSONs.Mapper.writeValueAsBytes(message));
        } catch (IOException e) {
            logger.warn("Cluster Error: Failed to send message {} to topic {}: ", message.fixedHeader().messageType(), application_topic, e);
        }
    }
}
