package server;

import java.io.Serializable;

public class GameInfo implements Serializable {
    private int gameAreaX;
    private int gameAreaY;
    private int maxPoint;
    private int totalGame;
    private int unavailableTiles;
    private int x2Tiles;
    private int x3Tiles;


    public GameInfo() {
        this.gameAreaX = 0;
        this.gameAreaY = 0;
        this.maxPoint = 0;
        this.totalGame = 0;
        this.unavailableTiles = 0;
        this.x2Tiles = 0;
        this.x3Tiles = 0;
    }

    public GameInfo(int gameAreaX, int gameAreaY, int maxPoint, int totalGame, int unavailableTiles, int x2Tiles, int x3Tiles) {
        this.gameAreaX = gameAreaX;
        this.gameAreaY = gameAreaY;
        this.maxPoint = maxPoint;
        this.totalGame = totalGame;
        this.unavailableTiles = unavailableTiles;
        this.x2Tiles = x2Tiles;
        this.x3Tiles = x3Tiles;
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

    public int getUnavailableTiles() {
        return unavailableTiles;
    }

    public void setUnavailableTiles(int unavailableTiles) {
        this.unavailableTiles = unavailableTiles;
    }

    public int getX2Tiles() {
        return x2Tiles;
    }

    public void setX2Tiles(int x2Tiles) {
        this.x2Tiles = x2Tiles;
    }

    public int getX3Tiles() {
        return x3Tiles;
    }

    public void setX3Tiles(int x3Tiles) {
        this.x3Tiles = x3Tiles;
    }
}
