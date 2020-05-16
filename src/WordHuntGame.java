import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class WordHuntGame extends BorderPane {

    public static ArrayList<String> dictionary;
    public static WordHuntGame wordHuntGame;

    private int gameAreaX;
    private int gameAreaY;
    private int maxPoint;
    private int totalGame;
    private String username;
    private String ip;
    private String port;
    private ArrayList<Integer> unavailableTilesPositions;
    private ArrayList<Integer> x2TilesPositions;
    private ArrayList<Integer> x3TilesPositions;

    private GridPane gameAreaPane;
    private VBox gameInfoPane;
    private HBox gameHandPane;

    private ListView<String> listView = new ListView<>();

    public WordHuntGame(int gameAreaX, int gameAreaY, int maxPoint, int totalGame,
                        String username, String ip, String port,
                        ArrayList<Integer> unavailableTilesPositions,
                        ArrayList<Integer> x2TilesPositions,
                        ArrayList<Integer> x3TilesPositions) {

        this.gameAreaX = gameAreaX;
        this.gameAreaY = gameAreaY;
        this.maxPoint = maxPoint;
        this.totalGame = totalGame;
        this.username = username;
        this.ip = ip;
        this.port = port;
        this.unavailableTilesPositions = unavailableTilesPositions;
        this.x2TilesPositions = x2TilesPositions;
        this.x3TilesPositions = x3TilesPositions;

        gameInfoPane = loadGameInfo();
        gameInfoPane.setPrefWidth(300);
        gameInfoPane.setStyle("-fx-border-color: red");
        super.setLeft(gameInfoPane);

        gameAreaPane = loadGameArea();
        super.setCenter(gameAreaPane);

        VBox vBottom = new VBox();
        vBottom.setPrefHeight(100);
        vBottom.setStyle("-fx-border-color: green");
        super.setBottom(vBottom);
    }

    private GridPane loadGameArea() {
        GridPane gridPane = new GridPane();
        for (int i = 0; i < gameAreaX; i++) {
            for (int j = 0; j < gameAreaY; j++) {
                int position = i * gameAreaX + j;
                Tile tile = new Tile();
                if (unavailableTilesPositions.contains(position))
                    tile.setState("unavailable");
                if (x2TilesPositions.contains(position))
                    tile.setState("2x");
                if (x3TilesPositions.contains(position))
                    tile.setState("3x");

                tile.setPosition(position);
                gridPane.add(tile, j, i);
            }
        }

        gridPane.setAlignment(Pos.CENTER);
        return gridPane;
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

        gridPane.add(text, 0, 0);
        gridPane.add(new Text("Oyun Alanı: "), 0, 2);
        gridPane.add(new Text(gameAreaX + "x" + gameAreaY), 1, 2);
        gridPane.add(new Text("Kullanılamaz Bölge Sayısı: "), 0, 3);
        gridPane.add(new Text(String.valueOf(unavailableTilesPositions.size())), 1, 3);
        gridPane.add(new Text("Toplam Oyun: "), 0, 4);
        gridPane.add(new Text(String.valueOf(totalGame)), 1, 4);
        gridPane.add(new Text("2x Sayısı: "), 0, 5);
        gridPane.add(new Text(String.valueOf(x2TilesPositions.size())), 1, 5);
        gridPane.add(new Text("3x Sayısı: "), 0, 6);
        gridPane.add(new Text(String.valueOf(x3TilesPositions.size())), 1, 6);

        playerListPane.add(text2, 0, 0);
        playerListPane.add(listView, 0, 2);

        gridPane.setHgap(15);
        gridPane.setVgap(15);
        playerListPane.setVgap(10);
        playerListPane.setHgap(10);
        vBox.getChildren().add(gridPane);
        vBox.getChildren().add(playerListPane);
        vBox.setSpacing(40);
        vBox.setPadding(new Insets(20, 20, 20,20));
        vBox.setAlignment(Pos.TOP_LEFT);

        return vBox;
    }

    private void fillDictionary() throws FileNotFoundException {
        dictionary = new ArrayList<>();

        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource("dictionary.txt").getFile());
        Scanner resourceReader = new Scanner(file);

        while (resourceReader.hasNext())
            dictionary.add(resourceReader.nextLine());
    }
}
