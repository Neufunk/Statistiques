package si;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import javafx.application.Platform;
import tools.Console;
import tools.DatabaseConnection;
import tools.ExceptionHandler;
import tools.Identification;

import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Date;

import static si.Queries.Query.*;

class IndicateurDeGestion implements Runnable {

    private final BaseColor BLUE_ASD = new BaseColor(0, 110, 130);
    private final BaseColor ORANGE_ASD = new BaseColor(254, 246, 233);
    private final BaseColor GREEN_ASD = new BaseColor(236, 244, 215);
    private final BaseColor LIGHT_RED = new BaseColor(255, 230, 230);
    private String centreName;
    private int centreNo, chapterCounter = 1;
    private final ControllerPopUpGestion c;
    private final int YEAR;
    private final Queries queries = new Queries();
    private final DatabaseConnection databaseConnection = new DatabaseConnection();
    private final Identification id = new Identification();
    private Connection conn;
    private PreparedStatement ps;
    private ResultSet rs;
    private final String[] CENTRE_NAME = {"Philippeville", "Ciney", "Gedinne", "Eghezée", "Namur", "Province"};
    private final int[] CENTRE_NO = {902, 913, 923, 931, 961, 997};

    private final Queries.Query[][] INDICATEUR_ARRAY = {
            // TARIFICATION
            {RECETTE_TOTALE, TARIFICATION_OA, TARIFICATION_NOMENCLATURE, TARIFICATION_FORFAITS_ABC, TARIFICATION_SOINS_SPECIFIQUES, FORFAITS_PALLIATIFS, DEPLACEMENTS,
                    TICKETS_MODERATEURS, SOINS_DIVERS, CONVENTIONS, RECETTE_OA_PAR_VISITE, RECETTE_OA_PAR_J_PRESTE, RECETTE_OA_PAR_J_AVEC_SOINS, RECETTE_TOTALE_PAR_J_AVEC_SOINS},
            // VISITES
            {NOMBRE_DE_VISITES, NOMBRE_DE_VISITES_PAR_FFA, NOMBRE_DE_VISITES_PAR_FFB, NOMBRE_DE_VISITES_PAR_FFC, VISITES_PAR_J_PRESTES, VISITES_PAR_J_AV_SOINS},
            // PATIENTS
            {NOMBRE_DE_PATIENTS, NOMBRE_DE_PATIENTS_FFA, NOMBRE_DE_PATIENTS_FFB, NOMBRE_DE_PATIENTS_FFC, NOMBRE_DE_PATIENTS_PALLIA,
                    TAUX_PATIENTS_NOMENCLATURE, TAUX_PATIENTS_FORFAITS, TAUX_PATIENTS_FFA, TAUX_PATIENTS_FFB, TAUX_PATIENTS_FFC,
                    TAUX_ROTATION_PATIENTS, TAUX_PATIENTS_MC_ACCORD, TAUX_PATIENTS_MC_AUTRES, TAUX_PATIENTS_AUTRES_OA, TAUX_PATIENTS_VIPO, TAUX_PATIENTS_NON_VIPO},
            // SOINS
            {NOMBRE_DE_SOINS, NOMBRE_DE_TOILETTES, NOMBRE_DE_TOILETTES_NOMENCLATURE, NOMBRE_INJECTIONS, NOMBRE_PANSEMENTS, NOMBRE_SOINS_SPECIFIQUES,
                    NOMBRE_CONSULTATIONS_INFI, NOMBRE_DE_PILULIERS, NOMBRE_DE_SOINS_DIVERS, NOMBRE_AUTRES_SOINS, NOMBRE_DE_SOINS_PAR_VISITE,
                    TAUX_TOILETTES, TAUX_TOILETTES_NOMENCLATURE, TAUX_INJECTIONS, TAUX_PANSEMENTS, TAUX_SOINS_DIVERS, TAUX_AUTRES_SOINS},
            // SUIVI DU PERSONNEL
            {J_PRESTES_AVEC_EMSS, J_PRESTES_AVEC_EMAS, EMSS, EMAS, TAUX_ADMINISTRATIF, TAUX_ADMINISTRATIF_IC, RECUPERATIONS, SOLDE_CP, TAUX_SMG}
    };

    IndicateurDeGestion(ControllerPopUpGestion c) {
        this.c = c;
        YEAR = c.getYear();
    }

    public void run() {
        try {
            buildPdf();
        } catch (Exception e) {
            ExceptionHandler.switchException(e, this.getClass());
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

    private void addMetaData(Document document) {
        document.addTitle("Indicateurs de gestion");
        document.addSubject("Statistiques");
        document.addAuthor("Johnathan Vanbeneden");
        document.addCreator("Logiciel Statistiques - Johnathan Vanbeneden");
    }

    private String format(double value) {
        if (value - Math.floor(value) == 0) {
            return String.format("%,.0f", value);
        }
        return String.format("%,." + 2 + "f", value);
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

            PdfPCell cell2 = new PdfPCell(new Phrase(centreName + " - " + YEAR, setInterstateFont(7)));
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
                    "Indicateurs_Gestion_" + YEAR + ".pdf";
            PdfWriter pdfWriter = PdfWriter.getInstance(document, new FileOutputStream(FILE));
            // Footer on every pages BUT last of each center because of a bug
            FooterPageEvent event = new FooterPageEvent();
            pdfWriter.setPageEvent(event);
            document.open();
            addMetaData(document);
            addTitlePage(document);
            this.conn = databaseConnection.connect(
                    id.set(Identification.info.D615_URL),
                    id.set(Identification.info.D615_USER),
                    id.set(Identification.info.D615_PASSWD),
                    id.set(Identification.info.D615_DRIVER)
            );
            // Loop to construct pages for every center
            for (int i = 0; i < CENTRE_NO.length; i++) {
                centreName = CENTRE_NAME[i];
                centreNo = CENTRE_NO[i];
                addPage(document);
                    /* Manually add footer on every last page of each center because there is a bug with
                    /* the FooterPageEvent in iText 5
                     */
                addFooter(pdfWriter, SheetOrientation.LANDSCAPE);
                chapterCounter++;
            }
            document.close();
            Platform.runLater(() -> c.updateGUI(true));
        } catch (Exception e) {
            Platform.runLater(() -> c.updateGUI(false));
            ControllerPopUpGestion.setError(e);
        } finally {
            databaseConnection.close(rs);
            databaseConnection.close(ps);
            databaseConnection.close(conn);
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
        Paragraph periode = new Paragraph(String.valueOf(YEAR), setInterstateFont(18));
        periode.setAlignment(Element.ALIGN_CENTER);
        addEmptyLine(preface, 5);
        preface.add(periode);

        document.add(preface);
    }

    private void addPage(Document document) throws Exception {
        document.setPageSize(PageSize.A4.rotate());
        Anchor anchor = new Anchor("CENTRE : " + centreName, setInterstateFont(16));
        Platform.runLater(() -> Console.appendln("######################### " + centreName + " #########################"));
        Chapter chapter = new Chapter(new Paragraph(anchor), chapterCounter);

        Paragraph paragraph = new Paragraph();
        addEmptyLine(paragraph, 1);
        createTable(paragraph);
        chapter.add(paragraph);
        document.add(chapter);
    }

    private void createTable(Paragraph paragraph) throws Exception {
        int indicateurNo = 1;
        for (int i = 0; i < INDICATEUR_ARRAY.length; i++) {
            // TITLE
            Paragraph title = new Paragraph(queries.CATEGORIE[i], setInterstateFont(12, "black", "bold"));
            paragraph.add(title);
            title.setSpacingBefore(12);
            title.setSpacingAfter(0);
            addEmptyLine(paragraph, 1);
            // TABLE
            PdfPTable table = new PdfPTable(17);
            table.setWidths(new int[]{1, 4, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 4, 3});
            table.setWidthPercentage(108);

            // HEADER
            String[] headerArray = {" ", "INDICATEURS", "MOY." + (YEAR - 1), "Janvier", "Février", "Mars", "Avril", "Mai", "Juin", "Juillet",
                    "Août", "Sept.", "Octobre", "Novembre", "Décembre", "TOTAL", "MOYENNE"};
            for (String aMonthArray : headerArray) {
                PdfPCell titleCell = new PdfPCell(new Phrase(aMonthArray, setInterstateFont(9, "white")));
                centerContent(titleCell);
                titleCell.setBackgroundColor(BLUE_ASD);
                table.addCell(titleCell);
            }

            for (int j = 0; j < INDICATEUR_ARRAY[i].length; j++) {
                // Update progress Bar
                Platform.runLater(c::updateProgress);
                Queries.Query currentIndicateur = INDICATEUR_ARRAY[i][j];
                System.out.println(currentIndicateur);
                Platform.runLater(() -> Console.appendln("----------------------------------- " + currentIndicateur.toString()));
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
                String query = queries.selectQuery(currentIndicateur);
                ps = conn.prepareStatement(query);
                rs = queries.setQuery(currentIndicateur, ps, YEAR - 1, centreNo);
                while (rs.next()) {
                    totalCount += rs.getDouble("TOTAL");
                }
                rs.close();
                double lastYearMean = totalCount / 12;
                PdfPCell lastYearMeanCell = new PdfPCell(new Phrase(format(lastYearMean), setInterstateFont(8)));
                System.out.println("MOYENNE PRÉCÉDENTE : " + totalCount / 12);
                double finalTotalCount = totalCount;
                Platform.runLater(() -> Console.appendln("PREVIOUS AVERAGE : " + finalTotalCount / 12));
                lastYearMeanCell.setBackgroundColor(ORANGE_ASD);
                centerContent(lastYearMeanCell);
                table.addCell(lastYearMeanCell);

                // RESULT CELLS
                totalCount = 0;
                int columnCounter = 0;
                rs = queries.setQuery(currentIndicateur, ps, YEAR, centreNo);
                while (rs.next()) {
                    double result = rs.getDouble("TOTAL");
                    System.out.println("ROW: " + result);
                    Platform.runLater(() -> Console.appendln("ROW: " + result));
                    PdfPCell cell = new PdfPCell(new Phrase(format(result), setInterstateFont(8)));
                    totalCount += result;
                    centerContent(cell);
                    table.addCell(cell);
                    columnCounter++;
                }
                rs.close();
                ps.close();
                // Basically, meanCounter will be the divider for the Mean Cell.
                int meanCounter = columnCounter;
                // Loop to add 0,00 to the row if RS returns less than 12 results, meaning the year is not complete yet.
                while (columnCounter < 12) {
                    PdfPCell cell = new PdfPCell(new Phrase(format(0), setInterstateFont(9)));
                    centerContent(cell);
                    table.addCell(cell);
                    columnCounter++;
                }

                // TOTAL CELLS
                PdfPCell totalCell = new PdfPCell(new Phrase(format(totalCount), setInterstateFont(9)));
                System.out.println("TOTAL: " + totalCount);
                double finalTotalCount1 = totalCount;
                Platform.runLater(() -> Console.appendln("TOTAL ROW: " + finalTotalCount1));
                centerContent(totalCell);
                totalCell.setBackgroundColor(ORANGE_ASD);
                table.addCell(totalCell);

                // MEAN CELLS
                final double MEAN = totalCount / meanCounter;
                PdfPCell meanCell = new PdfPCell(new Phrase(format(MEAN), setInterstateFont(8)));
                System.out.println("MEAN ROW: " + MEAN);
                System.out.println("----------------\n");
                Platform.runLater(() -> Console.appendln("AVERAGE: " + MEAN));
                Platform.runLater(() -> Console.appendln("----------------\n"));
                centerContent(meanCell);
                if (meanCounter >= 12 && lastYearMean < MEAN) {
                    meanCell.setBackgroundColor(GREEN_ASD);
                } else if (meanCounter >= 12 && lastYearMean > MEAN) {
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
                ExceptionHandler.switchException(e, this.getClass());
            }
        }
    }
}


