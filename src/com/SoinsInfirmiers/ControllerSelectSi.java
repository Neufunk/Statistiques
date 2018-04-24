package SoinsInfirmiers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.VBox;
import main.Menu;

import java.net.URL;
import java.util.ResourceBundle;

public class ControllerSelectSi implements Initializable {

    @FXML
    private VBox menuPane;

    private final Menu menu = new Menu();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        menu.loadMenuBar(menuPane);
    }

    public void openIndicateursPage() {
        menu.openIndicateursPage();
    }

    public void openComparaisonAnnees() {
        menu.openComparaisonAnnees();
    }

    public void openComparaisonCentres() {
        menu.openComparaisonCentres();
    }

    public void pdfActivite() {
        menu.pdfActivite();
    }

    public void openHomepage() {
        menu.openHomepage();
    }
}
