package com.SoinsInfirmiers;


public class YearExcel {

    private int year;
    private String fileA;
    private String fileB;
    private String fileC;
    private String path;

    public void toPath(int year){
        this.year = year;
        if (year == 2017){
            fileA = "Namur 2017.xls";
            fileB = "Données Namur 2017.xls";
            fileC = "Suivi pers. CJB Namur 2017.xls";
            path = "P:/PROVINCE et statistiques FASD/";
        }
    }

    public String getFileA(){
        return fileA;
    }
    public String getFileB(){
        return fileB;
    }
    public String getFileC(){
        return fileC;
    }
    public String getPath(){
        return path;
    }

}
