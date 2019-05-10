package avj;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import tools.ExceptionHandler;
import tools.Identification;

import java.sql.*;

class Database {

    private final Identification id = new Identification();
    private Connection conn;
    private Statement state;
    private ResultSet result;

    Connection connect() {
        try {
            System.out.println("\n---------------------------------- ");
            System.out.println("Test du driver...");
            Class.forName("org.postgresql.Driver");
            System.out.println("Driver O.K.");

            String url = id.set(Identification.info.D03_URL);
            String user = id.set(Identification.info.D03_USER);
            String passwd = id.set(Identification.info.D03_PASSWD);

            System.out.println("Connexion en cours...");
            conn = DriverManager.getConnection(url, user, passwd);
            System.out.println("Connexion effective !");
            System.out.println("---------------------------------- \n");

        } catch (Exception e) {
            e.printStackTrace();
        }
        return conn;
    }

    ObservableList loadColumnToCombo(String table, String column, String orderBy) {
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
            ExceptionHandler.switchException(e, this.getClass());
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
        } finally {
            close(result);
            close(state);
            close(conn);
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
            sql = "SELECT * FROM (" +
                    "SELECT annee, mois, indicateur, ROUND(SUM(valeur),2) AS valeur, antenne " +
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

                    "SELECT pot_depart_conges.annee, conges_pris.mois AS mois, 'Solde congés', " +
                    "(SELECT ROUND(SUM(valeur),2) AS valeur " +
                    "FROM pot_depart_conges " +
                    "INNER JOIN secteurs " +
                    "ON pot_depart_conges.numero_secteur = secteurs.id " +
                    "WHERE annee = '" + annee + "' AND antenne = '" + centre + "')" +
                    "-" +
                    "(SELECT ROUND(SUM(valeur),2) AS valeur " +
                    "FROM conges_pris " +
                    "INNER JOIN secteurs " +
                    "ON conges_pris.numero_secteur = secteurs.id " +
                    "WHERE annee = '" + annee + "' " +
                    "AND antenne = '" + centre + "' " +
                    "AND mois = '" + additionPeriode + "') AS Soustraction, " +
                        "antenne " +
                    "FROM pot_depart_conges " +
                    "INNER JOIN secteurs " +
                    "ON pot_depart_conges.numero_secteur = secteurs.id " +
                    "INNER JOIN conges_pris " +
                    "ON pot_depart_conges.numero_secteur = conges_pris.numero_secteur " +
                    "WHERE antenne = '" + centre + "' " +
                    "AND mois = '" + periode + "' " +
                    "AND pot_depart_conges.annee = '" + annee + "' " +
                    "GROUP BY pot_depart_conges.indicateur, antenne, pot_depart_conges.annee, mois " +

                    "UNION ALL " +

                    "SELECT annee, '', indicateur, ROUND(SUM(valeur),2) AS valeur, antenne " +
                    "FROM solde_heures_recup " +
                    "INNER JOIN secteurs " +
                    "ON solde_heures_recup.numero_secteur = secteurs.id " +
                    "WHERE antenne = '" + centre + "' " +
                    "AND annee = '" + annee + "' " +
                    "GROUP BY indicateur, antenne, annee " +
                    ") alias " +
                    "order by case indicateur " +
                    "WHEN 'Total Heures dispo par mois (Base 38)' THEN 1 " +
                    "WHEN 'Nbre H Prestées (code PR) (Base 38)'   THEN 2 " +
                    "WHEN 'Ecart H Dispo et H prestées (Base 38)' THEN 3 " +
                    "WHEN 'Nbre H Absentéisme (code M) (Base 38)' THEN 4 " +
                    "WHEN 'Pot départ congés' THEN 5 " +
                    "WHEN 'congés pris' THEN 6 " +
                    "WHEN 'solde congés'THEN 7 " +
                    "WHEN 'Solde heures récup' THEN 8 " +
                    "end asc";
        } else if (centre.equals("ASD")) {
            sql = "SELECT * FROM (" +
                    "SELECT annee, mois, indicateur, ROUND(SUM(valeur),2) AS valeur " +
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

                    "SELECT pot_depart_conges.annee, conges_pris.mois AS mois, 'Congés Restants', " +
                    "(SELECT ROUND(SUM(valeur),2) AS valeur " +
                    "FROM pot_depart_conges " +
                    "INNER JOIN secteurs " +
                    "ON pot_depart_conges.numero_secteur = secteurs.id " +
                    "WHERE annee = '" + annee + "')" +
                    "-" +
                    "(SELECT ROUND(SUM(valeur),2) AS valeur " +
                    "FROM conges_pris " +
                    "INNER JOIN secteurs " +
                    "ON conges_pris.numero_secteur = secteurs.id " +
                    "WHERE annee = '" + annee + "' " +
                    "AND mois = '" + additionPeriode + "') AS Soustraction " +
                    "FROM pot_depart_conges " +
                    "INNER JOIN secteurs " +
                    "ON pot_depart_conges.numero_secteur = secteurs.id " +
                    "INNER JOIN conges_pris " +
                    "ON pot_depart_conges.numero_secteur = conges_pris.numero_secteur " +
                    "WHERE mois = '" + periode + "' " +
                    "AND pot_depart_conges.annee = '" + annee + "' " +
                    "GROUP BY pot_depart_conges.indicateur, pot_depart_conges.annee, mois " +

                    "UNION ALL " +

                    "SELECT annee, '', indicateur, ROUND(SUM(valeur),2) AS valeur " +
                    "FROM solde_heures_recup " +
                    "INNER JOIN secteurs " +
                    "ON solde_heures_recup.numero_secteur = secteurs.id " +
                    "AND annee = '" + annee + "' " +
                    "GROUP BY indicateur, annee " +
                    ") alias " +
                    "order by case indicateur " +
                    "WHEN 'Total Heures dispo par mois (Base 38)' THEN 1 " +
                    "WHEN 'Nbre H Prestées (code PR) (Base 38)'   THEN 2 " +
                    "WHEN 'Ecart H Dispo et H prestées (Base 38)' THEN 3 " +
                    "WHEN 'Nbre H Absentéisme (code M) (Base 38)' THEN 4 " +
                    "WHEN 'Pot départ congés' THEN 5 " +
                    "WHEN 'congés pris' THEN 6 " +
                    "WHEN 'solde congés'THEN 7 " +
                    "WHEN 'Solde heures récup' THEN 8 " +
                    "END asc";
        } else {
            sql = "SELECT * FROM ( " +
                    "SELECT annee, mois, indicateur, ROUND(SUM(valeur),2) AS valeur, secteur_name AS Secteur, antenne " +
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

                    "SELECT pot_depart_conges.annee, conges_pris.mois AS mois, 'Congés Restants', " +
                    "(SELECT ROUND(SUM(valeur),2) AS valeur " +
                    "FROM pot_depart_conges " +
                    "INNER JOIN secteurs " +
                    "ON pot_depart_conges.numero_secteur = secteurs.id " +
                    "WHERE annee = '" + annee + "' AND antenne = '" + centre + "' AND secteur_name = '" + secteur +"')" +
                    "-" +
                    "(SELECT ROUND(SUM(valeur),2) AS valeur " +
                    "FROM conges_pris " +
                    "INNER JOIN secteurs " +
                    "ON conges_pris.numero_secteur = secteurs.id " +
                    "WHERE annee = '" + annee + "' " +
                    "AND antenne = '" + centre + "' " +
                    "AND secteur_name = '" + secteur + "' " +
                    "AND mois = '" + additionPeriode + "') AS Soustraction, " +
                    "secteur_name, antenne " +
                    "FROM pot_depart_conges " +
                    "INNER JOIN secteurs " +
                    "ON pot_depart_conges.numero_secteur = secteurs.id " +
                    "INNER JOIN conges_pris " +
                    "ON pot_depart_conges.numero_secteur = conges_pris.numero_secteur " +
                    "WHERE antenne = '" + centre + "' " +
                    "AND mois = '" + additionPeriode + "' " +
                    "AND secteur_name = '" + secteur + "' " +
                    "AND pot_depart_conges.annee = '" + annee + "' " +
                    "GROUP BY pot_depart_conges.indicateur, secteur_name, antenne, pot_depart_conges.annee, mois " +

                    "UNION ALL " +

                    "SELECT annee, '', indicateur, ROUND(SUM(valeur),2) AS valeur, secteur_name, antenne " +
                    "FROM solde_heures_recup " +
                    "INNER JOIN secteurs " +
                    "ON solde_heures_recup.numero_secteur = secteurs.id " +
                    "WHERE secteur_name = '" + secteur + "' " +
                    "AND annee = '" + annee + "' " +
                    "GROUP BY indicateur, antenne, annee, secteur_name " +
                    ") alias " +
                    "order by case indicateur " +
                    "WHEN 'Total Heures dispo par mois (Base 38)' THEN 1 " +
                    "WHEN 'Nbre H Prestées (code PR) (Base 38)'   THEN 2 " +
                    "WHEN 'Ecart H Dispo et H prestées (Base 38)' THEN 3 " +
                    "WHEN 'Nbre H Absentéisme (code M) (Base 38)' THEN 4 " +
                    "WHEN 'Pot départ congés' THEN 5 " +
                    "WHEN 'congés pris' THEN 6 " +
                    "WHEN 'solde congés'THEN 7 " +
                    "WHEN 'Solde heures récup' THEN 8 " +
                    "end asc";
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

    void close(ResultSet rs){
        try {
            System.out.println("Tentative de fermeture de ResultSet...");
            if (rs != null) {
                rs.close();
            }
            System.out.println("ResultSet terminé");
        } catch (SQLException e) {
            ExceptionHandler.switchException(e, this.getClass());
        }
    }

    void close(Statement st){
        try {
            System.out.println("Tentative de fermeture de Statement...");
            if (st != null) {
                st.close();
            }
            System.out.println("Statement terminé");
        } catch (SQLException e) {
            ExceptionHandler.switchException(e, this.getClass());
        }
    }

    void close(PreparedStatement ps){
        try {
            System.out.println("Tentative de fermeture de PreparedStatement...");
            if (ps != null) {
                ps.close();
            }
            System.out.println("PreparedStatement terminé");
        } catch (SQLException e) {
            ExceptionHandler.switchException(e, this.getClass());
        }
    }

    void close(Connection conn){
        try {
            System.out.println("Tentative de fermeture de Connexion...");
            if (conn != null) {
                conn.close();
                System.out.println("Connexion terminée");
            } else {
                System.out.println("Aucune connexion en cours");
            }
        } catch (SQLException e) {
            ExceptionHandler.switchException(e, this.getClass());
        }
    }
}

