package SoinsInfirmiers;

import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Date;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import main.ExceptionHandler;

import static SoinsInfirmiers.Database.Query.*;

class Rapports {

    private final BaseColor BLUE_ASD = new BaseColor(0, 110, 130);
    private final BaseColor ORANGE_ASD = new BaseColor(254, 246, 233);
    final double[] answerArr = new double[26];
    final double[] answerArrFileB = new double[3];
    private String centreVal;
    private String monthVal;
    private String yearVal;

    enum SheetOrientation {
        PORTRAIT,
        LANDSCAPE
    }

    private Font setInterstateFont(int size, String color) {
        BaseFont baseFont;
        Font interstate = null;
        try {
            baseFont = BaseFont.createFont("/fonts/Interstate-Regular.ttf", BaseFont.WINANSI, BaseFont.NOT_EMBEDDED);
            interstate = new Font(baseFont, size);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (interstate != null) {
            switch (color) {
                case "white":
                    interstate.setColor(BaseColor.WHITE);
                    break;
                case "black":
                    interstate.setColor(BaseColor.BLACK);
                    break;
                default:
                    interstate.setColor(BaseColor.BLACK);
                    break;
            }
        }
        return interstate;
    }

    private Font setInterstateFont(int size) {
        return setInterstateFont(size, "black");
    }

    void buildActivitePdf() {
        ActiviteSuiviPersonnel activiteSuiviPersonnel = new ActiviteSuiviPersonnel();
        try {
            activiteSuiviPersonnel.buildPdf();
        } catch (Exception e) {
            ExceptionHandler.switchException(e, this.getClass());
        }
    }

    void buildIndicateurDeGestionPdf() {
        IndicateursDeGestion indicateursDeGestion = new IndicateursDeGestion();
        try {
            indicateursDeGestion.buildPdf();
        } catch (Exception e) {
            ExceptionHandler.switchException(e, this.getClass());
        }
    }

    private void addEmptyLine(Paragraph paragraph, int number) {
        for (int i = 0; i < number; i++) {
            paragraph.add(new Paragraph(" "));
        }
    }

    private void addMetaData(Document document, String title) {
        document.addTitle(title);
        document.addSubject("Statistiques");
        document.addAuthor("Johnathan Vanbeneden");
        document.addCreator("Logiciel Statistiques - Johnathan Vanbeneden");
    }

    private String format(double value, int decimal) {
        return String.format("%,." + decimal + "f", value);
    }

    private void centerContent(PdfPCell cell) {
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
    }

    private void addFooter(PdfWriter writer, SheetOrientation sheetOrientation) throws IOException {
        PdfPTable table = new PdfPTable(3);
        try {
            table.setWidths(new int[]{33, 33, 33});
            switch (sheetOrientation) {
                case PORTRAIT:
                    table.setTotalWidth(525);
                    break;
                case LANDSCAPE:
                    table.setTotalWidth(770);
            }
            table.getDefaultCell().setFixedHeight(20);
            table.getDefaultCell().setBorder(Rectangle.BOTTOM);
            Image img = Image.getInstance(ClassLoader.getSystemResource("img/favicon.png"));
            table.addCell(img);
            table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);

            PdfPCell cell2 = new PdfPCell(new Phrase(centreVal + " - " + monthVal + " " + yearVal, setInterstateFont(7)));
            cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell2.setBorder(Rectangle.BOTTOM);
            table.addCell(cell2);

            table.addCell(new Phrase(String.format("Page %d", writer.getPageNumber()), setInterstateFont(10)));
            PdfPCell cell = new PdfPCell();
            cell.setBorder(Rectangle.BOTTOM);
            table.addCell(cell);
            PdfContentByte canvas = writer.getDirectContent();
            canvas.beginMarkedContentSequence(PdfName.ARTIFACT);
            table.writeSelectedRows(0, -1, 36, 30, canvas);
            canvas.endMarkedContentSequence();
        } catch (DocumentException e) {
            throw new ExceptionConverter(e);
        }
    }

    class ActiviteSuiviPersonnel {

        final ControllerPopUpActivite controllerPopUpActivite = new ControllerPopUpActivite();

        void buildPdf() throws Exception {
            centreVal = controllerPopUpActivite.getCentreVal();
            monthVal = controllerPopUpActivite.getMonthVal();
            yearVal = controllerPopUpActivite.getYearVal();
            Document document = new Document();
            String currentUser = System.getProperty("user.name");
            String file = "C:/users/" + currentUser + "/Desktop/" +
                    "Rapport_Activite_" + centreVal + "_" + monthVal +
                    "_" + yearVal + ".pdf";
            PdfWriter pdfWriter = PdfWriter.getInstance(document, new FileOutputStream(file));
            document.open();
            addMetaData(document, "Rapport d'activité");
            addTitlePage(document);
            addFirstPage(document);
            addFooter(pdfWriter, SheetOrientation.PORTRAIT);
            addSecondPage(document);
            addFooter(pdfWriter, SheetOrientation.PORTRAIT);
            document.close();
            System.out.println("PDF généré avec succès");
        }

        private void addTitlePage(Document document) throws DocumentException {
            Paragraph preface = new Paragraph();
            try {
                Image img = Image.getInstance(ClassLoader.getSystemResource("img/asd_logo.png"));
                img.setAlignment(Element.ALIGN_LEFT);
                img.scaleAbsolute(195f, 75f);
                preface.add(img);
            } catch (Exception e) {
                e.printStackTrace();
            }

            addEmptyLine(preface, 11);

            Paragraph title = new Paragraph("RAPPORT D'ACTIVITÉ ET SUIVI DU PERSONNEL", setInterstateFont(19, "black"));
            title.setAlignment(Element.ALIGN_CENTER);
            preface.add(title);

            addEmptyLine(preface, 1);
            Paragraph details = new Paragraph("Rapport généré par: " + System.getProperty("user.name") + ", " + new Date(),
                    setInterstateFont(10));
            details.setAlignment(Element.ALIGN_RIGHT);
            preface.add(details);
            Paragraph periode = new Paragraph(centreVal + " - " + monthVal + " " + yearVal, setInterstateFont(18));
            periode.setAlignment(Element.ALIGN_CENTER);
            addEmptyLine(preface, 5);
            preface.add(periode);

            document.add(preface);
        }

        private void addFirstPage(Document document) throws DocumentException {
            Anchor anchor = new Anchor("PARTIE 1 - RAPPORT D'ACTIVITE", setInterstateFont(16));
            Chapter chapter = new Chapter(new Paragraph(anchor), 1);

            Paragraph paragraph = new Paragraph();
            addEmptyLine(paragraph, 4);
            createActiviteTable(paragraph);
            chapter.add(paragraph);
            document.add(chapter);
        }

        private void addSecondPage(Document document) throws DocumentException {
            Anchor anchor = new Anchor("PARTIE 2 - SUIVI DU PERSONNEL", setInterstateFont(16));
            Chapter chapter = new Chapter(new Paragraph(anchor), 2);

            Paragraph paragraph = new Paragraph();
            addEmptyLine(paragraph, 4);
            createSuiviPersonnelTable(paragraph);
            chapter.add(paragraph);
            document.add(chapter);

        }

        private void createActiviteTable(Paragraph paragraph) throws DocumentException {
            /* Tableau 1 */
            PdfPTable table = new PdfPTable(1);
            table.setWidthPercentage(100);
            PdfPCell title1 = new PdfPCell(new Phrase("Recette totale OA / Jours prestés", setInterstateFont(12, "white")));
            title1.setHorizontalAlignment(Element.ALIGN_CENTER);
            title1.setVerticalAlignment(Element.ALIGN_CENTER);
            title1.setBackgroundColor(BLUE_ASD);

            PdfPCell cell = new PdfPCell(new Phrase("Facturation Totale : " + format(answerArr[0], 2)));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);

            PdfPCell cell2 = new PdfPCell(new Phrase("Tarification OA : " + format(answerArr[1], 2)));
            cell2.setHorizontalAlignment(Element.ALIGN_CENTER);

            PdfPCell cell3 = new PdfPCell(new Phrase("Tarification autre : " + format(answerArr[2], 2)));
            cell3.setHorizontalAlignment(Element.ALIGN_CENTER);

            PdfPCell cell4 = new PdfPCell(new Phrase("TM : " + format(answerArr[3], 2)));
            cell4.setHorizontalAlignment(Element.ALIGN_CENTER);

            PdfPCell cell5 = new PdfPCell(new Phrase("Facturation OA / Jour presté avec soin : " + format(answerArr[4], 2)));
            cell5.setHorizontalAlignment(Element.ALIGN_CENTER);

            table.addCell(title1);
            table.addCell(cell);
            table.addCell(cell2);
            table.addCell(cell3);
            table.addCell(cell4);
            table.addCell(cell5);

            /* Tableau 2 */
            PdfPTable table2 = new PdfPTable(2);
            table2.setWidthPercentage(100);
            title1 = new PdfPCell(new Phrase("Recette OA / Visite", setInterstateFont(10, "white")));
            title1.setHorizontalAlignment(Element.ALIGN_CENTER);
            title1.setBackgroundColor(BLUE_ASD);

            PdfPCell title2 = new PdfPCell(new Phrase("Nbre Visites / Jours prestés avec soins", setInterstateFont(10, "white")));
            title2.setHorizontalAlignment(Element.ALIGN_CENTER);
            title2.setBackgroundColor(BLUE_ASD);

            double result = new BigDecimal(answerArr[1] / answerArr[12]).setScale(2, RoundingMode.HALF_UP).doubleValue();
            PdfPCell cell6 = new PdfPCell(new Phrase(format(result, 2)));
            cell6.setHorizontalAlignment(Element.ALIGN_CENTER);

            PdfPCell cell7 = new PdfPCell(new Phrase(format(answerArr[5], 2)));
            cell7.setHorizontalAlignment(Element.ALIGN_CENTER);

            table2.addCell(title1);
            table2.addCell(title2);
            table2.addCell(cell6);
            table2.addCell(cell7);

            /* Tableau 3 */
            PdfPTable table3 = new PdfPTable(4);
            table3.setWidths(new int[]{26, 28, 30, 16});
            table3.setWidthPercentage(100);
            title1 = new PdfPCell(new Phrase("Profil patients", setInterstateFont(10, "white")));
            title1.setHorizontalAlignment(Element.ALIGN_CENTER);
            title1.setBackgroundColor(BLUE_ASD);
            table3.addCell(title1);

            title2 = new PdfPCell(new Phrase("Toilettes NOM", setInterstateFont(10, "white")));
            title2.setHorizontalAlignment(Element.ALIGN_CENTER);
            title2.setBackgroundColor(BLUE_ASD);
            table3.addCell(title2);

            PdfPCell title3 = new PdfPCell(new Phrase("Palliatifs", setInterstateFont(10, "white")));
            title3.setHorizontalAlignment(Element.ALIGN_CENTER);
            title3.setBackgroundColor(BLUE_ASD);
            table3.addCell(title3);

            PdfPCell title4 = new PdfPCell(new Phrase("Visites / Forfaits", setInterstateFont(10, "white")));
            title4.setHorizontalAlignment(Element.ALIGN_CENTER);
            title4.setBackgroundColor(BLUE_ASD);
            table3.addCell(title4);

            table3.addCell("Nombre patients :\n " + format(answerArr[6], 0));
            table3.addCell("Toilettes NOM / Visites :\n " + format(answerArr[9] * 100, 2) + "%");
            table3.addCell("Nbre patients FF palliatifs :\n " + format(answerArr[11], 0));
            table3.addCell("FFA : " + format(answerArr[19], 2));
            table3.addCell("NOM :\n " + format(answerArr[7] * 100, 2) + "%");
            table3.addCell("Nombre toilettes :\n " + format(answerArrFileB[0], 0));
            table3.addCell("Facturation Palliatifs :\n " + format(answerArr[10], 2));
            table3.addCell("FFB : " + format(answerArr[20], 2));
            table3.addCell("FF :\n " + format(answerArr[8] * 100, 2) + "%");
            table3.addCell("");
            table3.addCell("");
            table3.addCell("FFC : " + format(answerArr[21], 2));
            table3.addCell("Actes techniques :\n " + format(answerArrFileB[1], 0) + " (" + format(answerArrFileB[2], 0) + ")*");
            table3.addCell("");
            table3.addCell("");
            table3.addCell("");
            table3.setHorizontalAlignment(Element.ALIGN_CENTER);

            /* Tableau 4 */
            PdfPTable table4 = new PdfPTable(1);
            table4.setWidthPercentage(100);
            title1 = new PdfPCell(new Phrase("Nbre Visite + Nbre Patients", setInterstateFont(10, "white")));
            title1.setHorizontalAlignment(Element.ALIGN_CENTER);
            title1.setBackgroundColor(BLUE_ASD);
            title1.setHorizontalAlignment(Element.ALIGN_CENTER);
            table4.addCell(title1);

            PdfPCell cell1Table4 = new PdfPCell(new Phrase("Nombre de visites : " + format(answerArr[12], 0)));
            cell1Table4.setHorizontalAlignment(Element.ALIGN_CENTER);
            table4.addCell(cell1Table4);

            PdfPCell cell2Table4 = new PdfPCell(new Phrase("Nombre de patients : " + format(answerArr[6], 0)));
            cell2Table4.setHorizontalAlignment(Element.ALIGN_CENTER);
            table4.addCell(cell2Table4);

            paragraph.add(table);
            addEmptyLine(paragraph, 2);
            paragraph.add(table2);
            addEmptyLine(paragraph, 2);
            paragraph.add(table3);
            addEmptyLine(paragraph, 2);
            paragraph.add(table4);
            addEmptyLine(paragraph, 3);
            paragraph.add(new Phrase("* Actes techniques spécifiques", setInterstateFont(8)));
        }

        private void createSuiviPersonnelTable(Paragraph paragraph) {
            /* Tableau 1 */
            PdfPTable table = new PdfPTable(1);
            PdfPCell title = new PdfPCell(new Phrase("Total jours prestés Inf", setInterstateFont(10, "white")));
            title.setHorizontalAlignment(Element.ALIGN_CENTER);
            title.setBackgroundColor(BLUE_ASD);
            table.addCell(title);

            PdfPCell cell = new PdfPCell(new Phrase(format(answerArr[13], 1) + " jours"));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);

            /* Tableau 2 */
            PdfPTable table2 = new PdfPTable(1);
            PdfPCell title2 = new PdfPCell(new Phrase("Solde récup fin de mois", setInterstateFont(10, "white")));
            title2.setHorizontalAlignment(Element.ALIGN_CENTER);
            title2.setBackgroundColor(BLUE_ASD);
            table2.addCell(title2);

            PdfPCell cell2 = new PdfPCell(new Phrase(format(answerArr[14], 2)));
            cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
            table2.addCell(cell2);

            /* Tableau 3 */
            PdfPTable table3 = new PdfPTable(1);
            PdfPCell title3 = new PdfPCell(new Phrase("Total jours payés", setInterstateFont(10, "white")));
            title3.setHorizontalAlignment(Element.ALIGN_CENTER);
            title3.setBackgroundColor(BLUE_ASD);
            table3.addCell(title3);

            PdfPCell cell3 = new PdfPCell(new Phrase(format(answerArr[15], 1) + " jours"));
            cell3.setHorizontalAlignment(Element.ALIGN_CENTER);
            table3.addCell(cell3);
            PdfPCell cell4 = new PdfPCell(new Phrase("% SMG : " + format(answerArr[16] * 100, 2) + "%"));
            cell4.setHorizontalAlignment(Element.ALIGN_LEFT);
            table3.addCell(cell4);
            PdfPCell cell5 = new PdfPCell(new Phrase("% Fériés, VA, ... : " + format(answerArr[17] * 100, 2) + "%"));
            cell5.setHorizontalAlignment(Element.ALIGN_LEFT);
            table3.addCell(cell5);
            PdfPCell cell6 = new PdfPCell(new Phrase("% prestés : " + format(answerArr[18] * 100, 2) + "%"));
            cell6.setHorizontalAlignment(Element.ALIGN_LEFT);
            table3.addCell(cell6);

            //* Tableau 4 */
            PdfPTable table4 = new PdfPTable(1);
            PdfPCell title4 = new PdfPCell(new Phrase("Total presté", setInterstateFont(10, "white")));
            title4.setHorizontalAlignment(Element.ALIGN_CENTER);
            title4.setBackgroundColor(BLUE_ASD);
            table4.addCell(title4);
            PdfPCell cell7 = new PdfPCell(new Phrase("Sans soins : " + format(answerArr[22] * 100, 2) + "%"));
            cell7.setHorizontalAlignment(Element.ALIGN_LEFT);
            table4.addCell(cell7);
            PdfPCell cell8 = new PdfPCell(new Phrase("\t - Dont sans soins IC : " + format(answerArr[23] * 100, 2) + "%"));
            cell8.setHorizontalAlignment(Element.ALIGN_LEFT);
            table4.addCell(cell8);
            PdfPCell cell9 = new PdfPCell(new Phrase("\t - Dont sans soins Inf. : " + format(answerArr[24] * 100, 2) + "%"));
            cell8.setHorizontalAlignment(Element.ALIGN_LEFT);
            table4.addCell(cell9);
            PdfPCell cell10 = new PdfPCell(new Phrase("Avec soins : " + format(answerArr[25] * 100, 2) + "%"));
            cell8.setHorizontalAlignment(Element.ALIGN_LEFT);
            table4.addCell(cell10);

            paragraph.add(table);
            addEmptyLine(paragraph, 3);
            paragraph.add(table2);
            addEmptyLine(paragraph, 3);
            paragraph.add(table3);
            addEmptyLine(paragraph, 3);
            paragraph.add(table4);
        }
    }

    class IndicateursDeGestion {
        ControllerPopUpGestion controllerPopUpGestion = new ControllerPopUpGestion();
        Database database = new Database();
        Connection conn;
        PreparedStatement ps;
        ResultSet rs;
        String year = controllerPopUpGestion.getYearVal();
        String[] centreName = {"Philippeville", "Ciney", "Gedinne", "Eghezée", "Namur"};
        int[] centreNo = {902, 913, 923, 931, 961};
        int pageNo = 0;

        Database.Query[][] indicateurArray = {
                // TARIFICATION
                {TARIFICATION_OA, TARIFICATION_NOMENCLATURE, TARIFICATION_FORFAITS_ABC, TARIFICATION_SOINS_SPECIFIQUES, FORFAITS_PALLIATIFS,
                        DEPLACEMENTS, TICKETS_MODERATEURS, SOINS_DIVERS_ET_CONVENTIONS},
                // VISITES
                {NOMBRE_DE_VISITE, NOMBRE_DE_VISITE_PAR_FFA, NOMBRE_DE_VISITE_PAR_FFB, NOMBRE_DE_VISITE_PAR_FFC},
                // PATIENTS
                {NOMBRE_DE_PATIENTS, NOMBRE_DE_VISITE_PAR_FFA, NOMBRE_DE_VISITE_PAR_FFB, NOMBRE_DE_VISITE_PAR_FFC, NOMBRE_DE_PATIENTS_PALLIA,
                        TAUX_PATIENTS_NOMENCLATURE, TAUX_PATIENTS_FORFAITS, TAUX_PATIENTS_FFA, TAUX_PATIENTS_FFB, TAUX_PATIENTS_FFC,
                        TAUX_ROTATION_PATIENTS, TAUX_PATIENTS_MC_ACCORD, TAUX_PATIENTS_MC_AUTRES, TAUX_PATIENTS_AUTRES_OA},
                // SOINS
                {NOMBRE_DE_SOINS, NOMBRE_DE_TOILETTES, NOMBRE_DE_TOILETTES_NOMENCLATURE, NOMBRE_INJECTIONS, NOMBRE_PANSEMENTS, NOMBRE_SOINS_SPECIFIQUES,
                        NOMBRE_CONSULTATIONS_INFI, NOMBRE_DE_PILULIERS, NOMBRE_DE_SOINS_DIVERS, NOMBRE_AUTRES_SOINS, NOMBRE_DE_SOINS_PAR_VISITE,
                        TAUX_TOILETTES, TAUX_TOILETTES_NOMENCLATURE, TAUX_INJECTIONS, TAUX_PANSEMENTS, TAUX_SOINS_DIVERS, TAUX_AUTRES_SOINS},
                // SUIVI DU PERSONNEL
                {/*Nothing yet*/}
        };

        void buildPdf() {
            try {
                Document document = new Document();
                String currentUser = System.getProperty("user.name");
                final String FILE = "C:/users/" + currentUser + "/Desktop/" +
                        "Indicateurs_Gestion_" + year + ".pdf";
                PdfWriter pdfWriter = PdfWriter.getInstance(document, new FileOutputStream(FILE));
                // Footer on every pages BUT last of each center because of a bug
                FooterPageEvent event = new FooterPageEvent();
                pdfWriter.setPageEvent(event);
                document.open();
                addMetaData(document, "Indicateurs de gestion");
                addTitlePage(document);
                conn = database.connect();
                // Loop to construct pages for every center
                for (int i = 0; i < centreNo.length; i++) {
                    centreVal = centreName[i];
                    yearVal = year;
                    monthVal = "";
                    addPage(document);
                    /* Manually add footer on every last page of each center because there is a bug with
                    /* the FooterPageEvent in iText 5
                     */
                    addFooter(pdfWriter, SheetOrientation.LANDSCAPE);
                    pageNo++;
                }
                document.close();
                // Set the flag to 'true' to inform ControllerPopUpGestion everything was fine.
                ControllerPopUpGestion.flag = true;
                System.out.println("PDF généré avec succès");
            } catch (Exception e) {
                ExceptionHandler.switchException(e, this.getClass());
            } finally {
                database.close(rs);
                database.close(ps);
                database.close(conn);
            }
        }

        private void addTitlePage(Document document) throws DocumentException {
            Paragraph preface = new Paragraph();
            try {
                Image img = Image.getInstance(ClassLoader.getSystemResource("img/asd_logo.png"));
                img.setAlignment(Element.ALIGN_LEFT);
                img.scaleAbsolute(195f, 75f);
                preface.add(img);
            } catch (Exception e) {
                e.printStackTrace();
            }

            addEmptyLine(preface, 11);

            Paragraph title = new Paragraph("INDICATEURS DE GESTION", setInterstateFont(19, "black"));
            title.setAlignment(Element.ALIGN_CENTER);
            preface.add(title);

            addEmptyLine(preface, 1);
            Paragraph details = new Paragraph("Rapport généré par: " + System.getProperty("user.name") + ", " + new Date(),
                    setInterstateFont(10));
            details.setAlignment(Element.ALIGN_RIGHT);
            preface.add(details);
            Paragraph periode = new Paragraph(year, setInterstateFont(18));
            periode.setAlignment(Element.ALIGN_CENTER);
            addEmptyLine(preface, 5);
            preface.add(periode);

            document.add(preface);
        }

        private void addPage(Document document) throws Exception {
            document.setPageSize(PageSize.A4.rotate());
            Anchor anchor = new Anchor("CENTRE : " + centreName[pageNo], setInterstateFont(16));
            Chapter chapter = new Chapter(new Paragraph(anchor), pageNo + 1);

            Paragraph paragraph = new Paragraph();
            addEmptyLine(paragraph, 1);
            createTable(paragraph);
            chapter.add(paragraph);
            document.add(chapter);
        }

        private void createTable(Paragraph paragraph) throws Exception {
            int indicateurNo = 1;
            String[] titleArray = {"TARIFICATION", "VISITES", "PATIENTS", "SOINS", "SUIVI PERSONNEL"};
            String[] headerArray = {" ", "INDICATEURS", "Janvier", "Février", "Mars", "Avril", "Mai", "Juin", "Juillet",
                    "Août", "Septembre", "Octobre", "Novembre", "Décembre", "TOTAL", "MOYENNE"};

            for (int i = 0; i < indicateurArray.length; i++) {
                // TITLE
                Paragraph title = new Paragraph(titleArray[i], setInterstateFont(12, "black"));
                paragraph.add(title);
                title.setSpacingBefore(12);
                title.setSpacingAfter(0);
                addEmptyLine(paragraph, 1);
                // TABLE
                PdfPTable table = new PdfPTable(16);
                table.setWidths(new int[]{1, 4, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 4, 3});
                table.setWidthPercentage(108);

                // HEADER
                for (String aMonthArray : headerArray) {
                    PdfPCell titleCell = new PdfPCell(new Phrase(aMonthArray, setInterstateFont(9, "white")));
                    centerContent(titleCell);
                    titleCell.setBackgroundColor(BLUE_ASD);
                    table.addCell(titleCell);
                }

                for (int j = 0; j < indicateurArray[i].length; j++) {
                    System.out.print(indicateurArray[i][j] + "\n");
                    // COLUMN_NO
                    PdfPCell numberCell = new PdfPCell(new Phrase(String.valueOf(indicateurNo), setInterstateFont(7)));
                    indicateurNo++;
                    centerContent(numberCell);
                    table.addCell(numberCell);

                    // TITLE_COLUMN
                    PdfPCell indicateurCell = new PdfPCell(new Phrase(indicateurArray[i][j].toString().replace("_", " "), setInterstateFont(7, "black")));
                    centerContent(indicateurCell);
                    indicateurCell.setBackgroundColor(ORANGE_ASD);
                    table.addCell(indicateurCell);

                    // RESULT CELLS
                    double totalCount = 0;
                    int rowCounter = 0;
                    String query = database.setQuery(indicateurArray[i][j]);
                    ps = conn.prepareStatement(query);
                    ps.setString(1, (year + "01"));
                    ps.setString(2, (year + "12"));
                    ps.setInt(3, (centreNo[pageNo]));
                    rs = ps.executeQuery();
                    while (rs.next()) {
                        System.out.println("ROW :" + rs.getDouble("TOTAL"));
                        PdfPCell cell = new PdfPCell(new Phrase(format(rs.getDouble("TOTAL"), 2), setInterstateFont(9)));
                        totalCount += rs.getDouble("TOTAL");

                        centerContent(cell);
                        table.addCell(cell);
                        rowCounter++;
                    }
                    int meanCounter = rowCounter;
                    // Loop to add 0,00 to the row if RS returns less than 12 results, meaning the year is not complete yet.
                    while (rowCounter < 12) {
                        PdfPCell cell = new PdfPCell(new Phrase(format(0, 2), setInterstateFont(9)));
                        totalCount += 0;
                        centerContent(cell);
                        table.addCell(cell);
                        rowCounter++;
                    }

                    // TOTAL CELLS
                    PdfPCell meanCell = new PdfPCell(new Phrase(format(totalCount, 2), setInterstateFont(9)));
                    System.out.println("TOTAL ROW :" + totalCount);
                    centerContent(meanCell);
                    meanCell.setBackgroundColor(ORANGE_ASD);
                    table.addCell(meanCell);

                    // MEAN CELLS
                    PdfPCell totalCell = new PdfPCell(new Phrase(format(totalCount / meanCounter, 2), setInterstateFont(9)));
                    System.out.println("MEAN ROW :" + totalCount / meanCounter);
                    System.out.println("----------------\n");
                    centerContent(totalCell);
                    totalCell.setBackgroundColor(ORANGE_ASD);
                    table.addCell(totalCell);
                }
                paragraph.add(table);
            }
        }
    }

    public class FooterPageEvent extends PdfPageEventHelper {
        @Override
        public void onEndPage(PdfWriter writer, Document document) {
            try {
                if (document.getPageNumber() > 1 && document.getPageNumber() != 4 && document.getPageNumber() != 7
                        && document.getPageNumber() != 10 && document.getPageNumber() != 13) {
                    addFooter(writer, SheetOrientation.LANDSCAPE);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

