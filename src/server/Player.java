package server;

import java.io.*;
import java.net.Socket;
import java.time.LocalDate;
import java.time.LocalTime;

public class Player extends Thread {
    Socket socket;
    Player opponent;
    Game game;
    String mark;
    BufferedReader in;
    PrintWriter out;


    public Player(Socket socket, Game game, String mark) {
        this.mark = mark;
        this.socket = socket;
        this.game = game;
        try {
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out.println("CONNECT");
            out.println("WAIT_FOR_OPPONENT");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        try {
            String message = null;
            out.println("START_GAME");
            out.println("MARK " + mark);
            while (!((message = in.readLine()).equals("QUIT"))) {

                log(message);
                if (message.startsWith("MOVE")) {
                    int i = Integer.parseInt(message.substring(5));
                    if (i < 0 || i > 8) {
                        out.println("INVALID MOVE");
                    } else if (!game.setMove(i, this)) {
                        out.println("BAD MOVE");
                    } else {
                        out.println("ACCEPT");
                        if (game.isWin()) {
                            win();
                        } else if (game.isFiled()) {
                            draw();
                        }
                    }

                }
            }
        } catch (IOException e) {
            opponent.sendMessage("OPONENT_DISCONECT");
            log("PLAYER_DISCONNET");
        }
    }


    public void sendMessage(String message) {
        out.println(message);
    }

    public void oponentMove(int i) {
        out.println("OPPONENT_MOVE " + i);
    }

    public void log(String message) {
        System.out.println(LocalTime.now() + " - " + message);
    }

    public void setOpponent(Player opponent) {
        this.opponent = opponent;
    }

    public void win() {
        out.println("WIN");
        opponent.loose();
    }

    public void draw() {
        out.println("DRAW");
        opponent.out.println("DRAW");
    }

    public void loose() {
        out.println("LOOSE");
    }

    public void youStart() {
        out.println("YOU_START");
    }

    public void opponentStart() {
        out.println("OPPONENT_START");
    }
}
