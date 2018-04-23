package SoinsInfirmiers;

import javafx.scene.control.Alert;
import org.apache.poi.hssf.usermodel.HSSFFormulaEvaluator;
import org.apache.poi.hssf.util.CellReference;
import org.apache.poi.ss.usermodel.*;

import java.io.File;
import java.util.Arrays;

class IteratorExcel {

    private String contentTitleMasterCell = "";
    private double contentMasterCell = 0;
    private String[] titleArray = new String[5];
    private double[] contentArray = new double[5];
    private double[] lineChartResult = new double[12];

    private String path;
    private String fileA;
    private String fileB;
    private String fileC;
    private String column;
    private int sheet;

    private int masterRow;
    private int[] pieChartRow;

    void setPath(String path) {
        this.path = path;
    }

    void setFiles(String fileA, String fileB, String fileC) {
        this.fileA = fileA;
        this.fileB = fileB;
        this.fileC = fileC;
    }

    void setSheet(int sheet) {
        this.sheet = sheet;
    }

    void setColumn(String column) {
        this.column = column;
    }

    void setMasterRow(int masterRow) {
        this.masterRow = masterRow;
    }

    void setPieChartRow(int[] pieChartRow) {
        this.pieChartRow = pieChartRow;
    }

    void pieChartIteration() {
        Sheet selectionSheet = createWoorkbook();

        if (masterRow != 0) {
            CellReference cellReference1 = new CellReference(column + masterRow);
            Row masterRowB = selectionSheet.getRow(cellReference1.getRow());
            contentMasterCell = masterRowB.getCell(cellReference1.getCol()).getNumericCellValue();

            CellReference titleCellReference1 = new CellReference("B" + masterRow);
            Row masterRowC = selectionSheet.getRow(titleCellReference1.getRow());
            contentTitleMasterCell = masterRowC.getCell(titleCellReference1.getCol()).getStringCellValue();
        }
        if (pieChartRow != null) {
            for (int i = 0; i < pieChartRow.length; i++) {
                if (pieChartRow[i] != 0) {
                    CellReference cellReference = new CellReference(column + pieChartRow[i]);
                    Row row = selectionSheet.getRow(cellReference.getRow());
                    contentArray[i] = row.getCell(cellReference.getCol()).getNumericCellValue();
                    CellReference titleCellReference = new CellReference("B" + pieChartRow[i]);
                    Row titleRow = selectionSheet.getRow(titleCellReference.getRow());
                    titleArray[i] = titleRow.getCell(titleCellReference.getCol()).getStringCellValue();
                }
            }
        }
    }

    double getContentMasterCell() {
        return contentMasterCell;
    }

    double[] getContentArray() {
        return contentArray;
    }

    String getContentTitleMasterCell() {
        return contentTitleMasterCell;
    }

    String[] getTitleArray() {
        return titleArray;
    }

    void lineChartIteration() {
        Sheet selectionSheet = createWoorkbook();
        CellReference titleCellReference = new CellReference("B" + masterRow);
        Row title = selectionSheet.getRow(titleCellReference.getRow());
        contentTitleMasterCell = title.getCell(titleCellReference.getCol()).getStringCellValue();
        String[] columnReference = {"D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O"};
        for (int i = 0; i < columnReference.length; i++) {
            CellReference cellReference = new CellReference(columnReference[i] + masterRow);
            Row row = selectionSheet.getRow(cellReference.getRow());
            Cell cell = row.getCell(cellReference.getCol());
            lineChartResult[i] = cell.getNumericCellValue();
        }
    }

    double[] getLineChartResult() {
        return lineChartResult;
    }

    private Sheet createWoorkbook() {
        Sheet selectionSheet = null;
        try {
            Workbook wb = WorkbookFactory.create(new File(path + fileA));
            Workbook wb2 = WorkbookFactory.create(new File(path + fileB));
            Workbook wb3 = WorkbookFactory.create(new File(path + fileC));
            HSSFFormulaEvaluator evaluator = (HSSFFormulaEvaluator) wb.getCreationHelper().createFormulaEvaluator();
            HSSFFormulaEvaluator evaluator2 = (HSSFFormulaEvaluator) wb2.getCreationHelper().createFormulaEvaluator();
            HSSFFormulaEvaluator evaluator3 = (HSSFFormulaEvaluator) wb3.getCreationHelper().createFormulaEvaluator();
            String[] workbookNames = {fileA, fileB, fileC};
            HSSFFormulaEvaluator[] evaluators = {evaluator, evaluator2, evaluator3};
            HSSFFormulaEvaluator.setupEnvironment(workbookNames, evaluators);
            selectionSheet = wb.getSheetAt(sheet);
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        return selectionSheet;
    }

    void fileNotFound(Exception e0) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Fichier occupé ou introuvable");
        alert.setHeaderText("Fichier occupé ou introuvable : " + e0.getLocalizedMessage());
        alert.setContentText("STACKTRACE : \t\t" + Arrays.toString(e0.getStackTrace()) + "\n" +
                "FILE : \t\t\t" + e0.getLocalizedMessage() + "\n" + "METHOD : \t\t\t" + this.getClass().toString() + ".fileNotFound()");
        alert.showAndWait();
    }

    void resetVariables() {
        contentTitleMasterCell = "";
        contentArray = new double[5];
        titleArray = new String[5];
        contentMasterCell = 0;
        path = null;
        fileA = null;
        fileB = null;
        fileC = null;
        column = null;
        sheet = 0;
        masterRow = 0;
    }
}