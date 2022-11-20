import javafx.application.Application;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import javafx.stage.Stage;


    public class App extends Application {

        @Override
        public void start(Stage primaryStage) throws Exception{
            Parent root = FXMLLoader.load(getClass().getResource("login.fxml"));
            primaryStage.setScene(new Scene(root));
            primaryStage.setTitle("Login Page");
            primaryStage.setMinWidth(300);
            primaryStage.setMinHeight(500);
            primaryStage.show();
        }

        //you can download the glyph browser - link provided.
        public static void main(String[] args) {
            launch(args);
        }
    }
