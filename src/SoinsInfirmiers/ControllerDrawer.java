package SoinsInfirmiers;

import com.Main;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ControllerDrawer implements Initializable {

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void onIndicateursButtonClick(){
        Stage stage;
        stage = Main.getPrimaryStage();
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/com/FXML/StatistiquesSI.fxml"));
            stage.setScene(new Scene(root));
            stage.setTitle(Data.pageTitle0);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    public void onTableauxAnnuelsButtonClick(){
        Stage stage;
        stage = Main.getPrimaryStage();
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/com/FXML/TableauxAnnuels.fxml"));
            stage.setScene(new Scene(root));
            stage.setTitle(Data.pageTitle1);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    public void onSettingsButtonClick(){
        Stage stage;
        stage = Main.getPrimaryStage();
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/com/FXML/SettingsSI.fxml"));
            stage.setScene(new Scene(root));
            stage.setTitle(Data.pageTitle2);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    public void onBackButtonClick(){
        Stage stage;
        stage = Main.getPrimaryStage();
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/com/FXML/HomePage.fxml"));
            stage.setScene(new Scene(root));
            stage.setTitle(Data.homePageTitle);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }
}
