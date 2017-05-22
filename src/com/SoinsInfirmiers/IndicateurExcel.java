package com.SoinsInfirmiers;


public class IndicateurExcel {

    private int masterRow;
    private int rowA = 0;
    private int rowB = 0;
    private int rowC = 0;
    private int rowD = 0;
    private int rowE = 0;
    private int rowF = 0;
    boolean withGraphic = false;

    public void toRow(String indicateur) {
        switch (indicateur) {
            case "Total jours payés":
                masterRow = 6;
                rowA = 7;
                rowB = 8;
                rowC = 9;
                withGraphic = true;
                break;
            case "Total jours prestés Infirmières":
                masterRow = 11;
                rowA = 12;
                rowB = 13;
                withGraphic = true;
                break;
        }
    }
    public int getMasterRow(){
        return masterRow;
    }
    public int getRowA(){
        return rowA;
    }
    public int getRowB(){
        return rowB;
    }
    public int getRowC(){
        return rowC;
    }
    public int getRowD(){
        return rowD;
    }
    public int getRowE(){
        return rowE;
    }
    public int getRowF(){
        return rowF;
    }
    public boolean getGraphic(){
        return withGraphic;
    }

}
