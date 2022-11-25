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
    @FXML
    private Button btn_signUp;

    private static String userID= "";
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

        btn_signUp.setOnMouseClicked(event -> {
            System.out.println("Clicked");

            TextInputDialog dialog = new TextInputDialog("");
            dialog.setTitle("Sign UP");
            dialog.setHeaderText("What is yourID");
            dialog.setContentText("Please enter here:");
            String newID="false";
            // Traditional way to get the response value.
            Optional<String> result = dialog.showAndWait();
            String inputedNewID = "";
            String inputedNewPW = "";
            // Traditional way to get the response value.
            if (result.isPresent()) {
                inputedNewID = dialog.getEditor().getText();
                System.out.println("This is the inputedNEWID: " + inputedNewID);
            }

            if(!inputedNewID.equals("")) {
                TextInputDialog dialogPw = new TextInputDialog("");
                dialogPw.setTitle("Sign UP");
                dialogPw.setHeaderText("What is your Password");
                dialogPw.setContentText("Please enter here:");
                String newPassword = "false";
                Optional<String> resultPw = dialogPw.showAndWait();

                if (resultPw.isPresent()) {
                    inputedNewPW = dialogPw.getEditor().getText();
                    System.out.println("This is the inputedNEWID: " + inputedNewPW);
                }
            }
                   if(!(inputedNewID.equals(""))&&!(inputedNewPW.equals(""))){
                    //send socket to server
                    try {
                        String createdNewAC = createOrNot(inputedNewID,inputedNewPW);
                        if(createdNewAC.equals("true")){
                            Alert a = new Alert(Alert.AlertType.NONE);
                            a.setAlertType(Alert.AlertType.INFORMATION);
                            a.setContentText("Created");
                            a.show();
                        }else{
                            Alert a = new Alert(Alert.AlertType.NONE);
                            a.setAlertType(Alert.AlertType.WARNING);
                            a.setContentText("Cannot Create!");
                            a.show();
                        }
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
        });
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
        if(str.equals("true")){
            setUserID(inputedId);
        }
        loginSocket.close();
        return(str);
    }

    public String createOrNot(String newID, String newPW) throws IOException {
        String ip = "127.0.0.1";
        int port = 235;
        Socket loginSocket = new Socket(ip, port);

//        InetAddress myIp = InetAddress.getLocalHost();
//        String myIPStr = myIp.getHostAddress();

        String inputedId = newID;
        String inputedPW = newPW;

        //send user input data to server
        DataOutputStream out = new DataOutputStream(loginSocket.getOutputStream());
        out.writeUTF("NewAC " + inputedId + " " + inputedPW);

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

    public void setUserID(String userID){
        this.userID = userID;
    }

    public static String getUserID(){
        return userID;
    }

}
