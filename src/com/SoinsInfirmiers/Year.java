package SoinsInfirmiers;


import javafx.scene.control.Alert;

class Year {

    private String fileA;
    private String fileB;
    private String fileC;
    private  String fileD;

    void toPath(int year) {
        switch (year) {
            case 2017:
                fileA = "Namur 2017.xls";
                fileB = "Données Namur 2017.xls";
                fileC = "Suivi pers. CJB Namur 2017.xls";
                fileD = "Indicateurs 2017.xls";
                break;
            case 2016:
                fileA = "Namur 2016 new.xls";
                fileB = "Données Namur 2016 new.xls";
                fileC = "Suivi pers. CJB Namur 2016 new.xls";
                fileD = "Indicateurs 2016.xls";
                break;
            case 2015:
                fileA = "Namur 2015 new.xls";
                fileB = "Données Namur 2015 new.xls";
                fileC = "Suivi pers. CJB Namur 2015 new.xls";
                fileD = "Indicateurs 2015.xls";
                break;
            case 2018:
                fileA = "Namur 2018.xls";
                fileB = "Données Namur 2018.xls";
                fileC = "Suivi pers. CJB Namur 2018.xls";
                fileD = "Indicateurs 2018.xls";
                break;
            case 2019:
                fileA = "Namur 2019.xls";
                fileB = "Données Namur 2019.xls";
                fileC = "Suivi pers. CJB Namur 2019.xls";
                fileD = "Indicateurs 2019.xls";
                break;
            case 2020:
                fileA = "Namur 2020.xls";
                fileB = "Données Namur 2020.xls";
                fileC = "Suivi pers. CJB Namur 2020.xls";
                fileD = "Indicateurs 2020.xls";
                break;
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
        return "P:/PROVINCE et statistiques FASD/";
    }


}
