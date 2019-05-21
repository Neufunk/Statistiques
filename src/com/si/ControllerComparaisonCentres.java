package si;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXSpinner;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import tools.*;

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
    private JFXComboBox<String> comboCentre1, comboCentre2, comboCentre3, comboCentre4, comboCentre5, comboIndic, comboCategorie;
    @FXML
    private ImageView redCross1, redCross2, redCross3, redCross4, redCross5;
    @FXML
    private JFXSpinner idleSpinner;
    @FXML
    private AnchorPane menuPane;
    @FXML
    private LineChart<String, Float> lineChart;
    @FXML
    private NumberAxis yAxis;

    private final Year year = new Year();
    private final Centre centre = new Centre();
    private final Database database = new Database();
    private final Indicateur indicateur = new Indicateur();
    private final Effects effects = new Effects();
    private Connection conn = null;
    private PreparedStatement ps = null;
    private ResultSet rs = null;
    private int colorCounter = 0;

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
        for (int i = 0; i < database.CATEGORIE.length; i++) {
            comboCategorie.getItems().addAll(database.CATEGORIE[i]);
        }
    }

    public void setIndicateursInCombo() {
        if (comboCategorie.getValue() != null) {
            comboIndic.getItems().clear();
            int index = comboCategorie.getSelectionModel().getSelectedIndex();
            for (int i = 0; i < database.INDICATEUR_ARRAY[index].length; i++) {
                System.out.println(database.INDICATEUR_ARRAY[index][i].toString());
                comboIndic.getItems().addAll(database.INDICATEUR_ARRAY[index][i].toString().replace("_", " "));
            }
        }
    }

    public void onGenerateButtonClick() {
        effects.setFadeTransition(lineChart, 200, 1, 0);
        lineChart.getData().clear();
        idleSpinner.setVisible(true);
        if (checkEmpty()) {
            new Thread(() -> {
                try {
                    generateAll();
                } catch (Exception e) {
                    ExceptionHandler.switchException(e, this.getClass());
                } finally {
                    database.close(rs);
                    database.close(ps);
                    database.close(conn);
                }
            }).start();
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
        effects.setFadeTransition(lineChart, 200, 1, 0);
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
        colorCounter = 0;
        Database.Query currentIndicateur = database.INDICATEUR_ARRAY[comboCategorie.getSelectionModel().getSelectedIndex()][comboIndic.getSelectionModel().getSelectedIndex()];
        int year = comboYear.getValue();
        conn = database.connect();
        String query = database.selectQuery(currentIndicateur);
        ps = conn.prepareStatement(query);
        if (comboCentre1.getValue() != null) {
            rs = database.setQuery(currentIndicateur, ps, year, centre.CENTER_NO[comboCentre1.getSelectionModel().getSelectedIndex()]);
            addDataToGraphic(rs, comboCentre1.getValue());
        }
        if (comboCentre2.getValue() != null) {
            rs = database.setQuery(currentIndicateur, ps, year, centre.CENTER_NO[comboCentre2.getSelectionModel().getSelectedIndex()]);
            addDataToGraphic(rs, comboCentre2.getValue());
        }
        if (comboCentre3.getValue() != null) {
            rs = database.setQuery(currentIndicateur, ps, year, centre.CENTER_NO[comboCentre3.getSelectionModel().getSelectedIndex()]);
            addDataToGraphic(rs, comboCentre3.getValue());
        }
        if (comboCentre4.getValue() != null) {
            rs = database.setQuery(currentIndicateur, ps, year, centre.CENTER_NO[comboCentre4.getSelectionModel().getSelectedIndex()]);
            addDataToGraphic(rs, comboCentre4.getValue());
        }
        if (comboCentre5.getValue() != null) {
            rs = database.setQuery(currentIndicateur, ps, year, centre.CENTER_NO[comboCentre5.getSelectionModel().getSelectedIndex()]);
            addDataToGraphic(rs, comboCentre5.getValue());
        }
        Platform.runLater(this::buildGraphic);
    }

    private void addDataToGraphic(ResultSet rs, String centre) {
        XYChart.Series series = new XYChart.Series();
        try {
            yAxis.setForceZeroInRange(false); // Important for chart scale
            lineChart.setVisible(true);
            idleSpinner.setVisible(false);
            String[] MONTH = {"Janvier", "Février", "Mars", "Avril", "Mai", "Juin", "Juillet",
                    "Août", "Septembre", "Octobre", "Novembre", "Décembre"};
            int i = 0;
            while (rs.next()) {
                series.setName(centre);
                XYChart.Data<String, Double> data = new XYChart.Data<>(MONTH[i], rs.getDouble("TOTAL"));
                data.setNode(new HoveredNode(rs.getDouble("TOTAL"), colorCounter));
                series.getData().add(data);
                System.out.println(MONTH[i]);
                i++;
            }
            int finalI = i;
            Platform.runLater(() -> Console.append(finalI + "mois ajoutés au graphique avec succès"));
            Platform.runLater(() -> lineChart.getData().add(series));
            colorCounter++;
        } catch (Exception e) {
            ExceptionHandler.switchException(e, this.getClass());
        }
    }

    private void buildGraphic(){
        lineChart.setTitle(comboIndic.getValue());
        effects.setFadeTransition(lineChart, 200, 0, 1);
        idleSpinner.setVisible(false);
    }
}
