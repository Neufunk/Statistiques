package com;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Strings {

<<<<<<< HEAD
=======
    // Page titles
    public static String homePageTitle = "Aide & Soins à Domicile - Statistiques // FX_Alpha3";
    public static String pageTitle0 = "Soins Infirmiers - Indicateurs annuels // FX_Alpha3";
    public static String pageTitle1 = "Soins Infirmiers - Evolution d'un indicateur // FX_Alpha3";

>>>>>>> a33b3a7b1954e1a86b3af28986c5591d0a2475e8
    // Centres
    public ObservableList<String> centerList = FXCollections
            .observableArrayList("Global", "Namur", "Eghezée", "Ciney", "Philippeville", "Gedinne");

    // Années
<<<<<<< HEAD
    public ObservableList<Integer> yearList = FXCollections
            .observableArrayList(2015, 2016, 2017);

    // Titres des Indicateurs
    public ObservableList<String> titleList = FXCollections
            .observableArrayList("Suivi du personnel infirmier", "Soins et visites", "Facturation", "Patients", "Déplacements", "\n",
                    "Total jours payés en %", "Total presté en %", "Total absences non payées en %", "Répartition des blocs en %",
                    "Suppléments (en jours) en %", "\n", "Total joues payés / h");

    // Indicateurs
    public ObservableList<String> indicList = FXCollections
            .observableArrayList("Total jours payés", "Total jours prestés Infirmières", "% coupés", "Solde récup. fin de mois", "\n",
                    "Nombre de visites", "Nombre de soins", "Nbre soins/visite", "\n", "Nbre vis/J. prestés avec soins", "\n",
                    "Nombre visites NOM", "Nombre soins NOM", "Nombre plafonds journ.", "\n", "Nombre visites FFA", "Nombre visites FFB",
                    "Nombre visites FFC", "\n", "% visites MC Accord", "% visites autres MC", "% visites autres OA", "\n",
                    "Total toilettes NOM / total visites", "Total toilettes / total visites", "\n", "Facturation totale", "Facturation OA",
                    "Tarific autres", "TM", "Réintroduction");
=======
    public static ObservableList<Integer> yearList = FXCollections
            .observableArrayList(2015, 2016, 2017, 2018, 2019, 2020);

    // Titres des Indicateurs
    public ObservableList<String> categorieList = FXCollections
            .observableArrayList("SUIVI DU PERSONNEL INFIRMIER", "SOINS ET VISITES", "FACTURATION", "PATIENTS",
                    "DÉPLACEMENTS", "SUIVI DU PERSONNEL INFIRMIER - DÉTAILS EN %");

    // Indicateurs
    public ObservableList<String> emptyList = FXCollections.observableArrayList("");
    public ObservableList<String> indicListSuiviDuPersonnel = FXCollections
            .observableArrayList("Total de jours payés", "Total de jours prestés Infirmières", "% coupés",
                    "Solde récup. fin de mois");
    public ObservableList<String> indicListSoinsEtVisites = FXCollections
            .observableArrayList("Nombre de visites", "Nombre de soins", "Nombre de soins par visite",
                    "Nbre vis/J. prestés avec soins", "Nombre de visites NOM", "Nombre de soins NOM", "Nombre plafonds journ.",
                    "Nombre visites par forfait", "Visites par mutualité",
                    "Total toilettes / total visites");
    public ObservableList<String> indicListFacturation = FXCollections
            .observableArrayList("Facturation totale", "Tarification OA", "Réintroductions","Facturation par Visite",
                    "Facturation OA / jours prestés avec soins", "Facturation totale / jours prestés avec soins",
                    "Facturation totale par jours payés", "Recette OA");
    public ObservableList<String> indicListPatients = FXCollections
            .observableArrayList("Nombre de patients NOM/FF", "Nombre de patients VIPO", "Taux de rotation",
                    "FF palliatifs", "Patients par mutualité");
    public ObservableList<String> indicListDeplacements = FXCollections
            .observableArrayList("Kilomètres parcourus & par visite");
    public ObservableList<String> indicListSuiviDuPersonnelPourcents = FXCollections
            .observableArrayList("Total de jours payés en %", "Total presté", "Total absence non-payées", "Répartition des blocs",
                    "Suppléments en jours");
>>>>>>> a33b3a7b1954e1a86b3af28986c5591d0a2475e8

    // Périodes
    public ObservableList<String> periodList = FXCollections
            .observableArrayList("Année Complète", "Janvier", "Février", "Mars", "Avril", "Mai", "Juin", "Juillet",
                    "Août", "Septembre", "Octobre", "Novembre", "Décembre");
<<<<<<< HEAD
}
=======

}


>>>>>>> a33b3a7b1954e1a86b3af28986c5591d0a2475e8
