import java.util.ArrayList;

public class Game {

    private GameInfo gameInfo;
    private boolean isGameStarted;

    private PlayerHandler turn;

    private String currentClient;

    private ArrayList<ArrayList<Tile>> tileArrayLists;


    public Game() {
        isGameStarted = false;
        tileArrayLists = new ArrayList<>();
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

    public ArrayList<ArrayList<Tile>> getTileArrayLists() {
        return tileArrayLists;
    }

    public void setTileArrayLists(ArrayList<ArrayList<Tile>> tileArrayLists) {
        this.tileArrayLists = tileArrayLists;
    }
}
