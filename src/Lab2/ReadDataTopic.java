package Lab2;

import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;

public class ReadDataTopic {
    MqttClient mqttClient; //Client
    String subscribeTopic = "KYH/EG/#";
    String mqttBroker = "tcp://broker.hivemq.com:1883";
    String clientId = "e-readerData";

    ReadDataTopic() {
        try {
            MemoryPersistence persistence = new MemoryPersistence(); //Memory instance
            mqttClient = new MqttClient(mqttBroker, clientId, persistence); //Creates a new client
            MqttConnectOptions connOpts = new MqttConnectOptions(); //Sets connection options for the client
            connOpts.setCleanSession(true); //true if not needing a persistance, false if you do.
            System.out.println("Connecting to broker: " + mqttBroker);
            mqttClient.connect(connOpts);
            System.out.println("Connected and listening to topic: " + subscribeTopic);
            mqttClient.subscribe(subscribeTopic, 2,new MqttPostPropertyMessageListener());
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    class MqttPostPropertyMessageListener implements IMqttMessageListener {
        @Override
        public void messageArrived(String topic, MqttMessage content) throws IOException {
            Date date = new Date();
            String receivedContent = topic + ", " + content.toString();
            System.out.println(date + ": " + receivedContent);
            FileWriter fw = new FileWriter("src/Lab2/log.txt", true);
            fw.write(date + ", " + receivedContent + "\n");
            fw.close();
        }
    }

    public static void main(String[] args) {
        new ReadDataTopic();
    }
}
