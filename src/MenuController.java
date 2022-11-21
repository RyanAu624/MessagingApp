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
            loadStage("chat.fxml");
            //Send Detail to Server
//            try {
//                String stateForCheck = checkLogin();
//                if(stateForCheck.equals("true")){
//                    // screenController.activate("menu");
//                    System.out.println("TRUE");
//                    loadStage("menu.fxml");
//                    closeStage();
//                }else{
//                    Alert a = new Alert(Alert.AlertType.NONE);
//                    a.setAlertType(Alert.AlertType.WARNING);
//                    a.setContentText("ID or Password is WONG!");
//                    a.show();
//                }
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//           }
        });
    }
//    public String checkLogin() throws IOException {
//        String ip = "127.0.0.1"; // ip for server
//        //myIp = InetAddress.getLocalHost();
//        //myPort = 666;
//        int port = 235; // how to get port?
//        String inputedId = inputID.getText();
//        String inputedPassword = InputPassword.getText();
//        Socket loginSocket = new Socket(ip, port);
//        //send user input data to server
//        DataOutputStream out = new DataOutputStream(loginSocket.getOutputStream());
//        out.writeUTF("login "/*+ myIp.getHostAddress()+" "+myPort+" "*/+inputedId+" "+inputedPassword);
//
//        out.flush();
//
//
//        //receive true or false from server
//        DataInputStream dis = new DataInputStream(loginSocket.getInputStream());
//        String str = dis.readUTF();
//        System.out.println(str);
//        loginSocket.close();
//        return(str);
//
//    }

    public void loadStage(String fxml){
        try{
            Parent root = FXMLLoader.load(getClass().getResource(fxml));
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
//            stage.initModality(Modality.APPLICATION_MODAL);
            stage.show();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

//    public void closeStage(){
//        Stage stage = (Stage) btnUserEnter.getScene().getWindow();
//        stage.close();
//    }

}
