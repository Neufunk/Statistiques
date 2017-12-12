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
    public ComboBox<Integer> comboYear;
    @FXML
    public ComboBox<String> comboMonth;
    @FXML
    public ComboBox<String> comboCentre;
    @FXML
    private Label label;
    @FXML
    private Label label2;

    IteratorExcel iteratorExcel = new IteratorExcel();
    Pdf pdf = new Pdf();
    Year year = new Year();
    Periode periode = new Periode();
    Centre centre = new Centre();
    public static String centreVal;
    public static String monthVal;
    public static String yearVal;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadCombo();
    }

    private void loadCombo(){
        Data data = new Data();
        comboYear.setItems(data.yearList);
        comboMonth.setItems(data.periodList);
        comboCentre.setItems(data.centerList);
    }

    public void onButtonClick() {
        initIterator();
        Boolean flag;
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

    private void initIterator(){
        int[] rowIndex = {51, 52, 55, 56, 65, 28, 73, 74, 75, 46, 47, 79, 20, 11, 17, 6, 7, 8, 9};
        centreVal = comboCentre.getValue();
        monthVal = comboMonth.getValue();
        yearVal = String.valueOf(comboYear.getValue());
        centre.toExcelSheet(comboCentre.getValue());
        year.toPath(comboYear.getValue());
        periode.toExcelColumn(comboMonth.getValue());
        iteratorExcel.setSheet(centre.getSheet());
        iteratorExcel.setPath(year.getPath());
        iteratorExcel.setFiles(year.getFileA(), year.getFileB(), year.getFileC());
        iteratorExcel.setColumn(periode.getColumn());
        for (int i = 0; i < rowIndex.length; i++) {
            iteratorExcel.setMasterRow(rowIndex[i]);
            startIteration();
            pdf.answerArr[i] = iteratorExcel.getContentMasterCell();
        }
    }

    private void startIteration(){
        try {
            iteratorExcel.startIteration();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onCloseClick() {
        Menu.pdfStage.close();
    }
}
