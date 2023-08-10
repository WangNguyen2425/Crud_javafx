package Java;

import Java.data.DBConnection;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class App  extends Application {
        public void start(Stage stage) throws IOException {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/res/layout/teacher.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 1200, 840);
            stage.setTitle("Teacher Management!");
            stage.setScene(scene);
            stage.show();

        }

        public static void main(String[] args) {
              launch();

        }
}

