package si;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import main.Menu;

import java.net.URL;
import java.util.ResourceBundle;

public class ControllerSelectSi implements Initializable {

    @FXML
    private AnchorPane menuPane;

    private final Menu menu = new Menu();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        menuPane.getChildren().get(0).getStyleClass().add("white");
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

    public void openSelectVisitesPatients() { menu.openSelectVisitesPatients(); }

    public void openRepartitionAge() {
        menu.openRepartitionAge();
    }

    public void pdfGestion() {menu.pdfGestion();}

    public void openHomepage() {
        menu.openHomepage();
    }
}
