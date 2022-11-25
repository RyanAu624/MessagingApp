import com.sun.xml.internal.bind.v2.model.core.ID;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import javafx.stage.FileChooser;
import javafx.stage.Stage;


import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ResourceBundle;

public class ChatController implements Initializable {
    ObservableList<Node> children;
    int msgIndex = 0;
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private TextField txtInput;
    @FXML
    private Button btnFile;
    @FXML
    private VBox messagePane;

    private static String talkTo="";
    private static String historyFileName ="";
    private Sender sender;

    @FXML
    public void initialize(URL location, ResourceBundle resources) {

        sender = new Sender();

        children = messagePane.getChildren();

        talkTo = sender.getTalkTo();
        System.out.println("Receive"+talkTo);
        messagePane.heightProperty().addListener(event -> {
            scrollPane.setVvalue(1);
        });

        //display history in start
        JSONParser jsonParser = new JSONParser();
        try{
            FileReader reader = new FileReader("./src/chatHistory/"+sender.getHistoryFileName());
            Object obj = jsonParser.parse(reader);
            JSONArray array = (JSONArray) obj;
            for (int i=0;i<array.size();i++) {
                JSONObject textObject = (JSONObject) array.get(i);
                String sender = (String) textObject.get("sender");
                System.out.println("Sender is "+sender);
                String oldmessage = (String) textObject.get("text");
                if (sender.equals(LoginController.getUserID())) {
                    displayReceiveMessage(oldmessage);
                }else{
                    displaySenderOlsMessage(oldmessage);
                }
            }
        }catch(Exception e){
            System.out.println(e);
        }


        txtInput.setOnKeyPressed(event -> {
            if (event.getCode().toString().equals("ENTER"))
                displaySenderMessage();
        });

        btnFile.setOnMouseClicked(event -> {
            FileChooser chooser = new FileChooser();
            Stage stage = new Stage();
            File file = chooser.showOpenDialog(stage);

            if (file != null) {
//            dataOutputStream.writeUTF(file.getPath());
                String path = file.getPath();
                System.out.println("selected");
                System.out.println(file.getPath());

                sender.sendFile(path);

            }
        });

//        Runnable runnable = () -> {
//            Receiver receiver = new Receiver();
//            String message;
//            while (true) {
//                message = receiver.receiveMessage();
//
//                if (message.equals("")) {
//                    displayReceiveMessage(message);
//                }
//            }
//        };
//        Thread t = new Thread(runnable);
//        t.start();
    }

    //display history sender mehtods
    private Node clientMessageNode(String text) {
        HBox hbox = new HBox();
        hbox.setAlignment(Pos.CENTER_RIGHT);
        hbox.setPadding(new Insets(5, 5, 5, 10));

        javafx.scene.control.Label label = new Label(text);
        label.setStyle("-fx-background-color: #3461E6");
        label.setTextFill(Color.color(0.934, 0.945, 0.996));
        label.setPadding(new Insets(5, 10, 5, 10));
        label.setWrapText(true);
        hbox.getChildren().add(label);

        return hbox;
    }


    private void displaySenderOlsMessage(String oldtext) {
        Platform.runLater(() -> {
            children.add(clientMessageNode(oldtext));
            msgIndex = (msgIndex + 1) % 2;
        });
    }

    private Node serverMessageNode(String text) {
        HBox hbox = new HBox();
        hbox.setAlignment(Pos.CENTER_LEFT);
        hbox.setPadding(new Insets(5, 10, 5, 5));

        javafx.scene.control.Label label = new Label(text);
        label.setStyle("-fx-background-color: #FFFFFF");
        label.setPadding(new Insets(5, 10, 5, 10));
        label.setWrapText(true);
        hbox.getChildren().add(label);

        return hbox;
    }

    public void displayReceiveMessage(String text) {
        Platform.runLater(() -> {
            children.add(serverMessageNode(text));
            msgIndex = (msgIndex + 1) % 2;
        });
    }



    private void displaySenderMessage() {
        Platform.runLater(() -> {
            String text = txtInput.getText();
            if (!text.isEmpty()) {
                txtInput.clear();
                children.add(clientMessageNode(text));
                msgIndex = (msgIndex + 1) % 2;

            Runnable run = () -> {
                sender.sendMessage(text);
            };
            Thread t2 = new Thread(run);
            t2.start();
            }
        });
    }




}
