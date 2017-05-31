package com.SoinsInfirmiers;


public class Year {

    private Object year;
    private String fileA;
    private String fileB;
    private String fileC;
    private  String fileD;
    private String path;

    public void toPath(int year) {
        if (year == 2017) {
            fileA = "Namur 2017.xls";
            fileB = "Données Namur 2017.xls";
            fileC = "Suivi pers. CJB Namur 2017.xls";
            fileD = "Indicateurs 2017.xls";
            path = "P:/PROVINCE et statistiques FASD/";
        } else if (year == 2016) {
            fileA = "Namur 2016 new.xls";
            fileB = "Données Namur 2016 new.xls";
            fileC = "Suivi pers. CJB Namur 2016 new.xls";
            fileD = "Indicateurs 2016.xls";
            path = "P:/PROVINCE et statistiques FASD/";
        } else if (year == 2015) {
            fileA = "Namur 2015 new.xls";
            fileB = "Données Namur 2015 new.xls";
            fileC = "Suivi pers. CJB Namur 2015 new.xls";
            fileD = "Indicateurs 2015.xls";
            path = "P:/PROVINCE et statistiques FASD/";
        }
    }

    public String getFileA() {
        return fileA;
    }

    public String getFileD() {
        return fileD;
    }

    public String getFileB() {
        return fileB;
    }

    public String getFileC() {
        return fileC;
    }

    public String getPath() {
        return path;
    }

}
