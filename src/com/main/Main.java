package main;

import si.Data;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import tools.*;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.sql.Connection;
import java.sql.PreparedStatement;

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
            Console.appendln("STATISTIQUES \u00A9 2017-"+Date.getCurrentYearStr()+" VERSION " + Version.versionNumber);
            if (!System.getProperty("user.name").equals("johnathanv")) {
                logApplicationLaunch();
            } else {
                System.out.println("UNLOGGED CONNECTION");
                Console.appendln("UNLOGGED CONNECTION");
            }
        } catch (Exception e) {
            ExceptionHandler.switchException(e, this.getClass());
        }
    }

    private void logApplicationLaunch() throws Exception {
        Identification id = new Identification();
        InetAddress inetAddress = InetAddress.getLocalHost();
        final DatagramSocket socket = new DatagramSocket();

        socket.connect(InetAddress.getByName("8.8.8.8"), 10002);
        final String USER = System.getProperty("user.name");
        final String IP_ADRESS = socket.getLocalAddress().getHostAddress();
        final String JAVA_VERSION = System.getProperty("java.version");
        final String HOST_NAME = inetAddress.getHostName();
        final String SOFTWARE_VERSION = Version.versionNumber;

        DatabaseConnection databaseConnection = new DatabaseConnection();
        Connection conn = databaseConnection.connect(
                id.set(Identification.info.D03_URL),
                id.set(Identification.info.D03_USER),
                id.set(Identification.info.D03_PASSWD),
                id.set(Identification.info.D03_DRIVER)
        );

        String query = "INSERT INTO global.log_application_launched"
                + " VALUES (now(), ?, ?, ?, ?, ?)";

        PreparedStatement ps = conn.prepareStatement(query);
        try {
            ps.setString(1, USER);
            ps.setString(2, IP_ADRESS);
            ps.setString(3, JAVA_VERSION);
            ps.setString(4, HOST_NAME);
            ps.setString(5, SOFTWARE_VERSION);
            ps.executeUpdate();
            System.out.println("CONNECTION LOGGED -> " + USER);
            Console.appendln("Bienvenue " + USER);
        } catch (Exception e) {
            ExceptionHandler.switchException(e, this.getClass());
        } finally {
            databaseConnection.close(ps);
            databaseConnection.close(conn);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
