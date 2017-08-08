package com;

import AVJ.Database;
import com.jfoenix.controls.JFXTextField;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.Callback;

import java.sql.Connection;
import java.sql.ResultSet;

public class ASDB {

    Database database = new Database();

    @FXML
    private JFXTextField tableNameField;
    @FXML
    private TableView<ObservableList> issueDataList;
    @FXML
    public TabPane tabPane;

    private ObservableList<ObservableList> data;

    public void displayTable() {
        Connection c;
        data = FXCollections.observableArrayList();
        try {
            c = database.connect();
            String SQL = "SELECT * FROM "+ tableNameField.getText();
            ResultSet rs = c.createStatement().executeQuery(SQL);

            //TABLE COLUMN ADDED DYNAMICALLY

            for (int i = 0; i < rs.getMetaData().getColumnCount(); i++) {
                //non property style for making dynamic table
                final int j = i;
                TableColumn col = new TableColumn(rs.getMetaData().getColumnName(i + 1));
                col.setCellFactory(TextFieldTableCell.forTableColumn());
                col.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ObservableList, String>, ObservableValue<String>>() {
                    public ObservableValue<String> call(TableColumn.CellDataFeatures<ObservableList, String> param) {
                        return new SimpleStringProperty(param.getValue().get(j).toString());
                    }
                });
                issueDataList.getColumns().addAll(col);
                System.out.println("Column [" + i + "] ");
            }
            //Data added to ObservableList
            while (rs.next()) {
                //Iterate Row
                ObservableList<String> row = FXCollections.observableArrayList();
                for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                    //Iterate Column
                    row.add(rs.getString(i));
                }
                System.out.println("Row [1] ajoutée " + row);
                data.add(row);
            }
            //ADDED TO TableView
            issueDataList.setItems(data);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Erreur lors de la construction des données");
        }
    }

    public void aboutWindow(){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("ASDB Engine - 1.0");
        alert.setHeaderText("Aide & Soins à Domicile Database Engine");
        alert.setContentText("2017 - Aide & Soins à Domicile en province de Namur\n" +
                " Developer & Designer : Johnathan Vanbeneden");
        alert.show();
    }

    public void switchToWorkersTab() {
        tabPane.getSelectionModel().select(0);
    }
    public void switchToSectorTab() {
        tabPane.getSelectionModel().select(1);
    }
    public void switchToDBissueTab() {
        tabPane.getSelectionModel().select(2);
    }
    public void switchToTablesTab() {
        tabPane.getSelectionModel().select(3);
    }
    public void switchToSettingsTab() {
        tabPane.getSelectionModel().select(4);
    }


}
