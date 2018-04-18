package AVJ;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.VBox;
import main.Menu;

import java.net.URL;
import java.util.ResourceBundle;

public class ControllerSelectAVJ implements Initializable {

    @FXML
    private VBox menuPane;

    private final Menu menu = new Menu();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        menu.loadMenuBar(menuPane);
    }

    public void openContingentPage(){
        menu.openContingentPage();
    }

    public void openASDB(){
        menu.openASDB();
    }

    public void openHomepage() {
        menu.openHomepage();
    }
}