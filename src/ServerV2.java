import javax.imageio.IIOException;
import javax.security.sasl.SaslServer;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.*;

public class ServerV2 {

    private DatagramSocket datagramSocket;

    private byte[] buffer = new byte[256];

    public ServerV2(DatagramSocket datagramSocket) {
        this.datagramSocket = datagramSocket;
    }


    public static void main(String[] args) throws IOException {
       byte[] buffer = new byte[1024];
       System.out.println("Server started");
       while(true){
            ServerSocket chatServer = new ServerSocket(235);
            Socket s = chatServer.accept();
            System.out.println("Connected");
            DataInputStream dis = new DataInputStream(s.getInputStream());
            String[] str = dis.readUTF().split(" ");
            //dis.close();

            if(str[0].equals("login")) {
               // for (int i = 0; i < str.length; i++)
                    //System.out.println("message=" + str[i]);
                 if(str[1].equals("aaa")&&str[2].equals("123456")){
                     DataOutputStream out = new DataOutputStream(s.getOutputStream());
                     out.writeUTF("true");
                     out.flush();
                     System.out.print("logined");
                 }else {
                     DataOutputStream out = new DataOutputStream(s.getOutputStream());
                     out.writeUTF("false");
                     out.flush();
                     System.out.println("Fail to login");
                 }
                 chatServer.close();
            }else if (str[0].equals("chat")){

            }else {
                System.out.println("wong message type");
            }
                chatServer.close();
        }

    }


}
