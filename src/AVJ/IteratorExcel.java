package AVJ;

import org.apache.poi.hssf.util.CellReference;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFFormulaEvaluator;

import java.io.File;
import java.io.IOException;

public class IteratorExcel {

    Workbook workbook;

    public void startIteration(){
        try {
            workbook = WorkbookFactory.create(new File("P:\\SERVICE SOCIAL - SERVICE DU PERSONNEL\\Tableaux mensuels\\2017\\Stéphanie\\Secteur de Saint-Servais.xlsm"));
            Sheet selectionSheet = workbook.getSheet("Contingent");

            CellReference cellReference = new CellReference("C21");
            Row masterRowB = selectionSheet.getRow(cellReference.getRow());
            Cell rawCellA = masterRowB.getCell(cellReference.getCol());
            double cellA = rawCellA.getNumericCellValue();
            System.out.println(cellA);

        } catch (InvalidFormatException | IOException e) {
            e.printStackTrace();
        }


    }
}
