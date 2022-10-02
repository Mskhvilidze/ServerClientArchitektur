import java.io.Serializable;
import java.util.List;

public class ResponsLogin extends AbstractMessages implements Serializable {
    private String name;

    public ResponsLogin(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
