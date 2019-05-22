package si;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXSpinner;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import tools.Date;
import tools.Effects;
import tools.ExceptionHandler;

import java.net.URL;
import java.util.ResourceBundle;

public class ControllerComparaisonIndicateurs implements Initializable {

    @FXML
    private JFXComboBox<String> comboCentre, comboPeriode, comboIndic;
    @FXML
    private JFXComboBox<Integer> comboYear;
    @FXML
    private PieChart roundGraph;
    @FXML
    private JFXButton generateButton;
    @FXML
    private Label graphicTitle, noGraphicLabel, labelMasterIndic, labelMasterValue, labelIndicA, labelIndicB, labelIndicC, labelIndicD;
    @FXML
    private Label labelIndicE, labelValueA, labelValueB, labelValueC, labelValueD, labelValueE;
    @FXML
    private JFXSpinner spinner;
    @FXML
    private JFXComboBox<String> comboCategorie;
    @FXML
    private GridPane labelPane, valuePane;
    @FXML
    private Label monthLabel;
    @FXML
    private JFXButton backButton, nextButton;
    @FXML
    private AnchorPane menuPane;

    private final Year year = new Year();
    private final Centre centre = new Centre();
    private final Periode periode = new Periode();
    private final Indicateur indicateur = new Indicateur();
    private final IteratorExcel iteratorExcel = new IteratorExcel();
    private final Effects effects = new Effects();

    /* TODO : TOTAL JOURS PAYES
        TOTAL JOURS PRESTES INF
        NOMBRE DE VISITES
        FACTURATION TOT
        NOMBRE DE PATIENTS NOMENCLATURE / FF
        TOTAL PRESTE
        REPARTITION DES BLOCS
        SUPPLEMENT EN JOURS
     */

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        menuPane.getChildren().get(0).getStyleClass().add("indigo");
        initializeCombo();
        onGenerateButtonClick();
        navigateThroughMonths();
    }

    private void initializeCombo() {
        Data data = new Data();
        int[] yearList = Date.getYearList();
        for (int value : yearList) {
            comboYear.getItems().addAll(value);
        }
        comboCentre.setItems(data.centerList);
        comboPeriode.setItems(data.periodList);
        comboCategorie.setItems(data.categorieList);
    }

    public void setIndicateursInCombo() {
        Category category = new Category();
        if (comboCategorie.getValue() != null) {
            category.setCategorie(comboCategorie.getValue());
            comboIndic.setItems(category.getCategorie());
        }
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
        year.toPath(comboYear.getValue());
        centre.toExcelSheet(comboCentre.getValue());
        periode.toExcelColumn(comboPeriode.getValue());
        indicateur.toExcelRow(comboIndic.getValue());
        iteratorExcel.setPath(year.getPath());
        if (indicateur.getWithFileD()) {
            iteratorExcel.setFiles(year.getFileD(), year.getFileB(), year.getFileC());
        } else {
            iteratorExcel.setFiles(year.getFileA(), year.getFileB(), year.getFileC());
        }
        iteratorExcel.setSheet(centre.getSheet());
        iteratorExcel.setColumn(periode.getColumn());
        iteratorExcel.setMasterRow(indicateur.getMasterRow());
        iteratorExcel.setPieChartRow(new int[]{indicateur.getRowA(), indicateur.getRowB(), indicateur.getRowC(), indicateur.getRowD(), indicateur.getRowE()});
        startIteration();
        buildPieGraphic();
        buildRawData();
        iteratorExcel.resetVariables();
        indicateur.resetVariables();
    }

    private void startIteration() {
        try {
            iteratorExcel.pieChartIteration();
        } catch (Exception e) {
            ExceptionHandler.switchException(e, this.getClass());
        }
    }

    private void buildPieGraphic() {
        Graphic pieGraphic = new Graphic();
        if (indicateur.getWithGraphic()) {
            spinner.setVisible(false);
            roundGraph.getData().clear();
            noGraphicLabel.setVisible(false);
            graphicTitle.setText(comboIndic.getValue());
            String[] graphicArray = iteratorExcel.getTitleArray();
            double[] valueArray = iteratorExcel.getContentArray();
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
                    Tooltip.install(data.getNode(), new Tooltip(data.getPieValue() + "%")));
        }
    }

    private void buildRawData() {
        effects.setFadeTransition(labelPane, 1000, 0, 1);
        effects.setFadeTransition(valuePane, 1000, 0, 1);
        labelPane.setVisible(true);
        valuePane.setVisible(true);
        monthLabel.setText(comboPeriode.getValue());
        monthLabel.setVisible(true);
        Graphic setData = new Graphic();
        setData.setRawDataName(labelMasterIndic, iteratorExcel.getContentTitleMasterCell());
        Label[] indicLabel = {labelIndicA, labelIndicB, labelIndicC, labelIndicD, labelIndicE};
        String[] dataName = iteratorExcel.getTitleArray();
        for (int i = 0; i < indicLabel.length; i++) {
            setData.setRawDataName(indicLabel[i], dataName[i]);
        }
        setData.setMasterRawDataValue(labelMasterValue, iteratorExcel.getContentMasterCell());

        Label[] valueLabel = {labelValueA, labelValueB, labelValueC, labelValueD, labelValueE};
        double[] dataValue = iteratorExcel.getContentArray();
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
}