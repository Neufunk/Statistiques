package AVJ;

import RingProgressIndicator.RingProgressIndicator;
import SoinsInfirmiers.Data;
import com.Effects;
import com.Main;
import com.PatchNote;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXToggleButton;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.concurrent.Worker;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;
import java.util.ResourceBundle;

public class ControllerContingent implements Initializable {

    @FXML
    private JFXComboBox comboCentre;
    @FXML
    private JFXComboBox comboSecteur;
    @FXML
    private JFXComboBox comboPeriode;
    @FXML
    private JFXButton backButton;
    @FXML
    AnchorPane maskPane;
    @FXML
    private TableView<ObservableList> tableView;
    @FXML
    private TableView<ObservableList> tableView2;
    @FXML
    private JFXToggleButton toggleButton;
    @FXML
    private JFXToggleButton toggleButton1;
    @FXML
    private Label yearLabel1;
    @FXML
    private Label yearLabel2;
    @FXML
    private JFXCheckBox antenneCheckbox;

    AVJ.Data data = new AVJ.Data();
    Database database = new Database();
    public static Stage progressStage = new Stage();
    private ObservableList<ObservableList> observableList;
    private ObservableList<ObservableList> observableList2;
    RingProgressIndicator ringProgressIndicator = new RingProgressIndicator();

    int progress = 0;
    boolean endJob = false;

    public void initialize(URL location, ResourceBundle resources) {
        initializeCombo();
        onBackButtonClick();
        toggleButtonListener();
        toggleButtonDirectriceListener();
    }

    private void initializeCombo() {
        comboCentre.setItems(data.centerList);
        comboPeriode.setItems(data.periode);
    }

    public void toggleButtonListener() {
        toggleButton.setText(getCurrentYear());
    }

    public void toggleButtonDirectriceListener() {
        if (toggleButton1.isSelected()) {
            toggleButton1.setText("Laurence");
        } else {
            toggleButton1.setText("Sarah");
        }
    }

    public void displaySecteurs() {
        if (comboCentre.getValue() == "ASD") {
            comboSecteur.setPromptText("Province entière");
            comboSecteur.setDisable(true);
            antenneCheckbox.setDisable(true);
            antenneCheckbox.setSelected(false);
        } else if (antenneCheckbox.isSelected()) {
            comboSecteur.setDisable(true);
            comboSecteur.getSelectionModel().clearSelection();
            comboSecteur.setPromptText("Secteur entier");
        } else {
            comboSecteur.setPromptText("Secteurs");
            comboSecteur.setDisable(false);
            antenneCheckbox.setDisable(false);
        }
        database.connect();
        if (comboCentre.getValue() != null) {
            comboSecteur.setItems(database.loadSectorsToCombo(comboCentre.getValue().toString()));
        }
        database.closeConnection();
    }

    public boolean getCheckboxState() {
        if (antenneCheckbox.isSelected()) {
            return true;
        } else {
            return false;
        }
    }

    public void onUpdateButtonClick() {
        IteratorExcel iteratorExcel = new IteratorExcel();
        buildProgressWindow();
        endJob = false;
        new WorkerThread(ringProgressIndicator).start();

        final Service<Void> calculateService = new Service<Void>() {
            @Override
            protected Task<Void> createTask() {
                return new Task<Void>() {
                    @Override
                    protected Void call() throws Exception {
                        String sql = "SELECT * FROM travailleurs " +
                                "INNER JOIN secteurs " +
                                "ON travailleurs.id = secteurs.worker_id ";
                        Connection conn = database.connect();
                        try {
                            Statement statement = conn.createStatement();
                            ResultSet rs = statement.executeQuery(sql);
                            while (rs.next()) {
                                progress += 5;
                                String name = rs.getString("prenom");
                                String sect = rs.getString("secteur_name");
                                String centre = rs.getString("antenne");
                                String namPath = "";
                                String philPath = "";
                                if (toggleButton1.isSelected()) {
                                    namPath = "P:\\SERVICE SOCIAL - SERVICE DU PERSONNEL\\Tableaux mensuels\\";
                                    philPath = "W:\\SERVICE FAMILIAL\\SERVICE SOCIAL - SERVICE DU PERSONNEL\\Tableaux mensuels\\";
                                } else {
                                    namPath = "W:\\SERVICE SOCIAL - SERVICE DU PERSONNEL\\Tableaux mensuels\\";
                                    philPath = "P:\\SERVICE FAMILIAL\\SERVICE SOCIAL - SERVICE DU PERSONNEL\\Tableaux mensuels\\";
                                }
                                switch (centre) {
                                    case "Namur":
                                        iteratorExcel.startIteration(namPath, getCurrentYear(), name, sect);
                                        break;
                                    case "Philippeville":
                                        iteratorExcel.startIteration(philPath, getCurrentYear(), name, sect);
                                        break;
                                }
                            }
                        } catch (SQLException e1) {
                            e1.printStackTrace();
                            displayError(e1);
                        }
                        progressStage.close();
                        endJob = true;
                        return null;
                    }
                };
            }
        };
        calculateService.stateProperty().addListener((ObservableValue<? extends Worker.State> observableValue, Worker.State oldValue, Worker.State newValue) -> {
            switch (newValue) {
                case FAILED:
                case CANCELLED:
                case SUCCEEDED:
                    break;
            }
        });
        calculateService.start();
    }


    private void onBackButtonClick() {
        backButton.addEventHandler(MouseEvent.MOUSE_RELEASED, (e) -> {
            Stage stage = Main.getPrimaryStage();
            try {
                Parent root = FXMLLoader.load(getClass().getResource("/com/FXML/HomePage.fxml"));
                stage.setScene(new Scene(root));
                stage.setTitle(AVJ.Data.homePageTitle);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        });
    }

    private String getCurrentYear() {
        Calendar now = Calendar.getInstance();
        int year = now.get(Calendar.YEAR);
        if (toggleButton.isSelected()) {
            return String.valueOf(year);
        } else {
            return String.valueOf(year - 1);
        }
    }

    private void displayYearLabel() {
        Calendar now = Calendar.getInstance();
        int year = now.get(Calendar.YEAR);
        yearLabel1.setVisible(true);
        yearLabel2.setVisible(true);
        yearLabel1.setText(String.valueOf(year - 1));
        yearLabel2.setText(String.valueOf(year - 2));
        if (toggleButton.isSelected()) {
            yearLabel1.setText(String.valueOf(year));
            yearLabel2.setText(String.valueOf(year - 1));
        }
    }

    public void onGenerateButtonClick() {
        if (!checkEmpty()) {
            return;
        } else {
            displayTable();
        }
    }

    private void displayTable() {
        tableView.getColumns().clear();
        tableView2.getColumns().clear();
        displayYearLabel();
        observableList = FXCollections.observableArrayList();
        observableList2 = FXCollections.observableArrayList();
        observableList.clear();
        observableList2.clear();
        try {
            Connection c = database.connect();
            String centre = comboCentre.getValue().toString();
            String secteur = "";
            if (comboSecteur.getValue() != null) {
                secteur = comboSecteur.getValue().toString();
            }
            String periode = comboPeriode.getValue().toString();
            String year = getCurrentYear();
            // CURRENT YEAR TABLE
            String sql = database.loadContingent38(centre, secteur, periode, year, getCheckboxState());
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
            // LAST YEAR TABLE
            Calendar now = Calendar.getInstance();
            int currentYear = now.get(Calendar.YEAR);
            String lastYear = String.valueOf(currentYear - 1);
            if (toggleButton.isSelected()) {
                lastYear = String.valueOf(currentYear - 2);
            }
            String sql2 = database.loadContingent38(centre, secteur, periode, lastYear, getCheckboxState());
            ResultSet rs2 = c.createStatement().executeQuery(sql2);

            for (int i = 0; i < rs2.getMetaData().getColumnCount(); i++) {
                final int j = i;
                TableColumn col = new TableColumn(rs2.getMetaData().getColumnName(i + 1));
                col.setCellFactory(TextFieldTableCell.forTableColumn());
                col.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ObservableList, String>, ObservableValue<String>>() {
                    public ObservableValue<String> call(TableColumn.CellDataFeatures<ObservableList, String> param) {
                        return new SimpleStringProperty(param.getValue().get(j).toString());
                    }
                });
                tableView2.getColumns().addAll(col);
                System.out.println("Column [" + i + "] ");
            }
            while (rs2.next()) {
                //Iterate Row
                ObservableList<String> row = FXCollections.observableArrayList();
                for (int i = 1; i <= rs2.getMetaData().getColumnCount(); i++) {
                    //Iterate Column
                    row.add(rs2.getString(i));
                }
                System.out.println("Row [1] ajoutée " + row);
                observableList2.add(row);
            }
            tableView.setItems(observableList);
            tableView2.setItems(observableList2);
            database.closeConnection();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Erreur lors de la construction des données");
            displayError(e);
        }
    }

    private boolean checkEmpty() {
        if (comboCentre.getValue() == null) {
            showEmptyDialog("un centre");
            return false;
        } else if (!antenneCheckbox.isSelected() && comboSecteur.getValue() == null && comboCentre.getValue() != "ASD") {
            showEmptyDialog("un secteur");
            return false;
        } else if (comboPeriode.getValue() == null) {
            showEmptyDialog("une période");
            return false;
        } else {
            return true;
        }
    }

    private void showEmptyDialog(String string) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("NullPointerException");
        alert.setHeaderText("Veuillez sélectionner " + string);
        alert.setContentText("avoid NullPointerException");
        alert.showAndWait();
    }

    private void displayError(Exception e) {
        e.printStackTrace();
        String e1 = e.toString();
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur");
        alert.setHeaderText(e1);
        alert.setContentText("STACKTRACE : \t\t" + e.getStackTrace() + "\n" +
                "CAUSE : \t\t\t" + e.getLocalizedMessage() + "\n" + "\t\t" + this.getClass().toString());
        alert.showAndWait();
    }

    // Progress bar
    private void buildProgressWindow() {
        ringProgressIndicator.setRingWidth(400);
        ringProgressIndicator.makeIndeterminate();
        StackPane root = new StackPane();
        root.getChildren().add(ringProgressIndicator);
        Scene scene = new Scene(root, 300, 250, Color.TRANSPARENT);
        scene.setFill(Color.TRANSPARENT);
        progressStage.setScene(scene);
        progressStage.initOwner(Main.getPrimaryStage());
        progressStage.initModality(Modality.APPLICATION_MODAL);
        progressStage.initStyle(StageStyle.TRANSPARENT);
        progressStage.initStyle(StageStyle.UNDECORATED);
        progressStage.show();
    }

    class WorkerThread extends Thread {
        RingProgressIndicator rpi;

        // int progress = 0;
        public WorkerThread(RingProgressIndicator rpi) {
            this.rpi = rpi;
        }

        @Override
        public void run() {
            while (true) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Platform.runLater(() -> {
                    rpi.setProgress(progress);
                });
                // progress += 1;
                if (progress > 100) {
                    Platform.runLater(() -> {
                        while (endJob = false) {
                            ringProgressIndicator.makeIndeterminate();
                        }
                        progressStage.close();
                    });
                    break;
                }
            }
        }

    }

    //Menu bar
    public void showPatchnote() {
        PatchNote pn = new PatchNote();
        pn.patchNote();
    }

    public void changeLogs() {
        File file = new File("/resources/txt/Changelog.txt");
        if (!Desktop.isDesktopSupported()) {
            System.out.println("OS non supporté");
            return;
        }
        Desktop desktop = Desktop.getDesktop();
        try {
            if (file.exists()) desktop.open(file);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    public void openIndicateursPage() {
        Stage stage = Main.getPrimaryStage();
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/com/FXML/StatistiquesSI.fxml"));
            stage.setScene(new Scene(root));
            stage.setTitle(Data.pageTitle0);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    public void openComparaisonPage() {
        Stage stage = Main.getPrimaryStage();
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/com/FXML/TableauxAnnuels.fxml"));
            stage.setScene(new Scene(root));
            stage.setTitle(Data.pageTitle1);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    public void openSettingsPage() {
        Stage stage = Main.getPrimaryStage();
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/com/FXML/SettingsSI.fxml"));
            stage.setScene(new Scene(root));
            stage.setTitle(Data.pageTitle2);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    public void openContingentPage() {
        Stage stage = Main.getPrimaryStage();
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/com/FXML/Contingent.fxml"));
            stage.setScene(new Scene(root));
            stage.setTitle(AVJ.Data.pageTitle0);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    public void openASDB() {
        Stage stage = new Stage();
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/com/FXML/ASDB.fxml"));
            stage.setScene(new Scene(root));
            stage.setTitle(AVJ.Data.asdbTitle);
            stage.show();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }
}


