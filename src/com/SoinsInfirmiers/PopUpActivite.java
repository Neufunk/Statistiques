package SoinsInfirmiers;

import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import main.Menu;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
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

    private IteratorExcel iteratorExcel = new IteratorExcel();
    private Pdf pdf = new Pdf();
    private Year year = new Year();
    private Periode periode = new Periode();
    private Centre centre = new Centre();
    private static String centreVal;
    private static String monthVal;
    private static String yearVal;

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
        Boolean flag;
        try {
            initIterator();
            processFileA();
            processFileB();
            pdf.buildActivitePdf();
            flag = true;
        } catch (Exception e) {
            e.printStackTrace();
            iteratorExcel.fileNotFound(e);
            flag = false;
        }
        if (flag){
            label.setText("PDF généré avec succès");
            label2.setText("C:/users/" + System.getProperty("user.name") +
                    "/Desktop/Rapport_Activite_" + centreVal + "_" + monthVal + "_" + yearVal+".pdf");
            closeButton.setVisible(true);
            button.setVisible(false);
        } else {
            label.setText("Erreur lors de la génération du rapport PDF");
        }
    }

    private void initIterator() {
        centreVal = comboCentre.getValue();
        monthVal = comboMonth.getValue();
        yearVal = String.valueOf(comboYear.getValue());
        centre.toExcelSheet(comboCentre.getValue());
        year.toPath(comboYear.getValue());
        periode.toExcelColumn(comboMonth.getValue());
        iteratorExcel.setSheet(centre.getSheet());
        iteratorExcel.setPath(year.getPath());
        iteratorExcel.setFiles(year.getFileA(), year.getFileB(), year.getFileC());
    }

    private void processFileA() throws IOException, InvalidFormatException {
        int[] rowIndex = {51, 52, 55, 56, 65, 28, 73, 74, 75, 46, 54, 79, 20, 11, 17, 6, 7, 8, 9, 38, 39, 40, 101, 102, 103, 104};
        iteratorExcel.setColumn(periode.getColumn());
        for (int i = 0; i < rowIndex.length; i++) {
            iteratorExcel.setMasterRow(rowIndex[i]);
            iteratorExcel.startIteration();
            pdf.answerArr[i] = round(iteratorExcel.getContentMasterCell(), 2);
        }
    }

    private void processFileB() throws IOException, InvalidFormatException {
        int[] rowIndexFileB = {13, 14};
        periode.toExcelColumnFileB(comboMonth.getValue());
        iteratorExcel.setColumn(periode.getColumn());
        iteratorExcel.setFiles(year.getFileB(), year.getFileA(), year.getFileC());
        for (int i = 0; i < rowIndexFileB.length; i++) {
            iteratorExcel.setMasterRow(rowIndexFileB[i]);
            iteratorExcel.startIteration();
            pdf.answerArrFileB[i] = round(iteratorExcel.getContentMasterCell(), 2);
        }
    }

    private double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    public void onCloseClick() {
        Menu.pdfStage.close();
    }

    String getCentreVal() {
        return centreVal;
    }

    String getMonthVal() {
        return monthVal;
    }

    String getYearVal() {
        return yearVal;
    }
}
