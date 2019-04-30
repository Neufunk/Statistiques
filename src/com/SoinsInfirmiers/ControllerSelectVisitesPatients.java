package SoinsInfirmiers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import main.Menu;

import java.net.URL;
import java.util.ResourceBundle;



public class ControllerSelectVisitesPatients implements Initializable {

    @FXML
    private AnchorPane menuPane;

    private final Menu menu = new Menu();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        menuPane.getChildren().get(0).getStyleClass().add("white");
    }

    public void openVisiteLocalites(){
        menu.openVisitesLocalites();
    }

    public void openVisiteCentres(){
        menu.openVisitesCentres();
    }

    public void back(){
        menu.openSelectSi();
    }
}
