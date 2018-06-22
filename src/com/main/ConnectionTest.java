package main;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXSpinner;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.io.IOException;
import java.net.InetAddress;
import java.sql.Connection;
import java.sql.DriverManager;

public class ConnectionTest {

    @FXML
    private JFXSpinner progress1;
    @FXML
    private JFXSpinner progress2;
    @FXML
    private JFXSpinner progress3;
    @FXML
    private JFXSpinner progress4;
    @FXML
    private JFXSpinner progress5;
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
    @FXML
    private JFXButton launchButton;
    @FXML
    private JFXButton closeButton;

    Identification id = new Identification();

    public void onButtonClick() {
        progressSetVisible();
        new Thread(() -> {
            testDatabaseConnection(id.set(Identification.info.D03_URL), id.set(Identification.info.D03_USER), id.set(Identification.info.D03_PASSWD),
                    "org.postgresql.Driver", progress1, done1, failed1);
            testServerConnection("130.15.0.1", progress2, done2, failed2);
            testServerConnection("130.17.0.1", progress3, done3, failed3);
            testDatabaseConnection(id.set(Identification.info.D615_URL), id.set(Identification.info.D615_USER), id.set(Identification.info.D615_PASSWD),
                    "oracle.jdbc.driver.OracleDriver", progress4, done4, failed4);
            testDatabaseConnection(id.set(Identification.info.D015_URL), id.set(Identification.info.D015_USER), id.set(Identification.info.D015_PASSWD),
                    "oracle.jdbc.driver.OracleDriver", progress5, done5, failed5);
        }).start();
        launchButton.setVisible(false);
        closeButton.setVisible(true);
    }

    private void progressSetVisible() {
        progress1.setVisible(true);
        progress2.setVisible(true);
        progress3.setVisible(true);
        progress4.setVisible(true);
        progress5.setVisible(true);
    }

    private void testDatabaseConnection(String url, String user, String passwd, String driver, JFXSpinner progress, Label done, Label failed) {
        try {
            System.out.println("\n---------------------------------- ");
            System.out.println("Test du driver...");
            Class.forName(driver);
            System.out.println("Driver O.K.");
            System.out.println("Connexion en cours...");
            Connection conn = DriverManager.getConnection(url, user, passwd);
            System.out.println("Connexion réussie -> " + url + " !");
            conn.close();
            System.out.println("Connexion terminée !");
            System.out.println("---------------------------------- ");
            progress.setVisible(false);
            done.setVisible(true);
        } catch (Exception e) {
            progress.setVisible(false);
            failed.setVisible(true);
            ExceptionHandler.switchException(e, this.getClass());
        }
    }

    private void testServerConnection(String ip, JFXSpinner progress, Label done, Label failed) {
        try {
            progress.setVisible(false);
            if (InetAddress.getByName(ip).isReachable(5000)) {
                done.setVisible(true);
                System.out.println("Connexion à " + InetAddress.getByName(ip).getHostName() + " (" + ip + ") réussie");
                System.out.println("\n -------------------------------\n");
            } else {
                failed.setVisible(true);
            }
        } catch (IOException e) {
            failed.setVisible(true);
            ExceptionHandler.switchException(e, this.getClass());
        }
    }

    public void onCloseClick() {
        Menu.connectionTestStage.close();
    }
}
