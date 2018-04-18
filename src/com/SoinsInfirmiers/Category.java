package SoinsInfirmiers;

import javafx.collections.ObservableList;

class Category {
    private ObservableList<String> list;
    private final Data data = new Data();

     void setCategorie(String selectedCategorie) {
         switch (selectedCategorie) {
             case "SUIVI DU PERSONNEL INFIRMIER":
                 list = data.indicListSuiviDuPersonnel;
                 break;
             case "SOINS ET VISITES":
                 list = data.indicListSoinsEtVisites;
                 break;
             case "FACTURATION":
                 list = data.indicListFacturation;
                 break;
             case "PATIENTS":
                 list = data.indicListPatients;
                 break;
             case "DÉPLACEMENTS":
                 list = data.indicListDeplacements;
                 break;
             case "SUIVI DU PERSONNEL INFIRMIER - DÉTAILS EN %":
                 list = data.indicListSuiviDuPersonnelPourcents;
                 break;
             case "SUIVI DU PERSONNEL INFIRMIER - DÉTAILS PAR HEURE":
                 list = data.emptyList;
                 break;
             default:
                 list = data.emptyList;
                 break;
         }
    }

    ObservableList getCategorie(){
        return list;
    }
}
