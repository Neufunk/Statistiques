package SoinsInfirmiers;

import java.io.FileOutputStream;
import java.util.Date;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

class Pdf {
    private static final String INTERSTATE = "/fonts/Interstate-regular.ttf";

    private static Font titleFont = new Font(Font.FontFamily.TIMES_ROMAN, 18,
            Font.BOLD);
    private static Font whiteFont = new Font(Font.FontFamily.TIMES_ROMAN, 12,
            Font.NORMAL, BaseColor.WHITE);
    private static Font subFont = new Font(Font.FontFamily.TIMES_ROMAN, 16,
            Font.BOLD);
    private static Font smallBold = new Font(Font.FontFamily.TIMES_ROMAN, 10,
            Font.BOLD);

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
                final String IMG = "C:\\Users\\johnathanv\\IdeaProjects\\Statistiques\\src\\resources\\img\\asd_logo.png";
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
            addEmptyLine(preface, 3);

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
            chapter.add(paragraph);
            document.add(chapter);
        }

        private void createActiviteTable(Paragraph paragraph) throws BadElementException {
            /* Tableau 1 */
            PdfPTable table = new PdfPTable(1);
            PdfPCell title1 = new PdfPCell(new Phrase("Recette totale OA / Jours prestés", whiteFont));
            title1.setHorizontalAlignment(Element.ALIGN_CENTER);
            title1.setBackgroundColor(BaseColor.DARK_GRAY);
            PdfPCell cell = new PdfPCell(new Phrase("result"));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(title1);
            table.addCell(cell);

            /* Tableau 2 */
            PdfPTable table2 = new PdfPTable(2);
            title1 = new PdfPCell(new Phrase("Recette OA / Visite", whiteFont));
            title1.setHorizontalAlignment(Element.ALIGN_CENTER);
            title1.setBackgroundColor(BaseColor.DARK_GRAY);
            table2.addCell(title1);
            PdfPCell title2 = new PdfPCell(new Phrase("Nbre Visites / Jours prestés avec soins", whiteFont));
            title2.setHorizontalAlignment(Element.ALIGN_CENTER);
            title2.setBackgroundColor(BaseColor.DARK_GRAY);
            table2.addCell(title2);
            table2.addCell("result");
            table2.addCell("result");

            /* Tableau 3 */
            PdfPTable table3 = new PdfPTable(4);
            title1 = new PdfPCell(new Phrase("% Non Forfait", whiteFont));
            title1.setHorizontalAlignment(Element.ALIGN_CENTER);
            title1.setBackgroundColor(BaseColor.DARK_GRAY);
            table3.addCell(title1);

            title2 = new PdfPCell(new Phrase("% Toilettes Nom", whiteFont));
            title2.setHorizontalAlignment(Element.ALIGN_CENTER);
            title2.setBackgroundColor(BaseColor.DARK_GRAY);
            table3.addCell(title2);

            PdfPCell title3 = new PdfPCell(new Phrase("% Palliatifs", whiteFont));
            title3.setHorizontalAlignment(Element.ALIGN_CENTER);
            title3.setBackgroundColor(BaseColor.DARK_GRAY);
            table3.addCell(title3);

            PdfPCell title4 = new PdfPCell(new Phrase("Nbre Visite + Nbre Patients", whiteFont));
            title4.setHorizontalAlignment(Element.ALIGN_CENTER);
            title4.setBackgroundColor(BaseColor.DARK_GRAY);
            table3.addCell(title4);

            table3.addCell("result");
            table3.addCell("result");
            table3.addCell("result");
            table3.addCell("result");

            paragraph.add(table);
            addEmptyLine(paragraph, 2);
            paragraph.add(table2);
            addEmptyLine(paragraph, 2);
            paragraph.add(table3);
        }
    }

}
