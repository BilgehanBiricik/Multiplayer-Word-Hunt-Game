import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;

public class JoinGame extends BorderPane {

    private WHGPClient whgpClient;

    public JoinGame() {
        TextField txtFieldIp = new TextField();
        TextField txtFieldPort = new TextField();
        TextField txtFieldUsername = new TextField();
        Button btnJoinGame = new Button("Katıl");


        btnJoinGame.setPrefWidth(100);
        btnJoinGame.setOnAction(actionEvent -> {
            try {
                whgpClient = new WHGPClient(txtFieldIp.getText(), txtFieldPort.getText());
                whgpClient.joinGame(txtFieldUsername.getText());
            } catch (IOException e) {
                e.printStackTrace();
            }
            ((Node) actionEvent.getSource()).getScene().getWindow().hide();
        });

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
