package main;

import javafx.print.*;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.transform.Scale;
import javafx.stage.Stage;

class Print {

    // Create the JobStatus Label
    private final Label jobStatus = new Label();

    void printSetup(Node node, Stage owner) {
        // Create the PrinterJob
        PrinterJob job = PrinterJob.createPrinterJob();

        if (job == null) {
            return;
        }
        // Show the print setup dialog
        boolean proceed = job.showPrintDialog(owner);

        if (proceed) {
            print(job, node);
        }
    }

    private void print(PrinterJob job, Node node) {
        // Set the Job Status Message
        jobStatus.textProperty().bind(job.jobStatusProperty().asString());

        Printer printer = Printer.getDefaultPrinter();
        PageLayout pageLayout
                = printer.createPageLayout(Paper.A4, PageOrientation.LANDSCAPE, Printer.MarginType.HARDWARE_MINIMUM);
        double scaleX
                = pageLayout.getPrintableWidth() / node.getBoundsInParent().getWidth();
        double scaleY
                = pageLayout.getPrintableHeight() / node.getBoundsInParent().getHeight();
        Scale scale = new Scale(scaleX, scaleY);
        node.getTransforms().add(scale);

        // Print the node
        boolean printed = job.printPage(pageLayout, node);

        if (printed) {
            job.endJob();
        }
        node.getTransforms().remove(scale);
    }
}
