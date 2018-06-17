package server;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.time.LocalDate;
import java.time.LocalTime;

public class Player extends Thread {
    Socket socket;
    Player opponent;
    Game game;
    String mark;
    BufferedReader in;
    DataOutputStream output;
    DataInputStream input;
    PrintWriter out;

    public Player(Socket socket, Game game, String mark) {
        this.mark = mark;
        this.socket = socket;
        this.game = game;

        InetAddress addr = socket.getInetAddress();
        System.out.println("Connection made to : " + addr.getHostName() + " (" + addr.getHostAddress() + ")");
        try {
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            output = new DataOutputStream(socket.getOutputStream());
            input = new DataInputStream(socket.getInputStream());
//            out.println("CONNECT");
            sendRequest("CONNECT");
//            output.writeBytes("CONNECT");
//            output.writeInt(message.length); // write length of the message
//            output.write(message);
//            out.println("WAIT_FOR_OPPONENT");
//            output.writeBytes("WAIT_FOR_OPPONENT");
            sendRequest("WAIT_FOR_OPPONENT");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        try {
            String message = null;
//            out.println("START_GAME");
            sendRequest("START_GAME");
//            out.println("MARK " + mark);
            sendRequest("MARK " + mark);
            while (true) {
                message = receiveRequest();
                log(message);
                if (message.startsWith("MOVE")) {
                    int i = Integer.parseInt(message.substring(5));
                    if (i < 0 || i > 8) {
//                        out.println("INVALID MOVE");
                        sendRequest("INVALID MOVE");
                    } else if (!game.setMove(i, this)) {
//                        out.println("BAD MOVE");
                        sendRequest("BAD MOVE");
                    } else {
//                        out.println("ACCEPT");
                        sendRequest("ACCEPT");
                        if (game.isWin()) {
                            win();
                        } else if (game.isFiled()) {
                            draw();
                        }
                    }

                }
            }
        } catch (IOException e) {
//            opponent.sendMessage("OPONENT_DISCONECT");
            try {
                opponent.sendRequest("OPONENT_DISCONECT");
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            log("PLAYER_DISCONNET");
        }
    }


    public void sendMessage(String message) {
        out.println(message);
    }

    public void oponentMove(int i) throws IOException {
//        out.println("OPPONENT_MOVE " + i);
        sendRequest("OPPONENT_MOVE " + i);
    }

    public void log(String message) {
        System.out.println(LocalTime.now() + " - " + message);
    }

    public void setOpponent(Player opponent) {
        this.opponent = opponent;
    }

    public void win() throws IOException {
//        out.println("WIN");
        sendRequest("WIN");
        opponent.loose();
    }

    public void draw() throws IOException {
//        out.println("DRAW");
        sendRequest("DRAW");
//        opponent.out.println("DRAW");
        opponent.sendRequest("DRAW");
    }

    public void loose() throws IOException {
//        out.println("LOOSE");
        sendRequest("LOOSE");
    }

    public void youStart() throws IOException {
//        out.println("YOU_START");
        sendRequest("YOU_START");
    }

    public void opponentStart() throws IOException {
//        out.println("OPPONENT_START");
        sendRequest("OPPONENT_START");
    }

    public void sendRequest(String msg) throws IOException {
        byte[] bytes = ReqResConverter.StringToByte(msg);
        output.writeInt(bytes.length);
        output.write(bytes);
    }

    public String receiveRequest() throws IOException {
        int lenght = input.readInt();
        byte[] responsebyte = new byte[lenght];
        input.readFully(responsebyte, 0, responsebyte.length);
        return ReqResConverter.ByteToString(responsebyte);
    }

}
