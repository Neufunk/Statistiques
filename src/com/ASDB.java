package com;

import AVJ.Database;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.postgresql.util.PSQLException;

public class ASDB {

    Database database = new Database();

    @FXML
    private JFXTextField tableNameField;
    @FXML
    private TableView<?> issueDataList;
    @FXML
    public TabPane tabPane;
    @FXML
    private TableColumn<?, ?> c1;
    @FXML
    private TableColumn<?, ?> c2;

    public void displayTable() {
        database.connect();
        issueDataList.setItems(database.loadTabletoList(tableNameField.getText()));
        System.out.println("Envoi de la table " + tableNameField.getText());
        database.closeConnection();
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
