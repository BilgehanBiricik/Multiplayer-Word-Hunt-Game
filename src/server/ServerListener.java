package server;

import utils.message.WHGPMessage;
import utils.message.WHGPMessageType;
import utils.tile.Tile;
import utils.tile.TileType;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

public class ServerListener extends Thread {

    private Player player;

    private ObjectInputStream in;
    private ObjectOutputStream out;

    private Socket socket;

    public ServerListener(Socket socket, int playerId) throws IOException {
        this.out = new ObjectOutputStream(socket.getOutputStream());
        this.in = new ObjectInputStream(socket.getInputStream());
        this.socket = socket;
        this.player = new Player(playerId, out);
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

                                player.setName(message.getMessage());
                                player.setScore(0);
                                player.setPoint(0);

                                PlayerManager.getInstance().connectPlayer(player);
                                PlayerManager.getInstance().getPlayerList().add(player.getName());

                                WHGPServer.setGame(new Game());
                                WHGPServer.setHostName(player.getName());

                                try {
                                    WHGPMessage msg = new WHGPMessage();
                                    msg.setWhgpMessageType(WHGPMessageType.PLAYER_JOINED);
                                    msg.setPlayerList(PlayerManager.getInstance().printAllPlayersAndStats());
                                    msg.setMessage(player.getName());
                                    PlayerManager.getInstance().sendToPlayers(msg);

                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                            }
                            break;

                        case SET_GAME_INFO:
                            WHGPServer.getGame().setGameInfo(message.getGameInfo());

                            setTileGrid();

                            try {
                                WHGPMessage msg = new WHGPMessage();
                                msg.setWhgpMessageType(WHGPMessageType.GET_GAME_INFO);
                                msg.setGameInfo(WHGPServer.getGame().getGameInfo());
                                msg.setPlayerHost(player.getName().equals(WHGPServer.getHostName()));
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

                                        player.setName(message.getMessage());
                                        player.setScore(0);
                                        player.setPoint(0);

                                        PlayerManager.getInstance().connectPlayer(player);
                                        PlayerManager.getInstance().getPlayerList().add(player.getName());

                                        try {
                                            WHGPMessage msg = new WHGPMessage();
                                            msg.setWhgpMessageType(WHGPMessageType.PLAYER_JOINED);
                                            msg.setPlayerList(PlayerManager.getInstance().printAllPlayersAndStats());
                                            msg.setMessage(player.getName());
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
                            if (PlayerManager.getInstance().getPlayers().size() <= 1) {
                                WHGPMessage msg = new WHGPMessage();
                                msg.setWhgpMessageType(WHGPMessageType.MIN_PLAYER_LIMIT);
                                msg.setMessageHeader("Uyarı");
                                msg.setMessage("Oyuna başlamak için minumum 2 oyuncu gerekli.");
                                write(msg);
                                break;
                            }

                            WHGPServer.getGame().setGameStarted(true);

                            int playerQue = WHGPServer.getGame().getPlayerQue();
                            WHGPServer.getGame().setCurrentPlayer(PlayerManager.getInstance().getPlayers().get(playerQue));

                            WHGPMessage msg = new WHGPMessage();
                            msg.setWhgpMessageType(WHGPMessageType.CURRENT_PLAYER);
                            msg.setMessage(WHGPServer.getGame().getCurrentPlayer().getName());
                            PlayerManager.getInstance().sendToPlayers(msg);
                            break;

                        case SEND_WORD:
                            StringBuilder stringBuilder = new StringBuilder();
                            for (Tile tile : message.getSelectedTiles()) {
                                stringBuilder.append(tile.getLetter());
                            }

                            String word = stringBuilder.toString().trim();

                            if (!WHGPServer.getGame().getUsedWords().contains(word)
                                    && WHGPServer.getGame().getDictionary().contains(word)) {

                                WHGPServer.getGame().getUsedWords().add(stringBuilder.toString().trim());

                                int x2Counter = 0, x3Counter = 0, disabledCounter = 0;

                                for (int i = 0; i < message.getSelectedTiles().size(); i++) {

                                    if (message.getSelectedTiles().get(i).isDisabled())
                                        disabledCounter++;
                                    else if (message.getSelectedTiles().get(i).getTileType() == TileType.X2)
                                        x2Counter++;
                                    else if (message.getSelectedTiles().get(i).getTileType() == TileType.X3)
                                        x3Counter++;

                                    WHGPServer.getGame().getTileArrayLists()
                                            .get(message.getSelectedTiles().get(i).getPosX())
                                            .get(message.getSelectedTiles().get(i).getPosY()).setLetter(message.getSelectedTiles()
                                            .get(i).getLetter());
                                    WHGPServer.getGame().getTileArrayLists()
                                            .get(message.getSelectedTiles().get(i).getPosX())
                                            .get(message.getSelectedTiles().get(i).getPosY()).setDisabled(true);
                                }

                                int calcPoint = (x3Counter != 0 ? (3 * x3Counter) : 1) * (x2Counter != 0 ? (2 * x2Counter) : 1) * (message.getSelectedTiles().size() - disabledCounter);
                                player.setPoint(player.getPoint() + calcPoint);

                                playerQue = WHGPServer.getGame().getPlayerQue();
                                playerQue++;
                                playerQue %= PlayerManager.getInstance().getPlayers().size();
                                WHGPServer.getGame().setCurrentPlayer(PlayerManager.getInstance().getPlayers().get(playerQue));
                                WHGPServer.getGame().setPlayerQue(playerQue);

                                msg = new WHGPMessage();
                                msg.setWhgpMessageType(WHGPMessageType.PLAYER_LIST);
                                msg.setPlayerList(PlayerManager.getInstance().printAllPlayersAndStats());
                                PlayerManager.getInstance().sendToPlayers(msg);

                                if (player.getPoint() >= WHGPServer.getGame().getGameInfo().getMaxPoint()) {

                                    player.setScore(player.getScore() + 1);

                                    for (Player p : PlayerManager.getInstance().getPlayers()) {
                                        p.setPoint(0);
                                    }

                                    WHGPServer.getGame().setPlayerQue(0);
                                    WHGPServer.getGame().setCurrentPlayer(PlayerManager.getInstance().getPlayers().get(WHGPServer.getGame().getPlayerQue()));

                                    if (player.getScore() == WHGPServer.getGame().getGameInfo().getTotalGame()) {
                                        msg = new WHGPMessage();
                                        msg.setWhgpMessageType(WHGPMessageType.WINNER_OF_GAME);
                                        msg.setMessageHeader("Oyun Bitti");
                                        msg.setMessage("Oyunu kazanan " + player.getName() + " adlı kullanıcı oldu. Oyun kapatılıyor.");
                                        PlayerManager.getInstance().sendToPlayers(msg);

                                    }

                                    msg = new WHGPMessage();
                                    msg.setWhgpMessageType(WHGPMessageType.WINNER_OF_ROUND);
                                    msg.setMessage(player.getName());
                                    PlayerManager.getInstance().sendToPlayers(msg);

                                    break;
                                }


                                msg = new WHGPMessage();
                                msg.setWhgpMessageType(WHGPMessageType.CURRENT_PLAYER);
                                msg.setMessage(WHGPServer.getGame().getCurrentPlayer().getName());
                                PlayerManager.getInstance().sendToPlayers(msg);

                                msg = new WHGPMessage();
                                msg.setWhgpMessageType(WHGPMessageType.GET_TILE_GRID);
                                msg.setTileGird(WHGPServer.getGame().getTileArrayLists());
                                PlayerManager.getInstance().sendToPlayers(msg);


                            } else {
                                msg = new WHGPMessage();
                                msg.setWhgpMessageType(WHGPMessageType.WORD_REJECTED);
                                msg.setMessageHeader("Hata");
                                msg.setMessage("Girmiş olduğunuz kelime uygun değil veya daha önceden girilmiş. Lütfen farklı bir kelime giriniz.");
                                write(msg);

                                msg = new WHGPMessage();
                                msg.setWhgpMessageType(WHGPMessageType.GET_TILE_GRID);
                                msg.setTileGird(WHGPServer.getGame().getTileArrayLists());
                                PlayerManager.getInstance().sendToPlayers(msg);
                            }

                            break;

                        case RESET_ROUND:

                            WHGPServer.getGame().setUsedWords(new ArrayList<>());

                            if (message.isResetRound()) setTileGrid();

                            msg = new WHGPMessage();
                            msg.setWhgpMessageType(WHGPMessageType.CURRENT_PLAYER);
                            msg.setMessage(WHGPServer.getGame().getCurrentPlayer().getName());
                            PlayerManager.getInstance().sendToPlayers(msg);

                            msg = new WHGPMessage();
                            msg.setWhgpMessageType(WHGPMessageType.PLAYER_LIST);
                            msg.setPlayerList(PlayerManager.getInstance().printAllPlayersAndStats());
                            PlayerManager.getInstance().sendToPlayers(msg);

                            msg = new WHGPMessage();
                            msg.setWhgpMessageType(WHGPMessageType.GET_TILE_GRID);
                            msg.setTileGird(WHGPServer.getGame().getTileArrayLists());
                            PlayerManager.getInstance().sendToPlayers(msg);
                            break;

                        case CLIENT_QUIT:
                            Player player = null;
                            for (Player p : PlayerManager.getInstance().getPlayers()) {
                                if (p.getName().equals(message.getMessage())) {
                                    player = p;
                                    break;
                                }
                            }


                            PlayerManager.getInstance().disconnectPlayer(player);
                            PlayerManager.getInstance().getPlayerList().remove(player.getName());

                            if (WHGPServer.getGame().isGameStarted() && WHGPServer.getGame().getCurrentPlayer().getName().equals(player.getName())) {
                                playerQue = WHGPServer.getGame().getPlayerQue();

                                WHGPServer.getGame().setCurrentPlayer(PlayerManager.getInstance().getPlayers().get(playerQue));
                                WHGPServer.getGame().setPlayerQue(playerQue);

                                msg = new WHGPMessage();
                                msg.setWhgpMessageType(WHGPMessageType.CURRENT_PLAYER);
                                msg.setMessage(WHGPServer.getGame().getCurrentPlayer().getName());
                                PlayerManager.getInstance().sendToPlayers(msg);
                            }

                            msg = new WHGPMessage();
                            msg.setWhgpMessageType(WHGPMessageType.PLAYER_LIST);
                            msg.setPlayerList(PlayerManager.getInstance().printAllPlayersAndStats());
                            PlayerManager.getInstance().sendToPlayers(msg);

                            msg = new WHGPMessage();
                            msg.setWhgpMessageType(WHGPMessageType.PLAYER_LEFT);
                            msg.setMessageHeader("Uyarı");
                            msg.setMessage(message.getMessage() + " adlı kullanıcı oyundan çıkış yaptı.");
                            PlayerManager.getInstance().sendToPlayers(msg);
                            break;
                    }

                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

    }

    private void setTileGrid() {

        int gameArea = WHGPServer.getGame().getGameInfo().getGameAreaX() * WHGPServer.getGame().getGameInfo().getGameAreaY();

        ArrayList<Integer> unavailableTilesPositions = new ArrayList<>();
        ArrayList<Integer> x2TilesPositions = new ArrayList<>();
        ArrayList<Integer> x3TilesPositions = new ArrayList<>();

        unavailableTilesPositions = randomNumbersArray(unavailableTilesPositions, x2TilesPositions, x3TilesPositions, WHGPServer.getGame().getGameInfo().getUnavailableTiles(), gameArea);
        x2TilesPositions = randomNumbersArray(unavailableTilesPositions, x2TilesPositions, x3TilesPositions, WHGPServer.getGame().getGameInfo().getX2Tiles(), gameArea);
        x3TilesPositions = randomNumbersArray(unavailableTilesPositions, x2TilesPositions, x3TilesPositions, WHGPServer.getGame().getGameInfo().getX3Tiles(), gameArea);

        WHGPServer.getGame().setTileArrayLists(new ArrayList<>());
        for (int i = 0; i < WHGPServer.getGame().getGameInfo().getGameAreaX(); i++) {
            WHGPServer.getGame().getTileArrayLists().add(new ArrayList<>());
            for (int j = 0; j < WHGPServer.getGame().getGameInfo().getGameAreaY(); j++) {
                int position = i * WHGPServer.getGame().getGameInfo().getGameAreaX() + j;
                Tile tile = new Tile();
                if (unavailableTilesPositions.contains(position))
                    tile.setTileType(TileType.UNAVAILABLE);
                if (x2TilesPositions.contains(position))
                    tile.setTileType(TileType.X2);
                if (x3TilesPositions.contains(position))
                    tile.setTileType(TileType.X3);

                tile.setPosX(i);
                tile.setPosY(j);
                tile.setLetter("");
                WHGPServer.getGame().getTileArrayLists().get(i).add(tile);
            }
        }
        String pickedWord = WHGPServer.getGame().getDictionary().get((int) (Math.random() * (WHGPServer.getGame().getDictionary().size() + 1)));
        WHGPServer.getGame().getUsedWords().add(pickedWord.trim());
        for (int i = 0; i < pickedWord.length(); i++) {
            WHGPServer.getGame().getTileArrayLists().get((int) Math.ceil(WHGPServer.getGame().getGameInfo().getGameAreaX() / 2.0))
                    .get((int) ((Math.ceil(WHGPServer.getGame().getGameInfo().getGameAreaX() / 2.0) - (int) Math.ceil(pickedWord.length() / 2.0)) + i))
                    .setLetter(String.valueOf(pickedWord.charAt(i)));
            WHGPServer.getGame().getTileArrayLists().get((int) Math.ceil(WHGPServer.getGame().getGameInfo().getGameAreaX() / 2.0))
                    .get((int) ((Math.ceil(WHGPServer.getGame().getGameInfo().getGameAreaX() / 2.0) - (int) Math.ceil(pickedWord.length() / 2.0)) + i)).
                    setDisabled(true);
        }
    }

    private ArrayList<Integer> randomNumbersArray(ArrayList<Integer> unavailableTilesPositions, ArrayList<Integer> x2TilesPositions, ArrayList<Integer> x3TilesPositions, int length, int max) {
        ArrayList<Integer> arrayList = new ArrayList<>();
        for (int i = 0; i < length; i++) {
            int randPosition = (int) (Math.random() * (max + 1));
            while (arrayList.contains(randPosition)
                    || unavailableTilesPositions.contains(randPosition)
                    || x2TilesPositions.contains(randPosition)
                    || x3TilesPositions.contains(randPosition)
                    || (randPosition >= (WHGPServer.getGame().getGameInfo().getGameAreaX() * Math.ceil(WHGPServer.getGame().getGameInfo().getGameAreaX() / 2.0))
                    && randPosition <= (WHGPServer.getGame().getGameInfo().getGameAreaX() * Math.ceil(WHGPServer.getGame().getGameInfo().getGameAreaX() / 2.0) + WHGPServer.getGame().getGameInfo().getGameAreaX())))
                randPosition = (int) (Math.random() * (max + 1));
            arrayList.add(randPosition);
        }
        return arrayList;
    }

    private synchronized void write(WHGPMessage message) throws IOException {
        out.writeUnshared(message);
        out.reset();
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

}
