package AVJ;

import com.Version;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Data {

    public static String homePageTitle = "Aide & Soins à Domicile - Statistiques // beta "+ Version.versionNumber;
    public static String pageTitle0 = "Aide à la Vie Journalière - Contingent // beta "+Version.versionNumber;
    public static String asdbTitle = "ASDB Engine";

    public ObservableList<String> centerList = FXCollections
            .observableArrayList("ASD", "Namur", "Philippeville");

    public ObservableList<String> periode = FXCollections
            .observableArrayList( "Année Complète", "Janvier", "Février", "Mars", "Avril", "Mai", "Juin", "Juillet", "Août",
                    "Septembre", "Octobre", "Novembre", "Décembre");

    public ObservableList<String> queryList = FXCollections
            .observableArrayList("SELECT", "UPDATE", "INSERT", "DELETE");

}
