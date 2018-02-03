/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.service;

import com.model.Accounts;
import com.model.CustomerBankAccounts;
import com.model.Filenames;
import com.model.FinancialInstitution;
import com.model.MiniStatement;
import com.model.Payments;
import com.model.Transactions;
import com.model.User;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

/**
 *
 * @author ajibade
 */
@Service
public class SimpleDbServiceImpl implements SimpleDbService {

    @Autowired
    DataSource dataSource;

    @Autowired
    JdbcTemplate jdbcTemplate;

    
    
    @Override
    public  List<Map<String, Object>> queryUsers() {
        String sql = "SELECT * FROM tbl_user_profile INNER JOIN tbl_users ON tbl_user_profile.username = tbl_users.username";
        List<Map<String, Object>> list;
        try{
             list = jdbcTemplate.queryForList(sql);
        }catch(DataAccessException ex){
            list = Collections.EMPTY_LIST;
        }
        return list;
    }
    
    @Override
    public List<Map<String, Object>> queryUsersByLimit(String limit) {
        String sql = "SELECT * FROM tbl_user_profile INNER JOIN tbl_users ON tbl_user_profile.username = tbl_users.username "+limit;
        List<Map<String, Object>> list;
        try {
            list = jdbcTemplate.queryForList(sql);
        } catch (DataAccessException ex) {
            list = Collections.EMPTY_LIST;
        }
        return list;
    }
    
    @Override
    public User getUserProfile(String username) {
        String sql = "select * from tbl_user_profile left JOIN tbl_users ON tbl_user_profile.id=tbl_users.id LEFT JOIN tbl_customer_accounts on tbl_users.id=tbl_customer_accounts.id";
//        String sql = "SELECT * FROM tbl_user_profile WHERE username='" + username + "'";
        List<User> users = jdbcTemplate.query(sql, new UserProfileMapper());
        return users.size() > 0 ? users.get(0) : null;
    }

    class UserProfileMapper implements RowMapper<User> {

        @Override
        public User mapRow(ResultSet rs, int arg1) throws SQLException {
            User user = new User();
            user.setUsername(rs.getString("username"));
            user.setFirstName(rs.getString("firstname"));
            user.setSurname(rs.getString("surname"));
            user.setPhoneNumber(rs.getString("phonenumber"));
            user.setStatus(rs.getString("status"));
            user.setCreationdate(rs.getString("creationdate"));
            user.setLastlogindate(rs.getString("lastlogindate"));
            user.setUsername(rs.getString("username"));
            user.setAccountNumber(rs.getString("accountnumber"));
            user.setAccountName(rs.getString("accountname"));
            user.setBvn(rs.getString("bvn"));
            user.setKycLevel(rs.getString("kyclevel"));

            return user;
        }
    }

    @Override
    //public CustomerBankAccounts getCustomerBankAccounts (String username) {
    public List<CustomerBankAccounts> getCustomerBankAccounts(String username) {
        String sql;
        if (username.equalsIgnoreCase("*")) {
            sql = "SELECT * FROM tbl_customer_accounts";
        } else {
            sql = "SELECT * FROM tbl_customer_accounts WHERE username='" + username + "'";
        }

        List<CustomerBankAccounts> userAccounts = jdbcTemplate.query(sql, new CustomerAccountsMapper());
        return userAccounts;

    }
    
    

    class CustomerAccountsMapper implements RowMapper<CustomerBankAccounts> {

        public CustomerBankAccounts mapRow(ResultSet rs, int arg1) throws SQLException {
            CustomerBankAccounts customerBankAccounts = new CustomerBankAccounts();
            customerBankAccounts.setUsername(rs.getString("username"));
            customerBankAccounts.setAccountNumber(rs.getString("accountnumber"));
            customerBankAccounts.setAccountName(rs.getString("accountname"));
            customerBankAccounts.setBvn(rs.getString("bvn"));
            customerBankAccounts.setKycLevel(rs.getString("kyclevel"));

            return customerBankAccounts;
        }
    }

    @Override
    public List<User> getAllUserProfile() {
//        String sql = "SELECT * FROM tbl_user_profile INNER JOIN tbl_users ON tbl_user_profile.username = tbl_users.username";
        String sql = "select * from tbl_user_profile left JOIN tbl_users ON tbl_user_profile.id=tbl_users.id LEFT JOIN tbl_customer_accounts on tbl_users.id=tbl_customer_accounts.id";

        List<User> users = jdbcTemplate.query(sql, new UserProfileMapper());
        return users;
    }

//    class UserAllProfileMapper implements RowMapper<User> {
//
//        public User mapRow(ResultSet rs, int arg1) throws SQLException {
//            User user = new User();
//            user.setUsername(rs.getString("username"));
//            user.setFirstName(rs.getString("firstname"));
//            user.setSurname(rs.getString("surname"));
//            user.setPhoneNumber(rs.getString("phonenumber"));
//            user.setPassword(rs.getString("password"));
//            user.setStatus(rs.getString("status"));
//            user.setCreationdate(rs.getString("creationdate"));
//            user.setLastlogindate(rs.getString("lastlogindate"));
//
//            return user;
//        }
//    }

    @Override
    public List<User> searchAllUserProfile(String table_name, String search_by, String search_details) {
        String sql = "SELECT * FROM " + table_name + " WHERE " + search_by + " LIKE '%" + search_details + "%'";
        List<User> users;
        try{
            users = jdbcTemplate.query(sql, new UserProfileMapper());
        }catch(DataAccessException ex){
            users = Collections.EMPTY_LIST;
        }
        
        return users;
    }

//    class searchUserAllProfileMapper implements RowMapper<User> {
//
//        @Override
//        public User mapRow(ResultSet rs, int arg1) throws SQLException {
//            User user = new User();
//            user.setUsername(rs.getString("username"));
//            user.setFirstName(rs.getString("firstname"));
//            user.setSurname(rs.getString("surname"));
//            user.setPhoneNumber(rs.getString("phonenumber"));
//            user.setStatus(rs.getString("status"));
//            user.setCreationdate(rs.getString("creationdate"));
//            user.setLastlogindate(rs.getString("lastlogindate"));
//
//            return user;
//        }
//    }

    @Override
    public List<User> searchUsersByLimit(String table_name, String search_by, String search_details, String limit) {
        List<User> searchUsers;
        String sql = "SELECT * FROM " + table_name + " WHERE " + search_by + " LIKE '%" + search_details + "%' " + limit;
        System.out.println("sqqq:" + sql);

        try {
            searchUsers = jdbcTemplate.query(sql, new UserProfileMapper());
        } catch (DataAccessException ex) {
            searchUsers = Collections.EMPTY_LIST;
        }

        return searchUsers;
    }

    @Override
    public List<User> getUsersByLimit(String limit) {
        String sql =  "select * from tbl_user_profile left JOIN tbl_users ON tbl_user_profile.id=tbl_users.id LEFT JOIN tbl_customer_accounts on tbl_users.id=tbl_customer_accounts.id "+ limit;
        List<User> searchUsers;
        System.out.println("sqqq:" + sql);

        try {
            searchUsers = jdbcTemplate.query(sql, new UserProfileMapper());
        } catch (DataAccessException ex) {
            searchUsers = Collections.EMPTY_LIST;
        }

        return searchUsers;
    }

//    class UserAllProfileByLimitMapper implements RowMapper<User> {
//
//        @Override
//        public User mapRow(ResultSet rs, int arg1) throws SQLException {
//            User user = new User();
//            user.setUsername(rs.getString("username"));
//            user.setFirstName(rs.getString("firstname"));
//            user.setSurname(rs.getString("surname"));
//            user.setPhoneNumber(rs.getString("phonenumber"));
//            user.setStatus(rs.getString("status"));
//            user.setCreationdate(rs.getString("creationdate"));
//            user.setLastlogindate(rs.getString("lastlogindate"));
//
//            return user;
//        }
//
//    }

    @Override
    public List<Accounts> getAllAccounts() {
        String sql = "SELECT * FROM tbl_customer_accounts";
        List<Accounts> accounts = jdbcTemplate.query(sql, new UserAllAccountsMapper());
        return accounts;
    }

    class UserAllAccountsMapper implements RowMapper<Accounts> {

        public Accounts mapRow(ResultSet rs, int arg1) throws SQLException {
            Accounts accounts = new Accounts();
            accounts.setUsername(rs.getString("username"));
            accounts.setAccountNumber(rs.getString("accountnumber"));
            accounts.setAccountName(rs.getString("accountname"));
            accounts.setBvn(rs.getString("bvn"));
            accounts.setId(rs.getString("id"));
            accounts.setKycLevel(rs.getString("kyclevel"));

            return accounts;
        }
    }

    @Override
    public void updateLastLoginDate(String username) {
        try {
            String sql = "UPDATE tbl_user_profile SET lastlogindate = now() WHERE username = ?";

            int retVal = jdbcTemplate.update(sql, new Object[]{username});

            System.out.println("retVal: " + retVal);
        } catch (Exception ee) {
            ee.printStackTrace();
        }
    }

    @Override
    public boolean changeUserPassword(String username, String nPassword, String oPassword) {
        boolean isChanged = false;

        try {
            String sql = "UPDATE tbl_users SET password = ? WHERE username = ? AND password = ?";

            int retVal = jdbcTemplate.update(sql, new Object[]{nPassword, username, oPassword});

            if (retVal > 0) {
                isChanged = true;
            }

            System.out.println("SimpleDbServiceImpl :: changeUserPassword :: retVal: " + retVal);
        } catch (Exception ee) {
            ee.printStackTrace();
        }

        return isChanged;
    }

    @Override
    public boolean changeUserToken(String username, String nToken, String oToken) {
        boolean isChanged = false;

        try {
            String sql = "UPDATE tbl_transactioncode SET transactionCode = ? WHERE username = ? AND transactionCode = ?";

            int retVal = jdbcTemplate.update(sql, new Object[]{nToken, username, oToken});

            if (retVal > 0) {
                isChanged = true;
            }

            System.out.println("SimpleDbServiceImpl :: changeUserToken :: retVal: " + retVal);
        } catch (Exception ee) {
            ee.printStackTrace();
        }

        return isChanged;
    }

    @Override
    public boolean addUser(String username, String firstName, String surname, String phoneNumber, String accountNumber, String accountName, String kycLevel,
            String bvn, String password) {
        boolean isChanged = false;

        try {
            String sql = "INSERT INTO tbl_user_profile (firstname, surname, phonenumber, status, creationdate, lastlogindate, username) VALUES (?,?,?, 1, now(), now(), ?)";

            int retVal = jdbcTemplate.update(sql, new Object[]{firstName, surname, phoneNumber, username});

            if (retVal > 0) {
                sql = "INSERT INTO tbl_customer_accounts (username, accountnumber, accountname, kyclevel, bvn) VALUES (?,?,?,?,?)";

                retVal = jdbcTemplate.update(sql, new Object[]{username, accountNumber, accountName, kycLevel, bvn});

                if (retVal > 0) {
                    sql = "INSERT INTO tbl_user_roles (username, role) VALUES (?,'ROLE_USER')";

                    retVal = jdbcTemplate.update(sql, new Object[]{username});

                    if (retVal > 0) {
                        sql = "INSERT INTO tbl_users (username, password, enabled) VALUES (?,?,1)";

                        retVal = jdbcTemplate.update(sql, new Object[]{username, password});

                        if (retVal > 0) {
                            isChanged = true;
                        }
                        /*else {
                            sql = "DELETE FROM tbl_user_roles WHERE username=?";

                            retVal = jdbcTemplate.update(sql, new Object[]{username});

                            sql = "DELETE FROM tbl_customer_accounts WHERE username=?";

                            retVal = jdbcTemplate.update(sql, new Object[]{username});

                            sql = "DELETE FROM tbl_user_profile WHERE username=?";

                            retVal = jdbcTemplate.update(sql, new Object[]{username});
                        }*/
                    }
                }
            }

            System.out.println("SimpleDbServiceImpl :: addUser :: retVal: " + retVal);
        } catch (Exception ee) {
            ee.printStackTrace();
        }

        if (isChanged == false) {
            String sql = "DELETE FROM tbl_user_roles WHERE username=?";

            int retVal = jdbcTemplate.update(sql, new Object[]{username});

            sql = "DELETE FROM tbl_customer_accounts WHERE username=?";

            retVal = jdbcTemplate.update(sql, new Object[]{username});

            sql = "DELETE FROM tbl_user_profile WHERE username=?";

            retVal = jdbcTemplate.update(sql, new Object[]{username});
        }

        return isChanged;
    }

    @Override
    public int newUser(User user) {

        String sql = "INSERT INTO tbl_user_profile SET firstname = ?, surname =?, phonenumber = ? WHERE username = ?";
        String sql2 = "UPDATE tbl_users SET password = ?";

        int retVal = jdbcTemplate.update(sql,
                new Object[]{
                    user.getFirstName(),
                    user.getSurname(),
                    user.getPhoneNumber(),
                    user.getUsername()
                });

        int retVal2 = jdbcTemplate.update(sql2,
                new Object[]{
                    user.getPassword()
                });
        int result = retVal * retVal2;
        return result;
    }
    
    
    @Override
    public int editUser(User user) {

        String sql = "UPDATE tbl_user_profile SET firstname = ?, surname =?, phonenumber = ? WHERE username = ?";
        String sql2 = "UPDATE tbl_users SET password = ?";

        int retVal = jdbcTemplate.update(sql,
                new Object[]{
                    user.getFirstName(),
                    user.getSurname(),
                    user.getPhoneNumber(),
                    user.getUsername()
                });

        int retVal2 = jdbcTemplate.update(sql2,
                new Object[]{
                    user.getPassword()
                });
        int result = retVal * retVal2;
        return result;
    }

    @Override
    public String getPassword(String username) {
        String sql = "SELECT password FROM tbl_users WHERE username = '" + username + "'";
        List<String> passLst = jdbcTemplate.query(sql, new UserPasswordMapper());

        String pass = passLst.get(0);

        return pass;
    }

    class UserPasswordMapper implements RowMapper<String> {

        public String mapRow(ResultSet rs, int arg1) throws SQLException {
            String pass = new String();
            pass = rs.getString("password");

            return pass;
        }
    }

    @Override
    public boolean insertTransactions(String[] transactions) {
        boolean isInserted = false;

        try {
            String sql = "INSERT INTO tbl_transactions (srcAccountNumber, srcAccountName, srcKycLevel, srcBvn, beneficiaryAccountNumber, beneficiaryAccountName, beneficiaryKycLevel, "
                    + "beneficiaryBvn, narration, paymentReference, mandateReference, transactiondate, amount, creditordebit, username, sessionId, responsecode, sourceInstitutioncode, "
                    + "destinationInstitutioncode) VALUES (?,?,?,?,?,?,?,?,?,?,?,now(),?,?,?,?,?,?,?)";

            Object[] transObj = new Object[18];

            for (int i = 0; i < transactions.length; i++) {
                transObj[i] = transactions[i];
            }

            int retVal = jdbcTemplate.update(sql, transObj);

            System.out.println("retVal: " + retVal);

            if (retVal > 0) {
                isInserted = true;
            }
        } catch (Exception ee) {
            ee.printStackTrace();
        }
        return isInserted;
    }

    @Override
    public boolean validateToken(String transactionToken, String username) {
        boolean isValid = false;

        Object[] preparedStmtObjs = new Object[2];

        preparedStmtObjs[0] = transactionToken;
        preparedStmtObjs[1] = username;

        String sql = "SELECT transactionCode FROM tbl_transactioncode WHERE transactionCode = ? AND username = ?";
        List<String> passLst = jdbcTemplate.query(sql, preparedStmtObjs, new TransactionTokenMapper());

        if (passLst.size() > 0) {
            isValid = true;
        }

        return isValid;
    }

    class TransactionTokenMapper implements RowMapper<String> {

        public String mapRow(ResultSet rs, int arg1) throws SQLException {
            String transactionCode = new String();
            transactionCode = rs.getString("transactionCode");

            return transactionCode;
        }
    }

    @Override
    public List<FinancialInstitution> getAllFinancialInstitutions() {
        String sql = "SELECT financialInstitutionCode, financialInstitutionName FROM tbl_financialinstitutions ORDER BY financialInstitutionName ASC";
        List<FinancialInstitution> accounts = jdbcTemplate.query(sql, new UserAllFinancialInstitutionsMapper());
        return accounts;
    }

    class UserAllFinancialInstitutionsMapper implements RowMapper<FinancialInstitution> {

        public FinancialInstitution mapRow(ResultSet rs, int arg1) throws SQLException {
            FinancialInstitution financialInstitution = new FinancialInstitution();
            financialInstitution.setCode(rs.getString("financialInstitutionCode"));
            financialInstitution.setName(rs.getString("financialInstitutionName"));

            return financialInstitution;
        }
    }

    @Override
    public boolean insertAirtime(String username, String network, String bPhone, String amount, String debitSessionId, String responseCode) {
        boolean isInserted = false;

        try {
            String sql = "INSERT INTO tbl_airtimevend (username, network, beneficiaryPhone, amount, debitsessionid, isProcessed, responsecode, transactionDate)"
                    + " VALUES (?,?,?,?,?,0,?,now())";

            Object[] transObj = {username, network, bPhone, amount, responseCode, debitSessionId};

            int retVal = jdbcTemplate.update(sql, transObj);

            System.out.println("retVal: " + retVal);

            if (retVal > 0) {
                isInserted = true;
            }

        } catch (Exception ee) {
            ee.printStackTrace();
        }
        return isInserted;
    }

    @Override
    public boolean insertForTransferService(String[] transactions) {
        boolean isInserted = false;

        try {
            String sql = "INSERT INTO tbl_transferservice (srcAccountNumber, srcAccountName, srcKycLevel, srcBvn, beneficiaryAccountNumber, beneficiaryAccountName, beneficiaryKycLevel, "
                    + "beneficiaryBvn, narration, paymentReference, mandateReference, transactiondate, amount, creditordebit, username, sessionId, responsecode) VALUES (?,?,?,?,?,?,?,?,?,?,?,now(),?,?,?,?,?)";

            Object[] transObj = new Object[16];

            for (int i = 0; i < transactions.length; i++) {
                transObj[i] = transactions[i];
            }

            int retVal = jdbcTemplate.update(sql, transObj);

            System.out.println("retVal: " + retVal);

            if (retVal > 0) {
                isInserted = true;
            }
        } catch (Exception ee) {
            ee.printStackTrace();
        }
        return isInserted;
    }

    @Override
    public List<MiniStatement> getMinistatement(String username) {
        String sql = "SELECT amount, beneficiaryAccountName, creditordebit, srcAccountName, transactiondate, responseCode, srcAccountNumber FROM tbl_transactions "
                + "WHERE username = ? ORDER BY transactiondate desc LIMIT 10";

        Object[] preparedStmtObjs = new Object[1];

        preparedStmtObjs[0] = username;

        List<MiniStatement> accounts = jdbcTemplate.query(sql, preparedStmtObjs, new UserMinistatementMapper());

        return accounts;
    }

    class UserMinistatementMapper implements RowMapper<MiniStatement> {

        public MiniStatement mapRow(ResultSet rs, int arg1) throws SQLException {
            MiniStatement stmt = new MiniStatement();

            stmt.setAmount(rs.getString("amount"));
            stmt.setBeneficiary(rs.getString("beneficiaryAccountName"));
            stmt.setCreditOrDebit(rs.getString("creditordebit"));
            stmt.setSource(rs.getString("srcAccountName"));
            stmt.setTransactionDateAndTime(rs.getString("transactiondate"));
            stmt.setTransactionStatus(rs.getString("responseCode"));
            stmt.setFromAccount(rs.getString("srcAccountNumber"));

            return stmt;
        }
    }

    @Override
    public List<Transactions> getTransactions(String instCode) {
        String sql = "SELECT sessionId, sourceInstitutioncode, destinationInstitutioncode, amount, transactiondate, responseCode FROM tbl_transactions WHERE sourceInstitutioncode = ? "
                + "OR destinationInstitutioncode = ? ORDER BY transactiondate";

        System.out.println("SimpleDBService :: getTransactions :: sql: " + sql);
        Object[] preparedStmtObjs = new Object[2];

        preparedStmtObjs[0] = instCode;
        preparedStmtObjs[1] = instCode;

        List<Transactions> transactions = jdbcTemplate.query(sql, preparedStmtObjs, new TransactionMapper());

        return transactions;
    }

    class TransactionMapper implements RowMapper<Transactions> {

        @Override
        public Transactions mapRow(ResultSet rs, int arg1) throws SQLException {
            Transactions transactions = new Transactions();
            transactions.setSessionID(rs.getString("sessionId"));
            transactions.setFromBank(rs.getString("sourceInstitutioncode"));
            transactions.setToBank(rs.getString("destinationInstitutioncode"));
            transactions.setAmount(rs.getString("amount"));
            transactions.setTransactionDate(rs.getString("transactiondate"));
            transactions.setStatus(rs.getString("responseCode"));

            return transactions;
        }
    }

    @Override
    public List<Transactions> getTransactionsByLimit(String instCode, String limit) {
        String sql = "SELECT sessionId, sourceInstitutioncode, destinationInstitutioncode, amount, transactiondate, responseCode FROM tbl_transactions WHERE sourceInstitutioncode = ? "
                + "OR destinationInstitutioncode = ? ORDER BY transactiondate desc " + limit;

        System.out.println("SimpleDBService :: getTransactions :: sql: " + sql);
        Object[] preparedStmtObjs = new Object[2];

        preparedStmtObjs[0] = instCode;
        preparedStmtObjs[1] = instCode;

        List<Transactions> transactions = jdbcTemplate.query(sql, preparedStmtObjs, new TransactionMapper());

        return transactions;
    }
    
    @Override
    public int[] deleteUsers(String array[]) {

        String sql = " DELETE FROM tbl_user_profile WHERE username =";
        String query[] = new String[array.length];
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < array.length; i++) {

            sb.append(sql).append("");
            sb.append("'").append(array[i]).append("'");
            query[i] = sb.toString();
            System.out.println("sqllll:" + sb.toString());
            sb.setLength(0);
        }

        int[] result = jdbcTemplate.batchUpdate(query);
        System.out.println("");

        System.out.println("deleted:" + result.length);

        return result;
    }

}
