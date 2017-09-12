package AVJ;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;

import java.sql.*;

public class Database {

    private Connection conn;
    private Statement state = null;
    private ResultSet result = null;

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

    public ObservableList loadColumnToCombo(String table, String column) {
        ObservableList<String> array = FXCollections.observableArrayList();
        try {
            state = conn.createStatement();
            result = state.executeQuery("SELECT " + column + " FROM " + table);
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

    public String loadWorkerName(String secteur) {
        String answer = "";
        String query = "SELECT * " +
                "FROM travailleurs " +
                "INNER JOIN secteurs " +
                "ON travailleurs.id = secteurs.worker_id " +
                "WHERE secteur_name = '" + secteur + "'";
        try {
            Statement state = conn.createStatement();
            ResultSet result = state.executeQuery(query);
            while (result.next()) {
                answer = result.getString("prenom") + " " + result.getString("nom");
            }
            result.close();
            state.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return answer;
    }

    public String loadPathName(String secteur) {
        String prenom = "";
        String query = "SELECT * " +
                "FROM travailleurs " +
                "INNER JOIN secteurs " +
                "ON travailleurs.id = secteurs.worker_id " +
                "WHERE secteur_name = '" + secteur + "'";
        try {
            Statement state = conn.createStatement();
            ResultSet result = state.executeQuery(query);
            while (result.next()) {
                prenom = result.getString("prenom");
            }
            result.close();
            state.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return prenom;
    }

    public String loadPathSecteur(String secteur) {
        String sect = "";
        String query = "SELECT * " +
                "FROM travailleurs " +
                "INNER JOIN secteurs " +
                "ON travailleurs.id = secteurs.worker_id " +
                "WHERE secteur_name = '" + secteur + "'";
        try {
            Statement state = conn.createStatement();
            ResultSet result = state.executeQuery(query);
            while (result.next()) {
                sect = result.getString("secteur");
            }
            result.close();
            state.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return sect;
    }

    public ObservableList loadSectorsToCombo(String centre) {
        ObservableList<String> answer = FXCollections.observableArrayList();
        if (centre == "ASD") {
            centre = "Namur, Philippeville";
        }
        String sql = "SELECT * " +
                "FROM secteurs " +
                "WHERE antenne = '" + centre + "'" +
                "ORDER BY secteur_name ASC ";
        try {
            Statement state = conn.createStatement();
            ResultSet result = state.executeQuery(sql);
            ResultSetMetaData resultMeta = result.getMetaData();
            while (result.next()) {
                answer.add(result.getString("secteur_name"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return answer;
    }

    public String loadContingent38(String centre, String secteur, String periode, String annee, boolean checkboxState) {
        String sql;
        if (periode == "Année Complète") {
            periode = "Total";
        }
        if (checkboxState) {
            sql = "SELECT annee, mois, indicateur, ROUND (CAST(SUM(valeur) AS numeric),2) AS valeur, antenne " +
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
                    "GROUP BY indicateur, antenne, annee, mois";
        } else if (centre == "ASD") {
            sql = "SELECT annee, mois, indicateur, ROUND(CAST(SUM(valeur) AS numeric),2) AS valeur, antenne " +
                    "FROM contingent " +
                    "INNER JOIN secteurs " +
                    "ON contingent.numero_secteur = secteurs.id " +
                    "WHERE mois = '" + periode + "'" +
                    "AND annee = '" + annee + "'" +
                    "AND indicateur IN ('Total Heures dispo par mois (Base 38)', " +
                    "'Nbre H Absentéisme (code M) (Base 38)', " +
                    "'Nbre H Prestées (code PR) (Base 38)', " +
                    "'Ecart H Dispo et H prestées (Base 38)') " +
                    "GROUP BY annee, mois, indicateur, antenne";
        } else {
            sql = "SELECT annee, mois, indicateur, ROUND (CAST(valeur AS numeric),2) AS valeur, secteur_name, antenne " +
                    "FROM contingent " +
                    "INNER JOIN secteurs " +
                    "ON contingent.numero_secteur = secteurs.id " +
                    "WHERE secteur_name = '" + secteur + "'" +
                    "AND mois = '" + periode + "'" +
                    "AND annee = '" + annee + "'" +
                    "AND indicateur IN ('Total Heures dispo par mois (Base 38)', " +
                    "'Nbre H Absentéisme (code M) (Base 38)', " +
                    "'Nbre H Prestées (code PR) (Base 38)', " +
                    "'Ecart H Dispo et H prestées (Base 38)') ";
        }
        return sql;
    }

    public void updateContingent(String indicateur, double valeur, int year, String mois, String secteur) {
        try {
            String sql = "UPDATE contingent SET valeur = ? FROM secteurs " +
                    "WHERE indicateur = ? AND annee = ? AND numero_secteur = (SELECT secteurs.id FROM secteurs " +
                    "INNER JOIN contingent ON secteurs.id = contingent.numero_secteur WHERE secteur_name = ? LIMIT 1) AND mois = ?";
            PreparedStatement preparedStatement = connect().prepareStatement(sql);
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
        closeConnection();
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

    private void displayError(Exception e){
        e.printStackTrace();
        String e1 = e.toString();
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur");
        alert.setHeaderText(e1);
        alert.setContentText("STACKTRACE : \t\t" + e.getStackTrace() + "\n" +
                "CAUSE : \t\t\t" + e.getLocalizedMessage() + "\n" + "\t\t" + this.getClass().toString() + " - displayFormatException()");
        alert.showAndWait();
    }

}

