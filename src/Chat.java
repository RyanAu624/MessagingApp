import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.*;
import java.net.DatagramSocket;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

public class Chat implements Initializable {
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
//
//    @Override
//    public void start(Stage primaryStage) throws IOException {
//        FXMLLoader loader = new FXMLLoader(getClass().getResource("chat.fxml"));
//        loader.setController(this);
//        Parent root = loader.load();
//        primaryStage.setScene(new Scene(root));
//        primaryStage.setTitle("Chat");
//        primaryStage.setMinWidth(300);
//        primaryStage.setMinHeight(500);
//        primaryStage.show();
//    }

    @FXML
    public void initialize(URL location, ResourceBundle resources) {

//        Thread t = new Thread(() -> {
//            try {
//                Socket socket = new Socket("localhost", 1024);
//
//                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
//
//                String receivedMessage = in.readLine();
//                children.add(serverMessageNode(receivedMessage));
//                msgIndex = (msgIndex + 1) % 2;
//
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            }
//        });
//        t.start();

        children = messagePane.getChildren();

        messagePane.heightProperty().addListener(event -> {
            scrollPane.setVvalue(1);
        });

        txtInput.setOnKeyPressed(event -> {
            if (event.getCode().toString().equals("ENTER"))
                displaySenderMessage();

        });

        btnFile.setOnMouseClicked(event -> {
        });
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

            if (!text.isEmpty()) {
                txtInput.clear();
                children.add(serverMessageNode(text));
                msgIndex = (msgIndex + 1) % 2;
            }
        });
    }

    private void displaySenderMessage() {
        Platform.runLater(() -> {
            String text = txtInput.getText();
            if (!text.isEmpty()) {
                txtInput.clear();
                children.add(clientMessageNode(text));
                msgIndex = (msgIndex + 1) % 2;
                try {
                    Socket socket = new Socket("127.0.0.1", 235);

//                    PrintWriter out = new PrintWriter(socket.getOutputStream());
//
//                    out.println(text);
//                    out.flush();

                    DataOutputStream out = new DataOutputStream(socket.getOutputStream());
                    out.writeUTF("chat " + "senderNameee" + " " + "receiveNameee" + " " + text);

                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });

    }

//    public static void main(String[] args) {
//        launch(args);
//    }
}
