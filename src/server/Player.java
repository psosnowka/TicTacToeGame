package server;

import java.io.*;
import java.net.Socket;

public class Player extends Thread {
    Socket socket;
    Player opponent;
    Game game;
    BufferedReader in;
    PrintWriter out;

    public Player(Socket socket, Game game) {
        this.socket = socket;
        this.game = game;
        try {
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out.println("Polaczono");
            out.println("Czekaj na przeciwnika");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        try {
            String message = null;
            out.println("Znaleziono Przeciwnika");
            out.println("Gra Rozpoczeta");
            while (!((message = in.readLine()).equals("QUIT"))) {
                game.setMessageToOponent(message, opponent);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMesageToClient(String message) {
        out.println(message);
    }

    public void setOpponent(Player opponent) {
        this.opponent = opponent;
    }
}
