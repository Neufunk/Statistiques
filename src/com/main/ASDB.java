package main;

import AVJ.Data;
import AVJ.Database;
import com.jfoenix.controls.JFXTextField;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.Callback;

import java.net.URL;
import java.sql.*;
import java.util.Arrays;
import java.util.ResourceBundle;

public class ASDB implements Initializable {

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
    private ComboBox<String> workerTabSectorCombo;
    @FXML
    private ComboBox<String> workerTabWorkerCombo;
    @FXML
    private TextField workerTabCentreInput;
    // SECTOR TAB @resources.ui.FXML
    @FXML
    private ComboBox<String> sectorTabSectorCombo;
    @FXML
    private ComboBox<String> sectorTabWorkerCombo;
    @FXML
    private TextField sectorTabIdInput;
    @FXML
    private TextField sectorTabFirstNameInput;
    @FXML
    private TextField sectorTabLastNameInput;
    @FXML
    private TextField sectorTabSectorIdInput;
    @FXML
    private TextField sectorTabSectorInput;
    @FXML
    private TextField sectorTabCentreInput;
    // ISSUE TAB
    @FXML
    private ComboBox<String> issueTabComboQuery;
    @FXML
    private TextArea issueTabTextAreaQuery;

    private Database database = new Database();
    private AVJ.Data dataList = new Data();

    public void initialize(URL location, ResourceBundle resources) {
        initializeCombo();
    }

    private void initializeCombo(){
        database.connect();
        ObservableList sectorsList = database.loadColumnToCombo("secteurs", "secteur_name", "secteur_name");
        workerTabSectorCombo.setItems(sectorsList);
        sectorTabSectorCombo.setItems(sectorsList);

        ObservableList workersList = database.loadColumnToCombo("travailleurs", "nom", "nom");
        workerTabWorkerCombo.setItems(workersList);
        sectorTabWorkerCombo.setItems(workersList);

        issueTabComboQuery.setItems(dataList.queryList);

        database.closeConnection();
    }

    /*
     Worker Tab
     */
    public void searchBySecteursCombo(){
        Connection conn = database.connect();
        workerTabWorkerCombo.getSelectionModel().clearSelection();
        String sql = "SELECT *, travailleurs.id AS WorkerID " +
                "FROM secteurs " +
                "LEFT JOIN travailleurs " +
                "ON secteurs.worker_id = travailleurs.id " +
                "WHERE secteur_name ='"+workerTabSectorCombo.getValue()+"' " +
                "ORDER BY secteur_name ASC ";
        try {
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery(sql);
            while (rs.next()) {
                workerTabIdInput.setText(rs.getString("WorkerID"));
                workerTabFirstNameInput.setText(rs.getString("prenom"));
                workerTabLastNameInput.setText(rs.getString("nom"));
                workerTabSectorIdInput.setText(rs.getString("id"));
                workerTabCentreInput.setText(rs.getString("antenne"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            displayError(e);
        }
        database.closeConnection();
    }

    public void searchByWorkerCombo(){
        Connection conn = database.connect();
        workerTabSectorCombo.getSelectionModel().clearSelection();
        String sql = "SELECT *, secteurs.id AS sectorID " +
                "FROM travailleurs " +
                "LEFT JOIN secteurs " +
                "ON travailleurs.id = secteurs.worker_id " +
                "WHERE nom ='"+ workerTabWorkerCombo.getValue()+"'";
        try {
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery(sql);
            while (rs.next()) {
                workerTabIdInput.setText(rs.getString("id"));
                workerTabFirstNameInput.setText(rs.getString("prenom"));
                workerTabLastNameInput.setText(rs.getString("nom"));
                workerTabSectorIdInput.setText(rs.getString("sectorID"));
                workerTabCentreInput.setText(rs.getString("antenne"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            displayError(e);
        }
        database.closeConnection();
    }

    public void searchByIdWorkerDatabase() {
        int id = Integer.parseInt(workerTabIdInput.getText());
        String sql = "SELECT *, secteurs.id AS sectorID " +
                "FROM travailleurs " +
                "LEFT JOIN secteurs " +
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
                workerTabCentreInput.setText(rs.getString("antenne"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            displayError(e);
        }

    }

    public void searchByFirstNameWorkerDatabase() {
        String sql = "SELECT *, secteurs.id AS sectorID " +
                "FROM travailleurs " +
                "LEFT JOIN secteurs " +
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
                workerTabSectorCombo.setPromptText(rs.getString("secteur_name"));
                workerTabCentreInput.setText(rs.getString("antenne"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            displayError(e);
        }

    }

    public void searchByLastNameWorkerDatabase() {
        String sql = "SELECT *, secteurs.id AS sectorID " +
                "FROM travailleurs " +
                "LEFT JOIN secteurs " +
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
                workerTabSectorCombo.setPromptText(rs.getString("secteur_name"));
                workerTabCentreInput.setText(rs.getString("antenne"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            displayError(e);
        }
    }

    public void clearFieldsWorkerTab(){
        workerTabIdInput.clear();
        workerTabFirstNameInput.clear();
        workerTabLastNameInput.clear();
        workerTabSectorIdInput.clear();
        workerTabCentreInput.clear();
        workerTabSectorCombo.getSelectionModel().clearSelection();
    }

    public void updateWorkerDatabase(){
        int id = Integer.parseInt(workerTabIdInput.getText());
        String sql = "UPDATE travailleurs "+
                "SET prenom = '"+workerTabFirstNameInput.getText()+"', "+
                "nom = '"+workerTabLastNameInput.getText()+"' "+
                "WHERE id = "+id+"";
        Connection conn = database.connect();
        try {
            Statement statement = conn.createStatement();
            statement.executeUpdate(sql);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("TRAVAILLEUR MIS A JOUR");
            alert.setHeaderText("Travailleur : ID #"+ workerTabIdInput.getText()+ " - " +
                    workerTabFirstNameInput.getText() + " "+ workerTabLastNameInput.getText() +
                    "\n Correctement mis à jour.");
            alert.setContentText("");
            alert.show();
            searchByIdWorkerDatabase();

        } catch (SQLException e) {
            e.printStackTrace();
            displayError(e);
        }
    }

    public void addWorker(){
        String sql = "INSERT INTO travailleurs (nom, prenom) "+
                "VALUES ('"+workerTabLastNameInput.getText()+"', '"+workerTabFirstNameInput.getText()+"')";
        Connection conn = database.connect();
        try {
            Statement statement = conn.createStatement();
            statement.executeUpdate(sql);

            searchByLastNameWorkerDatabase();
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("TRAVAILLEUR AJOUTÉ");
            alert.setHeaderText("Travailleur : ID #"+ workerTabIdInput.getText()+ " - " +
                    workerTabFirstNameInput.getText() + " "+ workerTabLastNameInput.getText() +
                    "\n Correctement ajouté à la base de donnée.");
            alert.setContentText("");
            alert.show();

        } catch (SQLException e) {
            e.printStackTrace();
            displayError(e);
        }
        initializeCombo();
    }

    public void deleteWorker(){
        int id = Integer.parseInt(workerTabIdInput.getText());
        String sql = "DELETE FROM travailleurs "+
                "WHERE id = "+id+" AND nom = '"+workerTabLastNameInput.getText()+"' AND prenom = '"+workerTabFirstNameInput.getText()+"'";
        Connection conn = database.connect();
        try {
            Statement statement = conn.createStatement();
            statement.executeUpdate(sql);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("TRAVAILLEUR EFFACÉ");
            alert.setHeaderText("Travailleur : ID #"+ workerTabIdInput.getText()+ " - " +
                    workerTabFirstNameInput.getText() + " "+ workerTabLastNameInput.getText() +
                    "\n Correctement effacé de la base de donnée.");
            alert.setContentText("");
            alert.show();
            searchByIdWorkerDatabase();

    } catch (SQLException e) {
            e.printStackTrace();
        }
        initializeCombo();
        clearFieldsWorkerTab();
    }

    /*
    * Sector TAB
    */
    public void searchBySecteursCombo2(){
        workerTabWorkerCombo.getSelectionModel().clearSelection();
        String sql = "SELECT *, secteurs.id AS sectorID " +
                "FROM secteurs " +
                "LEFT JOIN travailleurs " +
                "ON secteurs.worker_id = travailleurs.id " +
                "WHERE secteur_name ='"+sectorTabSectorCombo.getValue()+"' " +
                "ORDER BY secteur_name ASC ";
        Connection conn = database.connect();
        try {
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery(sql);
            while (rs.next()) {
                sectorTabIdInput.setText(rs.getString("id"));
                sectorTabFirstNameInput.setText(rs.getString("prenom"));
                sectorTabLastNameInput.setText(rs.getString("nom"));
                sectorTabSectorIdInput.setText(rs.getString("sectorID"));
                sectorTabSectorInput.setText(rs.getString("secteur_name"));
                sectorTabCentreInput.setText(rs.getString("antenne"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            displayError(e);
        }
        database.closeConnection();
    }

    public void searchByWorkerCombo2(){
        workerTabSectorCombo.getSelectionModel().clearSelection();
        String sql = "SELECT *, secteurs.id AS sectorID " +
                "FROM travailleurs " +
                "LEFT JOIN secteurs " +
                "ON travailleurs.id = secteurs.worker_id " +
                "WHERE nom ='"+ sectorTabWorkerCombo.getValue()+"'";
        Connection conn = database.connect();
        try {
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery(sql);
            while (rs.next()) {
                sectorTabIdInput.setText(rs.getString("id"));
                sectorTabFirstNameInput.setText(rs.getString("prenom"));
                sectorTabLastNameInput.setText(rs.getString("nom"));
                sectorTabSectorIdInput.setText(rs.getString("sectorID"));
                sectorTabSectorInput.setText(rs.getString("secteur_name"));
                sectorTabCentreInput.setText(rs.getString("antenne"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            displayError(e);
        }
        database.closeConnection();
    }

    public void addSector() {
        String sql = "INSERT INTO secteurs (id, secteur_name, antenne) " +
                "VALUES ('" + sectorTabSectorIdInput.getText() + "', '" + sectorTabSectorInput.getText() + "', '" + sectorTabCentreInput.getText() + "')";
        Connection conn = database.connect();
        try {
            Statement statement = conn.createStatement();
            statement.executeUpdate(sql);

            searchBySecteursCombo2();
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("SECTEUR AJOUTÉ");
            alert.setHeaderText("Secteur : ID #" + sectorTabSectorIdInput.getText() + " - " +
                    sectorTabSectorInput.getText() + " " + sectorTabCentreInput.getText() +
                    "\n Correctement ajouté à la base de donnée.");
            alert.setContentText("");
            alert.show();
        } catch (SQLException e) {
            e.printStackTrace();
            displayError(e);
        }
    }

    public void updateSector(){
        String sql = "UPDATE secteurs " +
                "SET secteur_name = '" + sectorTabSectorInput.getText() + "', antenne = '" + sectorTabCentreInput.getText() + "' " +
                "WHERE id = '" + sectorTabSectorIdInput.getText() + "'";
        Connection conn = database.connect();
        try {
            Statement st = conn.createStatement();
            st.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
            displayError(e);
        }
        initializeCombo();
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Secteur mis à jour");
        alert.setContentText("ID : "+ sectorTabSectorIdInput.getText() + sectorTabSectorInput.getText() + " - " +
                sectorTabCentreInput + " a correctement été mis à jour dans la base de donnée.");
        alert.showAndWait();
    }

    public void deleteSector(){
        int id = Integer.parseInt(sectorTabSectorIdInput.getText());
        String sql = "DELETE FROM secteurs "+
                "WHERE id = "+id+" AND secteur_name = '"+sectorTabSectorInput.getText()+"' AND antenne = '"+sectorTabCentreInput.getText()+"'";
        Connection conn = database.connect();
        try {
            Statement statement = conn.createStatement();
            statement.executeUpdate(sql);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("SECTEUR EFFACÉ");
            alert.setHeaderText("Secteur : ID #"+ sectorTabSectorIdInput.getText()+ " - " +
                    sectorTabSectorInput.getText() + " - "+ sectorTabCentreInput.getText() +
                    "\n Correctement effacé de la base de donnée.");
            alert.setContentText("");
            alert.show();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        initializeCombo();
        clearFieldsWorkerTab();
    }

    /*
    * Tables Tab
    */
    public void displayTable() {
        issueDataList.getColumns().clear();
        Connection conn;
        ObservableList<ObservableList> data = FXCollections.observableArrayList();
        try {
            conn = database.connect();
            String SQL = "SELECT * FROM " + tableNameField.getText();
            ResultSet rs = conn.createStatement().executeQuery(SQL);

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
            displayError(e);
            System.out.println("Erreur lors de la construction des données");
        }
    }

    public void clearTable(){
        tableNameField.clear();
        issueDataList.getColumns().clear();
    }

    /* Menu Bar */
    public void aboutWindow() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("ASDB Engine - 1.0");
        alert.setHeaderText("Aide & Soins à Domicile Database Engine");
        alert.setContentText("2017 - Aide & Soins à Domicile en province de Namur\n" +
                " Coded and designed by Johnathan Vanbeneden");
        alert.show();
    }

    /* Switch Tab */
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

    /* ERRORS */
    private void displayError(Exception e) {
        e.printStackTrace();
        String e1 = e.toString();
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur");
        alert.setHeaderText(e1);
        alert.setContentText("STACKTRACE : \t\t" + Arrays.toString(e.getStackTrace()) + "\n" +
                "CAUSE : \t\t\t" + e.getLocalizedMessage() + "\n" + "\t\t" + this.getClass().toString());
        alert.showAndWait();
    }


}
