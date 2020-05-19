import javafx.stage.Stage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class WHGPClient {

    Socket socket;
    private ObjectInputStream in;
    private ObjectOutputStream out;

    public WHGPClient(String ip, String port) throws IOException {
        socket = new Socket(ip, Integer.parseInt(port));

        out = new ObjectOutputStream(socket.getOutputStream());
        in = new ObjectInputStream(socket.getInputStream());

        MessageHandler messageHandler = new MessageHandler(in, this);
        messageHandler.start();
    }

    public void initializeGame(String username, GameInfo gameInfo) throws IOException {
        write(new WHGPMessage(WHGPMessageType.INITIALIZE_GAME, username));
        write(new WHGPMessage(WHGPMessageType.SET_GAME_INFO, gameInfo.printGameInfo()));
    }

    public void joinGame(String username) throws IOException {
       write(new WHGPMessage(WHGPMessageType.JOIN_REQUEST, username));
    }

    public void startGame() throws IOException {
        write(new WHGPMessage(WHGPMessageType.START_GAME, ""));
    }

    void write(WHGPMessage message) throws IOException {
        out.writeObject(message);
        out.flush();
    }
}
