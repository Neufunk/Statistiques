package com.SoinsInfirmiers;


public class CentreExcel {

    private int sheet;

    public void toSheet(String centre){
        if (centre == "Global") {
            sheet = 5;
        } else if (centre == "Namur") {
            sheet = 4;
        } else if (centre == "Philippeville") {
            sheet = 0;
        } else if (centre == "Ciney") {
            sheet = 1;
        } else if (centre == "Gedinne") {
            sheet = 2;
        } else if (centre == "Eghezée") {
            sheet = 3;
        } else {
            System.out.println("Le centre n'existe pas");
        }
    }
    public int getSheet(){
        return sheet;
    }
}
