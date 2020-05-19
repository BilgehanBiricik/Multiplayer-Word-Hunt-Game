import javafx.application.Platform;

import java.io.*;
import java.lang.reflect.Array;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class PlayerHandler extends Thread {
    private int playerId;
    private String playerName = "";
    private int playerScore = 0;
    private int playerPoint = 0;
    private boolean isPlayerTurn = false;

    private ObjectInputStream in;
    private ObjectOutputStream out;

    private Socket socket;

    public PlayerHandler(Socket socket, int playerId) throws IOException {
        out = new ObjectOutputStream(socket.getOutputStream());
        in = new ObjectInputStream(socket.getInputStream());
        this.socket = socket;
        this.playerId = playerId;
    }

    @Override
    public void run() {

        while (socket.isConnected()) {
            try {
                WHGPMessage message = null;
                message = (WHGPMessage) in.readObject();
                if (message != null) {
                    switch (message.getWhgpMessageType()) {
                        case INITIALIZE_GAME:
                            if (PlayerManager.getInstance().getPlayers().size() == 0) {
                                System.out.println(message.getMessage() + " initiate the game");

                                setPlayerId(playerId);
                                setPlayerName(message.getMessage());
                                setPlayerScore(0);
                                setPlayerPoint(0);

                                PlayerManager.getInstance().connectPlayer(this);
                                PlayerManager.playerList.add(playerName);

                                WHGPServer.game = new Game();
                                WHGPServer.hostName = playerName;

                                try {
                                    write(new WHGPMessage(WHGPMessageType.PLAYER_JOINED, PlayerManager.getInstance().printAllPlayers()));
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                            }
                            break;
                        case SET_GAME_INFO:
                            List<String> gameInfoList = Arrays.asList(message.getMessage().split(";"));
                            WHGPServer.game.setGameInfo(new GameInfo(Integer.parseInt(gameInfoList.get(0)), Integer.parseInt(gameInfoList.get(1)), Integer.parseInt(gameInfoList.get(2)), Integer.parseInt(gameInfoList.get(3)),
                                    Arrays.asList(gameInfoList.get(4).split("#")).stream().map(s -> Integer.parseInt(s)).collect(Collectors.toCollection(ArrayList::new)),
                                    Arrays.asList(gameInfoList.get(5).split("#")).stream().map(s -> Integer.parseInt(s)).collect(Collectors.toCollection(ArrayList::new)),
                                    Arrays.asList(gameInfoList.get(6).split("#")).stream().map(s -> Integer.parseInt(s)).collect(Collectors.toCollection(ArrayList::new))));
                            try {
                                write(new WHGPMessage(WHGPMessageType.GET_GAME_INFO, WHGPServer.game.getGameInfo().printGameInfo()));
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            break;
                        case JOIN_REQUEST:
                            if (!PlayerManager.playerList.contains(message.getMessage())) {
                                setPlayerId(playerId);
                                setPlayerName(message.getMessage());
                                setPlayerScore(0);
                                setPlayerPoint(0);

                                PlayerManager.getInstance().connectPlayer(this);
                                PlayerManager.playerList.add(playerName);

                                try {
                                    PlayerManager.getInstance().sendToPlayers(new WHGPMessage(WHGPMessageType.PLAYER_JOINED, PlayerManager.getInstance().printAllPlayers()));
                                    write(new WHGPMessage(WHGPMessageType.GET_GAME_INFO, WHGPServer.game.getGameInfo().printGameInfo()));
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                            } else {
                                try {
                                    write(new WHGPMessage(WHGPMessageType.USERNAME_EXIST, "Böyle bir kullanıcı adı mevcut. Lütfen farklı bir isim giriniz."));
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                    }

                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

    }

    synchronized void write(WHGPMessage message) throws IOException {
        System.out.println("Send to " + playerName + " - " + out.toString());
        out.writeObject(message);
        out.flush();
    }

    public void updatePoint(int p) {
        this.playerPoint += p;
    }


    public int getPlayerId() {
        return playerId;
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public int getPlayerScore() {
        return playerScore;
    }

    public void setPlayerScore(int playerScore) {
        this.playerScore = playerScore;
    }

    public int getPlayerPoint() {
        return playerPoint;
    }

    public void setPlayerPoint(int playerPoint) {
        this.playerPoint = playerPoint;
    }

    public boolean isPlayerTurn() {
        return isPlayerTurn;
    }

    public void setPlayerTurn(boolean playerTurn) {
        isPlayerTurn = playerTurn;
    }
}
