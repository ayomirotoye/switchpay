/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.util;

import au.com.bytecode.opencsv.CSVWriter;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author Ayomide
 */
public class GenerateReport {

    public static final String SAVE_DIRECTORY = "reports";
    private static SimpleDateFormat sdf1 = new SimpleDateFormat("MM_dd_yyyy_hh_mm_ss");

    public static String pdfReport(ArrayList<String[]> userR, List<String> headings, String type) throws FileNotFoundException, IOException {
        String FilePath = "";
        if (type.trim().equalsIgnoreCase("pdf")) {
            FilePath = pathToFile(type.trim());
            pdf(userR, headings, FilePath);

        } else {
            FilePath = pathToFile(type.trim());
            csv(userR, headings, FilePath);

        }
// return FilePath;

        return FilePath;
    }

    public static String pathToFile(String type) {
        
        String filename = "";
        StringBuilder sb = new StringBuilder();
        java.util.Date today = Calendar.getInstance().getTime();
        String appPath = new File(" ").getAbsolutePath().trim();
        // String FilePath = null;

        appPath = appPath.replace('\\', '/');
        String ServerDir = "";
        if (appPath.endsWith("/")) {
            ServerDir = appPath + SAVE_DIRECTORY;
        } else {
            ServerDir = (appPath + "/" + SAVE_DIRECTORY).trim();
        }

        File fileSaveDir = new File(ServerDir);
        if (!fileSaveDir.exists()) {
            fileSaveDir.mkdir();
        }

        if (type.equalsIgnoreCase("pdf")) {
            filename = sb.append(ServerDir).append("/settlement").append(sdf1.format(today)).append(".pdf").toString();
        } else if ((type.equalsIgnoreCase("csv"))) {
            filename = sb.append(ServerDir).append("/settlement").append(sdf1.format(today)).append(".csv").toString();
        } else {
            filename = sb.append(ServerDir).append("/settlement").append(sdf1.format(today)).append(".zip").toString();
        }
        return filename;
    }

    private static String pdf(List<String[]> userR, List<String> headings, String file) throws FileNotFoundException {
        String path = file;

        Document my_pdf_report = new Document(PageSize.LEDGER);
        try {
            PdfWriter.getInstance(my_pdf_report, new FileOutputStream(path));
            my_pdf_report.open();
            //we have four columns in our table
            PdfPTable my_report_table = new PdfPTable(headings.size());
            //create a cell object
            PdfPCell table_cell;

            for (String str : headings) {
                String field = str;
                table_cell = new PdfPCell(new Phrase(field));
                my_report_table.addCell(table_cell);
            }
            for (int i = 0; i < userR.size(); i++) {
                for (String str : userR.get(i)) {
                    String field = str;
                    table_cell = new PdfPCell(new Phrase(field));
                    my_report_table.addCell(table_cell);
                }
            }
            //Attach report table to PDF /
            my_pdf_report.add(my_report_table);
            my_pdf_report.close();
        } catch (DocumentException ex) {
            Logger.getLogger(GenerateReport.class.getName()).log(Level.SEVERE, null, ex);
        }
        return path;
    }

    private static String csv(List<String[]> userR, List<String> headings, String file) throws IOException {
        String path = file;
        String array[] = new String[headings.size()];
        try (CSVWriter writer = new CSVWriter(new FileWriter(path))) {
            for (int i = 0; i < headings.size(); i++) {
                array[i] = headings.get(i);
            }
            writer.writeNext(array);

            userR.forEach((_item) -> {
                userR.forEach((str) -> {
                    writer.writeNext(str);
                });
            });
        }
        return path;
    }

}
