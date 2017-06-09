package com.SoinsInfirmiers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;

public class Graphic {

    ObservableList<PieChart.Data>pieChartData = FXCollections.observableArrayList();
    XYChart.Series lineChartData = new XYChart.Series();

    //PIE GRAPHIC
    public void buildPieGraphic(String title, double value){
        if (title != "" && value > 0){
            pieChartData.add(new PieChart.Data(title, value*100));
        }
    }

    public ObservableList<PieChart.Data> getPieChartData(){
        return pieChartData;
    }


    //LINEGRAPHIC
    public void buildLineGraphic(String title, double value, String xName) {
        if (title != "" && value > 0) {
            if (value < 1 && value > 0) {
                float floatValue = (float) value * 100;
                lineChartData.getData().add(new XYChart.Data<>(title, floatValue));
            } else {
                lineChartData.getData().add(new XYChart.Data<>(title, value));
            }
            lineChartData.setName(xName);
        }
    }

    public XYChart.Series getLineChartData(){
        return lineChartData;
    }


    //RAW DATA
    public void setRawDataName(Label label, String title){
        if (title != ""){
            label.setText(title);
            label.setVisible(true);
        }else{
            label.setText("");
            label.setVisible(false);
        }
    }

    public void setMasterRawDataValue(Label label, double value){
        if (value == 0) {
            label.setText("");
            label.setVisible(false);
        }else if (value == 1) {
            int intValue = (int) value * 100;
            label.setText(String.valueOf(intValue+"%"));
            label.setVisible(true);
        }else if (value > 1) {
            float floatValue = (float) value;
            String strValue = String.format("%,.2f", floatValue);
            label.setText(strValue);
            label.setVisible(true);
        }else if (value > 0 && value < 1){
            float floatValue = (float) value*100;
            String strValue = String.format("%,.2f", floatValue);
            label.setText(strValue+"%");
            label.setVisible(true);
        }else{
            label.setText("");
            label.setVisible(false);
        }
    }

    public void setRawDataValue(Label label, double value){
        if (value > 1) {
            float floatValue = (float) value;
            String strValue = String.format("%,.2f", floatValue);
            label.setText(String.valueOf(strValue));
            label.setVisible(true);
        }else if (value < 1 && value > 0) {
            float floatValue = (float) value * 100;
            String strValue = String.format("%,.2f", floatValue);
            label.setText(String.valueOf(strValue + "%"));
            label.setVisible(true);
        }else if (value == 1){
            float floatValue = (float) value;
            label.setText(String.valueOf(floatValue));
            label.setVisible(true);
        }else if (value == 0){
            label.setText("");
            label.setVisible(false);
        }else{
            label.setText("");
            label.setVisible(false);
        }
    }
}
