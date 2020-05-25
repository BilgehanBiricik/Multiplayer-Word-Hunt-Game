package client.stage;

import client.ClientListener;
import client.WHGPClient;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;

public class JoinGame extends BorderPane {

    private WHGPClient whgpClient;

    public JoinGame(Stage parentStage) {
        TextField txtFieldIp = new TextField();
        TextField txtFieldPort = new TextField();
        TextField txtFieldUsername = new TextField();
        Button btnJoinGame = new Button("Katıl");


        btnJoinGame.setPrefWidth(100);
        btnJoinGame.setOnAction(actionEvent -> {
            try {
                if (!txtFieldIp.getText().isEmpty() && !txtFieldPort.getText().isEmpty() && !txtFieldUsername.getText().isEmpty()) {
                    ClientListener.setParentStage(parentStage);
                    whgpClient = new WHGPClient(txtFieldIp.getText(), txtFieldPort.getText());
                    whgpClient.joinGame(txtFieldUsername.getText());
                } else {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
                    alert.setHeaderText("Dikkat");
                    alert.setContentText("Lütfen boş alanları doldurun.");
                    alert.show();
                }
            } catch (IOException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
                alert.setHeaderText("Hata");
                alert.setContentText("Sunucu bulunamadı.");
                alert.show();
            }
        });

        txtFieldUsername.setText("bilgehan");
        txtFieldIp.setText("127.0.0.1");
        txtFieldPort.setText("5555");

        GridPane gridPane = new GridPane();
        gridPane.add(new Text("Kullanıcı Adı: "), 0, 0);
        gridPane.add(txtFieldUsername, 1, 0);
        gridPane.add(new Text("IP: "), 0, 1);
        gridPane.add(txtFieldIp, 1, 1);
        gridPane.add(new Text("Port: "), 0, 2);
        gridPane.add(txtFieldPort, 1, 2);
        gridPane.add(btnJoinGame, 1, 3);

        gridPane.setAlignment(Pos.CENTER);
        gridPane.setGridLinesVisible(false);
        gridPane.setVgap(10);
        gridPane.setHgap(10);
        super.setCenter(gridPane);
    }
}
