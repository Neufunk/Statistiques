package com;

import AVJ.Data;
import SoinsInfirmiers.Strings;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXHamburger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class HomePage implements Initializable {

    @FXML
    private JFXButton siButton;
    @FXML
    private JFXButton avjButton;
    @FXML
    private JFXHamburger hamburger;

    // Top menu handled by FXML Controller instead of Java
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        onButtonSiClick();
        onHamburgerClick();
        onButtonAvjClick();
    }

    private void onButtonSiClick(){
        Tooltip.install(siButton, new Tooltip("Département Soins Infirmiers"));
        siButton.addEventHandler(MouseEvent.MOUSE_RELEASED, (e) ->  {
           openIndicateursPage();
        });
    }

    private void onButtonAvjClick(){
        avjButton.addEventHandler(MouseEvent.MOUSE_RELEASED, (e) ->{
            openContingentPage();
        });
    }

    private void onHamburgerClick(){
        Tooltip.install(hamburger, new Tooltip("Patchnotes"));
        hamburger.addEventHandler(MouseEvent.MOUSE_RELEASED, (e) -> {
            showPatchnote();
        });
    }

    public void showPatchnote(){
        PatchNote pn = new PatchNote();
        pn.patchNote();
    }

    public void changeLogs(){
            File file = new File("C:\\Users\\johnathanv\\IdeaProjects\\Statistiques_FX\\src\\resources\\txt\\Changelog.txt");
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

    public void openIndicateursPage(){
        Stage stage = Main.getPrimaryStage();
        try {
            Parent root = FXMLLoader.load(getClass().getResource("FXML/StatistiquesSI.fxml"));
            stage.setScene(new Scene(root));
            stage.setTitle(Strings.pageTitle0);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    public void openComparaisonPage(){
        Stage stage = Main.getPrimaryStage();
        try {
            Parent root = FXMLLoader.load(getClass().getResource("FXML/TableauxAnnuels.fxml"));
            stage.setScene(new Scene(root));
            stage.setTitle(Strings.pageTitle1);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    public void openContingentPage(){
        Stage stage = Main.getPrimaryStage();
        try {
            Parent root = FXMLLoader.load(getClass().getResource("FXML/Contingent.fxml"));
            stage.setScene(new Scene(root));
            stage.setTitle(Data.pageTitle0);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    public void openASDB(){
        Stage stage = new Stage();
        try {
            Parent root = FXMLLoader.load(getClass().getResource("FXML/ASDB.fxml"));
            stage.setScene(new Scene(root));
            stage.setTitle(Data.asdbTitle);
            stage.show();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    public void closeButtonAction() throws InterruptedException {
        System.exit(0);
    }

}

