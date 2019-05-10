package tools;

import java.sql.*;

public class DatabaseConnection {

    private Connection conn;

    public Connection connect(String url, String user, String passwd, String driver) {
        try {
            System.out.println("\n---------------------------------- ");
            System.out.println("Test du driver...");
            Class.forName(driver);
            System.out.println("Driver O.K.");

            System.out.println("Connexion en cours...");
            conn = DriverManager.getConnection(url, user, passwd);
            System.out.println("Connexion effective !");
            System.out.println("---------------------------------- \n");

        } catch (Exception e) {
            ExceptionHandler.switchException(e, this.getClass());
        }
        return conn;
    }

    public void close(ResultSet rs){
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

    public void close(Statement st){
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

    public void close(PreparedStatement ps){
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

    public void close(Connection conn){
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
