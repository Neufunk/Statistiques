package AVJ;

import main.Version;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Data {

    public static String homePageTitle = "Aide & Soins à Domicile - Statistiques - v"+ Version.versionNumber;
    public static String pageTitle0 = "Aide à la Vie Journalière - Contingent - v"+Version.versionNumber;
    public static String asdbTitle = "ASDB Engine";

    ObservableList<String> centerList = FXCollections
            .observableArrayList("ASD", "Namur", "Philippeville");

    ObservableList<String> periode = FXCollections
            .observableArrayList( "Année Complète", "Janvier", "Février", "Mars", "Avril", "Mai", "Juin", "Juillet", "Août",
                    "Septembre", "Octobre", "Novembre", "Décembre");

    public ObservableList<String> queryList = FXCollections
            .observableArrayList("SELECT", "UPDATE", "INSERT", "DELETE");

}
