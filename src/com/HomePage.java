package com;

import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


public class HomePage implements Initializable {

    @FXML
    private JFXButton siButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        statistiquesSi();
    }

    private void statistiquesSi(){
        Tooltip.install(siButton, new Tooltip("Département Soins Infirmiers"));
        siButton.addEventHandler(MouseEvent.MOUSE_CLICKED, (e) ->  {
            Stage stage = Main.getPrimaryStage();
            try {
                Parent root = FXMLLoader.load(getClass().getResource("FXML/StatistiquesSI.fxml"));
                stage.setScene(new Scene(root));
                stage.setTitle(Strings.pageTitle0);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        });
    }

    public void closeButtonAction() throws InterruptedException {
        System.exit(0);
    }
}

