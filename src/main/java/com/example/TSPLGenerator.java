package com.example;

public class TSPLGenerator {
    public static String generate(LabelRequest req) {
        StringBuilder sb = new StringBuilder();
        sb.append("SIZE 100 mm, 50 mm\n");
        sb.append("GAP 2 mm, 0 mm\n");
        sb.append("CLS\n");
        if (req.getText() != null && !req.getText().isEmpty()) {
            sb.append("TEXT 100,100,\"3\",0,1,1,\"")
              .append(req.getText())
              .append("\"\n");
        }
        if (req.getBarcode() != null && !req.getBarcode().isEmpty()) {
            sb.append("BARCODE 100,200,\"128\",100,1,0,2,2,\"")
              .append(req.getBarcode())
              .append("\"\n");
        }
        sb.append("PRINT 1,1\n");
        return sb.toString();
    }
} 