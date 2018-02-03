/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.model;

import java.util.Date;

/**
 *
 * @author Super Soft Tech
 */
public class Payments {

    private int id;
    private String first_name;
    private String last_name;
    private String account_name;

    public String getAccount_name() {
        return account_name;
    }

    public void setAccount_name(String account_name) {
        this.account_name = account_name;
    }
    private String account_number;
    private String bank_name;
    private String amount;
    private String narrations;

    public String getNarrations() {
        return narrations;
    }

    public void setNarrations(String narrations) {
        this.narrations = narrations;
    }
    private String token;
    private int payment_status;
    private String bank_code;
    private String account_numbers;
    private String bvn;
    private String kyc;

    public String getKyc() {
        return kyc;
    }

    public void setKyc(String kyc) {
        this.kyc = kyc;
    }

    public String getBvn() {
        return bvn;
    }

    public void setBvn(String bvn) {
        this.bvn = bvn;
    }

    public String getAccount_numbers() {
        return account_numbers;
    }

    public void setAccount_numbers(String account_numbers) {
        this.account_numbers = account_numbers;
    }

    public String getBank_code() {
        return bank_code;
    }

    public void setBank_code(String bank_code) {
        this.bank_code = bank_code;
    }

    private String uploaded_by;
    private int file_id;
    private String file_name;
    private Date date_uploaded;

    public int getFile_id() {
        return file_id;
    }

    public void setFile_id(int file_id) {
        this.file_id = file_id;
    }

    public String getFile_name() {
        return file_name;
    }

    public void setFile_name(String file_name) {
        this.file_name = file_name;
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

//    public String getOther_name() {
//        return other_name;
//    }
//
//    public void setOther_name(String other_name) {
//        this.other_name = other_name;
//    }

    public String getAccount_number() {
        return account_number;
    }

    public void setAccount_number(String account_number) {
        this.account_number = account_number;
    }

    public String getBank_name() {
        return bank_name;
    }

    public void setBank_name(String bank_name) {
        this.bank_name = bank_name;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

  
    public int getPayment_status() {
        return payment_status;
    }

    public void setPayment_status(int payment_status) {
        this.payment_status = payment_status;
    }
}
