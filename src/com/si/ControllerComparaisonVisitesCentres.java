package si;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.util.Callback;
import tools.*;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;

public class ControllerComparaisonVisitesCentres implements Initializable {

    @FXML
    public AnchorPane menuPane;
    @FXML
    private TableView<ObservableList> tableView;
    @FXML
    private BarChart barChart;
    @FXML
    private TextField yearPicker1;
    @FXML
    private GridPane waitingPane;

    final private Database database = new Database();
    final private DatabaseConnection dbco = new DatabaseConnection();
    final private Identification id = new Identification();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        menuPane.getChildren().get(0).getStyleClass().add("white");
        yearPicker1.setText(Date.getCurrentYearStr());
    }

    public void onAction() {
        if (checkEmpty()) {
            waitingPane.setVisible(true);
            tableView.getColumns().clear();
            barChart.getData().clear();
            new Thread(this::startQuery).start();
        }
    }

    private void startQuery() {
        int rowCount = 1;
        Platform.runLater(() -> tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY));
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String fromYear = yearPicker1.getText();
        String toYear = String.valueOf(Integer.parseInt(fromYear) + 1);
        ObservableList<ObservableList> data = FXCollections.observableArrayList();
        XYChart.Series series = new XYChart.Series();
        try {
            conn = dbco.connect(
                    id.set(Identification.info.D615_URL),
                    id.set(Identification.info.D615_USER),
                    id.set(Identification.info.D615_PASSWD),
                    id.set(Identification.info.D615_DRIVER)
            );
            String query = database.selectQuery(Database.Query.VISITES_PAR_CENTRE);
            ps = conn.prepareStatement(query);
            ps.setDate(1, java.sql.Date.valueOf("" + fromYear + "-01-01"));
            ps.setDate(2, java.sql.Date.valueOf("" + toYear + "-01-01"));
            rs = ps.executeQuery();

            for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                final int j = i - 1;
                TableColumn col = new TableColumn(rs.getMetaData().getColumnName(i));
                col.setCellFactory(TextFieldTableCell.forTableColumn());
                col.setCellValueFactory((Callback<TableColumn.CellDataFeatures<ObservableList, String>, ObservableValue<String>>) param -> new SimpleStringProperty(param.getValue().get(j).toString()));
                Platform.runLater(() -> tableView.getColumns().addAll(col));
                System.out.println("Column [" + i + "] ");
            }
            while (rs.next()) {
                ObservableList<String> row = FXCollections.observableArrayList();
                for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                    row.add(rs.getString(i));
                }
                System.out.println("Row [" + rowCount + "]" + row);
                rowCount++;
                data.add(row);
                // Starting rowCount @ 2 to avoid having the 'ALL' column in the chart
                if (rowCount > 2) {
                    series.getData().add(new XYChart.Data<>(rs.getString(1), rs.getDouble(14)));
                }
            }
            tableView.setItems(data);
            Platform.runLater(() -> generateBarChart(series));
        } catch (Exception e) {
            ExceptionHandler.switchException(e, this.getClass());
        } finally {
            waitingPane.setVisible(false);
            dbco.close(rs);
            dbco.close(ps);
            dbco.close(conn);
        }
    }

    private boolean checkEmpty() {
        if (yearPicker1.getText().length() != 4) {
            EmptyChecker.showEmptyYearDialog();
            return false;
        } else {
            return true;
        }
    }

    private void generateBarChart(XYChart.Series series) {
        barChart.getData().add(series);
    }
}
