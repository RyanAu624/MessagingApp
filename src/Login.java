import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.Scene;


import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import javax.swing.JOptionPane;

import static javafx.application.Application.launch;

public class Login extends Application {

    ObservableList<Node> children;
    @FXML
    private TextField inputID;
    @FXML
    private Button btnUserEnter;
    @FXML
    private VBox vBox_login;
    @FXML
    private HBox hbox_userData;
    @FXML
    private HBox hbox_Password;
    @FXML
    private PasswordField InputPassword;

    public static boolean loginedOrNot = false;
    Socket socket;
    BufferedReader read;
    PrintWriter output;
    InetAddress myIp ;
    int myPort =666; // start program to random a number?

    public void start(Stage primaryStage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("login.fxml"));
        loader.setController(this);
        Parent root = loader.load();
        primaryStage.setScene(new Scene(root));
        primaryStage.setTitle("Login Page");
        primaryStage.setMinWidth(300);
        primaryStage.setMinHeight(500);
        primaryStage.show();

        /*Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Please input!");*/
    }

    @FXML
    protected void initialize() {
       // children = hbox_Password.getChildren();


        btnUserEnter.setOnMouseClicked(event -> {
            System.out.println("Clicked");
            //System.out.println(inputID.getText());
            //System.out.println(InputPassword.getText());
            //Send Detail to Server
            try {
                checkLogin();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            //loginedOrNot = true;
           /* if(loginedOrNot == true){
                Program program = new Program("testing");
                program.start();
                //System.exit(0);
            }*/
        });
    }

   /* public static Boolean getLoginedState(){
        return loginedOrNot;
    }*/
    public void checkLogin() throws IOException {
        String ip = "127.0.0.1"; // ip for server
        myIp = InetAddress.getLocalHost();
        //myPort = 666;
        int port = 235; // how to get port?
        String inputedId = inputID.getText();
        String inputedPassword = InputPassword.getText();
        Socket loginSocket = new Socket(ip, port);
        //send user input data to server
        DataOutputStream out = new DataOutputStream(loginSocket.getOutputStream());
        out.writeUTF("login "+ myIp.getHostAddress()+" "+myPort+" "+inputedId+" "+inputedPassword);
        out.flush();


        //receive true or false from server
        DataInputStream dis = new DataInputStream(loginSocket.getInputStream());
        String str = dis.readUTF();
        System.out.println(str);
        loginSocket.close();
    }

    public static void main(String[] args) {
            launch(args);
    }
}
