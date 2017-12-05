package AVJ;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.apache.poi.hssf.util.CellReference;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.util.ZipSecureFile;
import org.apache.poi.ss.usermodel.*;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.util.Calendar;

class IteratorExcel extends ControllerContingent {

    private Database database = new Database();
    private Workbook workbook;
    private int j = 0;
    private int k = 0;
    private int sectorCount = 0;
    private static ObservableList<String> nonUpdated = FXCollections.observableArrayList();

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

    private double[] cellResult = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33,
            34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, 62, 63, 64, 65, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80,
            81, 82, 83, 84, 85, 86, 87, 89, 89, 90, 91, 92, 93, 94, 95, 96, 900, 901, 902, 903, 904, 905, 906, 907};

    private String[] indicateurs = {"Total Heures dispo par mois (Base 40)", "Total Heures dispo par mois (Base 38)",
            "Nbre H Absentéisme (code M) (Base 40)", "Nbre H Absentéisme (code M) (Base 38)",
            "Nbre H Prestées (code PR) (Base 40)", "Nbre H Prestées (code PR) (Base 38)",
            "Ecart H Dispo et H prestées (Base 40)", "Ecart H Dispo et H prestées (Base 38)"};

    private String[] mois = {"Janvier", "Février", "Mars", "Avril", "Mai", "Juin", "Juillet", "Août",
            "Septembre", "Octobre", "Novembre", "Décembre", "Total"};

    void startIteration(String path, String year, String firstName, String secteur, Connection connection) {
        progress += 1;
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
        secteurLabel = fileName + secteur;
        textField.setText(secteurLabel);
        try {
            ZipSecureFile.setMinInflateRatio(0); // Ratio pour éviter le blocage des zip-bombs
            workbook = WorkbookFactory.create(new File(path + year + "\\" + firstName + "\\" + fileName + secteur + ".xlsm"));
            Sheet selectionSheet = workbook.getSheet("Contingent");
            for (int i = 0; i < cellTab.length; i++) {
                CellReference cellReference = new CellReference(cellTab[i]);
                Row row = selectionSheet.getRow(cellReference.getRow());
                Cell cell = row.getCell(cellReference.getCol());
                double result = cell.getNumericCellValue();
                cellResult[i] = result;
            }
            for (double aCellResult : cellResult) {         // = for (int i = 0; i < cellResult.length; i++)
                if (j >= 8) {     // pour les 8 indicateurs
                    j = 0;
                    k++;
                }
                if (k >= 13) {    // pour les 13 périodes
                    k = 0;
                }
                database.updateContingent(indicateurs[j], aCellResult, getCurrentYear(), mois[k], secteur, connection);
                System.out.println("#" + sectorCount + ". " + fileName + secteur + " - " + mois[k] + " - " + indicateurs[j] + " : " + aCellResult);
                j++;
            }
            progress += 2;
            sectorCount++;
            addShell("#" + sectorCount + "/22 - " + fileName + secteur + " - MIS À JOUR");
        } catch (IOException | InvalidFormatException e) {
            sectorCount++;
            progress += 2;
            addShell("!!! - #" + sectorCount + "/22 - " + fileName + secteur + " - NON MIS À JOUR >> " + e.getMessage());
            e.printStackTrace();
            nonUpdated.add(fileName + secteur);
        }
        closeWorkbook();
    }

    private int getCurrentYear() {
        Calendar now = Calendar.getInstance();
        return now.get(Calendar.YEAR);
    }

    private void closeWorkbook() {
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
