import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class WHGPMessage implements Serializable {
    private WHGPMessageType whgpMessageType;
    private String messageHeader;
    private String message;
    private List<String> playerList;
    private GameInfo gameInfo;
    private boolean isGameStarted;
    private boolean isPlayerHost;
    private ArrayList<ArrayList<Tile>> tileGird;

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

    public List<String> getPlayerList() {
        return playerList;
    }

    public void setPlayerList(List<String> playerList) {
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
}
