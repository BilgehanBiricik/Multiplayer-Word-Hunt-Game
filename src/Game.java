import java.util.ArrayList;

public class Game {

    private GameInfo gameInfo;

    private PlayerHandler turn;
    private PlayerHandler confirmPlayer;

    private String currentClient;

    private static ArrayList<ArrayList<Tile>> tileArrayLists;

    private boolean isGameStarted;

    public Game() {
        isGameStarted = false;
    }

    public GameInfo getGameInfo() {
        return gameInfo;
    }

    public void setGameInfo(GameInfo gameInfo) {
        this.gameInfo = gameInfo;
    }

    public boolean isGameStarted() {
        return isGameStarted;
    }

    public void setGameStarted(boolean gameStarted) {
        isGameStarted = gameStarted;
    }
}
