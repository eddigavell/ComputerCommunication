package Lab2;

import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class ClientReadTopic {
    MqttClient mqttClient; //Client
    String subscribeTopic = "KYH/EG/sensor";
    String writeTopic = "KYH/EG/sensor/data";
    String clientId = "e-ClientReadTopic";

    ClientReadTopic() {
        try {
            MemoryPersistence persistence = new MemoryPersistence(); //Memory instance
            mqttClient = new MqttClient("tcp://broker.hivemq.com:1883", clientId, persistence); //Creates a new client
            MqttConnectOptions connOpts = new MqttConnectOptions(); //Sets connection options for the client
            connOpts.setCleanSession(true); //true if not needing a persistance, false if you do.
            System.out.println("Connecting to broker: " + mqttClient.getServerURI());
            mqttClient.connect(connOpts);
            System.out.println("Connected and listening to topic: " + subscribeTopic);
            System.out.println("Writes to topic: " + writeTopic);
            mqttClient.subscribe(subscribeTopic, new MqttPostPropertyMessageListener());
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    class MqttPostPropertyMessageListener implements IMqttMessageListener {
        @Override
        public void messageArrived(String topic, MqttMessage content) throws MqttException, IOException {
            int x = Integer.parseInt(content.toString().substring(13,15));
            System.out.println(x);
            System.out.println("Received - " + topic + ": " + content.toString());
            String s = "ctrl, ";
            if (x >= 22) {
                s = s + "-";
            } else {
                s = s + "+";
            }
            MqttMessage msg = new MqttMessage(s.getBytes(StandardCharsets.UTF_8));
            msg.setQos(2);
            mqttClient.publish(writeTopic, msg);
            System.out.println("Sent - " + writeTopic + ": " + s);
        }
    }

    public static void main(String[] args) {
        new ClientReadTopic();
    }
}
