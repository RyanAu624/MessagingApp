import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class Sender {

    private static String talkTo="";

    private static String historyFileName="";
    public void sendMessage(String message) {
        try {
            Socket socket = new Socket("127.0.0.1", 235);
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());

            String receiveName = getTalkTo();

            out.writeUTF("chat " + "senderName" + " " + receiveName + " " + message);
            out.flush();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendFile(String path) {
        try {
            Socket socket = new Socket("127.0.0.1", 235);
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());

            out.writeUTF(path.trim());
            out.flush();
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String getTalkTo() {
        return talkTo;
    }

    public static void setTalkTo(String talkTo) {
        Sender.talkTo = talkTo;
    }

    public static String getHistoryFileName() {
        return historyFileName;
    }

    public static void setHistoryFileName(String historyFileName) {
        Sender.historyFileName = historyFileName;
    }
}
