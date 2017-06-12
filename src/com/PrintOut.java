package com;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfBody;
import com.itextpdf.text.pdf.PdfWriter;
import javafx.print.Printer;
import javafx.print.PrinterJob;
import javafx.scene.Node;

import java.io.FileOutputStream;
import java.io.IOException;

public class PrintOut {

    public static final void printerPrint(Node node) {
        // TODO : PRINT MISE EN PAGE
        Printer printer = Printer.getDefaultPrinter();
        PrinterJob job = PrinterJob.createPrinterJob();
        if (job != null) {
            job.showPrintDialog(Main.getPrimaryStage());
            job.printPage(node);
            job.endJob();
        }
    }

    public static final void pdfPrint(String fileName) throws DocumentException, IOException {
        // step 1
        Document document = new Document();
        // step 2
        PdfWriter.getInstance(document, new FileOutputStream(fileName));
        // step 3
        document.open();
        // step 4
        document.add(new Paragraph(String.valueOf(Main.getPrimaryStage())));
        // step 5
        document.close();

    }
}
