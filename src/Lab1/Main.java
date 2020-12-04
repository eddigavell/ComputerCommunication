package Lab1;

import javax.xml.crypto.Data;
import java.io.IOException;
import java.net.*;

public class Main {

    public static void main(String[] args) {
        /*
        byte [] buf = {  36,   1,  0, -25,
                          0,   0,  0,   0,
                          0,   0,  0,   2,
                         80,  80, 83,   0,
                        -29, 116,  5,  61,  0,  0,    0,   0,
                        -29, 116,  5,  59, 14, 86,    0,   0,
                        -29, 116,  5,  62,  0, 47, -121, -38,
                        -29, 116,  5,  62,  0, 47, -113,  -1};
        SNTPMessage msg = new SNTPMessage(buf);
         */

        /* NTP servrar
        Göteborg:
        gbg1.ntp.se
        gbg2.ntp.se
        Malmö:
        mmo1.ntp.se
        mmo2.ntp.se
        Stockholm:
        sth1.ntp.se
        sth2.ntp.se
        Sundsvall:
        svl1.ntp.se
        svl2.ntp.se
         */

        try {
            int udpPort = 123;
            DatagramSocket socket = new DatagramSocket();
            InetAddress ip = InetAddress.getByName("gbg1.ntp.se");
            SNTPMessage message = new SNTPMessage();
            byte[] buf = message.toByteArray();

            System.out.println();
            DatagramPacket packet = new DatagramPacket(buf, buf.length, ip, udpPort);
            socket.send(packet);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
