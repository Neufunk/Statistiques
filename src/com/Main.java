package com;

import SoinsInfirmiers.Strings;
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
    public void start(Stage primaryStage) throws Exception{
        setPrimaryStage(primaryStage);
        Parent root = FXMLLoader.load(getClass().getResource("FXML/HomePage.fxml"));
        primaryStage.setTitle(Strings.homePageTitle);
        primaryStage.setScene(new Scene(root, 1280, 720));
        primaryStage.setResizable(false);
        primaryStage.show();
        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/resources/img/graphique.png")));
    }

    public static void main(String[] args) {
        launch(args);
    }
}
