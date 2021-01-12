package Practices;

import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.nio.charset.StandardCharsets;

public class PersistentSession implements MqttCallback {
    MqttClient mqttClient; //Client
    MqttConnectOptions connOpts;
    String clientID = "e-Persistent";
    String subscribeTopic = "KYH/EG/#";
    String statusTopic = "KYH/EG/status";

    PersistentSession() throws MqttException {
        ConnectionOptions();
        ConnectToBroker();
        mqttClient.subscribe(subscribeTopic, 2, (topic, rec) -> System.out.println(topic + ", " + rec.toString()));

        System.out.println("Subscribed: " + subscribeTopic);
        String online = clientID + ": online";
        byte[] msg = online.getBytes(StandardCharsets.UTF_8);
        mqttClient.publish(statusTopic, msg, 2, true);
    }

    void ConnectionOptions() {
        connOpts = new MqttConnectOptions(); //Sets connection options for the client
        String exitMsg =  clientID + ": offline";
        byte[] asd = exitMsg.getBytes(StandardCharsets.UTF_8);
        connOpts.setWill(statusTopic, asd, 2, true);
        connOpts.setWill("asd", asd, 2, false);
        connOpts.setCleanSession(false); //false because of persistent session.
    }

    void ConnectToBroker() throws MqttException {
        mqttClient = new MqttClient("tcp://broker.hivemq.com:1883", clientID, new MemoryPersistence()); //Creates a new client
        System.out.println("Connecting to broker: " + mqttClient.getServerURI() + ", clientid: " + mqttClient.getClientId());
        mqttClient.connect(connOpts);
        mqttClient.setCallback(this);
        System.out.println("Connected?: " + mqttClient.isConnected() + ", Clean session?: " + connOpts.isCleanSession());
    }

    @Override
    public void connectionLost(Throwable throwable) {

    }

    @Override
    public void messageArrived(String s, MqttMessage mqttMessage) {
        System.out.println(s + ", " + mqttMessage.toString());
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

    }

    public static void main(String[] args) {
        try {
            new PersistentSession();
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }
}
