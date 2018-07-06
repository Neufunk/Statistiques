package main;

import SoinsInfirmiers.Data;
import javafx.application.Application;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application {

    private static Stage primaryStage;


    private void setPrimaryStage(Stage stage) {
        Main.primaryStage = stage;
    }

    static public Stage getPrimaryStage() {
        return Main.primaryStage;
    }

    @Override
    public void start(Stage primaryStage) {
        try {
            setPrimaryStage(primaryStage);
            primaryStage.setTitle(Data.homePageTitle);
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/FXML/HomePage.fxml"));
            Parent root = loader.load();
            primaryStage.setScene(new Scene(root, 1280, 720));
            primaryStage.setResizable(true);
            primaryStage.show();
            primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/img/graphique.png")));
            } catch (Exception e) {
            ExceptionHandler.switchException(e, this.getClass());
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
