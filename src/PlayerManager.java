import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PlayerManager {
    private static PlayerManager playerManager;

    private List<PlayerHandler> players;
    private List<Game> inProcessGames;

    public static ArrayList<String> playerList = new ArrayList<>();

    private PlayerManager() {
        players = new ArrayList<>();
        inProcessGames = new ArrayList<>();
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

    public synchronized List<PlayerHandler> getPlayers() {
        return players;
    }

    public String printAllPlayers() {
        StringBuilder s = new StringBuilder();
        for (PlayerHandler ph: players) {
            s.append(ph.getPlayerName()).append(" ").append("(Puan: ").append(ph.getPlayerPoint()).append(" - Skor: ").append(ph.getPlayerScore()).append(")#");
        }
        return s.toString();
    }

    public synchronized ArrayList<String> getPlayerList() {
        return playerList;
    }


}
