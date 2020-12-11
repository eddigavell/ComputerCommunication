package Lab2;

import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class ReadDataTopic {
        String subscribeTopic = "KYH/EG/sensor/data";
        String mqttBroker = "tcp://broker.hivemq.com:1883";
        String clientId = "e-readerData";
        MqttClient mqttClient; //Client

        ReadDataTopic() {
            try {
                MemoryPersistence persistence = new MemoryPersistence(); //Memory instance
                mqttClient = new MqttClient(mqttBroker, clientId, persistence); //Creates a new client
                MqttConnectOptions connOpts = new MqttConnectOptions(); //Sets connection options for the client
                connOpts.setCleanSession(true); //true if not needing a persistance, false if you do.
                System.out.println("Connecting to broker: " + mqttBroker);
                mqttClient.connect(connOpts);
                System.out.println("Connected and listening to topic: " + subscribeTopic);
                mqttClient.subscribe(subscribeTopic, new MqttPostPropertyMessageListener());
            } catch (MqttException e) {
                e.printStackTrace();
            }
        }

        class MqttPostPropertyMessageListener implements IMqttMessageListener {
            @Override
            public void messageArrived(String topic, MqttMessage content) {
                String receivedContent = topic + ", " + content.toString();
                System.out.println(receivedContent);
            }
        }

        public static void main(String[] args) {
            new ReadDataTopic();
        }
}
