package com;

import javafx.print.Printer;
import javafx.print.PrinterJob;
import javafx.scene.Node;


public class Print {

    public static final void print(Node node){
        Printer printer = Printer.getDefaultPrinter();
        /* PageLayout pageLayout = printer.createPageLayout(Paper.A4, PageOrientation.LANDSCAPE,
                Printer.MarginType.HARDWARE_MINIMUM); */
        PrinterJob job = PrinterJob.createPrinterJob();
        if(job != null){
            job.showPrintDialog(Main.getPrimaryStage());
            job.printPage(node);
            job.endJob();
        }
    }
}
