package AVJ;

import com.Version;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Data {

    public static String homePageTitle = "Aide & Soins à Domicile - Statistiques // beta "+ Version.versionNumber;
    public static String pageTitle0 = "Aide à la Vie Journalière - Contingent // beta "+Version.versionNumber;
    public static String pageTitle1 = "Modifier le secteur d'un Travailleur Social";

    public ObservableList<String> centerList = FXCollections
            .observableArrayList("ASD", "Namur", "Philippeville");

    public ObservableList<String> secteursASD = FXCollections
            .observableArrayList("Province");

    public ObservableList<String> secteursNamur = FXCollections
            .observableArrayList("AM APE", "AM Jambes", "Andenne", "Bois de Villers", "Bouge", "Eghezée",
                    "Fosses-Mettet", "Gembloux", "Gesves",  "Jambes", "Jambes extérieur", "Namur",  "Namur Gare",
                    "Saint-Servais", "Salzinnes", "Sambreville", "Volantes Namur");

    public ObservableList<String> secteursPhilippeville = FXCollections
            .observableArrayList( "AM APE Philippeville",  "AM Philippeville", "Cerfontaine", "Couvin",
                    "Florennes", "Philippeville", "Viroinval",  "Volantes",  "Walcourt");

    public ObservableList<String> secteursAll = FXCollections
            .observableArrayList("AM APE", "AM APE Philippeville", "AM Jambes",   "AM Philippeville",
                    "Andenne", "Bois de Villers", "Bouge",  "Cerfontaine",  "Couvin", "Eghezée",  "Florennes",
                    "Fosses-Mettet", "Gembloux", "Gesves",  "Jambes", "Jambes extérieur", "Namur",  "Namur Gare",
                    "Philippeville", "Saint-Servais", "Salzinnes", "Sambreville", "Viroinval",  "Volantes",
                    "Volantes Namur",  "Walcourt");

}
