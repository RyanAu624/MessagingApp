import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.imageio.IIOException;
import javax.security.sasl.SaslServer;
import java.io.*;
import java.net.*;
import java.util.HashMap;
import java.util.Map;

public class ServerV2 {

    private DatagramSocket datagramSocket;

    private byte[] buffer = new byte[256];

    public ServerV2(DatagramSocket datagramSocket) {
        this.datagramSocket = datagramSocket;
    }


    public static void main(String[] args) throws IOException, ParseException {
       byte[] buffer = new byte[1024];
       System.out.println("Server started");
       while(true){
            ServerSocket chatServer = new ServerSocket(235);
            Socket s = chatServer.accept();
            System.out.println("Connected");
            DataInputStream dis = new DataInputStream(s.getInputStream());
            String[] str = dis.readUTF().split(" ");

            if(str[0].equals("login")) {
                boolean loginState = checkLogin(s, str[1], str[2]);
                if(loginState == true) {
                    noteClientIP(str[1], str[3]);
                }
            }else if (str[0].equals("chat")) {
                receiveThenSend(s, str[1], str[2], str[3]);
            } else if (str[0].equals("createChatRoom")) {


            }else if (str[0].equals("NewAC")) {
                

            }else {
                System.out.println("wong message type");
            }

            chatServer.close();
        }

    }

    public static void noteClientIP(String username, String clientIP) throws IOException, ParseException {
        //Add username and clientIP to JSON
        JSONParser jsonParser = new JSONParser();
        FileReader reader = new FileReader("./src/login.json");

        Object obj = jsonParser.parse(reader);
        JSONArray array = (JSONArray) obj;
        String foundData2 = "f";
        for (int i=0;i<array.size();i++){
            JSONObject login = (JSONObject) array.get(i);
            String name = (String) login.get("name");
            String ip = (String) login.get("ip");
            if(username.equals(name) && clientIP.equals(ip)) {
                foundData2 = "t";
                break;
            }
        }
        if(foundData2.equals("f")) {
            JSONObject newObj = new JSONObject();
            newObj.put("name", username);
            newObj.put("ip", clientIP);
            array.add(newObj);
            try {
                FileWriter file = new FileWriter("./src/login.json");
                file.write(array.toJSONString());
                file.flush();
                file.close();
            }catch (IOException e){
                e.printStackTrace();
            }
        } else {

        }

        System.out.println(username + clientIP);
    }

    public static void receiveThenSend(Socket s, String senderName, String receiveName, String message) throws IOException {
//        DataOutputStream out = new DataOutputStream(s.getOutputStream());
//        out.writeUTF(message);
//        out.flush();
        System.out.println(senderName + "    " + message + "    " + receiveName);
    }

    public static boolean checkLogin(Socket s, String username, String passwd) throws IOException, ParseException {
        DataOutputStream out = new DataOutputStream(s.getOutputStream());
        JSONParser jsonParser = new JSONParser();
        FileReader reader = new FileReader("./src/user.json");
        Object obj = jsonParser.parse(reader);
        JSONObject userjsonobj = (JSONObject) obj;
        JSONArray array = (JSONArray) userjsonobj.get("user");
        String foundData = "f";
        for (int i=0;i<array.size();i++){
            JSONObject user = (JSONObject) array.get(i);
            String name = (String) user.get("name");
            String password = (String) user.get("password");
            if(username.equals(name) && passwd.equals(password)) {
                foundData = "t";
                break;
            }
        }
        if(foundData.equals("t")) {
            out.writeUTF("true");
            out.flush();
            System.out.println("logined");
            return true;
        } else {
            out.writeUTF("false");
            out.flush();
            System.out.println("Fail to login");
            return false;
        }
    }

}
