/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.service;

import com.model.Filenames;
import com.model.Payments;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Service;

/**
 *
 * @author Ayomide
 */
@Service
public class PaymentService {

    @Autowired
    DataSource dataSource;
    @Autowired
    JdbcTemplate jdbcTemplate;

    public int[] paymentUploads(String sql[]) {
        int[] result = this.jdbcTemplate.batchUpdate(sql);
        return result;
    }

    public int insertFilename(String sql, String path, String username, Date date) {
        final String uu = username;
        final String ff = path;

        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
        String today = sdf1.format(date);

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(
                connection -> {
                    PreparedStatement ps = connection.prepareStatement(sql, new String[]{"ID"});
                    ps.setString(1, ff);
                    ps.setString(2, uu);
                    ps.setString(3, today);
                    return ps;
                }, keyHolder);

        Number key = keyHolder.getKey();
//        final PreparedStatementCreator psc = (final Connection connection) -> {
//            final PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
//            ps.setString(1, ff);
//            ps.setString(2, uu);
//            ps.setString(3, today);
//            return ps;
//        };

        long me = key.longValue();
//        int result = this.jdbcTemplate.update(psc, keyHolder);
        System.out.println("rr:" + me);

        return (int) me;
    }

    public List<Payments> getPaymentsByLimit(int fileid, String limit) {
        String sql = "SELECT * from tbl_salaries WHERE file_id = ? " + limit;
        System.out.println("sql:" + sql);
//// String sql = "SELECT * from tbl_salaries WHERE file_id = ? ";
        final PreparedStatementCreator psc = (final Connection connection) -> {
            final PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, fileid);
            return ps;
        };

        return jdbcTemplate.query(psc, (ResultSet rs, int rownumber) -> {
            Payments pay = new Payments();
            pay.setId(rs.getInt("id"));
            pay.setBank_code(rs.getString("bank_code"));
            pay.setAccount_numbers(rs.getString("account_numbers"));
            pay.setAccount_name(rs.getString("account_name"));
            pay.setBvn(rs.getString("bvn"));
            pay.setKyc(rs.getString("kyc"));
            pay.setNarrations(rs.getString("narrations"));
// pay.setBank_name(rs.getString("bank_name")); 
// pay.setAccount_number(rs.getString("account_number")); 
            pay.setAmount(rs.getString("amount"));
// pay.setNarration(rs.getString("narration"));
            pay.setPayment_status(rs.getInt("payment_status"));
            return pay;
        });

    }

    public List<Payments> allPayments(int fileid) {
        String sql = "SELECT * FROM tbl_salaries WHERE file_id = ?";
        final PreparedStatementCreator psc = (final Connection connection) -> {
            final PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, fileid);
            return ps;
        };

        return jdbcTemplate.query(psc, (ResultSet rs, int rownumber) -> {
            Payments pay = new Payments();
            pay.setId(rs.getInt("id"));
            pay.setBank_code(rs.getString("bank_code"));
            pay.setAccount_numbers(rs.getString("account_numbers"));
            pay.setAccount_name(rs.getString("account_name"));
            pay.setBvn(rs.getString("bvn"));
            pay.setKyc(rs.getString("kyc"));
            pay.setNarrations(rs.getString("narrations"));
// pay.setBank_name(rs.getString("bank_name")); 
// pay.setAccount_number(rs.getString("account_number")); 
            pay.setAmount(rs.getString("amount"));
// pay.setNarration(rs.getString("narration"));
            pay.setPayment_status(rs.getInt("payment_status"));
            return pay;
        });

    }

    public List<Filenames> getFilenames(String uploader) {
        List<Filenames> myFiles;
        String sql = "SELECT * from tbl_filenames WHERE uploaded_by = ?";
        final PreparedStatementCreator psc = (final Connection connection) -> {
            final PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, uploader);
            return ps;
        };

        myFiles = jdbcTemplate.query(psc, new BeanPropertyRowMapper(Filenames.class));
//        return jdbcTemplate.query(psc, (ResultSet rs, int rownumber) -> {
//            Filenames fil = new Filenames();
//            fil.setId(rs.getInt("id"));
//            fil.setFile_name(rs.getString("file_name"));
//            fil.setDate_uploaded(rs.getDate("date_uploaded"));
//            fil.setUploaded_by(rs.getString("uploaded_by"));
//            
//            return fil;
//        });
        return myFiles.size() > 0 ? myFiles : null;
    }
    
    public List<Filenames> getFilenamesByLimit(String uploader, String limit) {
        List<Filenames> myFiles;
        String sql = "SELECT * from tbl_filenames WHERE uploaded_by = ? "+limit;
        final PreparedStatementCreator psc = (final Connection connection) -> {
            final PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, uploader);
            return ps;
        };

        myFiles = jdbcTemplate.query(psc, new BeanPropertyRowMapper(Filenames.class));

        return myFiles.size() > 0 ? myFiles : null;
    }

    public List<Filenames> searchFiles(String table_name, String search_by, String search_details) {
        List<Filenames> myFiles;
        String sql = "SELECT * FROM "+table_name+" WHERE " + search_by+ " LIKE '%"+search_details+"%'";
        System.out.println("sqqq:"+sql);
//        final PreparedStatementCreator psc = (final Connection connection) -> {
//            final PreparedStatement ps = connection.prepareStatement(sql);
//            ps.setString(1, table_name);
//            ps.setString(2, search_by);
//            ps.setString(3, search_details);
//            return ps;
//        };

        myFiles = jdbcTemplate.query(sql, new BeanPropertyRowMapper(Filenames.class));

        return myFiles.size() > 0 ? myFiles : null;
    }
    
    public List<Filenames> searchFilesByLimit(String table_name, String search_by, String search_details, String limit) {
        List<Filenames> myFiles;
        String sql = "SELECT * FROM "+table_name+" WHERE " + search_by+ " LIKE '%"+search_details+"%' "+ limit;
        System.out.println("sqqq:" + sql);
//        final PreparedStatementCreator psc = (final Connection connection) -> {
//            final PreparedStatement ps = connection.prepareStatement(sql);
//            ps.setString(1, table_name);
//            ps.setString(2, search_by);
//            ps.setString(3, search_details);
//            return ps;
//        };

        myFiles = jdbcTemplate.query(sql, new BeanPropertyRowMapper(Filenames.class));

        return myFiles.size() > 0 ? myFiles : null;
    }
    public int[] deleteFiles(String array[]) {

        String sql = " DELETE FROM tbl_filenames WHERE id =";
        String query[] = new String[array.length];
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < array.length; i++) {

            sb.append(sql).append("");
            sb.append(array[i]);
            query[i] = sb.toString();
            System.out.println("sqllll:" + sb.toString());
            sb.setLength(0);
        }

        int[] result = jdbcTemplate.batchUpdate(query);
        System.out.println("");

        System.out.println("deleted:" + result.length);

        return result;
    }
    
    public List<Payments> getPayments(int fileid) {

        String sql = "SELECT * from tbl_salaries WHERE file_id = ?";
        final PreparedStatementCreator psc = (final Connection connection) -> {
            final PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, fileid);
            return ps;
        };

        return jdbcTemplate.query(psc, (ResultSet rs, int rownumber) -> {
            Payments pay = new Payments();
            pay.setId(rs.getInt("id"));
            pay.setBank_code(rs.getString("bank_code"));
            pay.setAccount_numbers(rs.getString("account_numbers"));
            pay.setAccount_name(rs.getString("account_name"));
            pay.setBvn(rs.getString("bvn"));
            pay.setKyc(rs.getString("kyc"));
            pay.setNarrations(rs.getString("narrations"));
//            pay.setBank_name(rs.getString("bank_name")); 
//            pay.setAccount_number(rs.getString("account_number"));   
            pay.setAmount(rs.getString("amount"));
//            pay.setNarration(rs.getString("narration"));
            pay.setPayment_status(rs.getInt("payment_status"));
            return pay;
        });

    }
    
    

    public int updatePayment(Payments payment) {
        int result = 0;

        // update
        String sql = "UPDATE tbl_salaries SET account_name=?, bank_code=?, account_numbers=?, amount=?, narrations=? WHERE id=?";

        result = jdbcTemplate.update(sql,
                payment.getAccount_name(),
                payment.getBank_code(),
                payment.getAccount_numbers(),
                payment.getAmount(),
                payment.getNarrations(),
                payment.getId()
        );

        return result;
    }

    public int activatePayment(Payments payment) {
        int result;

        // update
        String sql = "UPDATE tbl_salaries SET payment_status=1 WHERE id=?";
        result = jdbcTemplate.update(sql, payment.getId());
        return result;
    }

    public int suspendPayment(Payments payment) {
        int result;

        // update
        String sql = "UPDATE tbl_salaries SET payment_status=0 WHERE id=?";
        result = jdbcTemplate.update(sql, payment.getId());
        return result;
    }

    public int deletePayment(Payments payment) {
        int result;

        // update
        String sql = "DELETE FROM tbl_salaries WHERE id=?";
        result = jdbcTemplate.update(sql, payment.getId());
        return result;
    }
}
