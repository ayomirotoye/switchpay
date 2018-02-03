/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.controllers;

import au.com.bytecode.opencsv.CSVWriter;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.service.SwitchpayService;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author Supersoft
 */
public class CronController implements ServletContextAware {
    @Autowired
    SwitchpayService switchService;
    public static final String SAVE_DIRECTORY = "reports";
    private static SimpleDateFormat sdf1 = new SimpleDateFormat("MM_dd_yyyy");
    
    private static ServletContext servletContext;

    @Override
    public void setServletContext(ServletContext servletCtx) {
        CronController.servletContext = servletCtx;
    }
    
    @Scheduled(cron="0 */5 * * * ?")
    @RequestMapping(value = "/report_downloads", method = RequestMethod.POST)
    public void getReports() {
        LocalDate date = LocalDate.now();
        DayOfWeek today = date.getDayOfWeek();
        
        ModelAndView model = new ModelAndView();
//        List<String[]> getAllTransactions = null;
        List<String> getColumnNames = null;
        ArrayList<String[]> records = null;

        String filepath = null;
        String filepath2 = null;
        String zipFilepath = null;
        int result =0;
        // getAllTransactions = switchService.getAllTransactions(bankcode, type);
        getColumnNames = switchService.getColumnNames("tbl_transactions");
        records = switchService.getRecords(getColumnNames);

        if (!records.isEmpty()) {

            try {
                filepath = pdfReport(records, getColumnNames, "pdf");
                
                filepath2 = pdfReport(records, getColumnNames, "csv");
                
            } catch (IOException ex) {
//                Logger.getLogger(SwitchPayController.class.getName()).log(Level.SEVERE, null, ex);
            }
            String location = pathToFile("zip");
            String filename = "settlement_"+ today+".zip";
            final int BUFFER = 2048;
            try {
                BufferedInputStream origin = null;
                FileOutputStream dest = new FileOutputStream(location);
                ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(dest));
                byte data[] = new byte[BUFFER];

                String files[] = {filepath, filepath2};
                for (String file : files) {
                    System.out.println("Adding: " + file);
                    FileInputStream fi = new FileInputStream(file);
                    origin = new BufferedInputStream(fi, BUFFER);
                    ZipEntry entry = new ZipEntry(file);
                    System.out.println("name:" + entry.getName());
                    out.putNextEntry(entry);
                    int count;
                    while ((count = origin.read(data, 0, BUFFER)) != -1) {
                        out.write(data, 0, count);
                        System.out.println("count:" + count);
                    }
                    origin.close();
                }
                out.close();
                
            } catch (IOException e) {
                System.out.println("error:"+e.getMessage());
            }
            result = switchService.uploadSettlementReport(filename, location, "", "");

        }
        System.out.println("ff:" + filepath);
        System.out.println("ff2:" + filepath2);
        System.out.println("uploaded:");
        model.addObject("filepath", filepath);
        
        
    }
    public static String pdfReport(ArrayList<String[]> userR, List<String> headings, String type) throws FileNotFoundException, IOException {
        String FilePath;
        if (type.trim().equalsIgnoreCase("pdf")) {
            FilePath = pathToFile(type.trim());
            pdf(userR, headings, FilePath);

        } else {
            FilePath = pathToFile(type.trim());
            csv(userR, headings, FilePath);

        }

        return FilePath;
    }
    
    public static String pathToFile(String type) {

        String filename;
        StringBuilder sb = new StringBuilder();
        java.util.Date today = Calendar.getInstance().getTime();
//      String appPath = new File(" ").getAbsolutePath().trim();
        String appPath = servletContext.getRealPath("/");
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
