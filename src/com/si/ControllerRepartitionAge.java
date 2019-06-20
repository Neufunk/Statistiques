package si;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXSpinner;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import tools.Console;
import tools.Date;
import tools.Effects;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;

public class ControllerRepartitionAge implements Initializable {

    @FXML
    private AnchorPane menuPane;
    @FXML
    private BarChart barChart;
    @FXML
    private PieChart pieChart;
    @FXML
    private JFXComboBox<Integer> comboYear;
    @FXML
    private GridPane waitingPane, mainPane;
    @FXML
    private JFXSpinner pieChartSpinner;

    private Database database = new Database();
    private String query = database.selectQuery(Database.Query.PATIENTS_PAR_AGE);
    private Effects effects = new Effects();


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        waitingPane.setVisible(true);
        effects.setBoxBlur(mainPane);
        menuPane.getChildren().get(0).getStyleClass().add("purple");
        initializeCombo();
        new Thread(() -> {
            Connection conn = database.connect();
            try {
                populateBarChart(conn);
                populatePieChart(conn);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                database.close(conn);
            }
            Platform.runLater(() -> waitingPane.setVisible(false));
            Platform.runLater(() -> effects.unsetBoxBlur(mainPane));
        }).start();
    }

    private void initializeCombo() {
        int[] yearList = Date.getYearList();
        for (int value : yearList) {
            comboYear.getItems().addAll(value);
        }
        comboYear.setValue(Date.getCurrentYearInt());
    }

    private void populateBarChart(Connection conn) throws Exception {
        int[] yearList = {Date.getCurrentYearInt()-2, Date.getCurrentYearInt()-1, Date.getCurrentYearInt()};

        for (int value : yearList) {
            PreparedStatement ps = conn.prepareStatement(query);
            XYChart.Series serie = new XYChart.Series();
            serie.setName(String.valueOf(value));
            ResultSet rs = database.setQuery(Database.Query.PATIENTS_PAR_AGE, ps, value, 997);
            while (rs.next()) {
                serie.getData().add(new XYChart.Data<>(rs.getString(1), rs.getDouble(2)));
            }
            Platform.runLater(() -> barChart.getData().add(serie));
        }
    }

    @FXML
    private void populatePieChart(Connection conn) throws Exception {
        pieChart.getData().clear();
        PreparedStatement ps = conn.prepareStatement(query);
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
        ResultSet rs = database.setQuery(Database.Query.PATIENTS_PAR_AGE, ps, comboYear.getValue(), 997);
        Platform.runLater(() -> Console.appendln("\n***** REPARTITION DES PATIENTS PAR ÂGE " + comboYear.getValue().toString() + "*****"));
        while (rs.next()) {
            final String label = rs.getString(1);
            final double result = rs.getDouble(2);
            pieChartData.add(new PieChart.Data(label, result));
            Platform.runLater(() -> Console.appendln(label + " : " + result));
        }
        Platform.runLater(() -> pieChart.getData().addAll(pieChartData));
        Platform.runLater(this::installToolTip);
    }

    private void installToolTip() {
        for (final PieChart.Data data : pieChart.getData()) {
            data.getNode().addEventHandler(MouseEvent.MOUSE_ENTERED, (e1) ->
                    Tooltip.install(data.getNode(), new Tooltip(String.format("%,.0f", data.getPieValue()) + " patients")));
        }
    }

    @FXML
    private void onComboSelection() {
        pieChart.getData().clear();
        pieChartSpinner.setVisible(true);
        new Thread(() -> {
            Connection connection = database.connect();
            try {
                populatePieChart(connection);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                database.close(connection);
            }
            pieChartSpinner.setVisible(false);
        }).start();
    }
}
