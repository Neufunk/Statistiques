package AVJ;

import main.Version;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Data {

    public static final String homePageTitle = "Aide & Soins à Domicile - Statistiques - v"+ Version.versionNumber;
    public static final String pageTitle0 = "Aide à la Vie Journalière - Contingent - v"+Version.versionNumber;
    public static final String pageTitle1 = "Aide à la Vie Journalière - Sélection - v"+Version.versionNumber;
    public static final String asdbTitle = "ASDB Engine";

    final ObservableList<String> centerList = FXCollections
            .observableArrayList("ASD", "Namur", "Philippeville");

    final ObservableList<String> periode = FXCollections
            .observableArrayList( "Année Complète", "Janvier", "Février", "Mars", "Avril", "Mai", "Juin", "Juillet", "Août",
                    "Septembre", "Octobre", "Novembre", "Décembre");

    public final ObservableList<String> queryList = FXCollections
            .observableArrayList("SELECT", "UPDATE", "INSERT", "DELETE");

}
