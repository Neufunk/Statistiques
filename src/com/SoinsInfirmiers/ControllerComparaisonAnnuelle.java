package SoinsInfirmiers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXSpinner;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import main.Menu;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ControllerComparaisonAnnuelle implements Initializable {

    private final Data data = new Data();

    @FXML
    private JFXComboBox<String> comboCentre;
    @FXML
    private JFXComboBox<Integer> comboYear0;
    @FXML
    private JFXComboBox<Integer> comboYear1;
    @FXML
    private JFXComboBox<Integer> comboYear2;
    @FXML
    private JFXComboBox<String> comboIndic;
    @FXML
    private JFXButton generateButton;
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
    private VBox menuPane;

    private final Year year = new Year();
    private final Category category = new Category();
    private final Indicateur indicateur = new Indicateur();
    private final Centre centre = new Centre();
    private final IteratorExcel iteratorExcel0 = new IteratorExcel();
    private final IteratorExcel iteratorExcel1 = new IteratorExcel();
    private final IteratorExcel iteratorExcel2 = new IteratorExcel();
    private final Graphic serie1 = new Graphic();
    private final Graphic serie2 = new Graphic();
    private final Graphic serie3 = new Graphic();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Menu menu = new Menu();
        menu.loadMenuBar(menuPane);
        initializeCombo();
        onGenerateButtonClick();
        onClearButtonClick();
        onRedCrossClick();
    }

    private void initializeCombo() {
        comboCentre.setItems(data.centerList);
        comboYear0.setItems(data.yearList);
        comboYear1.setItems(data.yearList);
        comboYear2.setItems(data.yearList);
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
        comboYear0.getSelectionModel().clearSelection();
        comboYear1.getSelectionModel().clearSelection();
        comboYear2.getSelectionModel().clearSelection();
        comboCategorie.getSelectionModel().clearSelection();
        comboIndic.getSelectionModel().clearSelection();
    }

    private void onRedCrossClick() {
        redCross0.addEventHandler(MouseEvent.MOUSE_RELEASED, (e) -> {
            comboYear0.getSelectionModel().clearSelection();
            iteratorExcel0.resetVariables();
        });
        redCross1.addEventHandler(MouseEvent.MOUSE_RELEASED, (e) -> {
            comboYear1.getSelectionModel().clearSelection();
            iteratorExcel1.resetVariables();
        });
        redCross2.addEventHandler(MouseEvent.MOUSE_RELEASED, (e) -> {
            comboYear2.getSelectionModel().clearSelection();
            iteratorExcel2.resetVariables();
        });
    }

    private void onGenerateButtonClick() {
        generateButton.addEventHandler(MouseEvent.MOUSE_CLICKED, (e) -> {
            if (checkEmpty()) {
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
        if (comboYear0.getValue() != null) {
            generateYear(iteratorExcel0, comboYear0);
        }
        if (comboYear1.getValue() != null) {
            generateYear(iteratorExcel1, comboYear1);
        }
        if (comboYear2.getValue() != null) {
            generateYear(iteratorExcel2, comboYear2);
        }
        buildLineGraphic();
        iteratorExcel0.resetVariables();
        iteratorExcel1.resetVariables();
        iteratorExcel2.resetVariables();
    }

    private void generateYear(IteratorExcel iteratorExcel, ComboBox comboYear) {
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
        startIteration(iteratorExcel);
    }

    private void startIteration(IteratorExcel iteratorExcel) {
        try {
            iteratorExcel.allYearIteration();
        } catch (FileNotFoundException e0) {
            iteratorExcel.fileNotFound(e0);
            lineChart.setTitle("");
            lineChart.getData().clear();
            lineChart.setVisible(false);
            idleSpinner.setVisible(true);
        } catch (Exception e) {
            lineChart.setTitle("");
            lineChart.getData().clear();
            lineChart.setVisible(false);
            noGraphicLabel.setVisible(true);
            idleSpinner.setVisible(false);
        }
    }

    public void buildLineGraphic() {
        if (indicateur.getwithLineGraphic()) {
            yAxis.setForceZeroInRange(false); // Important for chart scale
            lineChart.getData().clear();
            iteratorExcel0.resetVariables();
            iteratorExcel1.resetVariables();
            iteratorExcel2.resetVariables();
            serie1.clear();
            serie2.clear();
            serie3.clear();
            indicateur.resetVariables();
            lineChart.setVisible(true);
            noGraphicLabel.setVisible(false);
            idleSpinner.setVisible(false);
            String[] month = {"Janvier", "Février", "Mars", "Avril", "Mai", "Juin", "Juillet",
                    "Août", "Septembre", "Octobre", "Novembre", "Décembre"};

            if (comboYear0.getValue() != null) {
                double[] value = {iteratorExcel0.getContentJanvierCell(), iteratorExcel0.getContentFevrierCell(),
                        iteratorExcel0.getContentMarsCell(), iteratorExcel0.getContentAvrilCell(),
                        iteratorExcel0.getContentMaiCell(), iteratorExcel0.getContentJuinCell(), iteratorExcel0.getContentJuilletCell(),
                        iteratorExcel0.getContentAoutCell(), iteratorExcel0.getContentSeptembreCell(),
                        iteratorExcel0.getContentOctobreCell(), iteratorExcel0.getContentNovembreCell(), iteratorExcel0.getContentDecembreCell()};
                for (int i = 0; i < month.length; i++) {
                    serie1.buildLineGraphic(month[i], value[i], comboYear0.getValue().toString());
                }
                lineChart.getData().add(serie1.getLineChartData());
            } else {
                serie1.clear();
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
                lineChart.getData().add(serie2.getLineChartData());
            } else {
                serie2.clear();
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
                lineChart.getData().add(serie3.getLineChartData());
            } else {
                serie3.clear();
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

}

