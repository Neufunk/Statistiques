package com.SoinsInfirmiers;

import com.Main;
import com.PrintOut;
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
import javafx.scene.chart.PieChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ControllerStatistiquesSI implements Initializable {

    // Injection des objets FXML
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
    @FXML
    private Label graphicTitle;
    @FXML
    private AnchorPane mainPane;
    @FXML
    private AnchorPane anchorPane0;
    @FXML
    private AnchorPane anchorPane1;
    @FXML
    private AnchorPane anchorPane2;
    @FXML
    private AnchorPane maskPane;
    @FXML
    private ImageView pdfIcon;
    @FXML
    private JFXComboBox comboCategorie;
    @FXML
    private Label noGraphicLabel;
    @FXML
    private Label labelMasterIndic;
    @FXML
    private Label labelMasterValue;
    @FXML
    private Label labelIndicA;
    @FXML
    private Label labelIndicB;
    @FXML
    private Label labelIndicC;
    @FXML
    private Label labelIndicD;
    @FXML
    private Label labelIndicE;
    @FXML
    private Label labelValueA;
    @FXML
    private Label labelValueB;
    @FXML
    private Label labelValueC;
    @FXML
    private Label labelValueD;
    @FXML
    private Label labelValueE;
    @FXML
    private AnchorPane labelPane;
    @FXML
    private AnchorPane valuePane;
    @FXML
    private ImageView xlsIcon;
    @FXML
    private ImageView printIcon;
    @FXML
    private JFXCheckBox debugBox;
    @FXML
    private Label monthLabel;
    @FXML
    private JFXButton backButton;
    @FXML
    private JFXButton nextButton;

    private Year year = new Year();
    private Centre centre = new Centre();
    private Periode periode = new Periode();
    private Indicateur indicateur = new Indicateur();
    private IteratorExcel iteratorExcel = new IteratorExcel();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        drawMenu();
        initializeCombo();
        pdfIcon();
        xlsIcon();
        printIcon();
        initializeDebugBox();
        onGenerateButtonClick();
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
        hamburger.addEventHandler(javafx.scene.input.MouseEvent.MOUSE_CLICKED, (e) -> {
            transition.setRate(transition.getRate() * -1);
            transition.play();
            if (drawer.isShown()) {
                drawer.close();
                maskPane.setVisible(false);
            } else {
                drawer.open();
                maskPane.setVisible(true);
                fadeTransition.setFromValue(0);
                fadeTransition.setToValue(0.2);
                fadeTransition.play();
                drawer.setVisible(true);
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

    private void initializeCombo() {
        Strings strings = new Strings();
        comboYear.setItems(strings.yearList);
        comboCentre.setItems(strings.centerList);
        comboPeriode.setItems(strings.periodList);
        comboCategorie.setItems(strings.categorieList);
        Category category = new Category();
        anchorPane0.addEventHandler(MouseEvent.ANY, (e) -> {
            if (comboCategorie.getValue() != null) {
                category.setCategorie(comboCategorie.getValue().toString());
                comboIndic.setItems(category.getCategorie());
            }
        });
    }

    private void onGenerateButtonClick() {
        generateButton.addEventHandler(MouseEvent.MOUSE_CLICKED, (e) -> {
            if (!checkEmpty()) {
                return;
            } else {
                generate();
            }
        });
    }

    private boolean checkEmpty() {
        if (comboYear.getValue() == null) {
            year.showEmptyDialog();
            return false;
        } else if (comboCentre.getValue() == null) {
            centre.showEmptyDialog();
            return false;
        } else if (comboPeriode.getValue() == null) {
            periode.showEmptyDialog();
            return false;
        } else if (comboIndic.getValue() == null) {
            indicateur.showEmptyDialog();
            return false;
        } else {
            return true;
        }
    }

    private void generate() {
        year.toPath((Integer) comboYear.getValue());
        centre.toExcelSheet(comboCentre.getValue().toString());
        periode.toExcelColumn(comboPeriode.getValue().toString());
        indicateur.toExcelRow(comboIndic.getValue().toString());
        if (indicateur.getWithFileD()) {
            iteratorExcel.setPath(year.getPath());
            iteratorExcel.setFileA(year.getFileD());
            iteratorExcel.setFileB(year.getFileB());
            iteratorExcel.setFileC(year.getFileC());
        } else {
            iteratorExcel.setPath(year.getPath());
            iteratorExcel.setFileA(year.getFileA());
            iteratorExcel.setFileB(year.getFileB());
            iteratorExcel.setFileC(year.getFileC());
        }
        iteratorExcel.setSheet(centre.getSheet());
        iteratorExcel.setColumn(periode.getColumn());
        iteratorExcel.setMasterRow(indicateur.getMasterRow());
        iteratorExcel.setRowA(indicateur.getRowA());
        iteratorExcel.setRowB(indicateur.getRowB());
        iteratorExcel.setRowC(indicateur.getRowC());
        iteratorExcel.setRowD(indicateur.getRowD());
        iteratorExcel.setRowE(indicateur.getRowE());
        startIteration();
        // PIEGRAPHIC
        buildPieGraphic();
        // RAWDATA
        buildRawData();
        iteratorExcel.resetVariables();
        closeConnection();
    }

    private void startIteration() {
        try {
            iteratorExcel.startIteration();
        } catch (FileNotFoundException e0) {
            e0.printStackTrace();
            String e1 = e0.toString();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Fichier occupé ou introuvable");
            alert.setHeaderText(e1);
            alert.setContentText("CAUSE : \t\t\t" + e0.getCause() + "\n" + "STACKTRACE : \t\t" + e0.getStackTrace() + "\n" +
                    "FICHIER : \t\t\t" + e0.getLocalizedMessage());
            alert.showAndWait();
        } catch (IOException | InvalidFormatException e1) {
            e1.printStackTrace();
        }
        return;
    }

    private void buildPieGraphic() {
        Graphic pieGraphic = new Graphic();
        if (indicateur.getWithGraphic()) {
            roundGraph.getData().clear();
            noGraphicLabel.setVisible(false);
            graphicTitle.setText(comboIndic.getValue().toString());
            String[] graphicArray = {iteratorExcel.getContentTitleCellA(), iteratorExcel.getContentTitleCellB(),
                    iteratorExcel.getContentTitleCellC(), iteratorExcel.getContentTitleCellD(), iteratorExcel.getContentTitleCellE()};
            Double[] valueArray = {iteratorExcel.getContentCellA(), iteratorExcel.getContentCellB(),
                    iteratorExcel.getContentCellC(), iteratorExcel.getContentCellD(), iteratorExcel.getContentCellE()};
            for (int i = 0; i < graphicArray.length; i++) {
                pieGraphic.buildGraphic(graphicArray[i], valueArray[i]);
            }
            roundGraph.setData(pieGraphic.getPieChartData());
            roundGraph.setStartAngle(90);
        } else {
            roundGraph.getData().clear();
            graphicTitle.setText("");
            noGraphicLabel.setText("Graphique non disponible pour cet indicateur");
            noGraphicLabel.setVisible(true);
        }
        for (final PieChart.Data data : roundGraph.getData()) {
            data.getNode().addEventHandler(MouseEvent.MOUSE_ENTERED, (e1) ->
                    Tooltip.install(data.getNode(), new Tooltip(String.valueOf(data.getPieValue() + "%"))));
        }
    }

    private void buildRawData() {
        FadeTransition fadeTransitionA = new FadeTransition(Duration.millis(1000), labelPane);
        FadeTransition fadeTransitionB = new FadeTransition(Duration.millis(1000), valuePane);
        labelPane.setVisible(true);
        valuePane.setVisible(true);
        fadeTransitionA.setFromValue(0);
        fadeTransitionA.setToValue(1);
        fadeTransitionA.play();
        fadeTransitionB.setFromValue(0);
        fadeTransitionB.setToValue(1);
        fadeTransitionB.play();
        navigateThroughMonths();
        Graphic setData = new Graphic();
        setData.setRawDataName(labelMasterIndic, iteratorExcel.getContentTitleMasterCell());
        Label[] indicLabel = {labelIndicA, labelIndicB, labelIndicC, labelIndicD, labelIndicE};
        String[] dataName = {iteratorExcel.getContentTitleCellA(), iteratorExcel.getContentTitleCellB(),
                iteratorExcel.getContentTitleCellC(), iteratorExcel.getContentTitleCellD(), iteratorExcel.getContentTitleCellE()};
        for (int i = 0; i < indicLabel.length; i++) {
            setData.setRawDataName(indicLabel[i], dataName[i]);
        }
        setData.setMasterRawDataValue(labelMasterValue, iteratorExcel.getContentMasterCell());

        Label[] valueLabel = {labelValueA, labelValueB, labelValueC, labelValueD, labelValueE};
        Double[] dataValue = {iteratorExcel.getContentCellA(), iteratorExcel.getContentCellB(),
                iteratorExcel.getContentCellC(), iteratorExcel.getContentCellD(), iteratorExcel.getContentCellE()};
        for (int i = 0; i < valueLabel.length; i++) {
            setData.setRawDataValue(valueLabel[i], dataValue[i]);
        }
    }

    private void navigateThroughMonths() {
        backButton.addEventHandler(MouseEvent.MOUSE_CLICKED, (event) -> {

            //TODO : Continuer navigation par mois
        });
        monthLabel.setText(comboPeriode.getValue().toString());
        monthLabel.setVisible(true);

    }

    private void initializeDebugBox() {
        debugBox.addEventHandler(MouseEvent.MOUSE_CLICKED, (event -> {
            if (debugBox.isSelected()) {
                Strings.yearList.add(1337);
            } else {
                Strings.yearList.remove(3);
            }
        }));


    }

    private void closeConnection() {
        try {
            iteratorExcel.closeConnection();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    private void pdfIcon() {
        Tooltip.install(pdfIcon, new Tooltip("Imprimer ou exporter en PDF"));
    }

    private void xlsIcon() {
        Tooltip.install(xlsIcon, new Tooltip("Ouvrir le fichier EXCEL original"));
        xlsIcon.addEventHandler(MouseEvent.MOUSE_CLICKED, (e) -> {
            if (comboYear.getValue() == null) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("NullPointerException");
                alert.setHeaderText("Veuillez sélectionner une année");
                alert.setContentText("Impossible d'ouvrir le fichier.");
                alert.showAndWait();
            } else {
                Year year = new Year();
                year.toPath((Integer) comboYear.getValue());
                File file = new File(year.getPath() + year.getFileA());
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
        });
    }

    private void printIcon() {
        Tooltip.install(printIcon, new Tooltip("Imprimer"));
        printIcon.addEventHandler(MouseEvent.MOUSE_CLICKED, (e) -> PrintOut.printerPrint(mainPane));
    }

    // TODO Graphique animé
    // TODO : Classe pour FadeTransitions
    // TODO : Fade in pour les deux panels
}

