
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page errorPage="errorPage.jsp" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <!--<script src="https://code.jquery.com/jquery-3.2.1.slim.min.js" integrity="sha384-KJ3o2DKtIkvYIK3UENzmM7KCkRr/rE9/Qpg6aAZGJwFDMVNA/GpGFF93hXpG5KkN" crossorigin="anonymous"></script>-->
        <script src="<spring:url value="/resources/js/jquery.js"/>"></script>
        <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.12.3/umd/popper.min.js" integrity="sha384-vFJXuSJphROIrBnz7yo7oB41mKfc8JzQZiCq4NCceLEaO4IHwicKwpJf9c9IpFgh" crossorigin="anonymous"></script>
        <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-beta.2/js/bootstrap.min.js" integrity="sha384-alpBpkh1PFOepccYVYDB4do5UnbKysX5WZXm3XxPqe5iKTfUKjNkCk9SaVuEZflJ" crossorigin="anonymous"></script>
        <!--<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css" integrity="sha384-Gn5384xqQ1aoWXA+058RXPxPg6fy4IWvTNh0E263XmFcJlSAwiGgFAW/dAiS6JXm" crossorigin="anonymous">-->
        <link href="<spring:url value="/resources/css/bootstrap.css"/>" rel="stylesheet" />
        <link href="<spring:url value="/resources/css/dropzone.css"/>" rel="stylesheet" />
        <link rel="stylesheet" href="<spring:url value="/resources/css/font-awesome.min.css"/>"/>
        <link href="<spring:url value="/resources/css/custom.css"/>" rel="stylesheet" />
        <link rel="icon" href="<spring:url value="/resources/imgs/logo.png"/>" type="image/png"/>
        <link href="https://fonts.googleapis.com/css?family=Roboto|Montserrat:900" rel="stylesheet"/>
        <title>${title}</title>
        <script>
            var spinner = `<div class="text-center p-5"><i class="fa fa-spinner fa-spin" aria-hidden="true" style="font-size:100px;"></i></div>`;
            var interval;
            function startAutoReload(url) {
                console.log('call start');
                interval = setInterval(function () {
                    $('#result1').html(spinner);
                    $.get(url, function (result) {
                        console.log('showing dashboard');
                        $('#result1').html(result);
                    });
                }, 5 * 1000);
            }

            $(document).ready(function () {
                $(document).on('click', '.link', function (e) {
                    e.preventDefault();
                    url = $(this).attr('href');
                    var location = url.split('/');
                    location = location[location.length - 1];
                    console.log(location);
                    $('#result1').html(spinner);
                    $.get(url, function (result) {
                        $('#result1').html(result);

                        if (location != 'dashboard' && location != 'settlement') {
                            clearInterval(interval);

                            if (location == 'bulkpayment') {
                                $('#toggleDiv').show();
                            } else {
                                $('#toggleDiv').hide();
                            }
                        } else {
                            startAutoReload(url);
                        }
                    });
                });

                $(document).on('click', '.changePassword', function (e) {
                    e.preventDefault();
                    var form = $(this).closest('form');
                    var data = form.serialize();
                    var url = form.attr('action');
                    $.get(url, data, function (result) {
                        alert(result);
                        form[0].reset();
                    });
                });

                $(document).on('click', '.changeToken', function (e) {
                    e.preventDefault();
                    var form = $(this).closest('form');
                    var data = form.serialize();
                    var url = form.attr('action');
                    $.get(url, data, function (result) {
                        alert(result);
                        form[0].reset();
                    });
                });

                $(document).on('click', '.adduser', function (e) {
                    e.preventDefault();
                    var form = $(this).closest('form');
                    var data = form.serialize();
                    var url = form.attr('action');
                    $.get(url, data, function (result) {
                        alert(result);
                        form[0].reset();
                    });
                });
                $(document).on('click', '.edituser', function (e) {
                    e.preventDefault();
                    var form = $(this).closest('form');
                    var data = form.serialize();
                    console.log(data);
                    var url = form.attr('action');
                    $.get(url, data, function (result) {
                        if(result === 'Successful'){
                        $('#edit_display').html('<div class="alert alert-success text-center">' + 'Update was SUCCESSFUL' + '</div>');
                        form[0].reset();
                        setTimeout(function () {
                           $('#edit_display').html(""); 
                    }, 7000);
                        
                    }else{
                        $('#edit_display').html('<div class="alert alert-success text-center">' + 'User update FAILED' + '</div>');;
                        setTimeout(function () {
                    }, 5000);
        }
                    });
                });

                $(document).on('click', '.closeModal', function (e) {
                    e.preventDefault();
                    var url = '${pageContext.request.contextPath}/users';
                    $('#result1').html(spinner); //just added this line
                    $.get(url, function (result) {
                        $('#result1').html(result);
                    });
                });

                $(document).on('click', '#continuepay', function (e) {
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
                    if (srcAcc === '') {
                        $(objSrc).after('<span class="errs text-danger"><small>Please select an account source</small></span>');
                        $(objSrc).focus();
                        return;
                    } else if (acnum.length < 10) {
                        $(objAcc).after('<span class="errs text-danger"><small>Account number can NOT be less than 10 characters</small></span>');
                        $(objAcc).focus();
                        return;
                    } else if (acnum.length > 10) {
                        $(objAcc).after('<span class="errs text-danger"><small>Account number can NOT be greater than 10 characters</small></span>');
                        $(objAcc).focus();
                        return;
                    } else if (acnum.match(/\D/)) {
                        $(objAcc).after('<span class="errs text-danger"><small>Can only contain Digits</small></span>');
                        $(objAcc).focus();
                        return;
                    } else if (amt.replace(' ', '') === "") {
                        $(objAmt).after('<span class="errs text-danger"><small>Amount cannot be empty</small></span>');
                        $(objAmt).focus();
                        return;
                    } else if (amt.match(/[^0-9.]/)) {
                        $(objAmt).after('<span class="errs text-danger"><small>Can only contain Digits</small></span>');
                        $(objAmt).focus();
                        return;
                    } else if (amt.match(/\.(\d){3,}/)) {
                        $(objAmt).after('<span class="errs text-danger"><small>You can only have 2 decimal places</small></span>');
                        $(objAmt).focus();
                        return;
                    }
                    $.get(url, data, function (result) {
                        form[0].reset();
                        $('#result1').html(result);
                    });
                });

                $(document).on('click', '#continuetransfer', function (e) {
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
                    if (srcAcc === '') {
                        $(objSrc).after('<span class="errs text-danger"><small>Please select an account source</small></span>');
                        $(objSrc).focus();
                        return;
                    } else if (acnum.length < 10) {
                        $(objAcc).after('<span class="errs text-danger"><small>Account number can NOT be less than 10 characters</small></span>');
                        $(objAcc).focus();
                        return;
                    } else if (acnum.length > 10) {
                        $(objAcc).after('<span class="errs text-danger"><small>Account number can NOT be greater than 10 characters</small></span>');
                        $(objAcc).focus();
                        return;
                    } else if (acnum.match(/\D/)) {
                        $(objAcc).after('<span class="errs text-danger"><small>Can only contain Digits</small></span>');
                        $(objAcc).focus();
                        return;
                    } else if (acnum == srcAcc.split('#')[0]) { //<---- 08/12/17 just add this 
                        $(objAcc).after('<span class="errs text-danger"><small>You can not pay to the same account</small></span>');
                        $(objAcc).focus();
                        return;//<---- 08/12/17 ends here 
                    } else if (amt.replace(' ', '') === "") {
                        $(objAmt).after('<span class="errs text-danger"><small>Amount cannot be empty</small></span>');
                        $(objAmt).focus();
                        return;
                    } else if (amt.match(/[^0-9.]/)) {
                        $(objAmt).after('<span class="errs text-danger"><small>Can only contain Digits</small></span>');
                        $(objAmt).focus();
                        return;
                    } else if (amt.match(/\.(\d){3,}/)) {
                        $(objAmt).after('<span class="errs text-danger"><small>You can only have 2 decimal places</small></span>');
                        $(objAmt).focus();
                        return;
                    }
                    $.get(url, data, function (result) {
                        form[0].reset();
                        $('#result1').html(result);
                    });
                });

                $(document).on('click', '#continuevend', function (e) {
                    e.preventDefault();
                    var form = $(this).closest('form');
                    form.find('.errs').remove();
                    var data = form.serialize();
                    var url = form.attr('action');
                    $.get(url, data, function (result) {
                        form[0].reset();
                        $('#result1').html(result);
                    });
                });

                $(document).on('click', '.completepay', function (e) {
                    e.preventDefault();
                    $(this).hide('fast');
                    var form = $(this).closest('form');
                    var data = form.serialize();
                    var url = '${pageContext.request.contextPath}/completePayment';
                    $.get(url, data, function (result) {
                        if (result == '00') {
                            $('#pay-alert').html('Payment successfully processed').addClass('alert alert-success');
                            alert('Payment successfully processed');
                        } else if (result == '98') {
                            $('#pay-alert').html('Wrong token entered, payment NOT successfully processed').addClass('alert alert-danger');

                            alert('Wrong token entered, payment NOT successfully processed');
                        } else {
                            $('#pay-alert').html('Payment NOT successfully processed').addClass('alert alert-danger');

                            alert('Payment NOT successfully processed');
                        }
                    });
                });

                $(document).on('click', '.cancelpay', function (e) {
                    $($(this)[0].dataset.bindlink).trigger('click');
                })

                $(document).on('click', '.completetransfer', function (e) {
                    e.preventDefault();
                    $(this).hide('fast');
                    var form = $(this).closest('form');
                    var data = form.serialize();
                    var url = '${pageContext.request.contextPath}/completetransfer';
                    $.get(url, data, function (result) {
                        if (result == '00') {
                            $('#transfer-alert').html('Fund Transfer successfully processed').addClass('alert alert-success');
                            alert('Fund Transfer successfully processed');
                        } else if (result == '98') {
                            $('#transfer-alert').html('Wrong token entered, fund transfer NOT successfully processed').addClass('alert alert-danger');

                            alert('Wrong token entered, fund transfer NOT successfully processed');
                        } else {
                            $('#transfer-alert').html('Fund transfer NOT successfully processed').addClass('alert alert-danger');

                            alert('Fund transfer NOT successfully processed');
                        }
                    });
                });

                $(document).on('click', '.canceltransfer', function (e) {
                    $($(this)[0].dataset.bindlink).trigger('click');
                });

                $(document).on('click', '.completevend', function (e) {
                    e.preventDefault();
                    $(this).hide('fast');
                    var form = $(this).closest('form');
                    var data = form.serialize();
                    var url = '${pageContext.request.contextPath}/completevend';
                    $.get(url, data, function (result) {
                        if (result == '00') {
                            $('#vend-alert').html('Airtime purchase successfully processed').addClass('alert alert-success');

                            alert('Airtime purchase successfully processed');
                        } else if (result == '98') {
                            $('#transfer-alert').html('Wrong token entered, airtime purchase NOT successfully processed').addClass('alert alert-danger');

                            alert('Wrong token entered, airtime purchase NOT successfully processed');
                        } else {
                            $('#transfer-alert').html('Airtime purchase NOT successfully processed').addClass('alert alert-danger');

                            alert('Airtime purchase NOT successfully processed');
                        }
                    });
                });

                $(document).on('click', '.cancelvend', function (e) {
                    $($(this)[0].dataset.bindlink).trigger('click');
                });
                
//                $(document).on('click', '.search_btn', function(e){
//                   e.preventDefault();
//                   var form = $(this).closest('form');
//                    form.find('.errs').remove();
//                    var data = form.serialize();
//                    var url = form.attr('action');
//                    
//                    var objSearchBy = form[0].search_by;
//                    var objSearchString = form[0].search_string;
//                    
//                    var search_by = objSearchBy.value.toString();
//                    
//                    console.log(search_by);
//                    console.log(objSearchString);
//                    
//                    
//                });
//                

            });

            $(document).on('change', '.masterCheck', function () {
                if ($(this).prop('checked') == true) {
                    $('.userProfiles').find('input:not(.masterCheck)').prop('checked', true);
                } else {
                    $('.userProfiles').find('input:not(.masterCheck)').prop('checked', false);
                }
            });

         

            $(document).on('click', '#searchForFiles', function (e) {
                e.preventDefault();
                $('#result1').html(spinner);
                var form = $(this).closest('form');
                var dataVal = form.serialize();
                var url = 'searchFiles';
                $.get(url, dataVal, function (result) {
                    $('#result1').html(result);
                    console.log(url);
                    console.log(result);
                });
            });
            $(document).on('click', '#searchForUsers', function (e) {
                e.preventDefault();
                $('#result1').html(spinner);
                var form = $(this).closest('form');
                var dataVal = form.serialize();
                var url = 'searchUsers';
                $.get(url, dataVal, function (result) {
                    $('#result1').html(result);
                    console.log(url);
                    console.log(result);
                });
            });
              $(document).on('click', '#searchForSettlements', function (e) {
                e.preventDefault();
                $('#result1').html(spinner);
                var form = $(this).closest('form');
                var dataVal = form.serialize();
                var url = 'searchSettlements';
                $.get(url, dataVal, function (result) {
                    $('#result1').html(result);
                    console.log(url);
                    console.log(result);
                });
            });
            $(document).on('change', '.masterCheck', function () {
                if ($(this).prop('checked') == true) {
                    $('.userProfiles').find('input:not(.masterCheck)').prop('checked', true);
                } else {
                    $('.userProfiles').find('input:not(.masterCheck)').prop('checked', false);
                }
            });
            
            $(document).on('change', '.masterCheck', function () {
                if ($(this).prop('checked') === true) {
                    $('.bulk_files').find('input:not(.masterCheck)').prop('checked', true);
                } else {
                    $('.bulk_files').find('input:not(.masterCheck)').prop('checked', false);
                }
            });
            
            $(document).on('change', '.masterCheck', function () {
                if ($(this).prop('checked') === true) {
                    $('.settlements_reports').find('input:not(.masterCheck)').prop('checked', true);
                } else {
                    $('.settlements_reports').find('input:not(.masterCheck)').prop('checked', false);
                }
            });
            var reportsArray = [];
            $(document).on('click', '.downloadReports', function (e) {
                e.preventDefault();
//                var firstEl = reportsArray[1] ? reportsArray[1] : false;
                var url = "downloadZipped";
                console.log('Hello world');
                var data = reportsArray.join(",");
                // var data = JSON.stringify(bkarray);
                console.log(data);
                $.get(url, {filename: data}, function () {
                    console.log(data);
                });
            });
            $(document).on('change', '.reportBox', function () {
                var chk = $(this);
                var id = chk.next().val();
//                return;
                $('.clicked_file').val(id);
//                $('.downloadReports').attr('href', '/'+id);
//                return;
                if (chk.prop("checked")) {
                    reportsArray.push(id);
                } else {
                    if (reportsArray.indexOf(id) > -1) {
                        var i = reportsArray.indexOf(id);
                        reportsArray.splice(i, 1);
                    }
                }
            });
            
            
            var usersArray = [];
           $(document).on('click', '.deleteUsers', function () {
                var url = "deleteUsers";
                console.log('Hello world');
                var data = usersArray.join(",");
// var data = JSON.stringify(bkarray);
                $.post(url, {array: data}, function (result) {
                    if (result === "Successful") {
                        $('#result1').html(spinner);
                        var url = "users";
                        $.get(url, function (result) {
                            $('#result1').html(result);
                        });
                        console.log(result);
                    }
                });
            });
            $(document).on('change', '.userBox', function () {
                var chk = $(this);
                var id = chk.next().val();
                if (chk.prop("checked")) {
                    usersArray.push(id);
                } else {
                    if (usersArray.indexOf(id) > -1) {
                        var i = usersArray.indexOf(id);
                        usersArray.splice(i, 1);
                    }
                }
            });
            var bkarray = [];
            $(document).on('click', '.bkdelete', function () {
                var url = "deleteFilename";
                console.log('Hello world');
                var data = bkarray.join(",");
// var data = JSON.stringify(bkarray);
                $.post(url, {array: data}, function (result) {
                    if (result === "Successful") {
                        $('#result1').html(spinner);
                        var url = "bulkpayment";
                        $.get(url, function (result) {
                            $('#result1').html(result);
                        });
                        console.log(result);
                    }
                });
            });
            $(document).on('change', '.bkbox', function () {
                var chk = $(this);
                var id = chk.next().val();
                if (chk.prop("checked")) {
                    bkarray.push(id);
                } else {
                    if (bkarray.indexOf(id) > -1) {
                        var i = bkarray.indexOf(id);
                        bkarray.splice(i, 1);
                    }
                }
// console.log("array:");
// console.log(bkarray);
            });
            
        </script>
    </head>
    <body>
        <div class="container">
            <div class="row">
                <div class="col-md-12">
                    <img <img src="<c:url value="/resources/imgs/nairadotcombanner.jpg"/>" alt="Naira.com Logo" style ="width: 100%; margin-bottom: 10px" />
                </div>
            </div>
            <div class="row mb-3">
                <div class="col-md-12">
                    <div class="card">
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
                        <li class="list-group-item"><a class="link" href="${pageContext.request.contextPath}/bulkpayment" data-toggle="tooltip" title="transfer funds to beneficiary">bulk payments</a></li>
                        <li class="list-group-item"><a class="link" href="${pageContext.request.contextPath}/bulkpayment" id="bulkpay" data-toggle="tooltip" title="transfer funds to beneficiary">bulk payments approval</a></li>
                        <li class="list-group-item"><a class="link" href="${pageContext.request.contextPath}/vend/0" data-toggle="tooltip" title="vend airtime from telecoms provider">airtime</a></li>
                        <li class="list-group-item"><a class="link" href="${pageContext.request.contextPath}/billsPayment" data-toggle="tooltip" title="accept bill payments from customers on behalf of billers">bill</a></li>
                        <li class="list-group-item"><a class="link" href="${pageContext.request.contextPath}/settlement" data-toggle="tooltip" title="enable download of settlement reports">settlements</a></li>
                        <li class="list-group-item"><a class="link" href="${pageContext.request.contextPath}/disputemgt" data-toggle="tooltip" title="allows for management of disputes">dispute management</a></li>
                        <li class="list-group-item"><a class="link" href="${pageContext.request.contextPath}/users" data-toggle="tooltip" title="manage users by either creating, deleting, modifying or viewing">users</a></li>
                        <li class="list-group-item"><a class="link" href="${pageContext.request.contextPath}/comingsoon" data-toggle="tooltip" title="manage tellers' till accounts">accounts</a></li>
                        <li class="list-group-item"><a class="link" href="${pageContext.request.contextPath}/dashboard" data-toggle="tooltip" title="view transactions happening on SwitchPay">switchpay transactions</a></li>
                        <li class="list-group-item"><a class="link" href="${pageContext.request.contextPath}/ministatement" data-toggle="tooltip" title="display last 10 transactions">ministatement</a></li>
                        <li class="list-group-item"><a class="link" href="${pageContext.request.contextPath}/cpassword" data-toggle="tooltip" title="you can change your password">change password</a></li>
                        <li class="list-group-item"><a class="link" href="${pageContext.request.contextPath}/ctoken" data-toggle="tooltip" title="you can change your password">change token</a></li>
                        <li class="list-group-item"><a href="logout" data-toggle="tooltip" title="done with using the channel, you can log out">logout</a></li>
                    </ul>
                </div>


                <div class="col-md-9">
                    <div id="toggleDiv" style="display:none;">
                        <div class="w-75 mx-auto">
                            <div class="bg-white mb-2 p-2">
                                <h4 class="p5"><b>Drop files here</b></h4>
                                <hr>
                                <div id="drop_display"></div>
                                <form action="/SwitchPayPortal/uploadFiles" method="POST" enctype="multipart/form-data" id="dropzoneForm" class="dropzone">                            

                                </form>

                                <div class="form-group text-center m-2">
                                    <span id="display"></span>
                                    <button type="button" class="btn btn-primary" id="submit_file">Upload Files</button>
                                </div>    
                            </div>
                        </div>
                    </div>
                    <div id="result1">
                        <jsp:include page="innerHome.jsp" />
                    </div>
                </div>
            </div>
        </div>
        <footer class="container-fluid text-center">
            <p>Powered by <a href="https://www.naira.com" title="Visit naira.com"><img src="<c:url value="/resources/imgs/logo.png"/>" height="25" alt="Naira Logo" </a></p> 
        </footer>
        <script src="<spring:url value="/resources/js/dropzone.js"/>"></script>
        <script>
            Dropzone.options.dropzoneForm = {
                // Prevents Dropzone from uploading dropped files immediately
                autoProcessQueue: false,
                init: function () {
                    var submitButton = document.querySelector("#submit_file");
                    dropzoneForm = this; // closure

                    submitButton.addEventListener("click", function () {
                        dropzoneForm.processQueue(); // Tell Dropzone to process all queued files.
                    });

                    // You might want to show the submit button only when 
                    // files are dropped here:
                    this.on("success", function (file) {
                        this.removeFile(file);

                    });

                },
                success: function () {
                    $('#drop_display').html('<div class="alert alert-success text-center">' + 'Successful' + '</div>');

                    setTimeout(function () {
                        $('#drop_display').html("");
                        $('#bulkpay').trigger('click');
                    }, 7000);
                }
            };
        </script>
    </body>
</html>