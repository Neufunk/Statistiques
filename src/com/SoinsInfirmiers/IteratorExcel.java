package com.SoinsInfirmiers;


import org.apache.poi.hssf.usermodel.HSSFFormulaEvaluator;
import org.apache.poi.hssf.util.CellReference;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;

import java.io.File;
import java.io.IOException;

public class IteratorExcel {

    private Cell masterCell;
    private Cell cellA, cellB, cellC, cellD, cellE;
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

    public void setPath(String path){
        this.path = path;
    }
    public void setFileA(String fileA){
        this.fileA = fileA;
    }
    public void setFileB(String fileB){
        this.fileB = fileB;
    }
    public void setFileC(String fileC){
        this.fileC = fileC;
    }
    public void setSheet(int sheet){
        this.sheet = sheet;
    }
    public void setColumn(String column){
        this.column = column;
    }
    public void setMasterRow (int masterRow){
        this.masterRow = masterRow;
    }
    public void setRowA(int rowA){
        this.rowA = rowA;
    }
    public void setRowB(int rowB){
        this.rowB = rowB;
    }
    public void setRowC(int rowC){
        this.rowC = rowC;
    }
    public void setRowD(int rowD){
        this.rowD = rowD;
    }
    public void setRowE(int rowE){
        this.rowE = rowE;
    }

    void startIteration() throws IOException, InvalidFormatException {

        Workbook wb = WorkbookFactory.create(new File(path+fileA));
        Workbook wb2 = WorkbookFactory.create(new File(path+fileB));
        Workbook wb3 = WorkbookFactory.create(new File(path+fileC));
        HSSFFormulaEvaluator evaluator = (HSSFFormulaEvaluator) wb.getCreationHelper().createFormulaEvaluator();
        HSSFFormulaEvaluator evaluator2 = (HSSFFormulaEvaluator) wb2.getCreationHelper().createFormulaEvaluator();
        HSSFFormulaEvaluator evaluator3 = (HSSFFormulaEvaluator) wb3.getCreationHelper().createFormulaEvaluator();
        String[] workbookNames = {fileA, fileB, fileC};
        HSSFFormulaEvaluator[] evaluators = {evaluator, evaluator2, evaluator3};
        HSSFFormulaEvaluator.setupEnvironment(workbookNames, evaluators);
        Sheet selectionSheet = wb.getSheetAt(sheet);

        CellReference cellReference = new CellReference(column + masterRow);
        Row masterRowB = selectionSheet.getRow(cellReference.getRow());
        masterCell = masterRowB.getCell(cellReference.getCol());

        if (rowA != 0) {
            CellReference cellReference2 = new CellReference(column + rowA);
            Row rowA = selectionSheet.getRow(cellReference2.getRow());
            cellA = rowA.getCell(cellReference2.getCol());
        }
        if (rowB != 0) {
            CellReference cellReference3 = new CellReference(column + rowB);
            Row rowB = selectionSheet.getRow(cellReference3.getRow());
            cellB = rowB.getCell(cellReference3.getCol());
        }
        if (rowC != 0) {
            CellReference cellReference4 = new CellReference(column + rowC);
            Row rowC = selectionSheet.getRow(cellReference4.getRow());
            cellC = rowC.getCell(cellReference4.getCol());
        }
        if (rowD != 0) {
            CellReference cellReference5 = new CellReference(column + rowD);
            Row rowD = selectionSheet.getRow(cellReference5.getRow());
            cellD = rowD.getCell(cellReference5.getCol());
        }
        if (rowE != 0) {
            CellReference cellReference6 = new CellReference(column + rowE);
            Row rowE = selectionSheet.getRow(cellReference6.getRow());
            cellE = rowE.getCell(cellReference6.getCol());
        }

        if (masterCell != null) {
            contentMasterCell = masterCell.getNumericCellValue();
            switch (evaluator.evaluateFormulaCell(masterCell)) {
                case Cell.CELL_TYPE_BOOLEAN:
                    System.out.println(masterCell.getBooleanCellValue());
                    break;
                case Cell.CELL_TYPE_NUMERIC:
                    System.out.println(masterCell.getNumericCellValue());
                    break;
                case Cell.CELL_TYPE_STRING:
                    System.out.println(masterCell.getStringCellValue());
                    break;
                case Cell.CELL_TYPE_BLANK:
                    break;
                case Cell.CELL_TYPE_ERROR:
                    System.out.println(masterCell.getErrorCellValue());
                    break;
                case Cell.CELL_TYPE_FORMULA:
                    break;
            }
        }
        if (cellA != null) {
            contentCellA = cellA.getNumericCellValue();
            switch (evaluator.evaluateFormulaCell(cellA)) {
                case Cell.CELL_TYPE_BOOLEAN:
                    System.out.println(cellA.getBooleanCellValue());
                    break;
                case Cell.CELL_TYPE_NUMERIC:
                    System.out.println(cellA.getNumericCellValue());
                    break;
                case Cell.CELL_TYPE_STRING:
                    System.out.println(cellA.getStringCellValue());
                    break;
                case Cell.CELL_TYPE_BLANK:
                    break;
                case Cell.CELL_TYPE_ERROR:
                    System.out.println(cellA.getErrorCellValue());
                    break;
                case Cell.CELL_TYPE_FORMULA:
                    break;
            }
        }
        if (cellB != null) {
            contentCellB = cellB.getNumericCellValue();
            switch (evaluator.evaluateFormulaCell(cellB)) {
                case Cell.CELL_TYPE_BOOLEAN:
                    System.out.println(cellB.getBooleanCellValue());
                    break;
                case Cell.CELL_TYPE_NUMERIC:
                    System.out.println(cellB.getNumericCellValue());
                    break;
                case Cell.CELL_TYPE_STRING:
                    System.out.println(cellB.getStringCellValue());
                    break;
                case Cell.CELL_TYPE_BLANK:
                    break;
                case Cell.CELL_TYPE_ERROR:
                    System.out.println(cellB.getErrorCellValue());
                    break;
                case Cell.CELL_TYPE_FORMULA:
                    break;
            }
        }
        if (cellC != null) {
            contentCellC = cellC.getNumericCellValue();
            switch (evaluator.evaluateFormulaCell(cellC)) {
                case Cell.CELL_TYPE_BOOLEAN:
                    System.out.println(cellC.getBooleanCellValue());
                    break;
                case Cell.CELL_TYPE_NUMERIC:
                    System.out.println(cellC.getNumericCellValue());
                    break;
                case Cell.CELL_TYPE_STRING:
                    System.out.println(cellC.getStringCellValue());
                    break;
                case Cell.CELL_TYPE_BLANK:
                    break;
                case Cell.CELL_TYPE_ERROR:
                    System.out.println(cellC.getErrorCellValue());
                    break;
                case Cell.CELL_TYPE_FORMULA:
                    break;
            }
        }
        if (cellD != null) {
            contentCellD = cellD.getNumericCellValue();
            switch (evaluator.evaluateFormulaCell(cellD)) {
                case Cell.CELL_TYPE_BOOLEAN:
                    System.out.println(cellD.getBooleanCellValue());
                    break;
                case Cell.CELL_TYPE_NUMERIC:
                    System.out.println(cellD.getNumericCellValue());
                    break;
                case Cell.CELL_TYPE_STRING:
                    System.out.println(cellD.getStringCellValue());
                    break;
                case Cell.CELL_TYPE_BLANK:
                    break;
                case Cell.CELL_TYPE_ERROR:
                    System.out.println(cellD.getErrorCellValue());
                    break;
                case Cell.CELL_TYPE_FORMULA:
                    break;
            }
        }
        if (cellE != null) {
            contentCellE = cellE.getNumericCellValue();
            switch (evaluator.evaluateFormulaCell(cellE)) {
                case Cell.CELL_TYPE_BOOLEAN:
                    System.out.println(cellE.getBooleanCellValue());
                    break;
                case Cell.CELL_TYPE_NUMERIC:
                    System.out.println(cellE.getNumericCellValue());
                    break;
                case Cell.CELL_TYPE_STRING:
                    System.out.println(cellE.getStringCellValue());
                    break;
                case Cell.CELL_TYPE_BLANK:
                    break;
                case Cell.CELL_TYPE_ERROR:
                    System.out.println(cellE.getErrorCellValue());
                    break;
                case Cell.CELL_TYPE_FORMULA:
                    break;
            }
        }
    }
    public double getContentMasterCell(){
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
}
