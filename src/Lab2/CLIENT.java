package Lab2;

import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.util.Scanner;

public class CLIENT {
    String topic = "KYH20";
    String broker = "tcp://broker.hivemq.com:1883";
    String clientId = "E-test123";

    CLIENT() {
        MemoryPersistence persistence = new MemoryPersistence();
        try {
            MqttClient sampleClient = new MqttClient(broker, clientId, persistence);
            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setCleanSession(true);
            System.out.println("Connecting to broker: " + broker);
            sampleClient.connect(connOpts);
            System.out.println("Connected");
            Scanner sc = new Scanner(System.in);
            while(sampleClient.isConnected()) {
                // The topic to which Paho MQTT subscribes for messages
                sampleClient.subscribe(topic, new MqttPostPropertyMessageListener());
                if (sc.nextLine().equals("exit")) {
                    sampleClient.disconnect();
                    System.out.println("Disconnected");
                    System.exit(0);
                }
            }
        } catch (MqttException me) {
            System.out.println("reason " + me.getReasonCode());
            System.out.println("msg " + me.getMessage());
            System.out.println("loc " + me.getLocalizedMessage());
            System.out.println("cause " + me.getCause());
            System.out.println("excep " + me);
            me.printStackTrace();
        }
    }

    class MqttPostPropertyMessageListener implements IMqttMessageListener {
        @Override
        public void messageArrived(String var1, MqttMessage var2) throws Exception {
            System.out.println(var1 + ": " + var2.toString());
        }
    }

    public static void main(String[] args) {
        new CLIENT();
    }
}