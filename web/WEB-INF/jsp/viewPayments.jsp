
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page language="java" import="java.sql.*, javax.naming.*, javax.sql.DataSource,java.util.*"  session="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@page session="true"%>

<script>
    // Validation starts//
    var validate = new FormValidator('editPayment', [
        {
            name: 'first_name',
            display: 'Name',
            rules: 'required'
        },
        {
            name: 'last_name',
            display: 'last name',
            rules: 'required'
        },
        {
            name: 'bank_name',
            display: 'bank name',
            rules: 'required'
        },
        {
            name: 'account_number',
            display: 'account number',
            rules: 'required|numeric'
        },
        {
            name: 'amount',
            display: 'amount',
            rules: 'required|numeric'
        },
        {
            name: 'narration',
            display: 'narration',
            rules: 'required'
        }

    ], function (errors, event) {
        event.preventDefault();

        console.log(errors);
        if (errors.length > 0) {
            //Form validation Failled
            // Do nothing
            var form = $('[name=editPayment]');
            form.find('.is-invalid').removeClass('is-invalid');
            form.find('.errors').remove();
            $(errors[0].element).addClass('is-invalid').after('<small class="errors text-danger">' + errors[0].message + '</small>');
            $(errors[0].element)[0].focus();
            console.log('error');
        } else {
            //Form validation Passed
            var form = $('[name=editPayment]');
            form.find('.is-invalid').removeClass('is-invalid');
            form.find('.errors').remove();
            console.log('worked');
            var dataVal = form.serialize();
            console.log(dataVal);
            $.post("updatePayment", dataVal, function (result) {
                console.log(result);
                // form[0].reset();
                //                       console.log(mhval);
                if (result === 'Successful') {
                    // myDisplay.text('Successful');
                    //modalhead.html('<h2 class="text-center">' + result + '</h2>');
                    $('#display').html('<div class="alert alert-success mx-auto">' + result + '</div>');
                    form[0].reset();
                    setTimeout(function () {

                    }, 2000);
                    window.location.reload(true);
                } else {

                    $('#display').html('<div class="alert alert-danger mx-auto">' + result + '</div>');
                    form.show();
                    setTimeout(function () {
                        $('#display').html("");
                    }, 2000);
                }
                // $('#display').remove();
            });
        }
    });//Validation ends here

    $('[name=activatePayment]').submit(function (e) {
        e.preventDefault();
        var form = $(this);
        var dataVal = form.serialize();
        console.log(dataVal);
        $.post("activatePayment", dataVal, function (result) {
            console.log(result);
            // form[0].reset();
            // console.log(mhval);
            if (result === 'Successful') {
                // myDisplay.text('Successful');
                //modalhead.html('<h2 class="text-center">' + result + '</h2>');
                $('#act_display').html('<div class="alert alert-success mx-auto">' + 'Activation' + ' ' + result + '</div>');
                form[0].reset();
                setTimeout(function () {

                }, 2000);
                window.location.reload(true);
            } else {

                $('#act_display').html('<div class="alert alert-danger mx-auto">' + 'Activation' + ' ' + result + '</div>');
                form.show();
                setTimeout(function () {
                    $('#act_display').html("");
                }, 2000);
            }
            // $('#display').remove();
        });
    });
    $('[name=deletePayment]').submit(function (e) {
        e.preventDefault();
        var form = $(this);
        var dataVal = form.serialize();
        console.log(dataVal);
        $.post("deletePayment", dataVal, function (result) {
            console.log(result);
            // form[0].reset();
            // console.log(mhval);
            if (result === 'Successful') {
                // myDisplay.text('Successful');
                //modalhead.html('<h2 class="text-center">' + result + '</h2>');
                $('#del_display').html('<div class="alert alert-success mx-auto">' + result + '</div>');
                form[0].reset();
                setTimeout(function () {

                }, 2000);
                window.location.reload(true);
            } else {

                $('#del_display').html('<div class="alert alert-danger mx-auto">' + result + '</div>');
                form.show();
                setTimeout(function () {
                    $('#del_display').html("");
                }, 2000);
            }
            // $('#display').remove();
        });
    });
    $('[name=suspendPayment]').submit(function (e) {
        e.preventDefault();
        var form = $(this);
        var dataVal = form.serialize();
        console.log(dataVal);
        $.post("suspendPayment", dataVal, function (result) {
            console.log(result);
            // form[0].reset();
            // console.log(mhval);
            if (result === 'Successful') {
                // myDisplay.text('Successful');
                //modalhead.html('<h2 class="text-center">' + result + '</h2>');
                $('#sus_display').html('<div class="alert alert-success mx-auto">' + result + '</div>');
                form[0].reset();
                setTimeout(function () {

                }, 2000);
                window.location.reload(true);
            } else {

                $('#sus_display').html('<div class="alert alert-danger mx-auto">' + result + '</div>');
                form.show();
                setTimeout(function () {
                    $('#sus_display').html("");
                }, 2000);
            }
            // $('#display').remove();
        });
    });

</script>
<script>
    $(document).ready(function () {
        $('.tab-all').click(function (event) {
            // event.preventDefault();
            // var th = $(this);
            // setTimeout(function(){
            //     th.removeClass('active');
            // }, 20);
        });
        $('[data-bind-link]').click(function () {
            var dataval = $(this).attr('data-bind-link');
            $(dataval).trigger('click');
        });

        $('#Editmodal').on('show.bs.modal', function (event) {
            button = $(event.relatedTarget);
            var id = button.data('id');
//                    var first_name = button.data('first_name');
            var account_name = button.data('account_name');
            var bank_code = button.data('bank_code');
            var account_numbers = button.data('account_numbers');
            var amount = button.data('amount');
            var narrations = button.data('narrations');

            var modal = $(this);
            // modal.find('[name=rc]').val(rc);
            modal.find('[name=id]').val(id);
//                    modal.find('[name=first_name]').val(first_name);
            modal.find('[name=account_name]').val(account_name);
            modal.find('[name=bank_code]').val(bank_code);
            modal.find('[name=account_numbers]').val(account_numbers);
            modal.find('[name=amount]').val(amount);
            modal.find('[name=narrations]').val(narrations);
        });
        $('#activateModal').on('show.bs.modal', function (event) {
            button = $(event.relatedTarget);
            var id = button.data('id');
            var modal = $(this);
            modal.find('[name=id]').val(id);
        });
        $('#suspendModal').on('show.bs.modal', function (event) {
            button = $(event.relatedTarget);
            var id = button.data('id');
            var modal = $(this);
            modal.find('[name=id]').val(id);
        });
        $('#deleteModal').on('show.bs.modal', function (event) {
            button = $(event.relatedTarget);
            var id = button.data('id');
            var modal = $(this);
            modal.find('[name=id]').val(id);
        });
    });


    $(window).scroll(function (event) {
        if ($(this).scrollTop() > 50 && !$('header').hasClass('bg-white')) {
            $('header').addClass('bg-white').addClass('text-dark');
        } else if ($(this).scrollTop() < 50 && $('header').hasClass('bg-white')) {
            $('header').removeClass('bg-white').removeClass('text-dark');
        }
    });

</script>
</head>

    <div class="m-5"></div>
    <div class="text-center m-5">
        <h3><u> List of Payments</u></h3>
    </div>
    <div class="row">
        <div class="col-md-4">
            <button class="btn btn-primary" data-toggle="modal" data-target="#addNewPayments">+ new payments</button>
            <button class="btn btn-danger deleteUsers">Delete</button>
        </div>
        <div class="col-md-8">
           
        </div>
        <div class="col-md-12" style="margin-top: 10px;">

            ${pagination}

        </div>
    </div>
    <div class="row">
        <div class="col-sm-1"></div>
        <div class="col-sm-10">
            <table class="table table-striped">
                <thead>
                    <tr>
                        <th>S/N</th>
                        <th>Beneficiary</th>
                        <th>Bank Code</th>
                        <th>Account Number</th>
                        <th>Amount</th>
                        <th>Narration</th>
                    </tr>
                </thead>
                <tbody>
                    <c:if test="${fn:length(payments)>0}">
                        <c:forEach var="payment" items="${payments}" varStatus="status">
                            <tr>
                                <td>${payment.id}</td> 
                                <td>${payment.account_name}</td>
                                <td>${payment.bank_code}</td>
                                <td>${payment.account_numbers}</td>
                                <td>${payment.amount}</td>
                                <td>${payment.narrations}</td>
                                <td>

                                    <c:choose>
                                        <c:when test="${payment.payment_status  gt 0}">

                                            <div class="btn-group">
                                                <button class="btn btn-success btn-sm" type="button">
                                                    Running
                                                </button>
                                                <button type="button" class="btn btn-sm btn-success dropdown-toggle dropdown-toggle-split" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                                                    <span class="sr-only">Toggle Dropdown</span>
                                                </button>
                                                <div class="dropdown-menu">
                                                    <a class="dropdown-item" 
                                                       href="#"
                                                       data-toggle="modal" 
                                                       data-target="#Editmodal" 
                                                       data-id="${payment.id}" 
                                                       data-account_name="${payment.account_name}"
                                                       data-bank_code="${payment.bank_code}"
                                                       data-account_numbers="${payment.account_numbers}"
                                                       data-amount="${payment.amount}"
                                                       data-narrations = "${payment.narrations}">
                                                        Edit
                                                    </a>

                                                    <a class="dropdown-item" 
                                                       href="#"
                                                       data-toggle="modal" 
                                                       data-id="${payment.id}" 
                                                       data-target="#deleteModal">
                                                        Delete
                                                    </a>
                                                    <div class="dropdown-divider"></div>
                                                    <a class="dropdown-item"
                                                       href="#"
                                                       data-toggle="modal" 
                                                       data-id="${payment.id}" 
                                                       data-target="#suspendModal">
                                                        Suspend
                                                    </a>
                                                </div>
                                            </div>
                                        </c:when>
                                        <c:otherwise>
                                            <div class="btn-group">
                                                <button class="btn btn-danger btn-sm" type="button">
                                                    Suspended
                                                </button>
                                                <button type="button" class="btn btn-sm btn-danger dropdown-toggle dropdown-toggle-split" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                                                    <span class="sr-only">Toggle Dropdown</span>
                                                </button>
                                                <div class="dropdown-menu">
                                                    <a class="dropdown-item" 
                                                       href="#"
                                                       data-toggle="modal" 
                                                       data-target="#Editmodal" 
                                                       data-id="${payment.id}" 
                                                       data-account_name="${payment.account_name}"
                                                       data-bank_code="${payment.bank_code}"
                                                       data-account_numbers="${payment.account_numbers}"
                                                       data-amount="${payment.amount}"
                                                       data-narrations = "${payment.narrations}">
                                                        Edit
                                                    </a>

                                                    <a class="dropdown-item" 
                                                       href="#"
                                                       data-toggle="modal" 
                                                       data-id="${payment.id}" 
                                                       data-target="#deleteModal">
                                                        Delete
                                                    </a>
                                                    <div class="dropdown-divider"></div>
                                                    <a class="dropdown-item"
                                                       href="#"
                                                       data-toggle="modal" 
                                                       data-id="${payment.id}" 
                                                       data-target="#activateModal">
                                                        Activate
                                                    </a>
                                                </div>
                                            </div>
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                            </tr>
                        </c:forEach>
                    </c:if>

                </tbody>

            </table>
            <br/>  

        </div>
        <div class="col-sm-1"></div>
    </div>
    <div id="Editmodal" class="modal" tabindex="-1" role="dialog">
        <div class="modal-dialog" role="document">
            <div class="modal-content">
                <div class="modal-header border-0">
                    <h5 class="modal-title text-secondary">Edit Payment record</h5>
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                        <span aria-hidden="true">&times;</span>
                    </button>
                </div>
                <div class="modal-body">
                    <form class="edit" action="updatePayment" method="POST" modelAttribute="payment_edit" name="editPayment">
                        <input type="hidden" name="username" value="${username}">
                        <input type="hidden" name="id" value="${payment.id}">

                        <div id="display"></div>
                        <!--                            <div class="form-group" id="first_name" name="first_name">
                                                        <label>First Name </label>
                                                        <input class="form-control" type="text" name="first_name">
                                                    </div>-->
                        <div class="form-group" id="account_name" name="account_name">
                            <label>Account Name </label>
                            <input class="form-control" type="text" name="account_name">
                        </div>
                        <div class="form-group" id="bank_name" name="bank_code">
                            <label>Bank </label>
                            <input class="form-control" type="text" name="bank_code">
                        </div>
                        <div class="form-group" id="account_number" name="account_numbers">
                            <label>Account number </label>
                            <input class="form-control" type="text" name="account_numbers">
                        </div>
                        <div class="form-group" id="amount" name="amount">
                            <label>Amount </label>
                            <input class="form-control" type="text" name="amount">
                        </div>
                        <div class="form-group" id="narration" name="narrations">
                            <label>Narration </label>
                            <input class="form-control" type="text" name="narrations">
                        </div>
                        <div class="form-group m-0 text-center">
                            <button type="submit" name="submit" class="btn btn-primary btn-block mb-3"><i class="fa fa-unlock" aria-hidden="true"></i> Save</button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>  
    <div id="activateModal" class="modal" tabindex="-1" role="dialog">
        <div class="modal-dialog" role="document">
            <div class="modal-content">
                <div class="modal-header border-0">
                    <h5>You are about to Activate this payment?</h5>
                </div>
                <div class="modal-body">
                    <div id="act_display"></div>
                    <form class="reg" action="activatePayment" method="POST" modelAttribute="activatePayment" name="activatePayment">
                        <input type="hidden" name="username" value="ayomirotoye@gmail.com">
                        <input type="hidden" name="id">

                        <div class="text-center">
                            <button type="submit" name="submit" class="btn btn-secondary mb-3"> Yes</button>
                            <button type="button" name="close_dialog" class="btn btn-primary mb-3" data-dismiss="modal"> No</button>

                        </div>   
                    </form>
                </div>
            </div>
        </div>
    </div>    
    <div id="deleteModal" class="modal" tabindex="-1" role="dialog">
        <div class="modal-dialog" role="document">
            <div class="modal-content">
                <div class="modal-header border-0">
                    <h5>You are about to Delete this payment from Record?</h5>
                </div>
                <div class="modal-body">
                    <div id="del_display"></div>
                    <form class="reg" action="deletePayment" method="POST" modelAttribute="deletePayment" name="deletePayment">
                        <input type="hidden" name="username" value="ayomirotoye@gmail.com">
                        <input type="hidden" name="id">

                        <div class="text-center">
                            <button type="submit" name="submit" class="btn btn-secondary mb-3"> Yes</button>
                            <button type="button" name="close_dialog" class="btn btn-primary mb-3" data-dismiss="modal"> No</button>

                        </div>   
                    </form>
                </div>
            </div>
        </div>
    </div>
    <div id="suspendModal" class="modal" tabindex="-1" role="dialog">
        <div class="modal-dialog" role="document">
            <div class="modal-content">
                <div class="modal-header border-0">
                    <h5>You are about to Suspend this payment?</h5>
                </div>
                <div class="modal-body">
                    <div id="sus_display"></div>
                    <form class="reg" action="suspendPayment" method="POST" modelAttribute="suspendPayment" name="suspendPayment">
                        <input type="hidden" name="username" value="ayomirotoye@gmail.com">
                        <input type="hidden" name="id">

                        <div class="text-center">
                            <button type="submit" name="submit" class="btn btn-secondary mb-3"> Yes</button>
                            <button type="button" name="close_dialog" class="btn btn-primary mb-3" data-dismiss="modal"> No</button>

                        </div>   
                    </form>
                </div>
            </div>
        </div>
    </div>