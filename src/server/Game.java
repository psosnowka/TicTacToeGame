package server;

public class Game {
    private Player player1;
    private Player player2;
    private Player currentPlayer;
    private static Integer GAME_ID = 0;

    private Player[] gamePool = {
            null, null, null,
            null, null, null,
            null, null, null};

    public Game() {
        GAME_ID++;
    }

    public Integer getGameId() {
        return GAME_ID;
    }

    public void setPlayer1(Player player1) {
        this.player1 = player1;
        currentPlayer = player1;

    }

    public void setPlayer2(Player player2) {
        this.player2 = player2;
    }

    public synchronized boolean setMove(int move, Player player) {
        if (player == currentPlayer && gamePool[move] == null) {
            gamePool[move] = player;
            player.opponent.oponentMove(move);
            currentPlayer = currentPlayer.opponent;
            return true;
        }
        return false;
    }

    public boolean isWin() {
        return
                (gamePool[0] != null && gamePool[0] == gamePool[1] && gamePool[0] == gamePool[2])
                        || (gamePool[3] != null && gamePool[3] == gamePool[4] && gamePool[3] == gamePool[5])
                        || (gamePool[6] != null && gamePool[6] == gamePool[7] && gamePool[6] == gamePool[8])
                        || (gamePool[0] != null && gamePool[0] == gamePool[3] && gamePool[0] == gamePool[6])
                        || (gamePool[1] != null && gamePool[1] == gamePool[4] && gamePool[1] == gamePool[7])
                        || (gamePool[2] != null && gamePool[2] == gamePool[5] && gamePool[2] == gamePool[8])
                        || (gamePool[0] != null && gamePool[0] == gamePool[4] && gamePool[0] == gamePool[8])
                        || (gamePool[2] != null && gamePool[2] == gamePool[4] && gamePool[2] == gamePool[6]);
    }

    public boolean isFiled() {
        for (int i = 0; i < 9; i++) {
            if (gamePool[i] == null)
                return false;
        }
        return true;
    }

    public void draw() {
        player1.draw();
        player2.draw();
    }

    public void startGame() {
        player1.youStart();
        player2.opponentStart();
    }

    public void win(Player player) {
        player.win();
        player.opponent.loose();
    }
}
