
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page language="java" import="java.sql.*, javax.naming.*, javax.sql.DataSource,java.util.*"  session="true"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@page session="true"%>
<!DOCTYPE html>
<html>
    <head>
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Naira.com</title>
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-beta.2/css/bootstrap.min.css" integrity="sha384-PsH8R72JQ3SOdhVi3uxftmaW6Vc51MKb0q5P2rRUpPvrszuE4W1povHYgTpBfshb" crossorigin="anonymous">
        <script src="https://code.jquery.com/jquery-3.2.1.slim.min.js" integrity="sha384-KJ3o2DKtIkvYIK3UENzmM7KCkRr/rE9/Qpg6aAZGJwFDMVNA/GpGFF93hXpG5KkN" crossorigin="anonymous"></script>
        <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.12.3/umd/popper.min.js" integrity="sha384-vFJXuSJphROIrBnz7yo7oB41mKfc8JzQZiCq4NCceLEaO4IHwicKwpJf9c9IpFgh" crossorigin="anonymous"></script>
        <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-beta.2/js/bootstrap.min.js" integrity="sha384-alpBpkh1PFOepccYVYDB4do5UnbKysX5WZXm3XxPqe5iKTfUKjNkCk9SaVuEZflJ" crossorigin="anonymous"></script>
        <link href="<spring:url value="/resources/css/bootstrap.css?v=1.00"/>" rel="stylesheet" /> 
        <link href="<spring:url value="/resources/css/custom.css?v=1.00"/>" rel="stylesheet" />
        <link rel="icon" href="<spring:url value="/resources/imgs/logo.png"/>" type="image/png"/> 
        <link href="https://fonts.googleapis.com/css?family=Roboto|Montserrat:900" rel="stylesheet"/>
    </head>
    <body class="">
        <header class="fixed-top" style="background-color: rgba(255,255,255,0.0);">
            <div class="container">
                <div class="row">
                    <div class="col-12">
                        <nav class="navbar navbar-expand-lg py-0">
                            <!-- <img src="<c:url value="/resources/imgs/logo.png"/>" alt="naira Logo" height=50>
                            <a class="navbar-brand" href="welcome">Naira.com</a> -->
                            <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarSupportedContent" aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
                                <span class="navbar-toggler-icon"><i class="fa fa-bars" aria-hidden="true"></i></span>
                            </button>
                            <div class="collapse navbar-collapse" id="navbarSupportedContent">
                                <ul class="navbar-nav ml-auto">
                                    <li class="nav-item">

                                        <a href="/payBills" class="btn btn-sm btn-primary" data-toggle="modal" data-target="#loginModal">Pay Bills</a>
                                        <a href="/airtimePurchase" class="btn btn-sm btn-primary" data-toggle="modal" data-target="#loginModal">Purchase Airtime</a>
                                        <a href="/fundTransfer" class="btn btn-sm btn-primary" data-toggle="modal" data-target="#loginModal">Transfer Funds</a>
                                    </li>           
                                </ul>
                            </div>
                        </nav>
                    </div>
                </div>
            </div>
        </header>

        <!-- Login Modal -->
        <div id="loginModal" class="modal" tabindex="-1" role="dialog">
            <div class="modal-dialog" role="document">
                <div class="modal-content">
                    <div class="modal-header border-0">
                        <h5 class="modal-title text-secondary display-4">Login</h5>
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                            <span aria-hidden="true">&times;</span>
                        </button>
                    </div>
                    <div class="modal-body">
                        <form name='loginForm' action="<c:url value='/j_spring_security_check' />" method='POST'>

                            <div class="form-group">
                                <label>Username</label>
                                <input type="text" name="username" placeholder="Username" class="form-control" required>
                            </div>
                            <div class="form-group">
                                <label>Password</label>
                                <input type="password" name="password" placeholder="Password" class="form-control">
                            </div>
                            <div class="form-group m-0 text-center">
                                <button type="submit" name="submit" class="btn btn-primary btn-block mb-3"><i class="fa fa-unlock" aria-hidden="true"></i> Login</button>
                                <!--<a class="text-secondary" href="#registerModal" data-toggle="modal" data-target="#registerModal">Register</a>-->
                            </div>

                            <input type="hidden" name="${_csrf.parameterName}"
                                   value="${_csrf.token}" />

                        </form>
                    </div>
                </div>
            </div>
        </div>


        <section style="background-image: url(<spring:url value="/resources/imgs/1.jpg"/>); background-repeat: no-repeat; background-size: cover; background-position: center;">
            <div class="container">
                <div class="py-5"></div>
                <div class="row">
                    <div class="col-12 d-flex">
                        <div class="bg-dark py-3 px-2" style="width: 300px; min-width:300px; height: 550px; border-radius: 20px;margin-bottom: -120px">
                            <div class="bg-light p-1 w-25 mx-auto" style="border-radius: 5px; margin-bottom: 5px;"></div>
                            <div class="bg-white w-100" style="border-radius: 20px; height: calc(100% - 20px); background-image: url(<spring:url value="/resources/imgs/mobile_naira.jpg"/>); background-size: 100%"></div>
                        </div>
                        <div class="w-auto pl-5 p-3 align-self-center">
                            <h2 class="display-3 text-white text-shadow font2">enjoy</h2>
                            <h2 class="display-3 text-white text-shadow font2">online</h2><br>
                            <h2 class="display-3 text-white text-shadow font2"><span class="text-primary">payment</span></h2><br>
                            <h2 class="display-3 text-white text-shadow font2">services </h2><br>
                            <div class="border border-primary mb-3 w-25"></div>
                            <a href="/payBills" class="btn btn-lg btn-primary" data-toggle="modal" data-target="#loginModal">Login</a>

                        </div>
                    </div>
                </div>
            </div>
        </section>
<footer class="container-fluid text-center">
  <p>Powered by <a href="https://www.naira.com" title="Visit naira.com"><img src="<c:url value="/resources/imgs/logo.png"/>" height="25" alt="Naira Logo" </a></p> 
</footer>             
        <script>
            $(window).scroll(function (event) {
                // console.log('hello world');
                // console.log($(this).scrollTop());
                if ($(this).scrollTop() > 50 && !$('header').hasClass('bg-white')) {
                    $('header').addClass('bg-white').addClass('text-dark');
                } else if ($(this).scrollTop() < 50 && $('header').hasClass('bg-white')) {
                    $('header').removeClass('bg-white').removeClass('text-dark');
                }
            });
            $(function () {
                $('.tab-all').click(function (event) {
                    event.preventDefault();
                    var th = $(this);
                    setTimeout(function () {
                        th.removeClass('active');
                    }, 20);
                });
                $('[data-bind-link]').click(function () {
                    var dataval = $(this).attr('data-bind-link');
                    $(dataval).trigger('click');
                });
            });
        </script>