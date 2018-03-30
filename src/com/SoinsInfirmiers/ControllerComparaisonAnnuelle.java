package SoinsInfirmiers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXSpinner;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import main.Menu;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ControllerComparaisonAnnuelle implements Initializable {


    private Data data = new Data();

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
    private JFXComboBox comboCategorie;
    @FXML
    private AnchorPane anchorPane0;
    @FXML
    private LineChart lineChart;
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

    private Year year = new Year();
    private Category category = new Category();
    private Indicateur indicateur = new Indicateur();
    private Centre centre = new Centre();
    private IteratorExcel iteratorExcel0 = new IteratorExcel();
    private IteratorExcel iteratorExcel1 = new IteratorExcel();
    private IteratorExcel iteratorExcel2 = new IteratorExcel();
    private Graphic serie1 = new Graphic();
    private Graphic serie2 = new Graphic();
    private Graphic serie3 = new Graphic();

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
            lineChart.setTitle("");
            lineChart.getData().clear();
            lineChart.setVisible(false);
            noGraphicLabel.setVisible(false);
            idleSpinner.setVisible(true);

        });
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
            generateYear0();
        }
        if (comboYear1.getValue() != null) {
            generateYear1();
        }
        if (comboYear2.getValue() != null) {
            generateYear2();
        }
        buildLineGraphic();
        iteratorExcel0.resetVariables();
        iteratorExcel1.resetVariables();
        iteratorExcel2.resetVariables();
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
            lineChart.setTitle("");
            lineChart.getData().clear();
            lineChart.setVisible(false);
            idleSpinner.setVisible(true);
        } catch (IllegalStateException e2) {
            System.out.print("Division par zéro !");
        } catch (IOException | InvalidFormatException e1) {
            e1.printStackTrace();
            System.out.print("IO / InvalidFormat");
        } catch (NullPointerException e3) {
            lineChart.setTitle("");
            lineChart.getData().clear();
            lineChart.setVisible(false);
            noGraphicLabel.setVisible(true);
            idleSpinner.setVisible(false);
        }
    }

    private void startIteration1() {
        try {
            iteratorExcel1.allYearIteration();
        } catch (FileNotFoundException e0) {
            iteratorExcel1.fileNotFound(e0);
            lineChart.getData().clear();
        } catch (IllegalStateException e2) {
            System.out.print("Erreur");
        } catch (IOException | InvalidFormatException e1) {
            e1.printStackTrace();
        } catch (NullPointerException e3) {
            lineChart.setTitle("");
            lineChart.getData().clear();
            lineChart.setVisible(false);
            noGraphicLabel.setVisible(true);
        }
    }

    private void startIteration2() {
        try {
            iteratorExcel2.allYearIteration();
        } catch (FileNotFoundException e0) {
            iteratorExcel2.fileNotFound(e0);
            lineChart.getData().clear();
        } catch (IllegalStateException e2) {
            System.out.print("Erreur");
        } catch (IOException | InvalidFormatException e1) {
            e1.printStackTrace();
        } catch (NullPointerException e3) {
            lineChart.setTitle("");
            lineChart.getData().clear();
            lineChart.setVisible(false);
            noGraphicLabel.setVisible(true);
        }
    }

    public void buildLineGraphic() {
        if (indicateur.getwithLineGraphic()) {
            yAxis.setForceZeroInRange(false);
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
        lineChart.setVisible(false);
        noGraphicLabel.setVisible(true);
        idleSpinner.setVisible(false);
    }

}

