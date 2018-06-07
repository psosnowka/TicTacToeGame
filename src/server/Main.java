package server;

public class Main {
    public static void main(String argv[]) throws Exception {
        TCPServer tcpServer = new TCPServer(22222);
        tcpServer.runServer();
    }
}

