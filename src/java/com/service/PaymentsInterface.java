/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.service;

import com.model.Filenames;
import com.model.Payments;
import java.util.List;
import java.util.Date;
/**
 *
 * @author Ayomide
 */
public interface PaymentsInterface  {
   public int[] paymentUploads (String sql[]);
   public int insertFilename(String sql, String path, String username, Date date);
   public List<Filenames> getFilenames (String uploader);
   public List<Payments> getPayments(int fileid);
   public int updatePayment(Payments payment);
   public int activatePayment(Payments payment);
   public int deletePayment(Payments payment);
   public int suspendPayment(Payments payment);
}
