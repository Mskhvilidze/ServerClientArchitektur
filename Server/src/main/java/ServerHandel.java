import com.google.common.eventbus.EventBus;

import java.io.*;
import java.net.ServerSocket;
import java.util.List;

@SuppressWarnings("UnstableApiUsage")
public class ServerHandel implements Runnable {
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private ServerSocket serverSocket;
    private List<String> list;
    private EventBus bus;
    private List<ServerHandel> serverHandels;
    private int session;

    public ServerHandel(ObjectOutputStream out, ObjectInputStream in, ServerSocket serverSocket, List<String> list, EventBus bus,
                        int session) {
        this.out = out;
        this.in = in;
        this.serverSocket = serverSocket;
        this.list = list;
        this.bus = bus;
        this.bus.register(this);
        this.session = session;
        new UserManagement(list, this.bus, this.out);
    }

    @Override
    public void run() {
        try {
            System.out.println(this.serverHandels.size());
            while (!this.serverSocket.isClosed()) {
                Messages messages = (Messages) this.in.readObject();
                if (messages instanceof RequestLogin) {
                    this.bus.post(messages);
                }

                if (messages instanceof RequestChatMessage) {
                    for (ServerHandel serverHandel : serverHandels) {
                        System.out.println(serverHandel.session);
                        if (serverHandel.session != this.session) {
                            serverHandel.bus.post(messages);
                        }
                    }
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void addServerHandler(List<ServerHandel> serverHandels) {
        this.serverHandels = serverHandels;
    }

    public void setOut(ObjectOutputStream out) {
        this.out = out;
    }

    public void setIn(ObjectInputStream in) {
        this.in = in;
    }

    public void setServerSocket(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }

    public void setBus(EventBus bus) {
        this.bus = bus;
    }

    public ObjectOutputStream getOut() {
        return out;
    }

    public ObjectInputStream getIn() {
        return in;
    }

    public ServerSocket getServerSocket() {
        return serverSocket;
    }

    public EventBus getBus() {
        return bus;
    }
}
