
<!DOCTYPE html>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page language="java" import="java.sql.*, javax.naming.*, javax.sql.DataSource,java.util.*"  session="true"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%@page session="true"%>
<html>
    <head>

        <title>${title}</title>
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <link href="<spring:url value="/resources/css/bootstrap.css?v=1.00"/>" rel="stylesheet" />
        <link href="<spring:url value="/resources/css/custom.css"/>" rel="stylesheet" />
        <link href="<spring:url value="/resources/css/dropzone.css"/>" rel="stylesheet" />
        <link rel="stylesheet" href="<spring:url value="/resources/css/font-awesome.min.css"/>"/>
        <script src="<spring:url value="/resources/js/jquery.js"/>"></script>
        <script src="<spring:url value="/resources/js/popper.min.js"/>"></script>
        <script src="<spring:url value="/resources/js/bootstrap.js"/>"></script>
        <link rel="icon" href="<spring:url value="/resources/imgs/naira_logo.png"/>" type="image/png"/>
        <link href="https://fonts.googleapis.com/css?family=Roboto|Montserrat:900" rel="stylesheet"/>
        <script>
            var spinner = '<i class="fa fa-spinner fa-spin" aria-hidden="true" style="font-size:100px;"></i>';
            var timeout;
            function startAutoReload(url){
                $('#result1').html(spinner);
                timeout = setTimeout(){
                    $.get(url, function(result) {
                        $('#result1').html(result);
                        
                    }, 5 * 60 * 60 * 1000);
                }
            }
            startAutoReload('${pageContext.request.contextPath}/innerHome');
            $(document).ready(function () {
                $('.link').click(function (e) {
                    e.preventDefault();
                    $('#showResult').empty();
                    $('#toggleDiv').hide();
                    $('#result1').show();
                    var url = $(this).attr('href');
                    var location = url.split('/');
                    location = location[location.length-1];
                    $('#result1').html(spinner);
                    $.get(url, function (result) {
                        $('#result1').html(result);
                        if(location == 'innerHome'){
                            startAutoReload(url);
                        }else{
                            clearTimeout(timeout);
                        }
                    });
                });
                $('#bulkpay').click(function (e) {
                    url = $(this).attr('href');
                    $.get(url, function (result) {
                        $('#showResult').html(result);
                    });
                    $('#result1').hide();
                    $('#toggleDiv').show();
                });
            });
        </script>
    </head>
    <body>
        <div class="container">
            <div class="row">
                <div class="col-md-12">
                    <img src="<c:url value="/resources/imgs/naira_banner.jpg"/>" alt="army logo" style ="width: 100%; height:126px; margin-bottom: 10px" />
                </div>

                <div class="col-md-9">
                    <h1 class="display-4">${heading}</h1>
                </div>
            </div>

            <div class="row">
                <div class="col-md-12">
                    <div class="card">
                        <div class="card-body">${message}</div>
                    </div>
                </div>
            </div>

            <div class="row">
                <div class="col-sm-3">
                    <ul class="list-group">

                        <li class="list-group-item"><a class="link" href="${pageContext.request.contextPath}/innerHome" id="innerHome" data-toggle="tooltip" title="view home page">home</a></li>

                        <li class="list-group-item"><a class="link" href="${pageContext.request.contextPath}/balance" id="balance" data-toggle="tooltip" title="view home page">balance</a></li>
                        
                        <li class="list-group-item"><a class="link" href="${pageContext.request.contextPath}/pay" id="singlepay" data-toggle="tooltip" title=" pay a beneficiary">pay</a>

                        <li class="list-group-item"><a class="link" href="${pageContext.request.contextPath}/collect" id="collect" data-toggle="tooltip" title="check payment history">collect</a></li>
                        
                        <li class="list-group-item"><a class="link" href="${pageContext.request.contextPath}/transfer" id="transfer" data-toggle="tooltip" title="check payment history">transfer</a></li>

                        <li class="list-group-item"><a class="link" href="${pageContext.request.contextPath}/bulkpay" id="bulkpay" data-toggle="tooltip" title="make bulk payments">bulk payments</a></li>

                        <li class="list-group-item"><a class="link" href="${pageContext.request.contextPath}/bills" id="bill" data-toggle="tooltip" title="check payment history">bill</a></li>
                        
                        <li class="list-group-item"><a class="link" href="${pageContext.request.contextPath}/settlements" data-toggle="tooltip" id="profile" title="edit your account information">settlements</a></li>

                        <li class="list-group-item"><a class="link" href="${pageContext.request.contextPath}/dispute_management" data-toggle="tooltip" id="profile" title="edit your account information">dispute management</a></li>

                        <li class="list-group-item"><a class="link" href="${pageContext.request.contextPath}/users" data-toggle="tooltip" id="profile" title="edit your account information">users</a></li>

                        <li class="list-group-item"><a class="link" href="${pageContext.request.contextPath}/accounts" data-toggle="tooltip" id="profile" title="edit your account information">accounts</a></li>
                        
                        <li class="list-group-item"><a class="link" href="${pageContext.request.contextPath}/ministatement" data-toggle="tooltip" id="profile" title="edit your account information">ministatement</a></li>
                        
                        <li class="list-group-item"><a class="link" href="${pageContext.request.contextPath}/change_password" data-toggle="tooltip" id="profile" title="edit your account information">change password</a></li>

                        <li class="list-group-item"><a class="link" href="${pageContext.request.contextPath}/change_token" data-toggle="tooltip" id="profile" title="edit your account information">change token</a></li>

                        <li class="list-group-item"><a href="${pageContext.request.contextPath}/logout" data-toggle="tooltip" title="done with using the platform, you can log out">logout</a></li>

                    </ul>
                </div>
                        <div class="col-md-9" id="toggleDiv" style="display:none;">
                            <div class="w-75 mx-auto">
                                <div class="bg-white mb-2 p-2">
                                    <h4 class="p5"><b>Drop files here</b></h4>
                                    <hr>
                                    <div id="drop_display"></div>
                                    <form action="/ArmyPosting/uploadFiles" method="POST" enctype="multipart/form-data" id="dropzoneForm" class="dropzone">                            

                                    </form>

                                    <div class="form-group text-center m-2">
                                        <span id="display"></span>
                                        <button type="button" class="btn btn-primary" id="submit_file">Upload Files</button>
                                    </div>    
                                </div>
                            </div>
                            <div id="showResult"></div>
                        </div>
                <div class="col-sm-9" id="result1">
                    <jsp:include page="innerHome.jsp" />
                </div>
            </div> 
        </div>
        <footer class="text-center">
            Powered by <a href="https://www.naira.com" title="Visit naira.com"><img src="resources/imgs/logo.png" height="25" alt="Naira Logo" </a> 
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
                        // Show submit button here and/or inform user to click it.
                        this.removeFile(file);
                        
                    });

                },
                 success: function(){
                     $('#drop_display').html('<div class="alert alert-success text-center">' + 'Successful' + '</div>'); 
                     setTimeout(function () {
                         $('#drop_display').html("");
                    }, 7000);
                }
            };
                </script>
         
    </body>
</html>