import java.util.ArrayList;

public class Game {

    private GameInfo gameInfo;

    private PlayerHandler turn;
    private PlayerHandler confirmPlayer;
    private PlayerHandler[] playerList;

    private String currentClient;

    private static ArrayList<ArrayList<Tile>> tileArrayLists;

    public Game() {
    }

    public GameInfo getGameInfo() {
        return gameInfo;
    }

    public void setGameInfo(GameInfo gameInfo) {
        this.gameInfo = gameInfo;
    }
}
