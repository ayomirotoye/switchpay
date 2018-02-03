/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.model;

import java.util.Date;

/**
 *
 * @author Supersoft Technology
 */
public class Filenames {
    private int id;

    public String getFile_name() {
        return file_name;
    }

    public void setFile_name(String file_name) {
        this.file_name = file_name;
    }
    private String file_name;
    private String uploaded_by;
    private Date date_uploaded;
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
   


    public String getUploaded_by() {
        return uploaded_by;
    }

    public void setUploaded_by(String uploaded_by) {
        this.uploaded_by = uploaded_by;
    }

    public Date getDate_uploaded() {
        return date_uploaded;
    }

    public void setDate_uploaded(Date date_uploaded) {
        this.date_uploaded = date_uploaded;
    }
}
