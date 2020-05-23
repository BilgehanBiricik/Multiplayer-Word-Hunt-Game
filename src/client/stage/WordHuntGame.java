package client.stage;

import client.WHGPClient;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import server.GameInfo;
import utils.tile.Tile;
import utils.tile.TileButton;

import java.io.IOException;
import java.util.ArrayList;

public class WordHuntGame {

    public static WordHuntGame wordHuntGame;

    private String word;
    private Tile lastSelectedTile;
    private ArrayList<Tile> wordToTileArrayList;

    private GameInfo gameInfo;
    private WHGPClient whgpClient;

    private GridPane gameAreaPane;
    private VBox gameInfoPane;
    private VBox gameWordInsertionPane;

    private ListView<String> listView;
    private Button btnStartGame;
    private Text txtCurrentPlayer;


    private WordHuntGame() {
        gameInfo = new GameInfo();
        listView = new ListView<>();
        btnStartGame = new Button("Oyunu Başlat");
        gameAreaPane = new GridPane();
        word = "";
        wordToTileArrayList = new ArrayList<>();
        txtCurrentPlayer = new Text("");
    }

    public static WordHuntGame getInstance() {
        if (wordHuntGame == null)
            wordHuntGame = new WordHuntGame();
        return wordHuntGame;
    }


    public BorderPane loadGameScene() {
        BorderPane borderPane = new BorderPane();
        gameInfoPane = loadGameInfo();
        gameInfoPane.setPrefWidth(300);
        gameInfoPane.setStyle("-fx-border-color: gray; -fx-border-width: 0 1 0 0 0");
        borderPane.setLeft(gameInfoPane);

        gameAreaPane.setAlignment(Pos.CENTER);
        gameAreaPane.setDisable(true);
        borderPane.setCenter(gameAreaPane);

        gameWordInsertionPane = loadGameWordInsertionPane();
        gameWordInsertionPane.setPrefHeight(100);
        gameWordInsertionPane.setStyle("-fx-border-color: gray; -fx-border-width: 1 0 0 0 0");
        gameWordInsertionPane.setDisable(true);
        borderPane.setBottom(gameWordInsertionPane);
        return borderPane;
    }


    private VBox loadGameInfo() {
        VBox vBox = new VBox();
        GridPane gridPane = new GridPane();
        GridPane playerListPane = new GridPane();

        Text text = new Text();
        text.setText("Oyun Bilgileri");
        text.setFill(Color.DARKBLUE);
        text.setFont(Font.font("Arial", FontWeight.BOLD, 16));

        Text text2 = new Text();
        text2.setText("Oyuncu Listesi");
        text2.setFill(Color.DARKBLUE);
        text2.setFont(Font.font("Arial", FontWeight.BOLD, 16));

        Button btn2x = new Button("2X");
        btn2x.setStyle("-fx-background-color: yellow");

        Button btn3x = new Button("3X");
        btn3x.setStyle("-fx-background-color: green");

        txtCurrentPlayer.setFill(Color.DARKBLUE);
        txtCurrentPlayer.setFont(Font.font("Arial", FontWeight.BOLD, 16));

        btnStartGame.setOnAction(actionEvent -> {
            try {
                whgpClient.startGame();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        gridPane.add(text, 0, 0);
        gridPane.add(new Text("Oyun Alanı: "), 0, 2);
        gridPane.add(new Text(gameInfo.getGameAreaX() + "x" + gameInfo.getGameAreaY()), 1, 2);
        gridPane.add(new Text("Kullanılamaz Bölge Sayısı: "), 0, 3);
        gridPane.add(new Text(String.valueOf(gameInfo.getUnavailableTilesPositions().size())), 1, 3);
        gridPane.add(new Text("Kazanma Puanı: "), 0, 4);
        gridPane.add(new Text(String.valueOf(gameInfo.getMaxPoint())), 1, 4);
        gridPane.add(new Text("Toplam Oyun: "), 0, 5);
        gridPane.add(new Text(String.valueOf(gameInfo.getTotalGame())), 1, 5);
        gridPane.add(new Text("2x Sayısı: "), 0, 6);
        gridPane.add(new Text(String.valueOf(gameInfo.getX2TilesPositions().size())), 1, 6);
        gridPane.add(new Text("3x Sayısı: "), 0, 7);
        gridPane.add(new Text(String.valueOf(gameInfo.getX3TilesPositions().size())), 1, 7);

        playerListPane.add(text2, 0, 0, 2, 1);
        playerListPane.add(listView, 0, 2, 2, 1);
        playerListPane.add(btn2x, 0, 4);
        playerListPane.add(btn3x, 1, 4);
        playerListPane.add(btnStartGame, 0, 6, 2, 1);
        playerListPane.add(txtCurrentPlayer, 0, 8, 2, 1);

        gridPane.setHgap(15);
        gridPane.setVgap(15);
        playerListPane.setVgap(10);
        playerListPane.setHgap(10);
        playerListPane.setGridLinesVisible(false);

        vBox.getChildren().add(gridPane);
        vBox.getChildren().add(playerListPane);
        vBox.setSpacing(40);
        vBox.setPadding(new Insets(20, 20, 20, 20));
        vBox.setAlignment(Pos.TOP_LEFT);

        return vBox;
    }

    private VBox loadGameWordInsertionPane() {

        VBox vBox = new VBox();
        HBox hBox = new HBox();

        Text text = new Text();
        text.setText("Kelimenizi aşağıya yazın ve daha sonra ilk harften başlayarak yerleşirin");
        text.setFill(Color.DARKBLUE);
        text.setFont(Font.font("Arial", FontWeight.BOLD, 16));

        TextField txtFieldWord = new TextField();
        txtFieldWord.setPrefWidth(200);
        txtFieldWord.setOnKeyReleased(keyEvent -> word = txtFieldWord.getText().trim());

        Button btnSend = new Button("Gönder");
        btnSend.setOnAction(actionEvent -> {
            int wordIndex = 0;
            boolean foundFlag = false;
            if (!word.equals("") && lastSelectedTile != null) {

                if ((lastSelectedTile.getPosY() - (word.length() - 1)) >= 0)
                    for (int i = (lastSelectedTile.getPosY() - (word.length() - 1)); i <= lastSelectedTile.getPosY(); i++) { // LEFT TO RIGHT
                        TileButton tileButton = getTileButtonFromGridPane(gameAreaPane, i, lastSelectedTile.getPosX());
                        if (tileButton.getTile().getLetter().equals(String.valueOf(word.charAt(wordIndex)))) {
                            wordToTileArrayList.add(tileButton.getTile());
                            wordIndex++;
                            foundFlag = true;
                        } else {
                            wordIndex = 0;
                            foundFlag = false;
                            wordToTileArrayList = new ArrayList<>();
                            break;
                        }
                    }

                if (!foundFlag && (lastSelectedTile.getPosY() + (word.length() - 1)) < gameInfo.getGameAreaX())
                    for (int i = (lastSelectedTile.getPosY() + (word.length() - 1)); i >= lastSelectedTile.getPosY(); i--) { // RIGHT TO LEFT
                        TileButton tileButton = getTileButtonFromGridPane(gameAreaPane, i, lastSelectedTile.getPosX());
                        if (tileButton.getTile().getLetter().equals(String.valueOf(word.charAt(wordIndex)))) {
                            wordToTileArrayList.add(tileButton.getTile());
                            wordIndex++;
                            foundFlag = true;
                        } else {
                            wordIndex = 0;
                            foundFlag = false;
                            wordToTileArrayList = new ArrayList<>();
                            break;
                        }
                    }

                if (!foundFlag && ((lastSelectedTile.getPosX() - (word.length() - 1)) >= 0))
                    for (int i = (lastSelectedTile.getPosX() - (word.length() - 1)); i <= lastSelectedTile.getPosX(); i++) { // TOP TO BOTTOM
                        TileButton tileButton = getTileButtonFromGridPane(gameAreaPane, lastSelectedTile.getPosY(), i);
                        if (tileButton.getTile().getLetter().equals(String.valueOf(word.charAt(wordIndex)))) {
                            wordToTileArrayList.add(tileButton.getTile());
                            wordIndex++;
                            foundFlag = true;
                        } else {
                            wordIndex = 0;
                            foundFlag = false;
                            wordToTileArrayList = new ArrayList<>();
                            break;
                        }
                    }

                if (!foundFlag && ((lastSelectedTile.getPosX() + (word.length() - 1)) < gameInfo.getGameAreaY()))
                    for (int i = (lastSelectedTile.getPosX() + (word.length() - 1)); i >= lastSelectedTile.getPosX(); i--) { // BOTTOM TO TOP
                        TileButton tileButton = getTileButtonFromGridPane(gameAreaPane, lastSelectedTile.getPosY(), i);
                        if (tileButton.getTile().getLetter().equals(String.valueOf(word.charAt(wordIndex)))) {
                            wordToTileArrayList.add(tileButton.getTile());
                            wordIndex++;
                            foundFlag = true;
                        } else {
                            wordIndex = 0;
                            foundFlag = false;
                            wordToTileArrayList = new ArrayList<>();
                            break;
                        }
                    }

                StringBuilder stringBuilder = new StringBuilder();
                for (Tile tile : wordToTileArrayList) {
                    stringBuilder.append(tile.getLetter());
                }
                System.out.println(stringBuilder);
                System.out.println(word);

                boolean disableFlag = false;
                for (Tile tile : wordToTileArrayList) {
                    if (tile.isDisabled()) {
                        disableFlag = true;
                        break;
                    }
                }

                if (stringBuilder.toString().equals(word) && disableFlag) {
                    try {
                        System.out.println(wordToTileArrayList);
                        whgpClient.sendWord(wordToTileArrayList);

                        wordIndex = 0;
                        txtFieldWord.clear();
                        word = "";
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
                    alert.setHeaderText("Dikkat");
                    alert.setContentText("Girmiş olduğunuz kelime ile alana yerleştirdiğiniz aynı değil veya kelime uygun yere girilmedi.");
                    alert.show();

                }

            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
                alert.setHeaderText("Dikkat");
                alert.setContentText("Lütfen kelimenizi kutucuğa girin ve daha sonra oyun alanına yerleştirin.");
                alert.show();
            }

            foundFlag = false;
            wordToTileArrayList = new ArrayList<>();

        });

        hBox.setSpacing(10);
        hBox.getChildren().addAll(txtFieldWord, btnSend);
        hBox.setAlignment(Pos.BASELINE_CENTER);

        vBox.setSpacing(5);
        vBox.setAlignment(Pos.CENTER);
        vBox.getChildren().addAll(text, hBox);
        return vBox;
    }

    private TileButton getTileButtonFromGridPane(GridPane gridPane, int col, int row) {
        GridPane rootNode = (GridPane) gridPane.getChildren().get(0);
        for (Node node : rootNode.getChildren()) {
            if (GridPane.getColumnIndex(node) == col && GridPane.getRowIndex(node) == row) {
                return (TileButton) node;
            }
        }
        return null;
    }

    public ListView<String> getListView() {
        return listView;
    }

    public void setListView(ListView<String> listView) {
        this.listView = listView;
    }

    public GameInfo getGameInfo() {
        return gameInfo;
    }

    public void setGameInfo(GameInfo gameInfo) {
        this.gameInfo = gameInfo;
    }

    public WHGPClient getWhgpClient() {
        return whgpClient;
    }

    public void setWhgpClient(WHGPClient whgpClient) {
        this.whgpClient = whgpClient;
    }

    public Button getBtnStartGame() {
        return btnStartGame;
    }

    public void setBtnStartGame(Button btnStartGame) {
        this.btnStartGame = btnStartGame;
    }

    public GridPane getGameAreaPane() {
        return gameAreaPane;
    }

    public void setGameAreaPane(GridPane gameAreaPane) {
        this.gameAreaPane = gameAreaPane;
    }

    public VBox getGameWordInsertionPane() {
        return gameWordInsertionPane;
    }

    public void setGameWordInsertionPane(VBox gameWordInsertionPane) {
        this.gameWordInsertionPane = gameWordInsertionPane;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public ArrayList<Tile> getWordToTileArrayList() {
        return wordToTileArrayList;
    }

    public void setWordToTileArrayList(ArrayList<Tile> wordToTileArrayList) {
        this.wordToTileArrayList = wordToTileArrayList;
    }

    public Tile getLastSelectedTile() {
        return lastSelectedTile;
    }

    public void setLastSelectedTile(Tile lastSelectedTile) {
        this.lastSelectedTile = lastSelectedTile;
    }

    public Text getTxtCurrentPlayer() {
        return txtCurrentPlayer;
    }

    public void setTxtCurrentPlayer(Text txtCurrentPlayer) {
        this.txtCurrentPlayer = txtCurrentPlayer;
    }
}
