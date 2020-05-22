import javafx.stage.Stage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

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
        WHGPMessage msg = new WHGPMessage();
        msg.setWhgpMessageType(WHGPMessageType.INITIALIZE_GAME);
        msg.setMessage(username);
        write(msg);

        msg = new WHGPMessage();
        msg.setWhgpMessageType(WHGPMessageType.SET_GAME_INFO);
        msg.setGameInfo(gameInfo);
        write(msg);
    }

    public void joinGame(String username) throws IOException {
        WHGPMessage msg = new WHGPMessage();
        msg.setWhgpMessageType(WHGPMessageType.JOIN_REQUEST);
        msg.setMessage(username);
        write(msg);
    }

    public void startGame() throws IOException {
        WHGPMessage msg = new WHGPMessage();
        msg.setWhgpMessageType(WHGPMessageType.START_GAME);
        msg.setGameStarted(true);
        write(msg);
    }

    public void sendWord(ArrayList<Tile> selectedTiles) throws IOException {
        WHGPMessage msg = new WHGPMessage();
        msg.setWhgpMessageType(WHGPMessageType.SEND_WORD);
        msg.setSelectedTiles(selectedTiles);
        write(msg);
    }

    private void write(WHGPMessage message) throws IOException {
        out.writeUnshared(message);
        out.reset();
        // Why I didn't use writeObject and flush functions? The answer is:
        // https://stackoverflow.com/questions/8089583/why-is-javas-object-stream-returning-the-same-object-each-time-i-call-readobjec
    }
}
