package SoinsInfirmiers;

import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import main.Menu;

import java.net.URL;
import java.util.ResourceBundle;

public class PopUpActivite implements Initializable {
    @FXML
    private JFXButton button;
    @FXML
    private JFXButton closeButton;
    @FXML
    private ComboBox<Integer> comboYear;
    @FXML
    private ComboBox<String> comboMonth;
    @FXML
    private Label label;
    @FXML
    private Label label2;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadCombo();
    }

    private void loadCombo(){
        Data data = new Data();
        comboYear.setItems(data.yearList);
        comboMonth.setItems(data.periodList);
    }

    public void onButtonClick() {
        Boolean flag;
        Pdf pdf = new Pdf();
        try {
            pdf.buildActivitePdf();
            flag = true;
        } catch (Exception e) {
            flag = false;
        }
        if (flag){
            label.setText("PDF généré avec succès");
            label2.setText("C:/users/" + System.getProperty("user.name") + "/Desktop/Rapport_Activite.pdf");
            closeButton.setVisible(true);
            button.setVisible(false);
        } else {
            label.setText("Erreur lors de la génération du rapport PDF");
        }
    }

    public void onCloseClick() {
        Menu.pdfStage.close();
    }
}
