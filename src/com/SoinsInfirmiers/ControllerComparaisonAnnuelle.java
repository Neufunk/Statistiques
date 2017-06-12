package com.SoinsInfirmiers;

import com.Main;
import com.Strings;
import com.jfoenix.controls.*;
import com.jfoenix.transitions.hamburger.HamburgerBackArrowBasicTransition;
import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import javafx.scene.control.Label;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ControllerComparaisonAnnuelle implements Initializable {

    // Instances de classes
    private Strings strings = new Strings();

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
    @FXML
    private LineChart lineGraph;
    @FXML
    private JFXSpinner idleSpinner;
    @FXML
    private Label noGraphicLabel;

    private Year year = new Year();
    private Category category = new Category();
    private Indicateur indicateur = new Indicateur();
    private Centre centre = new Centre();
    private IteratorExcel iteratorExcel0 = new IteratorExcel();
    private IteratorExcel iteratorExcel1 = new IteratorExcel();
    private IteratorExcel iteratorExcel2 = new IteratorExcel();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeCombo();
        drawMenu();
        onGenerateButtonClick();
        onClearButtonClick();
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

    private void initializeCombo() {
        comboCentre.setItems(strings.centerList);
        comboYear0.setItems(Strings.yearList);
        comboYear1.setItems(Strings.yearList);
        comboYear2.setItems(Strings.yearList);
        comboCategorie.setItems(strings.categorieList);
        anchorPane0.addEventHandler(MouseEvent.ANY, (e) -> {
            if (comboCategorie.getValue() != null) {
                category.setCategorie(comboCategorie.getValue().toString());
                comboIndic.setItems(category.getCategorie());
            }
        });
    }

    private void onClearButtonClick() {
        clearButton.addEventHandler(MouseEvent.MOUSE_CLICKED, (e) -> {
            comboCentre.getSelectionModel().clearSelection();
            comboYear0.getSelectionModel().clearSelection();
            comboYear1.getSelectionModel().clearSelection();
            comboYear2.getSelectionModel().clearSelection();
            comboCategorie.getSelectionModel().clearSelection();
            comboIndic.getSelectionModel().clearSelection();
            lineGraph.setTitle("");
            lineGraph.getData().clear();
            lineGraph.setVisible(false);
            idleSpinner.setVisible(true);

        });
    }

    private void onGenerateButtonClick() {
        generateButton.addEventHandler(MouseEvent.MOUSE_CLICKED, (e) -> {
            if (!checkEmpty()) {
            } else {
                generateAll();
            }
        });
    }

    private boolean checkEmpty() {
        if (comboYear0.getValue() == null && comboYear1.getValue() == null && comboYear2.getValue() == null) {
            year.showEmptyDialog();
            return false;
        } else if (comboCentre.getValue() == null) {
            centre.showEmptyDialog();
            return false;
        } else if (comboIndic.getValue() == null) {
            indicateur.showEmptyDialog();
            return false;
        } else {
            return true;
        }
    }

    private void generateAll() {
        generateYear0();
        if (comboYear1.getValue() != null) {
            generateYear1();
        }
        if (comboYear2.getValue() != null) {
            generateYear2();
        }
        buildLineGraphic();
        closeConnection();
    }

    private void generateYear0() {
        centre.toExcelSheet(comboCentre.getValue().toString());
        year.toPath((int) comboYear0.getValue());
        indicateur.toExcelRow(comboIndic.getValue().toString());
        iteratorExcel0.setSheet(centre.getSheet());
        iteratorExcel0.setPath(year.getPath());
        iteratorExcel0.setFiles(year.getFileA(), year.getFileB(), year.getFileC());
        if (indicateur.getWithFileD()) {
            iteratorExcel0.setFiles(year.getFileD(), year.getFileB(), year.getFileC());
        }
        iteratorExcel0.setMasterRow(indicateur.getMasterRow());
        startIteration();
    }

    private void generateYear1() {
        centre.toExcelSheet(comboCentre.getValue().toString());
        year.toPath((int) comboYear1.getValue());
        indicateur.toExcelRow(comboIndic.getValue().toString());
        iteratorExcel1.setSheet(centre.getSheet());
        iteratorExcel1.setPath(year.getPath());
        iteratorExcel1.setFiles(year.getFileA(), year.getFileB(), year.getFileC());
        if (indicateur.getWithFileD()) {
            iteratorExcel1.setFiles(year.getFileD(), year.getFileB(), year.getFileC());
        }
        iteratorExcel1.setMasterRow(indicateur.getMasterRow());
        startIteration1();
    }

    private void generateYear2() {
        centre.toExcelSheet(comboCentre.getValue().toString());
        year.toPath((int) comboYear2.getValue());
        indicateur.toExcelRow(comboIndic.getValue().toString());
        iteratorExcel2.setSheet(centre.getSheet());
        iteratorExcel2.setPath(year.getPath());
        iteratorExcel2.setFiles(year.getFileA(), year.getFileB(), year.getFileC());
        if (indicateur.getWithFileD()) {
            iteratorExcel2.setFiles(year.getFileD(), year.getFileB(), year.getFileC());
        }
        iteratorExcel2.setMasterRow(indicateur.getMasterRow());
        startIteration2();
    }

    private void startIteration() {
        try {
            iteratorExcel0.allYearIteration();
        } catch (FileNotFoundException e0) {
            iteratorExcel0.fileNotFound(e0);
            return;
        } catch (IOException | InvalidFormatException e1) {
            e1.printStackTrace();
            return;
        }
    }

    private void startIteration1() {
        try {
            iteratorExcel1.allYearIteration();
        } catch (FileNotFoundException e0) {
            iteratorExcel1.fileNotFound(e0);
            return;
        } catch (IOException | InvalidFormatException e1) {
            e1.printStackTrace();
            return;
        }
        return;
    }

    private void startIteration2() {
        try {
            iteratorExcel2.allYearIteration();
        } catch (FileNotFoundException e0) {
            iteratorExcel2.fileNotFound(e0);
            return;
        } catch (IOException | InvalidFormatException e1) {
            e1.printStackTrace();
            return;
        }
    }

    private void buildLineGraphic() {
        lineGraph.getData().clear();
        lineGraph.setVisible(true);
        idleSpinner.setVisible(false);
        Graphic serie1 = new Graphic();
        Graphic serie2 = new Graphic();
        Graphic serie3 = new Graphic();
        String[] month = {"Janvier", "Février", "Mars", "Avril", "Mai", "Juin", "Juillet",
                "Août", "Septembre", "Octobre", "Novembre", "Décembre"};
        double[] value = {iteratorExcel0.getContentJanvierCell(), iteratorExcel0.getContentFevrierCell(),
                iteratorExcel0.getContentMarsCell(), iteratorExcel0.getContentAvrilCell(),
                iteratorExcel0.getContentMaiCell(), iteratorExcel0.getContentJuinCell(), iteratorExcel0.getContentJuilletCell(),
                iteratorExcel0.getContentAoutCell(), iteratorExcel0.getContentSeptembreCell(),
                iteratorExcel0.getContentOctobreCell(), iteratorExcel0.getContentNovembreCell(), iteratorExcel0.getContentDecembreCell()};
        for (int i = 0; i < month.length; i++) {
            serie1.buildLineGraphic(month[i], value[i], comboYear0.getValue().toString());
        }
        if (comboYear1.getValue() != null) {
            double[] value2 = {iteratorExcel1.getContentJanvierCell(), iteratorExcel1.getContentFevrierCell(),
                    iteratorExcel1.getContentMarsCell(), iteratorExcel1.getContentAvrilCell(),
                    iteratorExcel1.getContentMaiCell(), iteratorExcel1.getContentJuinCell(), iteratorExcel1.getContentJuilletCell(),
                    iteratorExcel1.getContentAoutCell(), iteratorExcel1.getContentSeptembreCell(),
                    iteratorExcel1.getContentOctobreCell(), iteratorExcel1.getContentNovembreCell(), iteratorExcel1.getContentDecembreCell()};
            for (int i = 0; i < month.length; i++) {
                serie2.buildLineGraphic(month[i], value2[i], comboYear1.getValue().toString());
            }
        }
        if (comboYear2.getValue() != null) {
            double[] value3 = {iteratorExcel2.getContentJanvierCell(), iteratorExcel2.getContentFevrierCell(),
                    iteratorExcel2.getContentMarsCell(), iteratorExcel2.getContentAvrilCell(),
                    iteratorExcel2.getContentMaiCell(), iteratorExcel2.getContentJuinCell(), iteratorExcel2.getContentJuilletCell(),
                    iteratorExcel2.getContentAoutCell(), iteratorExcel2.getContentSeptembreCell(),
                    iteratorExcel2.getContentOctobreCell(), iteratorExcel2.getContentNovembreCell(), iteratorExcel2.getContentDecembreCell()};
            for (int i = 0; i < month.length; i++) {
                serie3.buildLineGraphic(month[i], value3[i], comboYear2.getValue().toString());
            }
        }
        lineGraph.getData().add(serie1.getLineChartData());
        if (comboYear1.getValue() != null) {
            lineGraph.getData().add(serie2.getLineChartData());
        }
        if (comboYear2.getValue() != null) {
            lineGraph.getData().add(serie3.getLineChartData());
        }
        lineGraph.setTitle(iteratorExcel0.getContentTitleMasterCell());
    }

    private void closeConnection() {
        try {
            iteratorExcel0.closeConnection();
            if (comboYear0.getValue() != null) {
                iteratorExcel1.closeConnection();
            }
            if (comboYear1.getValue() != null) {

                if (comboYear1.getValue() != null) {
                    iteratorExcel1.closeConnection();
                }
                if (comboYear2.getValue() != null) {

                    iteratorExcel2.closeConnection();
                }
            } catch(IOException e){
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

