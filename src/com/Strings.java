package com;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Strings {

    // Centres
    public ObservableList<String> centerList = FXCollections
            .observableArrayList("Global", "Namur", "Eghezée", "Ciney", "Philippeville", "Gedinne");

    // Années
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

    // Périodes
    public ObservableList<String> periodList = FXCollections
            .observableArrayList("Année Complète", "Janvier", "Février", "Mars", "Avril", "Mai", "Juin", "Juillet",
                    "Août", "Septembre", "Octobre", "Novembre", "Décembre");
}
