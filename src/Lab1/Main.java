package Lab1;

import java.io.IOException;
import java.net.*;

public class Main {
    String[] server = new String[8];

    Main() {
        addServers();
        SNTPMessage message = new SNTPMessage();
        /*
        SNTPMessage response = null;

        try {
            response = connectAndSend(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //response.printDataToConsole();
        System.out.println();
        if (response != null) {
            System.out.println(response.toString());
            System.out.println("----------------------------------------------------");
            calculateRoundTripTimeAndPrintIt(response);
        } else {
            System.out.println("Response message is empty...");
        }
        */
        try {
            connectToAllServersAndPrintToConsole(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void addServers() {
        server[0] = "gbg1.ntp.se";
        server[1] = "gbg2.ntp.se";
        server[2] = "mmo1.ntp.se";
        server[3] = "mmo2.ntp.se";
        server[4] = "sth1.ntp.se";
        server[5] = "sth2.ntp.se";
        server[6] = "svl1.ntp.se";
        server[7] = "svl2.ntp.se";
    }

    private static void calculateRoundTripTimeAndPrintIt(SNTPMessage message) {
        double t1 = message.getOriginateTimeStamp();
        double t2 = message.getReceiveTimeStamp();
        double t3 = message.getTransmitTimeStamp();
        double t4 = message.getReferenceTimeStamp();
        double delay = (t4 - t1) - (t3 - t2);
        double offset = ((t2 - t1) + (t3 - t4)) / 2;
        System.out.println("Delay: " + delay);
        System.out.println("Server offset: " + offset + " sekunder");
    }

    SNTPMessage connectAndSend(SNTPMessage msgToSend) throws IOException {
        SNTPMessage response;
        DatagramSocket socket = new DatagramSocket();
        int i = 0;
        byte[] buf = msgToSend.toByteArray();
        socket.setSoTimeout(1); //5ms to recieve packet
        while (true) {
            InetAddress ip = InetAddress.getByName(server[i++]);
            DatagramPacket packet = new DatagramPacket(buf, buf.length, ip, 123);
            socket.send(packet); //Sending packet
            System.out.println("Sent request to server: " + ip.getHostName() + ":123");
            try {
                socket.receive(packet); //Receives packet
            } catch (SocketTimeoutException e) {
                System.out.println("Couldn't get a response in time from server, trying another...");
            }
            response = new SNTPMessage(packet.getData());
            if (response.getMode() == 4) {
                System.out.println("Received message from server: " + packet.getAddress().getHostName() + ":" + packet.getPort());
                break;
            } else if (i == server.length) {
                i = 0;
            }
        }
        System.out.println("Connection closed to server");
        socket.close();
        return response;
    }

    void connectToAllServersAndPrintToConsole(SNTPMessage msgToSend) throws IOException {
        for (String s : server) {
        DatagramSocket socket = new DatagramSocket();
        byte[] buf = msgToSend.toByteArray();
        //socket.setSoTimeout(10);

            SNTPMessage response;
            InetAddress ip = InetAddress.getByName(s);
            DatagramPacket packet = new DatagramPacket(buf, buf.length, ip, 123);
            socket.send(packet); //Sending packet
            System.out.println("Sent request to server: " + ip.getHostName() + ":123");
            try {
                socket.receive(packet); //Receives packet
            } catch (SocketTimeoutException e) {
                System.out.println("No response, trying next server");
            }
            response = new SNTPMessage(packet.getData());
            System.out.println("Connection closed to server");
            System.out.println(response.toString());
            calculateRoundTripTimeAndPrintIt(response);
            System.out.println();
            if (response.getMode() != 4) {
                socket.close();
            }

        }
    }

    public static void main(String[] args) {
        new Main();
    }
}
