package AVJ;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import org.postgresql.util.PSQLException;

import java.sql.*;

public class Database {

    private Connection conn;
    private Statement state = null;
    private ResultSet result = null;

    public void connect() {
        try {
            System.out.println("\n---------------------------------- ");
            System.out.println("Test du driver...");
            Class.forName("org.postgresql.Driver");
            System.out.println("Driver O.K.");

            String url = "jdbc:postgresql://localhost/Statistiques";
            String user = "java_user";
            String passwd = "fasd";

            System.out.println("Connexion en cours...");
            conn = DriverManager.getConnection(url, user, passwd);
            System.out.println("Connexion effective !");
            System.out.println("---------------------------------- \n");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void update() {

    }

    public ObservableList loadColumnToCombo(String table, String column) {
        ObservableList<String> array = FXCollections.observableArrayList();
        try {
            state = conn.createStatement();
            result = state.executeQuery("SELECT "+column+ " FROM " + table);
            while (result.next()) {
                String answer = result.getString(column);
                array.add(answer);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return array;
    }

    public ObservableList loadTabletoList(String table) {
        ObservableList<String> array = FXCollections.observableArrayList();
        try {
            Statement state = conn.createStatement();
            ResultSet result = state.executeQuery("SELECT * FROM " + table);
            ResultSetMetaData resultMeta = result.getMetaData();
            while (result.next()) {
                array.add(result.getString("nom"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return array;
    }

    public String loadWorkerName(String secteur){
        String answer = "";
        String query = "SELECT * " +
                "FROM travailleurs " +
                "INNER JOIN secteurs " +
                    "ON travailleurs.id = secteurs.worker_id " +
                "WHERE secteur_name = '"+secteur+"'";
        try {
            Statement state = conn.createStatement();
            ResultSet result = state.executeQuery(query);
            while (result.next()){
                answer = result.getString("prenom") + " " +result.getString("nom");
            }
            result.close();
            state.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return answer;
    }

    public void closeConnection(){
        System.out.println("\n----------------------------------");
        System.out.println("Tentative de fermeture de connexion...");
        try {
            if (state!=null){
                state.close();
                System.out.println("\t - State fermé");
            }
            if (result!=null){
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

