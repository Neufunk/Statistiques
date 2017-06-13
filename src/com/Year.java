package com;


import javafx.scene.control.Alert;

class Year {

    private String fileA;
    private String fileB;
    private String fileC;
    private  String fileD;
    private String path;

    void toPath(int year) {
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
        } else if (year == 1337) {
            fileA = "debug.xls";
            fileB = "Données Namur 2016 new.xls";
            fileC = "Suivi pers. CJB Namur 2016 new.xls";
            fileD = "Indicateurs 2016.xls";
            path = "E:/Developpement/IntelliJ/";
        } else if (year == 2018) {
            fileA = "Namur 2018 new.xls";
            fileB = "Données Namur 2018 new.xls";
            fileC = "Suivi pers. CJB Namur 2018 new.xls";
            fileD = "Indicateurs 2018.xls";
            path = "P:/PROVINCE et statistiques FASD/";
        } else if (year == 2019) {
            fileA = "Namur 2019 new.xls";
            fileB = "Données Namur 2019 new.xls";
            fileC = "Suivi pers. CJB Namur 2019 new.xls";
            fileD = "Indicateurs 2019.xls";
            path = "P:/PROVINCE et statistiques FASD/";
        } else if (year == 2020) {
            fileA = "Namur 2020 new.xls";
            fileB = "Données Namur 2020 new.xls";
            fileC = "Suivi pers. CJB Namur 2020 new.xls";
            fileD = "Indicateurs 2020.xls";
            path = "P:/PROVINCE et statistiques FASD/";
        }
    }

    void showEmptyDialog(){
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Champ requis");
        alert.setHeaderText("Veuillez sélectionner une année");
        alert.setContentText("avoid NullPointerException");
        alert.showAndWait();
    }

    String getFileA() {
        return fileA;
    }

    String getFileD() {
        return fileD;
    }

    String getFileB() {
        return fileB;
    }

    String getFileC() {
        return fileC;
    }

    String getPath() {
        return path;
    }

}
