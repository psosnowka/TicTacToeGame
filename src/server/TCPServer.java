package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Set;

public class TCPServer {
    private int PORT;

    public TCPServer(int port) {
        this.PORT = port;
    }

    public void runServer() throws IOException {
        ServerSocket serverSocket = new ServerSocket(PORT);
        System.out.println("Server up....");
        while (true) {
            Game game = new Game();

            Player first = new Player(serverSocket.accept(), game, "X", game.getGameId());
            game.setPlayer1(first);

            Player second = new Player(serverSocket.accept(), game, "O", game.getGameId());
            game.setPlayer2(second);

            first.setOpponent(second);
            second.setOpponent(first);

            first.start();
            second.start();
            game.startGame();

        }
    }
}
