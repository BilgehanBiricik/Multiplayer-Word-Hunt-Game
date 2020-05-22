package utils.message;

import server.GameInfo;
import utils.tile.Tile;

import java.io.Serializable;
import java.util.ArrayList;

public class WHGPMessage implements Serializable {
    private WHGPMessageType whgpMessageType;
    private String messageHeader;
    private String message;
    private GameInfo gameInfo;

    private boolean isGameStarted;
    private boolean isPlayerHost;
    private boolean gamePanesActive;

    private ArrayList<ArrayList<Tile>> tileGird;
    private ArrayList<Tile> selectedTiles;
    private ArrayList<String> playerList;

    public WHGPMessage() {
    }
    
    public WHGPMessageType getWhgpMessageType() {
        return whgpMessageType;
    }

    public void setWhgpMessageType(WHGPMessageType whgpMessageType) {
        this.whgpMessageType = whgpMessageType;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessageHeader() {
        return messageHeader;
    }

    public void setMessageHeader(String messageHeader) {
        this.messageHeader = messageHeader;
    }

    public ArrayList<String> getPlayerList() {
        return playerList;
    }

    public void setPlayerList(ArrayList<String> playerList) {
        this.playerList = playerList;
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

    public boolean isPlayerHost() {
        return isPlayerHost;
    }

    public void setPlayerHost(boolean playerHost) {
        isPlayerHost = playerHost;
    }

    public ArrayList<ArrayList<Tile>> getTileGird() {
        return tileGird;
    }

    public void setTileGird(ArrayList<ArrayList<Tile>> tileGird) {
        this.tileGird = tileGird;
    }

    public ArrayList<Tile> getSelectedTiles() {
        return selectedTiles;
    }

    public void setSelectedTiles(ArrayList<Tile> selectedTiles) {
        this.selectedTiles = selectedTiles;
    }

    public boolean isGamePanesActive() {
        return gamePanesActive;
    }

    public void setGamePanesActive(boolean gamePanesActive) {
        this.gamePanesActive = gamePanesActive;
    }
}
