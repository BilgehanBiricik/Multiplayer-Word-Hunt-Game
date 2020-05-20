import javafx.scene.control.Button;

import java.io.Serializable;

public class Tile implements Serializable {
    private String letter;
    private int position;
    private TileType tileType;

    public Tile() {
        this.tileType = TileType.NORMAL;
    }

    public String getLetter() {
        return letter;
    }

    public void setLetter(String letter) {
        this.letter = letter;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public TileType getTileType() {
        return tileType;
    }

    public void setTileType(TileType tileType) {
        this.tileType = tileType;
    }
}
