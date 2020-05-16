public class WHGPMessage {
    private WHGPMessageType whgpMessageType;
    private String message;

    public WHGPMessage() {
    }

    public WHGPMessage(WHGPMessageType whgpMessageType, String message) {
        this.whgpMessageType = whgpMessageType;
        this.message = message;
    }

    public WHGPMessageType getWhgpMessageType() {
        return whgpMessageType;
    }

    public void setWhgpMessageType(WHGPMessageType whgpMessageType) {
        this.whgpMessageType = whgpMessageType;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
