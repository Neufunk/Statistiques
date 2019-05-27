package main;

import si.Data;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.MenuBar;
import javafx.stage.Stage;
import tools.Changelog;
import tools.Console;
import tools.Version;

import java.io.*;
import java.util.Calendar;

public class Menu {

    @FXML
    MenuBar menuBar;

    private static final Stage gestionStage = new Stage();
    static final Stage connectionTestStage = new Stage();

    public void openHomepage() {
        Stage stage = Main.getPrimaryStage();
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/ui/FXML/HomePage.fxml"));
            changeScene(root);
            stage.setTitle(avj.Data.homePageTitle);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    public void openSettings() {
        Stage stage = Main.getPrimaryStage();
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/ui/FXML/Settings.fxml"));
            changeScene(root);
            stage.setTitle("Paramètres - " + Version.versionNumber);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    public void showPatchnote() {
        PatchNote pn = new PatchNote();
        pn.patchNote();
    }

    public void changeLogs() {
        InputStream inputStream = this.getClass().getResourceAsStream("/txt/Changelog.txt");
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String str;
        try {
            while ((str = bufferedReader.readLine()) != null) {
                System.out.println(str);
                Changelog.buildChangelog();
                Changelog.append(str);
            }
        } catch (Exception e) {
                e.printStackTrace();
        } finally {
            try {
                bufferedReader.close();
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    public void print() {

    }

    public void openIndicateursPage() {
        Stage stage = Main.getPrimaryStage();
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/ui/FXML/Repartition.fxml"));
            changeScene(root);
            stage.setTitle(Data.pageTitle0);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    public void openComparaisonAnnees() {
        Stage stage = Main.getPrimaryStage();
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/ui/FXML/ComparaisonAnnees.fxml"));
            changeScene(root);
            stage.setTitle(Data.pageTitle1);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    public void openComparaisonCentres() {
        Stage stage = Main.getPrimaryStage();
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/ui/FXML/ComparaisonCentres.fxml"));
            changeScene(root);
            stage.setTitle(Data.pageTitle3);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    public void openSelectVisitesPatients() {
        Stage stage = Main.getPrimaryStage();
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/ui/FXML/SelectVisitesPatients.fxml"));
            Scene scene = stage.getScene();
            scene.setRoot(root);
            stage.setTitle(Data.pageTitle2);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    public void openVisitesCentres() {
        Stage stage = Main.getPrimaryStage();
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/ui/FXML/CompaisonVisitesCentres.fxml"));
            changeScene(root);
            stage.setTitle(Data.pageTitle5);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void openVisitesLocalites() {
        Stage stage = Main.getPrimaryStage();
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/ui/FXML/CompaisonVisitesLocalites.fxml"));
            changeScene(root);
            stage.setTitle(Data.pageTitle4);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void pdfGestion() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/ui/FXML/PopUpGestion.fxml"));
            gestionStage.setScene(new Scene(root, 370, 420));
            gestionStage.setTitle("Rapport - indicateurs de gestion");
            gestionStage.show();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    public void openContingentPage() {
        Stage stage = Main.getPrimaryStage();
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/ui/FXML/Contingent.fxml"));
            changeScene(root);
            stage.setTitle(avj.Data.pageTitle0);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    public void openASDB() {
        Stage stage = new Stage();
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/ui/FXML/ASDB.fxml"));
            stage.setScene(new Scene(root));
            stage.setTitle(avj.Data.asdbTitle);
            stage.show();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    public void openConnectionTest() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/ui/FXML/ConnectionTest.fxml"));
            connectionTestStage.setResizable(false);
            connectionTestStage.setScene(new Scene(root, 280, 350));
            connectionTestStage.setTitle("Tester les connexions");
            connectionTestStage.show();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    private void changeScene(Parent root) {
        Stage stage = Main.getPrimaryStage();
        Scene scene = stage.getScene();
        scene.setRoot(root);
    }

    public void openSelectSi() {
        Stage stage = Main.getPrimaryStage();
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/ui/FXML/SelectSi.fxml"));
            Scene scene = stage.getScene();
            scene.setRoot(root);
            stage.setTitle(Data.pageTitle2);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    public void openSelectAvj() {
        Stage stage = Main.getPrimaryStage();
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/ui/FXML/SelectAVJ.fxml"));
            Scene scene = stage.getScene();
            scene.setRoot(root);
            stage.setTitle(avj.Data.pageTitle1);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    public void openLog() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/ui/FXML/Log.fxml"));
            connectionTestStage.setResizable(true);
            connectionTestStage.setScene(new Scene(root, 1200, 980));
            connectionTestStage.setTitle("LOG");
            connectionTestStage.show();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    public void detectJavaVersion() {
        final String JAVA_VERSION = System.getProperty("java.version");
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Version de JAVA installée sur le système");
        alert.setHeaderText("La version de JAVA installée sur votre système et actuellement utilisée par STATISTIQUES est : \nJRE " + JAVA_VERSION);
        alert.setContentText("STATISTIQUES est optimisé pour les versions 8u172/181/201");
        alert.show();
        Console.appendln("JAVA VERSION DETECTED: " + JAVA_VERSION + "\n");
    }

    public void openAboutWindow() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        int year = Calendar.getInstance().get(Calendar.YEAR);
        alert.setTitle("Logiciel Statistiques - AIDE & SOINS À DOMICILE VERSION " + Version.versionNumber);
        alert.setHeaderText("Logiciel Statistiques - AIDE & SOINS À DOMICILE");
        alert.setContentText(year + " - AIDE & SOINS À DOMICILE EN PROVINCE DE NAMUR\n" +
                "Code & Design JOHNATHAN VANBENEDEN \n" +
                Version.versionNumber +
                "\nSDK 1.8.0_201 - Supported JRE : 1.8.0_172/181/201");
        alert.show();
    }

    public void closeButtonAction() {
        System.exit(0);
    }
}
