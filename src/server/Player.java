package server;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.time.LocalDate;
import java.time.LocalTime;

public class Player extends Thread {
    private int gameId;
    private Socket socket;
    Player opponent;
    private Game game;
    private String mark;
    private BufferedReader in;
    private PrintWriter out;
    private InetAddress inetAddress;

    public Player(Socket socket, Game game, String mark, int gameId) {
        this.gameId = gameId;
        this.mark = mark;
        this.socket = socket;
        inetAddress = socket.getInetAddress();
        this.game = game;
        try {
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out.println("CONNECT");
            out.println("WAIT_FOR_OPPONENT");
            log("Player " + inetAddress.getHostName() + " PORT: " + socket.getPort() + " is connected");
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

                log("Game_id: " + gameId + " - " + message);
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

                } else if (message.startsWith("QUIT")) {
                    log("PLAYER " + inetAddress.getHostName() + " PORT " + socket.getPort() + " DISCONNET");
                    finalize();
                } else {
                    out.println("BAD_REQUEST");
                }
            }
        } catch (IOException e) {
            opponent.sendMessage("OPONENT_DISCONECT");
            log("PLAYER " + inetAddress.getHostName() + " PORT " + socket.getPort() + " DISCONNET");
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }


    public void sendMessage(String message) {
        out.println(message);
    }

    public void oponentMove(int i) {
        out.println("OPPONENT_MOVE " + i);
    }

    public void log(String message) {
        System.out.println(LocalTime.now() + " " + message);
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
