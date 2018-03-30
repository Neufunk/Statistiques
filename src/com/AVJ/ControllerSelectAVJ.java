package AVJ;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import main.Main;
import main.Menu;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ControllerSelectAVJ implements Initializable {

    @FXML
    private VBox menuPane;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Menu menu = new Menu();
        menu.loadMenuBar(menuPane);
    }

    public void openContingentPage(){
        Stage stage = Main.getPrimaryStage();
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/ui/FXML/Contingent.fxml"));
            Scene scene = stage.getScene();
            scene.setRoot(root);
            stage.setTitle(AVJ.Data.pageTitle0);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    public void openASDB(){
        Stage stage = new Stage();
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/ui/FXML/ASDB.fxml"));
            stage.setScene(new Scene(root));
            stage.setTitle(AVJ.Data.asdbTitle);
            stage.show();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    public void openHomepage() {
        Stage stage = Main.getPrimaryStage();
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/ui/FXML/HomePage.fxml"));
            Scene scene = stage.getScene();
            scene.setRoot(root);
            stage.setTitle(AVJ.Data.homePageTitle);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }
}