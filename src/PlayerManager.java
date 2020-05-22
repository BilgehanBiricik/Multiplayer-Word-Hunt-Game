import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PlayerManager {
    private static PlayerManager playerManager;

    private ArrayList<PlayerHandler> players;

    private ArrayList<String> playerList;

    private PlayerManager() {
        players = new ArrayList<>();
        playerList = new ArrayList<>();
    }

    public static synchronized PlayerManager getInstance() {
        if (playerManager == null)
            playerManager = new PlayerManager();
        return playerManager;
    }

    public void sendToPlayers(WHGPMessage message) throws IOException {
        for (PlayerHandler ph : players) {
            ph.write(message);
        }
    }

    public synchronized void connectPlayer(PlayerHandler ph) {
        players.add(ph);
    }

    public synchronized void disconnectPlayer(PlayerHandler ph) {
        players.remove(ph);
    }

    public synchronized ArrayList<PlayerHandler> getPlayers() {
        return players;
    }

    public void setPlayers(ArrayList<PlayerHandler> players) {
        this.players = players;
    }

    public ArrayList<String> printAllPlayersAndStats() {
        ArrayList<String> tmp = new ArrayList<>();
        for (PlayerHandler ph: players) {
            tmp.add(ph.getPlayerName() + " (Puan: " + ph.getPlayerPoint() + " - Skor: " + ph.getPlayerScore() + ")");
        }
        return tmp;
    }

    public synchronized ArrayList<String> getPlayerList() {
        return playerList;
    }

    public void setPlayerList(ArrayList<String> playerList) {
        this.playerList = playerList;
    }
}
