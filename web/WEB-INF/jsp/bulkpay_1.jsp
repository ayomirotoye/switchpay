

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@page session="true"%>
        
            <div class="row">
                <div class= "text-center">
                    <p class="display-4 text-center">Recently Uploaded Files</p>
                </div>
                <div class="">
                    <table class="table table-hover">
                        <thead>
                            <tr>
                                <th>S/N</th>
                                <th>File</th>
                                <th>Date Uploaded</th>
                                <th>Uploaded By</th>
                                <th></th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:if test="${fn:length(filenames)>0}">
                                <c:forEach var="filename" items="${filenames}" varStatus="status">
                                    <tr>
                                        <td>${filename.id}</td> 
                                        <td>${filename.file_name}</td>
                                        <td>${filename.date_uploaded}</td>
                                        <td>${filename.uploaded_by}</td>
                                        <td>
                                            <form method="POST" action="viewPayments" name="payment_view" >
                                                <button class="btn btn-primary btn-secondary btn-sm" 
                                                        href="#"
                                                        data-toggle="modal" 
                                                        data-target="#paymentModal"
                                                        data-fileid="${filename.id}"
                                                        id="view_file" 
                                                        type="button">View</button>
                                            </form>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </c:if>
                        </tbody>

                    </table>
                </div>
            </div>
      
        <div id="paymentModal" class="modal" tabindex="-1" role="dialog">
            <div class="modal-dialog modal-lg" role="document">
                <div class="modal-content">
                    <div class="modal-header border-0">
                        <h5 class="modal-title text-secondary">Payment Record</h5>
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                            <span aria-hidden="true">&times;</span>
                        </button>
                    </div>
                    <div id="edit_result"></div>
                    <div class="modal-body">
                        <div id="show_payments"></div>
                    </div>
                </div>
            </div>
        </div>  

        <script>
//            $(window).scroll(function (event) {
//                if ($(this).scrollTop() > 50 && !$('header').hasClass('bg-white')) {
//                    $('header').addClass('bg-white').addClass('text-dark');
//                } else if ($(this).scrollTop() < 50 && $('header').hasClass('bg-white')) {
//                    $('header').removeClass('bg-white').removeClass('text-dark');
//                }
//            });
            $(document).ready(function () {
                var spinner = '<i class="fa fa-spinner fa-spin" aria-hidden="true" style="font-size:100px;"></i>';

                $(function () {
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
                });

//                $('[name=payment_view]').submit(function (e) {
//                    e.preventDefault();
//                    var form = $(this);
//                    var dataVal = form.serialize();
//                    console.log(dataVal);
//                    $.post("viewPayment", dataVal, function (result) {
//                        console.log(result);
//                        // form[0].reset();
//                        // console.log(mhval);
//                        if (result === 'Successful') {
//                            // myDisplay.text('Successful');
//                            //modalhead.html('<h2 class="text-center">' + result + '</h2>');
//                            $('#del_display').html('<div class="alert alert-success mx-auto">' + result + '</div>');
//                            form[0].reset();
//                            setTimeout(function () {
//
//                            }, 2000);
//                            window.location.reload(true);
//                        } else {
//
//                            $('#del_display').html('<div class="alert alert-danger mx-auto">' + result + '</div>');
//                            form.show();
//                            setTimeout(function () {
//                                $('#del_display').html("");
//                            }, 2000);
//                        }
//                        // $('#display').remove();
//                    });
//                });
                $('#paymentModal').on('show.bs.modal', function (event) {
                    button = $(event.relatedTarget);
                    var fileid = button.data('fileid');
                    console.log(fileid);
                    
                            $.post("viewPayments", {"fileid": fileid}, function (result) {
                                console.log(result);
                                $('#show_payments').html(result);

                            });
                });
            });

        </script>
        <!--<script src="<spring:url value="/resources/js/dropzone.js"/>"></script>--> 
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
                    this.on("addedfile", function () {
                        // Show submit button here and/or inform user to click it.

                    });

                },
                complete: function () {
                    $('#drop_display').html('<div class="alert alert-success text-center">' + 'Successful' + '</div>');

                    setTimeout(function () {
                    }, 7000);
                    window.location = 'bulkpay';
                }

            };
        </script>
