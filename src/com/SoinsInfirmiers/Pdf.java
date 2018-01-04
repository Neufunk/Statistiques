package SoinsInfirmiers;

import java.io.FileOutputStream;
import java.util.Date;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import static SoinsInfirmiers.PopUpActivite.centreVal;
import static SoinsInfirmiers.PopUpActivite.monthVal;
import static SoinsInfirmiers.PopUpActivite.yearVal;

class Pdf {
    private static final String INTERSTATE = "/fonts/Interstate-Regular.ttf";

    private static Font titleFont = new Font(Font.FontFamily.TIMES_ROMAN, 18,
            Font.BOLD);
    private static Font whiteFont = new Font(Font.FontFamily.TIMES_ROMAN, 12,
            Font.NORMAL, BaseColor.WHITE);
    private static Font subFont = new Font(Font.FontFamily.TIMES_ROMAN, 16,
            Font.BOLD);
    private static Font smallBold = new Font(Font.FontFamily.TIMES_ROMAN, 10,
            Font.BOLD);
    private BaseColor blueASD = new BaseColor(0, 110, 130);

    double[] answerArr = new double[22];

    void buildActivitePdf() throws Exception {
        ActiviteSuiviPersonnel activiteSuiviPersonnel = new ActiviteSuiviPersonnel();
        activiteSuiviPersonnel.buildPdf();
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
        document.addCreator("Johnathan Vanbeneden");
    }

    class ActiviteSuiviPersonnel {

        void buildPdf() throws Exception {
            Document document = new Document();
            String currentUser = System.getProperty("user.name");
            String file = "C:/users/" + currentUser + "/Desktop/Rapport_Activite.pdf";
            PdfWriter.getInstance(document, new FileOutputStream(file));
            document.open();
            addMetaData(document, "Rapport d'activité");
            addTitlePage(document);
            addFirstPage(document);
            addSecondPage(document);
            document.close();
            System.out.println("PDF généré avec succès");
        }

        private void addTitlePage(Document document) throws DocumentException {
            Paragraph preface = new Paragraph();
            try {
                final String IMG = "/img/asd_logo.png";
                Image img = Image.getInstance(IMG);
                img.setAlignment(Element.ALIGN_LEFT);
                img.scaleAbsolute(195f, 75f);
                preface.add(img);
            } catch (Exception e) {
                e.printStackTrace();
            }

            addEmptyLine(preface, 11);

            Paragraph title = new Paragraph("RAPPORT D'ACTIVITÉ ET SUIVI DU PERSONNEL", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            preface.add(title);

            addEmptyLine(preface, 1);
            Paragraph details = new Paragraph("Rapport généré par: " + System.getProperty("user.name") + ", " + new Date(),
                    smallBold);
            details.setAlignment(Element.ALIGN_RIGHT);
            preface.add(details);
            Paragraph periode = new Paragraph(centreVal + " " + monthVal + " " +
                    yearVal, titleFont);
            periode.setAlignment(Element.ALIGN_CENTER);
            addEmptyLine(preface, 5);
            preface.add(periode);


            document.add(preface);
        }

        private void addFirstPage(Document document) throws DocumentException {
            Anchor anchor = new Anchor("PARTIE 1 - RAPPORT D'ACTIVITE", subFont);
            Chapter chapter = new Chapter(new Paragraph(anchor), 1);

            Paragraph paragraph = new Paragraph();
            addEmptyLine(paragraph, 5);
            createActiviteTable(paragraph);
            chapter.add(paragraph);
            document.add(chapter);
        }

        private void addSecondPage(Document document) throws DocumentException {
            Anchor anchor = new Anchor("PARTIE 2 - SUIVI DU PERSONNEL", subFont);
            Chapter chapter = new Chapter(new Paragraph(anchor), 2);

            Paragraph paragraph = new Paragraph();
            addEmptyLine(paragraph, 3);
            createSuiviPersonnelTable(paragraph);
            chapter.add(paragraph);
            document.add(chapter);

        }

        private void createActiviteTable(Paragraph paragraph) throws BadElementException {
            /* Tableau 1 */
            PdfPTable table = new PdfPTable(1);
            PdfPCell title1 = new PdfPCell(new Phrase("Recette totale OA / Jours prestés", whiteFont));
            title1.setHorizontalAlignment(Element.ALIGN_CENTER);
            title1.setBackgroundColor(blueASD);

            PdfPCell cell = new PdfPCell(new Phrase("Facturation Totale : " + String.valueOf(answerArr[0])));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);

            PdfPCell cell2 = new PdfPCell(new Phrase("Tarification OA : " + String.valueOf(answerArr[1])));
            cell2.setHorizontalAlignment(Element.ALIGN_CENTER);

            PdfPCell cell3 = new PdfPCell(new Phrase("Tarification autre : " + String.valueOf(answerArr[2])));
            cell3.setHorizontalAlignment(Element.ALIGN_CENTER);

            PdfPCell cell4 = new PdfPCell(new Phrase("TM : " + String.valueOf(answerArr[3])));
            cell4.setHorizontalAlignment(Element.ALIGN_CENTER);

            PdfPCell cell5 = new PdfPCell(new Phrase("Facturation OA / Jour presté avec soin : " + String.valueOf(answerArr[4])));
            cell5.setHorizontalAlignment(Element.ALIGN_CENTER);

            table.addCell(title1);
            table.addCell(cell);
            table.addCell(cell2);
            table.addCell(cell3);
            table.addCell(cell4);
            table.addCell(cell5);

            /* Tableau 2 */
            PdfPTable table2 = new PdfPTable(2);
            title1 = new PdfPCell(new Phrase("Recette OA / Visite", whiteFont));
            title1.setHorizontalAlignment(Element.ALIGN_CENTER);
            title1.setBackgroundColor(BaseColor.DARK_GRAY);

            PdfPCell title2 = new PdfPCell(new Phrase("Nbre Visites / Jours prestés avec soins", whiteFont));
            title2.setHorizontalAlignment(Element.ALIGN_CENTER);
            title2.setBackgroundColor(BaseColor.DARK_GRAY);

            double result = (answerArr[1] / answerArr[12]);
            PdfPCell cell6 = new PdfPCell(new Phrase(String.valueOf(result)));
            cell6.setHorizontalAlignment(Element.ALIGN_CENTER);

            PdfPCell cell7 = new PdfPCell(new Phrase(String.valueOf(answerArr[5])));
            cell7.setHorizontalAlignment(Element.ALIGN_CENTER);

            table2.addCell(title1);
            table2.addCell(title2);
            table2.addCell(cell6);
            table2.addCell(cell7);

            /* Tableau 3 */
            PdfPTable table3 = new PdfPTable(4);
            title1 = new PdfPCell(new Phrase("Profil patients", whiteFont));
            title1.setHorizontalAlignment(Element.ALIGN_CENTER);
            title1.setBackgroundColor(BaseColor.DARK_GRAY);
            table3.addCell(title1);

            title2 = new PdfPCell(new Phrase("Toilettes Nom", whiteFont));
            title2.setHorizontalAlignment(Element.ALIGN_CENTER);
            title2.setBackgroundColor(BaseColor.DARK_GRAY);
            table3.addCell(title2);

            PdfPCell title3 = new PdfPCell(new Phrase("% Palliatifs", whiteFont));
            title3.setHorizontalAlignment(Element.ALIGN_CENTER);
            title3.setBackgroundColor(BaseColor.DARK_GRAY);
            table3.addCell(title3);

            PdfPCell title4 = new PdfPCell(new Phrase("Visites / Forfaits", whiteFont));
            title4.setHorizontalAlignment(Element.ALIGN_CENTER);
            title4.setBackgroundColor(BaseColor.DARK_GRAY);
            table3.addCell(title4);

            table3.addCell("Nombre patients : " + String.valueOf(answerArr[6]));
            table3.addCell("Total toilettes NOM/total visites : " + String.valueOf(answerArr[9] * 100 + "%"));
            table3.addCell("Nbre patients FF palliatifs : " + String.valueOf(answerArr[11]));
            table3.addCell("FFA : " + String.valueOf(answerArr[19]));
            table3.addCell("% NOM : " + String.valueOf(answerArr[7] * 100 + "%"));
            table3.addCell("Nombre toilettes");
            table3.addCell("Facturation Palliatifs : " + String.valueOf(answerArr[10]));
            table3.addCell("FFB : " + String.valueOf(answerArr[20]));
            table3.addCell("%FF : " + String.valueOf(answerArr[8] * 100 + "%"));
            table3.addCell("");
            table3.addCell("");
            table3.addCell("FFC : " + String.valueOf(answerArr[21]));
            table3.addCell("test");
            table3.addCell("");
            table3.addCell("");
            table3.addCell("");

            /* Tableau 4 */
            PdfPTable table4 = new PdfPTable(1);
            title1 = new PdfPCell(new Phrase("Nbre Visite + Nbre Patients", whiteFont));
            title1.setHorizontalAlignment(Element.ALIGN_CENTER);
            title1.setBackgroundColor(BaseColor.DARK_GRAY);
            title1.setHorizontalAlignment(Element.ALIGN_CENTER);
            table4.addCell(title1);
            PdfPCell cell1Table4 = new PdfPCell(new Phrase("Nombre visites : " + String.valueOf(answerArr[12])));
            cell1Table4.setHorizontalAlignment(Element.ALIGN_CENTER);
            table4.addCell(cell1Table4);
            PdfPCell cell2Table4 = new PdfPCell(new Phrase("Nombre patients : " + String.valueOf(answerArr[6])));
            cell2Table4.setHorizontalAlignment(Element.ALIGN_CENTER);
            table4.addCell(cell2Table4);

            paragraph.add(table);
            addEmptyLine(paragraph, 2);
            paragraph.add(table2);
            addEmptyLine(paragraph, 2);
            paragraph.add(table3);
            addEmptyLine(paragraph, 2);
            paragraph.add(table4);
        }

        private void createSuiviPersonnelTable(Paragraph paragraph) throws BadElementException {
            /* Tableau 1 */
            PdfPTable table = new PdfPTable(1);
            PdfPCell title = new PdfPCell(new Phrase("Total jours prestés Inf", whiteFont));
            title.setHorizontalAlignment(Element.ALIGN_CENTER);
            title.setBackgroundColor(BaseColor.DARK_GRAY);
            table.addCell(title);

            PdfPCell cell = new PdfPCell(new Phrase(String.valueOf(answerArr[13])));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);

            /* Tableau 2 */
            PdfPTable table2 = new PdfPTable(1);
            PdfPCell title2 = new PdfPCell(new Phrase("Solde récup fin de mois", whiteFont));
            title2.setHorizontalAlignment(Element.ALIGN_CENTER);
            title2.setBackgroundColor(BaseColor.DARK_GRAY);
            table2.addCell(title2);

            PdfPCell cell2 = new PdfPCell(new Phrase(String.valueOf(answerArr[14])));
            cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
            table2.addCell(cell2);

            /* Tableau 3 */
            PdfPTable table3 = new PdfPTable(1);
            PdfPCell title3 = new PdfPCell(new Phrase("Total jours payés", whiteFont));
            title3.setHorizontalAlignment(Element.ALIGN_CENTER);
            title3.setBackgroundColor(BaseColor.DARK_GRAY);
            table3.addCell(title3);

            PdfPCell cell3 = new PdfPCell(new Phrase(String.valueOf(answerArr[15])));
            cell3.setHorizontalAlignment(Element.ALIGN_CENTER);
            table3.addCell(cell3);
            PdfPCell cell4 = new PdfPCell(new Phrase("% SMG : " + String.valueOf(answerArr[16]*100 + "%")));
            cell4.setHorizontalAlignment(Element.ALIGN_CENTER);
            table3.addCell(cell4);
            PdfPCell cell5 = new PdfPCell(new Phrase("% Fériés, VA, ... : " + String.valueOf(answerArr[17]*100 + "%")));
            cell5.setHorizontalAlignment(Element.ALIGN_CENTER);
            table3.addCell(cell5);
            PdfPCell cell6 = new PdfPCell(new Phrase("% prestés : " + String.valueOf(answerArr[18]*100 + "%")));
            cell6.setHorizontalAlignment(Element.ALIGN_CENTER);
            table3.addCell(cell6);

            paragraph.add(table);
            addEmptyLine(paragraph, 3);
            paragraph.add(table2);
            addEmptyLine(paragraph, 3);
            paragraph.add(table3);
            addEmptyLine(paragraph, 3);
        }
    }

}
