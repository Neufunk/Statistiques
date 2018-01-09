package main;

import javafx.scene.control.Alert;

import java.io.IOException;
import java.util.Arrays;
import java.util.Properties;

class PatchNote {

    void patchNote() {
        try {
            Properties prop = new Properties();
            prop.load(getClass().getResourceAsStream("/properties/Patchnote.properties"));
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("PATCHNOTE " + Version.versionNumber);
            alert.setHeaderText(prop.getProperty("Patchnote", "vide"));
            alert.setContentText("");
            alert.show();
        } catch (IOException e) {
            displayError(e);
        }
    }

    private void displayError(Exception e) {
        e.printStackTrace();
        String e1 = e.toString();
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur");
        alert.setHeaderText(e1);
        alert.setContentText("STACKTRACE : \t\t" + Arrays.toString(e.getStackTrace()) + "\n" +
                "CAUSE : \t\t\t" + e.getLocalizedMessage() + "\n" + "\t\t" + this.getClass().toString() + " - displayFormatException()");
        alert.showAndWait();
    }
}
