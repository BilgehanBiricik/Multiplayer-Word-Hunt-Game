public class Player {
    private String username;
    private int score;
    private int point;
    private boolean isPlaying;

    public Player() {
    }

    public Player(String username, int score, int point, boolean isPlaying) {
        this.username = username;
        this.score = score;
        this.point = point;
        this.isPlaying = isPlaying;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public boolean isPlaying() {
        return isPlaying;
    }

    public void setPlaying(boolean playing) {
        isPlaying = playing;
    }
}
