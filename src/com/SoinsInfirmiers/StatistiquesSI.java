package com.SoinsInfirmiers;

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
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class StatistiquesSI implements Initializable {

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

    // Variables de classe
    Cell masterCell = null;
    Cell cellA = null;
    Cell cellB = null;
    Cell cellC = null;
    Cell cellD = null;
    Cell cellE = null;
    double contentMasterCell = 0;
    double contentCellA = 0;
    double contentCellB = 0;
    double contentCellC = 0;
    double contentCellD = 0;
    double contentCellE = 0;
    ObservableList<PieChart.Data> pieChartData;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        /*************************************DRAWER MENU**********************************************************/
        VBox box = null;
        try {
            box = FXMLLoader.load(getClass().getResource("../FXML/DrawerDesign.fxml"));
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
                                Parent root = FXMLLoader.load(getClass().getResource("../FXML/HomePage.fxml"));
                                stage.setScene(new Scene(root));
                                stage.setTitle("Aide & Soins à Domicile - Statistiques // FX_Alpha 1");
                            } catch (IOException e1) {
                                e1.printStackTrace();
                            }
                            break;
                        case "arrayYearButton":
                            stage = Main.getPrimaryStage();
                            try {
                                Parent root = FXMLLoader.load(getClass().getResource("../FXML/TableauxAnnuels.fxml"));
                                stage.setScene(new Scene(root));
                                stage.setTitle("Soins Infirmiers - Tableaux Annuels // FX_Alpha 1");
                            } catch (IOException e1) {
                                e1.printStackTrace();
                            }
                            break;
                        case "homeButton":
                            stage = Main.getPrimaryStage();
                            try {
                                Parent root = FXMLLoader.load(getClass().getResource("../FXML/StatistiquesSI.fxml"));
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
        Strings strings = new Strings();
        comboYear.setItems(strings.yearList);
        comboCentre.setItems(strings.centerList);
        comboIndic.setItems(strings.indicList);
        comboPeriode.setItems(strings.periodList);

        // GENERATE BUTTON
        generateButton.addEventHandler(MouseEvent.MOUSE_CLICKED, (e) -> {
            // RECUPERATION DES ITEMS DANS COMBO
            YearExcel year = new YearExcel();
            year.toPath((Integer)comboYear.getValue());
            CentreExcel centre = new CentreExcel();
            centre.toSheet(comboCentre.getValue().toString());
            PeriodeExcel column = new PeriodeExcel();
            column.toColumn(comboPeriode.getValue().toString());
            IndicateurExcel indicateur = new IndicateurExcel();
            indicateur.toRow(comboIndic.getValue().toString());

            // ITERATION
            IteratorExcel iteratorExcel = new IteratorExcel();
            iteratorExcel.setPath(year.getPath());
            iteratorExcel.setFileA(year.getFileA());
            iteratorExcel.setFileB(year.getFileB());
            iteratorExcel.setFileC(year.getFileC());
            iteratorExcel.setSheet(centre.getSheet());
            iteratorExcel.setColumn(column.getColumn());
            iteratorExcel.setMasterRow(indicateur.getMasterRow());
            iteratorExcel.setRowA(indicateur.getRowA());
            iteratorExcel.setRowB(indicateur.getRowB());
            iteratorExcel.setRowC(indicateur.getRowC());
            iteratorExcel.setRowD(indicateur.getRowD());
            iteratorExcel.setRowE(indicateur.getRowE());
            try {
                iteratorExcel.startIteration();
            } catch (IOException e1) {
                e1.printStackTrace();
            } catch (InvalidFormatException e1) {
                e1.printStackTrace();
            }

                // MISE EN FROMAGE
                if (indicateur.getGraphic()== true) {
                    pieChartData = FXCollections.observableArrayList(
                            new PieChart.Data("Test 0", iteratorExcel.getContentCellA()),
                            new PieChart.Data("Test 1", iteratorExcel.getContentCellB()),
                            new PieChart.Data("Test 2", iteratorExcel.getContentCellC()),
                            new PieChart.Data("Test 3", iteratorExcel.getContentCellD())
                    );
                    roundGraph.setData(pieChartData);
                    roundGraph.setStartAngle(90);
                }else{
                    pieChartData.clear();
                }

                // VIDAGE MEMOIRE
                masterCell = null;
                cellA = null;
                cellB = null;
                cellC = null;
                cellD = null;
                cellE = null;
        });
    }
    public Cell getMasterCell(){
        return masterCell;
    }
    public Cell getCellA(){
        return cellA;
    }
    public Cell getCellB(){
        return cellB;
    }
    public Cell getCellC(){
        return cellC;
    }
    public Cell getCellD(){
        return cellD;
    }
    public Cell getCellE(){
        return cellE;
    }

}
