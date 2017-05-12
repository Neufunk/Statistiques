package com.SoinsInfirmiers;

import com.Main;
import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.transitions.hamburger.HamburgerBackArrowBasicTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class TableauxAnnuels implements Initializable{

    @FXML
    private JFXHamburger hamburger;
    @FXML
    private JFXDrawer drawer;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // DRAWER MENU
        VBox box = null;
        try {
            box = FXMLLoader.load(getClass().getResource("DrawerDesign.fxml"));
            drawer.setSidePane(box);
        } catch (IOException e) {
            e.printStackTrace();
        }
        HamburgerBackArrowBasicTransition transition = new HamburgerBackArrowBasicTransition(hamburger);
        transition.setRate(-1);
        hamburger.addEventHandler(javafx.scene.input.MouseEvent.MOUSE_CLICKED, (e) -> {
            transition.setRate(transition.getRate() * -1);
            transition.play();
            if (drawer.isShown()) {
                drawer.close();
            } else {
                drawer.open();
            }
        });
        for (Node node : box.getChildren()) {
            if (node.getAccessibleText() != null) {
                node.addEventHandler(MouseEvent.MOUSE_CLICKED, (e) -> {
                    switch (node.getAccessibleText()) {
                        case "drawerButtonBack":
                            Stage stage = Main.getPrimaryStage();
                            try {
                                Parent root = FXMLLoader.load(getClass().getResource("../HomePage.fxml"));
                                stage.setScene(new Scene(root));
                                stage.setTitle("Aide & Soins à Domicile - Statistiques // FX_Alpha 1");
                            } catch (IOException e1) {
                                e1.printStackTrace();
                            }
                            break;
                        case "arrayYearButton":
                            stage = Main.getPrimaryStage();
                            try {
                                Parent root = FXMLLoader.load(getClass().getResource("TableauxAnnuels.fxml"));
                                stage.setScene(new Scene(root));
                                stage.setTitle("Soins Infirmiers - Tableaux Annuels // FX_Alpha 1");
                            } catch (IOException e1) {
                                e1.printStackTrace();
                            }
                            break;
                        case "homeButton":
                            stage = Main.getPrimaryStage();
                            try {
                                Parent root = FXMLLoader.load(getClass().getResource("SIPage.fxml"));
                                stage.setScene(new Scene(root));
                                stage.setTitle("Soins Infirmiers - Statistiques // FX_Alpha 1");
                            } catch (IOException e1) {
                                e1.printStackTrace();
                            }
                            break;
                    }
                });
            }
        }
    }}
