package main;

import javafx.fxml.FXML;
import javafx.scene.control.ProgressBar;

import java.awt.*;
import java.sql.Connection;
import java.sql.DriverManager;

public class ConnectionTest {

    @FXML
    private ProgressBar progressBar1;

    @FXML
    private ProgressBar progressBar2;

    @FXML
    private ProgressBar progressBar3;

    @FXML
    private ProgressBar progressBar4;

    @FXML
    private ProgressBar progressBar5;

    @FXML
    private Label done1;

    @FXML
    private Label done2;

    @FXML
    private Label done3;

    @FXML
    private Label done4;

    @FXML
    private Label done5;

    @FXML
    private Label failed1;

    @FXML
    private Label failed2;

    @FXML
    private Label failed3;

    @FXML
    private Label failed4;

    @FXML
    private Label failed5;

    public void onButtonClick(){

    }

    private void databaseConnection(String url, String user, String passwd, ProgressBar progressBar, Label done, Label failed){
        progressBar.setVisible(true);
        progressBar.setMaxWidth(Double.MAX_VALUE);
        try {
            System.out.println("\n---------------------------------- ");
            System.out.println("Test du driver...");
            Class.forName("org.postgresql.Driver");
            System.out.println("Driver O.K.");
            System.out.println("Connexion en cours...");
            Connection conn = DriverManager.getConnection(url, user, passwd);
            System.out.println("Connexion effective !");
            System.out.println("---------------------------------- \n");
            conn.close();
            System.out.println("Connexion terminée !");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
