import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.*;

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
                checkName(s,str[1]);
            }else if (str[0].equals("NewAC")) {
                newUser(s, str[1],str[2]);
            } else if (str[0].startsWith("/")) {
                receiveImage(str[0]);
            } else {
                System.out.println("wong message type");
            }

            chatServer.close();
        }

    }

    public static void receiveImage(String file) {
        try {
            BufferedImage sendImage = ImageIO.read(new File(file));
            System.out.println(sendImage);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void noteClientIP(String username, String clientIP) throws IOException, ParseException {
        //Add username and clientIP to JSON
        JSONParser jsonParser = new JSONParser();
        FileReader reader = new FileReader("./login.json");

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
                FileWriter file = new FileWriter("./login.json");
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
//        Runnable runnable = () -> {
//            try {
//                Socket socket = new Socket("127.0.0.1", 236);
//                DataOutputStream out = new DataOutputStream(socket.getOutputStream());
//
//                out.writeUTF(message);
//                out.flush();
//                System.out.println(senderName + "    " + message + "    " + receiveName);
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            }
//        };
//        Thread t = new Thread(runnable);
//        t.start();
                System.out.println(senderName + "    " + message + "    " + receiveName);
    }

    public static boolean checkLogin(Socket s, String username, String passwd) throws IOException, ParseException {
        DataOutputStream out = new DataOutputStream(s.getOutputStream());
        JSONParser jsonParser = new JSONParser();
        FileReader reader = new FileReader("./user.json");
        Object obj = jsonParser.parse(reader);
        JSONArray array = (JSONArray) obj;
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
    public static boolean checkName(Socket s, String username) throws IOException, ParseException {
        DataOutputStream out = new DataOutputStream(s.getOutputStream());
        JSONParser jsonParser = new JSONParser();
        FileReader reader = new FileReader("./user.json");
        Object obj = jsonParser.parse(reader);
        JSONArray array = (JSONArray) obj;
        String foundData = "f";
        for (int i=0;i<array.size();i++){
            JSONObject user = (JSONObject) array.get(i);
            String name = (String) user.get("name");
            if(username.equals(name)) {
                foundData = "t";
                break;
            }
        }
        if(foundData.equals("t")) {
            out.writeUTF("true");
            out.flush();
            System.out.println("have user");
            return true;
        } else {
            out.writeUTF("false");
            out.flush();
            System.out.println("No user");
            return false;
        }
    }
    public static void newUser(Socket s, String username, String password) throws IOException, ParseException {
        //Add username and clientIP to JSON
        DataOutputStream out = new DataOutputStream(s.getOutputStream());
        JSONParser jsonParser = new JSONParser();
        FileReader reader = new FileReader("./user.json");

        Object obj = jsonParser.parse(reader);
        JSONArray array = (JSONArray) obj;
        String foundData3 = "f";
        for (int i=0;i<array.size();i++){
            JSONObject login = (JSONObject) array.get(i);
            String name = (String) login.get("name");
            String pswd = (String) login.get("password");
            if(username.equals(name)) {
                foundData3 = "t";
                break;
            }
        }
        if(foundData3.equals("f")) {
            JSONObject newObj = new JSONObject();
            newObj.put("name", username);
            newObj.put("password", password);
            array.add(newObj);
            try {
                FileWriter file = new FileWriter("./user.json");
                file.write(array.toJSONString());
                file.flush();
                file.close();
                out.writeUTF("true");
                out.flush();
            }catch (IOException e){
                e.printStackTrace();
            }
        } else {
            out.writeUTF("false");
            out.flush();
        }

        System.out.println(username + password);
    }
}
