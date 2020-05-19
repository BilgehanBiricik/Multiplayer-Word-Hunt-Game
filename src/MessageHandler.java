import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.ObjectInputStream;

public class MessageHandler extends Thread {
    private ObjectInputStream in;
    private WHGPClient whgpClient;
    private static Stage parentStage;

    public MessageHandler(ObjectInputStream in, WHGPClient whgpClient) {
        this.in = in;
        this.whgpClient = whgpClient;
    }

    @Override
    public void run() {
        try {
            while (whgpClient.socket.isConnected()) {
                WHGPMessage message;
                message = (WHGPMessage) in.readObject();

                if (message != null) {
                    WHGPMessage finalMessage = message;
                    switch (finalMessage.getWhgpMessageType()) {
                        case PLAYER_JOINED:
                            Platform.runLater(() -> {
                                WordHuntGame.getInstance().getListView().getItems().setAll(finalMessage.getPlayerList());
                            });
                            break;
                        case GET_GAME_INFO:
                            Platform.runLater(() ->
                            {
                                WordHuntGame.getInstance().setGameInfo(finalMessage.getGameInfo());
                                WordHuntGame.getInstance().setWhgpClient(whgpClient);
                                WordHuntGame.getInstance().getBtnStartGame().setVisible(message.isPlayerHost());

                                Stage wordHuntGameStage = new Stage();
                                wordHuntGameStage.setScene(new Scene(WordHuntGame.getInstance().loadGameScene(), 1100, 900));
                                wordHuntGameStage.setTitle("Kelime Avı Oyunu");
                                wordHuntGameStage.show();
                                parentStage.close();
                            });
                            break;
                        case USERNAME_EXIST:
                            Platform.runLater(() -> {
                                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                                alert.setHeaderText(finalMessage.getMessageHeader());
                                alert.setContentText(finalMessage.getMessage());
                                alert.show();

                            });
                            break;
                        case GAME_IS_STARTED:
                            Platform.runLater(() -> {
                                Alert alert = new Alert(Alert.AlertType.WARNING);
                                alert.setHeaderText(finalMessage.getMessageHeader());
                                alert.setContentText(finalMessage.getMessage());
                                alert.show();
                            });
                            break;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    public static Stage getParentStage() {
        return parentStage;
    }

    public static void setParentStage(Stage parentStage) {
        MessageHandler.parentStage = parentStage;
    }
}
