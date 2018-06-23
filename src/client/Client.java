package client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Scanner;

public class Client {
    private String host;
    private int port;
    private boolean endGame = false;
    String mark;
    String opponentMark;
    PrintWriter out;

    private JFrame frame = new JFrame("Tic Tac Toe");
    private JLabel messageLabel = new JLabel("asdasdasd");

    private Square[] board = new Square[9];
    private Square currentSquare;

    public Client(String host, int ip) {
        this.host = host;
        this.port = ip;
    }

    public void onClientStart() throws IOException {
        connectToServer();
    }

    private void connectToServer() throws IOException {
        Socket socket = new Socket("localhost", 22222);
        out = new PrintWriter(socket.getOutputStream(), true);
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        setFrame(frame, out);
        receiveFromServer(in);

    }

    private void receiveFromServer(BufferedReader in) throws IOException {
        String msg;

        while (true) {
            msg = in.readLine();
            msg = decode(msg);
            log(msg);

            if (msg.endsWith("ACCEPT")) {
                messageLabel.setText("Ruch przeciwnika");
                currentSquare.setText(mark);
            } else if (msg.startsWith("OPPONENT_MOVE")) {
                int i = Integer.valueOf(msg.substring(14));
                setBoard(i, opponentMark);
                messageLabel.setText("Twoj ruch");
            } else if (msg.startsWith("OPONENT_DISCONECT")) {
                messageLabel.setText("Przeciwnik rozłączyl sie - Wygrales");
            } else if (msg.startsWith("MARK")) {
                mark = msg.substring(5);
                frame.setTitle("Znacznik - " + mark);
                if (mark.equals("X")) {
                    opponentMark = "O";
                } else {
                    opponentMark = "X";
                }
            } else if (msg.startsWith("WIN")) {
                endGame = true;
                messageLabel.setText("Wygrales");
            } else if (msg.startsWith("LOOSE")) {
                endGame = true;
                messageLabel.setText("Przegrales");
            } else if (msg.startsWith("DRAW")) {
                endGame = true;
                messageLabel.setText("Remis");
            } else if (msg.startsWith("WAIT_FOR_OPPONENT")) {
                messageLabel.setText("Czekaj na przeciwnika");
            } else if (msg.startsWith("YOU_START")) {
                messageLabel.setText("Ty zaczynasz");
            } else if (msg.startsWith("OPPONENT_START")) {
                messageLabel.setText("Przeciwnik zaczyna");
            } else if (msg.startsWith("INVALID_MESSAGE")) {
                messageLabel.setText("INVALID_MESSAGE");
            } else if (msg.equals("QUIT")) {
                return;
            }

        }
    }

    private void setFrame(JFrame frame, PrintWriter out) {
        messageLabel.setBackground(Color.lightGray);
        frame.getContentPane().add(messageLabel, "South");

        JPanel boardPanel = new JPanel();
        boardPanel.setBackground(Color.black);
        boardPanel.setLayout(new GridLayout(3, 3, 2, 2));
        for (int i = 0; i < board.length; i++) {
            final int j = i;
            board[i] = new Square();
            board[i].addMouseListener(new MouseAdapter() {
                public void mousePressed(MouseEvent e) {
                    currentSquare = board[j];
                    if (endGame)
                        return;
                    if (!currentSquare.isEmpty())
                        return;
//                    out.println("MOVE " + j);
                    sendToServer("MOVE " + j);


                }
            });
            boardPanel.add(board[i]);
        }
        frame.getContentPane().add(boardPanel, "Center");

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(240, 160);
        frame.setVisible(true);
        frame.setResizable(false);
    }


    public void setBoard(int i, String mark) {
        board[i].setText(mark);
    }

    public void log(String message) {
        System.out.println(LocalTime.now() + " - " + message);
    }


    public void sendToServer(String message) {
        out.println(message + "::");
    }

    public String decode(String message) {
        if (message.endsWith("::")) {
            return message.substring(0, message.length() - 2);
        }
        return "INVALID_MESSAGE";
    }

    static class Square extends JPanel {
        JLabel label = new JLabel((Icon) null);

        public Square() {
            setBackground(Color.white);
            add(label);
        }

        public void setText(String text) {
            label.setText(text);
        }

        public String getText() {
            return label.getText();
        }

        public boolean isEmpty() {
            return label.getText() == null ? true : false;
        }

        public void setIcon(Icon icon) {
            label.setIcon(icon);
        }
    }

}
