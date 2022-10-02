import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

@SuppressWarnings("UnstableApiUsage")
public class Service {

    private EventBus bus;
    private ObjectOutputStream out;
    private List<String> users = new ArrayList<String>();
    private String username;

    public Service(EventBus bus, ObjectOutputStream out) {
        this.bus = bus;
        this.out = out;
        this.bus = bus;
        bus.register(this);
    }

    @Subscribe
    public void onLoginResponse(Messages messages) {
        if (messages instanceof ResponsLogin) {
            if (((ResponsLogin) messages).getName().equals("Angemeldet")) {
                this.username = ((ResponsLogin) messages).getName();
                users.add(this.username);
                System.out.println("Message...");
                Scanner scanner = new Scanner(System.in);
                String text = scanner.nextLine();
                post(new RequestChatMessage(text, username));
            } else {
                System.out.println("Ihr eingegebener Name wurde nicht gefunden!");
                System.out.println("Bitte geben Sie Ihren Namen noch ein Mal!");
                Scanner scanner = new Scanner(System.in);
                String username = scanner.nextLine();
                post(new RequestLogin(username));
            }
        }
    }

    @Subscribe
    public void onResponseChatMessage(Messages messages) {
        if (messages instanceof ResponseChatMessage) {
            System.out.println(((ResponseChatMessage) messages).getUsername() + " : " + ((ResponseChatMessage) messages).getText());
            System.out.println("Message...");
            Scanner scanner = new Scanner(System.in);
            String text = scanner.nextLine();
            post(new RequestChatMessage(text, username));
        }
    }

    public void post(Messages messages) {
        try {
            this.out.writeObject(messages);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
