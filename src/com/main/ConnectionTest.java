package main;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXSpinner;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import tools.Console;
import tools.ExceptionHandler;
import tools.Identification;

import java.io.IOException;
import java.net.InetAddress;
import java.sql.Connection;
import java.sql.DriverManager;

public class ConnectionTest {

    @FXML
    private JFXSpinner progress1, progress2, progress3, progress4, progress5;
    @FXML
    private Label done1, done2, done3, done4, done5, failed1, failed2, failed3, failed4, failed5;
    @FXML
    private JFXButton launchButton, closeButton;

    private final Identification id = new Identification();

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
            Console.appendln("Connexion à "+ url + " réussie");
            conn.close();
            System.out.println("Connexion terminée !");
            System.out.println("---------------------------------- ");
            progress.setVisible(false);
            done.setVisible(true);
        } catch (Exception e) {
            Console.appendln("Connexion à "+ url + " échouée");
            progress.setVisible(false);
            failed.setVisible(true);
            ExceptionHandler.switchException(e, this.getClass());
        }
    }

    private void testServerConnection(String ip, JFXSpinner progress, Label done, Label failed) {
        try {
            final String HOSTNAME = InetAddress.getByName(ip).getHostName();
            progress.setVisible(false);
            if (InetAddress.getByName(ip).isReachable(5000)) {
                done.setVisible(true);
                System.out.println("Connexion à " + HOSTNAME + " (" + ip + ") réussie");
                Console.appendln("Connexion à " + HOSTNAME + " (" + ip + ") réussie");
                System.out.println("\n -------------------------------\n");
            } else {
                failed.setVisible(true);
            }
        } catch (IOException e) {
            Console.appendln("Connexion à " + ip + " échouée");
            failed.setVisible(true);
            ExceptionHandler.switchException(e, this.getClass());
        }
    }

    public void onCloseClick() {
        Menu.connectionTestStage.close();
    }
}
