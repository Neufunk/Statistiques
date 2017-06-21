package AVJ;

import com.LoadProperties;
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
import java.util.Properties;
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

    Data data = new Data();
    SectorToWorker sectorToWorker = new SectorToWorker();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        onCloseButtonClick();
        initializeCombo();
        displayName();
        onSaveButtonClick();
    }

    private void initializeCombo(){
        comboSecteur.setItems(data.secteursAll);
    }

    private void displayName(){
            comboSecteur.addEventHandler(MouseEvent.ANY, (e) ->{
                if (comboSecteur.getValue() != null) {
                    sectorToWorker.setKey(comboSecteur.getValue().toString());
                    sectorToWorker.secteurToWorker();
                    nameField.setText(sectorToWorker.getTravailleur());
                }
            });
        }

    private void onSaveButtonClick(){
        saveButton.addEventHandler(MouseEvent.MOUSE_RELEASED, (e) ->{
            sectorToWorker.changeWorker(nameField.getText());
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
            stage.hide();

        });;
    }

}
