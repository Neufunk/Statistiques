package com;

public class Year {

    private int year;
    public String filePathA;
    public String filePathB;
    public String filePathC;
    public String fileNameA;
    public String fileNameB;
    public String fileNameC;

    public Year (){
        year = 0;
    }
    public Year (int year){
        this.year = year;
    }

    public int getYear(){
        return year;
    }

    public void setYear(int year){
        this.year = year;
    }

    public void toPath(){
        if (year == 2017){
            filePathA = "P:/PROVINCE et statistiques FASD/";
            fileNameA = "Namur 2017.xls";
            filePathB = "P:/PROVINCE et statistiques FASD/";
            fileNameB = "Données Namur 2017.xls";
            filePathC = "P:/PROVINCE et statistiques FASD/";
            fileNameC = "Suivi pers. CJB Namur 2017.xls";
        }
    }

    public String getFilePathA(){
        return filePathA;
    }
    public String getFileNameA(){
        return fileNameA;
    }
}
