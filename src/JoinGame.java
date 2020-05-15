import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;

public class JoinGame extends BorderPane {

    public JoinGame() {
        TextField txtFieldIp = new TextField();
        TextField txtFieldPort = new TextField();
        TextField txtFieldUsername = new TextField();
        Button btnJoinGame = new Button("Katıl");

        btnJoinGame.setOnAction(actionEvent -> {

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
        gridPane.setGridLinesVisible(true);
        gridPane.setVgap(10);
        gridPane.setHgap(10);
        super.setCenter(gridPane);
    }
}
