package SoinsInfirmiers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.VBox;
import main.Menu;

import java.net.URL;
import java.util.ResourceBundle;



public class ControllerSelectVisitesPatients implements Initializable {

    @FXML
    private VBox menuPane;

    private final Menu menu = new Menu();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        menu.loadMenuBar(menuPane);
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
