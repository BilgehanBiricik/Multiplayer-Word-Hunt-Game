import javafx.scene.control.Button;

import java.io.Serializable;

public class Tile implements Serializable {
    private String letter;
    private int posX;
    private int poxY;
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

    public int getPosX() {
        return posX;
    }

    public void setPosX(int posX) {
        this.posX = posX;
    }

    public int getPoxY() {
        return poxY;
    }

    public void setPoxY(int poxY) {
        this.poxY = poxY;
    }

    public TileType getTileType() {
        return tileType;
    }

    public void setTileType(TileType tileType) {
        this.tileType = tileType;
    }
}
