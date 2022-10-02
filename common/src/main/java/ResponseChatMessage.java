import java.io.Serializable;

public class ResponseChatMessage extends AbstractMessages implements Serializable {
    private String text;
    private String username;

    public ResponseChatMessage(String text, String username) {
        this.text = text;
        this.username = username;
    }

    public String getText() {
        return text;
    }

    public String getUsername() {
        return username;
    }
}
