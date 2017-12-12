package SoinsInfirmiers;


import javafx.scene.control.Alert;
import org.apache.poi.hssf.usermodel.HSSFFormulaEvaluator;
import org.apache.poi.hssf.util.CellReference;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public class IteratorExcel {

    private Workbook wb;
    private Workbook wb2;
    private Workbook wb3;

    private Cell masterCell, masterTitleCell;
    private Cell cellA, cellB, cellC, cellD, cellE, titleCellA, titleCellB, titleCellC,
            titleCellD, titleCellE;

    private String contentTitleMasterCell = "";
    private String contentTitleCellA = "";
    private String contentTitleCellB = "";
    private String contentTitleCellC = "";
    private String contentTitleCellD = "";
    private String contentTitleCellE = "";

    private double contentMasterCell = 0;
    private double contentCellA = 0;
    private double contentCellB = 0;
    private double contentCellC = 0;
    private double contentCellD = 0;
    private double contentCellE = 0;
    private double contentJanvierCell = 0;
    private double contentFevrierCell = 0;
    private double contentMarsCell = 0;
    private double contentAvrilCell = 0;
    private double contentMaiCell = 0;
    private double contentJuinCell = 0;
    private double contentJuilletCell = 0;
    private double contentAoutCell = 0;
    private double contentSeptembreCell = 0;
    private double contentOctobreCell = 0;
    private double contentNovembreCell = 0;
    private double contentDecembreCell = 0;

    private String path;
    private String fileA;
    private String fileB;
    private String fileC;
    private String column;
    private int sheet;

    private int masterRow;
    private int rowA = 0;
    private int rowB = 0;
    private int rowC = 0;
    private int rowD = 0;
    private int rowE = 0;

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
    void setRowA(int rowA) {
        this.rowA = rowA;
    }
    void setRowB(int rowB) {
        this.rowB = rowB;
    }
    void setRowC(int rowC) {
        this.rowC = rowC;
    }
    void setRowD(int rowD) {
        this.rowD = rowD;
    }
    void setRowE(int rowE) {
        this.rowE = rowE;
    }

    void startIteration() throws IOException, InvalidFormatException {
        wb = WorkbookFactory.create(new File(path + fileA));
        wb2 = WorkbookFactory.create(new File(path + fileB));
        wb3 = WorkbookFactory.create(new File(path + fileC));
        HSSFFormulaEvaluator evaluator = (HSSFFormulaEvaluator) wb.getCreationHelper().createFormulaEvaluator();
        HSSFFormulaEvaluator evaluator2 = (HSSFFormulaEvaluator) wb2.getCreationHelper().createFormulaEvaluator();
        HSSFFormulaEvaluator evaluator3 = (HSSFFormulaEvaluator) wb3.getCreationHelper().createFormulaEvaluator();
        String[] workbookNames = {fileA, fileB, fileC};
        HSSFFormulaEvaluator[] evaluators = {evaluator, evaluator2, evaluator3};
        HSSFFormulaEvaluator.setupEnvironment(workbookNames, evaluators);
        Sheet selectionSheet = wb.getSheetAt(sheet);
         if (masterRow != 0) {
            CellReference cellReference1 = new CellReference(column + masterRow);
            Row masterRowB = selectionSheet.getRow(cellReference1.getRow());
            masterCell = masterRowB.getCell(cellReference1.getCol());
            CellReference titleCellReference1 = new CellReference("B" + masterRow);
            Row masterRowC = selectionSheet.getRow(titleCellReference1.getRow());
            masterTitleCell = masterRowC.getCell(titleCellReference1.getCol());
         }
        if (rowA != 0) {
            CellReference cellReference2 = new CellReference(column + rowA);
            Row rowAA = selectionSheet.getRow(cellReference2.getRow());
            cellA = rowAA.getCell(cellReference2.getCol());
            CellReference titleCellReference2 = new CellReference("B" + rowA);
            Row titleRowA = selectionSheet.getRow(titleCellReference2.getRow());
            titleCellA = titleRowA.getCell(titleCellReference2.getCol());

        }
        if (rowB != 0) {
            CellReference cellReference3 = new CellReference(column + rowB);
            Row rowB = selectionSheet.getRow(cellReference3.getRow());
            cellB = rowB.getCell(cellReference3.getCol());
            CellReference titleCellReference3 = new CellReference("B" + this.rowB);
            Row titleRowB = selectionSheet.getRow(titleCellReference3.getRow());
            titleCellB = titleRowB.getCell(titleCellReference3.getCol());
        }
        if (rowC != 0) {
            CellReference cellReference4 = new CellReference(column + rowC);
            Row rowC = selectionSheet.getRow(cellReference4.getRow());
            cellC = rowC.getCell(cellReference4.getCol());
            CellReference titleCellReference4 = new CellReference("B" + this.rowC);
            Row titleRowC = selectionSheet.getRow(titleCellReference4.getRow());
            titleCellC = titleRowC.getCell(titleCellReference4.getCol());
        }
        if (rowD != 0) {
            CellReference cellReference5 = new CellReference(column + rowD);
            Row rowD = selectionSheet.getRow(cellReference5.getRow());
            cellD = rowD.getCell(cellReference5.getCol());
            CellReference titleCellReference5 = new CellReference("B" + this.rowD);
            Row titleRowD = selectionSheet.getRow(titleCellReference5.getRow());
            titleCellD = titleRowD.getCell(titleCellReference5.getCol());
        }
        if (rowE != 0) {
            CellReference cellReference6 = new CellReference(column + rowE);
            Row rowE = selectionSheet.getRow(cellReference6.getRow());
            cellE = rowE.getCell(cellReference6.getCol());
            CellReference titleCellReference6 = new CellReference("B" + this.rowE);
            Row titleRowE = selectionSheet.getRow(titleCellReference6.getRow());
            titleCellE = titleRowE.getCell(titleCellReference6.getCol());
        }

        if (masterCell != null) {
            contentMasterCell = masterCell.getNumericCellValue();
            contentTitleMasterCell = masterTitleCell.getStringCellValue();
        }
        if (cellA != null) {
            contentCellA = cellA.getNumericCellValue();
            contentTitleCellA = titleCellA.getStringCellValue();
        }
        if (cellB != null) {
            contentCellB = cellB.getNumericCellValue();
            contentTitleCellB = titleCellB.getStringCellValue();
        }
        if (cellC != null) {
            contentCellC = cellC.getNumericCellValue();
            contentTitleCellC = titleCellC.getStringCellValue();
        }
        if (cellD != null) {
            contentCellD = cellD.getNumericCellValue();
            contentTitleCellD = titleCellD.getStringCellValue();
        }
        if (cellE != null) {
            contentCellE = cellE.getNumericCellValue();
            contentTitleCellE = titleCellE.getStringCellValue();
        }
    }

    double getContentMasterCell() {
        return contentMasterCell;
    }
    double getContentCellA() {
        return contentCellA;
    }
    double getContentCellB() {
        return contentCellB;
    }
    double getContentCellC() {
        return contentCellC;
    }
    double getContentCellD() {
        return contentCellD;
    }
    double getContentCellE() {
        return contentCellE;
    }
    String getContentTitleMasterCell() {
        return contentTitleMasterCell;
    }
    String getContentTitleCellA() {
        return contentTitleCellA;
    }
    String getContentTitleCellB() {
        return contentTitleCellB;
    }
    String getContentTitleCellC() {
        return contentTitleCellC;
    }
    String getContentTitleCellD() {
        return contentTitleCellD;
    }
    String getContentTitleCellE() {
        return contentTitleCellE;
    }

    /************************************LineChart****************************************/

    void allYearIteration() throws IOException, InvalidFormatException {
        wb = WorkbookFactory.create(new File(path + fileA));
        wb2 = WorkbookFactory.create(new File(path + fileB));
        wb3 = WorkbookFactory.create(new File(path + fileC));
        HSSFFormulaEvaluator evaluator = (HSSFFormulaEvaluator) wb.getCreationHelper().createFormulaEvaluator();
        HSSFFormulaEvaluator evaluator2 = (HSSFFormulaEvaluator) wb2.getCreationHelper().createFormulaEvaluator();
        HSSFFormulaEvaluator evaluator3 = (HSSFFormulaEvaluator) wb3.getCreationHelper().createFormulaEvaluator();
        String[] workbookNames = {fileA, fileB, fileC};
        HSSFFormulaEvaluator[] evaluators = {evaluator, evaluator2, evaluator3};
        HSSFFormulaEvaluator.setupEnvironment(workbookNames, evaluators);
        Sheet selectionSheet = wb.getSheetAt(sheet);

        CellReference titleCellReference = new CellReference("B" + masterRow);
        Row title = selectionSheet.getRow(titleCellReference.getRow());
        masterTitleCell = title.getCell(titleCellReference.getCol());
        CellReference cellReference1 = new CellReference("D" + masterRow);
        Row janvier = selectionSheet.getRow(cellReference1.getRow());
        Cell janvierCell = janvier.getCell(cellReference1.getCol());
        CellReference cellReference2 = new CellReference("E" + masterRow);
        Row fevrier = selectionSheet.getRow(cellReference2.getRow());
        Cell fevrierCell = fevrier.getCell(cellReference2.getCol());
        CellReference cellReference3 = new CellReference("F" + masterRow);
        Row mars = selectionSheet.getRow(cellReference3.getRow());
        Cell marsCell = mars.getCell(cellReference3.getCol());
        CellReference cellReference4 = new CellReference("G" + masterRow);
        Row avril = selectionSheet.getRow(cellReference4.getRow());
        Cell avrilCell = avril.getCell(cellReference4.getCol());
        CellReference cellReference5 = new CellReference("H" + masterRow);
        Row mai = selectionSheet.getRow(cellReference5.getRow());
        Cell maiCell = mai.getCell(cellReference5.getCol());
        CellReference cellReference6 = new CellReference("I" + masterRow);
        Row juin = selectionSheet.getRow(cellReference6.getRow());
        Cell juinCell = juin.getCell(cellReference6.getCol());
        CellReference cellReference7 = new CellReference("J" + masterRow);
        Row juillet = selectionSheet.getRow(cellReference7.getRow());
        Cell juilletCell = juillet.getCell(cellReference7.getCol());
        CellReference cellReference8 = new CellReference("K" + masterRow);
        Row aout = selectionSheet.getRow(cellReference8.getRow());
        Cell aoutCell = aout.getCell(cellReference8.getCol());
        CellReference cellReference9 = new CellReference("L" + masterRow);
        Row septembre = selectionSheet.getRow(cellReference9.getRow());
        Cell septembreCell = septembre.getCell(cellReference9.getCol());
        CellReference cellReference10 = new CellReference("M" + masterRow);
        Row octobre = selectionSheet.getRow(cellReference10.getRow());
        Cell octobreCell = octobre.getCell(cellReference10.getCol());
        CellReference cellReference11 = new CellReference("N" + masterRow);
        Row novembre = selectionSheet.getRow(cellReference11.getRow());
        Cell novembreCell = novembre.getCell(cellReference11.getCol());
        CellReference cellReference12 = new CellReference("O" + masterRow);
        Row decembre = selectionSheet.getRow(cellReference12.getRow());
        Cell decembreCell = decembre.getCell(cellReference12.getCol());

        contentTitleMasterCell = masterTitleCell.getStringCellValue();
        contentJanvierCell = janvierCell.getNumericCellValue();
        contentFevrierCell = fevrierCell.getNumericCellValue();
        contentMarsCell = marsCell.getNumericCellValue();
        contentAvrilCell = avrilCell.getNumericCellValue();
        contentMaiCell = maiCell.getNumericCellValue();
        contentJuinCell = juinCell.getNumericCellValue();
        contentJuilletCell = juilletCell.getNumericCellValue();
        contentAoutCell = aoutCell.getNumericCellValue();
        contentSeptembreCell = septembreCell.getNumericCellValue();
        contentOctobreCell = octobreCell.getNumericCellValue();
        contentNovembreCell = novembreCell.getNumericCellValue();
        contentDecembreCell = decembreCell.getNumericCellValue();
    }

    double getContentJanvierCell(){
        return contentJanvierCell;
    }
    double getContentFevrierCell(){
        return contentFevrierCell;
    }
    double getContentMarsCell(){
        return contentMarsCell;
    }
    double getContentAvrilCell(){
        return contentAvrilCell;
    }
    double getContentMaiCell(){
        return contentMaiCell;
    }
    double getContentJuinCell(){
        return contentJuinCell;
    }
    double getContentJuilletCell(){
        return contentJuilletCell;
    }
    double getContentAoutCell(){
        return contentAoutCell;
    }
    double getContentSeptembreCell(){
        return contentSeptembreCell;
    }
    double getContentOctobreCell(){
        return contentOctobreCell;
    }
    double getContentNovembreCell(){
        return contentNovembreCell;
    }
    double getContentDecembreCell(){
        return contentDecembreCell;
    }

    void fileNotFound(Exception e0) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Fichier occupé ou introuvable");
        alert.setHeaderText("Fichier occupé ou introuvable : "+e0.getLocalizedMessage());
        alert.setContentText("STACKTRACE : \t\t" + Arrays.toString(e0.getStackTrace()) + "\n" +
                "FILE : \t\t\t" + e0.getLocalizedMessage() + "\n" + "METHOD : \t\t\t" + this.getClass().toString() + ".fileNotFound()");
        alert.showAndWait();
    }

    void closeConnection() throws IOException {
        wb.close();
        wb2.close();
        wb3.close();
    }

    void resetVariables() {
        wb = null;
        wb2 = null;
        wb3 = null;
        masterCell = null;
        masterTitleCell = null;
        cellA = null;
        cellB = null;
        cellC = null;
        cellD = null;
        cellE = null;
        titleCellA = null;
        titleCellB = null;
        titleCellC = null;
        titleCellD = null;
        titleCellE = null;

        contentTitleMasterCell = "";
        contentTitleCellA = "";
        contentTitleCellB = "";
        contentTitleCellC = "";
        contentTitleCellD = "";
        contentTitleCellE = "";

        contentMasterCell = 0;
        contentCellA = 0;
        contentCellB = 0;
        contentCellC = 0;
        contentCellD = 0;
        contentCellE = 0;

        path = null;
        fileA = null;
        fileB = null;
        fileC = null;
        column = null;
        sheet = 0;

        masterRow = 0;
        rowA = 0;
        rowB = 0;
        rowC = 0;
        rowD = 0;
        rowE = 0;
    }
}
