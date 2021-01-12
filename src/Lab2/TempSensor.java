package Lab2;

import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.nio.charset.StandardCharsets;
import java.util.Timer;
import java.util.TimerTask;

public class TempSensor {
    String topic;
    String mqttBroker;
    String clientId;
    MqttClient mqttClient; //Client
    Timer timer;
    int secondsForTimer = 10; //Timer delay
    long delay = secondsForTimer * 1000L; //timer delay in ms to get s.

    TempSensor() {
            connectToBroker();
            timer = new Timer();
            timer.schedule(new TimerToDo(), delay);
    }

    void connectToBroker() {
        try {
        topic = "KYH/EG/sensor";
        mqttBroker = "tcp://broker.hivemq.com:1883";
        clientId = "e-TempSensor";
        MemoryPersistence persistence = new MemoryPersistence(); //Memory instance
        mqttClient = new MqttClient(mqttBroker, clientId, persistence); //Creates a new client
        MqttConnectOptions connOpts = new MqttConnectOptions(); //Sets connection options for the client
        connOpts.setCleanSession(true); //true if not needing a persistance, false if you do.
        System.out.println("Connecting to broker: " + mqttBroker);
        mqttClient.connect(connOpts);
        System.out.println("Connected and writing to topic: " + topic);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    class TimerToDo extends TimerTask {
        @Override
        public void run() {
            try {
                String temp = "temperature, " + getStringDegree() + "°C";
                byte[] asd = temp.getBytes(StandardCharsets.UTF_8);
                mqttClient.publish(topic, asd,2, false);
                System.out.println("Sent temperature: " + temp);
            } catch (MqttException e) {
                e.printStackTrace();
            }
            timer.cancel();
            timer = new Timer();
            timer.schedule(new TimerToDo(), delay);
            System.out.println("Created new timer");
        }
    }

    String getStringDegree() {
        int degree = (int) (Math.random() * 10) + 15;
        return String.valueOf(degree);
    }

    public static void main(String[] args) {
        new TempSensor();
    }
}
