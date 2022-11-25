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

//    private static String talkTo="";
    @FXML
    public void initialize(URL location, ResourceBundle resources) {

        children = messagePane.getChildren();

        messagePane.heightProperty().addListener(event -> {
            scrollPane.setVvalue(1);
        });

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
        });

//        Runnable runnable = () -> {
//            while (true) {
//                try {
//                    ServerSocket chatServer = new ServerSocket(1234);
//                    Socket s = chatServer.accept();
//                    System.out.println("Connected");
//                    DataInputStream dis = new DataInputStream(s.getInputStream());
//                    String receivedMessage = dis.readUTF();
//                    System.out.println("Receive " + receivedMessage);
//
//                    displayReceiveMessage(receivedMessage);
//                } catch (IOException e) {
//                    throw new RuntimeException(e);
//                }
//            }
//        };
//        Thread t = new Thread(runnable);
//        t.start();
    }

    private void getMessageHistory() {

    }

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

                Sender sender = new Sender();
                sender.sendMessage(text);
//                Runnable runnable = () -> {
//                    try {
//                        Socket socket = new Socket("127.0.0.1", 235);
//                        DataOutputStream out = new DataOutputStream(socket.getOutputStream());
//
//                        String receiveName = getReceiveName();
//
//                        out.writeUTF("chat " + "senderName" + " " + receiveName + " " + text);
//                        out.flush();
//
//                    } catch (IOException e) {
//                        throw new RuntimeException(e);
//                    }
//                };
//                Thread t = new Thread(runnable);
//                t.start();
            }
        });

    }

//    public static void setID(String receiveName){
//         talkTo = receiveName;
//    }
//
//    public String getReceiveName(){
//        return talkTo;
//    }
}
