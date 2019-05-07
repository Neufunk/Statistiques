package SoinsInfirmiers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Cursor;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

import java.util.Objects;

class Graphic {

    private final ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();

    //PIE GRAPHIC
    void buildPieGraphic(String title, double value) {
        if (!Objects.equals(title, "") && value > 0) {
            pieChartData.add(new PieChart.Data(title, value * 100));
        }
    }

    ObservableList<PieChart.Data> getPieChartData() {
        return pieChartData;
    }

    //RAW DATA
    void setRawDataName(Label label, String title) {
        if (!Objects.equals(title, "")) {
            label.setText(title);
            label.setVisible(true);
        } else {
            label.setText("");
            label.setVisible(false);
        }
    }

    void setMasterRawDataValue(Label label, double value) {
        if (value == 0) {
            label.setText("");
            label.setVisible(false);
        } else if (value == 1) {
            int intValue = (int) value * 100;
            label.setText(intValue + "%");
            label.setVisible(true);
        } else if (value > 1) {
            float floatValue = (float) value;
            String strValue = String.format("%,.2f", floatValue);
            label.setText(strValue);
            label.setVisible(true);
        } else if (value > 0 && value < 1) {
            float floatValue = (float) value * 100;
            String strValue = String.format("%,.2f", floatValue);
            label.setText(strValue + "%");
            label.setVisible(true);
        } else {
            label.setText("");
            label.setVisible(false);
        }
    }

    void setRawDataValue(Label label, double value) {
        if (value > 1) {
            float floatValue = (float) value;
            String strValue = String.format("%,.2f", floatValue);
            label.setText(String.valueOf(strValue));
            label.setVisible(true);
        } else if (value < 1 && value > 0) {
            float floatValue = (float) value * 100;
            String strValue = String.format("%,.2f", floatValue);
            label.setText(strValue + "%");
            label.setVisible(true);
        } else if (value == 1) {
            float floatValue = (float) value;
            label.setText(String.valueOf(floatValue));
            label.setVisible(true);
        } else if (value == 0) {
            label.setText("");
            label.setVisible(false);
        } else {
            label.setText("");
            label.setVisible(false);
        }
    }
}
