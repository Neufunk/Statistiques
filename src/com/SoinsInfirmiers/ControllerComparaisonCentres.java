package SoinsInfirmiers;

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

public class ControllerComparaisonCentres implements Initializable {

    @FXML
    private JFXComboBox<Integer> comboYear;
    @FXML
    private JFXComboBox<String> comboCentre1;
    @FXML
    private JFXComboBox<String> comboCentre2;
    @FXML
    private JFXComboBox<String> comboCentre3;
    @FXML
    private JFXComboBox<String> comboCentre4;
    @FXML
    private JFXComboBox<String> comboCentre5;
    @FXML
    private JFXComboBox<String> comboIndic;
    @FXML
    private JFXComboBox<String> comboCategorie;
    @FXML
    private ImageView redCross1;
    @FXML
    private ImageView redCross2;
    @FXML
    private ImageView redCross3;
    @FXML
    private ImageView redCross4;
    @FXML
    private ImageView redCross5;
    @FXML
    private Label noGraphicLabel;
    @FXML
    private JFXSpinner idleSpinner;
    @FXML
    private AnchorPane menuPane;
    @FXML
    private LineChart<String, Float> lineChart;
    @FXML
    private NumberAxis yAxis;

    private Year year = new Year();
    private Centre centre = new Centre();
    private Database database = new Database();
    private Indicateur indicateur = new Indicateur();
    private final Graphic serie1 = new Graphic();
    private final Graphic serie2 = new Graphic();
    private final Graphic serie3 = new Graphic();
    private final Graphic serie4 = new Graphic();
    private final Graphic serie5 = new Graphic();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        menuPane.getChildren().get(0).getStyleClass().add("brown");
        initializeCombo();
        onRedCrossClick();
    }

    private void initializeCombo() {
        Data data = new Data();
        comboYear.setItems(data.yearList);
        JFXComboBox[] comboCenterArray = {comboCentre1, comboCentre2, comboCentre3, comboCentre4, comboCentre5};
        for (JFXComboBox aComboCenterArray : comboCenterArray) {
            aComboCenterArray.setItems(data.centerList);
        }
        for (int i = 0; i < database.categorie.length; i++) {
            comboCategorie.getItems().addAll(database.categorie[i]);
        }
    }

    public void setIndicateursInCombo() {
        if (comboCategorie.getValue() != null) {
            comboIndic.getItems().clear();
            int index = comboCategorie.getSelectionModel().getSelectedIndex();
            for (int i = 0; i < database.indicateurArray[index].length; i++) {
                System.out.println(database.indicateurArray[index][i].toString());
                String indicateurArray = database.indicateurArray[index][i].toString();
                comboIndic.getItems().addAll(database.indicateurArray[index][i].toString());
            }
        }
    }

    public void onGenerateButtonClick() {
        clearSeries();
        lineChart.getData().clear();
        if (checkEmpty()) {
            generateAll();
        }
    }

    private boolean checkEmpty() {
        if (comboYear.getValue() == null) {
            year.showEmptyDialog();
            return false;
        } else if (checkComboCentre()) {
            centre.showEmptyDialog();
            return false;
        } else if (comboIndic.getValue() == null) {
            indicateur.showEmptyDialog();
            return false;
        } else {
            return true;
        }
    }

    private boolean checkComboCentre() {
        return comboCentre1.getValue() == null && comboCentre2.getValue() == null && comboCentre3.getValue() == null &&
                comboCentre4.getValue() == null && comboCentre5.getValue() == null;
    }

    public void onClearButtonClick() {
        clearCombos();
        lineChart.setTitle("");
        lineChart.getData().clear();
        lineChart.setVisible(false);
        noGraphicLabel.setVisible(false);
        idleSpinner.setVisible(true);
    }

    private void onRedCrossClick() {
        redCross1.addEventHandler(MouseEvent.MOUSE_RELEASED, (e) -> {
            comboCentre1.getSelectionModel().clearSelection();
            onGenerateButtonClick();
        });
        redCross2.addEventHandler(MouseEvent.MOUSE_RELEASED, (e) -> {
            comboCentre2.getSelectionModel().clearSelection();
            onGenerateButtonClick();
        });
        redCross3.addEventHandler(MouseEvent.MOUSE_RELEASED, (e) -> {
            comboCentre3.getSelectionModel().clearSelection();
            onGenerateButtonClick();
        });
        redCross4.addEventHandler(MouseEvent.MOUSE_RELEASED, (e) -> {
            comboCentre4.getSelectionModel().clearSelection();
            onGenerateButtonClick();
        });
        redCross5.addEventHandler(MouseEvent.MOUSE_RELEASED, (e) -> {
            comboCentre5.getSelectionModel().clearSelection();
            onGenerateButtonClick();
        });
    }

    public void onAllCenterButtonClick() {
        comboCentre1.getSelectionModel().select(1);
        comboCentre2.getSelectionModel().select(2);
        comboCentre3.getSelectionModel().select(3);
        comboCentre4.getSelectionModel().select(4);
        comboCentre5.getSelectionModel().select(5);
    }

    private void clearCombos() {
        comboYear.getSelectionModel().clearSelection();
        comboCentre1.getSelectionModel().clearSelection();
        comboCentre2.getSelectionModel().clearSelection();
        comboCentre3.getSelectionModel().clearSelection();
        comboCentre4.getSelectionModel().clearSelection();
        comboCentre5.getSelectionModel().clearSelection();
        comboCategorie.getSelectionModel().clearSelection();
        comboIndic.getSelectionModel().clearSelection();
    }

    private void generateAll() {
        if (comboCentre1.getValue() != null) {
            IteratorExcel iteratorExcel1 = new IteratorExcel();
            setFiles(iteratorExcel1, comboCentre1);
            buildLineGraphic(iteratorExcel1, comboCentre1, serie1);
        }
        if (comboCentre2.getValue() != null) {
            IteratorExcel iteratorExcel2 = new IteratorExcel();
            setFiles(iteratorExcel2, comboCentre2);
            buildLineGraphic(iteratorExcel2, comboCentre2, serie2);
        }
        if (comboCentre3.getValue() != null) {
            IteratorExcel iteratorExcel3 = new IteratorExcel();
            setFiles(iteratorExcel3, comboCentre3);
            buildLineGraphic(iteratorExcel3, comboCentre3, serie3);
        }
        if (comboCentre4.getValue() != null) {
            IteratorExcel iteratorExcel4 = new IteratorExcel();
            setFiles(iteratorExcel4, comboCentre4);
            buildLineGraphic(iteratorExcel4, comboCentre4, serie4);
        }
        if (comboCentre5.getValue() != null) {
            IteratorExcel iteratorExcel5 = new IteratorExcel();
            setFiles(iteratorExcel5, comboCentre5);
            buildLineGraphic(iteratorExcel5, comboCentre5, serie5);
        }
    }

    private void setFiles(IteratorExcel iteratorExcel, ComboBox<String> comboCentre) {
        year.toPath(comboYear.getValue());
        centre.toExcelSheet(comboCentre.getValue());
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

    private void buildLineGraphic(IteratorExcel iteratorExcel, ComboBox<String> comboCentre, Graphic serie) {
        if (indicateur.getwithLineGraphic()) {
            yAxis.setForceZeroInRange(false); // Important for chart scale
            lineChart.setVisible(true);
            noGraphicLabel.setVisible(false);
            idleSpinner.setVisible(false);
            String[] month = {"Janvier", "Février", "Mars", "Avril", "Mai", "Juin", "Juillet",
                    "Août", "Septembre", "Octobre", "Novembre", "Décembre"};
            double[] value = iteratorExcel.getLineChartResult();
            for (int i = 0; i < month.length; i++) {
                serie.buildLineGraphic(month[i], value[i], comboCentre.getValue());
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

    private void clearSeries() {
        serie1.clear();
        serie2.clear();
        serie3.clear();
        serie4.clear();
        serie5.clear();
    }

    private void unmountLineGraphic() {
        indicateur.resetVariables();
        lineChart.setVisible(false);
        noGraphicLabel.setVisible(true);
        idleSpinner.setVisible(false);
    }
}
