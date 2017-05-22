package com;

import java.sql.*;

public class AccessConnection {

    private static Connection connexion;

    public static Connection SiStatConnection() {

        String url = "jdbc:ucanaccess://P:/INFORMATIQUE/Statistiques SI.accdb";
        String user = "";
        String password = "";

        System.out.println("Connexion au fichier >> Statistiques SI.accdb...");
        try {
            Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
            System.out.println("Drivers O.K.");
            connexion = DriverManager.getConnection(url, user, password);
            System.out.println("Connexion effectuée");
        } catch (
                Exception e1)

        {
            System.err.println("Exception trouvée : ");
            System.err.println(e1.getMessage());
        }
        return connexion;
    }


    private void QueryExemple(){
        try {
            Statement st = AccessConnection.SiStatConnection().createStatement();
            String query1 = "SELECT * FROM 902_2015 WHERE id=6";
            ResultSet rs = st.executeQuery(query1);
            while (rs.next()) {
                String Result = rs.getString("Champ3");
                System.out.println(Result);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void PreparedStatementExample(){

        try {
            PreparedStatement ps0 = AccessConnection.SiStatConnection().prepareStatement("SELECT * FROM 902_2015 WHERE Champ2 = ?");
            ps0.setString(1, "INDICATEUR");
            ResultSet result = ps0.executeQuery();
            while (result.next()) {
                String rs = result.getString("Champ3");
                System.out.println(rs);
                ps0.close();
                AccessConnection.SiStatConnection().close();
                System.out.println("Connexion terminée");
            }
        } catch (SQLException e1) {
            e1.printStackTrace();
        }
    }
}

