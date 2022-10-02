import com.google.common.eventbus.EventBus;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

@SuppressWarnings("UnstableApiUsage")
public class ClientHandler implements Runnable {

    private ArrayList<ClientHandler> clientHandlers = new ArrayList<ClientHandler>();
    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private String clientUserName;
    private EventBus bus;

    public ClientHandler(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter, ObjectOutputStream out,
                         ObjectInputStream in, EventBus bus) {
        try {
            this.socket = socket;
            this.bufferedReader = bufferedReader;
            this.bufferedWriter = bufferedWriter;
            this.out = out;
            this.in = in;
            clientHandlers.add(this);
            this.bus = bus;
            bus.register(this);
            new Service(this.bus, this.out);
        } catch (Exception e) {
            closeEverything(socket, bufferedReader, bufferedWriter);
        }
    }


    public void run() {
        while (this.socket.isConnected()) {
            try {
                Messages messages = (Messages) this.in.readObject();
                this.bus.post(messages);
            } catch (Exception e) {
                closeEverything(socket, bufferedReader, bufferedWriter);
                break;
            }
        }
    }

    public void print(ResponsLogin list) {

    }

    public void broadcastMessage(String messageSend) throws IOException {
        for (ClientHandler clientHandler : clientHandlers) {
            //  System.out.println(clientUserName + " : " + clientHandler.clientUserName);
            try {
                if (!clientHandler.clientUserName.equals(clientUserName)) {
                    clientHandler.bufferedWriter.write(messageSend);
                    clientHandler.bufferedWriter.newLine();
                    clientHandler.bufferedWriter.flush();
                }
            } catch (IOException e) {
                closeEverything(socket, bufferedReader, bufferedWriter);
            }
        }
    }

    public void removeClientHandler() throws IOException {
        clientHandlers.remove(this);
        broadcastMessage("Server: " + clientUserName + " has left chat!");
    }

    public void closeEverything(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter) {

        try {
            removeClientHandler();
            if (bufferedReader != null) {
                bufferedReader.close();
            }

            if (bufferedWriter != null) {
                bufferedWriter.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public Socket getSocket() {
        return socket;
    }
}
