package com;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import javafx.print.PageLayout;
import javafx.print.Printer;
import javafx.print.PrinterJob;
import javafx.scene.Node;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;


public class PrintOut {

    public static final void printerPrint(Node node) {
        Printer printer = Printer.getDefaultPrinter();
        PrinterJob job = PrinterJob.createPrinterJob();


        if (job != null) {
            job.showPrintDialog(Main.getPrimaryStage());
            job.printPage(node);
            job.endJob();
        }
    }

    public static final void pdfPrint() {

    }
}
