/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.service;

import com.model.Accounts;
import com.model.CustomerBankAccounts;
import com.model.FinancialInstitution;
import com.model.MiniStatement;
import com.model.Transactions;
//import com.model.Transactions;
import com.model.User;
import java.util.List;
import java.util.Map;

/**
 *
 * @author ajibade
 */

public interface SimpleDbService {
    User getUserProfile (String username);
    List<CustomerBankAccounts> getCustomerBankAccounts (String username);
    List<User> getAllUserProfile ();
    List<Accounts> getAllAccounts ();
    void updateLastLoginDate(String username);
    boolean changeUserPassword(String username, String nPassword, String oPassword);
    boolean addUser(String username, String firstName, String surname, String phoneNumber, String accountNumber, String accountName, String kycLevel, String bvn, String password);
    String getPassword (String username);
    boolean insertTransactions (String[] transactions);
    boolean validateToken (String transactionToken, String username);
    List<FinancialInstitution> getAllFinancialInstitutions ();
    boolean insertAirtime (String username, String network, String bPhone, String amount, String debitSessionId, String responseCode);
    boolean insertForTransferService (String[] transactions);
    List<MiniStatement> getMinistatement (String username);
    boolean changeUserToken (String username, String nToken, String oToken);
//    public List<Transactions> getTransactions (String instCode, int maxNoOfRecords);
    public List<User> getUsersByLimit(String limit);
    public List<User> searchAllUserProfile(String table_name, String search_by, String search_details);
    public List<User> searchUsersByLimit(String table_name, String search_by, String search_details, String limit);
    public List<Transactions> getTransactions (String instCode);
    public int[] deleteUsers(String array[]);
    public int editUser(User user);
    public int newUser(User user);
    public  List<Map<String, Object>> queryUsers();
    public List<Map<String, Object>> queryUsersByLimit(String limit);
    public List<Transactions> getTransactionsByLimit(String instCode, String limit);
}