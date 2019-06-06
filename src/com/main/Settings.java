package main;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import tools.Console;

import java.net.URL;
import java.util.ResourceBundle;

public class Settings implements Initializable {

    @FXML
    public AnchorPane menuPane;
    public Button consoleButton;

    private Menu menu = new Menu();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        menuPane.getChildren().get(0).getStyleClass().add("white");
    }

    @FXML
    void onConsoleButtonClick() {
        if (Console.console.isShowing()) {
            Console.console.close();
        } else {
            Console.buildConsole();
        }
    }

    public void openLog(){
        menu.openLog();
    }
}
