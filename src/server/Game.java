package server;

public class Game {
    private Player player1;
    private Player player2;

    String msg = "wiadomosc";


    public void setPlayer1(Player player1) {
        this.player1 = player1;
    }

    public void setPlayer2(Player player2) {
        this.player2 = player2;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public boolean setMessageToOponent(String msg, Player player) {
        player.sendMesageToClient(msg);
        return true;
    }
}
