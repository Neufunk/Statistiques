package SoinsInfirmiers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXSpinner;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import main.Menu;

import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
    private Label label3;

    private final Rapports pdf = new Rapports();
    private static String yearVal;
    static Boolean flag = false;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Data data = new Data();
        comboYear.setItems(data.yearList);
        comboYear.setPromptText(getCurrentDate());
        yearVal = getCurrentDate();
    }

    private String getCurrentDate() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy");
        LocalDate localDate = LocalDate.now();
        return dtf.format(localDate);
    }

    public void onButtonClick() {
        label3.setVisible(true);
        button.setVisible(false);
        spinner.setVisible(true);

        new Thread(() -> {
            pdf.buildIndicateurDeGestionPdf();
            Platform.runLater(() -> {
                if (flag) {
                    label.setText("PDF généré avec succès");
                    label2.setText("C:/users/" + System.getProperty("user.name") +
                            "/Desktop/Indicateurs_Gestion_" + yearVal + ".pdf");
                } else {
                    label.setText("Erreur lors de la génération du rapport PDF");
                }
                spinner.setVisible(false);
                closeButton.setVisible(true);
            });
        }).start();
    }

    public void onCloseClick() {
        Menu.gestionStage.close();
    }

    public void onComboSelection(){
        yearVal = String.valueOf(comboYear.getValue());
        System.out.print(yearVal);
    }

    String getYearVal() {
        return yearVal;
    }

}
