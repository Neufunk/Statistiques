package com.SoinsInfirmiers;

import com.Main;
import com.Print;
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
import javafx.scene.chart.PieChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ControllerStatistiquesSI implements Initializable {

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


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        /*************************************DRAWER MENU**********************************************************/
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
                mainPane.addEventHandler(MouseEvent.MOUSE_CLICKED, (e) ->{
                    if (drawer.isShown()) {
                        drawer.close();
                        maskPane.setVisible(false);
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

        // PDF PRINT
        pdfIcon.addEventHandler(MouseEvent.MOUSE_CLICKED, (e) -> {
            Print.print(mainPane);
                });

        // GENERATE BUTTON
        generateButton.addEventHandler(MouseEvent.MOUSE_CLICKED, (e) -> {

            roundGraph.getData().clear();

            // VERIFICATION DES CHAMPS VIDES
            if (comboYear.getValue() == null) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("NullPointerException");
                alert.setHeaderText("Veuillez saisir l'année");
                alert.setContentText("avoid NullPointerException");
                alert.showAndWait();
            } else if (comboCentre.getValue() == null) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("NullPointerException");
                alert.setHeaderText("Veuillez sélectionner un centre");
                alert.setContentText("avoid NullPointerException");
                alert.showAndWait();
            } else if (comboPeriode.getValue() == null) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("NullPointerException");
                alert.setHeaderText("Veuillez saisir une période");
                alert.setContentText("avoid NullPointerException");
                alert.showAndWait();
            } else if (comboIndic.getValue() == null) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("NullPointerException");
                alert.setHeaderText("Veuillez saisir un indicateur");
                alert.setContentText("avoid NullPointerException");
                alert.showAndWait();
            } else {
                // RECUPERATION DES ITEMS DANS COMBO
                Year year = new Year();
                year.toPath((Integer) comboYear.getValue());
                Centre centre = new Centre();
                centre.toExcelSheet(comboCentre.getValue().toString());
                Periode column = new Periode();
                column.toExcelColumn(comboPeriode.getValue().toString());
                Indicateur indicateur = new Indicateur();
                indicateur.toExcelRow(comboIndic.getValue().toString());
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
                PieGraphic pieGraphic = new PieGraphic();
                if (indicateur.getWithGraphic() == true) {
                    graphicTitle.setText(comboIndic.getValue().toString());
                    pieGraphic.buildGraphic(iteratorExcel.getContentTitleCellA(), iteratorExcel.getContentCellA());
                    pieGraphic.buildGraphic(iteratorExcel.getContentTitleCellB(), iteratorExcel.getContentCellB());
                    pieGraphic.buildGraphic(iteratorExcel.getContentTitleCellC(), iteratorExcel.getContentCellC());
                    pieGraphic.buildGraphic(iteratorExcel.getContentTitleCellD(), iteratorExcel.getContentCellD());
                    pieGraphic.buildGraphic(iteratorExcel.getContentTitleCellE(), iteratorExcel.getContentCellE());
                    roundGraph.setData(pieGraphic.getPieChartData());
                    roundGraph.setStartAngle(90);
                } else {
                    roundGraph.getData().clear();
                    graphicTitle.setText("");
                }
                try {
                    iteratorExcel.closeConnection();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        });
    }
}
