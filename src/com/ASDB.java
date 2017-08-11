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

import java.sql.*;

public class ASDB {

    Database database = new Database();

    @FXML
    private JFXTextField tableNameField;
    @FXML
    private TableView<ObservableList> issueDataList;
    @FXML
    public TabPane tabPane;
    @FXML
    private TextField workerTabIdInput;
    @FXML
    private TextField workerTabFirstNameInput;
    @FXML
    private TextField workerTabLastNameInput;
    @FXML
    private TextField workerTabSectorIdInput;
    @FXML
    private TextField workerTabSectorInput;
    @FXML
    private TextField workerTabCentreInput;

    private ObservableList<ObservableList> data;

    public void searchByIdWorkerDatabase() {
        int id = Integer.parseInt(workerTabIdInput.getText());
        String sql = "SELECT *, secteurs.id AS sectorID " +
                "FROM travailleurs " +
                "INNER JOIN secteurs " +
                "ON travailleurs.id = secteurs.worker_id " +
                "WHERE travailleurs.id='"+id+"'";
        Connection conn = database.connect();
        try {
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery(sql);
            while (rs.next()) {
                workerTabFirstNameInput.setText(rs.getString("prenom"));
                workerTabLastNameInput.setText(rs.getString("nom"));
                workerTabSectorIdInput.setText(rs.getString("sectorID"));
                workerTabSectorInput.setText(rs.getString("secteur_name"));
                workerTabCentreInput.setText(rs.getString("antenne"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void searchByFirstNameWorkerDatabase() {
        String sql = "SELECT *, secteurs.id AS sectorID " +
                "FROM travailleurs " +
                "INNER JOIN secteurs " +
                "ON travailleurs.id = secteurs.worker_id " +
                "WHERE prenom ='"+workerTabFirstNameInput.getText()+"'";
        Connection conn = database.connect();
        try {
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery(sql);
            while (rs.next()) {
                workerTabIdInput.setText(rs.getString("id"));
                workerTabFirstNameInput.setText(rs.getString("prenom"));
                workerTabLastNameInput.setText(rs.getString("nom"));
                workerTabSectorIdInput.setText(rs.getString("sectorID"));
                workerTabSectorInput.setText(rs.getString("secteur_name"));
                workerTabCentreInput.setText(rs.getString("antenne"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void searchByLastNameWorkerDatabase() {
        String sql = "SELECT *, secteurs.id AS sectorID " +
                "FROM travailleurs " +
                "INNER JOIN secteurs " +
                "ON travailleurs.id = secteurs.worker_id " +
                "WHERE nom='"+workerTabLastNameInput.getText()+"'";
        Connection conn = database.connect();
        try {
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery(sql);
            while (rs.next()) {
                workerTabIdInput.setText(rs.getString("id"));
                workerTabFirstNameInput.setText(rs.getString("prenom"));
                workerTabLastNameInput.setText(rs.getString("nom"));
                workerTabSectorIdInput.setText(rs.getString("sectorID"));
                workerTabSectorInput.setText(rs.getString("secteur_name"));
                workerTabCentreInput.setText(rs.getString("antenne"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void clearTextFieldsWorkerTab(){
        workerTabIdInput.clear();
        workerTabFirstNameInput.clear();
        workerTabLastNameInput.clear();
        workerTabSectorIdInput.clear();
        workerTabSectorInput.clear();
        workerTabCentreInput.clear();
    }

    public void updateWorkerDatabase(){
        int id = Integer.parseInt(workerTabIdInput.getText());
        String sql = "UPDATE travailleurs "+
                "SET prenom = '"+workerTabFirstNameInput.getText()+"', "+
                "nom = '"+workerTabLastNameInput.getText()+"' "+
                "WHERE id = "+id+"";
        String sql2 = "UPDATE secteurs "+
                "SET id = '"+workerTabSectorIdInput.getText()+"', "+
                "secteur_name = '"+workerTabSectorInput.getText()+"', "+
                "antenne = '"+workerTabCentreInput.getText()+"' "+
                "WHERE worker_id = "+id+"";
        Connection conn = database.connect();
        try {
            Statement statement = conn.createStatement();
            statement.executeUpdate(sql);
            statement.executeUpdate(sql2);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("TRAVAILLEUR MIS A JOUR");
            alert.setHeaderText("Travailleur : ID "+ workerTabIdInput.getText()+ " " +
                    workerTabFirstNameInput.getText() + " "+ workerTabLastNameInput.getText() +
                    "\n Correctement mis à jour.");
            alert.setContentText("");
            alert.show();
            searchByIdWorkerDatabase();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }   // TODO : Finir ASDB. Problème avec certains ID + DELETE

    public void addWorker(){
        int id = Integer.parseInt(workerTabIdInput.getText());
        String sql = "INSERT INTO travailleurs (id, nom, prenom) "+
                "VALUES ("+id+", '"+workerTabLastNameInput.getText()+"', '"+workerTabFirstNameInput.getText()+"')";
        Connection conn = database.connect();
        try {
            Statement statement = conn.createStatement();
            statement.executeUpdate(sql);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteWorker(){
        int id = Integer.parseInt(workerTabIdInput.getText());
        String sql = "DELETE FROM travailleurs "+
                "WHERE id = "+id+" AND nom = +"+workerTabLastNameInput.getText()+" AND prenom = "+workerTabFirstNameInput.getText()+"";
        Connection conn = database.connect();
        try {
            Statement statement = conn.createStatement();
            statement.executeUpdate(sql);

    } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void displayTable() {
        issueDataList.getColumns().clear();
        Connection c;
        data = FXCollections.observableArrayList();
        try {
            c = database.connect();
            String SQL = "SELECT * FROM " + tableNameField.getText();
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

    public void clearTable(){
        tableNameField.clear();
        issueDataList.getColumns().clear();
    }

    public void aboutWindow() {
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
