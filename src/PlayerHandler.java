import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class PlayerHandler extends Thread {
    private int playerId;
    private String playerName = "";
    private int playerScore = 0;
    private int playerPoint = 0;
    private boolean isCurrentPlayer = false;

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
                WHGPMessage message;
                message = (WHGPMessage) in.readUnshared();
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
                                PlayerManager.getInstance().getPlayerList().add(playerName);

                                WHGPServer.setGame(new Game());
                                WHGPServer.setHostName(playerName);

                                try {
                                    WHGPMessage msg = new WHGPMessage();
                                    msg.setWhgpMessageType(WHGPMessageType.PLAYER_JOINED);
                                    msg.setPlayerList(PlayerManager.getInstance().printAllPlayersAndStats());
                                    PlayerManager.getInstance().sendToPlayers(msg);

                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                            }
                            break;
                        case SET_GAME_INFO:
                            WHGPServer.getGame().setGameInfo(message.getGameInfo());
                            WHGPServer.getGame().setTileArrayLists(new ArrayList<>());
                            for (int i = 0; i < WHGPServer.getGame().getGameInfo().getGameAreaX(); i++) {
                                WHGPServer.getGame().getTileArrayLists().add(new ArrayList<>());
                                for (int j = 0; j < WHGPServer.getGame().getGameInfo().getGameAreaY(); j++) {
                                    int position = i * WHGPServer.getGame().getGameInfo().getGameAreaX() + j;
                                    Tile tile = new Tile();
                                    if (WHGPServer.getGame().getGameInfo().getUnavailableTilesPositions().contains(position))
                                        tile.setTileType(TileType.UNAVAILABLE);
                                    if (WHGPServer.getGame().getGameInfo().getX2TilesPositions().contains(position))
                                        tile.setTileType(TileType.X2);
                                    if (WHGPServer.getGame().getGameInfo().getX3TilesPositions().contains(position))
                                        tile.setTileType(TileType.X3);

                                    tile.setPosX(i);
                                    tile.setPosY(j);
                                    tile.setLetter("");
                                    WHGPServer.getGame().getTileArrayLists().get(i).add(tile);
                                }
                            }
                            String pickedWord = WHGPServer.getGame().getDictionary().get((int) (Math.random() * (WHGPServer.getGame().getDictionary().size() + 1)));
                            System.out.println(pickedWord);
                            for (int i = 0; i < pickedWord.length(); i++) {
                                WHGPServer.getGame().getTileArrayLists().get((int) Math.ceil(WHGPServer.getGame().getGameInfo().getGameAreaX() / 2))
                                        .get((int) ((Math.ceil(WHGPServer.getGame().getGameInfo().getGameAreaX() / 2) - (int) Math.ceil(pickedWord.length() / 2)) + i))
                                        .setLetter(String.valueOf(pickedWord.charAt(i)));
                                WHGPServer.getGame().getTileArrayLists().get((int) Math.ceil(WHGPServer.getGame().getGameInfo().getGameAreaX() / 2))
                                        .get((int) ((Math.ceil(WHGPServer.getGame().getGameInfo().getGameAreaX() / 2) - (int) Math.ceil(pickedWord.length() / 2)) + i)).
                                        setDisabled(true);
                            }
                            try {
                                WHGPMessage msg = new WHGPMessage();
                                msg.setWhgpMessageType(WHGPMessageType.GET_GAME_INFO);
                                msg.setGameInfo(WHGPServer.getGame().getGameInfo());
                                msg.setPlayerHost(getPlayerName().equals(WHGPServer.getHostName()));
                                write(msg);

                                msg = new WHGPMessage();
                                msg.setWhgpMessageType(WHGPMessageType.GET_TILE_GRID);
                                msg.setTileGird(WHGPServer.getGame().getTileArrayLists());
                                write(msg);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            break;
                        case JOIN_REQUEST:
                            if (!WHGPServer.getGame().isGameStarted()) {
                                if (PlayerManager.getInstance().getPlayerList().size() < WHGPServer.getMaxPlayer()) {
                                    if (!PlayerManager.getInstance().getPlayerList().contains(message.getMessage())) {
                                        setPlayerId(playerId);
                                        setPlayerName(message.getMessage());
                                        setPlayerScore(0);
                                        setPlayerPoint(0);

                                        PlayerManager.getInstance().connectPlayer(this);
                                        PlayerManager.getInstance().getPlayerList().add(playerName);

                                        try {
                                            WHGPMessage msg = new WHGPMessage();
                                            msg.setWhgpMessageType(WHGPMessageType.PLAYER_JOINED);
                                            msg.setPlayerList(PlayerManager.getInstance().printAllPlayersAndStats());
                                            PlayerManager.getInstance().sendToPlayers(msg);

                                            msg = new WHGPMessage();
                                            msg.setWhgpMessageType(WHGPMessageType.GET_GAME_INFO);
                                            msg.setGameInfo(WHGPServer.getGame().getGameInfo());
                                            write(msg);

                                            msg = new WHGPMessage();
                                            msg.setWhgpMessageType(WHGPMessageType.GET_TILE_GRID);
                                            msg.setTileGird(WHGPServer.getGame().getTileArrayLists());
                                            write(msg);

                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }

                                    } else {
                                        try {
                                            WHGPMessage msg = new WHGPMessage();
                                            msg.setWhgpMessageType(WHGPMessageType.USERNAME_EXIST);
                                            msg.setMessageHeader("Kullanıcı Adı Mevcut");
                                            msg.setMessage("Böyle bir kullanıcı adı mevcut. Lütfen farklı bir isim giriniz.");
                                            write(msg);
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                } else {
                                    WHGPMessage msg = new WHGPMessage();
                                    msg.setWhgpMessageType(WHGPMessageType.MAX_PLAYER_LIMIT);
                                    msg.setMessageHeader("Oyuncu Sınırı");
                                    msg.setMessage("Oyuncu sayısı maksimum kapasiteye ulaştığı için katılamazsınız.");
                                    write(msg);
                                }

                            } else {
                                WHGPMessage msg = new WHGPMessage();
                                msg.setWhgpMessageType(WHGPMessageType.GAME_IS_STARTED);
                                msg.setMessageHeader("Oyun Başladı");
                                msg.setMessage("Oyun şuan da oynanmakta olduğu için giriş yapamazsınız!");
                                write(msg);
                            }
                            break;
                        case START_GAME:
                            WHGPServer.getGame().setGameStarted(true);

                            WHGPServer.getGame().setCurrentPlayer(PlayerManager.getInstance().getPlayers().get(0));

                            WHGPMessage msg = new WHGPMessage();
                            msg.setWhgpMessageType(WHGPMessageType.ACTIVATE_PANES);
                            msg.setGamePanesActive(true);
                            write(msg);

                            msg = new WHGPMessage();
                            msg.setWhgpMessageType(WHGPMessageType.CURRENT_PLAYER);
                            msg.setMessage(WHGPServer.getGame().getCurrentPlayer().getPlayerName());
                            PlayerManager.getInstance().sendToPlayers(msg);
                            break;

                        case SEND_WORD:
                            StringBuilder stringBuilder = new StringBuilder();
                            for (Tile tile : message.getSelectedTiles()) {
                                stringBuilder.append(tile.getLetter());
                            }

                            if (WHGPServer.getGame().getDictionary().contains(stringBuilder.toString())
                                    || !WHGPServer.getGame().getUsedWords().contains(stringBuilder.toString())) {

                                for(int i = 0; i < message.getSelectedTiles().size(); i++) {
                                    WHGPServer.getGame().getTileArrayLists()
                                            .get(message.getSelectedTiles().get(i).getPosX())
                                            .get(message.getSelectedTiles().get(i).getPosY()).setLetter(message.getSelectedTiles()
                                            .get(i).getLetter());
                                    WHGPServer.getGame().getTileArrayLists()
                                            .get(message.getSelectedTiles().get(i).getPosX())
                                            .get(message.getSelectedTiles().get(i).getPosY()).setDisabled(true);
                                }

                                msg = new WHGPMessage();
                                msg.setWhgpMessageType(WHGPMessageType.GET_TILE_GRID);
                                msg.setTileGird(WHGPServer.getGame().getTileArrayLists());
                                PlayerManager.getInstance().sendToPlayers(msg);

                            } else {
                                msg = new WHGPMessage();
                                msg.setWhgpMessageType(WHGPMessageType.WORD_REJECTED);
                                msg.setMessage("Girmiş olduğunuz kelime uygun değil veya daha önceden girilmiş. Lütfen farklı bir kelime giriniz.");
                                write(msg);
                            }

                            break;
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
        out.writeUnshared(message);
        out.reset();
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

}
