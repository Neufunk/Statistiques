package main;

import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class HomePage implements Initializable {

    public JFXButton closeButton;
    @FXML
    private JFXButton siButton;
    @FXML
    private JFXButton avjButton;
    @FXML
    private AnchorPane menuPane;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        menuPane.getChildren().get(0).getStyleClass().add("white");
        onButtonSiClick();
        onButtonAvjClick();
    }

    private void onButtonSiClick() {
        Tooltip.install(siButton, new Tooltip("Département Soins Infirmiers"));
        siButton.addEventHandler(MouseEvent.MOUSE_RELEASED, (e) -> openIndicateursPage());
    }

    private void onButtonAvjClick(){
        Tooltip.install(avjButton, new Tooltip("Département Aide à la Vie Journalière"));
        avjButton.addEventHandler(MouseEvent.MOUSE_RELEASED, (e) -> openContingentPage());
    }

    private void openIndicateursPage(){
            Stage stage = Main.getPrimaryStage();
            try {
                Parent root = FXMLLoader.load(getClass().getResource("/ui/FXML/SelectSi.fxml"));
                Scene scene = stage.getScene();
                scene.setRoot(root);
                stage.setTitle(Menu.TITLE_3);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
    }

    private void openContingentPage(){
        Stage stage = Main.getPrimaryStage();
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/ui/FXML/SelectAVJ.fxml"));
            Scene scene = stage.getScene();
            scene.setRoot(root);
            stage.setTitle(avj.Data.pageTitle1);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }
}

