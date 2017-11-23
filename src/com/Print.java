package com;

import javafx.print.*;
import javafx.scene.Node;
import javafx.stage.Stage;

public class Print {

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
}




