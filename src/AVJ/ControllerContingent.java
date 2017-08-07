package AVJ;

import com.Effects;
import com.Main;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.Calendar;
import java.util.ResourceBundle;

public class ControllerContingent implements Initializable {

    @FXML
    private AnchorPane mainPane;
    @FXML
    private AnchorPane pane0;
    @FXML
    private JFXComboBox comboCentre;
    @FXML
    private JFXComboBox comboSecteur;
    @FXML
    private JFXComboBox comboPeriode;
    @FXML
    private JFXButton generateButton;
    @FXML
    private AnchorPane pane1;
    @FXML
    private JFXButton updateButton;
    @FXML
    private JFXButton backButton;
    @FXML
    private JFXButton travailleurButton;
    @FXML
    AnchorPane maskPane;
    @FXML
    private ListView<String> listView1;
    @FXML
    private ListView<?> listView2;
    @FXML
    private TableView<ObservableList> tableView;

    Data data = new Data();
    static public Stage workerStage = new Stage();
    Effects effects = new Effects();
    Database database = new Database();
    private ObservableList<ObservableList> observableList;

    public void initialize(URL location, ResourceBundle resources) {
        initializeCombo();
        onWorkerButtonClick();
        onBackButtonClick();
        onUpdateButtonClick();
    }

    private void initializeCombo() {
        comboCentre.setItems(data.centerList);
        comboPeriode.setItems(data.periode);
    }

    public void displaySecteurs(){
        if (comboCentre.getValue() == "ASD"){
            comboSecteur.setPromptText("Province entière");
        }else{
            comboSecteur.setPromptText("Secteurs");
        }
        database.connect();
        if (comboCentre.getValue() != null) {
            comboSecteur.setItems(database.loadSectorsToCombo(comboCentre.getValue().toString()));
        }
        database.closeConnection();
    }

    private void onWorkerButtonClick() {
        travailleurButton.addEventHandler(MouseEvent.MOUSE_RELEASED, (e) -> {
            effects.setFadeTransition(maskPane, 600, 0, 0.15);
            effects.setBoxBlur(mainPane);
            maskPane.setVisible(true);
            buildWorkerWindow();
            workerStage.showAndWait();
            mainPane.setEffect(null);
            maskPane.setVisible(false);
        });
    }

    private void buildWorkerWindow() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("../com/FXML/WorkersAVJ.fxml"));
            workerStage.setScene(new Scene(root));
            workerStage.setTitle(Data.pageTitle1);
            workerStage.initStyle(StageStyle.UNDECORATED); //TODO : Résoudre bug avec la décoration de la fenêtre une fois fermée puis réouverte
            workerStage.initOwner(Main.getPrimaryStage());
            workerStage.initModality(Modality.APPLICATION_MODAL);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    private void onUpdateButtonClick(){
        updateButton.addEventHandler(MouseEvent.MOUSE_RELEASED, (e) -> {
            IteratorExcel iteratorExcel = new IteratorExcel();
            iteratorExcel.startIteration();
        });
    }

    private void onBackButtonClick() {
        backButton.addEventHandler(MouseEvent.MOUSE_RELEASED, (e) -> {
            Stage stage = Main.getPrimaryStage();
            try {
                Parent root = FXMLLoader.load(getClass().getResource("../com/FXML/HomePage.fxml"));
                stage.setScene(new Scene(root));
                stage.setTitle(Data.homePageTitle);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        });
    }

    private String getCurrentYear(){
        Calendar now = Calendar.getInstance();
        int year = now.get(Calendar.YEAR);
        return String.valueOf(year);
    }

    public void displayTable() {
        tableView.getColumns().clear();
        observableList = FXCollections.observableArrayList();
        observableList.clear();
        try {
            Connection c = database.connect();
            String centre = comboCentre.getValue().toString();
            String secteur = "";
            if (comboSecteur.getValue()!= null) {
                secteur = comboSecteur.getValue().toString();
            }
            String periode = comboPeriode.getValue().toString();
            String year= getCurrentYear();
            String sql = database.loadContingent(centre, secteur, periode, year);
            ResultSet rs = c.createStatement().executeQuery(sql);

            for (int i = 0; i < rs.getMetaData().getColumnCount(); i++) {
                final int j = i;
                TableColumn col = new TableColumn(rs.getMetaData().getColumnName(i + 1));
                col.setCellFactory(TextFieldTableCell.forTableColumn());
                col.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ObservableList, String>, ObservableValue<String>>() {
                    public ObservableValue<String> call(TableColumn.CellDataFeatures<ObservableList, String> param) {
                        return new SimpleStringProperty(param.getValue().get(j).toString());
                    }
                });
                tableView.getColumns().addAll(col);
                System.out.println("Column [" + i + "] ");
            }
            while (rs.next()) {
                //Iterate Row
                ObservableList<String> row = FXCollections.observableArrayList();
                for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                    //Iterate Column
                    row.add(rs.getString(i));
                }
                System.out.println("Row [1] ajoutée " + row);
                observableList.add(row);
            }

            tableView.setItems(observableList);
            database.closeConnection();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Erreur lors de la construction des données");

        }
    }

}


