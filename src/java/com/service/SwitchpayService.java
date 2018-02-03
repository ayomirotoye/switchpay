/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.service;

import com.model.settlements;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

/**
 *
 * @author Super Soft Tech
 */
@Service
public class SwitchpayService {

    @Autowired
    DataSource dataSource;

    @Autowired
    JdbcTemplate jdbcTemplate;

    java.util.Date today = Calendar.getInstance().getTime();

//    public List getSettlement(String bankcode){
//    List <Transactions> reports; 
//        String sql = "SELECT * FROM tbl_transactions WHERE" + " "
//                     + " tbl_transactions.destination_institution_code ='"+bankcode+"' "+ "OR tbl_transactions.session_id LIKE '"+bankcode + "%_"+"'";
//       
//        
//        System.out.println("sql:"+sql);
//        reports = jdbcTemplate.query(sql, (ResultSet rs, int i) -> {
//            Transactions trans = new Transanction();
//            trans.setSession_id(rs.getString("session_id"));
//            trans.setName_enquiry_ref(rs.getString("name_enquiry_ref"));
//            trans.setAmount(rs.getDouble("amount"));
//            trans.setBeneficiary_account_number(rs.getString("beneficiary_account_number"));
//            trans.setBeneficiary_account_name(rs.getString("beneficiary_account_name"));
//            trans.setDestination_institution_code(rs.getString("destination_institution_code"));
//            trans.setResponse_code(rs.getString("response_code"));  
//            return trans;   
//        }); 
//        return reports;
//    }
//    public List getDisputes(String bankcode) {
//        List<Disputes> reportedDisputes;
//        String sql = "SELECT * FROM tbl_disputes WHERE session_id LIKE '"+ bankcode+"%_'"+" OR logged_by ='"+bankcode+"'";
//        System.out.println("sql:" + sql);
//        
//        reportedDisputes = jdbcTemplate.query(sql, (ResultSet rs, int i) -> {
//            Disputes dispute= new Disputes();
//                dispute.setSession_id(rs.getString("session_id"));
//                dispute.setLogged_by(rs.getString("logged_by"));
//                dispute.setDate_logged(rs.getString("date_logged"));
//                dispute.setStatus(rs.getString("status"));
//            return dispute;
//        });
//        return reportedDisputes;
//    }
    public String bankcode(String username) {
//        String sql = "SELECT bank_id FROM tbl_users WHERE username =?";
        String sql2 = "SELECT bankcode FROM tbl_users LEFT JOIN tbl_banks ON tbl_users.bank_id = tbl_banks.bank_id WHERE tbl_users.username =?";
        String bankcode;
        System.out.println("sql:" + sql2);
        try {
            bankcode = this.jdbcTemplate.queryForObject(sql2, new Object[]{username}, String.class);
        } catch (DataAccessException e) {
            return "";
        }

        return bankcode;
    }
//    @Override
//    public int logDispute(Disputes dispute) {
//            String sql = "INSERT INTO tbl_disputes (SESSION_ID,LOGGED_BY, DATE_LOGGED, DESCRIPTION) VALUES (?,?,?,?)";
//            
//            SimpleDateFormat sdf = new SimpleDateFormat ("yyyy-MM-dd::hh:mm:ss");
//            String date_logged = sdf.format(today);
//            
//        final PreparedStatementCreator psc = (final Connection connection) -> {
//            final PreparedStatement ps = connection.prepareStatement(sql);
//            ps.setString(1, dispute.getSession_id());
//            ps.setString(2, dispute.getLogged_by());
//            ps.setString(3, date_logged);
//            ps.setString(4, dispute.getDescription());
//            return ps;
//        };
//        
//        int status = this.jdbcTemplate.update(psc);
//        
//        return status;
//    }
//    @Override
//    public int updateDispute(Disputes dispute) {
//        String sql = "UPDATE tbl_disputes SET STATUS = 1 WHERE SESSION_ID= ?";
//
//        final PreparedStatementCreator psc = (final Connection connection) -> {
//            final PreparedStatement ps = connection.prepareStatement(sql);
//            ps.setString(1, dispute.getSession_id());
//            
//            return ps;
//        };
//
//        int status = this.jdbcTemplate.update(psc);
//
//        return status;
//    }
//    public List getAllTransactions(String bankcode, String type) throws IOException{
//        
//        Map<Integer, String> dbResults = new HashMap();
//        
//        List<String> allTransactions;
//        String sql = "SELECT * FROM tbl_transactions WHERE" + " "
//                     + " tbl_transactions.destination_institution_code = ? OR tbl_transactions.session_id LIKE ?";
////        final PreparedStatementCreator psc = (final Connection connection) -> {
////            final PreparedStatement ps = connection.prepareStatement(sql);
////            ps.setString(1, bankcode);
////            ps.setString(2, bankcode+"%_");
////            return ps;
////        };
//
//        allTransactions =  jdbcTemplate.queryForList(sql, new Object[]{bankcode, bankcode+"%_"}, String.class);
//        
//        System.out.println("zzzzzzzzzzzz:"+allTransactions.size());
//        dbResults.size();
//        for(int i=0; i< allTransactions.size();i++){
//            System.out.println("dd:"+allTransactions.get(i));
//        }
//        return allTransactions;
//    }

    public List<String> getColumnNames(String tableName) {
        List<String> columnNames = null;
        String sql = "SELECT  column_name FROM Information_schema.columns WHERE TABLE_NAME ='" + tableName + "'";

        try {
            columnNames = jdbcTemplate.queryForList(sql, String.class);
        } catch (DataAccessException e) {
            System.out.println("error:" + e.getMessage());
        }

        return columnNames;
    }

    public Connection getDBConnection() throws Exception {
        Connection conn = null;

        String url = "jdbc:mysql://localhost:3306/dblmfbwebchannel";
        String dbUser = "root";
        String dbPass = "root";

        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(url, dbUser, dbPass);
        } catch (Exception e) {
            e.printStackTrace();
        }

//        try {
//            javax.naming.Context initContext = new InitialContext();
//            javax.naming.Context envContext = (javax.naming.Context) initContext.lookup("java:/comp/env");
//            DataSource ds = (DataSource) envContext.lookup("jdbc/CollectAm");
//
//            conn = ds.getConnection();
//        }
//
//        catch (Exception ee) {
//            ee.printStackTrace();
//        }
        return conn;
    }

    public ArrayList getRecords(List<String> getColumnNames) {

        ArrayList<String[]> records = new ArrayList<>();
        ArrayList<String> tb_columns = new ArrayList<>();
        //String arrRecords[] = new String [noOfCols];
        Connection conn = null;
        ResultSet result = null;
//        ResultSet result2 = null;
        PreparedStatement prepstmt = null;
//        PreparedStatement prepstmt2 = null;

//        String placeholders[] = {bankcode, bankcode + "%_"};
        String sql = "SELECT * FROM tbl_transactions";

        String columnNames = "SELECT  column_name FROM Information_schema.columns WHERE TABLE_NAME = ?";

        try {
            conn = getDBConnection();
            prepstmt = conn.prepareStatement(sql);
//            prepstmt2 = conn.prepareStatement(columnNames);

//            // Get column names
//            prepstmt2.setString(1, "tbl_transactions");
//            result2 = prepstmt2.executeQuery();
//            while (result2.next()) {
//                tb_columns.add(result.getString("column_name"));
//            }
            int noOfCols = getColumnNames.size();

            //Prepare preparedStatement by attaching placeholders
//            for (int i = 0; i < placeholders.length; i++) {
//                prepstmt.setString(i + 1, placeholders[i]);
//            }
            result = prepstmt.executeQuery();
            while (result.next()) {
                String arrRecords[] = new String[noOfCols];
                for (int i = 0; i < noOfCols; i++) {
                    arrRecords[i] = result.getString(i + 1);
                }
                records.add(arrRecords);
                //arrRecords = new String [noOfCols];
            }

        } catch (Exception ex) {
            System.out.println("error:" + ex.getMessage());
            ex.printStackTrace();
        } finally {
            try {

                closeConnections(conn, prepstmt, result);
//                closeConnections(conn, prepstmt2, result2);
            } catch (Exception ex) {
                System.out.println("Utility :: getRecords :: Error Occurred...");
                ex.printStackTrace();
            }
        }

        return records;
    }

    public void closeConnections(Connection con, PreparedStatement stmt, ResultSet res) {
        try {
            res.close();
            stmt.close();
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public int uploadSettlementReport(String filename, String location, String total_amount_received, String total_amount_sent) {
        String sql = "INSERT INTO tbl_settlements (filename, location, total_amount_received, total_amount_sent, settlement_date)VALUES (?,?,?,?, now())";

        int retVal = jdbcTemplate.update(sql,
                new Object[]{
                    filename,
                    location,
                    total_amount_received,
                    total_amount_sent
                });

        return retVal;
    }

    public List<settlements> getSettlements() {
        List<settlements> settlementReports;

        String sql = "SELECT * FROM tbl_settlements";

        settlementReports = jdbcTemplate.query(sql, new BeanPropertyRowMapper(settlements.class));

        return settlementReports != null ? settlementReports : null;
    }
    public List<settlements> getSettlementsByLimit(String limit) {
        List<settlements> settlementReports;

        String sql = "SELECT * FROM tbl_settlements "+limit;
        System.out.println("limisst:"+sql);

        settlementReports = jdbcTemplate.query(sql, new BeanPropertyRowMapper(settlements.class));

        return settlementReports != null ? settlementReports : null;
    }
   
    public List<settlements> searchSettlements(String table_name, String search_by, String search_details) {
        String sql = "SELECT * FROM " + table_name + " WHERE " + search_by + " LIKE '%" + search_details + "%'";
        List<settlements> settlementss;
        try{
            settlementss = jdbcTemplate.query(sql, new BeanPropertyRowMapper(settlements.class));
        }catch(DataAccessException ex){
            settlementss = Collections.EMPTY_LIST;
            return settlementss;
        }
            
        return settlementss;
    }



    public List<settlements> searchSettlementsByLimit(String table_name, String search_by, String search_details, String limit) {
        List<settlements> searchSettlements;
        String sql = "SELECT * FROM " + table_name + " WHERE " + search_by + " LIKE '%" + search_details + "%' " + limit;
        try {
            searchSettlements = jdbcTemplate.query(sql, new BeanPropertyRowMapper(settlements.class));
        } catch (DataAccessException ex) {
            searchSettlements = Collections.EMPTY_LIST;
            return searchSettlements;
        }

        return searchSettlements;
    }

}
