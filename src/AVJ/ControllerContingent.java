package AVJ;

import com.Effects;
import com.Main;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;
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
    private TableView<?> tableView;

    Centre centre = new Centre();
    Data data = new Data();
    static public Stage workerStage = new Stage();
    Effects effects = new Effects();
    Database database = new Database();

    public void initialize(URL location, ResourceBundle resources) {
        initializeCombo();
        onWorkerButtonClick();
        onBackButtonClick();
        onUpdateButtonClick();
        initializeListView();
    }

    private void initializeCombo() {
        comboCentre.setItems(data.centerList);
    }

    private void initializeListView() {
        TableColumn firstNameCol = new TableColumn("First Name");
        TableColumn lastNameCol = new TableColumn("Last Name");
        TableColumn emailCol = new TableColumn("Email");
        tableView.getColumns().addAll(firstNameCol, lastNameCol, emailCol);
    }

    public void displaySecteur(){
        if (comboCentre.getValue() != null) {
            centre.setCentre(comboCentre.getValue().toString());
            comboSecteur.setItems(centre.getSecteur());
        }
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

    public String getSecteur(){
        String secteur = database.loadPathSecteur(comboSecteur.getValue().toString());
        return secteur;
    }

    public String getName(){
        String name = database.loadPathName(comboSecteur.getValue().toString());
        return name;
    }


}


