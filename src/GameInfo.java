import java.io.Serializable;
import java.util.ArrayList;

public class GameInfo implements Serializable {
    private int gameAreaX;
    private int gameAreaY;
    private int maxPoint;
    private int totalGame;

    private ArrayList<Integer> unavailableTilesPositions;
    private ArrayList<Integer> x2TilesPositions;
    private ArrayList<Integer> x3TilesPositions;

    public GameInfo() {
        this.gameAreaX = 0;
        this.gameAreaY = 0;
        this.maxPoint = 0;
        this.totalGame = 0;
        this.unavailableTilesPositions = new ArrayList<>();
        this.x2TilesPositions = new ArrayList<>();
        this.x3TilesPositions = new ArrayList<>();
    }

    public GameInfo(int gameAreaX, int gameAreaY, int maxPoint, int totalGame, ArrayList<Integer> unavailableTilesPositions, ArrayList<Integer> x2TilesPositions, ArrayList<Integer> x3TilesPositions) {
        this.gameAreaX = gameAreaX;
        this.gameAreaY = gameAreaY;
        this.maxPoint = maxPoint;
        this.totalGame = totalGame;
        this.unavailableTilesPositions = unavailableTilesPositions;
        this.x2TilesPositions = x2TilesPositions;
        this.x3TilesPositions = x3TilesPositions;
    }

    public String printGameInfo() {
        return gameAreaX + ";" + gameAreaY + ";" + maxPoint + ";" + totalGame + ";"
                + arrayToString(unavailableTilesPositions) + ";" + arrayToString(x2TilesPositions) + ";"
                + arrayToString(x3TilesPositions);
    }

    private String arrayToString(ArrayList<Integer> arrayList) {
        StringBuilder s = new StringBuilder();
        for (Integer i : arrayList)
            s.append(i).append("#");
        return s.toString();
    }

    public int getGameAreaX() {
        return gameAreaX;
    }

    public void setGameAreaX(int gameAreaX) {
        this.gameAreaX = gameAreaX;
    }

    public int getGameAreaY() {
        return gameAreaY;
    }

    public void setGameAreaY(int gameAreaY) {
        this.gameAreaY = gameAreaY;
    }

    public int getMaxPoint() {
        return maxPoint;
    }

    public void setMaxPoint(int maxPoint) {
        this.maxPoint = maxPoint;
    }

    public int getTotalGame() {
        return totalGame;
    }

    public void setTotalGame(int totalGame) {
        this.totalGame = totalGame;
    }

    public ArrayList<Integer> getUnavailableTilesPositions() {
        return unavailableTilesPositions;
    }

    public void setUnavailableTilesPositions(ArrayList<Integer> unavailableTilesPositions) {
        this.unavailableTilesPositions = unavailableTilesPositions;
    }

    public ArrayList<Integer> getX2TilesPositions() {
        return x2TilesPositions;
    }

    public void setX2TilesPositions(ArrayList<Integer> x2TilesPositions) {
        this.x2TilesPositions = x2TilesPositions;
    }

    public ArrayList<Integer> getX3TilesPositions() {
        return x3TilesPositions;
    }

    public void setX3TilesPositions(ArrayList<Integer> x3TilesPositions) {
        this.x3TilesPositions = x3TilesPositions;
    }
}
