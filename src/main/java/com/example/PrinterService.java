package com.example;

import javax.print.*;
import javax.print.attribute.*;
import java.io.*;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import jakarta.inject.Inject;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class PrinterService {
    @Inject
    @ConfigProperty(name = "printer.name", defaultValue = "TSC TE300")
    String printerName;

    @Inject
    @ConfigProperty(name = "printer.density", defaultValue = "8")
    int printerDensity;

    @Inject
    @ConfigProperty(name = "printer.gap.horizontal", defaultValue = "2")
    int gapHorizontal;

    @Inject
    @ConfigProperty(name = "printer.gap.vertical", defaultValue = "9")
    int gapVertical;

    @Inject
    @ConfigProperty(name = "printer.label.width", defaultValue = "154")
    int labelWidth;

    @Inject
    @ConfigProperty(name = "printer.label.height", defaultValue = "24")
    int labelHeight;

    public boolean sendToPrinter(String tspl) {
        try {
            // Find the printer by name (as installed in Windows)
            PrintService[] printServices = PrintServiceLookup.lookupPrintServices(null, null);
            PrintService printer = null;
            for (PrintService ps : printServices) {
                if (ps.getName().contains(printerName)) { // Use configurable printer name
                    printer = ps;
                    break;
                }
            }
            if (printer == null) {
                System.out.println("Printer not found!");
                return false;
            }

            DocPrintJob job = printer.createPrintJob();
            DocFlavor flavor = DocFlavor.BYTE_ARRAY.AUTOSENSE;
            Doc doc = new SimpleDoc(tspl.getBytes("UTF-8"), flavor, null);
            job.print(doc, null);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Generates a TSPL command string for a label with a barcode, value, and info line.
     */
    public static String generateTSPL(String barcode, String value, String info, int count) {
        return "SIZE 100 mm,50 mm\n" +
               "GAP 2 mm,0 mm\n" +
               "DENSITY 8\n" +
               "DIRECTION 1\n" +
               "CLS\n" +
               // Barcode (Code128), at (60,30), height 60, human readable below
               "BARCODE 60,30,\"128\",60,1,0,2,2,\"" + barcode + "\"\n" +
               // Barcode text (centered under barcode)
               "TEXT 110,100,\"3\",0,1,1,\"" + barcode + "\"\n" +
               // Value (large font), at (60,150)
               "TEXT 60,150,\"4\",0,1,1,\"" + value + "\"\n" +
               // Info line, at (80,200)
               "TEXT 80,200,\"3\",0,1,1,\"" + info + "\"\n" +
               "PRINT 1," + count + "\n";
    }

    /**
     * Sends a label to the printer with the given barcode, value, info, and print count.
     */
    public boolean sendToPrinter(String barcode, String value, String info, int count) {
        String tspl = generateTSPL(barcode, value, info, count);
        return sendToPrinter(tspl);
    }

    /**
     * Generates a TSPL command string for two labels (left and right) on a double label.
     */
    public String generateDoubleLabelTSPL(
        String leftBarcode, String leftValue, String leftInfo,
        String rightBarcode, String rightValue, String rightInfo,
        int count
    ) {
        return "SIZE " + labelWidth + " mm," + labelHeight + " mm\n" +
               "GAP " + gapHorizontal + " mm," + gapVertical + " mm\n" +
               "DENSITY " + printerDensity + "\n" +
               "DIRECTION 1\n" +
               "CLS\n" +
               // Left label (start at x=10)
               "BARCODE 180,0,\"128\",50,0,0,3,3,\"" + leftBarcode + "\"\n" +
               "TEXT 240,57,\"3\",0,1,1,\"" + leftBarcode + "\"\n" +
               "TEXT 200,130,\"4\",0,1,1,\"" + leftValue + "\"\n" +
               "TEXT 210,180,\"3\",0,1,1,\"" + leftInfo + "\"\n" +
               // Right label (start at x=87, which is 77+10)
               "BARCODE 880,0,\"128\",50,0,0,3,3,\"" + rightBarcode + "\"\n" +
               "TEXT 940,57,\"3\",0,1,1,\"" + rightBarcode + "\"\n" +
               "TEXT 900,130,\"4\",0,1,1,\"" + rightValue + "\"\n" +
               "TEXT 910,180,\"3\",0,1,1,\"" + rightInfo + "\"\n" +
               "PRINT 1," + count + "\n";
    }

    /**
     * Sends a double label (left and right) to the printer.
     */
    public boolean sendDoubleLabelToPrinter(
        String leftBarcode, String leftValue, String leftInfo,
        String rightBarcode, String rightValue, String rightInfo,
        int count
    ) {
        String tspl = generateDoubleLabelTSPL(leftBarcode, leftValue, leftInfo, rightBarcode, rightValue, rightInfo, count);
        return sendToPrinter(tspl);
    }

} 