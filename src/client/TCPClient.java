package client;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class TCPClient {


    public static String receve_all(BufferedReader in) throws IOException {
        boolean loop = true;
        StringBuilder sb = new StringBuilder(8096);
        while (loop) {
            if (in.ready()) {
                int i = 0;
                while (i != -1) {
                    i = in.read();
                    sb.append((char) i);
                }
                loop = false;
            }
        }
        return sb.toString();
    }

    public static void main(String argv[]) {
        int port = 222222;
        String host = "localhost";
        Client client = new Client(host, port);
        try {
            client.onClientStart();
        } catch (IOException e) {
            System.out.println("SERVER NOT RESPONSE AT PORT: " + port+ " AND HOST: "+ host);
        }

    }
}
