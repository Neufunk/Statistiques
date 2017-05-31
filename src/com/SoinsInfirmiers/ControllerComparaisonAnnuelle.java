package com.SoinsInfirmiers;

import com.Main;
import com.Strings;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.transitions.hamburger.HamburgerBackArrowBasicTransition;
import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


public class ControllerComparaisonAnnuelle implements Initializable {

    // Instances de classes
    Strings strings = new Strings();

    // Instances des objets FXML
    @FXML
    private JFXHamburger hamburger;
    @FXML
    private JFXDrawer drawer;
    @FXML
    private JFXComboBox comboCentre;
    @FXML
    private JFXComboBox comboYear0;
    @FXML
    private JFXComboBox comboYear1;
    @FXML
    private JFXComboBox comboYear2;
    @FXML
    private JFXComboBox comboIndic;
    @FXML
    private JFXButton generateButton;
    @FXML
    private JFXButton clearButton;
    @FXML
    private AnchorPane maskPane;
    @FXML
    private AnchorPane mainPane;
    @FXML
    private JFXComboBox comboCategorie;
    @FXML
    private AnchorPane anchorPane0;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeCombo();
        drawMenu();
        onGenerateButtonClick();
    }

    private void initializeCombo(){
        comboCentre.setItems(strings.centerList);
        comboYear0.setItems(strings.yearList);
        comboYear1.setItems(strings.yearList);
        comboYear2.setItems(strings.yearList);
        comboCategorie.setItems(strings.categorieList);
        Category cat = new Category();
        anchorPane0.addEventHandler(MouseEvent.ANY, (e) -> {
            if (comboCategorie.getValue() != null) {
                cat.setCategorie(comboCategorie.getValue().toString());
                comboIndic.setItems(cat.getCategorie());
            }
        });
    }

    private void onGenerateButtonClick() {
        generateButton.addEventHandler(MouseEvent.MOUSE_CLICKED, (e) -> {
            // VERIFICATION DES CHAMPS VIDES
            if (comboCentre.getValue() == null) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("NullPointerException");
                alert.setHeaderText("Veuillez sélectionner un centre");
                alert.setContentText("avoid NullPointerException");
                alert.showAndWait();
            } else if (comboYear0.getValue() == null && comboYear1.getValue() == null && comboYear2.getValue() == null) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("NullPointerException");
                alert.setHeaderText("Veuillez sélectionner au moins une année");
                alert.setContentText("avoid NullPointerException");
                alert.showAndWait();
            } else if (comboIndic.getValue() == null) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("NullPointerException");
                alert.setHeaderText("Veuillez choisir un indicateur");
                alert.setContentText("avoid NullPointerException");
                alert.showAndWait();
            }else{
                System.out.print("OK");

            }
        });




    }

    private void drawMenu() {


        VBox box = null;
        FadeTransition fadeTransition = new FadeTransition(Duration.millis(500), maskPane);
        fadeTransition.setAutoReverse(false);
        try {
            box = FXMLLoader.load(getClass().getResource("../FXML/DrawerDesign.fxml"));
            drawer.setSidePane(box);
        } catch (IOException e) {
            e.printStackTrace();
        }
        HamburgerBackArrowBasicTransition transition = new HamburgerBackArrowBasicTransition(hamburger);
        transition.setRate(-1);
        hamburger.addEventHandler(MouseEvent.MOUSE_CLICKED, (e) -> {
            transition.setRate(transition.getRate() * -1);
            transition.play();
            if (drawer.isShown()) {
                drawer.close();
                maskPane.setVisible(false);
            } else {
                drawer.open();
                drawer.open();
                maskPane.setVisible(true);
                fadeTransition.setFromValue(0);
                fadeTransition.setToValue(0.2);
                fadeTransition.play();
            }
        });
        for (Node node : box.getChildren()) {
            if (node.getAccessibleText() != null) {
                node.addEventHandler(MouseEvent.MOUSE_CLICKED, (e) -> {
                    switch (node.getAccessibleText()) {
                        case "backButton":
                            Stage stage = Main.getPrimaryStage();
                            try {
                                Parent root = FXMLLoader.load(getClass().getResource("../FXML/HomePage.fxml"));
                                stage.setScene(new Scene(root));
                                stage.setTitle(Strings.homePageTitle);
                            } catch (IOException e1) {
                                e1.printStackTrace();
                            }
                            break;
                        case "arrayYearButton":
                            stage = Main.getPrimaryStage();
                            try {
                                Parent root = FXMLLoader.load(getClass().getResource("../FXML/TableauxAnnuels.fxml"));
                                stage.setScene(new Scene(root));
                                stage.setTitle(Strings.pageTitle1);
                            } catch (IOException e1) {
                                e1.printStackTrace();
                            }
                            break;
                        case "homeButton":
                            stage = Main.getPrimaryStage();
                            try {
                                Parent root = FXMLLoader.load(getClass().getResource("../FXML/StatistiquesSI.fxml"));
                                stage.setScene(new Scene(root));
                                stage.setTitle(Strings.pageTitle0);
                            } catch (IOException e1) {
                                e1.printStackTrace();
                            }
                            break;
                    }
                });
                mainPane.addEventHandler(MouseEvent.MOUSE_CLICKED, (e) -> {
                    if (drawer.isShown()) {
                        drawer.close();
                        maskPane.setVisible(false);
                        transition.setRate(transition.getRate() * -1);
                        transition.play();
                    }
                });
            }
        }
    }

}
