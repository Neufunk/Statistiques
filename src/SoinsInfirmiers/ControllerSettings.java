package SoinsInfirmiers;

import com.Effects;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.transitions.hamburger.HamburgerBackArrowBasicTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ControllerSettings implements Initializable {
    @FXML
    private AnchorPane mainPane;
    @FXML
    private JFXCheckBox debugMacBook;
    @FXML
    private AnchorPane maskPane;
    @FXML
    private JFXDrawer drawer;
    @FXML
    private JFXHamburger hamburger;
    @FXML
    private JFXCheckBox debugBox;
    @FXML
    private JFXCheckBox debugBoxMBP;


    private Effects effects = new Effects();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        drawMenu();
        initializeDebugBox();
    }

    private void drawMenu() {
        VBox box = null;
        try {
            box = FXMLLoader.load(getClass().getResource("/com/FXML/DrawerSI.fxml"));
            drawer.setSidePane(box);
        } catch (IOException e) {
            e.printStackTrace();
        }
        HamburgerBackArrowBasicTransition transition = new HamburgerBackArrowBasicTransition(hamburger);
        transition.setRate(-1);
        hamburger.addEventHandler(javafx.scene.input.MouseEvent.MOUSE_CLICKED, (e) -> {
            transition.setRate(transition.getRate() * -1);
            transition.play();
            if (drawer.isShown()) {
                drawer.close();
                maskPane.setVisible(false);
            } else {
                drawer.open();
                maskPane.setVisible(true);
                effects.setFadeTransition(maskPane, 600, 0, 0.2);
                drawer.setVisible(true);
            }
        });
        mainPane.addEventHandler(MouseEvent.MOUSE_CLICKED, (e) -> {
            if (drawer.isShown()) {
                drawer.close();
                maskPane.setVisible(false);
                transition.setRate(transition.getRate() * -1);
                transition.play();
            }
        });
        mainPane.addEventHandler(MouseEvent.MOUSE_MOVED, (e) -> {
            if (drawer.isHidden()){
                drawer.setVisible(false);
            }
        });
    }

    private void initializeDebugBox() {
        debugBox.addEventHandler(MouseEvent.MOUSE_CLICKED, (event -> {
            if (debugBox.isSelected()) {
                Data.yearList.add(1337);
            } else {
                Data.yearList.remove(3);
            }
        }));
    }
}
