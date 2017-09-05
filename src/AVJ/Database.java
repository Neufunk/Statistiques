package AVJ;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

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

            String url = "jdbc:postgresql://localhost/statistiques";
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
                "WHERE antenne = '" + centre + "'";
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

    public String loadContingent(String centre, String secteur, String periode, String annee) {
        String sql;
        if (periode == "Année Complète") {
            periode = "Total";
        }
        if (centre == "ASD") {
            sql = "SELECT annee, mois, indicateur, valeur, secteur_name, antenne " +
                    "FROM contingent " +
                    "INNER JOIN secteurs " +
                    "ON contingent.numero_secteur = secteurs.id " +
                    "WHERE mois = '" + periode + "'" +
                    "AND annee = '" + annee + "'";
        } else {
            sql = "SELECT annee, mois, indicateur, valeur, secteur_name, antenne " +
                    "FROM contingent " +
                    "INNER JOIN secteurs " +
                    "ON contingent.numero_secteur = secteurs.id " +
                    "WHERE secteur_name = '" + secteur + "'" +
                    "AND mois = '" + periode + "'" +
                    "AND annee = '" + annee + "'";
        }
        return sql;
    }

    public void updateContingent(String indicateur, double valeur, int year, String mois, String secteur) {
        Connection conn = connect();
        try {
            Statement state = conn.createStatement();
            String sql = "UPDATE contingent " +
                    "SET valeur = '" + valeur + "' " +
                    "WHERE EXISTS (SELECT secteur_name FROM secteurs WHERE secteurs.id = numero_secteur) = '" + secteur + "' AND indicateur = '" + indicateur + "' AND annee = '" + year + "' " +
                    "AND numero_secteur = '" + secteur + "' AND mois = '" + mois + "'";

                    /*
                    "INNER JOIN statistiques.secteurs " +
                    "ON contingent.numero_secteur = secteurs.id ";
                    */
            state.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            state.close();
            conn.close();
        } catch (Exception e) {
            System.out.println(e + " Erreur lors de l'écriture dans la BDD");
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

}

