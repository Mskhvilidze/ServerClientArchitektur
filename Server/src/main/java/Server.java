import com.google.common.eventbus.EventBus;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
@SuppressWarnings("UnstableApiUsage")
public class Server {
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private ServerSocket serverSocket;
    private List<String> list = new ArrayList<String>();
    private EventBus bus;
    private boolean isConnected = true;
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
                this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                this.out = new ObjectOutputStream(socket.getOutputStream());
                this.in = new ObjectInputStream(socket.getInputStream());
                new ServerHandel(this.bufferedReader, this.bufferedWriter, this.out, this.in, this.serverSocket, this.list, this.bus);
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
