package si;

import javafx.scene.control.Alert;

class Periode {

    private String column;

    void toExcelColumn(String periode){
        switch (periode) {
            case "Janvier":
                column = "D";
                break;
            case "Février":
                column = "E";
                break;
            case "Mars":
                column = "F";
                break;
            case "Avril":
                column = "G";
                break;
            case "Mai":
                column = "H";
                break;
            case "Juin":
                column = "I";
                break;
            case "Juillet":
                column = "J";
                break;
            case "Août":
                column = "K";
                break;
            case "Septembre":
                column = "L";
                break;
            case "Octobre":
                column = "M";
                break;
            case "Novembre":
                column = "N";
                break;
            case "Décembre":
                column = "O";
                break;
            case "Année Complète":
                column = "P";
                break;
            default:
                System.out.println("Erreur dans la période selectionnée");
                break;
        }
    }

    void toExcelColumnFileB(String periode){
        switch (periode) {
            case "Janvier":
                column = "E";
                break;
            case "Février":
                column = "F";
                break;
            case "Mars":
                column = "G";
                break;
            case "Avril":
                column = "H";
                break;
            case "Mai":
                column = "I";
                break;
            case "Juin":
                column = "J";
                break;
            case "Juillet":
                column = "K";
                break;
            case "Août":
                column = "L";
                break;
            case "Septembre":
                column = "M";
                break;
            case "Octobre":
                column = "N";
                break;
            case "Novembre":
                column = "O";
                break;
            case "Décembre":
                column = "P";
                break;
            case "Année Complète":
                column = "Q";
                break;
            default:
                System.out.println("Erreur dans la période selectionnée");
                break;
        }
    }

    void showEmptyDialog(){
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("NullPointerException");
        alert.setHeaderText("Veuillez sélectionner une période");
        alert.setContentText("avoid NullPointerException");
        alert.showAndWait();
    }

    String getColumn(){
        return column;
    }

}
