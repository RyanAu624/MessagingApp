import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class MenuController implements Initializable {
    ObservableList<Node> children;

    @FXML
    private Pane Pane_Menu;
    @FXML
    private Button btn_addNewChat;
    @FXML
    private VBox Vbox_Menu;
    @FXML
    private Label Label_userName;

    private String userID = LoginController.getUserID();
    private ArrayList<String> chatList = new ArrayList<String>(); // list of files.
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Label_userName.setText(userID);
        children = Vbox_Menu.getChildren();
        File dir = new File("./src/chatHistory"); // directory = target directory.
        if(dir.exists()) // Directory exists then proceed.
        {
            Pattern p = Pattern.compile(userID); //
            for(File f : dir.listFiles())
            {
                if(!f.isFile()) continue;
                try{
                    String fileName = f.getName();
                    Matcher m = p.matcher(fileName);
                    if(m.find()){
                        chatList.add(f.getName()); // add file to found-keyword list.
                }}catch(Exception e){
                    System.out.print("\n\t Error processing file : "+f.getName());
                }
            }

            System.out.print("\n\t List : "+chatList); // list of files containing keyword.
        } // IF directory exists then only process.
        else
        {
            System.out.print("\n Directory doesn't exist.");
        }

        //fileNameWithOutExt = "test.xml".replaceFirst("[.][^.]+$", "");
        Platform.runLater(() -> {
            showChatHistory();
        });



        btn_addNewChat.setOnMouseClicked(event -> {
            System.out.println("Clicked");
            TextInputDialog dialog = new TextInputDialog("");
            dialog.setTitle("Chat with who");
            dialog.setHeaderText("OPEN NEW CHAT ROOM");
            dialog.setContentText("Please enter ID here:");
            String correctIDOrNot="false";
            // Traditional way to get the response value.
            Optional<String> result = dialog.showAndWait();
            String inputedID = dialog.getEditor().getText();
            if (result.isPresent()) {
                //send socket to server
                try {
                    correctIDOrNot = askForCreateChatRoom(inputedID);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                // if(inputedID.equals("aaa")){
//                    System.out.println("True");
//                    correctIDOrNot = "true";
//                }else{
//                    Alert a = new Alert(Alert.AlertType.NONE);
//                    a.setAlertType(Alert.AlertType.WARNING);
//                    a.setContentText("ID  is WRONG!");
//                    a.show();
//                }

                //if true, open new chat room
                if (correctIDOrNot.equals("true")) {
                    loadStage("chat.fxml", inputedID);
                } else {
                    Alert a = new Alert(Alert.AlertType.NONE);
                    a.setAlertType(Alert.AlertType.WARNING);
                    a.setContentText("ID  is WRONG!");
                    a.show();
                }
            }
        });
    }

    private void showChatHistory() {
        for(int i=0; i<chatList.size();i++){
            String historyName = chatList.get(i).replaceFirst("[.][^.]+$", "");
            String[] involedName= historyName.split("With");
            String chatroomName = "";
            for(int h=0; h<involedName.length;h++){
                if(!(involedName[h].equals(userID))){
                    chatroomName += involedName[h]+" ";
                }
            }
            System.out.println("The name is "+chatroomName);
            Button btn_chatHistory =  new Button();
            btn_chatHistory.setText("chatroom: "+chatroomName);
            btn_chatHistory.setPrefWidth(270.0);
            btn_chatHistory.setPrefHeight(43.0);
            btn_chatHistory.setStyle("-fx-font-size:22");

            String finalChatroomName = chatroomName;
            int finalI = i;
            btn_chatHistory.setOnMouseClicked(event -> {
                System.out.println("Clicked");
                loadStageForOldChat("chat.fxml", finalChatroomName,chatList.get(finalI));
            });
            Vbox_Menu.getChildren().add(btn_chatHistory);
        }
    }

    public void loadStage(String fxml, String ID){
        try{
            Parent root = FXMLLoader.load(getClass().getResource(fxml));
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
//            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle(ID);
            stage.show();
            if(fxml.equals("chat.fxml")){

                Sender sender = new Sender();
                sender.setTalkTo(ID);
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public void loadStageForOldChat(String fxml, String ID,String fileName){
        try{
            Parent root = FXMLLoader.load(getClass().getResource(fxml));
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
//            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle(ID);
            if(fxml.equals("chat.fxml")){
                ChatController.setHistoryFileName(fileName);
                ChatController.setWhotoReceive(ID);
                Sender sender = new Sender();
                sender.setTalkTo(ID);

            }
            stage.show();

            System.out.println(fileName);
            if(fxml.equals("chat.fxml")){
                ChatController.setHistoryFileName(fileName);
                ChatController.setWhotoReceive(ID);
                Sender sender = new Sender();
                sender.setTalkTo(ID);

            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    private String askForCreateChatRoom(String inputedID) throws IOException {
        String ip = "127.0.0.1";
        int port = 235;
        Socket createSocket = new Socket(ip, port);

//        InetAddress myIp = InetAddress.getLocalHost();
//        String myIPStr = myIp.getHostAddress();

        //send user input data to server
        DataOutputStream out = new DataOutputStream(createSocket.getOutputStream());
        out.writeUTF("createChatRoom " + inputedID);
        out.flush();

        //receive true or false from server
        DataInputStream dis = new DataInputStream(createSocket.getInputStream());
        String str = dis.readUTF();
        System.out.println(str);
        createSocket.close();
        return(str);
    }

}
