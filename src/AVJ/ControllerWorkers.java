package AVJ;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import java.net.URL;
import java.util.ResourceBundle;

public class ControllerWorkers implements Initializable {

    @FXML
    private AnchorPane mainPane;
    @FXML
    private JFXComboBox comboSecteur;
    @FXML
    private JFXTextField nameField;
    @FXML
    private JFXButton closeButton;
    @FXML
    private JFXButton saveButton;

    Database database = new Database();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        onCloseButtonClick();
        initializeCombo();
        onSaveButtonClick();
    }

    private void initializeCombo(){
        database.connect();
        comboSecteur.setItems(database.loadColumnToCombo("secteurs", "secteur_name"));
    }

    public void displayName(){
                if (comboSecteur.getValue() != null) {
                    String name = database.loadWorkerName(comboSecteur.getValue().toString());
                    System.out.println(name);
                    nameField.setText(name);
                }
        }

    private void onSaveButtonClick(){
        saveButton.addEventHandler(MouseEvent.MOUSE_RELEASED, (e) ->{
            //TODO
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("NOUVEAU TRAVAILLEUR ENREGISTRE");
            alert.setHeaderText("Nouveau Travailleur : "+ nameField.getText() + "\n Enregistré pour le secteur :" + comboSecteur.getValue().toString());
            alert.setContentText("");
            alert.show();
        });

    }

    private void onCloseButtonClick(){
        closeButton.addEventHandler(MouseEvent.MOUSE_RELEASED, (e) -> {
            Stage stage = ControllerContingent.workerStage;
            stage.close();
            database.closeConnection();

        });
    }

}
