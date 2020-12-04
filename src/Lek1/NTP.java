package Lek1;

import java.io.IOException;
import java.net.*;

public class NTP {
    /* NTP servers in sweden
        https://www.ntp.se/

        The NTP service uses the following IP networks and AS number:
        IPv4 - 8 subnets: 194.58.200.0/24 ... 194.58.207.0/24
        IPv6 - 8 subnets: 2a01:3f7:0::/48 ... 2a01:3f7:7::/48
        AS: 57021

        Göteborg:
        gbg1.ntp.se
        gbg2.ntp.se
     */

    NTP() {
        try {
            connect();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    void connect() throws IOException {
        // 194.58.200.0/24
        InetAddress ntpServer = InetAddress.getByName("gbg1.ntp.se");
        int ntpServerPort = 24;
        DatagramSocket socket = new DatagramSocket();

        byte[] buf = new byte[256];
        DatagramPacket packet = new DatagramPacket(buf, buf.length, ntpServer, ntpServerPort);
        socket.send(packet);
        System.out.println("Package sent to: " + ntpServer.getHostName() + " (" + ntpServer.getHostAddress() + "):" + ntpServerPort);

        packet = new DatagramPacket(buf, buf.length);
        socket.receive(packet);
        String recieved = new String(packet.getData(), 0, packet.getLength());
        System.out.println("Time at the moment: " + recieved);
    }

    public static void main(String[] args) throws UnknownHostException {
        new NTP();
    }
}
