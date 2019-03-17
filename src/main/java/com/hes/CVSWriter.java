package com.hes;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

public class CVSWriter {
    private static String SAMPLE_CSV_FILE = "/tmp/sample.csv";
    private static String file_name = "/tmp/sample.csv";
    private static CSVPrinter csvPrinter = null;

    public static void setName(String name){
        file_name = name;
    }

    public static CSVPrinter getPrinter(){
        if (csvPrinter == null) {
            try {
                BufferedWriter writer = Files.newBufferedWriter(Paths.get(file_name));
                csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT);
                        //.withHeader("Email"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return csvPrinter;
    }

    static void println(){
        CSVPrinter csvPrinter = getPrinter();
        try {
            csvPrinter.println();
        } catch (Exception e) {

        }
    }
    static void writeRecord(String name, String email){
        CSVPrinter csvPrinter = getPrinter();
        try {
            csvPrinter.printRecord(name, email);

        } catch (Exception e) {

        }
    }

    static void writeRecord(String name, String email, String phone, String address){
        CSVPrinter csvPrinter = getPrinter();
        try {
            csvPrinter.printRecord(name, email, phone, address);

        } catch (Exception e) {

        }
    }

    static void writeRecord(String email){
        CSVPrinter csvPrinter = getPrinter();
        try {
            csvPrinter.printRecord(email);

        } catch (Exception e) {

        }
    }
    static void flush(){
        CSVPrinter csvPrinter = getPrinter();
        try {
            csvPrinter.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
