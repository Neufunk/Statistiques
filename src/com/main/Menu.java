package main;

import SoinsInfirmiers.Data;
import SoinsInfirmiers.Rapports;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.Calendar;

public class Menu {

    public static final Stage activiteStage = new Stage();
    public static final Stage gestionStage = new Stage();
    static final Stage connectionTestStage = new Stage();
    private final Print print = new Print();
    private static Node printableNode;

    public void loadMenuBar(Pane pane) {
        try {
            pane.getChildren().add(FXMLLoader.load((getClass().getResource("/ui/FXML/Menu.fxml"))));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setPrintableNode(Node printableNode) {
        Menu.printableNode = printableNode;
    }

    public void openHomepage() {
        Stage stage = Main.getPrimaryStage();
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/ui/FXML/HomePage.fxml"));
            changeScene(root);
            stage.setTitle(AVJ.Data.homePageTitle);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    public void openSettings() {
        Stage stage = Main.getPrimaryStage();
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/ui/FXML/Settings.fxml"));
            changeScene(root);
            stage.setTitle("Paramètres - "+ Version.versionNumber);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    public void showPatchnote(){
        PatchNote pn = new PatchNote();
        pn.patchNote();
    }

    public void changeLogs(){
        String changelog = "P:\\STATISTIQUES\\Changelog.txt";
        File file = new File(changelog);
        if (!Desktop.isDesktopSupported()) {
            System.out.println("OS non supporté");
            return;
        }
        Desktop desktop = Desktop.getDesktop();
        try {
            if (file.exists()) desktop.open(file);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    public void print() {
        print.printSetup(printableNode, Main.getPrimaryStage());
    }

    public void openIndicateursPage(){
        Stage stage = Main.getPrimaryStage();
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/ui/FXML/IndicateursAnnuels.fxml"));
            changeScene(root);
            stage.setTitle(Data.pageTitle0);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    public void openComparaisonAnnees(){
        Stage stage = Main.getPrimaryStage();
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/ui/FXML/ComparaisonAnnees.fxml"));
            changeScene(root);
            stage.setTitle(Data.pageTitle1);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    public void openComparaisonCentres(){
        Stage stage = Main.getPrimaryStage();
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/ui/FXML/ComparaisonCentres.fxml"));
            changeScene(root);
            stage.setTitle(Data.pageTitle3);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    public void openSelectVisitesPatients(){
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

    public void openVisitesCentres(){
        Stage stage = Main.getPrimaryStage();
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/ui/FXML/CompaisonVisitesCentres.fxml"));
            changeScene(root);
            stage.setTitle(Data.pageTitle5);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void openVisitesLocalites(){
        Stage stage = Main.getPrimaryStage();
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/ui/FXML/CompaisonVisitesLocalites.fxml"));
            changeScene(root);
            stage.setTitle(Data.pageTitle4);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void pdfActivite(){
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/ui/FXML/PopUpActivite.fxml"));
            activiteStage.setScene(new Scene(root, 350, 380));
            activiteStage.setTitle("Rapport - Activité et suivi du personnel");
            activiteStage.setResizable(false);
            activiteStage.show();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    public void pdfGestion() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/ui/FXML/PopUpGestion.fxml"));
            gestionStage.setScene(new Scene(root, 350, 380));
            gestionStage.setTitle("Rapport - indicateurs de gestion");
            gestionStage.setResizable(false);
            gestionStage.show();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    public void openContingentPage(){
        Stage stage = Main.getPrimaryStage();
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/ui/FXML/Contingent.fxml"));
            changeScene(root);
            stage.setTitle(AVJ.Data.pageTitle0);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    public void openASDB(){
        Stage stage = new Stage();
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/ui/FXML/ASDB.fxml"));
            stage.setScene(new Scene(root));
            stage.setTitle(AVJ.Data.asdbTitle);
            stage.show();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    public void openConnectionTest(){
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

    private void changeScene(Parent root){
        Stage stage = Main.getPrimaryStage();
        Scene scene = stage.getScene();
        scene.setRoot(root);
    }

    public void openSelectSi(){
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

    public void openSelectAvj(){
        Stage stage = Main.getPrimaryStage();
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/ui/FXML/SelectAVJ.fxml"));
            Scene scene = stage.getScene();
            scene.setRoot(root);
            stage.setTitle(AVJ.Data.pageTitle1);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    public void openAboutWindow() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        int year = Calendar.getInstance().get(Calendar.YEAR);
        alert.setTitle("Logiciel Statistiques - AIDE & SOINS À DOMICILE VERSION " + Version.versionNumber);
        alert.setHeaderText("Logiciel Statistiques - AIDE & SOINS À DOMICILE");
        alert.setContentText(year + " - AIDE & SOINS À DOMICILE EN PROVINCE DE NAMUR\n" +
                "Coded and designed by JOHNATHAN VANBENEDEN \n" +
                "Version " + Version.versionNumber + " - Last build " + Version.getCurrentDate() +
        " - Lines : 13.306 \nSDK 1.8.0_161 - Tested JRE : 1.8.0_151/161/172");
        alert.show();
    }

    public void closeButtonAction() {
        System.exit(0);
    }

}
