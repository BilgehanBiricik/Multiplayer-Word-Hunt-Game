import client.stage.JoinGame;
import client.stage.SetUpGame;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class WordHuntGameApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setScene(new Scene(launcher(primaryStage),  320, 150));
        primaryStage.setTitle("Kelime Avı Oyunu");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }


    private BorderPane launcher(Stage parentStage) {
        Button setUpGame, joinGame;
        VBox vbox = new VBox();
        BorderPane borderPane = new BorderPane();

        setUpGame = new Button("Oyun Kur");
        setUpGame.setPrefWidth(100);
        setUpGame.setOnAction(actionEvent -> {
            Stage setUpGameStage = new Stage();
            setUpGameStage.setScene(new Scene(new SetUpGame(setUpGameStage), 600, 400));
            setUpGameStage.setTitle("Oyun Kur");
            setUpGameStage.show();
            parentStage.close();
        });

        joinGame = new Button("Oyuna Katıl");
        joinGame.setPrefWidth(100);
        joinGame.setOnAction(actionEvent -> {
            Stage joinGameStage = new Stage();
            joinGameStage.setScene(new Scene(new JoinGame(joinGameStage), 350, 200));
            joinGameStage.setTitle("Oyuna Katıl");
            joinGameStage.show();
            parentStage.close();
        });

        vbox.setAlignment(Pos.CENTER);
        vbox.setSpacing(20);
        vbox.getChildren().addAll(setUpGame, joinGame);
        borderPane.setCenter(vbox);
        return borderPane;
    }

}
