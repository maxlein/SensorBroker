package home.mqtt;

import home.MqttHumiditySensorHandler;
import home.MqttSensorHandler;
import net.sf.xenqtt.client.*;
import net.sf.xenqtt.message.ConnectReturnCode;
import net.sf.xenqtt.message.QoS;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicReference;

@Component
@Profile({"mqtt", "all"})
public class MqttConnection implements CommandLineRunner {
    private static final Logger LOG = LoggerFactory.getLogger(MqttConnection.class);
    public static final String TEMPERATURE_TYPE = "temperature";
    public static final String HUMIDITY_TYPE = "humidity";

    final AtomicReference<ConnectReturnCode> connectReturnCode = new AtomicReference<ConnectReturnCode>();
    final CountDownLatch connectLatch = new CountDownLatch(1);

    private static MqttSensorHandler sensorHandlerStatic;
    private static MqttHumiditySensorHandler humiditySensorHandlerStatic;

    @Autowired
    private MqttSensorHandler sensorHandler;

    @Autowired
    private MqttHumiditySensorHandler humiditySensorHandler;

    @PostConstruct
    public void initStaticDao () {
        sensorHandlerStatic = this.sensorHandler;
        humiditySensorHandlerStatic = this.humiditySensorHandler;
    }

    @Value("${xenqtt.connection.url}")
    private String url;

    @Value("${xenqtt.connection.port}")
    private int port;

    private String clientId;

    @Value("${xenqtt.connection.cleanSession}")
    private boolean cleanSession;

    @Value("${xenqtt.subscriptions.list}")
    private String subscriptions;

    @Value("${xenqtt.subscriptions.QoS}")
    private String qos;

    @Value("${xenqtt.connection.username")
    private String username;

    @Value("${xenqtt.connection.password")
    private String password;

    @Override
    public void run(String... arg0) throws Exception {
        // Build your client. This client is an asynchronous one so all interaction with the broker will be non-blocking.
        AsyncMqttClient client = new AsyncMqttClient(url + ":" + port, new Subscriber(connectLatch, connectReturnCode), 5);
        try {
            clientId = InetAddress.getLocalHost().getHostName();

            // Connect to the broker with a specific client ID. Only if the broker accepted the connection shall we proceed.
            LOG.info("clientId: " + clientId);
            LOG.info("cleanSession: " + cleanSession);


            client.connect(clientId, cleanSession, username, password);

            connectLatch.await();

            ConnectReturnCode returnCode = connectReturnCode.get();
            if (returnCode == null || returnCode != ConnectReturnCode.ACCEPTED) {
                LOG.error("Unable to connect to the MQTT broker. Reason: " + returnCode);
                return;
            }
            LOG.info("connected");

            //set up subscriptions
            List<Subscription> subscriptions = getSubscriptions();
            if (subscriptions.size() > 0) {
                client.subscribe(subscriptions);
            }

        } catch (Exception ex) {
            LOG.error("An unexpected exception has occurred.", ex);
            if (!client.isClosed()) {
                client.disconnect();
            }
        } finally {
//			if (!client.isClosed()) {
//				client.disconnect();
//			}
        }
    }

    private List<Subscription> getSubscriptions() {
        List<Subscription> subscriptionsToCreate = new ArrayList<Subscription>();
        String[] subscriptionArray = subscriptions.split(",");
        String[] qosArray = qos.split(",");
        int[] qos = new int[qosArray.length];
        for (int i = 0; i < qosArray.length; i++) {
            try {
                qos[i] = Integer.parseInt(qosArray[i]);
            } catch (NumberFormatException nfe) {}
        }

        if (subscriptionArray.length != qos.length) {
            LOG.error("could not create subscriptions, qos array length: " + qos.length + " != topic array length: " + subscriptionArray.length);
        } else {
            for (int i = 0; i < subscriptionArray.length; i++) {
                System.out.println();
                subscriptionsToCreate.add(new Subscription(subscriptionArray[i], QoS.lookup(qos[i])));
            }
        }

        return subscriptionsToCreate;
    }

    private static class Subscriber implements AsyncClientListener{
        private static final Logger LOG = LoggerFactory.getLogger(Subscriber.class);
        private CountDownLatch latch;
        private AtomicReference<ConnectReturnCode> connectReturnCode;

        public Subscriber(CountDownLatch latch, AtomicReference<ConnectReturnCode> connectReturnCode) {
            this.latch = latch;
            this.connectReturnCode = connectReturnCode;
        }

        @Override
        public void disconnected(MqttClient arg0, Throwable cause, boolean reconnecting) {
            if (cause != null) {
                LOG.error("Disconnected from the broker due to an exception.", cause);
            } else {
                LOG.info("Disconnecting from the broker.");
            }

            if (reconnecting) {
                LOG.info("Attempting to reconnect to the broker.");
            }
        }

        @Override
        public void publishReceived(MqttClient arg0, PublishMessage arg1) {
            // TODO Auto-generated method stub
            LOG.info("received arg1: " + arg1.getPayloadString() + ", topic " + arg1.getTopic());

            String topic = arg1.getTopic();
            String[] substrings = topic.split("/");
            if (substrings.length != 3){
                LOG.error("substrings size != 3, size %d, %s", substrings.length, substrings);
                return;
            }
            String type = substrings[2];
            String location = substrings[1];
            String data = arg1.getPayloadString();

            switch (type) {
                case HUMIDITY_TYPE:
                    humiditySensorHandlerStatic.addHumidityReading(location, data);
                    break;
                case TEMPERATURE_TYPE:
                    sensorHandlerStatic.addTemperatureReading(location, data);
                    break;
            }
        }

        @Override
        public void connected(MqttClient arg0, ConnectReturnCode arg1) {
            // TODO Auto-generated method stub
            LOG.info("connected");
            connectReturnCode.set(arg1);
            latch.countDown();
        }

        @Override
        public void published(MqttClient arg0, PublishMessage arg1) {
            // TODO Auto-generated method stub
            LOG.info("published");
        }

        @Override
        public void subscribed(MqttClient arg0, Subscription[] arg1,
                               Subscription[] arg2, boolean arg3) {
            LOG.info("subscribed");

        }

        @Override
        public void unsubscribed(MqttClient arg0, String[] arg1) {
            LOG.info("unsubscribed");
        }

    }
}