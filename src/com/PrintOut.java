package com;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfBody;
import com.itextpdf.text.pdf.PdfWriter;
import javafx.print.*;
import javafx.scene.Node;
import javafx.stage.Stage;

import java.io.FileOutputStream;
import java.io.IOException;

public class PrintOut {

    public static void printerPrint(Node node) {
        // TODO : PRINT MISE EN PAGE
        Printer printer = Printer.getDefaultPrinter();
        PageLayout pageLayout = printer.createPageLayout(Paper.A4, PageOrientation.LANDSCAPE, Printer.MarginType.HARDWARE_MINIMUM);
        PrinterJob job = PrinterJob.createPrinterJob();
        if (job != null) {
            job.showPrintDialog(new Stage());
            job.printPage(pageLayout, node);
            job.endJob();
        }
    }

    public static void pdfPrint(String fileName) throws DocumentException, IOException {
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(fileName));
        document.open();
        document.add(new Paragraph(String.valueOf(Main.getPrimaryStage())));
        document.close();

    }
}




