import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;

public class Sender {

    private static String talkTo="";

    private static String sender;

    private static String historyFileName="";
    public void sendMessage(String message) {
        try {
            Socket socket = new Socket("127.0.0.1", 235);
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());

            String receiveName = getTalkTo().replaceAll("\\s+","");
            String senderName = getSender();

            JSONParser jsonParser = new JSONParser();
            File f = new File("./src/chatHistory/"+receiveName+"With"+senderName+".json");
            if (f.exists()) {
                f = new File("./src/chatHistory/"+receiveName+"With"+senderName+".json");
            }else{
                f = new File("./src/chatHistory/" + senderName + "With" + receiveName + ".json");
            }
            FileReader reader = new FileReader(f);
            Object obj = jsonParser.parse(reader);
            JSONArray array = (JSONArray) obj;
            JSONObject newObj = new JSONObject();
            newObj.put("sender", senderName);
            newObj.put("text", message);
            array.add(newObj);
            try {
                FileWriter file = new FileWriter(f);
                file.write(array.toJSONString());
                file.flush();
                file.close();
            }catch (IOException e){
                e.printStackTrace();
            }

            out.writeUTF("chat " + senderName + " " + receiveName + " " + message);
            out.flush();

        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ParseException e) {
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

    public static String getSender() {
        return sender;
    }

    public static void setSender(String sender) {
        Sender.sender = sender;
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
