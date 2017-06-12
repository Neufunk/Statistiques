package com.controllers.common;

import com.Main;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.transitions.hamburger.HamburgerNextArrowBasicTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


public class HomePage implements Initializable {

    @FXML
    private JFXHamburger hamburger;
    @FXML
    private JFXButton siButton;


    // Transition Hamburger Menu
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        HamburgerNextArrowBasicTransition transition = new HamburgerNextArrowBasicTransition(hamburger);
        transition.setRate(-1);
        hamburger.addEventHandler(MouseEvent.MOUSE_CLICKED, (e) -> {
            transition.setRate(transition.getRate() * -1);
            transition.play();
        });

        // Lien vers SI Page
        siButton.addEventHandler(MouseEvent.MOUSE_CLICKED, (e) ->  {
            Stage stage = Main.getPrimaryStage();
            try {
                Parent root = FXMLLoader.load(getClass().getResource("fxml/StatistiquesSI.fxml"));
                stage.setScene(new Scene(root));
                stage.setTitle("Soins Infirmiers - Statistiques // FX_Alpha 1");
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        });
    }

    // Fermeture du programme
    public void closeButtonAction() throws InterruptedException {
        System.exit(0);
    }
}
