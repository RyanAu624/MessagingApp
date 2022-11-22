import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
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


public class LoginController implements Initializable {
    @FXML
    private TextField inputID;
    @FXML
    private Button btnUserEnter;
    @FXML
    private VBox vBox_Login;
    @FXML
    private HBox hbox_userData;
    @FXML
    private HBox hbox_Password;
    @FXML
    private PasswordField InputPassword;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        btnUserEnter.setOnMouseClicked(event -> {
            System.out.println("Clicked");
            //Send Detail to Server
            try {
                String stateForCheck = checkLogin();
                if(stateForCheck.equals("true")){
                   // screenController.activate("menu");
                    System.out.println("TRUE");
                    loadStage("menu.fxml");
                    closeStage();
                }else{
                    Alert a = new Alert(Alert.AlertType.NONE);
                    a.setAlertType(Alert.AlertType.WARNING);
                    a.setContentText("ID or Password is WONG!");
                    a.show();
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }    });
    }
    public String checkLogin() throws IOException {
        String ip = "127.0.0.1";
        int port = 235;
        Socket loginSocket = new Socket(ip, port);

        InetAddress myIp = InetAddress.getLocalHost();
        String myIPStr = myIp.getHostAddress();

        String inputedId = inputID.getText();
        String inputedPassword = InputPassword.getText();

        //send user input data to server
        DataOutputStream out = new DataOutputStream(loginSocket.getOutputStream());
        out.writeUTF("login " + inputedId + " " + inputedPassword + " " + myIPStr);

        out.flush();

        //receive true or false from server
        DataInputStream dis = new DataInputStream(loginSocket.getInputStream());
        String str = dis.readUTF();
        System.out.println(str);
        loginSocket.close();
        return(str);
    }

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

    public void closeStage(){
        Stage stage = (Stage) btnUserEnter.getScene().getWindow();
        stage.close();
    }

}
