package SoinsInfirmiers;

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

import static main.Menu.pdfStage;

public class ControllerSelectSi implements Initializable {

    @FXML
    private VBox menuPane;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Menu menu = new Menu();
        menu.loadMenuBar(menuPane);
    }

    public void openIndicateursPage(){
        Stage stage = Main.getPrimaryStage();
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/ui/FXML/StatistiquesSI.fxml"));
            Scene scene = stage.getScene();
            scene.setRoot(root);
            stage.setTitle(Data.pageTitle0);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    public void openComparaisonPage(){
        Stage stage = Main.getPrimaryStage();
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/ui/FXML/TableauxAnnuels.fxml"));
            Scene scene = stage.getScene();
            scene.setRoot(root);
            stage.setTitle(Data.pageTitle1);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    public void pdfActivite(){
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/ui/FXML/PopUpActivite.fxml"));
            pdfStage.setScene(new Scene(root, 370, 205));
            pdfStage.setTitle("Rapport d'activité et suivi du personnel");
            pdfStage.setResizable(false);
            pdfStage.show();
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
