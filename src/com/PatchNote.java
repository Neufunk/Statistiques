package com;

import javafx.scene.control.Alert;

import java.io.IOException;
import java.util.Properties;

class PatchNote {

    private LoadProperties loadProperties = new LoadProperties();

    void patchNote() {
        try {
            Properties prop = loadProperties.load("C:\\Users\\johnathanv\\IdeaProjects\\Statistiques_FX\\src\\resources\\properties\\Patchnote.properties");
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("PATCHNOTE " + Version.versionNumber);
        alert.setHeaderText(prop.getProperty("Patchnote", "vide"));
        alert.setContentText("");
        alert.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
