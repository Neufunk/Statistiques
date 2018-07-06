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

    void close(ResultSet rs) {
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

    void close(Statement st) {
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

    void close(PreparedStatement ps) {
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

    void close(Connection conn) {
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

        // VISITES
        NOMBRE_DE_VISITE,
        NOMBRE_DE_VISITE_PAR_FFA,
        NOMBRE_DE_VISITE_PAR_FFB,
        NOMBRE_DE_VISITE_PAR_FFC,

        // PATIENTS
        NOMBRE_DE_PATIENTS,
        NOMBRE_DE_PATIENTS_NOMENCLATURE,
        NOMBRE_DE_PATIENTS_FFA,
        NOMBRE_DE_PATIENTS_FFB,
        NOMBRE_DE_PATIENTS_FFC,
        NOMBRE_DE_PATIENTS_FFC_PALLIA,
        TAUX_PATIENTS_NOMENCLATURE,
        TAUX_PATIENTS_FORFAITS,
        TAUX_PATIENTS_FFA,
        TAUX_PATIENTS_FFB,
        TAUX_PATIENTS_FFC,
        TAUX_ROTATION_PATIENTS,
        TAUX_PATIENTS_MC_ACCORD,
        TAUX_PATIENTS_MC_AUTRES,
        TAUX_PATIENTS_AUTRES_OA,

        // SOINS
        NOMBRE_DE_SOINS,
        NOMBRE_DE_TOILETTES,
        NOMBRE_DE_TOILETTES_NOMENCLATURE,
        NOMBRE_INJECTIONS,
        NOMBRE_PANSEMENTS,
        NOMBRE_SOINS_SPECIFIQUES,
        NOMBRE_CONSULTATIONS_INFI,
        NOMBRE_DE_PILULIERS,
        NOMBRE_DE_SOINS_DIVERS,
        NOMBRE_AUTRES_SOINS,
        NOMBRE_DE_SOINS_PAR_VISITE,
        TAUX_TOILETTES,
        TAUX_TOILETTES_NOMENCLATURE,
        TAUX_INJECTIONS,
        TAUX_PANSEMENTS,
        TAUX_SOINS_DIVERS,
        TAUX_AUTRES_SOINS,

        // TARIFICATION
        TARIFICATION_OA,
        TARIFICATION_NOMENCLATURE,
        TARIFICATION_FORFAITS_ABC,
        TARIFICATION_SOINS_SPECIFIQUES,
        FORFAITS_PALLIATIFS,
        DEPLACEMENTS,
        TICKETS_MODERATEURS,
        SOINS_DIVERS_ET_CONVENTIONS,
        RECETTE_OA_PAR_VISITE,
        RECETTE_OA_PAR_J_PRESTE,
        RECETTE_OA_PAR_J_AVEC_SOINS,
        RECETTE_OA_PAR_J_PRESTE_AVEC_SD_ET_CONVENTIONS,

        // SUIVI PERSONNEL
        J_PRESTES_AVEC_EMSS,
        J_PRESTES_AVEC_EMAS,
        EMSS,
        EMAS,
        TAUX_ADMINISTRATIF,
        TAUX_ADMINISTRATIF_IC,
        RECUPERATIONS,
        SOLDE_CP,
        TAUX_SMG,
        KM_PARCOURUS,
        KM_PARCOURUS_PAR_VISITE



    }

    String setQuery(Query queryName) {
        String query = "";
        switch (queryName) {
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
            case NOMBRE_DE_VISITE:
                query = "SELECT SUM(SOMME) AS Total, periode \n" +
                        "        FROM V_STAT_NAMUR\n" +
                        "        WHERE CODE_REF_NO IN (7, 8, 9, 10, 205, 227, 249, 271, 293)\n" +
                        "        AND periode BETWEEN ? AND ?\n" +
                        "        AND cee_ref_no = ?\n" +
                        "        GROUP BY periode\n" +
                        "        ORDER BY periode";
                break;
            case NOMBRE_DE_VISITE_PAR_FFA:
                query = "SELECT periode, cee_ref_no, total_1/total_2 as Total \n" +
                        "FROM (\n" +
                        "    SELECT periode, cee_ref_no, \n" +
                        "    SUM(Case when code_ref_no in (8, 205) Then somme else 0 end) as total_1,\n" +
                        "    SUM(Case when code_ref_no in (20, 200) Then somme else 0 end) as total_2\n" +
                        "    FROM V_STAT_NAMUR\n" +
                        "    WHERE code_ref_no IN (8, 20, 200, 205)\n" +
                        "    AND periode BETWEEN ? AND ?\n" +
                        "    AND cee_ref_no = ?\n" +
                        "    GROUP BY periode, cee_ref_no\n" +
                        ")\n" +
                        "ORDER BY periode";
                break;
            case NOMBRE_DE_VISITE_PAR_FFB:
                query = "SELECT periode, cee_ref_no, total_1/total_2 as Total \n" +
                        "FROM (\n" +
                        "    SELECT periode, cee_ref_no, \n" +
                        "    SUM(Case when code_ref_no in (9, 227) Then somme else 0 end) as total_1,\n" +
                        "    SUM(Case when code_ref_no in (21, 222) Then somme else 0 end) as total_2\n" +
                        "    FROM V_STAT_NAMUR\n" +
                        "    WHERE code_ref_no IN (9, 21, 222, 227)\n" +
                        "    AND periode BETWEEN ? AND ?\n" +
                        "    AND cee_ref_no = ?\n" +
                        "    GROUP BY periode, cee_ref_no\n" +
                        ")\n" +
                        "ORDER BY periode\n";
                break;
            case NOMBRE_DE_VISITE_PAR_FFC:
                query = "SELECT periode, cee_ref_no, total_1/total_2 as Total \n" +
                        "FROM (\n" +
                        "    SELECT periode, cee_ref_no, \n" +
                        "    SUM(Case when code_ref_no in (10, 249) Then somme else 0 end) as total_1,\n" +
                        "    SUM(Case when code_ref_no in (22, 244) Then somme else 0 end) as total_2\n" +
                        "    FROM V_STAT_NAMUR\n" +
                        "    WHERE code_ref_no IN (10, 22, 244, 249)\n" +
                        "    AND periode BETWEEN ? AND ?\n" +
                        "    AND cee_ref_no = ?\n" +
                        "    GROUP BY periode, cee_ref_no\n" +
                        ")\n" +
                        "ORDER BY periode\n";
                break;
            case NOMBRE_DE_PATIENTS:
                query = "SELECT SUM(SOMME) AS Total, periode, cee_ref_no FROM V_STAT_NAMUR \n" +
                        "WHERE CODE_REF_NO IN (23, 24) " +
                        "AND periode BETWEEN ? AND ?\n" +
                        "AND CEE_REF_NO = ?\n" +
                        "group by periode, cee_ref_no\n" +
                        "order by periode";
                break;
            case NOMBRE_DE_PATIENTS_NOMENCLATURE:
                query = "    SELECT SUM(SOMME) AS Total, periode FROM V_STAT_NAMUR \n" +
                        "    WHERE CODE_REF_NO IN (15) AND periode BETWEEN ? AND ?" +
                        "    AND V_STAT_NAMUR.CEE_REF_NO = ?\n" +
                        "    group by PERIODE\n" +
                        "    order by periode";
                break;
            case NOMBRE_DE_PATIENTS_FFA:
                query = "SELECT SUM(SOMME) AS Total, periode FROM V_STAT_NAMUR \n" +
                        "    WHERE CODE_REF_NO IN (16) AND periode BETWEEN ? AND ? AND V_STAT_NAMUR.CEE_REF_NO = ?\n" +
                        "    group by PERIODE\n" +
                        "    order by periode";
                break;
            case NOMBRE_DE_PATIENTS_FFB:
                query = "SELECT SUM(SOMME) AS Total, periode FROM V_STAT_NAMUR \n" +
                        "    WHERE CODE_REF_NO IN (17) AND periode BETWEEN ? AND ? AND V_STAT_NAMUR.CEE_REF_NO = ?\n" +
                        "    group by PERIODE\n" +
                        "    order by periode";
                break;
            case NOMBRE_DE_PATIENTS_FFC:
                query = "SELECT SUM(SOMME) AS Total, periode FROM V_STAT_NAMUR \n" +
                        "    WHERE CODE_REF_NO IN (18) AND periode BETWEEN ? AND ? AND V_STAT_NAMUR.CEE_REF_NO = ?\n" +
                        "    group by PERIODE\n" +
                        "    order by periode";
                break;
            case NOMBRE_DE_PATIENTS_FFC_PALLIA:
                query = "SELECT SUM(SOMME) AS Total, periode FROM V_STAT_NAMUR \n" +
                        "    WHERE CODE_REF_NO IN (331) AND periode BETWEEN ? AND ? AND V_STAT_NAMUR.CEE_REF_NO = ?\n" +
                        "    group by PERIODE\n" +
                        "    order by periode";
                break;
            case TAUX_PATIENTS_NOMENCLATURE:
                query = "    SELECT periode, cee_ref_no, (total_nbre_nomencl / total_nbre_patients) * 100 as Total \n" +
                        "FROM (\n" +
                        "    SELECT periode, cee_ref_no, \n" +
                        "    SUM(Case when code_ref_no in (15) Then somme else 0 end) as total_nbre_nomencl,\n" +
                        "    SUM(Case when code_ref_no in (23, 24) Then somme else 0 end) as total_nbre_patients\n" +
                        "    FROM V_STAT_NAMUR\n" +
                        "    WHERE code_ref_no IN (15, 23, 24)\n" +
                        "    AND periode BETWEEN ? AND ?\n" +
                        "    AND cee_ref_no = ?\n" +
                        "    GROUP BY periode, cee_ref_no\n" +
                        ")\n" +
                        "ORDER BY periode";
                break;
            case TAUX_PATIENTS_FORFAITS:
                query = "    SELECT periode, cee_ref_no, 100 - ((total_nbre_nomencl / total_nbre_patients) * 100) as Total \n" +
                        "FROM (\n" +
                        "    SELECT periode, cee_ref_no, \n" +
                        "    SUM(Case when code_ref_no in (15) Then somme else 0 end) as total_nbre_nomencl,\n" +
                        "    SUM(Case when code_ref_no in (23, 24) Then somme else 0 end) as total_nbre_patients\n" +
                        "    FROM V_STAT_NAMUR\n" +
                        "    WHERE code_ref_no IN (15, 23, 24)\n" +
                        "    AND periode BETWEEN ? AND ?\n" +
                        "    AND cee_ref_no = ?\n" +
                        "    GROUP BY periode, cee_ref_no\n" +
                        ")\n" +
                        "ORDER BY periode";
                break;
            case TAUX_PATIENTS_FFA:
                query = "    SELECT periode, cee_ref_no, (total_nbre_forfait_a / total_nbre_patients) * 100 as Total \n" +
                        "FROM (\n" +
                        "    SELECT periode, cee_ref_no, \n" +
                        "    SUM(Case when code_ref_no in (16) Then somme else 0 end) as total_nbre_forfait_a,\n" +
                        "    SUM(Case when code_ref_no in (23, 24) Then somme else 0 end) as total_nbre_patients\n" +
                        "    FROM V_STAT_NAMUR\n" +
                        "    WHERE code_ref_no IN (16, 23, 24)\n" +
                        "    AND periode BETWEEN ? AND ?\n" +
                        "    AND cee_ref_no = ?\n" +
                        "    GROUP BY periode, cee_ref_no\n" +
                        ")\n" +
                        "ORDER BY periode";
                break;
            case TAUX_PATIENTS_FFB:
                query = "    SELECT periode, cee_ref_no, (total_nbre_forfait_b / total_nbre_patients) * 100 as Total \n" +
                        "FROM (\n" +
                        "    SELECT periode, cee_ref_no, \n" +
                        "    SUM(Case when code_ref_no in (17) Then somme else 0 end) as total_nbre_forfait_b,\n" +
                        "    SUM(Case when code_ref_no in (23, 24) Then somme else 0 end) as total_nbre_patients\n" +
                        "    FROM V_STAT_NAMUR\n" +
                        "    WHERE code_ref_no IN (17, 23, 24)\n" +
                        "    AND periode BETWEEN ? AND ?\n" +
                        "    AND cee_ref_no = ?\n" +
                        "    GROUP BY periode, cee_ref_no\n" +
                        ")\n" +
                        "ORDER BY periode";
                break;
            case TAUX_PATIENTS_FFC:
                query = "    SELECT periode, cee_ref_no, (total_nbre_forfait_c / total_nbre_patients) * 100 as Total \n" +
                        "FROM (\n" +
                        "    SELECT periode, cee_ref_no, \n" +
                        "    SUM(Case when code_ref_no in (18) Then somme else 0 end) as total_nbre_forfait_c,\n" +
                        "    SUM(Case when code_ref_no in (23, 24) Then somme else 0 end) as total_nbre_patients\n" +
                        "    FROM V_STAT_NAMUR\n" +
                        "    WHERE code_ref_no IN (18, 23, 24)\n" +
                        "    AND periode BETWEEN ? AND ?\n" +
                        "    AND cee_ref_no = ?\n" +
                        "    GROUP BY periode, cee_ref_no\n" +
                        ")\n" +
                        "ORDER BY periode";
                break;
            case TAUX_ROTATION_PATIENTS:
                query = "    SELECT periode, cee_ref_no, (total_nvx_patients / total_nbre_patients) * 100 as Total \n" +
                        "FROM (\n" +
                        "    SELECT periode, cee_ref_no, \n" +
                        "    SUM(Case when code_ref_no in (170) Then somme else 0 end) as total_nvx_patients,\n" +
                        "    SUM(Case when code_ref_no in (23, 24) Then somme else 0 end) as total_nbre_patients\n" +
                        "    FROM V_STAT_NAMUR\n" +
                        "    WHERE code_ref_no IN (170, 23, 24)\n" +
                        "    AND periode BETWEEN ? AND ?\n" +
                        "    AND cee_ref_no = ?\n" +
                        "    GROUP BY periode, cee_ref_no\n" +
                        ")\n" +
                        "ORDER BY periode";
                break;
            case TAUX_PATIENTS_MC_ACCORD:
                query = "    SELECT periode, cee_ref_no, (total_nbre_patients_mc_accord/total_nbre_patients) * 100 as Total \n" +
                        "FROM (\n" +
                        "    SELECT periode, cee_ref_no, \n" +
                        "    SUM(Case when code_ref_no in (137, 138, 139, 140) Then somme else 0 end) as total_nbre_patients_mc_accord,\n" +
                        "    SUM(Case when code_ref_no in (137, 138, 139, 140, 141, 142, 143, 144, 145, 146, 147, 148) Then somme else 0 end) as total_nbre_patients\n" +
                        "    FROM V_STAT_NAMUR\n" +
                        "    WHERE code_ref_no IN (137, 138, 139, 140, 141, 142, 143, 144, 145, 146, 147, 148)\n" +
                        "    AND periode BETWEEN ? AND ?\n" +
                        "    AND cee_ref_no = ?\n" +
                        "    GROUP BY periode, cee_ref_no\n" +
                        ")\n" +
                        "ORDER BY periode";
                break;
            case TAUX_PATIENTS_MC_AUTRES:
                query = "    SELECT periode, cee_ref_no, (total_nbre_patients_mc_autres/total_nbre_patients) * 100 as Total \n" +
                        "FROM (\n" +
                        "    SELECT periode, cee_ref_no, \n" +
                        "    SUM(Case when code_ref_no in (141, 142, 143, 144) Then somme else 0 end) as total_nbre_patients_mc_autres,\n" +
                        "    SUM(Case when code_ref_no in (137, 138, 139, 140, 141, 142, 143, 144, 145, 146, 147, 148) Then somme else 0 end) as total_nbre_patients\n" +
                        "    FROM V_STAT_NAMUR\n" +
                        "    WHERE code_ref_no IN (137, 138, 139, 140, 141, 142, 143, 144, 145, 146, 147, 148)\n" +
                        "    AND periode BETWEEN ? AND ?\n" +
                        "    AND cee_ref_no = ?\n" +
                        "    GROUP BY periode, cee_ref_no\n" +
                        ")\n" +
                        "ORDER BY periode";
                break;
            case TAUX_PATIENTS_AUTRES_OA:
                query = "    SELECT periode, cee_ref_no, (total_nbre_patients_autres_oa/total_nbre_patients) * 100 as Total \n" +
                        "FROM (\n" +
                        "    SELECT periode, cee_ref_no, \n" +
                        "    SUM(Case when code_ref_no in (145, 146, 147, 148) Then somme else 0 end) as total_nbre_patients_autres_oa,\n" +
                        "    SUM(Case when code_ref_no in (137, 138, 139, 140, 141, 142, 143, 144, 145, 146, 147, 148) Then somme else 0 end) as total_nbre_patients\n" +
                        "    FROM V_STAT_NAMUR\n" +
                        "    WHERE code_ref_no IN (137, 138, 139, 140, 141, 142, 143, 144, 145, 146, 147, 148)\n" +
                        "    AND periode BETWEEN ? AND ?\n" +
                        "    AND cee_ref_no = ?\n" +
                        "    GROUP BY periode, cee_ref_no\n" +
                        ")\n" +
                        "ORDER BY periode";
                break;
            // SOINS
            case NOMBRE_DE_SOINS:
                query = "SELECT SUM(SOMME) AS Total, periode FROM V_STAT_NAMUR \n" +
                        "WHERE CODE_REF_NO IN (102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, " +
                        "116, 117, 118, 119, 120, 121, 122, 123, 124, 12, 209, 210, 211, 212, 213, 214, 231, 232, 233, " +
                        "234, 235, 236, 253, 254, 255, 256, 257, 258, 275, 276, 277, 278, 279, 280, 297, 298, 299, 301," +
                        " 302, 330, 354, 355, 356, 357, 358, 360, 361, 362, 363, 364, 365, 366, 367)\n" +
                        "AND periode BETWEEN ? AND ? " +
                        "AND CEE_REF_NO = ?\n" +
                        "group by PERIODE\n" +
                        "order by periode\n";
                break;
            case NOMBRE_DE_TOILETTES:
                query = "    SELECT SUM(SOMME) AS Total, periode FROM V_STAT_NAMUR \n" +
                        "    WHERE CODE_REF_NO IN (12, 107, 113, 119, 209, 231, 253, 275, 297)\n" +
                        "    AND periode BETWEEN ? AND ? AND V_STAT_NAMUR.CEE_REF_NO = ?\n" +
                        "    group by PERIODE\n" +
                        "    order by periode";
                break;
            case NOMBRE_DE_TOILETTES_NOMENCLATURE:
                query = "    SELECT periode, cee_ref_no, (total_1/total_2)*100 as Total \n" +
                        "FROM (\n" +
                        "    SELECT periode, cee_ref_no, \n" +
                        "    SUM(Case when code_ref_no in (12, 297, 275) Then somme else 0 end) as total_1,\n" +
                        "    SUM(Case when code_ref_no in (12, 107, 113, 119, 209, 231, 253, 275, 297) Then somme else 0 end) as total_2\n" +
                        "    FROM V_STAT_NAMUR\n" +
                        "    WHERE code_ref_no IN (12, 107, 113, 119, 209, 231, 253, 275, 297)\n" +
                        "    AND periode BETWEEN ? AND ?\n" +
                        "    AND cee_ref_no = ?\n" +
                        "    GROUP BY periode, cee_ref_no\n" +
                        ")\n" +
                        "ORDER BY periode";
                break;
            case NOMBRE_INJECTIONS:
                query = "SELECT SUM(SOMME) AS Total, periode FROM V_STAT_NAMUR \n" +
                        "WHERE CODE_REF_NO IN (102, 108, 114, 120, 210, 232, 254, 276, 298) AND periode BETWEEN ? AND ? AND V_STAT_NAMUR.CEE_REF_NO = ?\n" +
                        "group by PERIODE\n" +
                        "order by periode";
                break;
            case NOMBRE_PANSEMENTS:
                query = "SELECT SUM(SOMME) AS Total, periode FROM V_STAT_NAMUR \n" +
                        "WHERE CODE_REF_NO IN (103, 109, 115, 121, 211, 233, 255, 277, 299) AND periode BETWEEN ? AND ? AND V_STAT_NAMUR.CEE_REF_NO = ?\n" +
                        "group by PERIODE\n" +
                        "order by periode";
                break;
            case NOMBRE_SOINS_SPECIFIQUES:
                query = "SELECT SUM(SOMME) AS Total, periode FROM V_STAT_NAMUR \n" +
                        "WHERE CODE_REF_NO IN (14) AND periode BETWEEN ? AND ? AND V_STAT_NAMUR.CEE_REF_NO = ?\n" +
                        "group by PERIODE\n" +
                        "order by periode";
                break;
            case NOMBRE_CONSULTATIONS_INFI:
                query = "SELECT SUM(SOMME) AS Total, periode FROM V_STAT_NAMUR \n" +
                        "WHERE CODE_REF_NO IN (354) AND periode BETWEEN ? AND ? AND V_STAT_NAMUR.CEE_REF_NO = ?\n" +
                        "group by PERIODE\n" +
                        "order by periode";
                break;
            case NOMBRE_DE_PILULIERS:
                query = "SELECT SUM(SOMME) AS Total, periode FROM V_STAT_NAMUR \n" +
                        "WHERE CODE_REF_NO IN (365) AND periode BETWEEN ? AND ? AND V_STAT_NAMUR.CEE_REF_NO = ?\n" +
                        "group by PERIODE\n" +
                        "order by periode";
                break;
            case NOMBRE_DE_SOINS_DIVERS:
                query = "SELECT SUM(SOMME) AS Total, periode FROM V_STAT_NAMUR \n" +
                        "WHERE CODE_REF_NO IN (367) AND periode BETWEEN ? AND ? AND V_STAT_NAMUR.CEE_REF_NO = ?\n" +
                        "group by PERIODE\n" +
                        "order by periode";
                break;
            case NOMBRE_AUTRES_SOINS:
                query = "    SELECT periode, cee_ref_no, total_soins - total_toilettes - total_injections - total_pansements - total_soins_specif - total_consult_inf - total_pillulier - total_soins_divers as Total \n" +
                        "FROM (\n" +
                        "    SELECT periode, cee_ref_no, \n" +
                        "    SUM(Case when code_ref_no in (102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122, 123, 124, 12, 209, 210, 211, 212, 213, 214, 231, 232, 233, 234, 235, 236, 253, 254, 255, 256, 257, 258, 275, 276, 277, 278, 279, 280, 297, 298, 299, 301, 302, 330, 354, 355, 356, 357, 358, 360, 361, 362, 363, 364, 365, 366, 367) Then somme else 0 end) as total_soins,\n" +
                        "    SUM(Case when code_ref_no in (12, 107, 113, 119, 209, 231, 253, 275, 297) Then somme else 0 end) as total_toilettes,\n" +
                        "    SUM(Case when code_ref_no in (102, 108, 114, 120, 210, 232, 254, 276, 298) Then somme else 0 end) as total_injections,\n" +
                        "    SUM(Case when code_ref_no in (103, 109, 115, 121, 211, 233, 255, 277, 299) Then somme else 0 end) as total_pansements,\n" +
                        "    SUM(Case when code_ref_no in (14) Then somme else 0 end) as total_soins_specif,\n" +
                        "    SUM(Case when code_ref_no in (354) Then somme else 0 end) as total_consult_inf,\n" +
                        "    SUM(Case when code_ref_no in (365) Then somme else 0 end) as total_pillulier,\n" +
                        "    SUM(Case when code_ref_no in (367) Then somme else 0 end) as total_soins_divers\n" +
                        "    FROM V_STAT_NAMUR\n" +
                        "    WHERE code_ref_no IN (12, 14, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122, 123, 124, 12, 209, 210, 211, 212, 213, 214, 231, 232, 233, 234, 235, 236, 253, 254, 255, 256, 257, 258, 275, 276, 277, 278, 279, 280, 297, 298, 299, 301, 302, 330, 354, 355, 356, 357, 358, 360, 361, 362, 363, 364, 365, 366, 367)\n" +
                        "    AND periode BETWEEN ? AND ?\n" +
                        "    AND cee_ref_no = ?\n" +
                        "    GROUP BY periode, cee_ref_no\n" +
                        ")\n" +
                        "ORDER BY periode";
                break;
            case NOMBRE_DE_SOINS_PAR_VISITE:
                query = "    SELECT periode, cee_ref_no, (total_nbre_soins/total_nbre_visites) as Total \n" +
                        "FROM (\n" +
                        "    SELECT periode, cee_ref_no, \n" +
                        "    SUM(Case when code_ref_no in (7, 8, 9, 10, 205, 227, 249, 271, 293) Then somme else 0 end) as total_nbre_visites,\n" +
                        "    SUM(Case when code_ref_no in (102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122, 123, 124, 12, 209, 210, 211, 212, 213, 214, 231, 232, 233, 234, 235, 236, 253, 254, 255, 256, 257, 258, 275, 276, 277, 278, 279, 280, 297, 298, 299, 301, 302, 330, 354, 355, 356, 357, 358, 360, 361, 362, 363, 364, 365, 366, 367) Then somme else 0 end) as total_nbre_soins\n" +
                        "    FROM V_STAT_NAMUR\n" +
                        "    WHERE code_ref_no IN (7, 8, 9, 10, 205, 227, 249, 271, 293, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122, 123, 124, 12, 209, 210, 211, 212, 213, 214, 231, 232, 233, 234, 235, 236, 253, 254, 255, 256, 257, 258, 275, 276, 277, 278, 279, 280, 297, 298, 299, 301, 302, 330, 354, 355, 356, 357, 358, 360, 361, 362, 363, 364, 365, 366, 367)\n" +
                        "    AND periode BETWEEN ? AND ?\n" +
                        "    AND cee_ref_no = ?\n" +
                        "    GROUP BY periode, cee_ref_no\n" +
                        ")\n" +
                        "ORDER BY periode";
                break;
            case TAUX_TOILETTES:
                query = "    SELECT periode, cee_ref_no, total_1/total_nbre_soins as Total \n" +
                        "FROM (\n" +
                        "    SELECT periode, cee_ref_no, \n" +
                        "    SUM(Case when code_ref_no in (12, 107, 113, 119, 209, 231, 253, 275, 297) Then somme else 0 end) as total_1,\n" +
                        "    SUM(Case when code_ref_no in (102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122, 123, 124, 12, 209, 210, 211, 212, 213, 214, 231, 232, 233, 234, 235, 236, 253, 254, 255, 256, 257, 258, 275, 276, 277, 278, 279, 280, 297, 298, 299, 301, 302, 330, 354, 355, 356, 357, 358, 360, 361, 362, 363, 364, 365, 366, 367) Then somme else 0 end) as total_nbre_soins\n" +
                        "    FROM V_STAT_NAMUR\n" +
                        "    WHERE code_ref_no IN (12, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122, 123, 124, 12, 209, 210, 211, 212, 213, 214, 231, 232, 233, 234, 235, 236, 253, 254, 255, 256, 257, 258, 275, 276, 277, 278, 279, 280, 297, 298, 299, 301, 302, 330, 354, 355, 356, 357, 358, 360, 361, 362, 363, 364, 365, 366, 367)\n" +
                        "    AND periode BETWEEN ? AND ?\n" +
                        "    AND cee_ref_no = ?\n" +
                        "    GROUP BY periode, cee_ref_no\n" +
                        ")\n" +
                        "ORDER BY periode";
                break;
            case TAUX_TOILETTES_NOMENCLATURE:
                query = "    SELECT periode, cee_ref_no, (total_1/total_2)*100 as Total \n" +
                        "FROM (\n" +
                        "    SELECT periode, cee_ref_no, \n" +
                        "    SUM(Case when code_ref_no in (12, 297, 275) Then somme else 0 end) as total_1,\n" +
                        "    SUM(Case when code_ref_no in (12, 107, 113, 119, 209, 231, 253, 275, 297) Then somme else 0 end) as total_2\n" +
                        "    FROM V_STAT_NAMUR\n" +
                        "    WHERE code_ref_no IN (12, 107, 113, 119, 209, 231, 253, 275, 297)\n" +
                        "    AND periode BETWEEN ? AND ?\n" +
                        "    AND cee_ref_no = ?\n" +
                        "    GROUP BY periode, cee_ref_no\n" +
                        ")\n" +
                        "ORDER BY periode";
                break;
            case TAUX_INJECTIONS:
                query = "    SELECT periode, cee_ref_no, (total_nbre_injections/total_nbre_soins) * 100 as Total \n" +
                        "FROM (\n" +
                        "    SELECT periode, cee_ref_no, \n" +
                        "    SUM(Case when code_ref_no in (102, 108, 114, 120, 210, 232, 254, 276, 298) Then somme else 0 end) as total_nbre_injections,\n" +
                        "    SUM(Case when code_ref_no in (102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122, 123, 124, 12, 209, 210, 211, 212, 213, 214, 231, 232, 233, 234, 235, 236, 253, 254, 255, 256, 257, 258, 275, 276, 277, 278, 279, 280, 297, 298, 299, 301, 302, 330, 354, 355, 356, 357, 358, 360, 361, 362, 363, 364, 365, 366, 367) Then somme else 0 end) as total_nbre_soins\n" +
                        "    FROM V_STAT_NAMUR\n" +
                        "    WHERE code_ref_no IN (102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122, 123, 124, 12, 209, 210, 211, 212, 213, 214, 231, 232, 233, 234, 235, 236, 253, 254, 255, 256, 257, 258, 275, 276, 277, 278, 279, 280, 297, 298, 299, 301, 302, 330, 354, 355, 356, 357, 358, 360, 361, 362, 363, 364, 365, 366, 367)\n" +
                        "    AND periode BETWEEN ? AND ?\n" +
                        "    AND cee_ref_no = ?\n" +
                        "    GROUP BY periode, cee_ref_no\n" +
                        ")\n" +
                        "ORDER BY periode";
                break;
            case TAUX_PANSEMENTS:
                query = "    SELECT periode, cee_ref_no, (total_nbre_pansements/total_nbre_soins) * 100 as Total \n" +
                        "FROM (\n" +
                        "    SELECT periode, cee_ref_no, \n" +
                        "    SUM(Case when code_ref_no in (103, 109, 115, 121, 211, 233, 255, 277, 299) Then somme else 0 end) as total_nbre_pansements,\n" +
                        "    SUM(Case when code_ref_no in (102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122, 123, 124, 12, 209, 210, 211, 212, 213, 214, 231, 232, 233, 234, 235, 236, 253, 254, 255, 256, 257, 258, 275, 276, 277, 278, 279, 280, 297, 298, 299, 301, 302, 330, 354, 355, 356, 357, 358, 360, 361, 362, 363, 364, 365, 366, 367) Then somme else 0 end) as total_nbre_soins\n" +
                        "    FROM V_STAT_NAMUR\n" +
                        "    WHERE code_ref_no IN (102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122, 123, 124, 12, 209, 210, 211, 212, 213, 214, 231, 232, 233, 234, 235, 236, 253, 254, 255, 256, 257, 258, 275, 276, 277, 278, 279, 280, 297, 298, 299, 301, 302, 330, 354, 355, 356, 357, 358, 360, 361, 362, 363, 364, 365, 366, 367)\n" +
                        "    AND periode BETWEEN ? AND ?\n" +
                        "    AND cee_ref_no = ?\n" +
                        "    GROUP BY periode, cee_ref_no\n" +
                        ")\n" +
                        "ORDER BY periode";
                break;
            case TAUX_SOINS_DIVERS:
                query = "    SELECT periode, cee_ref_no, (total_nbre_soins_divers/total_nbre_soins) * 100 as Total \n" +
                        "FROM (\n" +
                        "    SELECT periode, cee_ref_no, \n" +
                        "    SUM(Case when code_ref_no in (367) Then somme else 0 end) as total_nbre_soins_divers,\n" +
                        "    SUM(Case when code_ref_no in (102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122, 123, 124, 12, 209, 210, 211, 212, 213, 214, 231, 232, 233, 234, 235, 236, 253, 254, 255, 256, 257, 258, 275, 276, 277, 278, 279, 280, 297, 298, 299, 301, 302, 330, 354, 355, 356, 357, 358, 360, 361, 362, 363, 364, 365, 366, 367) Then somme else 0 end) as total_nbre_soins\n" +
                        "    FROM V_STAT_NAMUR\n" +
                        "    WHERE code_ref_no IN (102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122, 123, 124, 12, 209, 210, 211, 212, 213, 214, 231, 232, 233, 234, 235, 236, 253, 254, 255, 256, 257, 258, 275, 276, 277, 278, 279, 280, 297, 298, 299, 301, 302, 330, 354, 355, 356, 357, 358, 360, 361, 362, 363, 364, 365, 366, 367)\n" +
                        "    AND periode BETWEEN ? AND ?\n" +
                        "    AND cee_ref_no = ?\n" +
                        "    GROUP BY periode, cee_ref_no\n" +
                        ")\n" +
                        "ORDER BY periode";
                break;
            case TAUX_AUTRES_SOINS:
                query = "    SELECT periode, cee_ref_no, ((total_soins - total_toilettes - total_injections - total_pansements - total_soins_specif - total_consult_inf - total_pillulier - total_soins_divers) / total_soins) * 100 as Total \n" +
                        "FROM (\n" +
                        "    SELECT periode, cee_ref_no, \n" +
                        "    SUM(Case when code_ref_no in (102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122, 123, 124, 12, 209, 210, 211, 212, 213, 214, 231, 232, 233, 234, 235, 236, 253, 254, 255, 256, 257, 258, 275, 276, 277, 278, 279, 280, 297, 298, 299, 301, 302, 330, 354, 355, 356, 357, 358, 360, 361, 362, 363, 364, 365, 366, 367) Then somme else 0 end) as total_soins,\n" +
                        "    SUM(Case when code_ref_no in (12, 107, 113, 119, 209, 231, 253, 275, 297) Then somme else 0 end) as total_toilettes,\n" +
                        "    SUM(Case when code_ref_no in (102, 108, 114, 120, 210, 232, 254, 276, 298) Then somme else 0 end) as total_injections,\n" +
                        "    SUM(Case when code_ref_no in (103, 109, 115, 121, 211, 233, 255, 277, 299) Then somme else 0 end) as total_pansements,\n" +
                        "    SUM(Case when code_ref_no in (14) Then somme else 0 end) as total_soins_specif,\n" +
                        "    SUM(Case when code_ref_no in (354) Then somme else 0 end) as total_consult_inf,\n" +
                        "    SUM(Case when code_ref_no in (365) Then somme else 0 end) as total_pillulier,\n" +
                        "    SUM(Case when code_ref_no in (367) Then somme else 0 end) as total_soins_divers\n" +
                        "    FROM V_STAT_NAMUR\n" +
                        "    WHERE code_ref_no IN (12, 14, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122, 123, 124, 12, 209, 210, 211, 212, 213, 214, 231, 232, 233, 234, 235, 236, 253, 254, 255, 256, 257, 258, 275, 276, 277, 278, 279, 280, 297, 298, 299, 301, 302, 330, 354, 355, 356, 357, 358, 360, 361, 362, 363, 364, 365, 366, 367)\n" +
                        "    AND periode BETWEEN ? AND ?\n" +
                        "    AND cee_ref_no = ?\n" +
                        "    GROUP BY periode, cee_ref_no\n" +
                        ")\n" +
                        "ORDER BY periode";
                break;
            //TARIFICATION
            case TARIFICATION_OA:
                query = "SELECT SUM(SOMME) AS Total, periode FROM V_STAT_NAMUR \n" +
                        "WHERE CODE_REF_NO IN (1, 2, 3, 4, 5, 6, 193, 215, 237, 259, 281) AND periode BETWEEN ? AND ? AND V_STAT_NAMUR.CEE_REF_NO = ?\n" +
                        "group by PERIODE\n" +
                        "order by PERIODE";
                break;
            case TARIFICATION_NOMENCLATURE:
                query = "SELECT SUM(SOMME) AS Total, periode FROM V_STAT_NAMUR \n" +
                        "WHERE CODE_REF_NO IN (1) AND periode BETWEEN ? AND ? AND V_STAT_NAMUR.CEE_REF_NO = ?\n" +
                        "group by PERIODE\n" +
                        "order by PERIODE";
                break;
            case TARIFICATION_FORFAITS_ABC:
                query = "SELECT SUM(SOMME) AS Total, periode FROM V_STAT_NAMUR \n" +
                        "WHERE CODE_REF_NO IN (2, 3, 4) AND periode BETWEEN ? AND ? AND V_STAT_NAMUR.CEE_REF_NO = ?\n" +
                        "group by PERIODE\n" +
                        "order by PERIODE";
                break;
            case TARIFICATION_SOINS_SPECIFIQUES:
                query = "SELECT SUM(SOMME) AS Total, periode FROM V_STAT_NAMUR \n" +
                        "WHERE CODE_REF_NO IN (5) AND periode BETWEEN ? AND ? AND V_STAT_NAMUR.CEE_REF_NO = ?\n" +
                        "group by PERIODE\n" +
                        "order by PERIODE";
                break;
            case FORFAITS_PALLIATIFS:
                query = "SELECT SUM(SOMME) AS Total, periode FROM V_STAT_NAMUR \n" +
                        "WHERE CODE_REF_NO IN (193, 215, 237, 259, 281) AND periode BETWEEN ? AND ? AND V_STAT_NAMUR.CEE_REF_NO = ?\n" +
                        "group by PERIODE\n" +
                        "order by PERIODE";
                break;
            case DEPLACEMENTS:
                query = "SELECT SUM(SOMME) AS Total, periode FROM V_STAT_NAMUR \n" +
                        "WHERE CODE_REF_NO IN (6) AND periode BETWEEN ? AND ? AND V_STAT_NAMUR.CEE_REF_NO = ?\n" +
                        "group by PERIODE\n" +
                        "order by PERIODE";
                break;
            case TICKETS_MODERATEURS:
                query = "SELECT SUM(SOMME) AS Total, periode FROM V_STAT_NAMUR \n" +
                        "WHERE CODE_REF_NO IN (11) AND periode BETWEEN ? AND ? AND V_STAT_NAMUR.CEE_REF_NO = ?\n" +
                        "group by PERIODE\n" +
                        "order by PERIODE";
                break;
            case SOINS_DIVERS_ET_CONVENTIONS:
                query = "SELECT SUM(SOMME) AS Total, periode FROM V_STAT_NAMUR \n" +
                        "WHERE CODE_REF_NO IN (99, 373) AND periode BETWEEN ? AND ? AND V_STAT_NAMUR.CEE_REF_NO = ?\n" +
                        "group by PERIODE\n" +
                        "order by PERIODE";
                break;
            case RECETTE_OA_PAR_VISITE:
                query = "SELECT periode, total_1/total_2 as TOTAL \n" +
                        "FROM (\n" +
                        "    SELECT periode, cee_ref_no, \n" +
                        "    SUM(Case when code_ref_no in (1, 2, 3, 4, 5, 6, 193, 215, 237, 259, 281) Then somme else 0 end) as total_1,\n" +
                        "    SUM(Case when code_ref_no in (7, 8, 9, 10, 205, 227, 249, 271, 293) Then somme else 0 end) as total_2\n" +
                        "    FROM V_STAT_NAMUR\n" +
                        "    WHERE code_ref_no IN (1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 193, 205, 215, 227, 237, 249, 259, 271, 281, 293)\n" +
                        "    AND periode BETWEEN ? AND ?\n" +
                        "    AND cee_ref_no = ?\n" +
                        "    GROUP BY periode, cee_ref_no\n" +
                        ")\n" +
                        "ORDER BY periode";
                break;
            case RECETTE_OA_PAR_J_PRESTE:
                query = "";
                break;
            case RECETTE_OA_PAR_J_AVEC_SOINS:
                query = "SELECT PERIODE, CEE_REF_NO, SUM(SOMME) AS TOTAL FROM V_STAT_NAMUR \n" +
                        "WHERE CODE_REF_NO IN (1, 2, 3, 4, 5, 6, 193, 215, 237, 259, 281)\n" +
                        "AND PERIODE BETWEEN ? AND ?\n" +
                        "AND cee_ref_no = ?\n" +
                        "GROUP BY PERIODE, CEE_REF_NO\n" +
                        "ORDER BY PERIODE";
                break;
            case RECETTE_OA_PAR_J_PRESTE_AVEC_SD_ET_CONVENTIONS:
                query = "";
                break;
            case KM_PARCOURUS:
                query="";
                break;
        }
        return query;
    }
}


