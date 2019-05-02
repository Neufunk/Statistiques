package SoinsInfirmiers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXSpinner;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;

import java.net.URL;
import java.util.ResourceBundle;

public class ControllerComparaisonAnnees implements Initializable {

    private final Data data = new Data();

    @FXML
    private JFXComboBox<String> comboCentre;
    @FXML
    private JFXComboBox<Integer> comboYear1;
    @FXML
    private JFXComboBox<Integer> comboYear2;
    @FXML
    private JFXComboBox<Integer> comboYear3;
    @FXML
    private JFXComboBox<String> comboIndic;
    @FXML
    private JFXButton clearButton;
    @FXML
    private JFXComboBox<String> comboCategorie;
    @FXML
    private LineChart<String, Float> lineChart;
    @FXML
    private NumberAxis yAxis;
    @FXML
    private JFXSpinner idleSpinner;
    @FXML
    private Label noGraphicLabel;
    @FXML
    private ImageView redCross0;
    @FXML
    private ImageView redCross1;
    @FXML
    private ImageView redCross2;
    @FXML
    private AnchorPane menuPane;

    private final Year year = new Year();
    private final Category category = new Category();
    private final Indicateur indicateur = new Indicateur();
    private final Centre centre = new Centre();
    private final Graphic serie1 = new Graphic();
    private final Graphic serie2 = new Graphic();
    private final Graphic serie3 = new Graphic();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        menuPane.getChildren().get(0).getStyleClass().add("green");
        initializeCombo();
        onClearButtonClick();
        onRedCrossClick();
    }

    private void initializeCombo() {
        comboCentre.setItems(data.centerList);
        comboYear1.setItems(data.yearList);
        comboYear2.setItems(data.yearList);
        comboYear3.setItems(data.yearList);
        comboCategorie.setItems(data.categorieList);
    }

    public void setIndicateursInCombo() {
        if (comboCategorie.getValue() != null) {
            category.setCategorie(comboCategorie.getValue());
            comboIndic.setItems(category.getCategorie());
        }
    }

    private void onClearButtonClick() {
        clearButton.addEventHandler(MouseEvent.MOUSE_CLICKED, (e) -> {
            clearCombos();
            lineChart.setTitle("");
            lineChart.getData().clear();
            lineChart.setVisible(false);
            noGraphicLabel.setVisible(false);
            idleSpinner.setVisible(true);
        });
    }

    private void clearCombos() {
        comboCentre.getSelectionModel().clearSelection();
        comboYear1.getSelectionModel().clearSelection();
        comboYear2.getSelectionModel().clearSelection();
        comboYear3.getSelectionModel().clearSelection();
        comboCategorie.getSelectionModel().clearSelection();
        comboIndic.getSelectionModel().clearSelection();
    }

    private void onRedCrossClick(){
        redCross0.addEventHandler(MouseEvent.MOUSE_RELEASED, (e) -> {
            comboYear1.getSelectionModel().clearSelection();
            onGenerateButtonClick();
        });
        redCross1.addEventHandler(MouseEvent.MOUSE_RELEASED, (e) -> {
            comboYear2.getSelectionModel().clearSelection();
            onGenerateButtonClick();
        });
        redCross2.addEventHandler(MouseEvent.MOUSE_RELEASED, (e) -> {
            comboYear3.getSelectionModel().clearSelection();
            onGenerateButtonClick();
        });
    }

    public void onGenerateButtonClick() {
        if (checkEmpty()) {
            clearSeries();
            lineChart.getData().clear();
            generateAll();
        }
    }

    private boolean checkEmpty() {
        if (comboYear1.getValue() == null && comboYear2.getValue() == null && comboYear3.getValue() == null) {
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
        if (comboYear1.getValue() != null) {
            IteratorExcel iteratorExcel1 = new IteratorExcel();
            setFiles(iteratorExcel1, comboYear1);
            buildLineGraphic(iteratorExcel1, comboYear1, serie1);
        }
        if (comboYear2.getValue() != null) {
            IteratorExcel iteratorExcel2 = new IteratorExcel();
            setFiles(iteratorExcel2, comboYear2);
            buildLineGraphic(iteratorExcel2, comboYear2, serie2);
        }
        if (comboYear3.getValue() != null) {
            IteratorExcel iteratorExcel3 = new IteratorExcel();
            setFiles(iteratorExcel3, comboYear3);
            buildLineGraphic(iteratorExcel3, comboYear3, serie3);
        }
    }

    private void setFiles(IteratorExcel iteratorExcel, ComboBox comboYear) {
        centre.toExcelSheet(comboCentre.getValue());
        year.toPath((int) comboYear.getValue());
        indicateur.toExcelRow(comboIndic.getValue());
        iteratorExcel.setSheet(centre.getSheet());
        iteratorExcel.setPath(year.getPath());
        iteratorExcel.setFiles(year.getFileA(), year.getFileB(), year.getFileC());
        if (indicateur.getWithFileD()) {
            iteratorExcel.setFiles(year.getFileD(), year.getFileB(), year.getFileC());
        }
        iteratorExcel.setMasterRow(indicateur.getMasterRow());
        iteratorExcel.lineChartIteration();
    }

    private void buildLineGraphic(IteratorExcel iteratorExcel, ComboBox<Integer> comboCentre, Graphic serie) {
        if (indicateur.getwithLineGraphic()) {
            yAxis.setForceZeroInRange(false); // Important for chart scale
            lineChart.setVisible(true);
            noGraphicLabel.setVisible(false);
            idleSpinner.setVisible(false);
            final String[] MONTH = {"Janvier", "Février", "Mars", "Avril", "Mai", "Juin", "Juillet",
                    "Août", "Septembre", "Octobre", "Novembre", "Décembre"};
            double[] value = iteratorExcel.getLineChartResult();
            for (int i = 0; i < MONTH.length; i++) {
                serie.buildLineGraphic(MONTH[i], value[i], comboCentre.getValue().toString());
            }
            lineChart.getData().add(serie.getLineChartData());

            for (final XYChart.Data<String, Float> datas : serie.getLineChartData().getData()) {
                datas.getNode().addEventHandler(MouseEvent.MOUSE_ENTERED, event -> {
                    String strValue = String.format("%,.2f", datas.getYValue());
                    Tooltip tooltip = new Tooltip(strValue);
                    tooltip.setFont(Font.font("INTERSTATE", 14));
                    Tooltip.install(datas.getNode(), tooltip);
                });
            }
        } else {
            unmountLineGraphic();
        }
    }

    private void unmountLineGraphic() {
        indicateur.resetVariables();
        lineChart.setVisible(false);
        noGraphicLabel.setVisible(true);
        idleSpinner.setVisible(false);
    }

    private void clearSeries() {
        serie1.clear();
        serie2.clear();
        serie3.clear();
    }

}

