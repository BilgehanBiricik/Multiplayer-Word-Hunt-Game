package server;

import utils.tile.Tile;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Game {

    private GameInfo gameInfo;
    private boolean isGameStarted;

    private Player currentPlayer;
    private int playerQue;

    private ArrayList<ArrayList<Tile>> tileArrayLists;
    private ArrayList<String> dictionary;
    private ArrayList<String> usedWords;


    public Game() throws IOException {
        isGameStarted = false;
        tileArrayLists = new ArrayList<>();
        usedWords = new ArrayList<>();
        playerQue = 0;
        fillDictionary();
    }

    private void fillDictionary() throws IOException {
        dictionary = new ArrayList<>();

        BufferedReader bufferedReader = new BufferedReader(new FileReader("resources/dictionary.txt"));
        String line = bufferedReader.readLine();
        while (line != null) {
            dictionary.add(line);
            line = bufferedReader.readLine();
        }
        bufferedReader.close();
    }

    public GameInfo getGameInfo() {
        return gameInfo;
    }

    public void setGameInfo(GameInfo gameInfo) {
        this.gameInfo = gameInfo;
    }

    public boolean isGameStarted() {
        return isGameStarted;
    }

    public void setGameStarted(boolean gameStarted) {
        isGameStarted = gameStarted;
    }

    public ArrayList<ArrayList<Tile>> getTileArrayLists() {
        return tileArrayLists;
    }

    public void setTileArrayLists(ArrayList<ArrayList<Tile>> tileArrayLists) {
        this.tileArrayLists = tileArrayLists;
    }

    public ArrayList<String> getDictionary() {
        return dictionary;
    }

    public void setDictionary(ArrayList<String> dictionary) {
        this.dictionary = dictionary;
    }

    public ArrayList<String> getUsedWords() {
        return usedWords;
    }

    public void setUsedWords(ArrayList<String> usedWords) {
        this.usedWords = usedWords;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(Player currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public int getPlayerQue() {
        return playerQue;
    }

    public void setPlayerQue(int playerQue) {
        this.playerQue = playerQue;
    }
}
