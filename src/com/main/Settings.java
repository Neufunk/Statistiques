package main;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.ResourceBundle;

public class Settings implements Initializable {

    @FXML
    public AnchorPane menuPane;
    public Button consoleButton;
    public TableView logTable;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        menuPane.getChildren().get(0).getStyleClass().add("white");
    }

    @FXML
    void onConsoleButtonClick(){
        System.out.println("BUTTON PRESSED");
    }
}
