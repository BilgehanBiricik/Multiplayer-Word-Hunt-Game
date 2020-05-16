import javafx.scene.control.Button;

public class Tile extends Button {
    private String letter;
    private int position;
    private String state;

    public Tile() {

        super.setPrefWidth(40);
        super.setPrefHeight(40);
        super.setText(String.valueOf(position));
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

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
        if (state.equals("unavailable"))
            super.setStyle("-fx-background-color: black");
        else if (state.equals("2x"))
            super.setStyle("-fx-background-color: yellow");
        else if (state.equals("3x"))
            super.setStyle("-fx-background-color: green");
    }
}
