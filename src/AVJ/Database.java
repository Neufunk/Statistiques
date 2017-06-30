package AVJ;

import java.sql.*;

public class Database {

    Connection conn;

    public void connect() {
        try {
            System.out.println("Test du driver...");
            Class.forName("org.postgresql.Driver");
            System.out.println("Driver O.K.");

            String url = "jdbc:postgresql://localhost/Statistiques";
            String user = "java_user";
            String passwd = "fasd";

            System.out.println("Connexion en cours...");
            conn = DriverManager.getConnection(url, user, passwd);
            System.out.println("Connexion effective !");
            System.out.println("\n---------------------------------- \n");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void update() {

    }

    public void loadTable(String table) {
        try {
            Statement state = conn.createStatement();
            ResultSet result = state.executeQuery("SELECT * FROM "+table);
            ResultSetMetaData resultMeta = result.getMetaData();

            System.out.println("\n**********************************");

            for (int i = 1; i <= resultMeta.getColumnCount(); i++)
                System.out.print("\t" + resultMeta.getColumnName(i).toUpperCase() + "\t *");

            System.out.println("\n**********************************");

            while (result.next()) {
                for (int i = 1; i <= resultMeta.getColumnCount(); i++)
                    System.out.print("\t" + result.getObject(i).toString() + "\t |");

                System.out.println("\n---------------------------------");
            }

            result.close();
            state.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadColumn(){
        String answer = "";
        String query = "SELECT * " +
                "FROM secteurs " +
                "INNER JOIN travailleurs " +
                    "ON secteurs.id = travailleurs.secteur_id";
        try {
            Statement state = conn.createStatement();
            ResultSet result = state.executeQuery(query);
            while (result.next()){
                System.out.println(result.getString("prenom") + " " + result.getString("nom") +
                        " - " + result.getString("secteur_name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println(answer);
    }
}

