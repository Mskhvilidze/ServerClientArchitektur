import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    public static void main(String[] args) throws IOException {
        ServerSocket ss = new ServerSocket(4999);
        Socket s = ss.accept();

        System.out.println("Client Connected...");

        InputStreamReader in = new InputStreamReader(s.getInputStream());
        BufferedReader reader = new BufferedReader(in);

        String str = reader.readLine();
        System.out.println("Client: " + str);

        PrintWriter pr = new PrintWriter(s.getOutputStream());

        pr.println("Yes");
        pr.flush();
    }
}
