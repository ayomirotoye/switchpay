/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.util;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.zip.*;

public class Zip {

    static final int BUFFER = 2048;
    public static final String SAVE_DIRECTORY = "zipp";
    private static SimpleDateFormat sdf1 = new SimpleDateFormat("MM_dd_yyyy_hh_mm_ss");

    public static void main(String argv[]) {
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

        filename = sb.append(ServerDir).append("/settlement").append(sdf1.format(today)).append(".zip").toString();

        try {
            BufferedInputStream origin = null;
            FileOutputStream dest = new FileOutputStream(filename);
            ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(dest));
            //out.setMethod(ZipOutputStream.DEFLATED);
            byte data[] = new byte[BUFFER];
            // get a list of files from current directory
            File f = new File("C:\\apache-tomcat-9.0.0.M17\\bin\\reports");
// String files[] = {"C:\\apache-tomcat-8.5.24\\bin\\reports\\settlement01_26_2018_06_34_27.pdf", "C:\\apache-tomcat-8.5.24\\bin\\reports\\settlement01_26_2018_06_34_29.csv"};
            File files[] = f.listFiles();
            for (File file : files) {
                System.out.println("Adding: " + file.toString());
                FileInputStream fi = new FileInputStream(file);
                origin = new BufferedInputStream(fi, BUFFER);
                ZipEntry entry = new ZipEntry(file.toString());
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
            e.printStackTrace();
        }
        System.out.println("sugarRRush");
    }
}
