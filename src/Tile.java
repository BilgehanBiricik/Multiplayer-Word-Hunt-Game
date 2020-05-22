import java.io.Serializable;

public class Tile implements Serializable {
    private TileType tileType;
    private String letter;
    private int posX;
    private int posY;
    private boolean isDisabled;

    public Tile() {
        this.tileType = TileType.NORMAL;
        this.isDisabled = false;
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

    public int getPosY() {
        return posY;
    }

    public void setPosY(int poxY) {
        this.posY = poxY;
    }

    public TileType getTileType() {
        return tileType;
    }

    public void setTileType(TileType tileType) {
        this.tileType = tileType;
    }

    public boolean isDisabled() {
        return isDisabled;
    }

    public void setDisabled(boolean disabled) {
        isDisabled = disabled;
    }

    @Override
    public String toString() {
        return "Tile{" +
                "tileType=" + tileType +
                ", letter='" + letter + '\'' +
                ", posX=" + posX +
                ", posY=" + posY +
                ", isDisable=" + isDisabled +
                '}';
    }
}
