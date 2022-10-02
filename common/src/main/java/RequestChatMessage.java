

import java.io.Serializable;

public class RequestChatMessage extends AbstractMessages  implements Serializable{

    private String text;
    private String username;

    public RequestChatMessage(String text, String username) {
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
