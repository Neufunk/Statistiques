package com.SoinsInfirmiers;


public class Periode {

    String column;

    public void toExcelColumn(String periode){

        if (periode == "Janvier") {
            column = "D";
        } else if (periode == "Février") {
            column = "E";
        } else if (periode == "Mars") {
            column = "F";
        } else if (periode == "Avril") {
            column = "G";
        } else if (periode == "Mai") {
            column = "H";
        } else if (periode == "Juin") {
            column = "I";
        } else if (periode == "Juillet") {
            column = "J";
        } else if (periode == "Août") {
            column = "K";
        } else if (periode == "Septembre") {
            column = "L";
        } else if (periode == "Octobre") {
            column = "M";
        } else if (periode == "Novembre") {
            column = "N";
        } else if (periode == "Décembre") {
            column = "O";
        } else if (periode == "Année Complète") {
            column = "P";
        } else {
            System.out.println("Erreur dans la période selectionnée");
        }
    }
    public String getColumn(){
        return column;
    }

}
