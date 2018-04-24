package SoinsInfirmiers;

import main.Version;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Data {
    // Page titles
    public static final String homePageTitle = "Aide & Soins à Domicile - Statistiques - v"+ Version.versionNumber;
    public static final String pageTitle1 = "Soins Infirmiers - Comparaison par Années - v"+Version.versionNumber;
    public static final String pageTitle0 = "Soins Infirmiers - Indicateurs annuels - v"+Version.versionNumber;
    public static final String pageTitle2 = "Soins Infirmiers - Sélection - v"+Version.versionNumber;
    public static final String pageTitle3 = "Soins Infirmiers - Comparaison par Centres - v"+Version.versionNumber;

    // Centres
    final ObservableList<String> centerList = FXCollections
            .observableArrayList("Global", "Namur", "Eghezée", "Ciney", "Philippeville", "Gedinne");

    // Années
    final ObservableList<Integer> yearList = FXCollections
            .observableArrayList(2015, 2016, 2017, 2018, 2019, 2020);

    // Titres des Indicateurs
    final ObservableList<String> categorieList = FXCollections
            .observableArrayList("SUIVI DU PERSONNEL INFIRMIER", "SOINS ET VISITES", "FACTURATION", "PATIENTS",
                    "DÉPLACEMENTS", "SUIVI DU PERSONNEL INFIRMIER - DÉTAILS EN %");

    // Indicateurs
    final ObservableList<String> emptyList = FXCollections.observableArrayList("");
    final ObservableList<String> indicListSuiviDuPersonnel = FXCollections
            .observableArrayList("Total de jours payés", "Total de jours prestés Infirmières", "% coupés",
                    "Solde récup. fin de mois");
    final ObservableList<String> indicListSoinsEtVisites = FXCollections
            .observableArrayList("Nombre de visites", "Nombre de soins", "Nombre de soins par visite",
                    "Nbre vis/J. prestés avec soins", "Nombre de visites NOM", "Nombre de soins NOM", "Nombre plafonds journ.",
                    "Nombre visites par forfait", "Visites par mutualité",
                    "Total toilettes / total visites");
    final ObservableList<String> indicListFacturation = FXCollections
            .observableArrayList("Facturation totale", "Tarification OA", "Réintroductions", "Facturation par Visite",
                    "Facturation OA / jours prestés avec soins", "Facturation totale / jours prestés avec soins",
                    "Facturation totale par jours payés", "Recette OA");
    final ObservableList<String> indicListPatients = FXCollections
            .observableArrayList("Nombre de patients NOM/FF", "Nombre de patients VIPO", "Taux de rotation",
                    "FF palliatifs", "Patients par mutualité");
    final ObservableList<String> indicListDeplacements = FXCollections
            .observableArrayList("Kilomètres parcourus & par visite");
    final ObservableList<String> indicListSuiviDuPersonnelPourcents = FXCollections
            .observableArrayList("Total de jours payés en %", "Total presté", "Total absence non-payées", "Répartition des blocs",
                    "Suppléments en jours");

    // Périodes
    final ObservableList<String> periodList = FXCollections
            .observableArrayList("Année Complète", "Janvier", "Février", "Mars", "Avril", "Mai", "Juin", "Juillet",
                    "Août", "Septembre", "Octobre", "Novembre", "Décembre");
}
