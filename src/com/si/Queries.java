package si;

import java.sql.*;

import static si.Queries.Query.*;

class Queries {

    final String[] CATEGORIE = {"TARIFICATION", "VISITES", "PATIENTS", "SOINS", "SUIVI DU PERSONNEL"};

    final Query[][] COMBO_INDICATEUR_ARRAY = {
            // TARIFICATION
            {RECETTE_TOTALE, TARIFICATION_OA, TARIFICATION_NOMENCLATURE, TARIFICATION_FORFAITS_ABC, TARIFICATION_SOINS_SPECIFIQUES, FORFAITS_PALLIATIFS, DEPLACEMENTS,
                    TICKETS_MODERATEURS, SOINS_DIVERS, CONVENTIONS, RECETTE_OA_PAR_VISITE, RECETTE_OA_PAR_J_PRESTE, RECETTE_OA_PAR_J_AVEC_SOINS, RECETTE_TOTALE_PAR_J_AVEC_SOINS},
            // VISITES
            {NOMBRE_DE_VISITES, NOMBRE_DE_VISITES_PAR_FFA, NOMBRE_DE_VISITES_PAR_FFB, NOMBRE_DE_VISITES_PAR_FFC, VISITES_PAR_J_PRESTES, VISITES_PAR_J_AV_SOINS, DUREE_MOYENNE_PAR_VISITE},
            // PATIENTS
            {NOMBRE_DE_PATIENTS, NOMBRE_DE_PATIENTS_FFA, NOMBRE_DE_PATIENTS_FFB, NOMBRE_DE_PATIENTS_FFC, NOMBRE_DE_PATIENTS_PALLIA,
                    TAUX_PATIENTS_NOMENCLATURE, TAUX_PATIENTS_FORFAITS, TAUX_PATIENTS_FFA, TAUX_PATIENTS_FFB, TAUX_PATIENTS_FFC,
                    TAUX_ROTATION_PATIENTS, TAUX_PATIENTS_MC_ACCORD, TAUX_PATIENTS_MC_AUTRES, TAUX_PATIENTS_AUTRES_OA},
            // SOINS
            {NOMBRE_DE_SOINS, NOMBRE_DE_TOILETTES, NOMBRE_DE_TOILETTES_NOMENCLATURE, NOMBRE_INJECTIONS, NOMBRE_PANSEMENTS, NOMBRE_SOINS_SPECIFIQUES,
                    NOMBRE_CONSULTATIONS_INFI, NOMBRE_DE_PILULIERS, NOMBRE_DE_SOINS_DIVERS, NOMBRE_AUTRES_SOINS, NOMBRE_DE_SOINS_PAR_VISITE,
                    TAUX_TOILETTES, TAUX_TOILETTES_NOMENCLATURE, TAUX_INJECTIONS, TAUX_PANSEMENTS, TAUX_SOINS_DIVERS, TAUX_AUTRES_SOINS},
            // SUIVI DU PERSONNEL
            {J_PRESTES_AVEC_EMSS, J_PRESTES_AVEC_EMAS, EMSS, EMAS, TAUX_ADMINISTRATIF, TAUX_ADMINISTRATIF_IC, RECUPERATIONS, SOLDE_CP, TAUX_SMG}
    };

    public enum Query {
        VISITES_PAR_CENTRE,
        VISITES_PAR_LOCALITE,
        PATIENTS_PAR_CENTRE,
        PATIENTS_PAR_LOCALITE,
        PATIENTS_PAR_AGE,

        /****************************************
         INDICATEURS DE GESTION
         ***************************************/
        // VISITES
        NOMBRE_DE_VISITES,
        NOMBRE_DE_VISITES_PAR_FFA,
        NOMBRE_DE_VISITES_PAR_FFB,
        NOMBRE_DE_VISITES_PAR_FFC,
        VISITES_PAR_J_PRESTES,
        VISITES_PAR_J_AV_SOINS,
        DUREE_MOYENNE_PAR_VISITE,

        // PATIENTS
        NOMBRE_DE_PATIENTS,
        NOMBRE_DE_PATIENTS_NOMENCLATURE,
        NOMBRE_DE_PATIENTS_FFA,
        NOMBRE_DE_PATIENTS_FFB,
        NOMBRE_DE_PATIENTS_FFC,
        NOMBRE_DE_PATIENTS_PALLIA,
        TAUX_PATIENTS_NOMENCLATURE,
        TAUX_PATIENTS_FORFAITS,
        TAUX_PATIENTS_FFA,
        TAUX_PATIENTS_FFB,
        TAUX_PATIENTS_FFC,
        TAUX_ROTATION_PATIENTS,
        TAUX_PATIENTS_MC_ACCORD,
        TAUX_PATIENTS_MC_AUTRES,
        TAUX_PATIENTS_AUTRES_OA,
        TAUX_PATIENTS_VIPO,
        TAUX_PATIENTS_NON_VIPO,


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
        RECETTE_TOTALE,
        TARIFICATION_OA,
        TARIFICATION_NOMENCLATURE,
        TARIFICATION_FORFAITS_ABC,
        TARIFICATION_SOINS_SPECIFIQUES,
        FORFAITS_PALLIATIFS,
        DEPLACEMENTS,
        TICKETS_MODERATEURS,
        SOINS_DIVERS,
        CONVENTIONS,
        RECETTE_OA_PAR_VISITE,
        RECETTE_OA_PAR_J_PRESTE,
        RECETTE_OA_PAR_J_AVEC_SOINS,
        RECETTE_OA_PAR_J_PRESTE_AVEC_SD_ET_CONVENTIONS,
        RECETTE_TOTALE_PAR_J_AVEC_SOINS,

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

    String selectQuery(Query queryName) {
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
            case PATIENTS_PAR_AGE:
                query = "WITH AgeData as\n" +
                        "         (\n" +
                        "             SELECT COUNT(fmr.FAY_SEQ_NO), (SYSDATE - BIRTH_DAT)/365.25 AS AGE, fmr.NAME\n" +
                        "             FROM municipalities mun,\n" +
                        "                  streets stt,\n" +
                        "                  fams fay,\n" +
                        "                  fam_membs fmr,\n" +
                        "                  patients pat,\n" +
                        "                  orgs ogn,\n" +
                        "                  org_membs omr,\n" +
                        "                  care_plans cpl\n" +
                        "             WHERE stt.mun_seq_no = mun.seq_no\n" +
                        "               AND stt.seq_no = fay.stt_seq_no\n" +
                        "               AND fay.seq_no = fmr.fay_seq_no\n" +
                        "               AND fmr.fay_seq_no = pat.fmr_fay_seq_no\n" +
                        "               AND fmr.ref_no = pat.fmr_ref_no\n" +
                        "               AND ogn.seq_no = omr.ogn_seq_no\n" +
                        "               AND fmr.fay_seq_no = omr.fmr_fay_seq_no\n" +
                        "               AND fmr.ref_no = omr.fmr_ref_no\n" +
                        "               AND pat.seq_no = cpl.cp_pat_seq_no\n" +
                        "               AND cpl.achiev_dat between to_date(?, 'YYYYMM') and to_date(?, 'YYYYMM')\n" +
                        "               AND fmr.CENTER LIKE ?\n" +
                        "             GROUP BY BIRTH_DAT, fmr.NAME\n" +
                        "         ),\n" +
                        "GroupAge AS (\n" +
                        "SELECT Age, AgeData.NAME,\n" +
                        "       (CASE\n" +
                        "           WHEN AGE < 40 THEN 'Moins de 40'\n" +
                        "           WHEN AGE BETWEEN 39 AND 49 THEN '40 - 49'\n" +
                        "           WHEN AGE BETWEEN 49 AND 59 THEN '50 - 59'\n" +
                        "           WHEN AGE BETWEEN 59 AND 69 THEN '60 - 69'\n" +
                        "           WHEN AGE BETWEEN 69 AND 79 THEN '70 - 79'\n" +
                        "           WHEN AGE BETWEEN 79 AND 89 THEN '80 - 89'\n" +
                        "           WHEN AGE > 89 THEN '90 et Plus'\n" +
                        "           ELSE 'Sans date' END) as Range\n" +
                        "FROM AgeData\n" +
                        "   )\n" +
                        "SELECT GroupAge.Range, COUNT(*) AS Patients\n" +
                        "FROM GroupAge\n" +
                        "GROUP BY Range\n" +
                        "ORDER BY (CASE  Range WHEN 'Sans date' THEN 1 END)DESC, Range ASC\n";
                break;
            case NOMBRE_DE_VISITES:
                query = "SELECT SUM(SOMME) AS Total, periode \n" +
                        "        FROM V_STAT_NAMUR\n" +
                        "        WHERE CODE_REF_NO IN (7, 8, 9, 10, 205, 227, 249, 271, 293)\n" +
                        "        AND periode BETWEEN ? AND ?\n" +
                        "        AND cee_ref_no LIKE ?\n" +
                        "        GROUP BY periode\n" +
                        "        ORDER BY periode";
                break;
            case NOMBRE_DE_VISITES_PAR_FFA:
                query = "SELECT periode, total_1/NULLIF(total_2, 0) as Total \n" +
                        "FROM (\n" +
                        "    SELECT periode, \n" +
                        "    SUM(Case when code_ref_no in (8, 205) Then somme else 0 end) as total_1,\n" +
                        "    SUM(Case when code_ref_no in (20, 200) Then somme else 0 end) as total_2\n" +
                        "    FROM V_STAT_NAMUR\n" +
                        "    WHERE code_ref_no IN (8, 20, 200, 205)\n" +
                        "    AND periode BETWEEN ? AND ?\n" +
                        "    AND cee_ref_no LIKE ?\n" +
                        "    GROUP BY periode \n" +
                        ")\n" +
                        "ORDER BY periode";
                break;
            case NOMBRE_DE_VISITES_PAR_FFB:
                query = "SELECT periode, total_1/NULLIF(total_2, 0) as Total \n" +
                        "FROM (\n" +
                        "    SELECT periode, \n" +
                        "    SUM(Case when code_ref_no in (9, 227) Then somme else 0 end) as total_1,\n" +
                        "    SUM(Case when code_ref_no in (21, 222) Then somme else 0 end) as total_2\n" +
                        "    FROM V_STAT_NAMUR\n" +
                        "    WHERE code_ref_no IN (9, 21, 222, 227)\n" +
                        "    AND periode BETWEEN ? AND ?\n" +
                        "    AND cee_ref_no LIKE ?\n" +
                        "    GROUP BY periode \n" +
                        ")\n" +
                        "ORDER BY periode\n";
                break;
            case NOMBRE_DE_VISITES_PAR_FFC:
                query = "SELECT periode, total_1/NULLIF(total_2, 0) as Total \n" +
                        "FROM (\n" +
                        "    SELECT periode, \n" +
                        "    SUM(Case when code_ref_no in (10, 249) Then somme else 0 end) as total_1,\n" +
                        "    SUM(Case when code_ref_no in (22, 244) Then somme else 0 end) as total_2\n" +
                        "    FROM V_STAT_NAMUR\n" +
                        "    WHERE code_ref_no IN (10, 22, 244, 249)\n" +
                        "    AND periode BETWEEN ? AND ?\n" +
                        "    AND cee_ref_no LIKE ?\n" +
                        "    GROUP BY periode \n" +
                        ")\n" +
                        "ORDER BY periode\n";
                break;
            case VISITES_PAR_J_PRESTES:
                query = "SELECT (x.total / y.total) AS TOTAL\n" +
                        "FROM (SELECT PERIODE, SUM(SOMME) AS TOTAL\n" +
                        "      FROM V_STAT_NAMUR\n" +
                        "      WHERE CODE_REF_NO IN (7, 8, 9, 10, 205, 227, 249, 271, 293)\n" +
                        "        AND periode BETWEEN ? AND ?\n" +
                        "        AND CEE_REF_NO LIKE ?\n" +
                        "      GROUP BY PERIODE\n" +
                        "      ORDER BY periode) x\n" +
                        "         JOIN\n" +
                        "     (SELECT SUM(UREN)/7.6 as TOTAL, MAAND\n" +
                        " FROM HR.WKN_PLANNINGEN wkp, HR.WERKNEMERS w, HR.TAAK_CODES tc, HR.V_KONTRAKTEN vk\n" +
                        "           WHERE wkp.maand BETWEEN ? AND ?\n" +
                        "             AND wkp.werknemer_id = w.werknemer_id\n" +
                        "             AND wkp.taak_id      = tc.taak_id\n" +
                        "             AND w.werknemer_id   = vk.werknemer_id\n" +
                        "             AND vk.functie_id    IN (121, 122, 128)\n" +
                        "             AND wkp.afdeling_id LIKE ?\n" +
                        "             AND wkp.planning_dt BETWEEN hist_start_dt AND nvl(hist_eind_dt, to_date('31-12-2099','dd-mm-yyyy'))\n" +
                        "             AND tc.taak_cd in ('AC', 'AS', 'CO', 'CP', 'CS', 'CT', 'DP', 'DS', 'ECO', 'EMSS', 'EMAS', 'EMRE', 'EQ', 'FS', 'GPA', 'GPS', 'GPSS', 'HMD', 'HOM', 'IS', 'PIS', 'PL', 'PMM', 'PR', 'RO', 'RR', 'RSV', 'SF', 'SH', 'SM', 'SP', 'SS', 'SSV', 'VW', 'VO', 'CS', 'CT', 'DP', 'DS', 'IS', 'PR',  'RSV', 'SH', 'SM', 'SS', 'SSV', 'VM', 'VO', 'TUTO')\n" +
                        "\t\t\tAND tc.taak_cd not in ('GPS', 'GPSS', 'GPW', 'GPWS', 'GPWD')\n" +
                        "            GROUP BY MAAND\n" +
                        "            ORDER BY MAAND) y\n" +
                        "     ON x.PERIODE = y.MAAND ";
                break;
            case VISITES_PAR_J_AV_SOINS:
                query = "SELECT (x.total / y.total) AS TOTAL\n" +
                        "FROM\n" +
                        "    (SELECT PERIODE, SUM(SOMME) AS TOTAL\n" +
                        "      FROM V_STAT_NAMUR\n" +
                        "      WHERE CODE_REF_NO IN (7, 8, 9, 10, 205, 227, 249, 271, 293)\n" +
                        "        AND periode BETWEEN ? AND ?\n" +
                        "        AND CEE_REF_NO LIKE ?\n" +
                        "      GROUP BY PERIODE\n" +
                        "      ORDER BY periode) x\n" +
                        "JOIN\n" +
                        "    (SELECT SUM(UREN)/7.6 as TOTAL, wkp.MAAND\n" +
                        "                        FROM HR.WKN_PLANNINGEN wkp, HR.WERKNEMERS w, HR.TAAK_CODES tc, HR.V_KONTRAKTEN vk\n" +
                        "                        WHERE wkp.maand BETWEEN ? AND ?\n" +
                        "                        AND wkp.werknemer_id = w.werknemer_id\n" +
                        "                        AND wkp.taak_id      = tc.taak_id\n" +
                        "                        AND w.werknemer_id   = vk.werknemer_id\n" +
                        "                        AND vk.functie_id    IN (121, 122, 128)\n" +
                        "                        AND wkp.afdeling_id LIKE ?\n" +
                        "                        AND wkp.planning_dt BETWEEN hist_start_dt AND nvl(hist_eind_dt, to_date('31-12-2099','dd-mm-yyyy'))\n" +
                        "                        AND tc.taak_cd in ('AS', 'EMAS', 'EMRE', 'HOM', 'GPA', 'PIS', 'HMD', 'PMM')\n" +
                        "                        AND tc.taak_cd not in ('GPS', 'GPSS', 'GPW', 'GPWS', 'GPWD')\n" +
                        "                        GROUP BY MAAND\n" +
                        "                        ORDER BY MAAND ASC) y\n" +
                        "ON x.PERIODE = y.MAAND ";
                break;
            case DUREE_MOYENNE_PAR_VISITE:
                query = "SELECT (x.TOTAL/y.TOTAL)*60 as TOTAL\n" +
                        "FROM (\n" +
                        "      SELECT SUM(uren) as TOTAL, MAAND\n" +
                        " FROM HR.WKN_PLANNINGEN wkp, HR.WERKNEMERS w, HR.TAAK_CODES tc, HR.V_KONTRAKTEN vk\n" +
                        "           WHERE wkp.maand BETWEEN ? AND ?\n" +
                        "             AND wkp.werknemer_id = w.werknemer_id\n" +
                        "             AND wkp.taak_id      = tc.taak_id\n" +
                        "             AND w.werknemer_id   = vk.werknemer_id\n" +
                        "             AND vk.functie_id    IN (121, 122, 128)\n" +
                        "             AND wkp.afdeling_id LIKE ?\n" +
                        "             AND wkp.planning_dt BETWEEN hist_start_dt AND nvl(hist_eind_dt, to_date('31-12-2099','dd-mm-yyyy'))\n" +
                        "             AND tc.taak_cd in ('AS', 'EMAS', 'EMRE')\n" +
                        "            AND tc.taak_cd not in ('GPS', 'GPSS', 'GPW', 'GPWS', 'GPWD')\n" +
                        "            GROUP BY MAAND\n" +
                        "            ORDER BY MAAND ASC) x\n" +
                        "         JOIN\n" +
                        "     (SELECT PERIODE, SUM(SOMME) AS TOTAL\n" +
                        "      FROM V_STAT_NAMUR\n" +
                        "      WHERE CODE_REF_NO IN (7, 8, 9, 10, 205, 227, 249, 271, 293)\n" +
                        "        AND periode BETWEEN ? AND ?\n" +
                        "        AND CEE_REF_NO LIKE ?\n" +
                        "      GROUP BY PERIODE\n" +
                        "      ORDER BY periode) y\n" +
                        "ON x.MAAND = y.PERIODE ";
                break;
            case NOMBRE_DE_PATIENTS:
                query = "SELECT SUM(SOMME) AS Total, periode FROM V_STAT_NAMUR \n" +
                        "WHERE CODE_REF_NO IN (23, 24) " +
                        "AND periode BETWEEN ? AND ?\n" +
                        "AND cee_ref_no LIKE ?\n" +
                        "group by periode \n" +
                        "order by periode";
                break;
            case NOMBRE_DE_PATIENTS_NOMENCLATURE:
                query = "    SELECT SUM(SOMME) AS Total, periode FROM V_STAT_NAMUR \n" +
                        "    WHERE CODE_REF_NO IN (15) AND periode BETWEEN ? AND ?" +
                        "    AND V_STAT_NAMUR.cee_ref_no LIKE ?\n" +
                        "    group by PERIODE\n" +
                        "    order by periode";
                break;
            case NOMBRE_DE_PATIENTS_FFA:
                query = "SELECT SUM(SOMME) AS Total, periode FROM V_STAT_NAMUR \n" +
                        "    WHERE CODE_REF_NO IN (16) AND periode BETWEEN ? AND ? AND V_STAT_NAMUR.cee_ref_no LIKE ?\n" +
                        "    group by PERIODE\n" +
                        "    order by periode";
                break;
            case NOMBRE_DE_PATIENTS_FFB:
                query = "SELECT SUM(SOMME) AS Total, periode FROM V_STAT_NAMUR \n" +
                        "    WHERE CODE_REF_NO IN (17) AND periode BETWEEN ? AND ? AND V_STAT_NAMUR.cee_ref_no LIKE ?\n" +
                        "    group by PERIODE\n" +
                        "    order by periode";
                break;
            case NOMBRE_DE_PATIENTS_FFC:
                query = "SELECT SUM(SOMME) AS Total, periode FROM V_STAT_NAMUR \n" +
                        "    WHERE CODE_REF_NO IN (18) AND periode BETWEEN ? AND ? AND V_STAT_NAMUR.cee_ref_no LIKE ?\n" +
                        "    group by PERIODE\n" +
                        "    order by periode";
                break;
            case NOMBRE_DE_PATIENTS_PALLIA:
                query = "SELECT SUM(SOMME) AS Total, periode FROM V_STAT_NAMUR \n" +
                        "    WHERE CODE_REF_NO IN (331) AND periode BETWEEN ? AND ? AND V_STAT_NAMUR.cee_ref_no LIKE ?\n" +
                        "    group by PERIODE\n" +
                        "    order by periode";
                break;
            case TAUX_PATIENTS_NOMENCLATURE:
                query = "    SELECT periode, (total_nbre_nomencl / NULLIF(total_nbre_patients, 0)) * 100 as Total \n" +
                        "FROM (\n" +
                        "    SELECT periode, \n" +
                        "    SUM(Case when code_ref_no in (15) Then somme else 0 end) as total_nbre_nomencl,\n" +
                        "    SUM(Case when code_ref_no in (23, 24) Then somme else 0 end) as total_nbre_patients\n" +
                        "    FROM V_STAT_NAMUR\n" +
                        "    WHERE code_ref_no IN (15, 23, 24)\n" +
                        "    AND periode BETWEEN ? AND ?\n" +
                        "    AND cee_ref_no LIKE ?\n" +
                        "    GROUP BY periode \n" +
                        ")\n" +
                        "ORDER BY periode";
                break;
            case TAUX_PATIENTS_FORFAITS:
                query = "    SELECT periode, 100 - ((total_nbre_nomencl / NULLIF(total_nbre_patients, 0)) * 100) as Total \n" +
                        "FROM (\n" +
                        "    SELECT periode,  \n" +
                        "    SUM(Case when code_ref_no in (15) Then somme else 0 end) as total_nbre_nomencl,\n" +
                        "    SUM(Case when code_ref_no in (23, 24) Then somme else 0 end) as total_nbre_patients\n" +
                        "    FROM V_STAT_NAMUR\n" +
                        "    WHERE code_ref_no IN (15, 23, 24)\n" +
                        "    AND periode BETWEEN ? AND ?\n" +
                        "    AND cee_ref_no LIKE ?\n" +
                        "    GROUP BY periode\n" +
                        ")\n" +
                        "ORDER BY periode";
                break;
            case TAUX_PATIENTS_FFA:
                query = "    SELECT periode, (total_nbre_forfait_a / NULLIF(total_nbre_patients, 0)) * 100 as Total \n" +
                        "FROM (\n" +
                        "    SELECT periode, \n" +
                        "    SUM(Case when code_ref_no in (16) Then somme else 0 end) as total_nbre_forfait_a,\n" +
                        "    SUM(Case when code_ref_no in (23, 24) Then somme else 0 end) as total_nbre_patients\n" +
                        "    FROM V_STAT_NAMUR\n" +
                        "    WHERE code_ref_no IN (16, 23, 24)\n" +
                        "    AND periode BETWEEN ? AND ?\n" +
                        "    AND cee_ref_no LIKE ?\n" +
                        "    GROUP BY periode\n" +
                        ")\n" +
                        "ORDER BY periode";
                break;
            case TAUX_PATIENTS_FFB:
                query = "    SELECT periode, (total_nbre_forfait_b / NULLIF(total_nbre_patients, 0)) * 100 as Total \n" +
                        "FROM (\n" +
                        "    SELECT periode, \n" +
                        "    SUM(Case when code_ref_no in (17) Then somme else 0 end) as total_nbre_forfait_b,\n" +
                        "    SUM(Case when code_ref_no in (23, 24) Then somme else 0 end) as total_nbre_patients\n" +
                        "    FROM V_STAT_NAMUR\n" +
                        "    WHERE code_ref_no IN (17, 23, 24)\n" +
                        "    AND periode BETWEEN ? AND ?\n" +
                        "    AND cee_ref_no LIKE ?\n" +
                        "    GROUP BY periode\n" +
                        ")\n" +
                        "ORDER BY periode";
                break;
            case TAUX_PATIENTS_FFC:
                query = "    SELECT periode, (total_nbre_forfait_c / NULLIF(total_nbre_patients, 0)) * 100 as Total \n" +
                        "FROM (\n" +
                        "    SELECT periode,  \n" +
                        "    SUM(Case when code_ref_no in (18) Then somme else 0 end) as total_nbre_forfait_c,\n" +
                        "    SUM(Case when code_ref_no in (23, 24) Then somme else 0 end) as total_nbre_patients\n" +
                        "    FROM V_STAT_NAMUR\n" +
                        "    WHERE code_ref_no IN (18, 23, 24)\n" +
                        "    AND periode BETWEEN ? AND ?\n" +
                        "    AND cee_ref_no LIKE ?\n" +
                        "    GROUP BY periode\n" +
                        ")\n" +
                        "ORDER BY periode";
                break;
            case TAUX_ROTATION_PATIENTS:
                query = "    SELECT periode, (total_nvx_patients / NULLIF(total_nbre_patients, 0)) * 100 as Total \n" +
                        "FROM (\n" +
                        "    SELECT periode, \n" +
                        "    SUM(Case when code_ref_no in (170) Then somme else 0 end) as total_nvx_patients,\n" +
                        "    SUM(Case when code_ref_no in (23, 24) Then somme else 0 end) as total_nbre_patients\n" +
                        "    FROM V_STAT_NAMUR\n" +
                        "    WHERE code_ref_no IN (170, 23, 24)\n" +
                        "    AND periode BETWEEN ? AND ?\n" +
                        "    AND cee_ref_no LIKE ?\n" +
                        "    GROUP BY periode\n" +
                        ")\n" +
                        "ORDER BY periode";
                break;
            case TAUX_PATIENTS_MC_ACCORD:
                query = "    SELECT periode, (total_nbre_patients_mc_accord/NULLIF(total_nbre_patients, 0)) * 100 as Total \n" +
                        "FROM (\n" +
                        "    SELECT periode, \n" +
                        "    SUM(Case when code_ref_no in (137, 138, 139, 140) Then somme else 0 end) as total_nbre_patients_mc_accord,\n" +
                        "    SUM(Case when code_ref_no in (137, 138, 139, 140, 141, 142, 143, 144, 145, 146, 147, 148) Then somme else 0 end) as total_nbre_patients\n" +
                        "    FROM V_STAT_NAMUR\n" +
                        "    WHERE code_ref_no IN (137, 138, 139, 140, 141, 142, 143, 144, 145, 146, 147, 148)\n" +
                        "    AND periode BETWEEN ? AND ?\n" +
                        "    AND cee_ref_no LIKE ?\n" +
                        "    GROUP BY periode\n" +
                        ")\n" +
                        "ORDER BY periode";
                break;
            case TAUX_PATIENTS_MC_AUTRES:
                query = "    SELECT periode, (total_nbre_patients_mc_autres/NULLIF(total_nbre_patients, 0)) * 100 as Total \n" +
                        "FROM (\n" +
                        "    SELECT periode, \n" +
                        "    SUM(Case when code_ref_no in (141, 142, 143, 144) Then somme else 0 end) as total_nbre_patients_mc_autres,\n" +
                        "    SUM(Case when code_ref_no in (137, 138, 139, 140, 141, 142, 143, 144, 145, 146, 147, 148) Then somme else 0 end) as total_nbre_patients\n" +
                        "    FROM V_STAT_NAMUR\n" +
                        "    WHERE code_ref_no IN (137, 138, 139, 140, 141, 142, 143, 144, 145, 146, 147, 148)\n" +
                        "    AND periode BETWEEN ? AND ?\n" +
                        "    AND cee_ref_no LIKE ?\n" +
                        "    GROUP BY periode\n" +
                        ")\n" +
                        "ORDER BY periode";
                break;
            case TAUX_PATIENTS_AUTRES_OA:
                query = "    SELECT periode, (total_nbre_patients_autres_oa/NULLIF(total_nbre_patients, 0)) * 100 as Total \n" +
                        "FROM (\n" +
                        "    SELECT periode, \n" +
                        "    SUM(Case when code_ref_no in (145, 146, 147, 148) Then somme else 0 end) as total_nbre_patients_autres_oa,\n" +
                        "    SUM(Case when code_ref_no in (137, 138, 139, 140, 141, 142, 143, 144, 145, 146, 147, 148) Then somme else 0 end) as total_nbre_patients\n" +
                        "    FROM V_STAT_NAMUR\n" +
                        "    WHERE code_ref_no IN (137, 138, 139, 140, 141, 142, 143, 144, 145, 146, 147, 148)\n" +
                        "    AND periode BETWEEN ? AND ?\n" +
                        "    AND cee_ref_no LIKE ?\n" +
                        "    GROUP BY periode\n" +
                        ")\n" +
                        "ORDER BY periode";
                break;
            case TAUX_PATIENTS_VIPO:
                query = "SELECT (x.total/(x.total+y.total))*100 as TOTAL\n" +
                        "FROM (SELECT SUM(somme) AS total, PERIODE\n" +
                        "         FROM V_STAT_NAMUR\n" +
                        "         WHERE CODE_REF_NO = 23\n" +
                        "           AND periode BETWEEN ? AND ?\n" +
                        "           AND cee_ref_no LIKE ? GROUP BY PERIODE\n" +
                        "     ) x\n" +
                        "JOIN\n" +
                        "    (SELECT SUM(somme) AS total, PERIODE\n" +
                        "         FROM V_STAT_NAMUR\n" +
                        "         WHERE CODE_REF_NO = 24\n" +
                        "           AND periode BETWEEN ? AND ?\n" +
                        "           AND cee_ref_no LIKE ? GROUP BY PERIODE) y\n" +
                        "ON x.PERIODE = y.PERIODE\n";
                break;
            case TAUX_PATIENTS_NON_VIPO:
                query = "SELECT (y.total/(x.total+y.total))*100 as TOTAL\n" +
                        "FROM (SELECT SUM(somme) AS total, PERIODE\n" +
                        "         FROM V_STAT_NAMUR\n" +
                        "         WHERE CODE_REF_NO = 23\n" +
                        "           AND periode BETWEEN ? AND ?\n" +
                        "           AND cee_ref_no LIKE ? GROUP BY PERIODE\n" +
                        "     ) x\n" +
                        "JOIN\n" +
                        "    (SELECT SUM(somme) AS total, PERIODE\n" +
                        "         FROM V_STAT_NAMUR\n" +
                        "         WHERE CODE_REF_NO = 24\n" +
                        "           AND periode BETWEEN ? AND ?\n" +
                        "           AND cee_ref_no LIKE ? GROUP BY PERIODE) y\n" +
                        "ON x.PERIODE = y.PERIODE\n";
                break;
            // SOINS
            case NOMBRE_DE_SOINS:
                query = "SELECT SUM(SOMME) AS Total, periode FROM V_STAT_NAMUR \n" +
                        "WHERE CODE_REF_NO IN (102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, " +
                        "116, 117, 118, 119, 120, 121, 122, 123, 124, 12, 209, 210, 211, 212, 213, 214, 231, 232, 233, " +
                        "234, 235, 236, 253, 254, 255, 256, 257, 258, 275, 276, 277, 278, 279, 280, 297, 298, 299, 301," +
                        " 302, 330, 354, 355, 356, 357, 358, 360, 361, 362, 363, 364, 365, 366, 367)\n" +
                        "AND periode BETWEEN ? AND ? " +
                        "AND cee_ref_no LIKE ?\n" +
                        "group by PERIODE\n" +
                        "order by periode\n";
                break;
            case NOMBRE_DE_TOILETTES:
                query = "    SELECT SUM(SOMME) AS Total, periode FROM V_STAT_NAMUR \n" +
                        "    WHERE CODE_REF_NO IN (12, 107, 113, 119, 209, 231, 253, 275, 297)\n" +
                        "    AND periode BETWEEN ? AND ? AND V_STAT_NAMUR.cee_ref_no LIKE ?\n" +
                        "    group by PERIODE\n" +
                        "    order by periode";
                break;
            case NOMBRE_DE_TOILETTES_NOMENCLATURE:
                query = "    SELECT periode, (total_1+total_2) as Total \n" +
                        "FROM (\n" +
                        "    SELECT periode, \n" +
                        "    SUM(Case when code_ref_no in (12, 297, 275) Then somme else 0 end) as total_1,\n" +
                        "    SUM(Case when code_ref_no in (12, 107, 113, 119, 209, 231, 253, 275, 297) Then somme else 0 end) as total_2\n" +
                        "    FROM V_STAT_NAMUR\n" +
                        "    WHERE code_ref_no IN (12, 107, 113, 119, 209, 231, 253, 275, 297)\n" +
                        "    AND periode BETWEEN ? AND ?\n" +
                        "    AND cee_ref_no LIKE ?\n" +
                        "    GROUP BY periode \n" +
                        ")\n" +
                        "ORDER BY periode";
                break;
            case NOMBRE_INJECTIONS:
                query = "SELECT SUM(SOMME) AS Total, periode FROM V_STAT_NAMUR \n" +
                        "WHERE CODE_REF_NO IN (102, 108, 114, 120, 210, 232, 254, 276, 298) AND periode BETWEEN ? AND ? AND V_STAT_NAMUR.cee_ref_no LIKE ?\n" +
                        "group by PERIODE\n" +
                        "order by periode";
                break;
            case NOMBRE_PANSEMENTS:
                query = "SELECT SUM(SOMME) AS Total, periode FROM V_STAT_NAMUR \n" +
                        "WHERE CODE_REF_NO IN (103, 109, 115, 121, 211, 233, 255, 277, 299) AND periode BETWEEN ? AND ? AND V_STAT_NAMUR.cee_ref_no LIKE ?\n" +
                        "group by PERIODE\n" +
                        "order by periode";
                break;
            case NOMBRE_SOINS_SPECIFIQUES:
                query = "SELECT SUM(SOMME) AS Total, periode FROM V_STAT_NAMUR \n" +
                        "WHERE CODE_REF_NO IN (14) AND periode BETWEEN ? AND ? AND V_STAT_NAMUR.cee_ref_no LIKE ?\n" +
                        "group by PERIODE\n" +
                        "order by periode";
                break;
            case NOMBRE_CONSULTATIONS_INFI:
                query = "SELECT SUM(SOMME) AS Total, periode FROM V_STAT_NAMUR \n" +
                        "WHERE CODE_REF_NO IN (354) AND periode BETWEEN ? AND ? AND V_STAT_NAMUR.cee_ref_no LIKE ?\n" +
                        "group by PERIODE\n" +
                        "order by periode";
                break;
            case NOMBRE_DE_PILULIERS:
                query = "SELECT SUM(SOMME) AS Total, periode FROM V_STAT_NAMUR \n" +
                        "WHERE CODE_REF_NO IN (365) AND periode BETWEEN ? AND ? AND V_STAT_NAMUR.cee_ref_no LIKE ?\n" +
                        "group by PERIODE\n" +
                        "order by periode";
                break;
            case NOMBRE_DE_SOINS_DIVERS:
                query = "SELECT SUM(SOMME) AS Total, periode FROM V_STAT_NAMUR \n" +
                        "WHERE CODE_REF_NO IN (367) AND periode BETWEEN ? AND ? AND V_STAT_NAMUR.cee_ref_no LIKE ?\n" +
                        "group by PERIODE\n" +
                        "order by periode";
                break;
            case NOMBRE_AUTRES_SOINS:
                query = "    SELECT periode, total_soins - total_toilettes - total_injections - total_pansements - total_soins_specif - total_consult_inf - total_pillulier - total_soins_divers as Total \n" +
                        "FROM (\n" +
                        "    SELECT periode, \n" +
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
                        "    AND cee_ref_no LIKE ?\n" +
                        "    GROUP BY periode\n" +
                        ")\n" +
                        "ORDER BY periode";
                break;
            case NOMBRE_DE_SOINS_PAR_VISITE:
                query = "    SELECT periode, (total_nbre_soins/NULLIF(total_nbre_visites, 0)) as Total \n" +
                        "FROM (\n" +
                        "    SELECT periode, \n" +
                        "    SUM(Case when code_ref_no in (7, 8, 9, 10, 205, 227, 249, 271, 293) Then somme else 0 end) as total_nbre_visites,\n" +
                        "    SUM(Case when code_ref_no in (102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122, 123, 124, 12, 209, 210, 211, 212, 213, 214, 231, 232, 233, 234, 235, 236, 253, 254, 255, 256, 257, 258, 275, 276, 277, 278, 279, 280, 297, 298, 299, 301, 302, 330, 354, 355, 356, 357, 358, 360, 361, 362, 363, 364, 365, 366, 367) Then somme else 0 end) as total_nbre_soins\n" +
                        "    FROM V_STAT_NAMUR\n" +
                        "    WHERE code_ref_no IN (7, 8, 9, 10, 205, 227, 249, 271, 293, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122, 123, 124, 12, 209, 210, 211, 212, 213, 214, 231, 232, 233, 234, 235, 236, 253, 254, 255, 256, 257, 258, 275, 276, 277, 278, 279, 280, 297, 298, 299, 301, 302, 330, 354, 355, 356, 357, 358, 360, 361, 362, 363, 364, 365, 366, 367)\n" +
                        "    AND periode BETWEEN ? AND ?\n" +
                        "    AND cee_ref_no LIKE ?\n" +
                        "    GROUP BY periode\n" +
                        ")\n" +
                        "ORDER BY periode";
                break;
            case TAUX_TOILETTES:
                query = "    SELECT periode,(total_1/NULLIF(total_nbre_soins, 0))*100 as Total \n" +
                        "FROM (\n" +
                        "    SELECT periode, \n" +
                        "    SUM(Case when code_ref_no in (12, 107, 113, 119, 209, 231, 253, 275, 297) Then somme else 0 end) as total_1,\n" +
                        "    SUM(Case when code_ref_no in (102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122, 123, 124, 12, 209, 210, 211, 212, 213, 214, 231, 232, 233, 234, 235, 236, 253, 254, 255, 256, 257, 258, 275, 276, 277, 278, 279, 280, 297, 298, 299, 301, 302, 330, 354, 355, 356, 357, 358, 360, 361, 362, 363, 364, 365, 366, 367) Then somme else 0 end) as total_nbre_soins\n" +
                        "    FROM V_STAT_NAMUR\n" +
                        "    WHERE code_ref_no IN (12, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122, 123, 124, 12, 209, 210, 211, 212, 213, 214, 231, 232, 233, 234, 235, 236, 253, 254, 255, 256, 257, 258, 275, 276, 277, 278, 279, 280, 297, 298, 299, 301, 302, 330, 354, 355, 356, 357, 358, 360, 361, 362, 363, 364, 365, 366, 367)\n" +
                        "    AND periode BETWEEN ? AND ?\n" +
                        "    AND cee_ref_no LIKE ?\n" +
                        "    GROUP BY periode\n" +
                        ")\n" +
                        "ORDER BY periode";
                break;
            case TAUX_TOILETTES_NOMENCLATURE:
                query = "    SELECT periode, (total_1/NULLIF(total_2, 0))*100 as Total \n" +
                        "FROM (\n" +
                        "    SELECT periode, \n" +
                        "    SUM(Case when code_ref_no in (12, 297, 275) Then somme else 0 end) as total_1,\n" +
                        "    SUM(Case when code_ref_no in (12, 107, 113, 119, 209, 231, 253, 275, 297) Then somme else 0 end) as total_2\n" +
                        "    FROM V_STAT_NAMUR\n" +
                        "    WHERE code_ref_no IN (12, 107, 113, 119, 209, 231, 253, 275, 297)\n" +
                        "    AND periode BETWEEN ? AND ?\n" +
                        "    AND cee_ref_no LIKE ?\n" +
                        "    GROUP BY periode\n" +
                        ")\n" +
                        "ORDER BY periode";
                break;
            case TAUX_INJECTIONS:
                query = "    SELECT periode, (total_nbre_injections/NULLIF(total_nbre_soins, 0)) * 100 as Total \n" +
                        "FROM (\n" +
                        "    SELECT periode, \n" +
                        "    SUM(Case when code_ref_no in (102, 108, 114, 120, 210, 232, 254, 276, 298) Then somme else 0 end) as total_nbre_injections,\n" +
                        "    SUM(Case when code_ref_no in (102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122, 123, 124, 12, 209, 210, 211, 212, 213, 214, 231, 232, 233, 234, 235, 236, 253, 254, 255, 256, 257, 258, 275, 276, 277, 278, 279, 280, 297, 298, 299, 301, 302, 330, 354, 355, 356, 357, 358, 360, 361, 362, 363, 364, 365, 366, 367) Then somme else 0 end) as total_nbre_soins\n" +
                        "    FROM V_STAT_NAMUR\n" +
                        "    WHERE code_ref_no IN (102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122, 123, 124, 12, 209, 210, 211, 212, 213, 214, 231, 232, 233, 234, 235, 236, 253, 254, 255, 256, 257, 258, 275, 276, 277, 278, 279, 280, 297, 298, 299, 301, 302, 330, 354, 355, 356, 357, 358, 360, 361, 362, 363, 364, 365, 366, 367)\n" +
                        "    AND periode BETWEEN ? AND ?\n" +
                        "    AND cee_ref_no LIKE ?\n" +
                        "    GROUP BY periode\n" +
                        ")\n" +
                        "ORDER BY periode";
                break;
            case TAUX_PANSEMENTS:
                query = "    SELECT periode, (total_nbre_pansements/NULLIF(total_nbre_soins, 0)) * 100 as Total \n" +
                        "FROM (\n" +
                        "    SELECT periode, \n" +
                        "    SUM(Case when code_ref_no in (103, 109, 115, 121, 211, 233, 255, 277, 299) Then somme else 0 end) as total_nbre_pansements,\n" +
                        "    SUM(Case when code_ref_no in (102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122, 123, 124, 12, 209, 210, 211, 212, 213, 214, 231, 232, 233, 234, 235, 236, 253, 254, 255, 256, 257, 258, 275, 276, 277, 278, 279, 280, 297, 298, 299, 301, 302, 330, 354, 355, 356, 357, 358, 360, 361, 362, 363, 364, 365, 366, 367) Then somme else 0 end) as total_nbre_soins\n" +
                        "    FROM V_STAT_NAMUR\n" +
                        "    WHERE code_ref_no IN (102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122, 123, 124, 12, 209, 210, 211, 212, 213, 214, 231, 232, 233, 234, 235, 236, 253, 254, 255, 256, 257, 258, 275, 276, 277, 278, 279, 280, 297, 298, 299, 301, 302, 330, 354, 355, 356, 357, 358, 360, 361, 362, 363, 364, 365, 366, 367)\n" +
                        "    AND periode BETWEEN ? AND ?\n" +
                        "    AND cee_ref_no LIKE ?\n" +
                        "    GROUP BY periode\n" +
                        ")\n" +
                        "ORDER BY periode";
                break;
            case TAUX_SOINS_DIVERS:
                query = "    SELECT periode, (total_nbre_soins_divers/NULLIF(total_nbre_soins,0)) * 100 as Total \n" +
                        "FROM (\n" +
                        "    SELECT periode, \n" +
                        "    SUM(Case when code_ref_no in (367) Then somme else 0 end) as total_nbre_soins_divers,\n" +
                        "    SUM(Case when code_ref_no in (102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122, 123, 124, 12, 209, 210, 211, 212, 213, 214, 231, 232, 233, 234, 235, 236, 253, 254, 255, 256, 257, 258, 275, 276, 277, 278, 279, 280, 297, 298, 299, 301, 302, 330, 354, 355, 356, 357, 358, 360, 361, 362, 363, 364, 365, 366, 367) Then somme else 0 end) as total_nbre_soins\n" +
                        "    FROM V_STAT_NAMUR\n" +
                        "    WHERE code_ref_no IN (102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122, 123, 124, 12, 209, 210, 211, 212, 213, 214, 231, 232, 233, 234, 235, 236, 253, 254, 255, 256, 257, 258, 275, 276, 277, 278, 279, 280, 297, 298, 299, 301, 302, 330, 354, 355, 356, 357, 358, 360, 361, 362, 363, 364, 365, 366, 367)\n" +
                        "    AND periode BETWEEN ? AND ?\n" +
                        "    AND cee_ref_no LIKE ?\n" +
                        "    GROUP BY periode\n" +
                        ")\n" +
                        "ORDER BY periode";
                break;
            case TAUX_AUTRES_SOINS:
                query = "    SELECT periode, ((total_soins - total_toilettes - total_injections - total_pansements - total_soins_specif - total_consult_inf - total_pillulier - total_soins_divers) / NULLIF(total_soins, 0)) * 100 as Total \n" +
                        "FROM (\n" +
                        "    SELECT periode, \n" +
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
                        "    AND cee_ref_no LIKE ?\n" +
                        "    GROUP BY periode\n" +
                        ")\n" +
                        "ORDER BY periode";
                break;
            //TARIFICATION
            case RECETTE_TOTALE:
                query = "SELECT x.TOTAL + y.TOTAL as TOTAL, PERIODE\n" +
                        "         FROM (\n" +
                        "                  SELECT SUM(SOMME) AS Total, TO_DATE(periode, 'yyyymm') as PERIODE\n" +
                        "                  FROM V_STAT_NAMUR\n" +
                        "                  WHERE CODE_REF_NO IN (1, 2, 3, 4, 5, 6, 11, 193, 215, 237, 259, 281, 373)\n" +
                        "                    AND periode BETWEEN ? AND ?\n" +
                        "                    AND V_STAT_NAMUR.cee_ref_no LIKE ?\n" +
                        "                  group by PERIODE\n" +
                        "                  order by PERIODE) x\n" +
                        "                  JOIN\n" +
                        "              (SELECT SUM(VAR_AMOUNT) as TOTAL, VAR_MONTH\n" +
                        "               FROM CONV_HC.INVOICE_CONVENTIONALS_V\n" +
                        "               WHERE VAR_MONTH BETWEEN TO_DATE(?, 'yyyymm') AND TO_DATE(?, 'yyyymm')\n" +
                        "                 AND CENTER LIKE ?" +
                        "                   GROUP BY VAR_MONTH) y\n" +
                        "              ON x.PERIODE = y.VAR_MONTH " +
                        "ORDER BY PERIODE";
                break;
            case TARIFICATION_OA:
                query = "SELECT SUM(SOMME) AS Total, periode FROM V_STAT_NAMUR \n" +
                        "WHERE CODE_REF_NO IN (1, 2, 3, 4, 5, 6, 193, 215, 237, 259, 281) AND periode BETWEEN ? AND ? AND V_STAT_NAMUR.cee_ref_no LIKE ?\n" +
                        "group by PERIODE\n" +
                        "order by PERIODE";
                break;
            case TARIFICATION_NOMENCLATURE:
                query = "SELECT SUM(SOMME) AS Total, periode FROM V_STAT_NAMUR \n" +
                        "WHERE CODE_REF_NO IN (1) AND periode BETWEEN ? AND ? AND V_STAT_NAMUR.cee_ref_no LIKE ?\n" +
                        "group by PERIODE\n" +
                        "order by PERIODE";
                break;
            case TARIFICATION_FORFAITS_ABC:
                query = "SELECT SUM(SOMME) AS Total, periode FROM V_STAT_NAMUR \n" +
                        "WHERE CODE_REF_NO IN (2, 3, 4) AND periode BETWEEN ? AND ? AND V_STAT_NAMUR.cee_ref_no LIKE ?\n" +
                        "group by PERIODE\n" +
                        "order by PERIODE";
                break;
            case TARIFICATION_SOINS_SPECIFIQUES:
                query = "SELECT SUM(SOMME) AS Total, periode FROM V_STAT_NAMUR \n" +
                        "WHERE CODE_REF_NO IN (5) AND periode BETWEEN ? AND ? AND V_STAT_NAMUR.cee_ref_no LIKE ?\n" +
                        "group by PERIODE\n" +
                        "order by PERIODE";
                break;
            case FORFAITS_PALLIATIFS:
                query = "SELECT SUM(SOMME) AS Total, periode FROM V_STAT_NAMUR \n" +
                        "WHERE CODE_REF_NO IN (193, 215, 237, 259, 281) AND periode BETWEEN ? AND ? AND V_STAT_NAMUR.cee_ref_no LIKE ?\n" +
                        "group by PERIODE\n" +
                        "order by PERIODE";
                break;
            case DEPLACEMENTS:
                query = "SELECT SUM(SOMME) AS Total, periode FROM V_STAT_NAMUR \n" +
                        "WHERE CODE_REF_NO IN (6) AND periode BETWEEN ? AND ? AND V_STAT_NAMUR.cee_ref_no LIKE ?\n" +
                        "group by PERIODE\n" +
                        "order by PERIODE";
                break;
            case TICKETS_MODERATEURS:
                query = "SELECT SUM(SOMME) AS Total, periode FROM V_STAT_NAMUR \n" +
                        "WHERE CODE_REF_NO IN (11) AND periode BETWEEN ? AND ? AND V_STAT_NAMUR.cee_ref_no LIKE ?\n" +
                        "group by PERIODE\n" +
                        "order by PERIODE";
                break;
            case SOINS_DIVERS:
                query = "SELECT SUM(SOMME) AS Total, periode FROM V_STAT_NAMUR \n" +
                        "WHERE CODE_REF_NO IN (99, 373) AND periode BETWEEN ? AND ? AND V_STAT_NAMUR.cee_ref_no LIKE ?\n" +
                        "group by PERIODE\n" +
                        "order by PERIODE";
                break;
            case CONVENTIONS:
                query = "SELECT SUM(VAR_AMOUNT) as TOTAL, VAR_MONTH\n" +
                        "FROM CONV_HC.INVOICE_CONVENTIONALS_V\n" +
                        "WHERE VAR_MONTH BETWEEN TO_DATE(?, 'yyyymm') AND TO_DATE(?, 'yyyymm')\n" +
                        "AND CENTER LIKE ?\n" +
                        "GROUP BY VAR_MONTH\n" +
                        "ORDER BY VAR_MONTH ";
                break;
            case RECETTE_OA_PAR_VISITE:
                query = "SELECT x.total_1/y.total_2 as TOTAL\n" +
                        "                         FROM (    \n" +
                        "                             SELECT SUM(somme) as total_1, PERIODE  \n" +
                        "                             FROM V_STAT_NAMUR   \n" +
                        "                             WHERE CODE_REF_NO IN (1, 2, 3, 4, 5, 6, 193, 215, 237, 259, 281)     \n" +
                        "                             AND periode BETWEEN ? AND ? \n" +
                        "                             AND V_STAT_NAMUR.cee_ref_no LIKE ? group by PERIODE    \n" +
                        "                             ) x  \n" +
                        "                         JOIN  \n" +
                        "                             (    \n" +
                        "                               SELECT SUM(somme) as total_2, PERIODE\n" +
                        "                               FROM V_STAT_NAMUR\n" +
                        "                               WHERE V_STAT_NAMUR.CODE_REF_NO IN (7, 8, 9, 10, 205, 227, 249, 271, 293)\n" +
                        "                               AND periode BETWEEN ? AND ? \n" +
                        "                               AND V_STAT_NAMUR.cee_ref_no LIKE ? group by PERIODE\n" +
                        "                             ) y     \n" +
                        "                             ON x.PERIODE = y.PERIODE\n" +
                        "ORDER BY x.PERIODE";
                break;
            case RECETTE_OA_PAR_J_PRESTE: // TARIF.OA/JOURS PRESTES+EMSS
                query = "SELECT x.total/y.total as TOTAL\n" +
                        "FROM (\n" +
                        "    SELECT SUM(SOMME) AS TOTAL, PERIODE\n" +
                        "    FROM V_STAT_NAMUR\n" +
                        "    WHERE CODE_REF_NO IN (1, 2, 3, 4, 5, 6, 193, 215, 237, 259, 281) \n" +
                        "    AND periode BETWEEN ? AND ?\n" +
                        "    AND V_STAT_NAMUR.cee_ref_no LIKE ?\n" +
                        "    GROUP BY PERIODE\n" +
                        "    ) x\n" +
                        "JOIN\n" +
                        "    (\n" +
                        "     SELECT SUM(UREN)/7.6 as TOTAL, wkp.MAAND\n" +
                        " FROM HR.WKN_PLANNINGEN wkp, HR.WERKNEMERS w, HR.TAAK_CODES tc, HR.V_KONTRAKTEN vk\n" +
                        "           WHERE wkp.maand BETWEEN ? AND ?\n" +
                        "             AND wkp.werknemer_id = w.werknemer_id\n" +
                        "             AND wkp.taak_id      = tc.taak_id\n" +
                        "             AND w.werknemer_id   = vk.werknemer_id\n" +
                        "             AND vk.functie_id    IN (121, 122, 128)\n" +
                        "             AND wkp.afdeling_id LIKE ?\n" +
                        "             AND wkp.planning_dt BETWEEN hist_start_dt AND nvl(hist_eind_dt, to_date('31-12-2099','dd-mm-yyyy'))\n" +
                        "             AND tc.taak_cd in ('AC', 'AS', 'CO', 'CP', 'CS', 'CT', 'DP', 'DS', 'ECO', 'EMSS', 'EMAS', 'EMRE', 'EQ', 'FS', 'GPA', 'GPS', 'GPSS', 'HMD', 'HOM', 'IS', 'PIS', 'PL', 'PMM', 'PR', 'RO', 'RR', 'RSV', 'SF', 'SH', 'SM', 'SP', 'SS', 'SSV', 'VW', 'VO', 'CS', 'CT', 'DP', 'DS', 'IS', 'PR',  'RSV', 'SH', 'SM', 'SS', 'SSV', 'VM', 'VO', 'TUTO')\n" +
                        "\t\t\tAND tc.taak_cd not in ('GPS', 'GPSS', 'GPW', 'GPWS', 'GPWD')\n" +
                        "            GROUP BY wkp.MAAND\n" +
                        "            ORDER BY MAAND ASC\n" +
                        "    ) y \n" +
                        "    ON x.PERIODE = y.MAAND\n" +
                        "    ORDER BY PERIODE";
                break;
            case RECETTE_OA_PAR_J_AVEC_SOINS:
                query = "SELECT x.total/y.total as TOTAL, PERIODE\n" +
                        "FROM (\n" +
                        "    SELECT SUM(SOMME) AS TOTAL, PERIODE\n" +
                        "    FROM V_STAT_NAMUR\n" +
                        "    WHERE CODE_REF_NO IN (1, 2, 3, 4, 5, 6, 193, 215, 237, 259, 281) \n" +
                        "    AND periode BETWEEN ? AND ?\n" +
                        "    AND V_STAT_NAMUR.cee_ref_no LIKE ?\n" +
                        "    GROUP BY PERIODE\n" +
                        "    ) x\n" +
                        "JOIN\n" +
                        "    (\n" +
                        "      SELECT SUM(uren)/7.6 as TOTAL, MAAND\n" +
                        " FROM HR.WKN_PLANNINGEN wkp, HR.WERKNEMERS w, HR.TAAK_CODES tc, HR.V_KONTRAKTEN vk\n" +
                        "           WHERE wkp.maand BETWEEN ? AND ?\n" +
                        "             AND wkp.werknemer_id = w.werknemer_id\n" +
                        "             AND wkp.taak_id      = tc.taak_id\n" +
                        "             AND w.werknemer_id   = vk.werknemer_id\n" +
                        "             AND vk.functie_id    IN (121, 122, 128)\n" +
                        "             AND wkp.afdeling_id LIKE ?\n" +
                        "             AND wkp.planning_dt BETWEEN hist_start_dt AND nvl(hist_eind_dt, to_date('31-12-2099','dd-mm-yyyy'))\n" +
                        "             AND tc.taak_cd in ('AS', 'EMAS', 'EMRE', 'GPA', 'HMD', 'HOM', 'PIS', 'PMM')\n" +
                        "            AND tc.taak_cd not in ('GPS', 'GPSS', 'GPW', 'GPWS', 'GPWD')\n" +
                        "            GROUP BY MAAND\n" +
                        "            ORDER BY MAAND ASC\n" +
                        "    ) y \n" +
                        "    ON x.PERIODE = y.MAAND\n" +
                        "    ORDER BY PERIODE";
                break;
            case RECETTE_OA_PAR_J_PRESTE_AVEC_SD_ET_CONVENTIONS:
                query = "";
                break;
            case J_PRESTES_AVEC_EMSS:
                query = " SELECT SUM(UREN)/7.6 as TOTAL, wkp.MAAND\n" +
                        " FROM HR.WKN_PLANNINGEN wkp, HR.WERKNEMERS w, HR.TAAK_CODES tc, HR.V_KONTRAKTEN vk\n" +
                        "           WHERE wkp.maand BETWEEN ? AND ?\n" +
                        "             AND wkp.werknemer_id = w.werknemer_id\n" +
                        "             AND wkp.taak_id      = tc.taak_id\n" +
                        "             AND w.werknemer_id   = vk.werknemer_id\n" +
                        "             AND vk.functie_id    IN (121, 122, 128)\n" +
                        "             AND wkp.afdeling_id LIKE ?\n" +
                        "             AND wkp.planning_dt BETWEEN hist_start_dt AND nvl(hist_eind_dt, to_date('31-12-2099','dd-mm-yyyy'))\n" +
                        "             AND tc.taak_cd in ('AC', 'AS', 'CO', 'CP', 'CS', 'CT', 'DP', 'DS', 'ECO', 'EMSS', 'EMAS', 'EMRE', 'EQ', 'FS', 'GPA', 'GPS', 'GPSS', 'HMD', 'HOM', 'IS', 'PIS', 'PL', 'PMM', 'PR', 'RO', 'RR', 'RSV', 'SF', 'SH', 'SM', 'SP', 'SS', 'SSV', 'VW', 'VO', 'CS', 'CT', 'DP', 'DS', 'IS', 'PR',  'RSV', 'SH', 'SM', 'SS', 'SSV', 'VM', 'VO', 'TUTO')\n" +
                        "\t\t\tAND tc.taak_cd not in ('GPS', 'GPSS', 'GPW', 'GPWS', 'GPWD')\n" +
                        "            GROUP BY wkp.MAAND\n" +
                        "            ORDER BY wkp.MAAND";
                break;
            case RECETTE_TOTALE_PAR_J_AVEC_SOINS:
                query = "SELECT a.TOTAL / b.TOTAL AS TOTAL \n" +
                        "FROM (\n" +
                        "         SELECT x.TOTAL + y.TOTAL as TOTAL, PERIODE\n" +
                        "         FROM (\n" +
                        "                  SELECT SUM(SOMME) AS Total, TO_DATE(periode, 'yyyymm') as PERIODE\n" +
                        "                  FROM V_STAT_NAMUR\n" +
                        "                  WHERE CODE_REF_NO IN (1, 2, 3, 4, 5, 6, 193, 215, 237, 259, 281, 11, 99, 373)\n" +
                        "                    AND periode BETWEEN ? AND ?\n" +
                        "                    AND V_STAT_NAMUR.cee_ref_no LIKE ?\n" +
                        "                  group by PERIODE\n" +
                        "                  order by PERIODE) x\n" +
                        "                  JOIN\n" +
                        "              (SELECT SUM(NVL(VAR_AMOUNT, 0)) as TOTAL, VAR_MONTH\n" +
                        "               FROM CONV_HC.INVOICE_CONVENTIONALS_V\n" +
                        "               WHERE VAR_MONTH BETWEEN TO_DATE(?, 'yyyymm') AND TO_DATE(?, 'yyyymm')\n" +
                        "                 AND CENTER LIKE ?\n" +
                        "                  GROUP BY VAR_MONTH) y\n" +
                        "              ON x.PERIODE = y.VAR_MONTH\n" +
                        "     ) a\n" +
                        "JOIN\n" +
                        "     (SELECT SUM(uren)/7.6 as TOTAL, TO_DATE(MAAND, 'yyyymm') AS MAAND\n" +
                        " FROM HR.WKN_PLANNINGEN wkp, HR.WERKNEMERS w, HR.TAAK_CODES tc, HR.V_KONTRAKTEN vk\n" +
                        "           WHERE wkp.maand BETWEEN ? AND ?\n" +
                        "             AND wkp.werknemer_id = w.werknemer_id\n" +
                        "             AND wkp.taak_id      = tc.taak_id\n" +
                        "             AND w.werknemer_id   = vk.werknemer_id\n" +
                        "             AND vk.functie_id    IN (121, 122, 128)\n" +
                        "             AND wkp.afdeling_id LIKE ?\n" +
                        "             AND wkp.planning_dt BETWEEN hist_start_dt AND nvl(hist_eind_dt, to_date('31-12-2099','dd-mm-yyyy'))\n" +
                        "             AND tc.taak_cd in ('AS', 'EMAS', 'EMRE', 'HOM', 'GPA', 'PIS', 'HMD', 'PMM')\n" +
                        "            AND tc.taak_cd not in ('GPS', 'GPSS', 'GPW', 'GPWS', 'GPWD')\n" +
                        "            GROUP BY MAAND\n" +
                        "            ORDER BY MAAND ASC) b\n" +
                        "ON a.PERIODE = b.MAAND ";
                break;
            case J_PRESTES_AVEC_EMAS:
                query = " SELECT SUM(UREN)/7.6 as TOTAL, wkp.MAAND\n" +
                        " FROM HR.WKN_PLANNINGEN wkp, HR.WERKNEMERS w, HR.TAAK_CODES tc, HR.V_KONTRAKTEN vk\n" +
                        " WHERE wkp.maand BETWEEN ? AND ?\n" +
                        " AND wkp.werknemer_id = w.werknemer_id\n" +
                        " AND wkp.taak_id      = tc.taak_id\n" +
                        " AND w.werknemer_id   = vk.werknemer_id\n" +
                        " AND vk.functie_id    IN (121, 122, 128)\n" +
                        " AND wkp.afdeling_id LIKE ?\n" +
                        " AND wkp.planning_dt BETWEEN hist_start_dt AND nvl(hist_eind_dt, to_date('31-12-2099','dd-mm-yyyy'))\n" +
                        " AND tc.taak_cd in ('AS', 'EMAS', 'EMRE', 'HOM', 'GPA', 'PIS', 'HMD', 'PMM')\n" +
                        " AND tc.taak_cd not in ('GPS', 'GPSS', 'GPW', 'GPWS', 'GPWD')\n" +
                        "GROUP BY MAAND\n" +
                        "ORDER BY MAAND ASC";
                break;
            case EMSS:
                query = " SELECT SUM(UREN)/7.6 as TOTAL, MAAND\n" +
                        " FROM HR.WKN_PLANNINGEN wkp, HR.WERKNEMERS w, HR.TAAK_CODES tc, HR.V_KONTRAKTEN vk\n" +
                        " WHERE wkp.maand BETWEEN ? AND ?\n" +
                        " AND wkp.werknemer_id = w.werknemer_id\n" +
                        " AND wkp.taak_id      = tc.taak_id\n" +
                        " AND w.werknemer_id   = vk.werknemer_id\n" +
                        " AND vk.functie_id    IN (121, 122, 128)\n" +
                        " AND wkp.afdeling_id LIKE ?\n" +
                        " AND wkp.planning_dt BETWEEN hist_start_dt AND nvl(hist_eind_dt, to_date('31-12-2099','dd-mm-yyyy'))\n" +
                        " AND tc.taak_cd in ('EMSS')\n" +
                        " AND tc.taak_cd not in ('GPS', 'GPSS', 'GPW', 'GPWS', 'GPWD')\n" +
                        " GROUP BY MAAND\n" +
                        " ORDER BY MAAND ASC";
                break;
            case EMAS:
                query = " SELECT SUM(UREN)/7.6 as TOTAL, MAAND\n" +
                        " FROM HR.WKN_PLANNINGEN wkp, HR.WERKNEMERS w, HR.TAAK_CODES tc, HR.V_KONTRAKTEN vk\n" +
                        " WHERE wkp.maand BETWEEN ? AND ?\n" +
                        " AND wkp.werknemer_id = w.werknemer_id\n" +
                        " AND wkp.taak_id      = tc.taak_id\n" +
                        " AND w.werknemer_id   = vk.werknemer_id\n" +
                        " AND vk.functie_id    IN (121, 122, 128)\n" +
                        " AND wkp.afdeling_id LIKE ?\n" +
                        " AND wkp.planning_dt BETWEEN hist_start_dt AND nvl(hist_eind_dt, to_date('31-12-2099','dd-mm-yyyy'))\n" +
                        " AND tc.taak_cd in ('EMAS')\n" +
                        " AND tc.taak_cd not in ('GPS', 'GPSS', 'GPW', 'GPWS', 'GPWD')\n" +
                        " GROUP BY MAAND\n" +
                        " ORDER BY MAAND ASC";
                break;
            case TAUX_ADMINISTRATIF:
                query = " SELECT (y.TOTAL/x.TOTAL * 100) as TOTAL\n" +
                        " FROM (\n" +
                        " SELECT SUM(uren) as TOTAL, MAAND\n" +
                        " FROM HR.WKN_PLANNINGEN wkp, HR.WERKNEMERS w, HR.TAAK_CODES tc, HR.V_KONTRAKTEN vk\n" +
                        "           WHERE wkp.maand BETWEEN ? AND ?\n" +
                        "             AND wkp.werknemer_id = w.werknemer_id\n" +
                        "             AND wkp.taak_id      = tc.taak_id\n" +
                        "             AND w.werknemer_id   = vk.werknemer_id\n" +
                        "             AND vk.functie_id    IN (121, 122, 128)\n" +
                        "             AND wkp.afdeling_id LIKE ?\n" +
                        "             AND wkp.planning_dt BETWEEN hist_start_dt AND nvl(hist_eind_dt, to_date('31-12-2099','dd-mm-yyyy'))\n" +
                        "             AND tc.taak_cd in ('AC', 'RR', 'SF', 'CO', 'CP', 'CS', 'CT', 'DP', 'DS', 'ECO', 'EMSS', 'EQ', 'FS', 'IS', 'PL', 'PR', 'RO', 'RSV', 'SH', 'SM', 'SS', 'SSV', 'TUTO', 'VM', 'VO', 'AS', 'EMAS', 'EMRE', 'GPA', 'HMD', 'HOM', 'PIS', 'PMM')\n" +
                        "             AND tc.taak_cd not in ('GPS', 'GPSS', 'GPW', 'GPWS', 'GPWD')\n" +
                        "            GROUP BY MAAND\n" +
                        "            ORDER BY MAAND ASC) x\n" +
                        "            \n" +
                        "            JOIN\n" +
                        "            \n" +
                        "            (SELECT SUM(uren) as TOTAL, MAAND\n" +
                        " FROM HR.WKN_PLANNINGEN wkp, HR.WERKNEMERS w, HR.TAAK_CODES tc, HR.V_KONTRAKTEN vk\n" +
                        "           WHERE wkp.maand BETWEEN ? AND ?\n" +
                        "             AND wkp.werknemer_id = w.werknemer_id\n" +
                        "             AND wkp.taak_id      = tc.taak_id\n" +
                        "             AND w.werknemer_id   = vk.werknemer_id\n" +
                        "             AND vk.functie_id    IN (121, 122, 128)\n" +
                        "             AND wkp.afdeling_id LIKE ?\n" +
                        "             AND wkp.planning_dt BETWEEN hist_start_dt AND nvl(hist_eind_dt, to_date('31-12-2099','dd-mm-yyyy'))\n" +
                        "             AND tc.taak_cd in ('AC', 'RR', 'SF', 'CO', 'CP', 'CS', 'CT', 'DP', 'DS', 'ECO', 'EMSS', 'EQ', 'FS', 'IS', 'PL', 'PR', 'RO', 'RSV', 'SH', 'SM', 'SS', 'SSV', 'TUTO', 'VM', 'VO')\n" +
                        "             AND tc.taak_cd not in ('GPS', 'GPSS', 'GPW', 'GPWS', 'GPWD')\n" +
                        "            GROUP BY MAAND\n" +
                        "            ORDER BY MAAND ASC) y\n" +
                        "            ON x.MAAND = y.MAAND";
                break;
            case TAUX_ADMINISTRATIF_IC:
                query = " SELECT (y.TOTAL/x.TOTAL * 100) as TOTAL\n" +
                        " FROM (\n" +
                        " SELECT SUM(uren) as TOTAL, MAAND\n" +
                        " FROM HR.WKN_PLANNINGEN wkp, HR.WERKNEMERS w, HR.TAAK_CODES tc, HR.V_KONTRAKTEN vk\n" +
                        "           WHERE wkp.maand BETWEEN ? AND ?\n" +
                        "             AND wkp.werknemer_id = w.werknemer_id\n" +
                        "             AND wkp.taak_id      = tc.taak_id\n" +
                        "             AND w.werknemer_id   = vk.werknemer_id\n" +
                        "             AND vk.functie_id    IN (121, 122, 128)\n" +
                        "             AND wkp.afdeling_id LIKE ?\n" +
                        "             AND wkp.planning_dt BETWEEN hist_start_dt AND nvl(hist_eind_dt, to_date('31-12-2099','dd-mm-yyyy'))\n" +
                        "             AND tc.taak_cd in ('AC', 'RR', 'SF', 'CO', 'CP', 'CS', 'CT', 'DP', 'DS', 'ECO', 'EMSS', 'EQ', 'FS', 'IS', 'PL', 'PR', 'RO', 'RSV', 'SH', 'SM', 'SS', 'SSV', 'TUTO', 'VM', 'VO', 'AS', 'EMAS', 'EMRE', 'GPA', 'HMD', 'HOM', 'PIS', 'PMM')\n" +
                        "             AND tc.taak_cd not in ('GPS', 'GPSS', 'GPW', 'GPWS', 'GPWD')\n" +
                        "            GROUP BY MAAND\n" +
                        "            ORDER BY MAAND ASC) x\n" +
                        "            \n" +
                        "            JOIN\n" +
                        "            \n" +
                        "            (SELECT SUM(uren) as TOTAL, MAAND\n" +
                        " FROM HR.WKN_PLANNINGEN wkp, HR.WERKNEMERS w, HR.TAAK_CODES tc, HR.V_KONTRAKTEN vk\n" +
                        "           WHERE wkp.maand BETWEEN ? AND ?\n" +
                        "             AND wkp.werknemer_id = w.werknemer_id\n" +
                        "             AND wkp.taak_id      = tc.taak_id\n" +
                        "             AND w.werknemer_id   = vk.werknemer_id\n" +
                        "             AND vk.functie_id    IN (121, 122, 128)\n" +
                        "             AND wkp.afdeling_id LIKE ?\n" +
                        "             AND wkp.planning_dt BETWEEN hist_start_dt AND nvl(hist_eind_dt, to_date('31-12-2099','dd-mm-yyyy'))\n" +
                        "             AND tc.taak_cd in ('AC', 'RR', 'SF')\n" +
                        "             AND tc.taak_cd not in ('GPS', 'GPSS', 'GPW', 'GPWS', 'GPWD')\n" +
                        "            GROUP BY MAAND\n" +
                        "            ORDER BY MAAND ASC) y\n" +
                        "            ON x.MAAND = y.MAAND";
                break;

            case RECUPERATIONS:
                query = "SELECT SUM(EIND_WAARDE)/7.6 AS TOTAL, t.VERW_MAAND\n" +
                        "FROM HR.WKN_TEGOEDEN t, HR.WERKNEMERS w\n" +
                        "WHERE t.WERKNEMER_ID = w.WERKNEMER_ID\n" +
                        "AND t.VERW_MAAND BETWEEN to_char(to_date(?, 'yyyymm'), 'YY-MM-YYYY') AND LAST_DAY(to_char(to_date(?, 'yyyymm'), 'YY-MM-YYYY'))\n" +
                        "AND w.DIPLOMA_ID IN ('101', '103', '120', '111', '116')\n" +
                        "AND w.afdeling_id LIKE ?\n" +
                        "AND t.TAAK_ID IN ('348', '375', '185')\n" +
                        "GROUP BY t.VERW_MAAND\n" +
                        "ORDER BY t.VERW_MAAND";
                break;
            case SOLDE_CP:
                query = "SELECT SUM(EIND_WAARDE)/7.6 AS TOTAL, t.VERW_MAAND\n" +
                        "FROM HR.WKN_TEGOEDEN t, HR.WERKNEMERS w\n" +
                        "WHERE t.WERKNEMER_ID = w.WERKNEMER_ID\n" +
                        "AND t.VERW_MAAND BETWEEN to_char(to_date(?, 'yyyymm'), 'YY-MM-YYYY') AND LAST_DAY(to_char(to_date(?, 'yyyymm'), 'YY-MM-YYYY'))\n" +
                        "AND w.DIPLOMA_ID IN ('101', '103', '120', '111', '116')\n" +
                        "AND w.afdeling_id LIKE ?\n" +
                        "AND t.TAAK_ID IN ('351', '353')\n" +
                        "GROUP BY t.VERW_MAAND\n" +
                        "ORDER BY t.VERW_MAAND";
                break;
            case TAUX_SMG:
                query = "SELECT (y.code_250_251 * 100) / (x.TOTAL + y.code_250_251 + z.code_220) as TOTAL, x.MAAND\n" +
                        "FROM (SELECT wkp.MAAND, SUM(uren) as TOTAL\n" +
                        "      FROM HR.WKN_PLANNINGEN wkp,\n" +
                        "           HR.WERKNEMERS w,\n" +
                        "           HR.TAAK_CODES tc,\n" +
                        "           HR.V_KONTRAKTEN vk\n" +
                        "      WHERE wkp.MAAND BETWEEN ? AND ?\n" +
                        "        AND wkp.werknemer_id = w.werknemer_id\n" +
                        "        AND wkp.taak_id = tc.taak_id\n" +
                        "        AND w.werknemer_id = vk.werknemer_id\n" +
                        "        AND vk.functie_id IN (121, 122, 128)\n" +
                        "        AND wkp.afdeling_id LIKE ?\n" +
                        "        AND wkp.planning_dt BETWEEN hist_start_dt AND nvl(hist_eind_dt, to_date('31-12-2099', 'dd-mm-yyyy'))\n" +
                        "        AND tc.taak_cd in\n" +
                        "            ('AC', 'CA', 'CC', 'RR', 'SF', 'CO', 'CP', 'CS', 'CT', 'DP', 'DS', 'ECO', 'EMSS', 'EQ', 'FS', 'IS', 'PL',\n" +
                        "             'PR', 'RE', 'RF', 'RO', 'RSV', 'SH', 'SM', 'SS', 'SSV', 'TUTO', 'VM', 'VO', 'AS', 'EMAS', 'EMRE', 'GPA',\n" +
                        "             'HMD', 'HOM', 'PIS', 'PMM', 'VA')\n" +
                        "        AND tc.taak_cd not in ('GPS', 'GPSS', 'GPW', 'GPWS', 'GPWD')\n" +
                        "      GROUP BY MAAND) x\n" +
                        "         JOIN\n" +
                        "     (WITH Months (CurrentMonth, MaxYear) AS (\n" +
                        "         SELECT CAST(TO_DATE(?, 'YYYYMM') AS DATE) AS CurrentMonth, ? AS MaxYear\n" +
                        "         FROM DUAL\n" +
                        "\n" +
                        "         UNION ALL\n" +
                        "\n" +
                        "         SELECT CAST(ADD_MONTHS(CurrentMonth, 1) AS DATE), MaxYear\n" +
                        "         FROM Months\n" +
                        "         WHERE EXTRACT(YEAR FROM ADD_MONTHS(CurrentMonth, 1)) <= MaxYear\n" +
                        "     ),\n" +
                        "           Query as (\n" +
                        "               SELECT sl.loonperiode_dt, sum(slr.uren) code_250_251\n" +
                        "               FROM HR.soc_loonbrief_regels slr,\n" +
                        "                    HR.soc_loonbrieven sl,\n" +
                        "                    HR.werknemers w,\n" +
                        "                    HR.v_kontrakten vk\n" +
                        "               WHERE TO_CHAR(sl.loonperiode_dt, 'YYYYMM') BETWEEN ? AND ?\n" +
                        "                 AND slr.loon_code_id IN (424, 425)\n" +
                        "                 AND slr.loonbrief_id = sl.loonbrief_id\n" +
                        "                 AND w.werknemer_id = sl.werknemer_id\n" +
                        "                 AND w.werknemer_id = vk.werknemer_id\n" +
                        "                 AND vk.functie_id IN (121, 122, 128)\n" +
                        "                 AND sl.loonperiode_dt BETWEEN hist_start_dt AND last_day(nvl(hist_eind_dt, sl.loonperiode_dt))\n" +
                        "                 AND w.afdeling_id LIKE ?\n" +
                        "               GROUP BY sl.loonperiode_dt\n" +
                        "           )\n" +
                        "      SELECT TO_CHAR(LAST_DAY(Months.CurrentMonth), 'YYYYMM') AS LastDay\n" +
                        "           , NVL(Query.code_250_251, 0)                           AS code_250_251\n" +
                        "      FROM Months\n" +
                        "\n" +
                        "               Left Join Query\n" +
                        "                         on Extract(MONTH FROM Months.CurrentMonth) = Extract(MONTH FROM Query.LOONPERIODE_DT)\n" +
                        "      ORDER BY LastDay ASC\n" +
                        "     ) y\n" +
                        "     ON x.MAAND = y.LastDay\n" +
                        "         JOIN\n" +
                        "     (WITH Months (CurrentMonth, MaxYear) AS (\n" +
                        "         SELECT CAST(TO_DATE(?, 'YYYYMM') AS DATE) AS CurrentMonth, ? AS MaxYear\n" +
                        "         FROM DUAL\n" +
                        "\n" +
                        "         UNION ALL\n" +
                        "\n" +
                        "         SELECT CAST(ADD_MONTHS(CurrentMonth, 1) AS DATE), MaxYear\n" +
                        "         FROM Months\n" +
                        "         WHERE EXTRACT(YEAR FROM ADD_MONTHS(CurrentMonth, 1)) <= MaxYear\n" +
                        "     ),\n" +
                        "           Query as (\n" +
                        "               SELECT sl.loonperiode_dt, (sum(slr.uren)) code_220\n" +
                        "               FROM HR.soc_loonbrief_regels slr,\n" +
                        "                    HR.soc_loonbrieven sl,\n" +
                        "                    HR.werknemers w,\n" +
                        "                    HR.v_kontrakten vk\n" +
                        "               WHERE slr.loon_code_id IN (394)\n" +
                        "                 AND TO_CHAR(LOONPERIODE_DT, 'YYYYMM') BETWEEN ? AND ?\n" +
                        "                 AND slr.loonbrief_id = sl.loonbrief_id\n" +
                        "                 AND w.werknemer_id = sl.werknemer_id\n" +
                        "                 AND w.werknemer_id = vk.werknemer_id\n" +
                        "                 AND vk.functie_id IN (121, 122, 128)\n" +
                        "                 AND sl.loonperiode_dt BETWEEN hist_start_dt AND last_day(nvl(hist_eind_dt, sl.loonperiode_dt))\n" +
                        "                 AND w.afdeling_id LIKE ?\n" +
                        "               GROUP BY sl.loonperiode_dt\n" +
                        "               ORDER BY sl.loonperiode_dt\n" +
                        "           )\n" +
                        "      SELECT TO_CHAR(LAST_DAY(Months.CurrentMonth), 'YYYYMM') AS LastDay\n" +
                        "           , NVL(Query.code_220, 0)                           AS code_220\n" +
                        "      FROM Months\n" +
                        "\n" +
                        "               Left Join Query\n" +
                        "                         on Extract(MONTH FROM Months.CurrentMonth) = Extract(MONTH FROM Query.loonperiode_dt)\n" +
                        "      ORDER BY LastDay ASC\n" +
                        "     ) z\n" +
                        "     ON x.MAAND = z.LastDay\n";
                break;
            case KM_PARCOURUS:
                query = "";
                break;
        }
        return query;
    }

    ResultSet setQuery(Query currentIndicateur, PreparedStatement ps, int year, int centreNo) throws Exception {
        ps.setString(1, (year + "01"));
        ps.setString(2, (year + "12"));
        if (centreNo == 997) {
            ps.setString(3, "%");
        } else {
            ps.setInt(3, (centreNo));
        }

        final boolean SIX_PARAMETERS_NEEDED = (
                currentIndicateur.equals(RECETTE_OA_PAR_J_PRESTE) ||
                        currentIndicateur.equals(RECETTE_TOTALE) ||
                        currentIndicateur.equals(RECETTE_OA_PAR_J_AVEC_SOINS) ||
                        currentIndicateur.equals(RECETTE_OA_PAR_VISITE) ||
                        currentIndicateur.equals(TAUX_ADMINISTRATIF) ||
                        currentIndicateur.equals(TAUX_ADMINISTRATIF_IC) ||
                        currentIndicateur.equals(VISITES_PAR_J_PRESTES) ||
                        currentIndicateur.equals(VISITES_PAR_J_AV_SOINS) ||
                        currentIndicateur.equals(RECETTE_TOTALE_PAR_J_AVEC_SOINS) ||
                        currentIndicateur.equals(DUREE_MOYENNE_PAR_VISITE) ||
                        currentIndicateur.equals(TAUX_PATIENTS_VIPO) ||
                        currentIndicateur.equals(TAUX_PATIENTS_NON_VIPO)
        );
        // Check if the SQL query takes 6 parameters
        if (SIX_PARAMETERS_NEEDED) {
            ps.setString(4, (year + "01"));
            ps.setString(5, (year + "12"));
            if (centreNo == 997) {
                ps.setString(6, "%");
            } else {
                ps.setInt(6, (centreNo));
            }
        }
        // Check if the SQL query takes 9 parameters
        if (currentIndicateur.equals(RECETTE_TOTALE_PAR_J_AVEC_SOINS)) {
            ps.setString(7, (year + "01"));
            ps.setString(8, (year + "12"));
            if (centreNo == 997) {
                ps.setString(9, "%");
            } else {
                ps.setInt(9, (centreNo));
            }
        }
        // Check if the SQL query takes 13 parameters
        if (currentIndicateur.equals(TAUX_SMG)) {
            ps.setString(4, year + "01");
            ps.setInt(5, year);
            ps.setString(6, year + "01");
            ps.setString(7, year + "12");
            if (centreNo == 997) {
                ps.setString(8, "%");
            } else {
                ps.setInt(8, (centreNo));
            }
            ps.setString(9, year + "01");
            ps.setInt(10, year);
            ps.setString(11, year + "01");
            ps.setString(12, year + "12");
            if (centreNo == 997) {
                ps.setString(13, "%");
            } else {
                ps.setInt(13, (centreNo));
            }
        }
        return ps.executeQuery();
    }
}



