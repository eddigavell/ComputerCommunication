package Lektion1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import org.w3c.dom.ls.LSOutput;

public class EchoServer {
    int portNumber = 8080;
    boolean run = true;
    boolean authorized;

    EchoServer() {
        try {
            ServerSocket serverSocket = new ServerSocket(portNumber); //TODO Skapa en server socket som lyssnar på en angiven port på server
            Socket clientSocket = serverSocket.accept(); //TODO acceptera anslutningar från en klient
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream())); //TODO läsa data från klienten
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            String inputLine;
            while (run) {
                out.println(">Username?: ");
                String username = in.readLine();
                out.println(">Password?: ");
                String password = in.readLine();
                authorized = Authorize("eg", "xxx", username, password);
                if (authorized) {
                    run = false;
                    out.println("Welcome to EchoServer");
                } else {
                    out.println("denied.");
                }
            }
            while (authorized) {
                inputLine = in.readLine();
                if (inputLine.equals("break")) {
                    in.close();
                    out.close();
                    clientSocket.close();
                    serverSocket.close();
                } else {
                    //System.out.println(inputLine);
                    out.println("Echo: " + inputLine); //TODO skriva samma data till klienten
                }
            }
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    boolean Authorize(String username, String password, String usernameFromUser, String passwordFromUser) {
        boolean flag = true;
        if (!username.equals(usernameFromUser)) {
            flag = false;
        }
        if (!password.equals(passwordFromUser)) {
            flag = false;
        }
        return flag;
    }

    public static void main(String[] args) {
        new EchoServer();
    }
}
