package AVJ;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.effect.DropShadow;
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

    Data data = new Data();
    SectorToWorker sectorToWorker = new SectorToWorker();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        onCloseButtonClick();
        initializeCombo();
        displayName();
    }

    private void initializeCombo(){
        comboSecteur.setItems(data.secteursAll);
    }

    private void displayName(){
            mainPane.addEventHandler(MouseEvent.ANY, (e) ->{
                if (comboSecteur.getValue() != null) {
                    sectorToWorker.secteurToWorker(comboSecteur.getValue().toString());
                    nameField.setText(sectorToWorker.getSecteur());
                }
            });
        }

    private void onSaveButtonClick(){

    }

    private void onCloseButtonClick(){
        closeButton.addEventHandler(MouseEvent.MOUSE_RELEASED, (e) -> {
            Stage stage = ControllerContingent.workerStage;
            stage.hide();

        });;
    }

}
