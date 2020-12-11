package Lab2;

import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Date;

public class ClientReadTopic {
    String subscribeTopic = "KYH/EG/sensor";
    String writeTopic = "KYH/EG/sensor/data";
    String mqttBroker = "tcp://broker.hivemq.com:1883";
    String clientId = "e-ClientReadTopic";
    MqttClient mqttClient; //Client

    ClientReadTopic() {
        try {
            MemoryPersistence persistence = new MemoryPersistence(); //Memory instance
            mqttClient = new MqttClient(mqttBroker, clientId, persistence); //Creates a new client
            MqttConnectOptions connOpts = new MqttConnectOptions(); //Sets connection options for the client
            connOpts.setCleanSession(true); //true if not needing a persistance, false if you do.
            System.out.println("Connecting to broker: " + mqttBroker);
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
            String text = content.toString();
            saveToLog( "temperature, " + text);
            int x = Integer.parseInt(text.substring(0,2));
            System.out.println(text);
            String s = "ctrl, ";
            if (x >= 22) {
                s = s + "-";
            } else {
                s = s + "+";
            }
            saveToLog(s);
            MqttMessage msg = new MqttMessage(s.getBytes(StandardCharsets.UTF_8));
            msg.setQos(2);
            mqttClient.publish(writeTopic, msg);
        }
    }

    void saveToLog(String content) throws IOException {
        Date date = new Date();
        FileWriter fw = new FileWriter("src/Lab2/log.txt", true);
        fw.write(date + ", " + content + "\n");
        fw.close();
    }


    public static void main(String[] args) {
        new ClientReadTopic();
    }

}
