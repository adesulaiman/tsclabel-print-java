package com.example;

import java.io.OutputStream;
import java.net.Socket;

public class PrinterService {
    private static final String PRINTER_IP = "192.168.1.100"; // TODO: Set your printer's IP
    private static final int PRINTER_PORT = 9100;

    public static boolean sendToPrinter(String tspl) {
        try (Socket socket = new Socket(PRINTER_IP, PRINTER_PORT);
             OutputStream out = socket.getOutputStream()) {
            out.write(tspl.getBytes("UTF-8"));
            out.flush();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
} 