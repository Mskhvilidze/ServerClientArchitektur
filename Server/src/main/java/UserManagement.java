import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.List;

@SuppressWarnings("UnstableApiUsage")
public class UserManagement {
    private List<String> list;
    private EventBus bus;
    private Service service;

    public UserManagement(List<String> list, EventBus bus, ObjectOutputStream out) {
        this.list = list;
        this.bus = bus;
        bus.register(this);
        this.service = new Service(this.bus, out);
    }

    @Subscribe
    public void onRequestChatMessage(Messages messages) {
        try {
            if (messages instanceof RequestChatMessage) {
                if (((RequestChatMessage) messages).getText().equals("")) {
                    this.service.sendMessageToChat(new ResponseChatMessage("***", ((RequestChatMessage) messages).getUsername()));
                } else {
                    this.service.sendMessageToChat(new ResponseChatMessage(((RequestChatMessage) messages).getText(),
                            ((RequestChatMessage) messages).getUsername()));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Subscribe
    public void onRequestLogin(Messages messages) {
        if (messages instanceof RequestLogin) {
            validLogin(((RequestLogin) messages).getName());
        }
    }

    public void validLogin(String name) {
        try {
            boolean isValid = false;
            for (String username : list) {
                if (username.equals(name)) {
                    this.service.sendObjectOnClient(new ResponsLogin("Angemeldet"));
                    isValid = true;
                    break;
                }
            }
            if (!isValid) {
                service.sendObjectOnClient(new ResponsLogin("Nope"));
            }

        } catch (IOException e) {
            e.printStackTrace();

        }
    }
}
