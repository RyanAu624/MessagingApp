import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Receiver {

    public String receiveMessage() {
        String receivedMessage;
        try {
            ServerSocket chatServer = new ServerSocket(1234);
            Socket s = chatServer.accept();
            System.out.println("Connected");
            DataInputStream dis = new DataInputStream(s.getInputStream());
            receivedMessage = dis.readUTF();
            System.out.println("Receive " + receivedMessage);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return receivedMessage;
    }

}
