import com.google.common.eventbus.EventBus;

import java.io.*;
import java.net.ServerSocket;
import java.util.List;

@SuppressWarnings("UnstableApiUsage")
public class ServerHandel {
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private ServerSocket serverSocket;
    private final List<String> list;
    private EventBus bus;

    public ServerHandel(BufferedReader bufferedReader, BufferedWriter bufferedWriter, ObjectOutputStream out, ObjectInputStream in,
                        ServerSocket serverSocket, List<String> list, EventBus bus) {
        this.bufferedReader = bufferedReader;
        this.bufferedWriter = bufferedWriter;
        this.out = out;
        this.in = in;
        this.serverSocket = serverSocket;
        this.list = list;
        this.bus = bus;
        this.bus.register(this);
        new UserManagement(list, this.bus, this.out);
        sendObjectOnServer();
    }

    public void sendObjectOnServer() {
        new Thread(() -> {
            try {
                while (!this.serverSocket.isClosed()) {
                    Messages messages = (Messages) this.in.readObject();
                    this.bus.post(messages);
                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }).start();

    }

    public void setBufferedReader(BufferedReader bufferedReader) {
        this.bufferedReader = bufferedReader;
    }

    public void setBufferedWriter(BufferedWriter bufferedWriter) {
        this.bufferedWriter = bufferedWriter;
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

    public BufferedReader getBufferedReader() {
        return bufferedReader;
    }

    public BufferedWriter getBufferedWriter() {
        return bufferedWriter;
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
