package SoinsInfirmiers;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import javafx.application.Platform;

import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Date;

import static SoinsInfirmiers.Database.Query.*;

class IndicateurDeGestion implements Runnable {

    IndicateurDeGestion(ControllerPopUpGestion c) {
        this.c = c;
        year = c.getYearStr();
        yearInt = Integer.parseInt(year);
    }

    private final BaseColor BLUE_ASD = new BaseColor(0, 110, 130);
    private final BaseColor ORANGE_ASD = new BaseColor(254, 246, 233);
    private final BaseColor GREEN_ASD = new BaseColor(236, 244, 215);
    private final BaseColor LIGHT_RED = new BaseColor(255, 230, 230);
    private String centreVal;
    private String monthVal;
    private String yearVal;
    private ControllerPopUpGestion c;
    private String year;
    private int yearInt;
    private Database database = new Database();
    private Connection conn;
    private PreparedStatement ps;
    private ResultSet rs;
    private String[] centreName = {"Philippeville", "Ciney", "Gedinne", "Eghezée", "Namur", "Province"};
    private int[] centreNo = {902, 913, 923, 931, 961, 997};
    private int centreCounter = 0;

    public void run() {
        try {
            buildPdf();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    enum SheetOrientation {
        PORTRAIT,
        LANDSCAPE
    }

    private Font setInterstateFont(int size, String color, String weight) {
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
                default:
                    interstate.setColor(BaseColor.BLACK);
                    break;
            }
            if (weight.equals("bold")) {
                interstate.setStyle(Font.BOLD);
            } else {
                interstate.setStyle(Font.NORMAL);
            }
        }
        return interstate;
    }

    private Font setInterstateFont(int size, String color) {
        return setInterstateFont(size, color, "normal");
    }

    private Font setInterstateFont(int size) {
        return setInterstateFont(size, "black");
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
        if (value - Math.floor(value) == 0) {
            return String.format("%,.0f", value);
        }
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

    private void buildPdf() {
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
                centreCounter++;
            }
            document.close();
            Platform.runLater(() -> c.updateGUI(true));
            System.out.println("PDF généré avec succès");
        } catch (Exception e) {
            Platform.runLater(() -> c.updateGUI(false));
            ControllerPopUpGestion.setError(e);
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
        preface.add(details); // Ajoute le paragraphe 'details' à la page de garde
        Paragraph periode = new Paragraph(year, setInterstateFont(18));
        periode.setAlignment(Element.ALIGN_CENTER);
        addEmptyLine(preface, 5);
        preface.add(periode);

        document.add(preface);
    }

    private void addPage(Document document) throws Exception {
        document.setPageSize(PageSize.A4.rotate());
        Anchor anchor = new Anchor("CENTRE : " + centreName[centreCounter], setInterstateFont(16));
        Chapter chapter = new Chapter(new Paragraph(anchor), centreCounter + 1);

        Paragraph paragraph = new Paragraph();
        addEmptyLine(paragraph, 1);
        createTable(paragraph);
        chapter.add(paragraph);
        document.add(chapter);
    }

    private void createTable(Paragraph paragraph) throws Exception {
        int indicateurNo = 1;
        for (int i = 0; i < database.indicateurArray.length; i++) {
            // TITLE
            Paragraph title = new Paragraph(database.categorie[i], setInterstateFont(12, "black", "bold"));
            paragraph.add(title);
            title.setSpacingBefore(12);
            title.setSpacingAfter(0);
            addEmptyLine(paragraph, 1);
            // TABLE
            PdfPTable table = new PdfPTable(17);
            table.setWidths(new int[]{1, 4, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 4, 3});
            table.setWidthPercentage(108);

            // HEADER
            String[] headerArray = {" ", "INDICATEURS", "MOY." + (yearInt - 1), "Janvier", "Février", "Mars", "Avril", "Mai", "Juin", "Juillet",
                    "Août", "Sept.", "Octobre", "Novembre", "Décembre", "TOTAL", "MOYENNE"};
            for (String aMonthArray : headerArray) {
                PdfPCell titleCell = new PdfPCell(new Phrase(aMonthArray, setInterstateFont(9, "white")));
                centerContent(titleCell);
                titleCell.setBackgroundColor(BLUE_ASD);
                table.addCell(titleCell);
            }

            for (int j = 0; j < database.indicateurArray[i].length; j++) {
                // Update progress Bar
                Platform.runLater(() -> c.updateProgress());
                Database.Query currentIndicateur = database.indicateurArray[i][j];
                System.out.println(currentIndicateur);
                // COLUMN_NO
                PdfPCell numberCell = new PdfPCell(new Phrase(String.valueOf(indicateurNo), setInterstateFont(8)));
                indicateurNo++;
                centerContent(numberCell);
                table.addCell(numberCell);

                // TITLE_COLUMN
                PdfPCell indicateurCell = new PdfPCell(new Phrase(currentIndicateur.toString().replace("_", " "), setInterstateFont(7, "black", "bold")));
                centerContent(indicateurCell);
                indicateurCell.setBackgroundColor(ORANGE_ASD);
                table.addCell(indicateurCell);

                // LAST YEAR MEAN CELLS
                double totalCount = 0;
                String query = database.setQuery(currentIndicateur);
                ps = conn.prepareStatement(query);
                ps.setString(1, (yearInt - 1 + "01"));
                ps.setString(2, (yearInt - 1 + "12"));
                if (centreNo[centreCounter] == 997) {
                    ps.setString(3, "%");
                } else {
                    ps.setInt(3, (centreNo[centreCounter]));
                }
                // Check if the SQL query takes 6 parameters
                final boolean MORE_PARAMETERS_NEEDED = (
                        currentIndicateur.equals(RECETTE_OA_PAR_J_PRESTE) ||
                                currentIndicateur.equals(RECETTE_OA_PAR_J_AVEC_SOINS) ||
                                currentIndicateur.equals(RECETTE_OA_PAR_VISITE) ||
                                currentIndicateur.equals(TAUX_ADMINISTRATIF) ||
                                currentIndicateur.equals(TAUX_ADMINISTRATIF_IC) ||
                                currentIndicateur.equals(TAUX_SMG) ||
                                currentIndicateur.equals(VISITES_PAR_J_PRESTES) ||
                                currentIndicateur.equals(VISITES_PAR_J_AV_SOINS) ||
                                currentIndicateur.equals(RECETTE_TOTALE_PAR_J_AVEC_SOINS)
                );

                if (MORE_PARAMETERS_NEEDED) {
                    ps.setString(4, (yearInt - 1 + "01"));
                    ps.setString(5, (yearInt - 1 + "12"));
                    if (centreNo[centreCounter] == 997) {
                        ps.setString(6, "%");
                    } else {
                        ps.setInt(6, (centreNo[centreCounter]));
                    }
                }
                if (currentIndicateur.equals(RECETTE_TOTALE_PAR_J_AVEC_SOINS)) {
                    ps.setString(7, (yearInt - 1 + "01"));
                    ps.setString(8, (yearInt - 1 + "12"));
                    if (centreNo[centreCounter] == 997) {
                        ps.setString(9, "%");
                    } else {
                        ps.setInt(9, (centreNo[centreCounter]));
                    }
                }
                // Check if the SQL query takes 11 parameters
                if (currentIndicateur.equals(TAUX_SMG)) {
                    ps.setString(7, (yearInt - 1 + "01"));
                    ps.setInt(8, yearInt - 1);
                    ps.setString(9, (yearInt - 1 + "01"));
                    ps.setString(10, (yearInt - 1 + "12"));
                    if (centreNo[centreCounter] == 997) {
                        ps.setString(11, "%");
                    } else {
                        ps.setInt(11, (centreNo[centreCounter]));
                    }
                }
                rs = ps.executeQuery();
                while (rs.next()) {
                    totalCount += rs.getDouble("TOTAL");
                }
                double lastYearMean = totalCount / 12;
                PdfPCell lastYearMeanCell = new PdfPCell(new Phrase(format(lastYearMean, 2), setInterstateFont(8)));
                System.out.println("MOYENNE PRÉCÉDENTE : " + totalCount / 12);
                lastYearMeanCell.setBackgroundColor(ORANGE_ASD);
                centerContent(lastYearMeanCell);
                table.addCell(lastYearMeanCell);
                rs.close();
                ps.close();

                // RESULT CELLS
                totalCount = 0;
                int columnCounter = 0;
                query = database.setQuery(currentIndicateur);
                ps = conn.prepareStatement(query);
                ps.setString(1, (yearInt + "01"));
                ps.setString(2, (yearInt + "12"));
                if (centreNo[centreCounter] == 997) {
                    ps.setString(3, "%");
                } else {
                    ps.setInt(3, (centreNo[centreCounter]));
                }
                // Check if the SQL query takes 6 parameters
                if (MORE_PARAMETERS_NEEDED) {
                    ps.setString(4, (yearInt + "01"));
                    ps.setString(5, (yearInt + "12"));
                    if (centreNo[centreCounter] == 997) {
                        ps.setString(6, "%");
                    } else {
                        ps.setInt(6, (centreNo[centreCounter]));
                    }
                }
                if (currentIndicateur.equals(RECETTE_TOTALE_PAR_J_AVEC_SOINS)) {
                    ps.setString(7, (yearInt + "01"));
                    ps.setString(8, (yearInt + "12"));
                    if (centreNo[centreCounter] == 997) {
                        ps.setString(9, "%");
                    } else {
                        ps.setInt(9, (centreNo[centreCounter]));
                    }
                }
                // Check if the SQL query takes 11 parameters
                if (currentIndicateur.equals(TAUX_SMG)) {
                    ps.setString(7, yearInt + "01");
                    ps.setInt(8, yearInt);
                    ps.setString(9, (yearInt + "01"));
                    ps.setString(10, (yearInt + "12"));
                    if (centreNo[centreCounter] == 997) {
                        ps.setString(11, "%");
                    } else {
                        ps.setInt(11, (centreNo[centreCounter]));
                    }
                }
                rs = ps.executeQuery();
                while (rs.next()) {
                    System.out.println("ROW :" + rs.getDouble("TOTAL"));
                    PdfPCell cell = new PdfPCell(new Phrase(format(rs.getDouble("TOTAL"), 2), setInterstateFont(8)));
                    totalCount += rs.getDouble("TOTAL");
                    centerContent(cell);
                    table.addCell(cell);
                    columnCounter++;
                }
                // Basically, meanCounter will be the divider for the Mean Cell.
                int meanCounter = columnCounter;
                // Loop to add 0,00 to the row if RS returns less than 12 results, meaning the year is not complete yet.
                while (columnCounter < 12) {
                    PdfPCell cell = new PdfPCell(new Phrase(format(0, 2), setInterstateFont(9)));
                    totalCount += 0;
                    centerContent(cell);
                    table.addCell(cell);
                    columnCounter++;
                }

                // TOTAL CELLS
                PdfPCell totalCell = new PdfPCell(new Phrase(format(totalCount, 2), setInterstateFont(9)));
                System.out.println("TOTAL ROW :" + totalCount);
                centerContent(totalCell);
                totalCell.setBackgroundColor(ORANGE_ASD);
                table.addCell(totalCell);

                // MEAN CELLS
                PdfPCell meanCell = new PdfPCell(new Phrase(format(totalCount / meanCounter, 2), setInterstateFont(8)));
                System.out.println("MEAN ROW :" + totalCount / meanCounter);
                System.out.println("----------------\n");
                centerContent(meanCell);
                if (lastYearMean < totalCount / meanCounter && meanCounter >= 12) {
                    meanCell.setBackgroundColor(GREEN_ASD);
                } else if (lastYearMean > totalCount / meanCounter && meanCounter >= 12) {
                    meanCell.setBackgroundColor(LIGHT_RED);
                } else {
                    meanCell.setBackgroundColor(ORANGE_ASD);
                }
                table.addCell(meanCell);
            }
            paragraph.add(table);
        }
    }

    public class FooterPageEvent extends PdfPageEventHelper {
        @Override
        public void onEndPage(PdfWriter writer, Document document) {
            try {
                if (document.getPageNumber() > 1 && document.getPageNumber() != 4 && document.getPageNumber() != 7
                        && document.getPageNumber() != 10 && document.getPageNumber() != 13 && document.getPageNumber() != 16) {
                    addFooter(writer, SheetOrientation.LANDSCAPE);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}


