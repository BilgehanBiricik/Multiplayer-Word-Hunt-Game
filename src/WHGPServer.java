import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class WHGPServer extends Thread {

    private String username;
    private String ip;
    private String port;

    private static int playerCounter = 0;
    private static int maxPlayer = 10;

    public static String hostName = "";

    static ArrayList<String> playerList = new ArrayList<>();
    static ArrayList<Socket> playerSocketList = new ArrayList<>();

    static Game game;

    public WHGPServer(String username, String ip, String port) {
        this.username = username;
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
}
