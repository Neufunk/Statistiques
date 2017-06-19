package com;

import javafx.scene.control.Alert;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class PatchNote {

    public static Properties load (String fileName) throws IOException {
        Properties properties = new Properties();
        FileInputStream input = new FileInputStream(fileName);
        try {
            properties.load(input);
            return properties;
        } finally {
            input.close();
        }
    }

    public void patchNote() {
        try {
            Properties prop = PatchNote.load("C:\\Users\\johnathanv\\IdeaProjects\\Statistiques_FX\\src\\resources\\properties\\Patchnote.properties");
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
