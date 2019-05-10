package main;

import javafx.scene.control.Alert;
import tools.ExceptionHandler;
import tools.Version;

import java.io.IOException;
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
            ExceptionHandler.switchException(e, this.getClass());
        }
    }
}
