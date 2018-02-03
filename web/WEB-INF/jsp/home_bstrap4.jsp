<%-- 
    Document   : index
    Created on : Nov 25, 2017, 4:19:41 PM
    Author     : ajibade
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page errorPage="errorPage.jsp" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link href="<spring:url value="/resources/css/bootstrap.css?v=1.00"/>" rel="stylesheet" />
        <link href="<spring:url value="/resources/css/custom.css"/>" rel="stylesheet" />
        
        <link rel="stylesheet" href="<spring:url value="/resources/css/font-awesome.min.css"/>"/>
        <script src="<spring:url value="/resources/js/jquery.js"/>"></script>
        <script src="<spring:url value="/resources/js/popper.min.js"/>"></script>
        <script src="<spring:url value="/resources/js/bootstrap.js"/>"></script>
        <link rel="icon" href="<spring:url value="/resources/imgs/logo.png"/>" type="image/png"/>
        <link href="https://fonts.googleapis.com/css?family=Roboto|Montserrat:900" rel="stylesheet"/>

        <!-- <link href = "//maxcdn.bootstrapcdn.com/bootstrap/3.3.1/css/bootstrap.min.css" rel = "stylesheet">
        <link href="<spring:url value="/resources/css/custom.css"/>" rel="stylesheet" />
        <link rel="stylesheet" href="<spring:url value="/resources/css/font-awesome.min.css"/>"/>
        <script src="<spring:url value="/resources/js/popper.min.js"/>"></script>

        <link rel="icon" href="<spring:url value="/resources/imgs/logo.png"/>" type="image/png"/>

        <link href="https://fonts.googleapis.com/css?family=Roboto|Montserrat:900" rel="stylesheet"/>

        <!--<script src = "https://ajax.googleapis.com/ajax/libs/jquery/1.11.1/jquery.min.js"></script>->-->

       <!--  <script src="https://code.jquery.com/jquery-3.2.1.min.js" integrity="sha256-hwg4gsxgFZhOsEEamdOYGBf13FyQuiTwlAQgxVSNgt4=" crossorigin="anonymous"></script>

        <script src = "//maxcdn.bootstrapcdn.com/bootstrap/3.3.1/js/bootstrap.min.js"></script> --> -->

        <title>${title}</title>

        <script>
                var spinner = `<i class="fa fa-spinner fa-spin" aria-hidden="true" style="font-size:100px;"></i>`;
            $(document).ready(function() {
                $('.link').click(function(e){
                    e.preventDefault();
                    url = $(this).attr('href');
                    $('#result1').html(spinner); //just added this line
                    $.get(url, function(result){
                        $('#result1').html(result);
                    });
                });

                $(document).on('click', '.changePassword', function(e){
                    e.preventDefault();
                    var form = $(this).closest('form');
                    var data = form.serialize();
                    var url = form.attr('action');
                    $.get(url, data, function(result){
                        alert(result);
                        form[0].reset();
                    });
                });

                $(document).on('click', '.changeToken', function(e){
                    e.preventDefault();
                    var form = $(this).closest('form');
                    var data = form.serialize();
                    var url = form.attr('action');
                    $.get(url, data, function(result){
                        alert(result);
                        form[0].reset();
                    });
                });

                $(document).on('click', '.adduser', function(e){
                    e.preventDefault();
                    var form = $(this).closest('form');
                    var data = form.serialize();
                    var url = form.attr('action');
                    $.get(url, data, function(result){
                        alert(result);
                        form[0].reset();
                    });
                });

                $(document).on('click', '.closeModal', function(e){
                    e.preventDefault();
                    var url = '${pageContext.request.contextPath}/users';
                    $('#result1').html(spinner); //just added this line
                    $.get(url, function(result){
                        $('#result1').html(result);
                    });
                });

                $(document).on('click', '#continuepay', function(e){
                    e.preventDefault();
                    var form = $(this).closest('form');
                    form.find('.errs').remove();
                    var data = form.serialize();
                    var url = form.attr('action');

                      var objSrc = form[0].srcAccount;
                      var objAcc = form[0].acctNumber;
                      var objAmt = form[0].amount;

                      var srcAcc = objSrc.value;
                      var acnum = objAcc.value.toString();
                      var amt = objAmt.value;

                      console.log(srcAcc);
                      if(srcAcc === ''){
                       $(objSrc).after('<span class="errs text-danger"><small>Please select an account source</small></span>');
                       $(objSrc).focus();
                       return;
                      }else if(acnum.length < 10){
                       $(objAcc).after('<span class="errs text-danger"><small>Account number can NOT be less than 10 characters</small></span>');
                       $(objAcc).focus();
                       return;
                      }else if(acnum.length > 10){
                       $(objAcc).after('<span class="errs text-danger"><small>Account number can NOT be greater than 10 characters</small></span>');
                       $(objAcc).focus();
                       return;
                      }else if(acnum.match(/\D/)){
                       $(objAcc).after('<span class="errs text-danger"><small>Can only contain Digits</small></span>');
                       $(objAcc).focus();
                       return;
                      }else if(amt.replace(' ', '') === ""){
                       $(objAmt).after('<span class="errs text-danger"><small>Amount cannot be empty</small></span>');
                       $(objAmt).focus();
                       return;
                      }else if(amt.match(/[^0-9.]/)){
                       $(objAmt).after('<span class="errs text-danger"><small>Can only contain Digits</small></span>');
                       $(objAmt).focus();
                       return;
                      }else if(amt.match(/\.(\d){3,}/)){
                       $(objAmt).after('<span class="errs text-danger"><small>You can only have 2 decimal places</small></span>');
                       $(objAmt).focus();
                       return;
                      }
                      $.get(url, data, function(result){
                          form[0].reset();
                          $('#result1').html(result);
                      });
                  });
                  
                   $(document).on('click', '#continuetransfer', function(e){
                    e.preventDefault();
                    var form = $(this).closest('form');
                    form.find('.errs').remove();
                    var data = form.serialize();
                    var url = form.attr('action');

                      var objSrc = form[0].srcAccount;
                      var objAcc = form[0].acctNumber;
                      var objAmt = form[0].amount;

                      var srcAcc = objSrc.value;
                      var acnum = objAcc.value.toString();
                      var amt = objAmt.value;

                      console.log(srcAcc);
                      if(srcAcc === ''){
                       $(objSrc).after('<span class="errs text-danger"><small>Please select an account source</small></span>');
                       $(objSrc).focus();
                       return;
                      }else if(acnum.length < 10){
                       $(objAcc).after('<span class="errs text-danger"><small>Account number can NOT be less than 10 characters</small></span>');
                       $(objAcc).focus();
                       return;
                      }else if(acnum.length > 10){
                       $(objAcc).after('<span class="errs text-danger"><small>Account number can NOT be greater than 10 characters</small></span>');
                       $(objAcc).focus();
                       return;
                      }else if(acnum.match(/\D/)){
                       $(objAcc).after('<span class="errs text-danger"><small>Can only contain Digits</small></span>');
                       $(objAcc).focus();
                       return;
                      }else if(acnum == srcAcc.split('#')[0]){ //<---- 08/12/17 just add this 
                       $(objAcc).after('<span class="errs text-danger"><small>You can not pay to the same account</small></span>');
                       $(objAcc).focus();
                       return;//<---- 08/12/17 ends here 
                      }else if(amt.replace(' ', '') === ""){
                       $(objAmt).after('<span class="errs text-danger"><small>Amount cannot be empty</small></span>');
                       $(objAmt).focus();
                       return;
                      }else if(amt.match(/[^0-9.]/)){
                       $(objAmt).after('<span class="errs text-danger"><small>Can only contain Digits</small></span>');
                       $(objAmt).focus();
                       return;
                      }else if(amt.match(/\.(\d){3,}/)){
                       $(objAmt).after('<span class="errs text-danger"><small>You can only have 2 decimal places</small></span>');
                       $(objAmt).focus();
                       return;
                      }
                      $.get(url, data, function(result){
                          form[0].reset();
                          $('#result1').html(result);
                      });
                  });
                  
                  $(document).on('click', '#continuevend', function(e){
                    e.preventDefault();
                    var form = $(this).closest('form');
                    form.find('.errs').remove();
                    var data = form.serialize();
                    var url = form.attr('action');
                      $.get(url, data, function(result){
                          form[0].reset();
                          $('#result1').html(result);
                      });
                  });

                $(document).on('click', '.completepay', function(e){
                    e.preventDefault();
                    $(this).hide('fast');
                    var form = $(this).closest('form');
                    var data = form.serialize();
                    var url = '${pageContext.request.contextPath}/completePayment';
                    $.get(url, data, function(result){
                        // form.closest('row').before('<div class="alert alert-success">'+ result +'</div>');
                        if(result=='00') {
                            $('#pay-alert').html('Payment successfully processed').addClass('alert alert-success');
                            //$('.completepay').hide();
//                            $('.cancelpay').val('Make Another ?');
//                            $('.cancelpay').addClass('btn-success')

                            alert('Payment successfully processed');
                        }

                        else if(result=='98') {
                            $('#pay-alert').html('Wrong token entered, payment NOT successfully processed').addClass('alert alert-danger');

                            alert('Wrong token entered, payment NOT successfully processed');
                        }
                        else {
                            $('#pay-alert').html('Payment NOT successfully processed').addClass('alert alert-danger');

                            alert('Payment NOT successfully processed');
                        }
                    });
                });

                $(document).on('click', '.cancelpay', function(e){
                    $($(this)[0].dataset.bindlink).trigger('click');
                })

                $(document).on('click', '.completetransfer', function(e){
                    e.preventDefault();
                    $(this).hide('fast');
                    var form = $(this).closest('form');
                    var data = form.serialize();
                    var url = '${pageContext.request.contextPath}/completetransfer';
                    $.get(url, data, function(result){
                        if(result=='00') {
                            $('#transfer-alert').html('Fund Transfer successfully processed').addClass('alert alert-success');
//                            $('.completetransfer').hide();
//                            $('.canceltransfer').val('Make Another ?');
//                            $('.canceltransfer').addClass('btn-success')

                            alert('Fund Transfer successfully processed');
                        }

                        else if(result=='98') {
                            $('#transfer-alert').html('Wrong token entered, fund transfer NOT successfully processed').addClass('alert alert-danger');

                            alert('Wrong token entered, fund transfer NOT successfully processed');
                        }
                        else {
                            $('#transfer-alert').html('Fund transfer NOT successfully processed').addClass('alert alert-danger');

                            alert('Fund transfer NOT successfully processed');
                        }
                    });
                });

                $(document).on('click', '.canceltransfer', function(e){
                    $($(this)[0].dataset.bindlink).trigger('click');
                })

                $(document).on('click', '.completevend', function(e){
                    e.preventDefault();
                    $(this).hide('fast');
                    var form = $(this).closest('form');
                    var data = form.serialize();
                    var url = '${pageContext.request.contextPath}/completevend';
                    $.get(url, data, function(result){
                        if(result=='00') {
                            $('#vend-alert').html('Airtime purchase successfully processed').addClass('alert alert-success');
//                            $('.completevend').hide();
//                            $('.cancelvend').val('Make Another ?');
//                            $('.cancelvend').addClass('btn-success')

                            alert('Airtime purchase successfully processed');
                        }

                        else if(result=='98') {
                            $('#transfer-alert').html('Wrong token entered, airtime purchase NOT successfully processed').addClass('alert alert-danger');

                            alert('Wrong token entered, airtime purchase NOT successfully processed');
                        }
                        else {
                            $('#transfer-alert').html('Airtime purchase NOT successfully processed').addClass('alert alert-danger');

                            alert('Airtime purchase NOT successfully processed');
                        }
                    });
                });

                $(document).on('click', '.cancelvend', function(e){
                    $($(this)[0].dataset.bindlink).trigger('click');
                })

            });

            $(document).on('change', '.masterCheck', function(){
                if($(this).prop('checked')  == true){
                 $('.userProfiles').find('input:not(.masterCheck)').prop('checked', true);
                }else{
                 $('.userProfiles').find('input:not(.masterCheck)').prop('checked', false);
                }
               });

               $(document).on('click', '.deleteUsers', function(){
                var allIds = [];
                $('.userProfiles').find('input:checked').each(function(index, el){
                 allIds.push($(el).next().val());
                });
                console.log(allIds.JSONStringify());
                $.post(url, {'ids':allIds.JSONStringify()}, function(res){
                 alert(res);
                 // remove elements
                });
               });
        </script>
    </head>
    <body>
        <div class="container">
            <div class="row">
                <div class="col-lg-12">
                    <img src="&lt;c:url value=" resources="" imgs="" nairadotcombanner.jpg
                    "=" ">&quot; alt=&quot;Naira.com Logo&quot; style =&quot;width: 100%; margin-bottom: 10px&quot; /&gt;

                    </div>

                </div>



                <div class="row ">

                    <div class="col-lg-12 ">

                        <div class="card ">

                            <div class="card-body ">${message}</div>

                        </div>

                    </div>

                </div>



                <div class="row ">

                    <div class="col-lg-3 ">

                        <ul class="list-group ">

                            <li class="list-group-item "><a class="link
                    " href="${pageContext.request.contextPath}/innerHome " id="home
                    " data-toggle="tooltip " title="view home page ">home</a></li>

                            <li class="list-group-item "><a class="link
                    " href="${pageContext.request.contextPath}/balance " id="balance
                    " data-toggle="tooltip " title="view account balance(s) ">balance</a></li>

                            <li class="list-group-item "><a class="link
                    " href="${pageContext.request.contextPath}/pay/0 " id="pay " data-toggle="tooltip
                    " title="debit customer&apos;s account and give cash out ">pay</a></li>

                            <li class="list-group-item "><a class="link
                    " href="${pageContext.request.contextPath}/pay/2 " data-toggle="tooltip
                    " title="debit teller&apos;s account and collect cash from customer ">collect</a></li>        

                            <li class="list-group-item "><a class="link
                    " href="${pageContext.request.contextPath}/transfer/0 " data-toggle="tooltip
                    " title="transfer funds to beneficiary ">transfer</a></li>

                            <li class="list-group-item "><a class="link
                    " href="${pageContext.request.contextPath}/comingsoon " data-toggle="tooltip
                    " title="transfer funds to beneficiary ">bulk payments</a></li>

                            <li class="list-group-item "><a class="link
                    " href="${pageContext.request.contextPath}/vend/0 " data-toggle="tooltip
                    " title="vend airtime from telecoms provider ">airtime</a></li>

                            <li class="list-group-item "><a class="link
                    " href="${pageContext.request.contextPath}/comingsoon " data-toggle="tooltip
                    " title="accept bill payments from customers on behalf of billers ">bill</a></li>

                            <li class="list-group-item "><a class="link
                    " href="${pageContext.request.contextPath}/settlement " data-toggle="tooltip
                    " title="enable download of settlement reports ">settlements</a></li>

                            <li class="list-group-item "><a class="link
                    " href="${pageContext.request.contextPath}/disputemgt " data-toggle="tooltip
                    " title="allows for management of disputes ">dispute management</a></li>

                            <li class="list-group-item "><a class="link
                    " href="${pageContext.request.contextPath}/users " data-toggle="tooltip
                    " title="manage users by either creating, deleting, modifying or viewing ">users</a></li>

                            <li class="list-group-item "><a class="link
                    " href="${pageContext.request.contextPath}/comingsoon " data-toggle="tooltip
                    " title="manage tellers&apos; till accounts ">accounts</a></li>

                            <li class="list-group-item "><a class="link
                    " href="${pageContext.request.contextPath}/comingsoon " data-toggle="tooltip
                    " title="view transactions happening on SwitchPay ">switchpay transactions</a></li>

                            <li class="list-group-item "><a class="link
                    " href="${pageContext.request.contextPath}/ministatement " data-toggle="tooltip
                    " title="display last 10 transactions ">ministatement</a></li>

                            <li class="list-group-item "><a class="link
                    " href="${pageContext.request.contextPath}/cpassword " data-toggle="tooltip
                    " title="you can change your password ">change password</a></li>

                            <li class="list-group-item "><a class="link
                    " href="${pageContext.request.contextPath}/ctoken " data-toggle="tooltip
                    " title="you can change your password ">change token</a></li>

                            <li class="list-group-item "><a href="logout
                    " data-toggle="tooltip " title="done with using the channel, you can log out ">logout</a></li>

                        </ul>

                    </div>



                    <div class="col-lg-9 " id="result1 ">

                        <jsp:include page="balance.jsp ">

                    </jsp:include></div>

                </div>

            </div>

            <footer class="container-fluid text-center ">

                <p>Powered by <a href="https://www.naira.com " title="Visit
                    naira.com "><img src="&lt;c:url value=" resources=" " imgs="
                    " logo.png"="">&quot; height=&quot;25&quot; alt=&quot;Naira Logo&quot;</a>
                    </p>
                    </footer>
    </body>
    <!-- <body>
        <div class="container">
            <div class="row">
                <div class="col-md-12">
                    <img src="<c:url value="/resources/imgs/nairadotcombanner.jpg"/>" alt="Naira.com Logo" style ="width: 100%; margin-bottom: 10px" />
                </div>
            </div>

            <div class="row">
                <div class="col-md-12">
                    <div class="card card-default">
                        <div class="card-body">${message}</div>
                    </div>
                </div>
            </div>

            <div class="row">
                <div class="col-md-3">
                    <ul class="list-group">
                        <li class="list-group-item"><a class="link" href="${pageContext.request.contextPath}/innerHome" id="home" data-toggle="tooltip" title="view home page">home</a></li>
                        <li class="list-group-item"><a class="link" href="${pageContext.request.contextPath}/balance" id="balance" data-toggle="tooltip" title="view account balance(s)">balance</a></li>
                        <li class="list-group-item"><a class="link" href="${pageContext.request.contextPath}/pay/0" id="pay" data-toggle="tooltip" title="debit customer's account and give cash out">pay</a></li>
                        <li class="list-group-item"><a class="link" href="${pageContext.request.contextPath}/pay/2" data-toggle="tooltip" title="debit teller's account and collect cash from customer">collect</a></li>        
                        <li class="list-group-item"><a class="link" href="${pageContext.request.contextPath}/transfer/0" data-toggle="tooltip" title="transfer funds to beneficiary">transfer</a></li>
                        <li class="list-group-item"><a class="link" href="${pageContext.request.contextPath}/comingsoon" data-toggle="tooltip" title="transfer funds to beneficiary">bulk payments</a></li>
                        <li class="list-group-item"><a class="link" href="${pageContext.request.contextPath}/vend/0" data-toggle="tooltip" title="vend airtime from telecoms provider">airtime</a></li>
                        <li class="list-group-item"><a class="link" href="${pageContext.request.contextPath}/comingsoon" data-toggle="tooltip" title="accept bill payments from customers on behalf of billers">bill</a></li>
                        <li class="list-group-item"><a class="link" href="${pageContext.request.contextPath}/settlement" data-toggle="tooltip" title="enable download of settlement reports">settlements</a></li>
                        <li class="list-group-item"><a class="link" href="${pageContext.request.contextPath}/disputemgt" data-toggle="tooltip" title="allows for management of disputes">dispute management</a></li>
                        <li class="list-group-item"><a class="link" href="${pageContext.request.contextPath}/users" data-toggle="tooltip" title="manage users by either creating, deleting, modifying or viewing">users</a></li>
                        <li class="list-group-item"><a class="link" href="${pageContext.request.contextPath}/comingsoon" data-toggle="tooltip" title="manage tellers' till accounts">accounts</a></li>
                        <li class="list-group-item"><a class="link" href="${pageContext.request.contextPath}/comingsoon" data-toggle="tooltip" title="view transactions happening on SwitchPay">switchpay transactions</a></li>
                        <li class="list-group-item"><a class="link" href="${pageContext.request.contextPath}/ministatement" data-toggle="tooltip" title="display last 10 transactions">ministatement</a></li>
                        <li class="list-group-item"><a class="link" href="${pageContext.request.contextPath}/cpassword" data-toggle="tooltip" title="you can change your password">change password</a></li>
                        <li class="list-group-item"><a class="link" href="${pageContext.request.contextPath}/ctoken" data-toggle="tooltip" title="you can change your password">change token</a></li>
                        <li class="list-group-item"><a href="logout" data-toggle="tooltip" title="done with using the channel, you can log out">logout</a></li>
                    </ul>
                </div>

                <div class="col-md-9" id="result1">
                    <jsp:include page="balance.jsp" />
                </div>
            </div>
        </div>
        <footer class="container-fluid text-center">
            <p>Powered by <a href="https://www.naira.com" title="Visit naira.com"><img src="<c:url value="/resources/imgs/logo.png"/>" height="25" alt="Naira Logo" </a></p> 
        </footer>
    </body> -->
</html>