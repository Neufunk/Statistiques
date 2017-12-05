package SoinsInfirmiers;

import java.io.FileOutputStream;
import java.util.Date;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

class Pdf {

    private static Font catFont = new Font(Font.FontFamily.TIMES_ROMAN, 18,
            Font.BOLD);
    private static Font redFont = new Font(Font.FontFamily.TIMES_ROMAN, 12,
            Font.NORMAL, BaseColor.RED);
    private static Font subFont = new Font(Font.FontFamily.TIMES_ROMAN, 16,
            Font.BOLD);
    private static Font smallBold = new Font(Font.FontFamily.TIMES_ROMAN, 10,
            Font.BOLD);

    void buildPdf() {
        try {
            Document document = new Document();
            String currentUser = System.getProperty("user.name");
            String file = "C:/users/" + currentUser + "/Desktop/Rapport_Statistique.pdf";
            PdfWriter.getInstance(document, new FileOutputStream(file));
            document.open();
            addMetaData(document);
            addTitlePage(document);
            addFirstPage(document);
            addSecondPage(document);
            document.close();
            System.out.println("PDF généré avec succès");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addMetaData(Document document){
        document.addTitle("Rapport activité");
        document.addSubject("Logiciel Statistiques");
        document.addAuthor("Johnathan Vanbeneden");
        document.addCreator("Johnathan Vanbeneden");
    }

    private void addTitlePage(Document document) throws DocumentException {
        Paragraph preface = new Paragraph();
        try {
            final String IMG = "src/resources/img/favicon.png";
            Image img = Image.getInstance(IMG);
            img.setAlignment(Element.ALIGN_LEFT);
            img.scaleAbsolute(50f, 50f);
            preface.add(img);
        } catch (Exception e) {
            e.printStackTrace();
        }

        addEmptyLine(preface, 11);

        Paragraph title = new Paragraph("TITRETITRETITRE", catFont);
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
        addEmptyLine(paragraph, 3);
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
        PdfPTable table = new PdfPTable(3);

        // t.setBorderColor(BaseColor.GRAY);
        // t.setPadding(4);
        // t.setSpacing(4);
        // t.setBorderWidth(1);

        PdfPCell c1 = new PdfPCell(new Phrase("Table Header 1"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase("Table Header 2"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase("Table Header 3"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);
        table.setHeaderRows(1);

        table.addCell("1.0");
        table.addCell("1.1");
        table.addCell("1.2");
        table.addCell("2.1");
        table.addCell("2.2");
        table.addCell("2.3");

        paragraph.add(table);
    }

    private static void addEmptyLine(Paragraph paragraph, int number) {
        for (int i = 0; i < number; i++) {
            paragraph.add(new Paragraph(" "));
        }
    }





}
