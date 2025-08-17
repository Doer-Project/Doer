package app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
//        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/LoginPage.fxml"));
//        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/household/HouseHoldDB.fxml"));
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/worker/WorkerDB.fxml"));
//        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/RegisterPage.fxml"));
//        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/HouseHoldDB.fxml"));
        Parent root = loader.load();
        primaryStage.setTitle("DOER");
        primaryStage.setScene(new Scene(root, 1400, 800));
//        primaryStage.setScene(new Scene(root, 1400, 900));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
