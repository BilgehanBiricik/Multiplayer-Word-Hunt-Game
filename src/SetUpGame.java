import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;


public class SetUpGame extends BorderPane {
    public SetUpGame() {
        TextField txtFieldGameAreaX = new TextField();
        TextField txtFieldGameAreaY = new TextField();
        TextField txtFieldUnAvailableRegions = new TextField();
        TextField txtFieldMaxPoint = new TextField();
        TextField txtFieldTotalGame = new TextField();
        TextField txtField2X = new TextField();
        TextField txtField3X = new TextField();
        Button btnSetUpGame = new Button("Kur");

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
        gridPane.add(btnSetUpGame, 3, 5);

        gridPane.setAlignment(Pos.CENTER);
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setGridLinesVisible(true);
        super.setCenter(gridPane);
    }
}
