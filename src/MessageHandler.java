import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class MessageHandler extends Thread {
    private ObjectInputStream in;
    private WHGPClient whgpClient;
    public static Stage parentStage;

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
                                List<String> playerList = Arrays.asList(finalMessage.getMessage().split("#"));
                                WordHuntGame.getInstance().getListView().getItems().setAll(playerList);
                            });
                            break;
                        case GET_GAME_INFO:
                            Platform.runLater(() ->
                            {
                                List<String> gameInfoList = Arrays.asList(finalMessage.getMessage().split(";"));
                                WordHuntGame.getInstance().setGameInfo(new GameInfo(Integer.parseInt(gameInfoList.get(0)), Integer.parseInt(gameInfoList.get(1)), Integer.parseInt(gameInfoList.get(2)), Integer.parseInt(gameInfoList.get(3)),
                                        Arrays.asList(gameInfoList.get(4).split("#")).stream().map(s -> Integer.parseInt(s)).collect(Collectors.toCollection(ArrayList::new)),
                                        Arrays.asList(gameInfoList.get(5).split("#")).stream().map(s -> Integer.parseInt(s)).collect(Collectors.toCollection(ArrayList::new)),
                                        Arrays.asList(gameInfoList.get(6).split("#")).stream().map(s -> Integer.parseInt(s)).collect(Collectors.toCollection(ArrayList::new))));
                                WordHuntGame.getInstance().setWhgpClient(whgpClient);

                                Stage wordHuntGameStage = new Stage();
                                wordHuntGameStage.setScene(new Scene(WordHuntGame.getInstance().loadGameScene(), 1100, 900));
                                wordHuntGameStage.setTitle("Kelime Avı Oyunu");
                                wordHuntGameStage.show();
                                parentStage.close();
                            });
                            break;
                        case USERNAME_EXIST:
                            Platform.runLater(() -> {
                                List<String> messageList = Arrays.asList(message.getMessage().split(";"));
                                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                                alert.setHeaderText(messageList.get(0));
                                alert.setContentText(messageList.get(1));
                                alert.show();

                            });
                            break;
                        case GAME_IS_STARTED:
                            Platform.runLater(() -> {
                                List<String> messageList = Arrays.asList(message.getMessage().split(";"));
                                Alert alert = new Alert(Alert.AlertType.WARNING);
                                alert.setHeaderText(messageList.get(0));
                                alert.setContentText(messageList.get(1));
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
}
