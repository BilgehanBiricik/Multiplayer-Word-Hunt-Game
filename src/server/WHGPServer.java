package server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class WHGPServer extends Thread {

    private String ip;
    private String port;

    private static int playerCounter = 0;
    private static int maxPlayer = 10;

    private static String hostName = "";

    private static Game game;

    public WHGPServer(String ip, String port) {
        this.ip = ip;
        this.port = port;
    }

    @Override
    public void run() {
        try (ServerSocket serverSocket = new ServerSocket(Integer.parseInt(port), 50, InetAddress.getByName(ip))) {
            System.out.println("The server is running...");
            while (true) {
                Socket socket = serverSocket.accept();
                new ServerListener(socket, playerCounter).start();
                playerCounter++;
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

    public static int getMaxPlayer() {
        return maxPlayer;
    }

    public static void setMaxPlayer(int maxPlayer) {
        WHGPServer.maxPlayer = maxPlayer;
    }
}
