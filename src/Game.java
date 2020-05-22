import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;

public class Game {

    private GameInfo gameInfo;
    private boolean isGameStarted;

    private PlayerHandler currentPlayer;

    private ArrayList<ArrayList<Tile>> tileArrayLists;
    private ArrayList<String> dictionary;
    private ArrayList<String> usedWords;


    public Game() throws FileNotFoundException {
        isGameStarted = false;
        tileArrayLists = new ArrayList<>();
        usedWords = new ArrayList<>();
        fillDictionary();
    }

    private void fillDictionary() throws FileNotFoundException {
        dictionary = new ArrayList<>();
        File file = new File(Objects.requireNonNull(getClass().getClassLoader().getResource("dictionary.txt")).getFile());
        Scanner resourceReader = new Scanner(file);

        while (resourceReader.hasNext())
            dictionary.add(resourceReader.nextLine());
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

    public PlayerHandler getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(PlayerHandler currentPlayer) {
        this.currentPlayer = currentPlayer;
    }
}
