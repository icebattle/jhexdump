package com.wayneizatt;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class JHexDump {

    private static void dump(FileInputStream in, int numlines)
            throws IOException {
        int c;
        int linestoprint;
        int linesprinted = 0;
        byte[] barr = new byte[16];
        int pos = 0;
        int offset = 0;

        if (numlines == 0) {
            linestoprint = Integer.MAX_VALUE;
        } else {
            linestoprint = numlines;
        }
        while (((c = in.read()) != -1) && (linesprinted < linestoprint)) {
            barr[pos++] = (byte) c;
            if (pos == 16) {
                printline(barr, pos, offset);
                offset += 16;
                pos = 0;
                linesprinted++;
            }
        }
        if (pos > 0) { // file is finished, but we have a few bytes left to
            // print
            printline(barr, pos, offset);
        }
    }

    private static void printline(byte[] arr, int length, int offset) {
        length--;
        String byteline = String.format("%08X ", offset);
        String charline = new String();
        int i;
        for (i = 0; i <= length; i++) {
            byteline = byteline + String.format(" %02X", arr[i]);
            if (arr[i] > 31 && arr[i] < 127) {
                charline = charline + (char)arr[i];
            } else {
                charline = charline + ".";
            }
        }
        if (i < 16) {
            while (i < 16) {
                byteline = byteline + "   ";
                charline = charline + ".";
                i++;
            }
        }
        byteline = byteline + "  |";
        charline = charline + "|";
        System.out.println(byteline + charline);
    }

    public static void main(String[] args) throws IOException {

        if(args.length == 0) {
            usage();
            System.exit(1);
        }

        FileInputStream in = null;
        int numlines = 0;
        try {
            in = new FileInputStream(args[0]);
            if (args.length > 1) {
                numlines = new Integer(args[1]).intValue();
            }
            dump(in, numlines);
        } catch (FileNotFoundException fnfex) {
            System.err.println("File not found: " + args[0]);
            usage();
        } catch (NumberFormatException nfex) {
            System.err.println("Invalid number of lines specified: " + args[1]);
            usage();
        } finally {
            if (in != null) {
                in.close();
            }
        }
    }

    private static void usage() {
        System.err.println("Usage: JHexDump file <number of lines to dump>");
    }
}