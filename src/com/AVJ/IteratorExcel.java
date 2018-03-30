package AVJ;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.apache.poi.hssf.util.CellReference;
import org.apache.poi.openxml4j.util.ZipSecureFile;
import org.apache.poi.ss.usermodel.*;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.util.Arrays;
import java.util.Calendar;

class IteratorExcel extends ControllerContingent {

    private Database database = new Database();
    private static ObservableList<String> nonUpdated = FXCollections.observableArrayList();
    private int sectorCount = 0;

    private String[] cellTab = {"C21", "C22", "C23", "C24", "C25", "C26", "C27", "C28",
            "D21", "D22", "D23", "D24", "D25", "D26", "D27", "D28",
            "E21", "E22", "E23", "E24", "E25", "E26", "E27", "E28",
            "F21", "F22", "F23", "F24", "F25", "F26", "F27", "F28",
            "G21", "G22", "G23", "G24", "G25", "G26", "G27", "G28",
            "H21", "H22", "H23", "H24", "H25", "H26", "H27", "H28",
            "I21", "I22", "I23", "I24", "I25", "I26", "I27", "I28",
            "J21", "J22", "J23", "J24", "J25", "J26", "J27", "J28",
            "K21", "K22", "K23", "K24", "K25", "K26", "K27", "K28",
            "L21", "L22", "L23", "L24", "L25", "L26", "L27", "L28",
            "M21", "M22", "M23", "M24", "M25", "M26", "M27", "M28",
            "N21", "N22", "N23", "N24", "N25", "N26", "N27", "N28",
            "O21", "O22", "O23", "O24", "O25", "O26", "O27", "O28"};

    private double[] cellResult = new double[104];

    private String[] indicateurs = {"Total Heures dispo par mois (Base 40)", "Total Heures dispo par mois (Base 38)",
            "Nbre H Absentéisme (code M) (Base 40)", "Nbre H Absentéisme (code M) (Base 38)",
            "Nbre H Prestées (code PR) (Base 40)", "Nbre H Prestées (code PR) (Base 38)",
            "Ecart H Dispo et H prestées (Base 40)", "Ecart H Dispo et H prestées (Base 38)"};

    private String[] mois = {"Janvier", "Février", "Mars", "Avril", "Mai", "Juin", "Juillet", "Août",
            "Septembre", "Octobre", "Novembre", "Décembre", "Total"};

    void startIteration(String path, String year, String firstName, String secteur, Connection connection) {
        String fileName = setFileName(secteur);
        secteurLabel = fileName + secteur;
        textField.setText(secteurLabel);
        ZipSecureFile.setMinInflateRatio(0); // Ratio pour éviter le blocage des zip-bombs
        try {
            iterateContingent(path, year, firstName, secteur, fileName, connection);
            iteratePotDepartConges(path, year, firstName, secteur, fileName, connection);
            iterateCongesPris(path, year, firstName, secteur, fileName, connection);
            iterateSoldeHeuresRecup(path, year, firstName, secteur, fileName, connection);
            sectorCount++;
            addShell("#" + sectorCount + "/21 - " + fileName + secteur + " - MIS À JOUR");
            progress += 1;
        } catch (Exception e) {
            progress+=2;
            sectorCount++;
            addShell("!!! - #" + sectorCount + "/21 - " + fileName + secteur + " - NON MIS À JOUR >> " + e.getMessage());
            e.printStackTrace();
            nonUpdated.add(fileName + secteur);
        }
    }

    private void iterateContingent(String path, String year, String firstName, String secteur, String fileName, Connection connection) throws Exception {
        int indicateursCount = 0;
        int periodeCount = 0;
        Workbook workbook = WorkbookFactory.create(new File(path + year + "\\" + firstName + "\\" + fileName + secteur + ".xlsm"));
        Sheet selectionSheet = workbook.getSheet("Contingent");
        for (int i = 0; i < cellTab.length; i++) {
            CellReference cellReference = new CellReference(cellTab[i]);
            Row row = selectionSheet.getRow(cellReference.getRow());
            Cell cell = row.getCell(cellReference.getCol());
            double result = cell.getNumericCellValue();
            cellResult[i] = result;
        }
        for (double aCellResult : cellResult) {
            if (indicateursCount >= 8) {
                indicateursCount = 0;
                periodeCount++;
            }
            if (periodeCount >= 13) {
                periodeCount = 0;
            }
            database.updateContingent(indicateurs[indicateursCount], aCellResult, getCurrentYear(), mois[periodeCount], secteur, connection);
            System.out.println("#" + sectorCount + ". " + fileName + secteur + " - " + mois[periodeCount] + " - " + indicateurs[indicateursCount] + " : " + aCellResult);
            indicateursCount++;
        }
        closeWorkbook(workbook);
    }

    private void iteratePotDepartConges(String path, String year, String firstName, String secteur, String fileName, Connection connection) throws Exception {
        String[] columnArr = {"E", "F", "G", "H"};
        int rowStart = 8;
        int rowEnd = 37;
        double result = 0;
        Workbook workbook = WorkbookFactory.create(new File(path + year + "\\" + firstName + "\\" + fileName + secteur + ".xlsm"));
        Sheet selectionSheet = workbook.getSheet("Compteurs");
        for (String column : columnArr) {
            for (int i = rowStart; i <= rowEnd; i++) {
                String line = String.valueOf(i);
                CellReference cellReference = new CellReference(column + line);
                Row row = selectionSheet.getRow(cellReference.getRow());
                Cell cell = row.getCell(cellReference.getCol());
                result += cell.getNumericCellValue();
            }
            database.updatePotDepartConges(connection, getCurrentYear(), secteur, result);
            progress+=1;
        }
        System.out.println("#" + sectorCount + ". " + fileName + secteur + " - Pot Départ Congés : " + result);
        closeWorkbook(workbook);
    }

    private void iterateCongesPris(String path, String year, String firstName, String secteur, String fileName, Connection connection) throws Exception {
        String[] columnArr = {"Q", "R", "S", "T"};
        int[] rowArr = {45, 74};
        double result = 0;
        Workbook workbook = WorkbookFactory.create(new File(path + year + "\\" + firstName + "\\" + fileName + secteur + ".xlsm"));
        Sheet selectionSheet = workbook.getSheet("Compteurs");
        for (int i = 0; i <= 11; i++) {
            for (String column : columnArr) {
                for (int line = rowArr[0]; line <= rowArr[1]; line++) {
                    CellReference cellReference = new CellReference(column + line);
                    Row row = selectionSheet.getRow(cellReference.getRow());
                    Cell cell = row.getCell(cellReference.getCol());
                    result += cell.getNumericCellValue();
                }
            }
            System.out.println("#" + sectorCount + ". " + fileName + secteur + " - " + Arrays.copyOf(mois, mois.length-1)[i] + " - " + "Conges Pris : " + result);
            database.updateCongesPris(connection, getCurrentYear(), Arrays.copyOf(mois, mois.length-1)[i], secteur, result );
            result = 0;
            if (rowArr[1] < 437) {
                rowArr[0] += 33;
                rowArr[1] += 33;
            }
        }
        closeWorkbook(workbook);
    }

    private void iterateSoldeHeuresRecup(String path, String year, String firstName, String secteur, String fileName, Connection connection) throws Exception {
        String column = "AE";
        int[] rowArr = {408, 437};
        double result = 0;
        Workbook workbook = WorkbookFactory.create(new File(path + year + "\\" + firstName + "\\" + fileName + secteur + ".xlsm"));
        Sheet selectionSheet = workbook.getSheet("Compteurs");
        for(int i = rowArr[0]; i <= rowArr[1]; i++){
            String line = String.valueOf(i);
            CellReference cellReference = new CellReference(column+line);
            Row row = selectionSheet.getRow(cellReference.getRow());
            Cell cell = row.getCell(cellReference.getCol());
            result += cell.getNumericCellValue();
        }
        System.out.println("#" + sectorCount + ". " + fileName + secteur + " - Soldes heures récup : " + result);
        database.updateSoldeHeuresRecup(connection, getCurrentYear(), secteur, result);
        System.out.println("---------------------------------------------------------------\n");
        closeWorkbook(workbook);
    }

    private String setFileName(String secteur) {
        String fileName;
        switch (secteur) {
            case "Andenne":
                fileName = "Secteur d'";
                break;
            case "Eghezée":
                fileName = "Secteur d'";
                break;
            case "Volantes Namur":
                fileName = "Secteur ";
                break;
            case "Volantes Philippeville":
                fileName = "Secteur des ";
                break;
            default:
                fileName = "Secteur de ";
        }
        return fileName;
    }

    private int getCurrentYear() {
        Calendar now = Calendar.getInstance();
        return now.get(Calendar.YEAR);
    }

    private void closeWorkbook(Workbook workbook) {
        try {
            workbook.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static String getNonUpdated() {
        return nonUpdated.toString();
    }

}
