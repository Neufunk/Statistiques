package SoinsInfirmiers;

import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;

class Pdf {

    private BaseColor blueASD = new BaseColor(0, 110, 130);

    double[] answerArr = new double[26];
    double[] answerArrFileB = new double[2];

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

    private Font setInterstateFont(int size){
        return setInterstateFont(size, "black");

    }

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

    private String format(double value) {
        return String.format("%,.2f", value);
    }

    private void addFooter(PdfWriter writer) throws IOException {
        PdfPTable table = new PdfPTable(3);
        try {
            table.setWidths(new int[]{24, 24, 0});
            table.setTotalWidth(525);
            table.getDefaultCell().setFixedHeight(20);
            table.getDefaultCell().setBorder(Rectangle.BOTTOM);
            Image img = Image.getInstance(ClassLoader.getSystemResource("img/favicon.png"));
            table.addCell(img);
            table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
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

        PopUpActivite popUpActivite = new PopUpActivite();

        void buildPdf() throws Exception {
            Document document = new Document();
            String currentUser = System.getProperty("user.name");
            String file = "C:/users/" + currentUser + "/Desktop/" +
                    "Rapport_Activite_" + popUpActivite.getCentreVal() + "_" + popUpActivite.getMonthVal() +
                    "_" + popUpActivite.getYearVal() + ".pdf";
            PdfWriter pdfWriter = PdfWriter.getInstance(document, new FileOutputStream(file));
            document.open();
            addMetaData(document, "Rapport d'activité");
            addTitlePage(document);
            addFirstPage(document);
            addFooter(pdfWriter);
            addSecondPage(document);
            addFooter(pdfWriter);
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
            Paragraph periode = new Paragraph(popUpActivite.getCentreVal() + " - " + popUpActivite.getMonthVal() +
                    " " + popUpActivite.getYearVal(), setInterstateFont(18));
            periode.setAlignment(Element.ALIGN_CENTER);
            addEmptyLine(preface, 5);
            preface.add(periode);

            document.add(preface);
        }

        private void addFirstPage(Document document) throws DocumentException {
            Anchor anchor = new Anchor("PARTIE 1 - RAPPORT D'ACTIVITE", setInterstateFont(16));
            Chapter chapter = new Chapter(new Paragraph(anchor), 1);

            Paragraph paragraph = new Paragraph();
            addEmptyLine(paragraph, 5);
            createActiviteTable(paragraph);
            chapter.add(paragraph);
            document.add(chapter);
        }

        private void addSecondPage(Document document) throws DocumentException {
            Anchor anchor = new Anchor("PARTIE 2 - SUIVI DU PERSONNEL", setInterstateFont(16));
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
            table.setWidthPercentage(100);
            PdfPCell title1 = new PdfPCell(new Phrase("Recette totale OA / Jours prestés", setInterstateFont(12, "white")));
            title1.setHorizontalAlignment(Element.ALIGN_CENTER);
            title1.setVerticalAlignment(Element.ALIGN_CENTER);
            title1.setBackgroundColor(blueASD);

            PdfPCell cell = new PdfPCell(new Phrase("Facturation Totale : " + format(answerArr[0])));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);

            PdfPCell cell2 = new PdfPCell(new Phrase("Tarification OA : " + format(answerArr[1])));
            cell2.setHorizontalAlignment(Element.ALIGN_CENTER);

            PdfPCell cell3 = new PdfPCell(new Phrase("Tarification autre : " + format(answerArr[2])));
            cell3.setHorizontalAlignment(Element.ALIGN_CENTER);

            PdfPCell cell4 = new PdfPCell(new Phrase("TM : " + format(answerArr[3])));
            cell4.setHorizontalAlignment(Element.ALIGN_CENTER);

            PdfPCell cell5 = new PdfPCell(new Phrase("Facturation OA / Jour presté avec soin : " + format(answerArr[4])));
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
            title1.setBackgroundColor(blueASD);

            PdfPCell title2 = new PdfPCell(new Phrase("Nbre Visites / Jours prestés avec soins", setInterstateFont(10, "white")));
            title2.setHorizontalAlignment(Element.ALIGN_CENTER);
            title2.setBackgroundColor(blueASD);

            double result = new BigDecimal(answerArr[1] / answerArr[12]).setScale(1, RoundingMode.HALF_UP).doubleValue();
            PdfPCell cell6 = new PdfPCell(new Phrase(String.valueOf(result)));
            cell6.setHorizontalAlignment(Element.ALIGN_CENTER);

            PdfPCell cell7 = new PdfPCell(new Phrase(format(answerArr[5])));
            cell7.setHorizontalAlignment(Element.ALIGN_CENTER);

            table2.addCell(title1);
            table2.addCell(title2);
            table2.addCell(cell6);
            table2.addCell(cell7);

            /* Tableau 3 */
            PdfPTable table3 = new PdfPTable(4);
            table3.setWidthPercentage(100);
            title1 = new PdfPCell(new Phrase("Profil patients", setInterstateFont(10, "white")));
            title1.setHorizontalAlignment(Element.ALIGN_CENTER);
            title1.setBackgroundColor(blueASD);
            table3.addCell(title1);

            title2 = new PdfPCell(new Phrase("Toilettes Nom", setInterstateFont(10, "white")));
            title2.setHorizontalAlignment(Element.ALIGN_CENTER);
            title2.setBackgroundColor(blueASD);
            table3.addCell(title2);

            PdfPCell title3 = new PdfPCell(new Phrase("% Palliatifs", setInterstateFont(10, "white")));
            title3.setHorizontalAlignment(Element.ALIGN_CENTER);
            title3.setBackgroundColor(blueASD);
            table3.addCell(title3);

            PdfPCell title4 = new PdfPCell(new Phrase("Visites / Forfaits", setInterstateFont(10, "white")));
            title4.setHorizontalAlignment(Element.ALIGN_CENTER);
            title4.setBackgroundColor(blueASD);
            table3.addCell(title4);

            table3.addCell("Nombre patients : " + format(answerArr[6]));
            table3.addCell("Total toilettes NOM/total visites : " + String.valueOf(answerArr[9] * 100 + "%"));
            table3.addCell("Nbre patients FF palliatifs : " + format(answerArr[11]));
            table3.addCell("FFA : " + format(answerArr[19]));
            table3.addCell("% NOM : " + String.valueOf(answerArr[7] * 100 + "%"));
            table3.addCell("Toilettes : " + format(answerArrFileB[0]));
            table3.addCell("Facturation Palliatifs : " + format(answerArr[10]));
            table3.addCell("FFB : " + format(answerArr[20]));
            table3.addCell("% FF : " + String.valueOf(answerArr[8] * 100 + "%"));
            table3.addCell("");
            table3.addCell("");
            table3.addCell("FFC : " + format(answerArr[21]));
            table3.addCell("Actes techniques : " + format(answerArrFileB[1]));
            table3.addCell("");
            table3.addCell("");
            table3.addCell("");

            /* Tableau 4 */
            PdfPTable table4 = new PdfPTable(1);
            table4.setWidthPercentage(100);
            title1 = new PdfPCell(new Phrase("Nbre Visite + Nbre Patients", setInterstateFont(10, "white")));
            title1.setHorizontalAlignment(Element.ALIGN_CENTER);
            title1.setBackgroundColor(blueASD);
            title1.setHorizontalAlignment(Element.ALIGN_CENTER);
            table4.addCell(title1);

            PdfPCell cell1Table4 = new PdfPCell(new Phrase("Nombre visites : " + format(answerArr[12])));
            cell1Table4.setHorizontalAlignment(Element.ALIGN_CENTER);
            table4.addCell(cell1Table4);

            PdfPCell cell2Table4 = new PdfPCell(new Phrase("Nombre patients : " + format(answerArr[6])));
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
            PdfPCell title = new PdfPCell(new Phrase("Total jours prestés Inf", setInterstateFont(10, "white")));
            title.setHorizontalAlignment(Element.ALIGN_CENTER);
            title.setBackgroundColor(blueASD);
            table.addCell(title);

            PdfPCell cell = new PdfPCell(new Phrase(format(answerArr[13])));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);

            /* Tableau 2 */
            PdfPTable table2 = new PdfPTable(1);
            PdfPCell title2 = new PdfPCell(new Phrase("Solde récup fin de mois", setInterstateFont(10, "white")));
            title2.setHorizontalAlignment(Element.ALIGN_CENTER);
            title2.setBackgroundColor(blueASD);
            table2.addCell(title2);

            PdfPCell cell2 = new PdfPCell(new Phrase(format(answerArr[14])));
            cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
            table2.addCell(cell2);

            /* Tableau 3 */
            PdfPTable table3 = new PdfPTable(1);
            PdfPCell title3 = new PdfPCell(new Phrase("Total jours payés", setInterstateFont(10, "white")));
            title3.setHorizontalAlignment(Element.ALIGN_CENTER);
            title3.setBackgroundColor(blueASD);
            table3.addCell(title3);

            PdfPCell cell3 = new PdfPCell(new Phrase(format(answerArr[15])));
            cell3.setHorizontalAlignment(Element.ALIGN_CENTER);
            table3.addCell(cell3);
            PdfPCell cell4 = new PdfPCell(new Phrase("% SMG : " + String.valueOf(answerArr[16]*100 + "%")));
            cell4.setHorizontalAlignment(Element.ALIGN_LEFT);
            table3.addCell(cell4);
            PdfPCell cell5 = new PdfPCell(new Phrase("% Fériés, VA, ... : " + String.valueOf(answerArr[17]*100 + "%")));
            cell5.setHorizontalAlignment(Element.ALIGN_LEFT);
            table3.addCell(cell5);
            PdfPCell cell6 = new PdfPCell(new Phrase("% prestés : " + String.valueOf(answerArr[18]*100 + "%")));
            cell6.setHorizontalAlignment(Element.ALIGN_LEFT);
            table3.addCell(cell6);

            //* Tableau 4 */
            PdfPTable table4 = new PdfPTable(1);
            PdfPCell title4 = new PdfPCell(new Phrase("Total presté", setInterstateFont(10, "white")));
            title4.setHorizontalAlignment(Element.ALIGN_CENTER);
            title4.setBackgroundColor(blueASD);
            table4.addCell(title4);
            PdfPCell cell7 = new PdfPCell(new Phrase("Sans soins : " + format(answerArr[22]*100) + "%"));
            cell7.setHorizontalAlignment(Element.ALIGN_LEFT);
            table4.addCell(cell7);
            PdfPCell cell8 = new PdfPCell(new Phrase("\t - Dont sans soins IC : " + format(answerArr[23]*100) + "%"));
            cell8.setHorizontalAlignment(Element.ALIGN_LEFT);
            table4.addCell(cell8);
            PdfPCell cell9 = new PdfPCell(new Phrase("\t - Dont sans soins Inf. : " + format(answerArr[24]*100) +"%"));
            cell8.setHorizontalAlignment(Element.ALIGN_LEFT);
            table4.addCell(cell9);
            PdfPCell cell10 = new PdfPCell(new Phrase("Avec soins : " + format(answerArr[25]*100) +"%"));
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

}
