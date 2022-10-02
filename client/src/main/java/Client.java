import com.google.common.eventbus.EventBus;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

@SuppressWarnings("UnstableApiUsage")
public class Client {
    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private String username;
    private EventBus bus;

    public Client(Socket socket, String username) {
        try {
            this.socket = socket;
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(this.socket.getOutputStream()));
            this.bufferedReader = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
            this.out = new ObjectOutputStream(this.socket.getOutputStream());
            this.in = new ObjectInputStream(this.socket.getInputStream());
            this.bus = new EventBus();
            this.bus.register(this);
            this.username = username;
            this.out.writeObject(new RequestLogin(this.username));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void started() {
        ClientHandler clientHandler = new ClientHandler(this.socket, this.bufferedReader, this.bufferedWriter, this.out, this.in, this.bus);
        new Thread(clientHandler).start();
    }

    public static void main(String[] args) {
        try {
            Scanner scanner = new Scanner(System.in);
            String username = scanner.nextLine();
            Socket socket = new Socket("localhost", 1000);
            Client client = new Client(socket, username);
            client.started();
        } catch (UnknownHostException e) {
            e.printStackTrace();
            System.out.println("Unbekanter Host");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("ClientVerbindung unterbrochen");
        }
    }
}
