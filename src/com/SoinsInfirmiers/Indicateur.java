package SoinsInfirmiers;

import javafx.scene.control.Alert;

public class Indicateur {

    private int masterRow;
    private int rowA = 0;
    private int rowB = 0;
    private int rowC = 0;
    private int rowD = 0;
    private int rowE = 0;
    private int rowF = 0;
    private boolean withGraphic = false;
    boolean withFileD = false;
    private boolean withLineGraphic = true;

    public void toExcelRow(String indicateur) {
        switch (indicateur) {
            case "Total de jours payés":
                masterRow = 6;
                rowA = 7;
                rowB = 8;
                rowC = 9;
                withGraphic = true;
                break;
            case "Total de jours prestés Infirmières":
                masterRow = 11;
                rowA = 12;
                rowB = 13;
                withGraphic = true;
                break;
            case "% coupés":
                masterRow = 15;
                withGraphic = false;
                break;
            case "Solde récup. fin de mois":
                masterRow = 17;
                withGraphic = false;
                break;
            case "Nombre de visites":
                masterRow = 20;
                rowA = 21;
                rowB = 22;
                rowC = 23;
                rowD = 24;
                withGraphic = true;
                break;
            case "Nombre de soins":
                masterRow = 25;
                withGraphic = false;
                break;
            case "Nombre de soins par visite":
                masterRow = 26;
                withGraphic = false;
                break;
            case "Nbre vis/J. prestés avec soins":
                masterRow = 28;
                withGraphic = false;
                break;
            case "Nombre de visites NOM":
                masterRow = 30;
                withGraphic = false;
                break;
            case "Nombre de soins NOM":
                masterRow = 31;
                rowA = 32;
                rowB = 33;
                rowC = 34;
                rowD = 35;
                withGraphic = true;
                break;
            case "Nombre plafonds journ.":
                masterRow = 36;
                withGraphic = false;
                break;
            case "Nombre visites par forfait":
                masterRow = 1;
                rowA = 38;
                rowB = 39;
                rowC = 40;
                withGraphic = false;
                withLineGraphic = false;
                break;
            case "Visites par mutualité":
                masterRow = 19;
                rowA = 42;
                rowB = 43;
                rowC = 44;
                withGraphic = true;
                withLineGraphic = false;
                break;
            case "Total toilettes / total visites":  // TODO : Arriver à 100%
                masterRow = 1;
                rowA = 46;
                rowB = 47;
                withGraphic = false;
                break;
            case "Facturation totale":          // FACTURATION
                masterRow = 51;
                rowA = 52;
                rowB = 55;
                rowC = 56;
                withGraphic = true;
                break;
            case "Tarification OA":
                masterRow = 52;
                rowA = 53;
                rowB = 54;
                withGraphic = true;
                break;
            case "Réintroductions":
                masterRow = 57;
                withGraphic = false;
                break;
            case "Facturation par Visite":
                masterRow = 59;
                rowA = 59;
                rowB = 60;
                rowC = 61;
                rowD = 62;
                rowE = 63;
                withGraphic = false;
                withLineGraphic = true;
                break;
            case "Facturation OA / jours prestés avec soins":
                masterRow = 65;
                withGraphic = false;
                break;
            case "Facturation totale / jours prestés avec soins":
                masterRow = 67;
                withGraphic = false;
                break;
            case "Facturation totale par jours payés":
                masterRow = 68;
                withGraphic = false;
                break;
            case "Recette OA":
                masterRow = 60;
                rowA = 61;
                rowB = 62;
                rowC = 63;
                rowD = 64;
                withGraphic = false;
                withFileD = true;
                withLineGraphic = true;
                break;
            case "Nombre de patients NOM/FF":         // PATIENTS
                masterRow = 73;
                rowA = 74;
                rowB = 75;
                withGraphic = true;
                break;
            case "Nombre de patients VIPO":
                masterRow = 73;
                rowA = 76;
                rowB = 77;
                withGraphic = true;
                break;
            case "Taux de rotation":
                masterRow = 78;
                withGraphic = true;
                break;
            case "FF palliatifs":
                masterRow = 1;
                rowA = 79;
                rowB = 80;
                withGraphic = false;
                withLineGraphic = false;
                break;
            case "Patients par mutualité":
                masterRow = 1;
                rowA = 82;
                rowB = 83;
                rowC = 84;
                withGraphic = true;
                withLineGraphic = false;
                break;
            case "Kilomètres parcourus & par visite":       // Déplacements
                masterRow = 66;
                rowA = 66;
                rowB = 67;
                withFileD = true;
                break;

            case "Total de jours payés en %":           // Détail en %
                masterRow = 93;
                rowA = 94;
                rowB = 95;
                rowC = 96;
                rowD = 97;
                rowE = 98;
                withGraphic = true;
                withLineGraphic = false;
                break;
            case "Total presté":
                masterRow = 100;
                rowA = 101;
                rowB = 102;
                rowC = 103;
                rowD = 104;
                withGraphic = true;
                withLineGraphic = false;
                break;
            case "Total absence non-payées":
                masterRow = 106;
                rowA = 107;
                rowB = 108;
                rowC = 109;
                rowD = 110;
                rowE = 111;
                withGraphic = true;
                withLineGraphic = false;
                break;
            case "Répartition des blocs":
                masterRow = 113;
                rowA = 114;
                rowB = 115;
                rowC = 116;
                withGraphic = true;
                withLineGraphic = false;
                break;
            case "Suppléments en jours":
                masterRow = 118;
                rowA = 119;
                rowB = 120;
                rowC = 121;
                rowD = 122;
                rowE = 123;
                withGraphic = true;
                withLineGraphic = false;
                break;
        }
    }

    public void showEmptyDialog(){
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("NullPointerException");
        alert.setHeaderText("Veuillez choisir un indicateur");
        alert.setContentText("avoid NullPointerException");
        alert.showAndWait();
    }

    public void resetVariables(){
        masterRow = 0;
        rowA = 0;
        rowB = 0;
        rowC = 0;
        rowD = 0;
        rowE = 0;
        rowF = 0;
        withFileD = false;
        withLineGraphic = true;
    }

    int getMasterRow() {
        return masterRow;
    }

    int getRowA() {
        return rowA;
    }

    int getRowB() {
        return rowB;
    }

    int getRowC() {
        return rowC;
    }

    int getRowD() {
        return rowD;
    }

    int getRowE() {
        return rowE;
    }

    int getRowF() {
        return rowF;
    }

    boolean getWithGraphic() {
        return withGraphic;
    }

    boolean getWithFileD() {
        return withFileD;
    }

    boolean getwithLineGraphic(){
        return withLineGraphic;
    }

}
