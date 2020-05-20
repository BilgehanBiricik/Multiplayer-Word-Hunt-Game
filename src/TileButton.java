import javafx.scene.control.Button;

public class TileButton extends Button {
    private Tile tile;

    public TileButton(Tile tile) {

        super.setPrefWidth(40);
        super.setPrefHeight(40);
        super.setText(tile.getLetter());

        switch (tile.getTileType()) {
            case UNAVAILABLE:
                super.setDisable(true);
                super.setStyle("-fx-background-color: black");
                break;
            case X2:
                super.setStyle("-fx-background-color: yellow");
                break;
            case X3:
                super.setStyle("-fx-background-color: green");
                break;
        }
    }

    public Tile getTile() {
        return tile;
    }

    public void setTile(Tile tile) {
        this.tile = tile;
    }
}
