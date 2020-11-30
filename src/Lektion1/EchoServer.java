package Lektion1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import org.w3c.dom.ls.LSOutput;

public class EchoServer {
    public static void main(String[] args) {
        int portNumber = 8080;
        try {
            ServerSocket serverSocket = new ServerSocket(portNumber); //TODO Skapa en server socket som lyssnar på en angiven port på server
            Socket clientSocket = serverSocket.accept(); //TODO acceptera anslutningar från en klient
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream())); //TODO läsa data från klienten
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            String inputLine;
            while((inputLine = in.readLine()) != null) {
                if (inputLine.equals("break")) {
                    break;
                }
                System.out.println(inputLine);
                out.println("Echo: " + inputLine); //TODO skriva samma data till klienten
            }
            //out.close();
            //in.close();
            //clientSocket.close();
            //serverSocket.close();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }
}
