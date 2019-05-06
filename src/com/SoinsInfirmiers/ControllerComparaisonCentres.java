package SoinsInfirmiers;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXSpinner;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;
import main.Date;
import main.ExceptionHandler;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Arrays;
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
    private Connection conn = null;
    private PreparedStatement ps = null;
    private ResultSet rs = null;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        menuPane.getChildren().get(0).getStyleClass().add("brown");
        initializeCombo();
        onRedCrossClick();
    }

    private void initializeCombo() {
        String[] centreArray = Arrays.copyOf(centre.CENTER_NAME, centre.CENTER_NAME.length - 1);
        int[] yearList = Date.getYearList();
        for (int value : yearList) {
            comboYear.getItems().addAll(value);
        }
        comboYear.setValue(Date.getCurrentYearInt());
        JFXComboBox[] comboCenterArray = {comboCentre1, comboCentre2, comboCentre3, comboCentre4, comboCentre5};
        for (JFXComboBox aComboCenterArray : comboCenterArray) {
            aComboCenterArray.getItems().addAll(centreArray);
        }
        for (int i = 0; i < database.categorie.length; i++) {
            comboCategorie.getItems().addAll(database.categorie[i]);
        }
        comboIndic.requestFocus();
    }

    public void setIndicateursInCombo() {
        if (comboCategorie.getValue() != null) {
            comboIndic.getItems().clear();
            int index = comboCategorie.getSelectionModel().getSelectedIndex();
            for (int i = 0; i < database.indicateurArray[index].length; i++) {
                System.out.println(database.indicateurArray[index][i].toString());
                comboIndic.getItems().addAll(database.indicateurArray[index][i].toString());
            }
        }
    }

    public void onGenerateButtonClick() {
        clearSeries();
        lineChart.getData().clear();
        if (checkEmpty()) {
            try {
                generateAll();
            } catch (Exception e) {
                ExceptionHandler.switchException(e, this.getClass());
            } finally {
                database.close(rs);
                database.close(ps);
                database.close(conn);
            }
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
        comboCentre1.getSelectionModel().select(0);
        comboCentre2.getSelectionModel().select(1);
        comboCentre3.getSelectionModel().select(2);
        comboCentre4.getSelectionModel().select(3);
        comboCentre5.getSelectionModel().select(4);
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

    private void generateAll() throws Exception {
        Database.Query currentIndicateur = database.indicateurArray[comboCategorie.getSelectionModel().getSelectedIndex()][comboIndic.getSelectionModel().getSelectedIndex()];
        int year = comboYear.getValue();
        conn = database.connect();
        String query = database.selectQuery(currentIndicateur);
        ps = conn.prepareStatement(query);
        if (comboCentre1.getValue() != null) {
            rs = database.setQuery(currentIndicateur, ps, year, centre.CENTER_NO[comboCentre1.getSelectionModel().getSelectedIndex()]);
            buildLineGraphic(rs, serie1, comboCentre1.getValue());
        }
        if (comboCentre2.getValue() != null) {
            rs = database.setQuery(currentIndicateur, ps, year, centre.CENTER_NO[comboCentre2.getSelectionModel().getSelectedIndex()]);
            buildLineGraphic(rs, serie2, comboCentre2.getValue());
        }
        if (comboCentre3.getValue() != null) {
            rs = database.setQuery(currentIndicateur, ps, year, centre.CENTER_NO[comboCentre3.getSelectionModel().getSelectedIndex()]);
            buildLineGraphic(rs, serie3, comboCentre3.getValue());
        }
        if (comboCentre4.getValue() != null) {
            rs = database.setQuery(currentIndicateur, ps, year, centre.CENTER_NO[comboCentre4.getSelectionModel().getSelectedIndex()]);
            buildLineGraphic(rs, serie4, comboCentre4.getValue());
        }
        if (comboCentre5.getValue() != null) {
            rs = database.setQuery(currentIndicateur, ps, year, centre.CENTER_NO[comboCentre5.getSelectionModel().getSelectedIndex()]);
            buildLineGraphic(rs, serie5, comboCentre5.getValue());
        }
    }

    private void buildLineGraphic(ResultSet rs, Graphic serie, String centre) {
        try {
            yAxis.setForceZeroInRange(false); // Important for chart scale
            lineChart.setVisible(true);
            idleSpinner.setVisible(false);
            String[] month = {"Janvier", "Février", "Mars", "Avril", "Mai", "Juin", "Juillet",
                    "Août", "Septembre", "Octobre", "Novembre", "Décembre"};
            int i = 0;

            while (rs.next()) {
                serie.buildLineGraphic(month[i], rs.getDouble("TOTAL"), centre);
                i++;
            }
            lineChart.getData().add(serie.getLineChartData());

            for (final XYChart.Data<String, Float> datas : serie.getLineChartData().getData()) {
                datas.getNode().addEventHandler(MouseEvent.MOUSE_ENTERED, event -> {
                    String strValue = String.format("%,.2f", datas.getYValue());
                    Tooltip tooltip = new Tooltip(strValue);
                    tooltip.setFont(Font.font("PRODUCT SANS", 14));
                    Tooltip.install(datas.getNode(), tooltip);
                });
            }
        } catch (Exception e) {
            ExceptionHandler.switchException(e, this.getClass());
        }
    }

    private void clearSeries() {
        serie1.clear();
        serie2.clear();
        serie3.clear();
        serie4.clear();
        serie5.clear();
    }
}
