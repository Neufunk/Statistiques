package AVJ;

import SoinsInfirmiers.Strings;
import com.Effects;
import com.LoadProperties;
import com.Main;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.effect.BoxBlur;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Effect;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.Properties;
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

    Properties prop;
    Centre centre = new Centre();
    Data data = new Data();
    static public Stage workerStage = new Stage();
    Effects effects = new Effects();
    LoadProperties loadProperties = new LoadProperties();

    public void initialize(URL location, ResourceBundle resources) {
        loadProperties();
        initializeCombo();
        buildWorkerWindow();
        onWorkerButtonClick();
        onBackButtonClick();
    }

    private void initializeCombo() {
        comboCentre.setItems(data.centerList);
        pane0.addEventHandler(MouseEvent.ANY, (e) -> {
            if (comboCentre.getValue() != null) {
                centre.setCentre(comboCentre.getValue().toString());
                comboSecteur.setItems(centre.getSecteur());
            }
        });
    }

    private void loadProperties() {
        try {
            prop = loadProperties.load("C:\\Users\\johnathanv\\IdeaProjects\\Statistiques_FX\\src\\resources\\properties\\Contingent.properties");
        } catch (IOException | NullPointerException e) {
            e.printStackTrace();
        }
    }

    private void onWorkerButtonClick() {
        travailleurButton.addEventHandler(MouseEvent.MOUSE_RELEASED, (e) -> {
            effects.setFadeTransition(maskPane, 600, 0, 0.15);
            effects.setBoxBlur(mainPane);
            maskPane.setVisible(true);
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
            workerStage.initStyle(StageStyle.UNDECORATED);
            workerStage.initOwner(Main.getPrimaryStage());
            workerStage.initModality(Modality.APPLICATION_MODAL);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
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

}


