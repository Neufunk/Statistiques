package com.SoinsInfirmiers;

import com.AccessConnection;
import com.Main;
import com.Strings;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.transitions.hamburger.HamburgerBackArrowBasicTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class SIPageController implements Initializable {

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
    private JFXComboBox comboPeriode;
    @FXML
    private JFXComboBox comboIndic;
    @FXML
    private JFXComboBox comboYear;
    @FXML
    private PieChart roundGraph;
    @FXML
    private JFXButton generateButton;

    // Variables
    String selectedChamp = "";

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        /*************************************DRAWER MENU**********************************************************/
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
        /******************************************************************************************************/


        // COMBOBOX
        comboYear.setItems(strings.yearList);
        comboCentre.setItems(strings.centerList);
        comboIndic.setItems(strings.indicList);
        comboPeriode.setItems(strings.periodList);




        // MISE EN FROMAGE
        ObservableList<PieChart.Data> pieChartData
                = FXCollections.observableArrayList(
                new PieChart.Data("Test 0", 25),
                new PieChart.Data("Test 1", 50),
                new PieChart.Data("Test 2", 15),
                new PieChart.Data("Test 3", 10)
        );
        roundGraph.setData(pieChartData);
        roundGraph.setAnimated(true);
        roundGraph.setStartAngle(90);


        // BOUTTON
        generateButton.addEventHandler(MouseEvent.MOUSE_CLICKED, (e) -> {
            // RECUPERATION DES ITEMS DANS COMBO
            String comparePeriode = comboPeriode.getValue().toString();
            if (comparePeriode == "Année Complète"){
                selectedChamp = "Champ16";
            }else if (comparePeriode == "Janvier"){
                selectedChamp = "Champ4";
            }else if (comparePeriode == "Février"){
                selectedChamp = "Champ5";
            }else if (comparePeriode == "Mars"){
                selectedChamp = "Champ6";
            }else if (comparePeriode == "Avril"){
                selectedChamp = "Champ7";
            }else if (comparePeriode == "Mai"){
                selectedChamp = "Champ8";
            }else if (comparePeriode == "Juin"){
                selectedChamp = "Champ9";
            }else if (comparePeriode == "Juillet"){
                selectedChamp = "Champ10";
            }else if (comparePeriode == "Août"){
                selectedChamp = "Champ11";
            }else if (comparePeriode == "Septembre"){
                selectedChamp = "Champ12";
            }else if (comparePeriode == "Octobre"){
                selectedChamp = "Champ13";
            }else if (comparePeriode == "Novembre"){
                selectedChamp = "Champ14";
            }else if (comparePeriode == "Décembre"){
                selectedChamp = "Champ15";
            }

            // DB CONNECTION
            String compareIndic = comboIndic.getValue().toString();
            try {
                PreparedStatement ps0 = AccessConnection.SiStatConnection().prepareStatement("SELECT * FROM 902_2015 WHERE Champ2 = ?");
                ps0.setString(1, compareIndic);
                ResultSet result = ps0.executeQuery();
                while (result.next()) {
                    String rs = result.getString(selectedChamp);
                    System.out.println(rs);
                    ps0.close();
                    AccessConnection.SiStatConnection().close();
                    System.out.println("Connexion terminée");
                    }
            } catch (SQLException e1) {
                e1.printStackTrace();
            }

        });


    }

}
