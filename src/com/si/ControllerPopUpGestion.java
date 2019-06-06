package si;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXSpinner;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import tools.*;

import java.net.URL;
import java.util.ResourceBundle;

public class ControllerPopUpGestion implements Initializable {

    @FXML
    private ComboBox<Integer> comboYear;
    @FXML
    private JFXButton closeButton, button;
    @FXML
    private JFXSpinner spinner;
    @FXML
    private Label label, label2, label3;

    private int year = Date.getCurrentYearInt();
    private double progress = 0;
    private static Exception error;
    private final Effects effects = new Effects();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        for (int value : Date.getYearList()){
            comboYear.getItems().addAll(value);
        }
        comboYear.setValue(year);
    }

    public void onButtonClick() {
        Time.setStartTime();
        effects.setFadeTransition(button, 800, 1, 0);
        spinner.setVisible(true);
        Thread t = new Thread(new IndicateurDeGestion(this));
        t.start();
    }

    public void onRefreshButtonClick() {
        Time.setStartTime();
        label.setVisible(false);
        label2.setVisible(false);
        label3.setVisible(false);
        effects.setFadeTransition(closeButton, 500, 1, 0);
        spinner.setVisible(true);
        Thread t = new Thread(new IndicateurDeGestion(this));
        t.start();
    }

    void updateProgress() {
        spinner.setProgress(progress+=0.002777); // 100%/(NBRE INDICATEURS * CENTRES) = 1/(60*6) = 1/360
    }

    void updateGUI(Boolean success) {
        button.setVisible(false);
        effects.setFadeTransition(closeButton, 200, 0, 1);
        label.setVisible(true);
        if (success) {
            label.setText("PDF généré avec succès");
            label2.setVisible(true);
            label3.setVisible(true);
            label2.setText("C:/users/" + System.getProperty("user.name") +
                    "/Desktop/Indicateurs_Gestion_" + year + ".pdf");
            label3.setText("Rapport généré en " + Time.computeElapsed() + " secondes");
            Console.appendln("Rapport généré avec succès en " + Time.computeElapsed() + " secondes");
        } else {
            label.setText("Erreur lors de la génération du rapport PDF");
            Console.appendln("Erreur lors de la génération du rapport PDF");
            ExceptionHandler.switchException(error, this.getClass());
        }
        spinner.setVisible(false);
        closeButton.setVisible(true);
        spinner.setProgress(ProgressIndicator.INDETERMINATE_PROGRESS);
        progress = 0;
    }

    int getYear() {
        return comboYear.getValue();
    }

    static void setError(Exception e) {
        error = e;
    }
}
