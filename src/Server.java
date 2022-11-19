import javax.imageio.IIOException;
import javax.security.sasl.SaslServer;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.*;

public class Server {
    public static void main(String[] args) {

        try {
            ServerSocket serverSocket = new ServerSocket(1024);
            System.out.println("Server is running...");

            while (true) {
                Socket socket = serverSocket.accept();
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                String receiveMessage = in.readLine();
                System.out.println("Received Message: " + receiveMessage);

                PrintWriter out = new PrintWriter(socket.getOutputStream());
                out.println("This data is from server");
                out.flush();
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
