package SoinsInfirmiers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXSpinner;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import main.Effects;
import main.Menu;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ControllerStatistiquesSI implements Initializable {

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
    private AnchorPane anchorPane0;
    @FXML
    private JFXSpinner spinner;
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
    private Label monthLabel;
    @FXML
    private JFXButton backButton;
    @FXML
    private JFXButton nextButton;
    @FXML
    private VBox menuPane;

    private Year year = new Year();
    private Centre centre = new Centre();
    private Periode periode = new Periode();
    private Indicateur indicateur = new Indicateur();
    private IteratorExcel iteratorExcel = new IteratorExcel();
    private Effects effects = new Effects();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Menu menu = new Menu();
        menu.loadMenuBar(menuPane);
        initializeCombo();
        xlsIcon();
        onGenerateButtonClick();
        navigateThroughMonths();
    }

    private void initializeCombo() {
        Data data = new Data();
        comboYear.setItems(Data.yearList);
        comboCentre.setItems(data.centerList);
        comboPeriode.setItems(data.periodList);
        comboCategorie.setItems(data.categorieList);
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
            if (checkEmpty()) {
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
        year.toPath((Integer)comboYear.getValue());
        centre.toExcelSheet(comboCentre.getValue().toString());
        periode.toExcelColumn(comboPeriode.getValue().toString());
        indicateur.toExcelRow(comboIndic.getValue().toString());
        iteratorExcel.setPath(year.getPath());
        if (indicateur.getWithFileD()) {
            iteratorExcel.setFiles(year.getFileD(), year.getFileB(), year.getFileC());
        }else {
            iteratorExcel.setFiles(year.getFileA(), year.getFileB(), year.getFileC());
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
        buildPieGraphic();
        buildRawData();
        closeConnection();
        iteratorExcel.resetVariables();
        indicateur.resetVariables();
    }

    private void startIteration() {
        try {
            iteratorExcel.startIteration();
        } catch (FileNotFoundException e0) {
            iteratorExcel.fileNotFound(e0);
            // return;
        } catch (IOException | InvalidFormatException e1) {
            e1.printStackTrace();
        }
    }

    private void buildPieGraphic() {
        Graphic pieGraphic = new Graphic();
        if (indicateur.getWithGraphic()) {
            spinner.setVisible(false);
            roundGraph.getData().clear();
            noGraphicLabel.setVisible(false);
            graphicTitle.setText(comboIndic.getValue().toString());
            String[] graphicArray = {iteratorExcel.getContentTitleCellA(), iteratorExcel.getContentTitleCellB(),
                    iteratorExcel.getContentTitleCellC(), iteratorExcel.getContentTitleCellD(), iteratorExcel.getContentTitleCellE()};
            Double[] valueArray = {iteratorExcel.getContentCellA(), iteratorExcel.getContentCellB(),
                    iteratorExcel.getContentCellC(), iteratorExcel.getContentCellD(), iteratorExcel.getContentCellE()};
            for (int i = 0; i < graphicArray.length; i++) {
                pieGraphic.buildPieGraphic(graphicArray[i], valueArray[i]);
            }
            roundGraph.setData(pieGraphic.getPieChartData());
            roundGraph.setStartAngle(90);
        } else {
            roundGraph.getData().clear();
            spinner.setVisible(false);
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
        effects.setFadeTransition(labelPane, 1000, 0, 1);
        effects.setFadeTransition(valuePane, 1000, 0, 1);
        labelPane.setVisible(true);
        valuePane.setVisible(true);
        monthLabel.setText(comboPeriode.getValue().toString());
        monthLabel.setVisible(true);
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
        backButton.addEventHandler(MouseEvent.MOUSE_RELEASED, (event) -> {
            if (checkEmpty()) {
                comboPeriode.getSelectionModel().selectPrevious();
                generate();
            }
        });
        nextButton.addEventHandler(MouseEvent.MOUSE_RELEASED, (event) -> {
            if (checkEmpty()) {
                comboPeriode.getSelectionModel().selectNext();
                generate();
            }
        });
    }

    private void closeConnection() {
        try {
            iteratorExcel.closeConnection();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
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
}

