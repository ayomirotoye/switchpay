/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.model;

/**
 *
 * @author Supersoft
 */
public class settlements {
    private int id;
    private String filename;
    private String location;

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
    private String total_amount_sent;
    private String total_amount_received;
    private String settlement_date;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getTotal_amount_sent() {
        return total_amount_sent;
    }

    public void setTotal_amount_sent(String total_amount_sent) {
        this.total_amount_sent = total_amount_sent;
    }

    public String getTotal_amount_received() {
        return total_amount_received;
    }

    public void setTotal_amount_received(String total_amount_received) {
        this.total_amount_received = total_amount_received;
    }

    public String getSettlement_date() {
        return settlement_date;
    }

    public void setSettlement_date(String settlement_date) {
        this.settlement_date = settlement_date;
    }
}
