package SoinsInfirmiers;

import main.ExceptionHandler;
import main.Identification;

import java.sql.*;

class Database {

    private Connection conn;
    private Identification id = new Identification();

    Connection connect() {
        try {
            System.out.println("\n---------------------------------- ");
            System.out.println("Test du driver...");
            Class.forName("org.postgresql.Driver");
            System.out.println("Driver O.K.");

            String url = id.set(Identification.info.D615_URL);
            String user = id.set(Identification.info.D615_USER);
            String passwd = id.set(Identification.info.D615_PASSWD);

            System.out.println("Connexion en cours à " + url);
            conn = DriverManager.getConnection(url, user, passwd);
            System.out.println("Connexion effective !");
            System.out.println("---------------------------------- \n");
        } catch (Exception e) {
            ExceptionHandler.switchException(e, this.getClass());
        }
        return conn;
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
            }
            System.out.println("Connexion terminée");
        } catch (SQLException e) {
            ExceptionHandler.switchException(e, this.getClass());
        }
    }

    public enum Query {
        VISITES_PAR_CENTRE,
        VISITES_PAR_LOCALITE,
        PATIENTS_PAR_CENTRE,
        PATIENTS_PAR_LOCALITE,
        NOMBRE_DE_VISITES
    }

    String setQuery(Query queryName) {
        String query = "";
        switch(queryName){
            case VISITES_PAR_CENTRE:
                query = "SELECT COALESCE( TO_CHAR( Center ), 'ALL' ) AS Centre,\n" +
                        "NVL(Janvier,0) AS Janvier,\n" +
                        "NVL(Février,0) AS Février,\n" +
                        "NVL(Mars,0) AS Mars,\n" +
                        "NVL(Avril,0) AS Avril, \n" +
                        "NVL(Mai,0) AS Mai, \n" +
                        "NVL(Juin,0) AS Juin,\n" +
                        "NVL(Juillet,0) AS Juillet, \n" +
                        "NVL(Aout,0) AS Aout,\n" +
                        "NVL(Septembre,0) AS Septembre, \n" +
                        "NVL(Octobre,0) AS Octobre,  \n" +
                        "NVL(Novembre,0) AS Novembre, \n" +
                        "NVL(Décembre,0) AS Décembre,  \n" +
                        "(NVL(Janvier,0) + NVL(Février,0) + NVL(Mars,0) + NVL(Avril,0) + NVL(Mai,0) + NVL(Juin,0) + NVL(Juillet,0) + NVL(Aout,0) + NVL(Septembre,0) + NVL(Octobre,0) + NVL(Novembre,0) + NVL(Décembre,0)) AS Total\n" +
                        "FROM ( \n" +
                        "   SELECT COUNT(*) AS cnt, ioi.center, EXTRACT( MONTH FROM ioi.achiev_dat ) AS month\n" +
                        "   FROM   INVOICE_OUT_ITEMS ioi, INVOICES_OUT ino, CONV_HC.CARE_TARIFFS ctf, MUNICIPALITIES mu, STREETS st, FAMS fa, FAM_membs fmr \n" +
                        "   WHERE  ioi.center IN ( 961, 931, 923, 913, 902 ) \n" +
                        "   AND ioi.ctf_seq_no = ctf.seq_no\n" +
                        "   AND ioi.ino_seq_no = ino.seq_no\n" +
                        "   AND fmr.fay_seq_no = INO.fmr_fay_seq_no_2\n" +
                        "   AND fmr.ref_no = INO.fmr_ref_no_2\n" +
                        "   AND fa.seq_no = fmr.fay_seq_no\n" +
                        "   AND fa.stt_seq_no = st.seq_no\n" +
                        "   AND st.mun_seq_no = mu.seq_no\n" +
                        "   AND    ioi.ACHIEV_DAT >= ?  \n" +
                        "   AND    ioi.ACHIEV_DAT < ? \n" +
                        "   AND    ioi.REINVOICING = '1' \n" +
                        "   AND    ino.INVOICE_TYPE = 'HM'\n" +
                        "   AND    ctf.class IN ('B1','B2','B3','P1','P2','P3','P4','P5')\n" +
                        "   AND mu.postal_code BETWEEN '5000' AND '9999' \n" +
                        "   GROUP BY ROLLUP( ioi.Center ), EXTRACT( MONTH FROM ioi.achiev_dat ), substr(ino.seq_no, 1, 6)\n" +
                        ") \n" +
                        "PIVOT ( MAX(cnt) FOR month IN ( \n" +
                        "   1 AS Janvier, \n" +
                        "   2 AS Février, \n" +
                        "   3 AS Mars, \n" +
                        "   4 AS Avril, \n" +
                        "   5 AS Mai, \n" +
                        "   6 AS Juin, \n" +
                        "   7 AS Juillet, \n" +
                        "   8 AS Aout, \n" +
                        "   9 AS Septembre, \n" +
                        "   10 AS Octobre, \n" +
                        "   11 AS Novembre, \n" +
                        "   12 AS Décembre \n" +
                        "   ) \n" +
                        ") \n" +
                        "ORDER BY Centre NULLS LAST";
                break;

            case VISITES_PAR_LOCALITE:
                query = "SELECT COALESCE( TO_CHAR( Name ), 'ALL' ) AS Localité,\n" +
                        "POSTAL_CODE AS Code_Postal,  \n" +
                        "NVL(Janvier,0) AS Janvier,\n" +
                        "NVL(Février,0) AS Février,\n" +
                        "NVL(Mars,0) AS Mars,\n" +
                        "NVL(Avril,0) AS Avril, \n" +
                        "NVL(Mai,0) AS Mai, \n" +
                        "NVL(Juin,0) AS Juin,\n" +
                        "NVL(Juillet,0) AS Juillet, \n" +
                        "NVL(Aout,0) AS Aout,\n" +
                        "NVL(Septembre,0) AS Septembre, \n" +
                        "NVL(Octobre,0) AS Octobre,  \n" +
                        "NVL(Novembre,0) AS Novembre, \n" +
                        "NVL(Décembre,0) AS Décembre,  \n" +
                        "(NVL(Janvier,0) + NVL(Février,0) + NVL(Mars,0) + NVL(Avril,0) + NVL(Mai,0) + NVL(Juin,0) + NVL(Juillet,0) + NVL(Aout,0) + NVL(Septembre,0) + NVL(Octobre,0) + NVL(Novembre,0) + NVL(Décembre,0)) AS Total\n" +
                        "FROM ( \n" +
                        "   SELECT COUNT(*) AS cnt, ioi.center, mu.name, mu.POSTAL_CODE, EXTRACT( MONTH FROM ioi.achiev_dat ) AS month\n" +
                        "   FROM   INVOICE_OUT_ITEMS ioi, INVOICES_OUT ino, CONV_HC.CARE_TARIFFS ctf, MUNICIPALITIES mu, STREETS st, FAMS fa, FAM_membs fmr \n" +
                        "   WHERE  ioi.center IN ( 961, 931, 923, 913, 902 ) \n" +
                        "   AND ioi.ctf_seq_no = ctf.seq_no\n" +
                        "   AND ioi.ino_seq_no = ino.seq_no\n" +
                        "   AND fmr.fay_seq_no = INO.fmr_fay_seq_no_2\n" +
                        "   AND fmr.ref_no = INO.fmr_ref_no_2\n" +
                        "   AND fa.seq_no = fmr.fay_seq_no\n" +
                        "   AND fa.stt_seq_no = st.seq_no\n" +
                        "   AND st.mun_seq_no = mu.seq_no\n" +
                        "   AND    ioi.ACHIEV_DAT >= ? \n" +
                        "   AND    ioi.ACHIEV_DAT < ? \n" +
                        "   AND    ioi.REINVOICING = '1' \n" +
                        "   AND    ino.INVOICE_TYPE = 'HM'\n" +
                        "   AND    ctf.class IN ('B1','B2','B3','P1','P2','P3','P4','P5')\n" +
                        "   AND    mu.postal_code BETWEEN ? AND ? \n" +
                        "   GROUP BY ROLLUP( mu.name ), mu.POSTAL_CODE, EXTRACT( MONTH FROM ioi.achiev_dat ), substr(ino.seq_no, 1, 6), ioi.center\n" +
                        ") \n" +
                        "PIVOT ( MAX(cnt) FOR month IN ( \n" +
                        "   1 AS Janvier, \n" +
                        "   2 AS Février, \n" +
                        "   3 AS Mars, \n" +
                        "   4 AS Avril, \n" +
                        "   5 AS Mai, \n" +
                        "   6 AS Juin, \n" +
                        "   7 AS Juillet, \n" +
                        "   8 AS Aout, \n" +
                        "   9 AS Septembre, \n" +
                        "   10 AS Octobre, \n" +
                        "   11 AS Novembre, \n" +
                        "   12 AS Décembre\n" +
                        "   ) \n" +
                        ")\n" +
                        "WHERE Name NOT LIKE ('ALL') \n" +
                        "ORDER BY Localité NULLS LAST";
                break;
            case NOMBRE_DE_VISITES:
                query = "SELECT SUM(SOMME) AS Total, periode \n" +
                        "        FROM V_STAT_NAMUR\n" +
                        "        WHERE CODE_REF_NO IN (7, 8, 9, 10, 205, 227, 249, 271, 293)\n" +
                        "        AND periode BETWEEN ? AND ?\n" +
                        "        GROUP BY periode\n" +
                        "        ORDER BY periode";
                break;
        }
        return query;
    }
}


