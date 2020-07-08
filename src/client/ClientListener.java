package client;

import client.stage.WordHuntGame;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import utils.message.WHGPMessage;
import utils.tile.Tile;
import utils.tile.TileButton;

import java.io.IOException;
import java.io.ObjectInputStream;

public class ClientListener extends Thread {
    private ObjectInputStream in;
    private WHGPClient whgpClient;
    private static Stage parentStage;
    private Stage wordHuntGameStage;

    private String clientName;

    public ClientListener(ObjectInputStream in, WHGPClient whgpClient) {
        this.in = in;
        this.whgpClient = whgpClient;
        clientName = "";
    }

    @Override
    public void run() {
        try {
            while (whgpClient.socket.isConnected()) {
                WHGPMessage message;
                message = (WHGPMessage) in.readUnshared();

                if (message != null) {
                    switch (message.getWhgpMessageType()) {
                        case PLAYER_JOINED:
                            Platform.runLater(() ->
                            {
                                WordHuntGame.getInstance().getListView().getItems().setAll(message.getPlayerList());
                                if (clientName.equals("")) {
                                  clientName = message.getMessage();
                                }
                            });
                            break;
                        case PLAYER_LIST:
                            Platform.runLater(() -> WordHuntGame.getInstance().getListView().getItems().setAll(message.getPlayerList()));
                            break;
                        case GET_GAME_INFO:
                            Platform.runLater(() ->
                            {
                                WordHuntGame.getInstance().setGameInfo(message.getGameInfo());
                                WordHuntGame.getInstance().setWhgpClient(whgpClient);
                                WordHuntGame.getInstance().getBtnStartGame().setVisible(message.isPlayerHost());

                                wordHuntGameStage = new Stage();
                                wordHuntGameStage.setScene(new Scene(WordHuntGame.getInstance().loadGameScene(), 1100, 900));

                                wordHuntGameStage.setTitle(clientName + " - Kelime Avı Oyunu");
                                wordHuntGameStage.show();
                                wordHuntGameStage.setOnCloseRequest(windowEvent -> {
                                    try {
                                        whgpClient.playerLeft(clientName);
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                });
                                parentStage.close();
                            });
                            break;
                        case USERNAME_EXIST:
                            Platform.runLater(() -> {
                                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                                alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
                                alert.setHeaderText(message.getMessageHeader());
                                alert.setContentText(message.getMessage());
                                alert.show();
                            });
                            break;
                        case WORD_REJECTED:
                        case GAME_IS_STARTED:
                        case MAX_PLAYER_LIMIT:
                        case PLAYER_LEFT:
                            Platform.runLater(() -> {
                                Alert alert = new Alert(Alert.AlertType.WARNING);
                                alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
                                alert.setHeaderText(message.getMessageHeader());
                                alert.setContentText(message.getMessage());
                                alert.show();
                            });
                            break;
                        case GET_TILE_GRID:
                            GridPane gridPane = new GridPane();
                            for (int i = 0; i < message.getTileGird().get(0).size(); i++) {
                                for (int j = 0; j < message.getTileGird().size(); j++) {
                                    Tile tile = message.getTileGird().get(i).get(j);
                                    TileButton tileButton = new TileButton(tile);
                                    tileButton.setOnAction(actionEvent -> {
                                        if (WordHuntGame.getInstance().getWord().length() > 1) {
                                            int index = tileButton.getClickCounter();
                                            if (index >= 0 && index <= WordHuntGame.getInstance().getWord().length()) {
                                                if (index == WordHuntGame.getInstance().getWord().length()) {
                                                    tile.setLetter("");
                                                    tileButton.setText("");
                                                    tileButton.setClickCounter(0);
                                                    WordHuntGame.getInstance().setLastSelectedTile(null);
                                                } else {
                                                    String letter = String.valueOf(WordHuntGame.getInstance().getWord().charAt(index));
                                                    tile.setLetter(letter);
                                                    tileButton.setText(letter.toUpperCase());
                                                    tileButton.setClickCounter(++index);
                                                    WordHuntGame.getInstance().setLastSelectedTile(tile);
                                                }
                                            }
                                        } else {
                                            Alert alert = new Alert(Alert.AlertType.WARNING);
                                            alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
                                            alert.setHeaderText("Uyarı");
                                            alert.setContentText("Girdiğiniz kelime 2 harften uzun olmalı!");
                                            alert.show();
                                        }
                                    });
                                    gridPane.add(tileButton, j, i);
                                }
                            }
                            Platform.runLater(() -> {
                                gridPane.setAlignment(Pos.CENTER);
                                WordHuntGame.getInstance().getGameAreaPane().getChildren().clear();
                                WordHuntGame.getInstance().getGameAreaPane().getChildren().setAll(gridPane);
                            });
                            break;
                        case CURRENT_PLAYER:
                            Platform.runLater(() -> {
                                WordHuntGame.getInstance().getTxtCurrentPlayer().setText("Sıra " + message.getMessage() + " adlı kullanıcıda");
                                WordHuntGame.getInstance().getGameWordInsertionPane().setDisable(!message.getMessage().equals(this.clientName));
                                WordHuntGame.getInstance().getGameAreaPane().setDisable(!message.getMessage().equals(this.clientName));
                            });
                            break;
                        case WINNER_OF_ROUND:
                            Platform.runLater(() -> {
                                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                                alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
                                alert.setHeaderText("Tur Bitti");
                                alert.setContentText("Bu turu kazanan " + message.getMessage() + " adlı oyuncu oldu.");
                                alert.show();
                                alert.setOnCloseRequest(dialogEvent -> {
                                    try {
                                        whgpClient.resetRound(clientName.equals(message.getMessage()));
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                });
                            });
                            break;
                        case WINNER_OF_GAME:
                            Platform.runLater(() -> {
                                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                                alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
                                alert.setHeaderText(message.getMessageHeader());
                                alert.setContentText(message.getMessage());
                                alert.show();
                                alert.setOnCloseRequest(dialogEvent -> {
                                    wordHuntGameStage.close();
                                });
                            });
                            break;
                        case MIN_PLAYER_LIMIT:
                            Platform.runLater(() -> {
                                Alert alert = new Alert(Alert.AlertType.WARNING);
                                alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
                                alert.setHeaderText(message.getMessageHeader());
                                alert.setContentText(message.getMessage());
                                alert.show();
                                WordHuntGame.getInstance().getBtnStartGame().setDisable(false);
                            });
                            break;
                    }
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    public static Stage getParentStage() {
        return parentStage;
    }

    public static void setParentStage(Stage parentStage) {
        ClientListener.parentStage = parentStage;
    }
}
