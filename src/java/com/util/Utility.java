/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.util;

import com.model.CustomerBankAccounts;
import com.service.SimpleDbService;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author ajibade
 */

public class Utility {
    private Logger logger = Logger.getLogger(Utility.class.getName());
    String url;
    String srcInstCode;
    String hashKey;
    SimpleDbService simpleDbService;

    public Utility (String url, String srcInstCode, String hashKey) {
        this.url = url;
        this.srcInstCode = srcInstCode;
        this.hashKey = hashKey;
    }

    public Utility (String url) {
        this.url = url;
    }

    public SimpleDbService getSimpleDbService() {
        return simpleDbService;
    }

    public void setSimpleDbService(SimpleDbService simpleDbService) {
        this.simpleDbService = simpleDbService;
    }

    public boolean getAccountBalance (List<CustomerBankAccounts> customerBankAccounts, String passwordToken, String username) {
        java.text.SimpleDateFormat sformatter = new java.text.SimpleDateFormat ("yyMMddHHmmss");
        String cDateTime = sformatter.format(new java.util.Date());
        boolean isBalReturned = false;

        int j = 1;
        JSONParser jsonParser = new JSONParser();

        try {
            RestTemplate restTemplate = new RestTemplate();
            String uniqueNumber = generateUniqueId();

            String sessionID = srcInstCode + cDateTime + uniqueNumber;

            String hashVal = generateHash512Value(sessionID + username + hashKey);

            String jsonRequest = "{\"sessionid\":\"" + sessionID + "\", "
                                + "\"username\":\"" + username + "\","
                                + "\"hashvalue\":\"" + hashVal + "\"}";

            HttpHeaders headers = new HttpHeaders();

            headers.setContentType(MediaType.APPLICATION_JSON);

            headers.add("principalid", passwordToken);

            HttpEntity<String> entity = new HttpEntity<String>(jsonRequest, headers);

            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);

            logger.info("response status code: " + response.getStatusCode().toString());

            if (response.getStatusCode().toString().equals("200")) {
                logger.info("response body: " + response.getBody());

                if (response.getBody() != null) {
                    String retBody = response.getBody();

                    if (retBody.length() > 16) {
                        JSONObject jsonObject = (JSONObject) jsonParser.parse(retBody);

                        String responseCode = (String) jsonObject.get("responsecode");

                        logger.info("responseCode: " + responseCode);

                        if (responseCode.equals("00")) {
                            JSONArray accountsJson = (JSONArray) jsonObject.get("accounts");
                            Iterator i = accountsJson.iterator();

                            while (i.hasNext()) {
                                JSONObject acctDetails = (JSONObject) i.next();

                                String targetAccountName = (String) acctDetails.get("accountnumber");

                                for (CustomerBankAccounts custItr : customerBankAccounts) {
                                    String bal = (String) acctDetails.get("availablebalance");

                                    if (bal == null || bal.equals("")) {
                                        isBalReturned = false;
                                        continue;
                                    }

                                    if (custItr.getAccountNumber().equals(targetAccountName)) {
                                        NumberFormat numberFormatter = new DecimalFormat("###,###.00");
                                        bal = numberFormatter.format(Double.parseDouble(bal));
                                        custItr.setBalance("N" + bal);

                                        isBalReturned = true;

                                        break;
                                    }
                                }
                            }
                        }
                    }
                }
            }

        } catch (Exception ee) {
            ee.printStackTrace();
        }

        return isBalReturned;
    }

    public List<String> getAccountName (String accountNumber, String destInstCode, String passwordToken, String username) {
        java.text.SimpleDateFormat sformatter = new java.text.SimpleDateFormat ("yyMMddHHmmss");
        String cDateTime = sformatter.format(new java.util.Date());

        List<String> retVal = new ArrayList<String>();

        JSONParser jsonParser = new JSONParser();

        boolean noError = false;

        String responseCode = "";

        try {
            RestTemplate restTemplate = new RestTemplate();

            String uniqueNumber = generateUniqueId();

            String sessionID = srcInstCode + cDateTime + uniqueNumber;

            //String hashInput = sessionID + sessionID.substring(0, 6) + accountNumber + hashKey;
            String hashInput = sessionID + destInstCode + accountNumber + hashKey;

            logger.info("hashInput: " + hashInput);

            String hashVal = generateHash512Value(hashInput);

            String jsonRequest = "{\"sessionid\":\"" + sessionID + "\", "
                                    + "\"destinationinstitutioncode\":\"" + destInstCode + "\","
                                    + "\"username\":\"" + username + "\","
                                    + "\"accountnumber\":\"" + accountNumber + "\","
                                    + "\"hashvalue\":\"" + hashVal + "\"}";

            HttpHeaders headers = new HttpHeaders();

            headers.setContentType(MediaType.APPLICATION_JSON);

            headers.add("principalid", passwordToken);

            HttpEntity<String> entity = new HttpEntity<String>(jsonRequest, headers);

            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);

            logger.info("response status code: " + response.getStatusCode().toString());

            if (response.getStatusCode().toString().equals("200")) {
                logger.info("response body: " + response.getBody());

                if (response.getBody() != null) {
                    String retBody = response.getBody();

                    if (retBody.length() > 16) {
                        JSONObject jsonObject = (JSONObject) jsonParser.parse(retBody);

                        responseCode = (String) jsonObject.get("responsecode");

                        logger.info("responseCode: " + responseCode);

                        if (responseCode.equals("00")) {
                            String accountNumberRet = (String) jsonObject.get("accountnumber");
                            String accountName = (String) jsonObject.get("accountname");
                            String kycLevel = (String) jsonObject.get("kyclevel");
                            String bvn = (String) jsonObject.get("bvn");

                            retVal.add(accountNumberRet);
                            retVal.add(accountName);
                            retVal.add(bvn);
                            retVal.add(kycLevel);

                            logger.info("accountNumberRet: " + accountNumberRet);
                            logger.info("accountName: " + accountName);
                            logger.info("bvn: " + bvn);
                            logger.info("kycLevel: " + kycLevel);

                            noError = true;
                        }

                        else
                            retVal.add(responseCode);
                    }
                }
            }
        } catch (Exception ee) {
            ee.printStackTrace();
        }

        if (noError == false) {
            retVal.add("91");
        }
        return retVal;
    }

    public String processDebitTransfer (String destinationBank, String dAccountNumber, String dAccountName, String dBvn, String dKycLevel, String bAccountNumber, String bAccountName, String bBvn, 
            String bKycLevel, String narration, String amt, String nameEnqRef, String passwordToken, String username, String token/*, String phone, String network, boolean isAirtime*/) {

        java.text.SimpleDateFormat sformatter = new java.text.SimpleDateFormat ("yyMMddHHmmss");
        String cDateTime = sformatter.format(new java.util.Date());

        String responseCode = "91";

        JSONParser jsonParser = new JSONParser();

        try {
            RestTemplate restTemplate = new RestTemplate();

            String uniqueNumber = generateUniqueId();

            String sessionID = srcInstCode + cDateTime + uniqueNumber;

            String hashInput = username + sessionID + destinationBank + nameEnqRef + dAccountNumber + dAccountName + dBvn + dKycLevel + bAccountNumber + bAccountName + bBvn 
                    + bKycLevel + narration + "100001/ref" + sessionID.substring(18) + "0.00" + amt + hashKey;

            logger.info("hashInput: " + hashInput);

            String hashVal = generateHash512Value(hashInput);

            String jsonRequest = "{\"sessionid\":\"" + sessionID + "\", "
                                    + "\"destinationinstitutioncode\":\"" + destinationBank + "\","
                                    + "\"nameenquiryref\":\"" + nameEnqRef + "\","
                                    + "\"debitaccountnumber\":\"" + dAccountNumber + "\","
                                    + "\"debitaccountname\":\"" + dAccountName + "\","
                                    + "\"debitbvn\":\"" + dBvn + "\","
                                    + "\"debitkyclevel\":\"" + dKycLevel + "\","
                                    + "\"beneficiaryaccountnumber\":\"" + bAccountNumber + "\","
                                    + "\"beneficiaryaccountname\":\"" + bAccountName + "\","
                                    + "\"beneficiarybvn\":\"" + bBvn + "\","
                                    + "\"beneficiarykyclevel\":\"" + bKycLevel + "\","
                                    + "\"narration\":\"" + narration + "\","
                                    + "\"paymentreference\":\"100001/ref\","
                                    + "\"mandatereference\":\"" + sessionID.substring(18) + "\","
                                    + "\"transactionfee\":\"0.00\","
                                    + "\"amount\":\"" + amt + "\","
                                    + "\"username\":\"" + username + "\","
                                    + "\"transactioncode\":\"" + token + "\","
                                    + "\"hashvalue\":\"" + hashVal + "\"}";
            
            logger.info("jsonRequest: " + jsonRequest);

            HttpHeaders headers = new HttpHeaders();

            headers.setContentType(MediaType.APPLICATION_JSON);

            headers.add("principalid", passwordToken);

            HttpEntity<String> entity = new HttpEntity<String>(jsonRequest, headers);

            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);

            logger.info("response status code: " + response.getStatusCode().toString());

            if (response.getStatusCode().toString().equals("200")) {
                logger.info("response body: " + response.getBody());

                if (response.getBody() != null) {
                    String retBody = response.getBody();

                    if (retBody.length() > 16) {
                        JSONObject jsonObject = (JSONObject) jsonParser.parse(retBody);

                        responseCode = (String) jsonObject.get("responsecode");

                        logger.info("responseCode: " + responseCode);
                    }
                }
            }

            /*if (isAirtime == true) {
                boolean isInserted = simpleDbService.insertAirtime (username, network, phone, amt, sessionID, responseCode);
                logger.info("isInserted: " + isInserted);
            }*/
        } catch (Exception ee) {
            ee.printStackTrace();
        }

        return responseCode;
    }

    public String processCreditTransfer (String destinationBank, String oAccountNumber, String oAccountName, String oBvn, String oKycLevel, String bAccountNumber, String bAccountName, String bBvn, 
            String bKycLevel, String narration, String amt, String nameEnqRef, String passwordToken, String username, String token) {

        java.text.SimpleDateFormat sformatter = new java.text.SimpleDateFormat ("yyMMddHHmmss");
        String cDateTime = sformatter.format(new java.util.Date());
        
        String responseCode = "91";

        JSONParser jsonParser = new JSONParser();

        try {
            RestTemplate restTemplate = new RestTemplate();

            String uniqueNumber = generateUniqueId();

            String sessionID = srcInstCode + cDateTime + uniqueNumber;

            String hashInput = username + sessionID + destinationBank + nameEnqRef + oAccountNumber + oAccountName + oBvn + oKycLevel + bAccountNumber + bAccountName 
                    + bBvn + bKycLevel + narration + "100002/ref" + amt + hashKey;

            logger.info("hashInput: " + hashInput);

            String hashVal = generateHash512Value(hashInput);

            String jsonRequest = "{\"sessionid\":\"" + sessionID + "\", "
                                    + "\"destinationinstitutioncode\":\"" + destinationBank + "\","
                                    + "\"nameenquiryref\":\"" + nameEnqRef + "\","
                                    + "\"srcaccountnumber\":\"" + oAccountNumber + "\","
                                    + "\"srcaccountname\":\"" + oAccountName + "\","
                                    + "\"srcbvn\":\"" + oBvn + "\","
                                    + "\"srckyclevel\":\"" + oKycLevel + "\","
                                    + "\"beneficiaryaccountnumber\":\"" + bAccountNumber + "\","
                                    + "\"beneficiaryaccountname\":\"" + bAccountName + "\","
                                    + "\"beneficiarybvn\":\"" + bBvn + "\","
                                    + "\"beneficiarykyclevel\":\"" + bKycLevel + "\","
                                    + "\"narration\":\"" + narration + "\","
                                    + "\"paymentreference\":\"100002/ref\","
                                    + "\"amount\":\"" + amt + "\","
                                    + "\"username\":\"" + username + "\","
                                    + "\"transactioncode\":\"" + token + "\","
                                    + "\"hashvalue\":\"" + hashVal + "\"}";

            HttpHeaders headers = new HttpHeaders();

            headers.setContentType(MediaType.APPLICATION_JSON);

            headers.add("principalid", passwordToken);

            HttpEntity<String> entity = new HttpEntity<String>(jsonRequest, headers);

            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);

            logger.info("response status code: " + response.getStatusCode().toString());

            if (response.getStatusCode().toString().equals("200")) {
                logger.info("response body: " + response.getBody());

                if (response.getBody() != null) {
                    String retBody = response.getBody();

                    if (retBody.length() > 16) {
                        JSONObject jsonObject = (JSONObject) jsonParser.parse(retBody);

                        responseCode = (String) jsonObject.get("responsecode");

                        logger.info("responseCode: " + responseCode);
                    }
                }
            }

//            String[] transArr = new String[16];
//
//            transArr[0] = oAccountNumber;
//            transArr[1] = oAccountName;
//            transArr[2] = oKycLevel;
//            transArr[3] = oBvn;
//            transArr[4] = bAccountNumber;
//            transArr[5] = bAccountName;
//            transArr[6] = bKycLevel;
//            transArr[7] = bBvn;
//            transArr[8] = narration;
//            transArr[9] = sessionID.substring(18);
//            transArr[10] = "";
//            transArr[11] = amt;
//            transArr[12] = "1";
//            transArr[13] = username;
//            transArr[14] = sessionID;
//            transArr[15] = responseCode;
//
//            boolean isInserted = simpleDbService.insertForTransferService(transArr);

//            logger.info("isInserted: " + isInserted);
        } catch (Exception ee) {
            ee.printStackTrace();
        }

        return responseCode;
    }

    public String processAirtimeVend (String dAccountNumber, String dAccountName, String dBvn, String dKycLevel, String amt, String passwordToken, String username, String token, 
            String phone, String network, String paymentReference) {

        java.text.SimpleDateFormat sformatter = new java.text.SimpleDateFormat ("yyMMddHHmmss");
        String cDateTime = sformatter.format(new java.util.Date());

        String responseCode = "91";

        JSONParser jsonParser = new JSONParser();

        try {
            RestTemplate restTemplate = new RestTemplate();

            String uniqueNumber = generateUniqueId();

            String sessionID = srcInstCode + cDateTime + uniqueNumber;

//            String hashInput = username + sessionID + sessionID.substring(0, 6) + dAccountNumber + dAccountName + dBvn + dKycLevel + network + phone + paymentReference + amt + hashKey;

            String hashInput = username + sessionID + dAccountNumber + dAccountName + dBvn + dKycLevel + network + phone + paymentReference + amt + hashKey;

            logger.info("hashInput: " + hashInput);

            String hashVal = generateHash512Value(hashInput);

            String jsonRequest = "{\"sessionid\":\"" + sessionID + "\", "
//                                    + "\"destinationinstitutioncode\":\"" + sessionID.substring(0, 6) + "\","
                                    + "\"debitaccountnumber\":\"" + dAccountNumber + "\","
                                    + "\"debitaccountname\":\"" + dAccountName + "\","
                                    + "\"debitbvn\":\"" + dBvn + "\","
                                    + "\"debitkyclevel\":\"" + dKycLevel + "\","
                                    + "\"network\":\"" + network + "\","
                                    + "\"phone\":\"" + phone + "\","
                                    + "\"amount\":\"" + amt + "\","
                                    + "\"paymentreference\":\"" + paymentReference + "\","
                                    + "\"username\":\"" + username + "\","
                                    + "\"transactioncode\":\"" + token + "\","
                                    + "\"hashvalue\":\"" + hashVal + "\"}";

            HttpHeaders headers = new HttpHeaders();

            headers.setContentType(MediaType.APPLICATION_JSON);

            headers.add("principalid", passwordToken);

            HttpEntity<String> entity = new HttpEntity<String>(jsonRequest, headers);

            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);

            logger.info("response status code: " + response.getStatusCode().toString());

            if (response.getStatusCode().toString().equals("200")) {
                logger.info("response body: " + response.getBody());

                if (response.getBody() != null) {
                    String retBody = response.getBody();

                    if (retBody.length() > 16) {
                        JSONObject jsonObject = (JSONObject) jsonParser.parse(retBody);

                        responseCode = (String) jsonObject.get("responsecode");

                        logger.info("responseCode: " + responseCode);
                    }
                }
            }
        } catch (Exception ee) {
            ee.printStackTrace();
        }

        return responseCode;
    }

    public String storeCreditTransfer (String destinationBank, String oAccountNumber, String oAccountName, String oBvn, String oKycLevel, String bAccountNumber, String bAccountName, String bBvn, 
            String bKycLevel, String narration, String amt, String nameEnqRef, String passwordToken, String username, String token) {

        java.text.SimpleDateFormat sformatter = new java.text.SimpleDateFormat ("yyMMddHHmmss");
        String cDateTime = sformatter.format(new java.util.Date());
        
        String responseCode = "91";

//        JSONParser jsonParser = new JSONParser();

        try {
            //RestTemplate restTemplate = new RestTemplate();

            String uniqueNumber = generateUniqueId();

            String sessionID = srcInstCode + cDateTime + uniqueNumber;

//            String hashInput = username + sessionID + destinationBank + nameEnqRef + oAccountNumber + oAccountName + oBvn + oKycLevel + bAccountNumber + bAccountName 
//                    + bBvn + bKycLevel + narration + "100002/ref" + amt + hashKey;
//
//            logger.info("hashInput: " + hashInput);
//
//            String hashVal = generateHash512Value(hashInput);
//
//            String jsonRequest = "{\"sessionid\":\"" + sessionID + "\", "
//                                    + "\"destinationinstitutioncode\":\"" + destinationBank + "\","
//                                    + "\"nameenquiryref\":\"" + nameEnqRef + "\","
//                                    + "\"srcaccountnumber\":\"" + oAccountNumber + "\","
//                                    + "\"srcaccountname\":\"" + oAccountName + "\","
//                                    + "\"srcbvn\":\"" + oBvn + "\","
//                                    + "\"srckyclevel\":\"" + oKycLevel + "\","
//                                    + "\"beneficiaryaccountnumber\":\"" + bAccountNumber + "\","
//                                    + "\"beneficiaryaccountname\":\"" + bAccountName + "\","
//                                    + "\"beneficiarybvn\":\"" + bBvn + "\","
//                                    + "\"beneficiarykyclevel\":\"" + bKycLevel + "\","
//                                    + "\"narration\":\"" + narration + "\","
//                                    + "\"paymentreference\":\"100002/ref\","
//                                    + "\"amount\":\"" + amt + "\","
//                                    + "\"username\":\"" + username + "\","
//                                    + "\"transactioncode\":\"" + token + "\","
//                                    + "\"hashvalue\":\"" + hashVal + "\"}";
//
//            HttpHeaders headers = new HttpHeaders();
//
//            headers.setContentType(MediaType.APPLICATION_JSON);
//
//            headers.add("principalid", passwordToken);
//
//            HttpEntity<String> entity = new HttpEntity<String>(jsonRequest, headers);
//
//            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
//
//            logger.info("response status code: " + response.getStatusCode().toString());
//
//            if (response.getStatusCode().toString().equals("200")) {
//                logger.info("response body: " + response.getBody());
//
//                if (response.getBody() != null) {
//                    String retBody = response.getBody();
//
//                    if (retBody.length() > 16) {
//                        JSONObject jsonObject = (JSONObject) jsonParser.parse(retBody);
//
//                        responseCode = (String) jsonObject.get("responsecode");
//
//                        logger.info("responseCode: " + responseCode);
//                    }
//                }
//            }

            String[] transArr = new String[18];

            transArr[0] = oAccountNumber;
            transArr[1] = oAccountName;
            transArr[2] = oKycLevel;
            transArr[3] = oBvn;
            transArr[4] = bAccountNumber;
            transArr[5] = bAccountName;
            transArr[6] = bKycLevel;
            transArr[7] = bBvn;
            transArr[8] = narration;
            transArr[9] = sessionID.substring(18);
            transArr[10] = "";
            transArr[11] = amt;
            transArr[12] = "1";
            transArr[13] = username;
            transArr[14] = sessionID;
            transArr[15] = "00";
            transArr[16] = srcInstCode;
            transArr[17] = destinationBank;

            boolean isInserted = simpleDbService.insertTransactions (transArr);
            
            if (isInserted == true)
                responseCode = "00";

//            logger.info("isInserted: " + isInserted);
        } catch (Exception ee) {
            ee.printStackTrace();
        }

        return responseCode;
    }

    public String getUserToken (String username) {
        String uniqueToken = "";
        JSONParser jsonParser = new JSONParser();

        try {
            RestTemplate restTemplate = new RestTemplate();

            String jsonRequest = "{\"username\":\"" + username + "\", \"channel\":\"2\"}";

            HttpHeaders headers = new HttpHeaders();

            headers.setContentType(MediaType.APPLICATION_JSON);

            headers.add("principalid", username);

            HttpEntity<String> entity = new HttpEntity<String>(jsonRequest, headers);

            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);

            logger.info("response status code: " + response.getStatusCode().toString());

            if (response.getStatusCode().toString().equals("200")) {
                logger.info("response body: " + response.getBody());

                if (response.getBody() != null) {
                    String retBody = response.getBody();

                    if (retBody.length() > 16) {
                        JSONObject jsonObject = (JSONObject) jsonParser.parse(retBody);

                        String responseCode = (String) jsonObject.get("responsecode");

                        logger.info("responseCode: " + responseCode);

                        if (responseCode.equals("00")) {
                            uniqueToken = (String) jsonObject.get("uniquetoken");

                            logger.info("uniqueToken: " + uniqueToken);
                        }
                    }
                }
            }
        } catch (Exception ee) {
            ee.printStackTrace();
        }

        return uniqueToken;
    }

    public String getPasswordToken (String userToken, String userPassword) {
        String uniqueToken = "";
        JSONParser jsonParser = new JSONParser();

        try {
            RestTemplate restTemplate = new RestTemplate();

            String jsonRequest = "{\"password\":\"" + userPassword + "\", \"channel\":\"2\"}";

            HttpHeaders headers = new HttpHeaders();

            headers.setContentType(MediaType.APPLICATION_JSON);

            headers.add("principalid", userToken);

            HttpEntity<String> entity = new HttpEntity<String>(jsonRequest, headers);

            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);

            logger.info("response status code: " + response.getStatusCode().toString());

            if (response.getStatusCode().toString().equals("200")) {
                logger.info("response body: " + response.getBody());

                if (response.getBody() != null) {
                    String retBody = response.getBody();

                    if (retBody.length() > 16) {
                        JSONObject jsonObject = (JSONObject) jsonParser.parse(retBody);

                        String responseCode = (String) jsonObject.get("responsecode");

                        logger.info("responseCode: " + responseCode);

                        if (responseCode.equals("00")) {
                            uniqueToken = (String) jsonObject.get("uniquetoken");

                            logger.info("uniqueToken: " + uniqueToken);
                        }
                    }
                }
            }
        } catch (Exception ee) {
            ee.printStackTrace();
        }

        return uniqueToken;
    }

    public String generateHash512Value(String msg) {
        MessageDigest m = null;
        String hashText = null;

        try {
            m = MessageDigest.getInstance("SHA-512");

            try {
                m.update(msg.getBytes("UTF-8"), 0, msg.length());
            } catch (UnsupportedEncodingException ex) {
                ex.printStackTrace();
            }
            hashText = new BigInteger(1, m.digest()).toString(16);
        } catch (NoSuchAlgorithmException ex) {
            ex.printStackTrace();
        }

        if (hashText.length() < 64) {
            int numberOfZeroes = 64 - hashText.length();
            String zeroes = "";

            for (int i = 0; i < numberOfZeroes; i++)
                zeroes = zeroes + "0";

            hashText = zeroes + hashText;
        }

        return hashText;
    }

    public String generateUniqueId() {
        return String.valueOf(Math.random()).substring(2, 14);
    }
}
