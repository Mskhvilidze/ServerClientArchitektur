import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;

import java.io.IOException;
import java.io.ObjectOutputStream;

public class Service {
    private EventBus bus;
    private ObjectOutputStream out;

    public Service(EventBus bus, ObjectOutputStream out) {
        this.bus = bus;
        bus.register(this);
        this.out = out;
    }


    public void sendObjectOnClient(Messages messages) throws IOException {
        try {
            if (messages instanceof ResponsLogin) {
                this.out.writeObject((ResponsLogin) messages);
            }
        } catch (IOException e) {
            e.printStackTrace();
            out.close();
        }
    }

    public void sendMessageToChat(Messages messages) throws IOException {
        try {
            if (messages instanceof ResponseChatMessage) {
                this.out.writeObject((ResponseChatMessage) messages);
            }
        } catch (IOException e) {
            e.printStackTrace();
            out.close();
        }
    }
}