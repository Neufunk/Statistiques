package main;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;

public class Settings implements Initializable {

    @FXML
    private VBox menuBox;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Menu menu = new Menu();
        menu.loadMenuBar(menuBox);
    }
}
