import java.io.Serializable;

public class RequestLogin extends AbstractMessages implements Serializable {

    private String name;

    public RequestLogin(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
