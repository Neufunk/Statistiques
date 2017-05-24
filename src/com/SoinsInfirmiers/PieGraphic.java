package com.SoinsInfirmiers;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.PieChart;

public class PieGraphic {

    ObservableList<PieChart.Data>pieChartData = FXCollections.observableArrayList();


    public void buildGraphic(String title, double value){
        if (title != "" && value > 0){
            pieChartData.add(new PieChart.Data(title, value));
        }
    }

    public ObservableList<PieChart.Data>getPieChartData(){
        return pieChartData;
    }
}
