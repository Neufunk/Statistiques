package SoinsInfirmiers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXSpinner;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import main.Menu;

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
    private ComboBox<Integer> comboYear;
    @FXML
    private ComboBox<String> comboMonth;
    @FXML
    private ComboBox<String> comboCentre;
    @FXML
    private JFXSpinner spinner;
    @FXML
    private Label label;
    @FXML
    private Label label2;
    @FXML
    private Label label3;

    private final IteratorExcel iteratorExcel = new IteratorExcel();
    private final PdfActivite pdf = new PdfActivite();
    private final Year YEAR = new Year();
    private final Periode PERIODE = new Periode();
    private final Centre CENTRE = new Centre();
    private static String centreVal;
    private static String monthVal;
    private static String yearVal;
    private Boolean flag = false;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadCombo();
    }

    private void loadCombo() {
        Data data = new Data();
        comboYear.setItems(data.yearList);
        comboMonth.setItems(data.periodList);
        comboCentre.setItems(data.centerList);
    }

    public void onButtonClick() {
        label3.setVisible(true);
        button.setVisible(false);
        spinner.setVisible(true);

        new Thread(() -> {
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
            Platform.runLater(() -> {
                if (flag) {
                    label.setText("PDF généré avec succès");
                    label2.setText("C:/users/" + System.getProperty("user.name") +
                            "/Desktop/Rapport_Activite_" + centreVal + "_" + monthVal + "_" + yearVal + ".pdf");
                } else {
                    label.setText("Erreur lors de la génération du rapport PDF");
                }
                spinner.setVisible(false);
                closeButton.setVisible(true);
            });
        }).start();
    }

    private void initIterator() {
        centreVal = comboCentre.getValue();
        monthVal = comboMonth.getValue();
        yearVal = String.valueOf(comboYear.getValue());
        CENTRE.toExcelSheet(comboCentre.getValue());
        YEAR.toPath(comboYear.getValue());
        PERIODE.toExcelColumn(comboMonth.getValue());
        iteratorExcel.setSheet(CENTRE.getSheet());
        iteratorExcel.setPath(YEAR.getPath());
        iteratorExcel.setFiles(YEAR.getFileA(), YEAR.getFileB(), YEAR.getFileC());
    }

    private void processFileA() {
        int[] rowIndex = {51, 52, 55, 56, 65, 28, 73, 74, 75, 46, 54, 79, 20, 11, 17, 6, 7, 8, 9, 38, 39, 40, 101, 102, 103, 104};
        iteratorExcel.setColumn(PERIODE.getColumn());
        for (int i = 0; i < rowIndex.length; i++) {
            iteratorExcel.setMasterRow(rowIndex[i]);
            iteratorExcel.pieChartIteration();
            pdf.answerArr[i] = iteratorExcel.getContentMasterCell();
        }
    }

    private void processFileB() {
        int[] rowIndexFileB = {13, 14, 15};
        PERIODE.toExcelColumnFileB(comboMonth.getValue());
        iteratorExcel.setColumn(PERIODE.getColumn());
        iteratorExcel.setFiles(YEAR.getFileB(), YEAR.getFileA(), YEAR.getFileC());
        for (int i = 0; i < rowIndexFileB.length; i++) {
            iteratorExcel.setMasterRow(rowIndexFileB[i]);
            iteratorExcel.pieChartIteration();
            pdf.answerArrFileB[i] = round(iteratorExcel.getContentMasterCell());
        }
    }

    private double round(double value) {
        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(2, RoundingMode.HALF_UP);
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
