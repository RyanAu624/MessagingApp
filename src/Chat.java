import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
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

import java.io.IOException;
import java.net.DatagramSocket;

public class Chat extends Application {
    ObservableList<Node> children;
    int msgIndex = 0;

    private String ip = "127.0.0.1";
    private int port = 1234;

    DatagramSocket socket;

    @FXML
    private ScrollPane scrollPane;
    @FXML
    private TextField txtInput;
    @FXML
    private Button btnFile;
    @FXML
    private VBox messagePane;


    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Chat.fxml"));
        loader.setController(this);
        Parent root = loader.load();
        primaryStage.setScene(new Scene(root));
        primaryStage.setTitle("Chat");
        primaryStage.setMinWidth(300);
        primaryStage.setMinHeight(500);
        primaryStage.show();
    }

    @FXML
    protected void initialize() {
        children = messagePane.getChildren();

        messagePane.heightProperty().addListener(event -> {
            scrollPane.setVvalue(1);
        });

        txtInput.setOnKeyPressed(event -> {
            if (event.getCode().toString().equals("ENTER"))
                displayMessage();

        });

        btnFile.setOnMouseClicked(event -> {
        });
    }

    private Node clientMessageNode(String text) {
//        HBox box = new HBox();
//        box.paddingProperty().setValue(new Insets(10, 10, 10, 10));
//
//        if (alignToRight)
//            box.setAlignment(Pos.BASELINE_RIGHT);
//        javafx.scene.control.Label label = new Label(text);
//        label.setWrapText(true);
//        box.getChildren().add(label);
//        return box;

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

    private void displayMessage() {
        Platform.runLater(() -> {
            String text = txtInput.getText();
            if (!text.isEmpty()) {
                txtInput.clear();
                children.add(clientMessageNode(text));
                msgIndex = (msgIndex + 1) % 2;
            }
        });

    }

    public static void main(String[] args) {
        launch(args);
    }
}
