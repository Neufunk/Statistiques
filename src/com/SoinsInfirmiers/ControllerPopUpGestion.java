package SoinsInfirmiers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXSpinner;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import main.Date;
import main.Effects;
import main.ExceptionHandler;

import java.net.URL;
import java.util.ResourceBundle;

public class ControllerPopUpGestion implements Initializable {

    @FXML
    private ComboBox<Integer> comboYear;
    @FXML
    private JFXButton closeButton;
    @FXML
    private JFXButton button;
    @FXML
    private JFXSpinner spinner;
    @FXML
    private Label label2;
    @FXML
    private Label label;
    @FXML
    private VBox closePane;

    private String yearStr;
    private double progress = 0;
    private static Exception error;
    private Effects effects = new Effects();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Data data = new Data();
        comboYear.setItems(data.yearList);
        yearStr = Date.getCurrentYearStr();
        comboYear.setPromptText(yearStr);
    }

    public void onButtonClick() {
        effects.setFadeTransition(button, 800, 1, 0);
        spinner.setVisible(true);
        Thread t = new Thread(new IndicateurDeGestion(this));
        t.start();
    }

    public void onRefreshButtonClick(){
        label.setVisible(false);
        label2.setVisible(false);
        progress = 0;
        spinner.setProgress(0);
        spinner.setProgress(-1);
        effects.setFadeTransition(closeButton, 200, 1, 0);
        spinner.setVisible(true);
        Thread t = new Thread(new IndicateurDeGestion(this));
        t.start();
    }

    void updateProgress(){
        spinner.setProgress(progress+=0.2816/100);
    }

    void updateGUI(Boolean bool){
        closePane.setVisible(true);
        closeButton.setVisible(true);
        label.setVisible(true);
        label2.setVisible(true);
        System.out.println("GUI UPDATED ON THREAD: " + Thread.currentThread().getName());
        if (bool) {
            label.setText("PDF généré avec succès");
            label2.setText("C:/users/" + System.getProperty("user.name") +
                    "/Desktop/Indicateurs_Gestion_" + yearStr + ".pdf");
        } else {
            label.setText("Erreur lors de la génération du rapport PDF");
            ExceptionHandler.switchException(error, this.getClass());
        }
        spinner.setVisible(false);
        closeButton.setVisible(true);
        spinner.setProgress(-1);
        progress = 0;
    }

    public void onComboSelection() {
        yearStr = String.valueOf(comboYear.getValue());
        System.out.print(yearStr);
    }

    String getYearStr() {
        return yearStr;
    }

    static void setError(Exception e) {
        error = e;
    }
}
