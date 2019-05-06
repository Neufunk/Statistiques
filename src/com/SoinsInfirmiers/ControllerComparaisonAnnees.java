package SoinsInfirmiers;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXSpinner;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import main.Date;
import main.ExceptionHandler;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;

public class ControllerComparaisonAnnees implements Initializable {
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
    private final Indicateur indicateur = new Indicateur();
    private final Database database = new Database();
    private final Centre centre = new Centre();

    private Connection conn = null;
    private PreparedStatement ps = null;
    private ResultSet rs = null;
    private int colorCounter = 0;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        menuPane.getChildren().get(0).getStyleClass().add("green");
        initializeCombo();
        onRedCrossClick();
    }

    private void initializeCombo() {
        comboCentre.getItems().addAll(centre.CENTER_NAME);

        int[] yearList = Date.getYearList();
        int currentYear = Date.getCurrentYearInt() - 2;
        JFXComboBox[] comboYearArray = new JFXComboBox[]{comboYear1, comboYear2, comboYear3};
        for (JFXComboBox combo : comboYearArray) {
            combo.setValue(currentYear);
            for (int value : yearList) {
                combo.getItems().add(value);
            }
            currentYear++;
        }

        comboCategorie.getItems().addAll(database.categorie);
    }

    public void setIndicateursInCombo() {
        if (comboCategorie.getValue() != null) {
            comboIndic.getItems().clear();
            int index = comboCategorie.getSelectionModel().getSelectedIndex();
            for (int i = 0; i < database.indicateurArray[index].length; i++) {
                comboIndic.getItems().add(database.indicateurArray[index][i].toString().replace("_", " "));
            }
        }
    }

    public void onClearButtonClick() {
        clearCombos();
        lineChart.setTitle("");
        lineChart.getData().clear();
        lineChart.setVisible(false);
        noGraphicLabel.setVisible(false);
        idleSpinner.setVisible(true);
    }

    private void clearCombos() {
        comboCentre.getSelectionModel().clearSelection();
        comboYear1.getSelectionModel().clearSelection();
        comboYear2.getSelectionModel().clearSelection();
        comboYear3.getSelectionModel().clearSelection();
        comboCategorie.getSelectionModel().clearSelection();
        comboIndic.getSelectionModel().clearSelection();
    }

    private void onRedCrossClick() {
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
            lineChart.getData().clear();
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

    private void generateAll() throws Exception {
        colorCounter = 0;
        Database.Query currentIndicateur = database.indicateurArray[comboCategorie.getSelectionModel().getSelectedIndex()][comboIndic.getSelectionModel().getSelectedIndex()];
        conn = database.connect();
        final int CENTRE_NO = centre.CENTER_NO[comboCentre.getSelectionModel().getSelectedIndex()];
        String query = database.selectQuery(currentIndicateur);
        ps = conn.prepareStatement(query);

        if (comboYear1.getValue() != null) {
            rs = database.setQuery(currentIndicateur, ps, comboYear1.getValue(), CENTRE_NO);
            buildLineGraphic(rs, comboYear1.getValue().toString());
        }
        if (comboYear2.getValue() != null) {
            rs = database.setQuery(currentIndicateur, ps, comboYear2.getValue(), CENTRE_NO);
            buildLineGraphic(rs, comboYear2.getValue().toString());
        }
        if (comboYear3.getValue() != null) {
            rs = database.setQuery(currentIndicateur, ps, comboYear3.getValue(), CENTRE_NO);
            buildLineGraphic(rs, comboYear3.getValue().toString());
        }
    }

    private void buildLineGraphic(ResultSet rs, String year) {
        XYChart.Series series = new XYChart.Series();
        try {
            yAxis.setForceZeroInRange(false); // Important for chart scale
            lineChart.setVisible(true);
            noGraphicLabel.setVisible(false);
            idleSpinner.setVisible(false);
            final String[] MONTH = {"Janvier", "Février", "Mars", "Avril", "Mai", "Juin", "Juillet",
                    "Août", "Septembre", "Octobre", "Novembre", "Décembre"};
            int i = 0;
            while (rs.next()) {
                series.setName(year);
                XYChart.Data<String, Double> data = new XYChart.Data<>(MONTH[i], rs.getDouble("TOTAL"));
                data.setNode(new Graphic.HoveredNode(rs.getDouble("TOTAL"), colorCounter));
                series.getData().add(data);
                System.out.println(i);
                i++;
            }
            lineChart.getData().add(series);
            lineChart.setTitle(comboIndic.getValue());
            colorCounter++;
        } catch (Exception e) {
            ExceptionHandler.switchException(e, this.getClass());
        }
    }
}

