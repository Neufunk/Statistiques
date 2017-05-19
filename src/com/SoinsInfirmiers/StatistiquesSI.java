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
import org.apache.poi.hssf.usermodel.HSSFFormulaEvaluator;
import org.apache.poi.hssf.util.CellReference;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class StatistiquesSI implements Initializable {

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

    // Variables de classe
    String fileNameA = "";
    String fileNameB = "";
    String fileNameC = "";
    int selectedSheet = 0;
    String selectedColumn = "";
    int selectedRow = 0;
    int selectedRow2 = 0;
    int selectedRow3 = 0;
    int selectedRow4 = 0;
    int selectedRow5 = 0;
    int selectedRow6 = 0;
    Cell cell = null;
    Cell cell2 = null;
    Cell cell3 = null;
    Cell cell4 = null;
    Cell cell5 = null;
    Cell cell6 = null;
    double from1 = 0;
    double from2 = 0;
    double from3 = 0;
    double from4 = 0;
    double from5 = 0;
    double from6 = 0;
    boolean withGraphic = false;
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
        comboYear.setItems(strings.yearList);
        comboCentre.setItems(strings.centerList);
        comboIndic.setItems(strings.indicList);
        comboPeriode.setItems(strings.periodList);

        // GENERATE BUTTON
        generateButton.addEventHandler(MouseEvent.MOUSE_CLICKED, (e) -> {

            // RECUPERATION DES ITEMS DANS COMBO
            int compareYear = (int) comboYear.getValue();
            if (compareYear == 2017) {
                System.out.println("Okay ça marche");
                fileNameA = "P:/PROVINCE et statistiques FASD/Namur 2017.xls";
                fileNameB = "P:/PROVINCE et statistiques FASD/Données Namur 2017.xls";
                fileNameC = "P:/PROVINCE et statistiques FASD/Suivi pers. CJB Namur 2017.xls";
            } else if (compareYear == 2016){
                System.out.println("Back in 2016");
            }else{
                System.out.println("Année introuvable");
            }
            String compareCentre = comboCentre.getValue().toString();
            if (compareCentre == "Global") {
                selectedSheet = 5;
            } else if (compareCentre == "Namur") {
                selectedSheet = 4;
            } else if (compareCentre == "Philippeville") {
                selectedSheet = 0;
            } else if (compareCentre == "Ciney") {
                selectedSheet = 1;
            } else if (compareCentre == "Gedinne") {
                selectedSheet = 2;
            } else if (compareCentre == "Eghezée") {
                selectedSheet = 3;
            } else {
                System.out.println("Le centre n'existe pas");
            }
            String comparePeriode = comboPeriode.getValue().toString();
            if (comparePeriode == "Janvier") {
                selectedColumn = "D";
            } else if (comparePeriode == "Février") {
                selectedColumn = "E";
            } else {
                System.out.println("Période pas encore implementée");
            }
            String compareIndic = comboIndic.getValue().toString();
            if (compareIndic == "Total jours payés") {
                selectedRow = 6;
                selectedRow2 = 7;
                selectedRow3 = 8;
                selectedRow4 = 9;
                withGraphic = true;
            } else if (compareIndic == "Total jours prestés Infirmières") {
                selectedRow = 11;
                selectedRow2 = 12;
                selectedRow3 = 13;
                withGraphic = false;
            } else {
                System.out.println("Indicateur pas encore implementé");
            }

            try {
                Workbook wb = WorkbookFactory.create(new File(fileNameA));
                Workbook wb2 = WorkbookFactory.create(new File(fileNameB));
                Workbook wb3 = WorkbookFactory.create(new File(fileNameC));
                HSSFFormulaEvaluator evaluator = (HSSFFormulaEvaluator) wb.getCreationHelper().createFormulaEvaluator();
                HSSFFormulaEvaluator evaluator2 = (HSSFFormulaEvaluator) wb2.getCreationHelper().createFormulaEvaluator();
                HSSFFormulaEvaluator evaluator3 = (HSSFFormulaEvaluator) wb3.getCreationHelper().createFormulaEvaluator();
                String[] workbookNames = {"Namur 2017.xls", "Données Namur 2017.xls", "Suivi pers. CJB Namur 2017.xls"};
                HSSFFormulaEvaluator[] evaluators = {evaluator, evaluator2, evaluator3};
                HSSFFormulaEvaluator.setupEnvironment(workbookNames, evaluators);
                Sheet sheet = wb.getSheetAt(selectedSheet);
                CellReference cellReference = new CellReference(selectedColumn + selectedRow);
                Row row = sheet.getRow(cellReference.getRow());
                cell = row.getCell(cellReference.getCol());
                if (selectedRow2 != 0) {
                    CellReference cellReference2 = new CellReference(selectedColumn + selectedRow2);
                    Row row2 = sheet.getRow(cellReference2.getRow());
                    cell2 = row2.getCell(cellReference2.getCol());
                }
                if (selectedRow3 != 0) {
                    CellReference cellReference3 = new CellReference(selectedColumn + selectedRow3);
                    Row row3 = sheet.getRow(cellReference3.getRow());
                    cell3 = row3.getCell(cellReference3.getCol());
                }
                if (selectedRow4 != 0) {
                    CellReference cellReference4 = new CellReference(selectedColumn + selectedRow4);
                    Row row4 = sheet.getRow(cellReference4.getRow());
                    cell4 = row4.getCell(cellReference4.getCol());
                }
                if (selectedRow5 != 0) {
                    CellReference cellReference5 = new CellReference(selectedColumn + selectedRow5);
                    Row row5 = sheet.getRow(cellReference5.getRow());
                    cell5 = row5.getCell(cellReference5.getCol());
                }
                if (selectedRow6 != 0) {
                    CellReference cellReference6 = new CellReference(selectedColumn + selectedRow6);
                    Row row6 = sheet.getRow(cellReference6.getRow());
                    cell6 = row6.getCell(cellReference6.getCol());
                }
                if (cell != null) {
                    from1 = cell.getNumericCellValue();
                    switch (evaluator.evaluateFormulaCell(cell)) {
                        case Cell.CELL_TYPE_BOOLEAN:
                            System.out.println(cell.getBooleanCellValue());
                            break;
                        case Cell.CELL_TYPE_NUMERIC:
                            System.out.println(cell.getNumericCellValue());
                            break;
                        case Cell.CELL_TYPE_STRING:
                            System.out.println(cell.getStringCellValue());
                            break;
                        case Cell.CELL_TYPE_BLANK:
                            break;
                        case Cell.CELL_TYPE_ERROR:
                            System.out.println(cell.getErrorCellValue());
                            break;
                        case Cell.CELL_TYPE_FORMULA:
                            break;
                    }
                }
                if (cell2 != null) {
                    from2 = cell2.getNumericCellValue();
                    switch (evaluator.evaluateFormulaCell(cell2)) {
                        case Cell.CELL_TYPE_BOOLEAN:
                            System.out.println(cell2.getBooleanCellValue());
                            break;
                        case Cell.CELL_TYPE_NUMERIC:
                            System.out.println(cell2.getNumericCellValue());
                            break;
                        case Cell.CELL_TYPE_STRING:
                            System.out.println(cell2.getStringCellValue());
                            break;
                        case Cell.CELL_TYPE_BLANK:
                            break;
                        case Cell.CELL_TYPE_ERROR:
                            System.out.println(cell2.getErrorCellValue());
                            break;
                        case Cell.CELL_TYPE_FORMULA:
                            break;
                    }
                }
                if (cell3 != null) {
                    from3 = cell3.getNumericCellValue();
                    switch (evaluator.evaluateFormulaCell(cell3)) {
                        case Cell.CELL_TYPE_BOOLEAN:
                            System.out.println(cell3.getBooleanCellValue());
                            break;
                        case Cell.CELL_TYPE_NUMERIC:
                            System.out.println(cell3.getNumericCellValue());
                            break;
                        case Cell.CELL_TYPE_STRING:
                            System.out.println(cell3.getStringCellValue());
                            break;
                        case Cell.CELL_TYPE_BLANK:
                            break;
                        case Cell.CELL_TYPE_ERROR:
                            System.out.println(cell3.getErrorCellValue());
                            break;
                        case Cell.CELL_TYPE_FORMULA:
                            break;
                    }
                }
                if (cell4 != null) {
                    from4 = cell4.getNumericCellValue();
                    switch (evaluator.evaluateFormulaCell(cell4)) {
                        case Cell.CELL_TYPE_BOOLEAN:
                            System.out.println(cell4.getBooleanCellValue());
                            break;
                        case Cell.CELL_TYPE_NUMERIC:
                            System.out.println(cell4.getNumericCellValue());
                            break;
                        case Cell.CELL_TYPE_STRING:
                            System.out.println(cell4.getStringCellValue());
                            break;
                        case Cell.CELL_TYPE_BLANK:
                            break;
                        case Cell.CELL_TYPE_ERROR:
                            System.out.println(cell4.getErrorCellValue());
                            break;
                        case Cell.CELL_TYPE_FORMULA:
                            break;
                    }
                }
                if (cell5 != null) {
                    from5 = cell5.getNumericCellValue();
                    switch (evaluator.evaluateFormulaCell(cell5)) {
                        case Cell.CELL_TYPE_BOOLEAN:
                            System.out.println(cell5.getBooleanCellValue());
                            break;
                        case Cell.CELL_TYPE_NUMERIC:
                            System.out.println(cell5.getNumericCellValue());
                            break;
                        case Cell.CELL_TYPE_STRING:
                            System.out.println(cell5.getStringCellValue());
                            break;
                        case Cell.CELL_TYPE_BLANK:
                            break;
                        case Cell.CELL_TYPE_ERROR:
                            System.out.println(cell5.getErrorCellValue());
                            break;
                        case Cell.CELL_TYPE_FORMULA:
                            break;
                    }
                }
                if (cell6 != null) {
                    from6 = cell6.getNumericCellValue();
                    switch (evaluator.evaluateFormulaCell(cell6)) {
                        case Cell.CELL_TYPE_BOOLEAN:
                            System.out.println(cell6.getBooleanCellValue());
                            break;
                        case Cell.CELL_TYPE_NUMERIC:
                            System.out.println(cell6.getNumericCellValue());
                            break;
                        case Cell.CELL_TYPE_STRING:
                            System.out.println(cell6.getStringCellValue());
                            break;
                        case Cell.CELL_TYPE_BLANK:
                            break;
                        case Cell.CELL_TYPE_ERROR:
                            System.out.println(cell6.getErrorCellValue());
                            break;
                        case Cell.CELL_TYPE_FORMULA:
                            break;
                    }
                }
                // MISE EN FROMAGE
                if (withGraphic == true) {
                    pieChartData = FXCollections.observableArrayList(
                            new PieChart.Data("Test 2", from2),
                            new PieChart.Data("Test 3", from3),
                            new PieChart.Data("Test 3", from4),
                            new PieChart.Data("Test 3", from5)
                    );
                    roundGraph.setData(pieChartData);
                    roundGraph.setStartAngle(90);
                }else{
                    pieChartData.clear();
                }

                // VIDAGE MEMOIRE
                cell = null;
                cell2 = null;
                cell3 = null;
                cell4 = null;
                cell5 = null;
                cell6 = null;
                selectedRow = 0;
                selectedRow2 = 0;
                selectedRow3 = 0;
                selectedRow4 = 0;
                selectedRow5 = 0;
                selectedRow6 = 0;
                wb.close();
                wb2.close();
                wb3.close();

            } catch (IOException e1) {
                e1.printStackTrace();
            } catch (InvalidFormatException e1) {
                e1.printStackTrace();
            }
        });
    }

}
