import com.google.common.eventbus.EventBus;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@SuppressWarnings("UnstableApiUsage")
public class Server {
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private ServerSocket serverSocket;
    private List<String> list = new ArrayList<String>();
    private EventBus bus;
    private List<ServerHandel> serverHandels = new ArrayList<>();

    public Server(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }

    public void serverStarted() {
        try {
            while (!this.serverSocket.isClosed()) {
                Socket socket = this.serverSocket.accept();
                System.out.println("New Client Connected...");
                this.bus = new EventBus();
                this.bus.register(this);
                list.add("Beka");
                list.add("Ana");
                list.add("Tomas");
                this.out = new ObjectOutputStream(socket.getOutputStream());
                this.in = new ObjectInputStream(socket.getInputStream());
                ServerHandel serverHandel = new ServerHandel(this.out, this.in, this.serverSocket, this.list, this.bus, new Random().nextInt());
                serverHandels.add(serverHandel);
                serverHandel.addServerHandler(serverHandels);
                Thread thread = new Thread(serverHandel);
                thread.start();
            }

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Verbindung mit dem Server unterbrochen");
        }
    }


    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(1000);
            Server server = new Server(serverSocket);
            server.serverStarted();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
