package main;

import SoinsInfirmiers.Data;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.util.Arrays;

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
            Parent root = FXMLLoader.load(getClass().getResource("/ui/FXML/HomePage.fxml"));
            primaryStage.setTitle(Data.homePageTitle);
            primaryStage.setScene(new Scene(root, 1280, 720));
            primaryStage.setResizable(false);
            primaryStage.show();
            primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/img/graphique.png")));
        } catch (Exception e) {
            e.printStackTrace();
            String e1 = e.toString();
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Erreur");
            alert.setHeaderText(Arrays.toString(e.getStackTrace()));
            alert.setContentText(e1);
            alert.showAndWait();
        }

    }

    public static void main(String[] args) {
        launch(args);
    }
}
