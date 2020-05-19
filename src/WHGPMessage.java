import java.io.Serializable;
import java.util.ArrayList;

public class WHGPMessage implements Serializable {
    private WHGPMessageType whgpMessageType;
    private String messageHeader;
    private String message;
    private ArrayList<String> playerList;
    private GameInfo gameInfo;
    private boolean isGameStarted;

    public WHGPMessage(WHGPMessageType whgpMessageType, String message) {
        this.whgpMessageType = whgpMessageType;
        this.message = message;
    }

    public WHGPMessageType getWhgpMessageType() {
        return whgpMessageType;
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
}
