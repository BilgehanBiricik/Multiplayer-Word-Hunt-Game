import java.io.IOException;
import java.util.ArrayList;

public class PlayerManager {

    private static PlayerManager playerManager;

    private ArrayList<Player> players;

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
        for (Player ph : players) {
            ph.write(message);
        }
    }

    public synchronized void connectPlayer(Player ph) {
        players.add(ph);
    }

    public synchronized void disconnectPlayer(Player ph) {
        players.remove(ph);
    }

    public synchronized ArrayList<Player> getPlayers() {
        return players;
    }

    public void setPlayers(ArrayList<Player> players) {
        this.players = players;
    }

    public ArrayList<String> printAllPlayersAndStats() {
        ArrayList<String> tmp = new ArrayList<>();
        for (Player ph: players) {
            tmp.add(ph.getName() + " (Puan: " + ph.getPoint() + " - Skor: " + ph.getScore() + ")");
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
