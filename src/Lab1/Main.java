package Lab1;

import java.io.IOException;
import java.net.*;

public class Main {

    public static void main(String[] args) {
        String[] server = new String[8];
        //server[0] = "gbg1.ntp.se";
        server[0] = "google.se";
        server[1] = "gbg2.ntp.se";
        server[2] = "mmo1.ntp.se";
        server[3] = "mmo2.ntp.se";
        server[4] = "sth1.ntp.se";
        server[5] = "sth2.ntp.se";
        server[6] = "svl1.ntp.se";
        server[7] = "svl2.ntp.se";



        //TODO utöka koden så att den försöker ansluta till en annan server om anslutningen misslyckas

        try {
            DatagramSocket socket = new DatagramSocket();
            boolean run = true;
            int i = 0;
            SNTPMessage message = new SNTPMessage();
            byte[] buf = message.toByteArray();
            SNTPMessage response = new SNTPMessage(buf);
            socket.setSoTimeout(5); //5ms to recieve packet
            while (run) {
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
                    System.out.println("Recieved message from server: " + packet.getAddress().getHostName() + ":" + packet.getPort());
                    run = false;
                } else if (i == server.length) {
                    i = 0;
                }
            }

            socket.close();
            System.out.println("Connection closed to server");

            response.printDataToConsole();

            calculateRoundTripTimeAndPrintIt(response);

        } catch (IOException e) {
            e.printStackTrace();
        }

/*
        try {
            int udpPort = 123;
            DatagramSocket socket = new DatagramSocket();
            InetAddress ip = InetAddress.getByName("gbg1.ntp.se");
            SNTPMessage message = new SNTPMessage();
            byte[] buf = message.toByteArray();

            DatagramPacket packet = new DatagramPacket(buf, buf.length, ip, udpPort);
            socket.send(packet);
            System.out.println("Sent request to server: " + ip.getHostName() + ":" + udpPort);

            socket.receive(packet);
            System.out.println("Recieved message from server");
            SNTPMessage response = new SNTPMessage(packet.getData());

            socket.close();
            System.out.println("Connection closed to server");

            response.printDataToConsole();

            calculateRoundTripTimeAndPrintIt(response);
        } catch (IOException e) {
            e.printStackTrace();
        }

 */
    }

    private static void calculateRoundTripTimeAndPrintIt(SNTPMessage message) {
        /*
        When the server reply is received, the client determines a
        Destination Timestamp variable as the time of arrival according to
        its clock in NTP timestamp format.  The following table summarizes
        the four timestamps.

        Timestamp Name          ID   When Generated
        ------------------------------------------------------------
        Originate Timestamp     T1   time request sent by client
        Receive Timestamp       T2   time request received by server
        Transmit Timestamp      T3   time reply sent by server
        Destination Timestamp   T4   time reply received by client

        The roundtrip delay d and system clock offset t are defined as:

        d = (T4 - T1) - (T3 - T2)     t = ((T2 - T1) + (T3 - T4)) / 2.
        */
        double t1 = message.getOriginateTimeStamp();
        double t2 = message.getReceiveTimeStamp();
        double t3 = message.getTransmitTimeStamp();
        double t4 = message.getReferenceTimeStamp();

        double delay = (t4 - t1) - (t3 - t2);
        double offset = ((t2 - t1) + (t3 - t4)) / 2;

        //System.out.println("Delay: " + delay);
        System.out.println("Server offset: " + offset + " sekunder");
    }
}
