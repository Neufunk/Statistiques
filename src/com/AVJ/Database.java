package AVJ;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;

import java.sql.*;
import java.util.Arrays;

public class Database {

    private Connection conn;
    private Statement state;
    private ResultSet result;

    public Connection connect() {
        try {
            System.out.println("\n---------------------------------- ");
            System.out.println("Test du driver...");
            Class.forName("org.postgresql.Driver");
            System.out.println("Driver O.K.");

            String url = "jdbc:postgresql://130.15.0.89/statistiques";
            String user = "java_user";
            String passwd = "fasd";

            System.out.println("Connexion en cours...");
            conn = DriverManager.getConnection(url, user, passwd);
            System.out.println("Connexion effective !");
            System.out.println("---------------------------------- \n");

        } catch (Exception e) {
            e.printStackTrace();
        }
        return conn;
    }

    public ObservableList loadColumnToCombo(String table, String column, String orderBy) {
        ObservableList<String> array = FXCollections.observableArrayList();
        try {
            state = conn.createStatement();
            result = state.executeQuery("SELECT " + column + " FROM " + table + " ORDER BY " + orderBy);
            while (result.next()) {
                String answer = result.getString(column);
                array.add(answer);
            }
        } catch (Exception e) {
            e.printStackTrace();
            displayError(e);
        }
        return array;
    }

    ObservableList<String> loadSectorsToCombo(String centre) {
        ObservableList<String> answer = FXCollections.observableArrayList();
        if (centre.equals("ASD")) {
            centre = "Namur, Philippeville";
        }
        String sql = "SELECT * " +
                "FROM secteurs " +
                "WHERE antenne = '" + centre + "'" +
                "ORDER BY secteur_name ASC ";
        try {
            Statement state = conn.createStatement();
            ResultSet result = state.executeQuery(sql);
            while (result.next()) {
                answer.add(result.getString("secteur_name"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return answer;
    }

    String loadContingent38(String centre, String secteur, String periode, String annee, boolean checkboxState) {
        String sql;
        String additionPeriode = periode;
        if (periode.equals("Année Complète")) {
            periode = "Total";
            additionPeriode = "Décembre";
        }
        if (checkboxState) {
            sql = "SELECT annee, mois, indicateur, ROUND(SUM(valeur),2) AS valeur, antenne " +
                    "FROM contingent " +
                    "INNER JOIN secteurs " +
                    "ON contingent.numero_secteur = secteurs.id " +
                    "WHERE antenne = '" + centre + "'" +
                    "AND mois = '" + periode + "'" +
                    "AND annee = '" + annee + "'" +
                    "AND indicateur IN ('Total Heures dispo par mois (Base 38)', " +
                    "'Nbre H Absentéisme (code M) (Base 38)', " +
                    "'Nbre H Prestées (code PR) (Base 38)', " +
                    "'Ecart H Dispo et H prestées (Base 38)') " +
                    "GROUP BY indicateur, antenne, annee, mois " +
                    "UNION ALL " +
                    "SELECT annee, '', indicateur, ROUND(SUM(valeur),2) AS valeur, antenne " +
                    "FROM pot_depart_conges " +
                    "INNER JOIN secteurs " +
                    "ON pot_depart_conges.numero_secteur = secteurs.id " +
                    "WHERE antenne = '" + centre + "' " +
                    "AND annee = '" + annee + "' " +
                    "GROUP BY indicateur, antenne, annee " +
                    "UNION ALL " +
                    "SELECT annee, mois, indicateur, ROUND(SUM(valeur),2) AS valeur, antenne " +
                    "FROM conges_pris " +
                    "INNER JOIN secteurs " +
                    "ON conges_pris.numero_secteur = secteurs.id " +
                    "WHERE antenne = '" + centre + "'" +
                    "AND mois = '" + additionPeriode + "'" +
                    "AND annee = '" + annee + "'" +
                    "GROUP BY indicateur, antenne, annee, mois " +
                    "UNION ALL " +
                    "SELECT annee, '', indicateur, ROUND(SUM(valeur),2) AS valeur, antenne " +
                    "FROM solde_heures_recup " +
                    "INNER JOIN secteurs " +
                    "ON solde_heures_recup.numero_secteur = secteurs.id " +
                    "WHERE antenne = '" + centre + "' " +
                    "AND annee = '" + annee + "' " +
                    "GROUP BY indicateur, antenne, annee ";
        } else if (centre.equals("ASD")) {
            sql = "SELECT annee, mois, indicateur, ROUND(SUM(valeur),2) AS valeur " +
                    "FROM contingent " +
                    "INNER JOIN secteurs " +
                    "ON contingent.numero_secteur = secteurs.id " +
                    "WHERE mois = '" + periode + "' " +
                    "AND annee = '" + annee + "' " +
                    "AND indicateur IN ('Total Heures dispo par mois (Base 38)', " +
                    "'Nbre H Absentéisme (code M) (Base 38)', " +
                    "'Nbre H Prestées (code PR) (Base 38)', " +
                    "'Ecart H Dispo et H prestées (Base 38)') " +
                    "GROUP BY annee, mois, indicateur " +
                    "UNION ALL " +
                    "SELECT annee, '', indicateur, ROUND(SUM(valeur),2) AS valeur " +
                    "FROM pot_depart_conges " +
                    "INNER JOIN secteurs " +
                    "ON pot_depart_conges.numero_secteur = secteurs.id " +
                    "AND annee = '" + annee + "' " +
                    "GROUP BY indicateur, annee " +
                    "UNION ALL " +
                    "SELECT annee, mois, indicateur, ROUND(SUM(valeur),2) AS valeur " +
                    "FROM conges_pris " +
                    "INNER JOIN secteurs " +
                    "ON conges_pris.numero_secteur = secteurs.id " +
                    "WHERE mois = '" + additionPeriode + "' " +
                    "AND annee = '" + annee + "' " +
                    "GROUP BY indicateur, annee, mois " +
                    "UNION ALL " +
                    "SELECT annee, '', indicateur, ROUND(SUM(valeur),2) AS valeur " +
                    "FROM solde_heures_recup " +
                    "INNER JOIN secteurs " +
                    "ON solde_heures_recup.numero_secteur = secteurs.id " +
                    "AND annee = '" + annee + "' " +
                    "GROUP BY indicateur, annee";
        } else {
            sql = "SELECT annee, mois, indicateur, ROUND(SUM(valeur),2) AS valeur, secteur_name, antenne " +
                    "FROM contingent " +
                    "INNER JOIN secteurs " +
                    "ON contingent.numero_secteur = secteurs.id " +
                    "WHERE secteur_name = '" + secteur + "'" +
                    "AND mois = '" + periode + "'" +
                    "AND annee = '" + annee + "'" +
                    "AND indicateur IN ('Total Heures dispo par mois (Base 38)', " +
                    "'Nbre H Absentéisme (code M) (Base 38)', " +
                    "'Nbre H Prestées (code PR) (Base 38)', " +
                    "'Ecart H Dispo et H prestées (Base 38)') " +
                    "GROUP BY annee, mois, indicateur, antenne, secteur_name " +
                    "UNION ALL " +
                    "SELECT annee, '', indicateur, ROUND(SUM(valeur),2) AS valeur, secteur_name, antenne " +
                    "FROM pot_depart_conges " +
                    "INNER JOIN secteurs " +
                    "ON pot_depart_conges.numero_secteur = secteurs.id " +
                    "WHERE secteur_name = '" + secteur + "'" +
                    "AND annee = '" + annee + "'" +
                    "GROUP BY indicateur, antenne, annee, secteur_name " +
                    "UNION ALL " +
                    "SELECT annee, mois, indicateur, ROUND(SUM(valeur),2) AS valeur, secteur_name, antenne " +
                    "FROM conges_pris " +
                    "INNER JOIN secteurs " +
                    "ON conges_pris.numero_secteur = secteurs.id " +
                    "WHERE secteur_name = '" + secteur + "'" +
                    "AND mois = '" + additionPeriode + "'" +
                    "AND annee = '" + annee + "'" +
                    "GROUP BY indicateur, antenne, annee, mois, secteur_name " +
                    "UNION ALL " +
                    "SELECT annee, '', indicateur, ROUND(SUM(valeur),2) AS valeur, secteur_name, antenne " +
                    "FROM solde_heures_recup " +
                    "INNER JOIN secteurs " +
                    "ON solde_heures_recup.numero_secteur = secteurs.id " +
                    "WHERE secteur_name = '" + secteur + "'" +
                    "AND annee = '" + annee + "'" +
                    "GROUP BY indicateur, antenne, annee, secteur_name ";
        }
        return sql;
    }

    void updateContingent(String indicateur, double valeur, int year, String mois, String secteur, Connection connection) {
        try {
            String sql = "UPDATE contingent SET valeur = ? FROM secteurs " +
                    "WHERE indicateur = ? AND annee = ? AND numero_secteur = (SELECT secteurs.id FROM secteurs " +
                    "INNER JOIN contingent ON secteurs.id = contingent.numero_secteur WHERE secteur_name = ? LIMIT 1) AND mois = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setObject(1, valeur);
            preparedStatement.setObject(2, indicateur);
            preparedStatement.setObject(3, year);
            preparedStatement.setObject(4, secteur);
            preparedStatement.setObject(5, mois);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println(e + " - Erreur lors de la requête SQL");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e + " - Erreur lors de l'écriture dans la BDD");
        }
    }

    void updateSoldeHeuresRecup(Connection connection, int year, String secteur, double valeur) {
        try {
            String sql = "UPDATE solde_heures_recup SET valeur = ? FROM secteurs " +
                    "WHERE annee = ? AND numero_secteur = (SELECT secteurs.id FROM secteurs " +
                    "INNER JOIN solde_heures_recup ON secteurs.id = solde_heures_recup.numero_secteur WHERE secteur_name = ? LIMIT 1)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setObject(1, valeur);
            preparedStatement.setObject(2, year);
            preparedStatement.setObject(3, secteur);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println(e + " - Erreur lors de la requête SQL");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e + " - Erreur lors de l'écriture dans la BDD");
        }
    }

    void updatePotDepartConges(Connection connection, int year, String secteur, double valeur) {
        try {
            String sql = "UPDATE pot_depart_conges SET valeur = ? FROM secteurs " +
                    "WHERE annee = ? AND numero_secteur = (SELECT secteurs.id FROM secteurs " +
                    "INNER JOIN pot_depart_conges ON secteurs.id = pot_depart_conges.numero_secteur WHERE secteur_name = ? LIMIT 1)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setObject(1, valeur);
            preparedStatement.setObject(2, year);
            preparedStatement.setObject(3, secteur);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println(e + " - Erreur lors de la requête SQL");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e + " - Erreur lors de l'écriture dans la BDD");
        }
    }

    void updateCongesPris(Connection connection, int year, String mois, String secteur, double valeur) {
        try {
            String sql = "UPDATE conges_pris SET valeur = ? FROM secteurs " +
                    "WHERE annee = ? AND mois = ? AND numero_secteur = (SELECT secteurs.id FROM secteurs " +
                    "INNER JOIN conges_pris ON secteurs.id = conges_pris.numero_secteur WHERE secteur_name = ? LIMIT 1)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setObject(1, valeur);
            preparedStatement.setObject(2, year);
            preparedStatement.setObject(3, mois);
            preparedStatement.setObject(4, secteur);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println(e + " - Erreur lors de la requête SQL");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e + " - Erreur lors de l'écriture dans la BDD");
        }
    }

    public void closeConnection() {
        System.out.println("\n----------------------------------");
        System.out.println("Tentative de fermeture de connexion...");
        try {
            if (state != null) {
                state.close();
                System.out.println("\t - State fermé");
            }
            if (result != null) {
                result.close();
                System.out.println("\t - Result fermé");
            }
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("Connexion terminée.");
        System.out.println("---------------------------------- \n");

    }

    private void displayError(Exception e) {
        e.printStackTrace();
        String e1 = e.toString();
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur");
        alert.setHeaderText(e1);
        alert.setContentText("STACKTRACE : \t\t" + Arrays.toString(e.getStackTrace()) + "\n" +
                "CAUSE : \t\t\t" + e.getLocalizedMessage() + "\n" + "\t\t" + this.getClass().toString() + " - displayFormatException()");
        alert.showAndWait();
    }

}

