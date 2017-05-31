package com.SoinsInfirmiers;


import org.apache.poi.hssf.usermodel.HSSFFormulaEvaluator;
import org.apache.poi.hssf.util.CellReference;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;

import java.io.File;
import java.io.IOException;

import static org.apache.poi.ss.usermodel.Cell.CELL_TYPE_ERROR;

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
    private String path;
    private String fileA;
    private String fileB;
    private String fileC;
    private String column;
    private int sheet;
    private int masterRow;
    private int rowA;
    private int rowB;
    private int rowC;
    private int rowD;
    private int rowE;

    void setPath(String path) {
        this.path = path;
    }

    void setFileA(String fileA) {
        this.fileA = fileA;
    }

    void setFileB(String fileB) {
        this.fileB = fileB;
    }

    void setFileC(String fileC) {
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

        CellReference cellReference1 = new CellReference(column + masterRow);
        Row masterRowB = selectionSheet.getRow(cellReference1.getRow());
        masterCell = masterRowB.getCell(cellReference1.getCol());

        CellReference titleCellReference1 = new CellReference("B" + masterRow);
        Row masterRowC = selectionSheet.getRow(titleCellReference1.getRow());
        masterTitleCell = masterRowC.getCell(titleCellReference1.getCol());

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
        // TODO : Case CellValue.ERROR
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

    public double getContentMasterCell() {
        return contentMasterCell;
    }

    public double getContentCellA() {
        return contentCellA;
    }

    public double getContentCellB() {
        return contentCellB;
    }

    public double getContentCellC() {
        return contentCellC;
    }

    public double getContentCellD() {
        return contentCellD;
    }

    public double getContentCellE() {
        return contentCellE;
    }

    public String getContentTitleMasterCell() {
        return contentTitleMasterCell;
    }

    public String getContentTitleCellA() {
        return contentTitleCellA;
    }

    public String getContentTitleCellB() {
        return contentTitleCellB;
    }

    public String getContentTitleCellC() {
        return contentTitleCellC;
    }

    public String getContentTitleCellD() {
        return contentTitleCellD;
    }

    public String getContentTitleCellE() {
        return contentTitleCellE;
    }

    public void closeConnection() throws IOException {
            wb.close();
            wb2.close();
            wb3.close();
    }
}
