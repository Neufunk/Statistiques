package com.SoinsInfirmiers;


import javafx.scene.control.Alert;

public class Centre {

    private int sheet;

    public void toExcelSheet(String centre) {
        switch (centre) {
            case "Global":
                sheet = 5;
                break;
            case "Namur":
                sheet = 4;
                break;
            case "Philippeville":
                sheet = 0;
                break;
            case "Ciney":
                sheet = 1;
                break;
            case "Gedinne":
                sheet = 2;
                break;
            case "Eghezée":
                sheet = 3;
                break;
            default:
                System.out.println("Le centre n'existe pas");
                break;
        }
    }

    public void showEmptyDialog(){
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("NullPointerException");
        alert.setHeaderText("Veuillez sélectionner un centre");
        alert.setContentText("avoid NullPointerException");
        alert.showAndWait();
    }

    public int getSheet() {
        return sheet;
    }

}
