package main;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;
import tools.*;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;

public class Log implements Initializable {
    @FXML
    private BarChart<?, ?> barChart;
    @FXML
    private PieChart pieChart;
    @FXML
    private ScatterChart<?, ?> scatterChart;
    @FXML
    private TableView<ObservableList> logTable;

    private final Identification id = new Identification();
    private final DatabaseConnection db = new DatabaseConnection();

    private final String URL = id.set(Identification.info.D03_URL);
    private final String USER = id.set(Identification.info.D03_USER);
    private final String PASSWD = id.set(Identification.info.D03_PASSWD);
    private final String DRIVER = id.set(Identification.info.D03_DRIVER);

    private final Connection conn = db.connect(URL, USER, PASSWD, DRIVER);
    private PreparedStatement ps;
    private ResultSet rs;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            populateLogTable();
            populatePieChart();
            populateBarChart();
            populateScatterChart();
        } catch (Exception e) {
            ExceptionHandler.switchException(e, this.getClass());
        } finally {
            db.close(conn);
        }
    }

    private void populateLogTable() throws Exception {
        ObservableList<ObservableList> observableList = FXCollections.observableArrayList();
        String query = "SELECT date, \"user\", ip_adress, host_name, software_version " +
                "FROM global.log_application_launched " +
                "ORDER BY date DESC";
        int rowCount = 1;
        ps = conn.prepareStatement(query);
        rs = ps.executeQuery();
        for (int i = 0; i < rs.getMetaData().getColumnCount(); i++) {
            final int j = i;
            TableColumn col = new TableColumn(rs.getMetaData().getColumnName(i + 1));
            col.setCellFactory(TextFieldTableCell.forTableColumn());
            col.setCellValueFactory((Callback<TableColumn.CellDataFeatures<ObservableList, String>, ObservableValue<String>>) param -> new SimpleStringProperty(param.getValue().get(j).toString()));
            logTable.getColumns().addAll(col);
            System.out.println("Column [" + i + "] " + rs.getMetaData().getColumnName(i + 1));
            Console.appendln("Column [" + i + "] " + rs.getMetaData().getColumnName(i + 1));
        }
        while (rs.next()) {
            ObservableList<String> row = FXCollections.observableArrayList();
            for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                row.add(rs.getString(i));
            }
            System.out.println("Row [" + rowCount + "] - " + row);
            Console.appendln("Row [" + rowCount + "] - " + row);
            rowCount++;
            observableList.add(row);
        }
        System.out.println(rowCount-1 + " entrées \n");
        Console.appendln(rowCount-1 + " entrées \n");
        logTable.setItems(observableList);
        db.close(rs);
        db.close(ps);
    }

    private void populatePieChart() throws Exception {
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
        String query = "SELECT \"user\", count(\"user\") FROM global.log_application_launched GROUP BY \"user\"";
        ps = conn.prepareStatement(query);
        rs = ps.executeQuery();
        while (rs.next()){
            pieChartData.add(new PieChart.Data(rs.getString(1), rs.getDouble(2)));
        }
        pieChart.getData().addAll(pieChartData);
        pieChart.setVisible(true);
        for (final PieChart.Data data : pieChart.getData()) {
            data.getNode().addEventHandler(MouseEvent.MOUSE_ENTERED, (e1) ->
                    Tooltip.install(data.getNode(), new Tooltip(data.getName() + " - " + String.format("%,.0f", data.getPieValue()) + " connexions")));
        }
        System.out.println("PieChart OK \n");
        db.close(rs);
        db.close(ps);
    }

    private void populateBarChart() throws Exception {
        XYChart.Series series = new XYChart.Series();
        String query = "SELECT TO_DATE(SUBSTRING(date, 1, 10), 'yyyy-mm-dd') AS newdate, count(\"user\") " +
                "FROM global.log_application_launched " +
                "WHERE TO_DATE(SUBSTRING(date, 1, 10), 'yyyy-mm-dd') > current_date - 31 " +
                "GROUP BY newdate " +
                "ORDER BY newdate ASC";
        ps = conn.prepareStatement(query);
        rs = ps.executeQuery();
        while (rs.next()){
            series.getData().add(new XYChart.Data<>(rs.getString(1), rs.getDouble(2)));
        }
        barChart.getData().add(series);
        System.out.println("BarChart OK \n");
        db.close(rs);
        db.close(ps);
    }

    private void populateScatterChart() throws Exception {
        XYChart.Series series = new XYChart.Series();
        String query = "SELECT software_version, count(software_version) " +
                "FROM global.log_application_launched " +
                "GROUP BY software_version " +
                "ORDER BY software_version ASC";
        ps = conn.prepareStatement(query);
        rs = ps.executeQuery();
        while (rs.next()){
            XYChart.Data<String, Double> data = new XYChart.Data<>(rs.getString(1), rs.getDouble(2));
            data.setNode(new HoveredNode(rs.getDouble(2), 0));
            series.getData().add(data);
        }
        scatterChart.getData().add(series);
        System.out.println("ScatterPlot OK \n");
        db.close(rs);
        db.close(ps);
    }
}
