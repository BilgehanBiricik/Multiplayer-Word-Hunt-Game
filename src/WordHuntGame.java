import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;

public class WordHuntGame {

    public static WordHuntGame wordHuntGame;

    public static ArrayList<String> dictionary;

    private GameInfo gameInfo;
    private WHGPClient whgpClient;

    private GridPane gameAreaPane;
    private VBox gameInfoPane;
    private HBox gameHandPane;

    private ListView<String> listView;
    private Button btnStartGame;

    private WordHuntGame() {
        gameInfo = new GameInfo();
        listView = new ListView<>();
        btnStartGame = new Button("Oyunu Başlat");
        gameAreaPane = new GridPane();
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
        gameInfoPane.setStyle("-fx-border-color: red");
        borderPane.setLeft(gameInfoPane);

        gameAreaPane.setAlignment(Pos.CENTER);
        borderPane.setCenter(gameAreaPane);

        VBox vBottom = new VBox();
        vBottom.setPrefHeight(100);
        vBottom.setStyle("-fx-border-color: green");
        borderPane.setBottom(vBottom);
        return borderPane;
    }

//    private GridPane loadGameArea() {
//        GridPane gridPane = new GridPane();
//        if(tileGrid.size() > 0) {
//            for (int i = 0; i < gameInfo.getGameAreaX(); i++) {
//                for (int j = 0; j < gameInfo.getGameAreaY(); j++) {
//                    gridPane.add(tileGrid.get(i).get(j), j, i);
//                }
//            }
//        }
//
//        gridPane.setAlignment(Pos.CENTER);
//        return gridPane;
//    }

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

        btnStartGame.setOnAction(actionEvent -> {
            try {
                whgpClient.startGame();
                btnStartGame.setDisable(true);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        gridPane.add(text, 0, 0);
        gridPane.add(new Text("Oyun Alanı: "), 0, 2);
        gridPane.add(new Text(gameInfo.getGameAreaX() + "x" + gameInfo.getGameAreaY()), 1, 2);
        gridPane.add(new Text("Kullanılamaz Bölge Sayısı: "), 0, 3);
        gridPane.add(new Text(String.valueOf(gameInfo.getUnavailableTilesPositions().size())), 1, 3);
        gridPane.add(new Text("Toplam Oyun: "), 0, 4);
        gridPane.add(new Text(String.valueOf(gameInfo.getTotalGame())), 1, 4);
        gridPane.add(new Text("2x Sayısı: "), 0, 5);
        gridPane.add(new Text(String.valueOf(gameInfo.getX2TilesPositions().size())), 1, 5);
        gridPane.add(new Text("3x Sayısı: "), 0, 6);
        gridPane.add(new Text(String.valueOf(gameInfo.getX3TilesPositions().size())), 1, 6);

        playerListPane.add(text2, 0, 0);
        playerListPane.add(listView, 0, 2);
        playerListPane.add(btnStartGame, 0, 4);

        gridPane.setHgap(15);
        gridPane.setVgap(15);
        playerListPane.setVgap(10);
        playerListPane.setHgap(10);
        vBox.getChildren().add(gridPane);
        vBox.getChildren().add(playerListPane);
        vBox.setSpacing(40);
        vBox.setPadding(new Insets(20, 20, 20, 20));
        vBox.setAlignment(Pos.TOP_LEFT);

        return vBox;
    }

    private void fillDictionary() throws FileNotFoundException {
        dictionary = new ArrayList<>();

        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(Objects.requireNonNull(classLoader.getResource("dictionary.txt")).getFile());
        Scanner resourceReader = new Scanner(file);

        while (resourceReader.hasNext())
            dictionary.add(resourceReader.nextLine());
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
}
