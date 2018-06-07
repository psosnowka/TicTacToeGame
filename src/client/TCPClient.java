package client;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class TCPClient {
    private int ip = 222222;
    private String host = "localhost";

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

    public static void main(String argv[]) throws Exception {
        Socket socket = new Socket("localhost", 22222);
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
//        DataInputStream dis = new DataInputStream(socket.getInputStream());
        Scanner scanner = new Scanner(System.in);
        String msg;
        while (true) {
            msg = in.readLine();
            System.out.println(msg);
            if(msg.equals("QUIT"))
                return;

        }
    }
}
