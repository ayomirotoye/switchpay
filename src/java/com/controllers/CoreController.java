/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.controllers;

/**
 *
 * @author ajibade
 */
import au.com.bytecode.opencsv.CSVReader;
//import com.Service.SwitchpayService;
import com.model.Accounts;
import com.model.Billers;
import com.model.CustomerBankAccounts;
import com.model.Filenames;
import com.model.FinancialInstitution;
import com.model.MiniStatement;
import com.model.Payments;
import com.model.Transactions;
import com.model.User;
import com.model.settlements;
import com.oreilly.servlet.MultipartRequest;
import com.service.BillerService;
import com.service.PaymentService;
import com.service.SimpleDbService;
import com.service.SwitchpayService;
import com.util.Pagination;
import com.util.Utility;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
//import org.apache.poi.hssf.usermodel.HSSFSheet;
//import org.apache.poi.hssf.usermodel.HSSFWorkbook;
//import org.apache.poi.ss.usermodel.Cell;
//import org.apache.poi.ss.usermodel.Row;
//import org.apache.poi.xssf.usermodel.XSSFSheet;
//import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
//import com.oreilly.servlet.MultipartRequest;
import org.springframework.web.servlet.ModelAndView;

@Controller
@Scope("session")

public class CoreController {

    private Logger logger = Logger.getLogger(CoreController.class.getName());

    final String SAVE_DIRECTORY = "uploads";
    String fullSavePath;
    java.util.Date today = Calendar.getInstance().getTime();

    @Autowired
    private SimpleDbService simpleDbService;

    @Autowired
    PaymentService pService;

    @Autowired
    SwitchpayService switchService;
    
    @Autowired
    BillerService billerService;

//    @Autowired
//    SwitchpayService switchService;
    @Value("${urlBalanceEnq}")
    String urlBalanceEnq;
    @Value("${urlAccountNameEnq}")
    String urlAccountNameEnq;
    @Value("${urlUserTokenReq}")
    String urlUserTokenReq;
    @Value("${urlPasswordTokenReq}")
    String urlPasswordTokenReq;
    @Value("${urlDebitTransferReq}")
    String urlDebitTransferReq;
    @Value("${urlCreditTransferReq}")
    String urlCreditTransferReq;
    @Value("${urlAirtimeVendReq}")
    String urlAirtimeVendReq;
    @Value("${sourceInstitutionCode}")
    String sourceInstitutionCode;
    @Value("${hashKey}")
    String hashKey;
//    @Value("${chamsSwitchAcctNumber}") String chamsSwitchAcctNumber;
//    @Value("${chamsSwitchAcctName}") String chamsSwitchAcctName;
//    @Value("${chamsSwitchBVN}") String chamsSwitchBVN;
//    @Value("${chamsSwitchKYCLevel}") String chamsSwitchKYCLevel;

    @RequestMapping(value = {"/"}, method = {RequestMethod.POST, RequestMethod.GET})
    public ModelAndView defaultPage() {

        ModelAndView model = new ModelAndView();
        model.addObject("title", "Naira.com Multi-platform");
        model.setViewName("index");

        return model;
    }

    @RequestMapping(value = {"/welcome"}, method = RequestMethod.GET)
    public ModelAndView defaultPages(HttpServletRequest request, HttpSession session) {
        ModelAndView model = new ModelAndView();

        model.addObject("title", "Naira.com Multi-platform");

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        String name = auth.getName();

        if (name == null) {
            model.setViewName("login");
            return model;
        }
        model.addObject("username", name);

        request.getSession().setAttribute("username", name);

        User user = simpleDbService.getUserProfile(name);

        simpleDbService.updateLastLoginDate(name);

        model.addObject("message", "<b>" + user.getFirstName() + " " + user.getSurname() + "</b>, you're welcome!" + " You are logged in as " + name
                + " and your last login date is " + user.getLastlogindate());

        if (name.equals("abc")) {
            model.addObject("userType", "admin");
        } else if (name.equals("alex")) {
            model.addObject("userType", "user");
        }

        Utility util = new Utility(urlUserTokenReq);

        String userToken = util.getUserToken(name);

//        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
//
//        logger.info("encoded 123: " + passwordEncoder.encode("123"));
//
//        logger.info("encoded Admin12+: " + passwordEncoder.encode("Admin12+"));
        if (!userToken.equals("")) {
            String password = simpleDbService.getPassword(name);

            util = new Utility(urlPasswordTokenReq);

            String passwordToken = util.getPasswordToken(userToken, password);

            request.getSession().setAttribute("passwordToken", passwordToken);

            List<CustomerBankAccounts> userAccounts = simpleDbService.getCustomerBankAccounts(name);

            util = new Utility(urlBalanceEnq, sourceInstitutionCode, hashKey);

            boolean isBal = util.getAccountBalance(userAccounts, passwordToken, name);

            request.getSession().setAttribute("userAccounts", userAccounts);

            List<FinancialInstitution> financialInstitutions = simpleDbService.getAllFinancialInstitutions();

            request.getSession().setAttribute("financialInstitutions", financialInstitutions);
        }

        model.setViewName("home");

        return model;
    }

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public String logoutPage(HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth != null) {
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }

        return "redirect:/";
    }

    @RequestMapping(value = "/403", method = RequestMethod.GET)
    public ModelAndView accesssDenied() {
        ModelAndView model = new ModelAndView();

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (!(auth instanceof AnonymousAuthenticationToken)) {
            UserDetails userDetail = (UserDetails) auth.getPrincipal();

            //logger.info(userDetail);
            model.addObject("username", userDetail.getUsername());

        }

        model.setViewName("403");

        return model;
    }

    @RequestMapping(value = "/errorPage", method = RequestMethod.GET)
    public ModelAndView errorHandler() {
        ModelAndView model = new ModelAndView();

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (!(auth instanceof AnonymousAuthenticationToken)) {
            UserDetails userDetail = (UserDetails) auth.getPrincipal();

            //logger.info(userDetail);
            model.addObject("username", userDetail.getUsername());

        }

        model.setViewName("errorPage");

        return model;
    }

    @RequestMapping(value = "/login", method = {RequestMethod.POST, RequestMethod.GET})
    public ModelAndView login(@RequestParam(value = "error", required = false) String error, @RequestParam(value = "logout", required = false) String logout) {
        ModelAndView model = new ModelAndView();

        if (error != null) {
            model.addObject("error", "Invalid username and password!");
        }

        if (logout != null) {
            model.addObject("msg", "You've been logged out successfully.");
        }

        model.setViewName("index");

        return model;
    }

    @RequestMapping(value = "/innerHome", method = {RequestMethod.POST, RequestMethod.GET})
    public ModelAndView innerHome() {
        ModelAndView model = new ModelAndView();

        model.setViewName("innerHome");

        return model;
    }

    @RequestMapping(value = "/balance", method = {RequestMethod.POST, RequestMethod.GET})
    public ModelAndView balance(HttpServletRequest request) {
        ModelAndView model = new ModelAndView();

        String alertMessage = "";

        try {
            List<CustomerBankAccounts> userAccounts = (List<CustomerBankAccounts>) request.getSession().getAttribute("userAccounts");

            model.addObject("userAccounts", userAccounts);

            request.getSession().setAttribute("userAccounts", userAccounts);

            Utility util = new Utility(urlBalanceEnq, sourceInstitutionCode, hashKey);

            String passwordToken = (String) request.getSession().getAttribute("passwordToken");

            String username = (String) request.getSession().getAttribute("username");

            boolean isBalReturned = util.getAccountBalance(userAccounts, passwordToken, username);

            logger.info("CoreController :: Balance :: isBalReturned: " + isBalReturned);

            if (isBalReturned == false) {
                alertMessage = "New Account Balances could not be fetched";

                model.addObject("alertMessage", alertMessage);
            }
        } catch (Exception ee) {
            ee.printStackTrace();
        }

        model.setViewName("balance");

        return model;
    }

    @RequestMapping(value = "/activities", method = {RequestMethod.POST, RequestMethod.GET})
    public ModelAndView activities(HttpServletRequest request) {
        ModelAndView model = new ModelAndView();

        model.setViewName("activities");

        return model;
    }

    @RequestMapping(value = "/pay/{opts}", method = {RequestMethod.POST, RequestMethod.GET})
    public ModelAndView pay(HttpServletRequest request, @PathVariable("opts") String opts) {
        ModelAndView model = new ModelAndView();

        logger.info("opts: " + opts);

        try {
            if (opts.equals("0")) {
                List<CustomerBankAccounts> userAccounts = (List<CustomerBankAccounts>) request.getSession().getAttribute("userAccounts");

                model.addObject("userAccounts", userAccounts);

                request.getSession().setAttribute("whichPay", "Pay");

                model.setViewName("pay");
            } else if (opts.equals("1")) {
                String alertMessage = "";

                Utility util = new Utility(urlAccountNameEnq, sourceInstitutionCode, hashKey);

                String acctNumber = request.getParameter("acctNumber");
                String amount = request.getParameter("amount");
                String srcAccount = request.getParameter("srcAccount");
                String srcAccountNumber = srcAccount.substring(0, srcAccount.indexOf("#"));
                String srcAccountName = srcAccount.substring(srcAccount.indexOf("#") + 1);

                logger.info("acctNumber: " + acctNumber);
                logger.info("amount: " + amount);
                logger.info("srcAccountNumber: " + srcAccountNumber);
                logger.info("srcAccountName: " + srcAccountName);

                if (srcAccountNumber.equals(acctNumber)) {
                    alertMessage = "Source and Beneficiary Account cannot be similar, please try another Beneficiary Account";

                    model.addObject("alertMessage", alertMessage);

                    model.setViewName("pay");

                    return model;
                }

                String passwordToken = (String) request.getSession().getAttribute("passwordToken");

                String username = (String) request.getSession().getAttribute("username");

                List<String> accountDetails = util.getAccountName(acctNumber, sourceInstitutionCode, passwordToken, username);

                String firstElement = accountDetails.get(0);

                if (firstElement.length() != 2) {
                    if (accountDetails != null) {
                        model.addObject("accountName", accountDetails.get(1));
                        model.addObject("bvn", accountDetails.get(2));
                        model.addObject("kycLevel", accountDetails.get(3));
                    }

                    model.addObject("srcAccountNumber", srcAccountNumber);
                    model.addObject("srcAccountName", srcAccountName);
                    model.addObject("acctNumber", acctNumber);

                    NumberFormat numberFormatter = new DecimalFormat("###,###.00");
                    String formattedAmount = "N" + numberFormatter.format(Double.parseDouble(amount));
                    model.addObject("formattedAmount", formattedAmount);
                    model.addObject("amount", amount);

                    model.setViewName("continuepay");
                } else {
                    if (firstElement.equals("91")) {
                        alertMessage = "Beneficiary Account Name Look up failed: <b>Destination Institution is unavailable</b>";
                    } else if (firstElement.equals("07")) {
                        alertMessage = "Beneficiary Account Name Look up failed: <b>Invalid Account</b>";
                    } else {
                        alertMessage = "Beneficiary Account Name Look up failed: <b> Please try another Beneficiary</b>";
                    }

                    model.addObject("alertMessage", alertMessage);
                    model.setViewName("pay");
                }
            } else if (opts.equals("2")) {
                List<CustomerBankAccounts> userAccounts = (List<CustomerBankAccounts>) request.getSession().getAttribute("userAccounts");

                model.addObject("userAccounts", userAccounts);

                request.getSession().setAttribute("whichPay", "Collect");

                model.setViewName("pay");
            }
        } catch (Exception ee) {
            ee.printStackTrace();
        }

        return model;
    }

    @RequestMapping(value = "/completePayment", method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public String completePayment(HttpServletRequest request) {
        String alertMessage = "99";

        try {
            String srcAccountName = request.getParameter("srcAccountName");
            String srcAccountNumber = request.getParameter("srcAccountNumber");
            String accountName = request.getParameter("accountName");
            String acctNumber = request.getParameter("acctNumber");
            String bvn = request.getParameter("bvn");
            String kycLevel = request.getParameter("kycLevel");
            String amount = request.getParameter("amount");
            String token = request.getParameter("token");

            logger.info("token: " + token);
            logger.info("srcAccountName: " + srcAccountName);

            String passwordToken = (String) request.getSession().getAttribute("passwordToken");
            String username = (String) request.getSession().getAttribute("username");

            List<CustomerBankAccounts> userAccounts = (List<CustomerBankAccounts>) request.getSession().getAttribute("userAccounts");

            for (CustomerBankAccounts custItr : userAccounts) {
                if (custItr.getAccountNumber().equals((String) custItr.getAccountNumber())) {
                    String whichPay = (String) request.getSession().getAttribute("whichPay");

                    logger.info("whichPay: " + whichPay);

                    if (whichPay.equals("Pay")) {
                        Utility util = new Utility(urlDebitTransferReq, sourceInstitutionCode, hashKey);

                        util.setSimpleDbService(simpleDbService);

                        String debitRes = util.processDebitTransfer(sourceInstitutionCode, acctNumber, accountName, bvn, kycLevel, srcAccountNumber, srcAccountName, custItr.getBvn(),
                                custItr.getKycLevel(), "Cash withdrwal in favour of " + accountName, amount, "", passwordToken, username, token);

                        if (debitRes.equals("00")) {
                            util = new Utility(urlCreditTransferReq, sourceInstitutionCode, hashKey);

                            util.setSimpleDbService(simpleDbService);

                            String creditRes = util.processCreditTransfer(sourceInstitutionCode, acctNumber, accountName, bvn, kycLevel, srcAccountNumber, srcAccountName, custItr.getBvn(), custItr.getKycLevel(),
                                    "Cash withdrwal in favour of " + accountName, amount, username, passwordToken, username, token);

                            if (creditRes.equals("00")) {
                                alertMessage = "00";
                            }
                        }

                        break;
                    } else if (whichPay.equals("Collect")) {
                        Utility util = new Utility(urlDebitTransferReq, sourceInstitutionCode, hashKey);

                        util.setSimpleDbService(simpleDbService);

                        String debitRes = util.processDebitTransfer(sourceInstitutionCode, srcAccountNumber, srcAccountName, custItr.getBvn(), custItr.getKycLevel(), acctNumber, accountName, bvn,
                                kycLevel, "Cash withdrwal in favour of " + accountName, amount, "", passwordToken, username, token);

                        if (debitRes.equals("00")) {
                            util = new Utility(urlCreditTransferReq, sourceInstitutionCode, hashKey);

                            util.setSimpleDbService(simpleDbService);

                            String creditRes = util.processCreditTransfer(sourceInstitutionCode, srcAccountNumber, srcAccountName, custItr.getBvn(), custItr.getKycLevel(), acctNumber, accountName, bvn, kycLevel,
                                    "Cash withdrwal in favour of " + accountName, amount, username, passwordToken, username, token);

                            if (creditRes.equals("00")) {
                                alertMessage = "00";
                            }
                        }

                        break;
                    }
                }
            }
        } catch (Exception ee) {
            ee.printStackTrace();
        }

        return alertMessage;
    }

    @RequestMapping(value = "/transfer/{opts}", method = {RequestMethod.POST, RequestMethod.GET})
    public ModelAndView transfer(HttpServletRequest request, @PathVariable("opts") String opts) {
        ModelAndView model = new ModelAndView();

        try {
            if (opts.equals("0")) {
                List<CustomerBankAccounts> userAccounts = (List<CustomerBankAccounts>) request.getSession().getAttribute("userAccounts");

                model.addObject("userAccounts", userAccounts);

                List<FinancialInstitution> financialInstitutions = (List<FinancialInstitution>) request.getSession().getAttribute("financialInstitutions");

                logger.info("transfer :: financialInstitutions.size(): " + financialInstitutions.size());

                model.addObject("financialInstitutions", financialInstitutions);

                model.setViewName("transfer");
            } else if (opts.equals("1")) {
                String alertMessage = "";

                Utility util = new Utility(urlAccountNameEnq, sourceInstitutionCode, hashKey);

                String acctNumber = request.getParameter("acctNumber");
                String amount = request.getParameter("amount");
                String srcAccount = request.getParameter("srcAccount");
                String purpose = request.getParameter("purpose");
                String finInst = request.getParameter("finInst");
                String srcAccountNumber = srcAccount.substring(0, srcAccount.indexOf("#"));
                String srcAccountName = srcAccount.substring(srcAccount.indexOf("#") + 1);
                String finInstCode = finInst.substring(0, finInst.indexOf("#"));
                String finInstName = finInst.substring(finInst.indexOf("#") + 1);

                logger.info("acctNumber: " + acctNumber);
                logger.info("amount: " + amount);
                logger.info("srcAccountNumber: " + srcAccountNumber);
                logger.info("srcAccountName: " + srcAccountName);
                logger.info("purpose: " + purpose);
                logger.info("finInstCode: " + finInstCode);
                logger.info("finInstName: " + finInstName);

                String passwordToken = (String) request.getSession().getAttribute("passwordToken");

                String username = (String) request.getSession().getAttribute("username");

                List<String> accountDetails = util.getAccountName(acctNumber, finInstCode, passwordToken, username);

                String firstElement = accountDetails.get(0);

                if (firstElement.length() != 2) {
                    if (accountDetails != null) {
                        model.addObject("accountName", accountDetails.get(1));
                        model.addObject("bvn", accountDetails.get(2));
                        model.addObject("kycLevel", accountDetails.get(3));
                    }

                    model.addObject("srcAccountNumber", srcAccountNumber);
                    model.addObject("srcAccountName", srcAccountName);
                    model.addObject("acctNumber", acctNumber);
                    model.addObject("purpose", purpose);

                    model.addObject("finInstCode", finInstCode);
                    model.addObject("finInstName", finInstName);

                    NumberFormat numberFormatter = new DecimalFormat("###,###.00");
                    String formattedAmount = "N" + numberFormatter.format(Double.parseDouble(amount));
                    model.addObject("formattedAmount", formattedAmount);
                    model.addObject("amount", amount);

                    model.setViewName("continuetransfer");
                } else {
                    if (firstElement.equals("91")) {
                        alertMessage = "Beneficiary Account Name Look up failed: <b>Destination Institution is unavailable</b>";
                    } else if (firstElement.equals("07")) {
                        alertMessage = "Beneficiary Account Name Look up failed: <b>Invalid Account</b>";
                    } else {
                        alertMessage = "Beneficiary Account Name Look up failed: <b> Please try another Beneficiary</b>";
                    }

                    model.addObject("alertMessage", alertMessage);

                    model.setViewName("transfer");
                }
            }
        } catch (Exception ee) {
            ee.printStackTrace();
        }

        return model;
    }

    @RequestMapping(value = "/completetransfer", method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public String completeTransfer(HttpServletRequest request) {
        String alertMessage = "99";

        try {
            String srcAccountName = request.getParameter("srcAccountName");
            String srcAccountNumber = request.getParameter("srcAccountNumber");
            String accountName = request.getParameter("accountName");
            String acctNumber = request.getParameter("acctNumber");
            String bvn = request.getParameter("bvn");
            String kycLevel = request.getParameter("kycLevel");
            String amount = request.getParameter("amount");
            String narration = request.getParameter("purpose");
            String token = request.getParameter("token");
            String finInstCode = request.getParameter("finInstCode");

            String passwordToken = (String) request.getSession().getAttribute("passwordToken");
            String username = (String) request.getSession().getAttribute("username");

            List<CustomerBankAccounts> userAccounts = (List<CustomerBankAccounts>) request.getSession().getAttribute("userAccounts");

            for (CustomerBankAccounts custItr : userAccounts) {
                if (custItr.getAccountNumber().equals((String) custItr.getAccountNumber())) {
                    Utility util = new Utility(urlDebitTransferReq, sourceInstitutionCode, hashKey);

                    util.setSimpleDbService(simpleDbService);

                    String debitRes = util.processDebitTransfer(sourceInstitutionCode, srcAccountNumber, srcAccountName, custItr.getBvn(), custItr.getKycLevel(), acctNumber,
                            accountName, bvn, kycLevel, "fund transfer; " + narration, amount, "", passwordToken, username, token);

                    if (debitRes.equals("00")) {
                        logger.info("sourceInstitutionCode: " + sourceInstitutionCode);
                        logger.info("finInstCode: " + finInstCode);

                        if (sourceInstitutionCode.equals(finInstCode)) {
                            logger.info("source and desitnation institutions are similar, calling the credit function.");

                            util = new Utility(urlCreditTransferReq, sourceInstitutionCode, hashKey);

                            util.setSimpleDbService(simpleDbService);

                            String creditRes = util.processCreditTransfer(sourceInstitutionCode, srcAccountNumber, srcAccountName, custItr.getBvn(), custItr.getKycLevel(), acctNumber, accountName, bvn,
                                    kycLevel, "fund transfer; " + narration, amount, username, passwordToken, username, token);

                            if (creditRes.equals("00")) {
                                alertMessage = "00";
                            }
                        } else {
                            String creditRes = util.storeCreditTransfer(finInstCode, srcAccountNumber, srcAccountName, custItr.getBvn(), custItr.getKycLevel(), acctNumber, accountName, bvn,
                                    kycLevel, "fund transfer; " + narration, amount, username, passwordToken, username, token);

                            if (creditRes.equals("00")) {
                                alertMessage = "00";
                            }
                        }
                    }

                    break;
                }
            }
        } catch (Exception ee) {
            ee.printStackTrace();
        }

        return alertMessage;
    }

    @RequestMapping(value = "/vend/{opts}", method = {RequestMethod.POST, RequestMethod.GET})
    public ModelAndView vend(HttpServletRequest request, @PathVariable("opts") String opts) {
        ModelAndView model = new ModelAndView();

        try {
            if (opts.equals("0")) {
                List<CustomerBankAccounts> userAccounts = (List<CustomerBankAccounts>) request.getSession().getAttribute("userAccounts");

                model.addObject("userAccounts", userAccounts);

                model.setViewName("vend");
            } else if (opts.equals("1")) {
                String srcAccount = request.getParameter("srcAccount");
                String network = request.getParameter("network");
                String phone = request.getParameter("phone");
                String amount = request.getParameter("amount");
                String srcAccountNumber = srcAccount.substring(0, srcAccount.indexOf("#"));
                String srcAccountName = srcAccount.substring(srcAccount.indexOf("#") + 1);

                logger.info("amount: " + amount);
                logger.info("srcAccountNumber: " + srcAccountNumber);
                logger.info("srcAccountName: " + srcAccountName);

                model.addObject("srcAccountNumber", srcAccountNumber);
                model.addObject("srcAccountName", srcAccountName);
                model.addObject("network", network);
                model.addObject("phone", phone);
                model.addObject("amount", amount);

                model.setViewName("continuevend");
            }
        } catch (Exception ee) {
            ee.printStackTrace();
        }

        return model;
    }

    @RequestMapping(value = "/completevend", method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public String completeVend(HttpServletRequest request) {
        String alertMessage = "99";

        try {
            String srcAccountName = request.getParameter("srcAccountName");
            String srcAccountNumber = request.getParameter("srcAccountNumber");
            String network = request.getParameter("network");
            String phone = request.getParameter("phone");
            String amount = request.getParameter("amount");
            String token = request.getParameter("token");

            String passwordToken = (String) request.getSession().getAttribute("passwordToken");
            String username = (String) request.getSession().getAttribute("username");

            List<CustomerBankAccounts> userAccounts = (List<CustomerBankAccounts>) request.getSession().getAttribute("userAccounts");

            for (CustomerBankAccounts custItr : userAccounts) {
                if (custItr.getAccountNumber().equals((String) custItr.getAccountNumber())) {
                    Utility util = new Utility(urlAirtimeVendReq, sourceInstitutionCode, hashKey);

                    util.setSimpleDbService(simpleDbService);

                    String vendAirtimeRes = util.processAirtimeVend(srcAccountNumber, srcAccountName, custItr.getBvn(), custItr.getKycLevel(), amount, passwordToken, username,
                            token, phone, network, "100003/ref");

                    if (vendAirtimeRes.equals("00")) {
                        alertMessage = "00";
                    }

                    break;
                }
            }
        } catch (Exception ee) {
            ee.printStackTrace();
        }

        return alertMessage;
    }

    @RequestMapping(value = "/users", method = {RequestMethod.POST, RequestMethod.GET})
    public ModelAndView users(HttpServletRequest request, @RequestParam(defaultValue = "1") int page_num) {
        ModelAndView model = new ModelAndView();
        List<User> userAllProfiles;

        userAllProfiles = simpleDbService.getAllUserProfile();
        if (userAllProfiles != null) {
            int total = userAllProfiles.size();

            String link = "/SwitchPayPortal/users/" + "?page_num=";
            System.out.println("page:" + page_num);
            Pagination pg = new Pagination(page_num, total);
            String limit = pg.getLimit();

            List<User> allUsers = simpleDbService.getUsersByLimit(limit);
//            List<CustomerBankAccounts> allAccounts = simpleDbService.getCustomerBankAccounts("*");
//            System.out.println("aaa:" + allAccounts.size());
            pg.setLink(link);
            String pages = pg.getControls();
//        
            System.out.println("pages:" + pages);
            model.addObject("userAllProfiles", allUsers);
//            model.addObject("allUserAccounts", allAccounts);
            model.addObject("pagination", pages);
            model.setViewName("users");
        } else {
            model.addObject("message", " <div class=\"alert alert-info text-center\">\n"
                    + "          <h5 class=\"font-weight-bold\"> No result found... try with another entry</h5>\n"
                    + "      </div>");
            List<User> allUsers = Collections.EMPTY_LIST;
            model.addObject("userAllProfiles", allUsers);
            model.setViewName("users");
        }

        return model;
    }

    @RequestMapping(value = "/accounts", method = {RequestMethod.POST, RequestMethod.GET})
    public ModelAndView acoounts(HttpServletRequest request) {
        ModelAndView model = new ModelAndView();

        try {
            List<Accounts> userAllAccounts = simpleDbService.getAllAccounts();

            model.addObject("userAllAccounts", userAllAccounts);
        } catch (Exception ee) {
            ee.printStackTrace();
        }

        model.setViewName("accounts");
        return model;
    }

    @RequestMapping(value = "/cpassword", method = {RequestMethod.POST, RequestMethod.GET})
    public ModelAndView changePassword(HttpServletRequest request) {
        ModelAndView model = new ModelAndView();

        try {
            String username = (String) request.getSession().getAttribute("username");

            model.addObject("username", username);
        } catch (Exception ee) {
            ee.printStackTrace();
        }

        model.setViewName("changePassword");
        return model;
    }

    @RequestMapping(value = "/cpassword1", method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public String changePassword1(HttpServletRequest request) {
        String alertMessage = "Password NOT successfully changed";

        try {
            String username = (String) request.getSession().getAttribute("username");

            String oPassword = request.getParameter("oPassword");
            String nPassword = request.getParameter("nPassword");
            String cPassword = request.getParameter("cPassword");

            if (cPassword.equals(nPassword)) {
                if (!nPassword.equals(oPassword)) {
                    //String pattern = "(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}";
                    String pattern = "(?=.*[0-9])";
                    //logger.info(passwd.matches(pattern));

                    //if (pattern.matches(nPassword)) {
                    if (nPassword.matches(pattern)) {
                        boolean isChanged = simpleDbService.changeUserPassword(username, nPassword, oPassword);

                        if (isChanged == true) {
                            alertMessage = "Password successfully changed";
                        }
                    } else {
                        alertMessage = "Password must contain at least a capital letter, a small letter, a number, a special symbol and must be at least 8 characters in length: password NOT successfully changed";
                    }
                } else {
                    alertMessage = "Old and new passwords are the same: password NOT successfully changed";
                }
            } else {
                alertMessage = "New and confirm passwords are NOT the same, please re-enter again";
            }

        } catch (Exception ee) {
            ee.printStackTrace();
        }

        return alertMessage;
    }

    @RequestMapping(value = "/ctoken", method = {RequestMethod.POST, RequestMethod.GET})
    public ModelAndView changeToken(HttpServletRequest request) {
        ModelAndView model = new ModelAndView();

        try {
            String username = (String) request.getSession().getAttribute("username");

            model.addObject("username", username);
        } catch (Exception ee) {
            ee.printStackTrace();
        }

        model.setViewName("changeToken");
        return model;
    }

    @RequestMapping(value = "/ctoken1", method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public String changeToken1(HttpServletRequest request) {
        String alertMessage = "Transaction Token NOT successfully changed";

        try {
            String username = (String) request.getSession().getAttribute("username");

            String oToken = request.getParameter("oToken");
            String nToken = request.getParameter("nToken");
            String cToken = request.getParameter("cToken");

            if (cToken.equals(nToken)) {
                if (!nToken.equals(oToken)) {
                    boolean isChanged = simpleDbService.changeUserToken(username, request.getParameter("nToken"), request.getParameter("oToken"));

                    if (isChanged == true) {
                        alertMessage = "Transaction Token successfully changed";
                    }
                } else {
                    alertMessage = "Old and new tokens are the same: transaction Token NOT successfully changed";
                }
            } else {
                alertMessage = "new and confirm tokens are NOT the same, please re-enter again";
            }

        } catch (Exception ee) {
            ee.printStackTrace();
        }

        return alertMessage;
    }

    @RequestMapping(value = "/adduser", method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public String adduser(HttpServletRequest request) {
        String alertMessage = "User NOT successfully added";

        try {
            String username = (String) request.getSession().getAttribute("username");

            boolean isChanged = simpleDbService.addUser(request.getParameter("username"), request.getParameter("firstname"), request.getParameter("surname"), request.getParameter("phonenumber"),
                    request.getParameter("accountnumber"), request.getParameter("accountname"), request.getParameter("kyclevel"), request.getParameter("bvn"),
                    request.getParameter("password"));

            if (isChanged == true) {
                alertMessage = "User successfully added";
            }

        } catch (Exception ee) {
            ee.printStackTrace();
        }

        return alertMessage;
    }

    @RequestMapping(value = "/edituser", method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public String edituser(HttpServletRequest request, @ModelAttribute("editUser") User user) {
        String alertMessage = "Failed";

        try {
            String username = (String) request.getSession().getAttribute("username");

            int isEdited = simpleDbService.editUser(user);

            if (isEdited >= 1) {
                alertMessage = "Successful";
            }

        } catch (Exception ee) {
            ee.printStackTrace();
        }

        return alertMessage;
    }

    @RequestMapping(value = "/ministatement", method = {RequestMethod.POST, RequestMethod.GET})
    public ModelAndView ministatement(HttpServletRequest request) {
        ModelAndView model = new ModelAndView();

        try {
            String username = (String) request.getSession().getAttribute("username");
            List<MiniStatement> stmt = simpleDbService.getMinistatement(username);

            for (MiniStatement stmt1 : stmt) {
                if (stmt1.getCreditOrDebit().equals("0")) {
                    stmt1.setCreditOrDebit("Withdrawal");
                } else if (stmt1.getCreditOrDebit().equals("1")) {
                    stmt1.setCreditOrDebit("Deposit");
                }

                if (stmt1.getTransactionStatus().equals("00")) {
                    stmt1.setTransactionStatus("success");
                } else {
                    stmt1.setTransactionStatus("failed");
                }
            }

            model.addObject("ministatement", stmt);
        } catch (Exception ee) {
            ee.printStackTrace();
        }

        model.setViewName("ministatement");

        return model;
    }

    @RequestMapping(value = "/comingsoon", method = {RequestMethod.POST, RequestMethod.GET})
    public ModelAndView comingSoon(HttpServletRequest request) {
        ModelAndView model = new ModelAndView();

        model.setViewName("comingsoon");

        return model;
    }

    @RequestMapping(value = "/disputemgt", method = {RequestMethod.POST, RequestMethod.GET})
    public ModelAndView processDispute(HttpServletRequest request) {
        ModelAndView model = new ModelAndView();

        model.setViewName("disputeManagement");

        return model;
    }
    @RequestMapping(value = "/billsPayment", method = {RequestMethod.POST, RequestMethod.GET})
    public ModelAndView billsPayment(HttpServletRequest request) {
        ModelAndView model = new ModelAndView();
        List<Billers> getBillers = billerService.Billers();
        System.out.println("billers:" + getBillers.size());
        model.addObject("billers", getBillers);

        model.setViewName("billsPayment");

        return model;
    }
    
    @RequestMapping(value = "/settlement", method = {RequestMethod.POST, RequestMethod.GET})
    public ModelAndView viewSettlementRpt(HttpServletRequest request, @RequestParam(defaultValue = "1") int page_num) {
        ModelAndView model = new ModelAndView();

        List<settlements> getSettlements = switchService.getSettlements();

        if (getSettlements != null) {
            int total = getSettlements.size();

            String link = "/SwitchPayPortal/settlement/" + "?page_num=";
            System.out.println("page:" + page_num);
            Pagination pg = new Pagination(page_num, total);
            String limit = pg.getLimit();
            System.out.println("limit:"+limit);

            List<settlements> allSettlements = switchService.getSettlementsByLimit(limit);
            
            
            pg.setLink(link);
            String pages = pg.getControls();
//        
            System.out.println("pages:" + pages);
            model.addObject("allSettlements", allSettlements);
            model.addObject("pagination", pages);
            model.setViewName("settlements");
        } else {
            model.addObject("message", " <div class=\"alert alert-info text-center\">\n"
                    + "          <h5 class=\"font-weight-bold\"> No result found... try with another entry</h5>\n"
                    + "      </div>");
            List<settlements> allSettlements = Collections.EMPTY_LIST;
            model.addObject("allSettlements", allSettlements);

            model.setViewName("settlements");
        }
//        Transactions transactions = simpleDbService.getTransactions(sourceInstitutionCode, 10);
//
//        model.addObject("transactions", transactions);
//        model.setViewName("dashboard");
        
        
        return model;
    }
    
    @RequestMapping(value = {"/downloadZipped"})
    public void downloadFile3(HttpServletResponse resonse, HttpServletRequest request, @RequestParam String filename) throws IOException {
        
        
//        for(String str: myArray){
            File file = new File(filename);

            resonse.setContentType("application/zip");
            resonse.setHeader("Content-Disposition", "attachment;filename=" + file.getName());
            BufferedInputStream inStrem = new BufferedInputStream(new FileInputStream(file));
            BufferedOutputStream outStream = new BufferedOutputStream(resonse.getOutputStream());

            byte[] buffer = new byte[1024];
            int bytesRead = 0;
            while ((bytesRead = inStrem.read(buffer)) != -1) {
                outStream.write(buffer, 0, bytesRead);
            }
            outStream.flush();
            inStrem.close();
//        }
        
    }


    @ResponseBody
    @RequestMapping(value = {"/downloadZip"})
    public Object downloadReport(HttpSession session, HttpServletResponse response, HttpServletRequest request) throws IOException {
        String getSettlementsArray = request.getParameter("array");

        String myArray[] = getSettlementsArray.split(",");
        for(int i =0; i< myArray.length;i++){
            File file = new File(myArray[i]);
            System.out.println("ssss:" + myArray[i]);
            if (!file.exists()) {
                String errorMessage = "Sorry. The file you are looking for does not exist";
                System.out.println(errorMessage);

            }
            String mimeType = URLConnection.guessContentTypeFromName(file.getName());
            if (mimeType == null) {
                System.out.println("mimetype is not detectable, will take default");
                mimeType = "application/octet-stream";
            }
            System.out.println("mimetype : " + mimeType);
            response.setContentType(mimeType);
//                response.setHeader("Content-Disposition", String.format("inline; filename=\"" + file.getName() + "\""));
            /* "Content-Disposition : attachment" will be directly download, may provide save as popup, based on your browser setting*/
            response.setHeader("Content-Disposition", String.format("attachment; filename=\"%s\"", file.getName()));
            response.setContentLength((int) file.length());
            InputStream inputStream = new BufferedInputStream(new FileInputStream(file));
            FileCopyUtils.copy(inputStream, response.getOutputStream());
        }
        
        
//        if(myArray.length>0){
//            for (String myArray1 : myArray) {
//                File file = new File(myArray1);
//                System.out.println("ssss:"+myArray1);
//                if (!file.exists()) {
//                    String errorMessage = "Sorry. The file you are looking for does not exist";
//                    System.out.println(errorMessage);
//
//                }
//                String mimeType = URLConnection.guessContentTypeFromName(file.getName());
//                if (mimeType == null) {
//                    System.out.println("mimetype is not detectable, will take default");
//                    mimeType = "application/octet-stream";
//                }
//                System.out.println("mimetype : " + mimeType);
//                response.setContentType(mimeType);
////                response.setHeader("Content-Disposition", String.format("inline; filename=\"" + file.getName() + "\""));
//                /* "Content-Disposition : attachment" will be directly download, may provide save as popup, based on your browser setting*/
//                response.setHeader("Content-Disposition", String.format("attachment; filename=\"%s\"", file.getName()));
//                response.setContentLength((int) file.length());
//                InputStream inputStream = new BufferedInputStream(new FileInputStream(file));
//                FileCopyUtils.copy(inputStream, response.getOutputStream());
//            }
//        
//        }
      return null;    
    }
    
    @RequestMapping(value = "/downloadFile", method = RequestMethod.GET)
    public void downloadFile(HttpServletResponse response, HttpServletRequest request) throws IOException {

        File file = null;
        String getSettlementsArray = request.getParameter("array");

        String myArray[] = getSettlementsArray.split(",");
      
        
            file = new File(myArray[0]);
       

        if (!file.exists()) {
            String errorMessage = "Sorry. The file you are looking for does not exist";
            System.out.println(errorMessage);
            OutputStream outputStream = response.getOutputStream();
            outputStream.write(errorMessage.getBytes(Charset.forName("UTF-8")));
            outputStream.close();
            return;
        }

        String mimeType = URLConnection.guessContentTypeFromName(file.getName());
        if (mimeType == null) {
            System.out.println("mimetype is not detectable, will take default");
            mimeType = "application/octet-stream";
        }

        System.out.println("mimetype : " + mimeType);

        response.setContentType(mimeType);

        /* "Content-Disposition : inline" will show viewable types [like images/text/pdf/anything viewable by browser] right on browser 
            while others(zip e.g) will be directly downloaded [may provide save as popup, based on your browser setting.]*/
        response.setHeader("Content-Disposition", String.format("inline; filename=\"" + file.getName() + "\""));

        /* "Content-Disposition : attachment" will be directly download, may provide save as popup, based on your browser setting*/
        //response.setHeader("Content-Disposition", String.format("attachment; filename=\"%s\"", file.getName()));
        response.setContentLength((int) file.length());

        InputStream inputStream = new BufferedInputStream(new FileInputStream(file));

        //Copy bytes from source to destination(outputstream in this example), closes both streams.
        FileCopyUtils.copy(inputStream, response.getOutputStream());
        
        
    }
    
    
    @RequestMapping(value = "/dashboard", method = {RequestMethod.POST, RequestMethod.GET})
    public ModelAndView viewDashboard(HttpServletRequest request, @RequestParam(defaultValue = "1") int page_num) {
        ModelAndView model = new ModelAndView();

        List<Transactions> transactions = simpleDbService.getTransactions(sourceInstitutionCode);

        if (transactions != null) {
            int total = transactions.size();

            String link = "/SwitchPayPortal/dashboard/" + "?page_num=";
            System.out.println("page:" + page_num);
            Pagination pg = new Pagination(page_num, total);
            String limit = pg.getLimit();
            System.out.println("limit:" + limit);

            List<Transactions> allTransactions = simpleDbService.getTransactionsByLimit(sourceInstitutionCode,limit);

            pg.setLink(link);
            String pages = pg.getControls();
//        
            System.out.println("pages:" + pages);
            model.addObject("transactions", allTransactions);
            model.addObject("pagination", pages);
            model.setViewName("dashboard");
        } else {
            model.addObject("message", " <div class=\"alert alert-info text-center\">\n"
                    + "          <h5 class=\"font-weight-bold\"> No result found... try with another entry</h5>\n"
                    + "      </div>");
            List<settlements> allTransactions = Collections.EMPTY_LIST;
            model.addObject("transactions", allTransactions);

           model.setViewName("dashboard");
        }
       
        return model;
    }

    @RequestMapping(value = "/bulkpayment", method = {RequestMethod.POST, RequestMethod.GET})
    public ModelAndView processBulkPayments(HttpSession session, @RequestParam(defaultValue = "1") int page_num) {
        ModelAndView model = new ModelAndView();
        String user = (String) session.getAttribute("username");

        List<Filenames> getFiles = pService.getFilenames(user);
        if (getFiles != null) {
            int total = getFiles.size();

            String link = "/SwitchPayPortal/bulkpayment/" + "?page_num=";
            System.out.println("page:" + page_num);
            Pagination pg = new Pagination(page_num, total);
            String limit = pg.getLimit();

            List<Filenames> allFiles = pService.getFilenamesByLimit(user, limit);

            pg.setLink(link);
            String pages = pg.getControls();
//        
            System.out.println("pages:" + pages);
            model.addObject("pagination", pages);
            System.out.println("username:" + user);

            model.addObject("filenames", allFiles);

            model.setViewName("bulkpay");

        } else {
            model.addObject("message", " <div class=\"alert alert-info text-center\">\n"
                    + "          <h5 class=\"font-weight-bold\"> No result found... try with another search details</h5>\n"
                    + "      </div>");
            List<Filenames> allFiles = Collections.EMPTY_LIST;
            model.addObject("filenames", allFiles);
            model.setViewName("bulkpay");
        }

        return model;
    }

    @ResponseBody
    @RequestMapping(value = {"/deleteFilename"}, method = {RequestMethod.POST})
    public String deleteFilenames(HttpServletRequest req, HttpServletResponse res, HttpSession session) {
        int status = 0;
        String message = "Failed";
        String getFilesArray = req.getParameter("array");

        String myArray[] = getFilesArray.split(",");

        int me[] = pService.deleteFiles(myArray);
        System.out.println("me:" + me.length);

        if (me.length > 0) {
            message = "Successful";
        }

        return message;
    }

    @ResponseBody
    @RequestMapping(value = {"/deleteUsers"}, method = {RequestMethod.POST})
    public String deleteUserss(HttpServletRequest req, HttpServletResponse res, HttpSession session) {
        int status = 0;
        String message = "Failed";
        String getFilesArray = req.getParameter("array");

        String myArray[] = getFilesArray.split(",");

        int me[] = simpleDbService.deleteUsers(myArray);
        System.out.println("me:" + me.length);

        if (me.length > 0) {
            message = "Successful";
        }

        return message;
    }

    @RequestMapping(value = "/searchFiles", method = {RequestMethod.POST, RequestMethod.GET})
    public ModelAndView getSearchFiles(HttpSession session, @RequestParam(defaultValue = "1") int page_num, @RequestParam String table_name, @RequestParam String search_by, @RequestParam String search_string) {
        ModelAndView model = new ModelAndView();
        String user = (String) session.getAttribute("username");

        List<Filenames> getFiles = pService.searchFiles(table_name, search_by, search_string);

        if (getFiles != null) {
            int total = getFiles.size();
            System.out.println("gett:" + total);
            String link = "/SwitchPayPortal/bulkpayment/" + "?page_num=";
            System.out.println("page:" + page_num);
            Pagination pg = new Pagination(page_num, total);
            String limit = pg.getLimit();

            List<Filenames> allFiles = pService.searchFilesByLimit(table_name, search_by, search_string, limit);

            pg.setLink(link);
            String pages = pg.getControls();
//        
            System.out.println("pages:" + pages);
            model.addObject("pagination", pages);
            System.out.println("username:" + user);

            model.addObject("filenames", allFiles);

            model.setViewName("bulkpay");

        } else {
            model.addObject("message", " <div class=\"alert alert-info text-center\">\n"
                    + "          <h5 class=\"font-weight-bold\"> No result found... try with another search details</h5>\n"
                    + "      </div>");
            List<Filenames> allFiles = Collections.EMPTY_LIST;
            model.addObject("filenames", allFiles);
            model.setViewName("bulkpay");
        }

        return model;
    }

    @RequestMapping(value = "/searchUsers", method = {RequestMethod.POST, RequestMethod.GET})
    public ModelAndView getSearchUsers(HttpSession session, @RequestParam(defaultValue = "1") int page_num, @RequestParam String table_name, @RequestParam String search_by, @RequestParam String search_string) {
        ModelAndView model = new ModelAndView();
        String user = (String) session.getAttribute("username");

        List<User> getUsers = simpleDbService.searchAllUserProfile(table_name, search_by, search_string);

        if (getUsers != null) {
            int total = getUsers.size();
            System.out.println("gett:" + total);

            String link = "/SwitchPayPortal/users/" + "?page_num=";
            System.out.println("page:" + page_num);
            Pagination pg = new Pagination(page_num, total);
            String limit = pg.getLimit();

            List<User> allUsers = simpleDbService.searchUsersByLimit(table_name, search_by, search_string, limit);

            pg.setLink(link);
            String pages = pg.getControls();
//        
            System.out.println("pages:" + pages);
            model.addObject("userAllProfiles", allUsers);
            model.addObject("pagination", pages);
            model.setViewName("users");

        } else {
            model.addObject("err_message", " <div class=\"alert alert-info text-center\">\n"
                    + "          <h5 class=\"font-weight-bold\"> No result found... try with another search details</h5>\n"
                    + "      </div>");
            List<User> allUsers = Collections.EMPTY_LIST;
            model.addObject("userAllProfiles", allUsers);
            model.setViewName("users");
        }

        return model;
    }
    @RequestMapping(value = "/searchSettlements", method = {RequestMethod.POST, RequestMethod.GET})
    public ModelAndView getSearchSettlements(HttpSession session, @RequestParam(defaultValue = "1") int page_num, @RequestParam  String  table_name, @RequestParam (defaultValue= "") String search_by, @RequestParam (defaultValue= "") String search_string) {
        ModelAndView model = new ModelAndView();
        String user = (String) session.getAttribute("username");

        List<settlements> getSettlements = switchService.searchSettlements(table_name, search_by, search_string);

        if (getSettlements != null) {
            int total = getSettlements.size();
            System.out.println("gett:" + total);
            String link = "/SwitchPayPortal/settlement/" + "?page_num=";
            System.out.println("page:" + page_num);
            Pagination pg = new Pagination(page_num, total);
            String limit = pg.getLimit();

            
            List<settlements> allSettlements = switchService.searchSettlementsByLimit(table_name, search_by, search_string, limit);

            pg.setLink(link);
            String pages = pg.getControls();
//        
            System.out.println("pages:" + pages);
            model.addObject("allSettlements", allSettlements);
            model.addObject("pagination", pages);
            model.setViewName("settlements");
            

        } else {
            model.addObject("message", " <div class=\"alert alert-info text-center\">\n"
                    + "          <h5 class=\"font-weight-bold\"> No result found... try with another search details</h5>\n"
                    + "      </div>");
            List<settlements> allSettlements = Collections.EMPTY_LIST;
            model.addObject("allSettlements", allSettlements);
            model.setViewName("settlement");
        }

        return model;
    }

//    @RequestMapping(value = {"/viewPayments"}, method = {RequestMethod.GET})
//    public ModelAndView viewPayments(HttpSession session, @RequestParam ("fileid") int fileid) {
//        //check if user is logged in 
//        Object user =  session.getAttribute("username");
//        ModelAndView model = new ModelAndView();
//        if(user == null){
//            model.setViewName("/");
//            return model;
//        }//check if user is logged in 
//        
//        List<Payments> getPayments = pService.getPayments(fileid);
//        
//        model.addObject("payments", getPayments);
//        model.setViewName("viewPayments");
//        return model;
//    }
    @RequestMapping(value = {"/viewPayments/{fileid}"})
    public ModelAndView viewPaymentss(HttpSession session, @PathVariable int fileid, @RequestParam(defaultValue = "1") int page_num) {
        //check if user is logged in 
        Object user = session.getAttribute("username");
        ModelAndView model = new ModelAndView();
        if (user == null) {
            model.setViewName("/");
            return model;
        }//check if user is logged in 

        List<Payments> getPayments = pService.allPayments(fileid);
        int total = getPayments.size();

        String link = "/SwitchPayPortal/viewPayments/" + fileid + "?page_num=";
        System.out.println("page" + page_num);
        Pagination pg = new Pagination(page_num, total);
        String limit = pg.getLimit();

        List<Payments> allResults = pService.getPaymentsByLimit(fileid, limit);

        pg.setLink(link);
        String pages = pg.getControls();
        model.addObject("payments", allResults);
        System.out.println("pages:" + pages);
        model.addObject("pagination", pages);
        model.setViewName("viewPayments");
        return model;
    }
//

    @RequestMapping(value = "/uploadFiles", method = RequestMethod.POST)
    public ModelAndView uploadImage(ModelMap model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {

        String username = (String) request.getSession().getAttribute("username");

        StringBuilder sb = new StringBuilder();

        try {
            response.setContentType("text/html");
            PrintWriter out = response.getWriter();

            String appPath = request.getServletContext().getRealPath("");
            appPath = appPath.replace('\\', '/');

            System.out.println("got here <<<<<<<<<<<<<<<<<<<<<<<<< >>>>>>>>>>>>>>>> uploadFiles... username: " + username);

            if (appPath.endsWith("/")) {
                fullSavePath = appPath + SAVE_DIRECTORY;
            } else {
                fullSavePath = appPath + "/" + SAVE_DIRECTORY;
            }
            System.out.println("aa:" + fullSavePath);

            File fileSaveDir = new File(fullSavePath);
            if (!fileSaveDir.exists()) {
                fileSaveDir.mkdir();
            }

            MultipartRequest m = new MultipartRequest(request, fullSavePath,
                    4000000);

            Enumeration uploadedFile = (Enumeration) m.getFileNames();
            String name_filee = "";
            while (uploadedFile.hasMoreElements()) {
                String name_file = (String) uploadedFile.nextElement();
                System.out.println("a:" + name_file);
                name_filee = m.getFilesystemName(name_file);
                System.out.println("b:" + name_filee);
                //sb.append(fileSaveDir);
                sb.append(SAVE_DIRECTORY).append("/").append(name_filee).append("EOF");
            }

            String imgFilePath = sb.toString();
            System.getProperty("java.class.path");
            session.setAttribute("filepath", imgFilePath);
            System.out.println("aa:" + imgFilePath);
            sb.setLength(0);

            fullSavePath = fullSavePath.replace("//", "\\\\");
            fullSavePath = fullSavePath.concat("/").concat(name_filee);
            System.out.println("pp:" + fullSavePath);

            System.out.println("got here <<<<<<<<<<<<<<<<<<<<<<<<< >>>>>>>>>>>>>>>> uploadFiles... 2");

            String sql = "INSERT INTO tbl_filenames (file_name, uploaded_by, date_uploaded) VALUES (?,?,?)";
            int file_id = pService.insertFilename(sql, fullSavePath, username, today);

            String sq[] = readXLSFile(fullSavePath, username, file_id);
            for (int i = 0; i < sq.length; i++) {
                System.out.println("record[" + i + "]:" + sq[i]);
            }

            int uploadStatus[] = pService.paymentUploads(sq);

            if (uploadStatus.length > 1) {
                System.out.println("Uploaded:" + uploadStatus.length + "records");
            }

            System.out.println("got here <<<<<<<<<<<<<<<<<<<<<<<<< >>>>>>>>>>>>>>>> uploadFiles... 3");

        } catch (IOException ex) {
            System.out.println("CoreController :: uploadImage :: Error occurred...");
            ex.printStackTrace();
        }
        return null;
    }

    public String[] readXLSFile(String path, String uploaded_by, int fileid) throws
            IOException {
        StringBuilder sb = new StringBuilder();
        ArrayList<String> batchList = new ArrayList();

        String inputFilename = new File(path).getName();
        switch (inputFilename.substring(inputFilename.lastIndexOf(".") + 1,
                inputFilename.length())) {
            case "csv":
                CSVReader reader = null;
                reader = new CSVReader(new FileReader(path));
                String[] line;
                while ((line = reader.readNext()) != null) {
//                    sb.append("INSERT INTO tblsalaries (BANK_CODE,ACCOUNT_NUMBER, "
//                        + "ACCOUNT_NAME, NARRATION,AMOUNT,TOKEN, FILE_ID, FILE_NAME) VALUES(");
                    sb.append("INSERT INTO tbl_salaries (BANK_CODE, ACCOUNT_NUMBERS, "
                            + "ACCOUNT_NAME, BVN, KYC, NARRATIONS, AMOUNT, FILE_ID, FILE_NAME) VALUES(");

                    sb.append("'").append(line[0]).append("'").append(",");
                    sb.append("'").append(line[1]).append("'").append(",");
                    sb.append("'").append(line[2]).append("'").append(",");
                    sb.append("'").append(line[3]).append("'").append(",");
                    sb.append("'").append(line[4]).append("'").append(",");
                    sb.append("'").append(line[5]).append("'").append(",");
                    sb.append("'").append(line[6]).append("'").append(",");
//                    sb.append("'").append(line[7]).append("'").append(",");
                    sb.append("'").append(fileid).append("'").append(",");
                    sb.append("'").append(fullSavePath).append("'");
//                    sb.append("'").append(uploaded_by).append("'");
                    sb.append(")");
                    batchList.add(sb.toString());
                    sb.setLength(0);
                    System.out.println("");
//                    System.out.println("Country [id= " + line[0] + ", code= " + line[1] + " , name=" + line[2] + "]");
                }
            default:
                System.out.println("Incorrect file");
        }

        String[] sql = null;
        if (!batchList.isEmpty()) {
            sql = new String[batchList.size()];
            for (int i = 0; i < sql.length; i++) {
                sql[i] = batchList.get(i);
            }

        }
        return sql;
    }

    @ResponseBody
    @RequestMapping(value = {"/updatePayment"}, method = {RequestMethod.POST})
    public String updatePayment(HttpSession session, @ModelAttribute("payment_edit") Payments payment) {
        Object user = session.getAttribute("username");
        if (user == null) {
            return "/";
        }
        String message = "Failed";

        int result = pService.updatePayment(payment);
        if (result == 1) {
            message = "Successful";
            return message;
        }

        return message;
    }

    @ResponseBody
    @RequestMapping(value = {"/activatePayment"}, method = {RequestMethod.POST})
    public String activatePayment(HttpSession session, @ModelAttribute("payment_activate") Payments payment) {
        String message = "Failed";

        int result = pService.activatePayment(payment);
        if (result == 1) {
            message = "Successful";
            return message;
        }

        return message;
    }

    @ResponseBody
    @RequestMapping(value = {"/deletePayment"}, method = {RequestMethod.POST})
    public String deletePayment(HttpSession session, @ModelAttribute("payment_delete") Payments payment) {
        String message = "Failed";

        int result = pService.deletePayment(payment);
        if (result == 1) {
            message = "Successful";
            return message;
        }

        return message;
    }

    @ResponseBody
    @RequestMapping(value = {"/suspendPayment"}, method = {RequestMethod.POST})
    public String suspendPayment(HttpSession session, @ModelAttribute("suspendPayment") Payments payment) {
        String message = "Failed";

        int result = pService.suspendPayment(payment);
        if (result == 1) {
            message = "Successful";
            return message;
        }
        return message;
    }

//    @ResponseBody
//    @RequestMapping(value = "/downloadZip", method = {RequestMethod.POST, RequestMethod.GET})
//    public Object getFileV1(HttpServletRequest request, HttpServletResponse response) {
//        try {
//            response.setContentType("application/zip");
//            response.setHeader("Content-Disposition", "attachment; filename=Settlements" + new Date().toString() + ".zip");
////            String stringValue1 = "This is a test value for csv1";
////            String stringValue2 = "This is a test value for csv2";
//
////            PrintWriter writer1 = new PrintWriter(new OutputStreamWriter(new FileOutputStream("stringValue1.csv"), "UTF-8"));
////            writer1.print(stringValue1);
////            writer1.close();
////
////            PrintWriter writer2 = new PrintWriter(new OutputStreamWriter(new FileOutputStream("stringValue2.csv"), "UTF-8"));
////            writer2.print(stringValue2);
////            writer2.close();
//            File f = new File("C:\\apache-tomcat-8.5.24\\bin\\reports");
//
//// File file1 = new File("stringValue1.csv");
//// File file2 = new File("stringValue2.csv");
//            filesToZip(response, f);
//
//// file1.delete();
//// file2.delete();
//            response.flushBuffer();
//            return null;
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            return e;
//        }
//    }

//    public static void filesToZip(HttpServletResponse response, File file) throws IOException {
//        final int BUFFER = 2048;
//        // Create a buffer for reading the files
//        byte[] buf = new byte[1024];
//        // create the ZIP file
//// File files[] = file.listFiles();
//        // compress the files
//        try {
//            BufferedInputStream origin = null;
//// FileOutputStream dest = new FileOutputStream(filename);
//            ZipOutputStream out = new ZipOutputStream(response.getOutputStream());
//            //out.setMethod(ZipOutputStream.DEFLATED);
//            byte data[] = new byte[BUFFER];
//            // get a list of files from current directory
//            File f = new File("C:\\apache-tomcat-8.5.24\\bin\\reports");
//// String files[] = {"C:\\apache-tomcat-8.5.24\\bin\\reports\\settlement01_26_2018_06_34_27.pdf", "C:\\apache-tomcat-8.5.24\\bin\\reports\\settlement01_26_2018_06_34_29.csv"};
//            File files[] = f.listFiles();
//            for (int i = 0; i < files.length; i++) {
//                System.out.println("Adding: " + files[i].toString());
//                FileInputStream fi = new FileInputStream((files[i]));
//                origin = new BufferedInputStream(fi, BUFFER);
//                ZipEntry entry = new ZipEntry(files[i].toString());
//                System.out.println("name:" + entry.getName());
//                out.putNextEntry(entry);
//                int count;
//                while ((count = origin.read(data, 0, BUFFER)) != -1) {
//                    out.write(data, 0, count);
//                    System.out.println("count:" + count);
//                }
//                origin.close();
//            }
//            out.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
// try (ZipOutputStream out = new ZipOutputStream(response.getOutputStream())) {
// // compress the files
// for (int i = 0; i < files.length; i++) {
// FileInputStream in = new FileInputStream(files[i].getName());
// // add ZIP entry to output stream
// out.putNextEntry(new ZipEntry(files[i].getName()));
// // transfer bytes from the file to the ZIP file
// int len;
// while ((len = in.read(buf)) > 0) {
// out.write(buf, 0, len);
// }
// // complete the entry
// out.closeEntry();
// in.close();
// }
// // complete the ZIP file
// }
    

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

            for (int i = 0; i < numberOfZeroes; i++) {
                zeroes = zeroes + "0";
            }

            hashText = zeroes + hashText;
        }

        return hashText;

    }
}
