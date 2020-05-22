package server;

import utils.message.WHGPMessage;

import java.io.IOException;
import java.io.ObjectOutputStream;

public class Player {
    private int id;
    private String name;
    private int score;
    private int point;
    private boolean isCurrentPlayer;

    private ObjectOutputStream out;

    public Player(int id, ObjectOutputStream out) {
        this.id = id;
        this.out = out;
    }

    public synchronized void write(WHGPMessage message) throws IOException {
        out.writeUnshared(message);
        out.reset();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getPoint() {
        return point;
    }

    public void setPoint(int point) {
        this.point = point;
    }

    public boolean isCurrentPlayer() {
        return isCurrentPlayer;
    }

    public void setCurrentPlayer(boolean currentPlayer) {
        isCurrentPlayer = currentPlayer;
    }
}
