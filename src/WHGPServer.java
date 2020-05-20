import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class WHGPServer extends Thread {

    private String ip;
    private String port;

    private static int playerCounter = 0;
    private static int maxPlayer = 10;

    private static String hostName = "";

    static ArrayList<String> playerList = new ArrayList<>();
    static ArrayList<Socket> playerSocketList = new ArrayList<>();

    private static Game game;

    public WHGPServer(String ip, String port) {
        this.ip = ip;
        this.port = port;
    }

    @Override
    public void run() {
        try (ServerSocket serverSocket = new ServerSocket(Integer.parseInt(port))) {
            System.out.println("The server is running...");
            while (true) {
                Socket socket = serverSocket.accept();
                playerCounter++;
                playerList.add("Player" + playerCounter);
                playerSocketList.add(socket);
                new PlayerHandler(socket, playerCounter).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getHostName() {
        return hostName;
    }

    public static void setHostName(String hostName) {
        WHGPServer.hostName = hostName;
    }

    public static Game getGame() {
        return game;
    }

    public static void setGame(Game game) {
        WHGPServer.game = game;
    }
}
