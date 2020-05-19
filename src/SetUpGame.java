import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;


public class SetUpGame extends BorderPane {

    private WHGPClient whgpClient;
    private WHGPServer whgpServer;

    private TextField txtFieldGameAreaX = new TextField();
    private TextField txtFieldGameAreaY = new TextField();
    private TextField txtFieldUnAvailableRegions = new TextField();
    private TextField txtFieldMaxPoint = new TextField();
    private TextField txtFieldTotalGame = new TextField();
    private TextField txtField2X = new TextField();
    private TextField txtField3X = new TextField();
    private TextField txtFieldIp = new TextField();
    private TextField txtFieldPort = new TextField();
    private TextField txtFieldUsername = new TextField();
    private Button btnSetUpGame = new Button("Kur");

    private int gameAreaX;
    private int gameAreaY;
    private int gameArea;
    private int unavailableRegions;
    private int x2Tiles;
    private int x3Tiles;
    private int maxPoint;
    private int totalGame;

    private ArrayList<Integer> unavailableTilesPositions = new ArrayList<>();
    private ArrayList<Integer> x2TilesPositions = new ArrayList<>();
    private ArrayList<Integer> x3TilesPositions = new ArrayList<>();

    public SetUpGame() {

        txtFieldGameAreaX.setMaxWidth(50);
        txtFieldGameAreaY.setMaxWidth(50);
        txtFieldUnAvailableRegions.setMaxWidth(50);
        txtFieldMaxPoint.setMaxWidth(50);
        txtFieldTotalGame.setMaxWidth(50);
        txtField2X.setMaxWidth(50);
        txtField3X.setMaxWidth(50);
        txtFieldUsername.setMaxWidth(150);
        txtFieldIp.setMaxWidth(150);
        txtFieldPort.setMaxWidth(150);


        btnSetUpGame.setPrefWidth(100);
        btnSetUpGame.setOnAction(actionEvent -> {

            if (checkRules()) {
                try {
                    whgpServer = new WHGPServer(txtFieldUsername.getText(), txtFieldIp.getText(), txtFieldPort.getText());
                    whgpServer.start();
                    whgpClient = new WHGPClient(txtFieldIp.getText(), txtFieldPort.getText());
                    whgpClient.initializeGame(txtFieldUsername.getText(), new GameInfo(gameAreaX, gameAreaY, maxPoint, totalGame,
                            unavailableTilesPositions, x2TilesPositions, x3TilesPositions));
                } catch (IOException e) {
                    e.printStackTrace();
                }
//                Stage wordHuntGameStage = new Stage();
//                wordHuntGameStage.setScene(new Scene(WordHuntGame.getInstance().loadGameScene(), 1100, 900));
//                wordHuntGameStage.setTitle("Kelime Avı Oyunu");
//                wordHuntGameStage.show();
                ((Node) actionEvent.getSource()).getScene().getWindow().hide();
            }

        });

        txtFieldGameAreaX.setText("16");
        txtFieldGameAreaY.setText("16");
        txtFieldUnAvailableRegions.setText("20");
        txtFieldMaxPoint.setText("200");
        txtFieldTotalGame.setText("5");
        txtField3X.setText("5");
        txtField2X.setText("5");
        txtFieldUsername.setText("bilgehan");
        txtFieldIp.setText("localhost");
        txtFieldPort.setText("5555");

        GridPane gridPane = new GridPane();
        gridPane.add(new Text("Oyun Alanı X: "), 0, 0);
        gridPane.add(txtFieldGameAreaX, 1, 0);
        gridPane.add(new Text("Oyun Alanı Y: "), 2, 0);
        gridPane.add(txtFieldGameAreaY, 3, 0);
        gridPane.add(new Text("Kullanılamaz Bölge Sayısı: "), 0, 1);
        gridPane.add(txtFieldUnAvailableRegions, 1, 1);
        gridPane.add(new Text("Kazanma Puanı: "), 0, 2);
        gridPane.add(txtFieldMaxPoint, 1, 2);
        gridPane.add(new Text("Toplam Oyun: "), 0, 3);
        gridPane.add(txtFieldTotalGame, 1, 3);
        gridPane.add(new Text("2X Sayısı: "), 0, 4);
        gridPane.add(txtField2X, 1, 4);
        gridPane.add(new Text("3X Sayısı: "), 2, 4);
        gridPane.add(txtField3X, 3, 4);
        gridPane.add(new Text("Kulanıcı Adı: "), 0, 7);
        gridPane.add(txtFieldUsername, 1, 7, 3, 1);
        gridPane.add(new Text("Ip "), 0, 8);
        gridPane.add(txtFieldIp, 1, 8, 3, 1);
        gridPane.add(new Text("Port: "), 0, 9);
        gridPane.add(txtFieldPort, 1, 9, 3, 1);
        gridPane.add(btnSetUpGame, 0, 11);

        gridPane.setAlignment(Pos.CENTER);
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setGridLinesVisible(false);
        super.setCenter(gridPane);
    }

    private boolean checkRules() {

        if (txtFieldGameAreaX.getText().isEmpty()
                || txtFieldGameAreaY.getText().isEmpty()
                || txtFieldMaxPoint.getText().isEmpty()
                || txtField2X.getText().isEmpty()
                || txtField3X.getText().isEmpty()
                || txtFieldUnAvailableRegions.getText().isEmpty()
                || txtFieldTotalGame.getText().isEmpty()
                || txtFieldUsername.getText().isEmpty()
                || txtFieldIp.getText().isEmpty()
                || txtFieldPort.getText().isEmpty())
            return false;

        gameAreaX = Integer.parseInt(txtFieldGameAreaX.getText());
        gameAreaY = Integer.parseInt(txtFieldGameAreaY.getText());
        gameArea = gameAreaX * gameAreaY;
        unavailableRegions = Integer.parseInt(txtFieldUnAvailableRegions.getText());
        x2Tiles = Integer.parseInt(txtField2X.getText());
        x3Tiles = Integer.parseInt(txtField3X.getText());

        boolean checkMaxPoint = (gameArea - unavailableRegions) < (gameArea);
        boolean checkUnavailableRegions = (unavailableRegions < (int) (gameArea * 0.1));
        boolean checkSumOfSpecialTiles = (unavailableRegions + x3Tiles + x2Tiles) < gameArea;
        boolean checkSumOfGameAreaAxises = gameAreaX == gameAreaY;

        if (!checkMaxPoint || !checkUnavailableRegions || !checkSumOfSpecialTiles || !checkSumOfGameAreaAxises)
            return false;

        maxPoint = Integer.parseInt(txtFieldMaxPoint.getText());
        totalGame = Integer.parseInt(txtFieldTotalGame.getText());

        unavailableTilesPositions = randomNumbersArray(unavailableRegions, gameArea);
        x2TilesPositions = randomNumbersArray(x2Tiles, gameArea);
        x3TilesPositions = randomNumbersArray(x3Tiles, gameArea);

        return true;
    }

    private ArrayList<Integer> randomNumbersArray(int length, int max) {
        ArrayList<Integer> arrayList = new ArrayList<>();
        for (int i = 0; i < length; i++) {
            int randPosition = (int) (Math.random() * (max + 1));
            while (arrayList.contains(randPosition)
                    || unavailableTilesPositions.contains(randPosition)
                    || x2TilesPositions.contains(randPosition)
                    || x3TilesPositions.contains(randPosition))
                randPosition = (int) (Math.random() * (max + 1));
            arrayList.add(randPosition);
        }
        return arrayList;
    }
}
