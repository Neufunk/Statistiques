package SoinsInfirmiers;

import javafx.collections.ObservableList;

class Category {
    private ObservableList list;
    private Strings strings = new Strings();

     void setCategorie(String selectedCategorie){
         switch (selectedCategorie) {
             case "SUIVI DU PERSONNEL INFIRMIER":
                 list = strings.indicListSuiviDuPersonnel;
                 break;
             case "SOINS ET VISITES":
                 list = strings.indicListSoinsEtVisites;
                 break;
             case "FACTURATION":
                 list = strings.indicListFacturation;
                 break;
             case "PATIENTS":
                 list = strings.indicListPatients;
                 break;
             case "DÉPLACEMENTS":
                 list = strings.indicListDeplacements;
                 break;
             case "SUIVI DU PERSONNEL INFIRMIER - DÉTAILS EN %":
                 list = strings.indicListSuiviDuPersonnelPourcents;
                 break;
             case "SUIVI DU PERSONNEL INFIRMIER - DÉTAILS PAR HEURE":
                 list = strings.emptyList;
                 break;
             default:
                 list = strings.emptyList;
                 break;
         }
    }

    ObservableList getCategorie(){
        return list;
    }
}
