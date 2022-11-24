import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;


public class MenuController implements Initializable {

    @FXML
    private Button btn_addNewChat;
    @FXML
    private VBox vBox_Menu;
    @FXML
    private Label Label_userName;


    @Override
    public void initialize(URL location, ResourceBundle resources) {

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

    public void loadStage(String fxml, String ID){
        try{
            Parent root = FXMLLoader.load(getClass().getResource(fxml));
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
//            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle(ID);
            stage.show();
            if(fxml.equals("chat.fxml")){
                ChatController.setID(ID);
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
