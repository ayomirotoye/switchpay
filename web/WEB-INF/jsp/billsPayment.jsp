<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<div class="card card-default">
    <div class="card-header">
    	<h4 class="text-left display-4">
    		Bills 
    	</h4>
    </div>
    <div class="card-body">
    	<div class="row">
    		<div class="col-md-4">
    			<button class=" btn btn-primary" data-toggle="modal" data-target="addNewBiller">+Add Billers</button>
    			<button class="btn btn-danger deleteBillers">Delete Biller</button>
    		</div>
    		<div class="col-md-8 mx-auto">
    		    <form action="searchBillers" method="post">
    		        <div class="input-group mb-3">
    		            <div class="input-group-prepend">
    		                <label class="input-group-text" for="inputGroupSelect01">Search by</label>
    		            </div>
    		            <select class="custom-select" id="search_by" name="search_by">
    		                <option selected>Choose...</option>
    		                <option value="username">Biller's Name</option>
    		            </select>
    		            <input type="text" class="form-control" placeholder="Enter search details" name="search_string" id="search_string">
    		            <input type="hidden" name="table_name" value="tbl_user_profile">
    		            <div class="input-group-append">
    		                <button class="btn btn-outline-secondary" id = "searchForBillers" type="submit">Search</button>
    		            </div>
    		        </div>

    		    </form>
    		</div>
    	</div>
    	<div class="col-md-12" style="margin-top: 10px;">

    	    ${pagination}

    	</div>    
       
		<div class="row">
			<div class="col-md-12 mx-auto screen-1">
				<h3 class="text-center text-muted">Select Biller</h3>
				<div class="row">
	                            <c:forEach var="biller" items="${billers}" varStatus="status">
	                            <div class="col-md-2">
	                            	<div class="card" style="max-width: 18rem;" !important">
	                            		ayomide
	                            	</div>
	                            </div>
	                                <!-- <div class="col-md-2">
	                                   <a href="${biller.biller_name}" class="screen-1-billers">
							<div class="card mb-3">
								<div class="card-body" style="background-image:url(<spring:url value="/resources/imgs/9mobile.png"/>)">
									
									<!-- <p class="text-white text-center mb-0">${biller.biller_name}</p> 
								</div>
							</div>
						</a> 
	                                </div> -->
	                            </c:forEach>
				
	                        </div>
				<!-- <div class="text-right"><button class="btn btn-primary screen-1-billers">Next</button></div> -->
			</div>
			<div class="col-md-6 mx-auto screen-2" style="display: none;">
				<form action="payBill" method="post" name="form_bill" modelAttribute="billForm">
	                            <input type="hidden" name="username" value="${username}">
					<div class="form-group">
						<label>Biller</label>
						<input class="form-control biller-name" type="text" name="${biller.biller_name}" data-bind=".bill_er">
					</div>
					<div class="form-group">
						<label>Amount</label>
						<input class="form-control" type="number" name="amount" min="100" pattern="[0-9]+" data-bind=".bill_amount">
						<small class="text-muted">e.g 10,000.00, 10000.00, 10000</small>
					</div>
					<div class="form-group">
						<label>Biller info</label>
						<input class="form-control" type="text" name="billerinfo" data-bind=".bill_info">
					</div>
					<div class="form-group">
						<label>Phone number</label>
						<input class="form-control" type="tel" name="phone" data-bind=".bill_phone">
					</div>
					<div class="form-group text-right">
						<button type="button" class="btn btn-primary bills_back">Back</button>
						<button type="button" class="btn btn-primary pay_bill">Next</button>
					</div>							
				</form>
			</div>
			<div class="col-md-6 mx-auto screen-3" style="display: none;">
				<h2 class="text-muted">Payment Gateway here</h2> 
				<button type="button" class="btn btn-secondary btn-block">Buy</button>
				<p class="text-center py-3"><a class="airtime_payment_back" href="#">Back</a></p>
			</div>
		</div>
	</div>

	<div class="modal fade" id="payBillsModal" tabindex="-1" role="dialog" aria-labelledby="modalLabel" aria-hidden="true">
		<div class="modal-dialog" role="document">
			<div class="modal-content">
				<div class="modal-header">
					<h5 class="modal-title text-primary" id="modalLabel">Confirm Payment</h5>
					<button type="button" class="close" data-dismiss="modal" aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
				</div>
				<div class="modal-body">
	                            <p class="text-muted"><b>Biller</b></p>
					<p class="bill_er">...</p>
					<p class="text-muted"><b>Amount</b></p>
					<p class="bill_amount">...</p>
					<p class="text-muted"><b>Biller Info</b></p>
					<p class="bill_info">...</p>
					<p class="text-muted"><b>Senders's Phone Number</b></p>
					<p class="bill_phone">...</p>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-outline-dark" data-dismiss="modal">NO</button>
					<button type="button" class="btn btn-secondary btn-screen-3" data-dismiss="modal">Yes</button>
				</div>
			</div>
		</div>
	</div>




<script>
	// FORM VALIDATOR ENGINE
	var validate = new FormValidator('form_bill', [{
		name: 'biller',
		display: 'Biller',
		rules: 'required'
	}, {
		name: 'amount',
		display: 'Amount',
		rules: 'required|is_natural_no_zero'
	}, {
		name: 'billerinfo',
		display: 'Biller info',
		rules: 'required'
	}, {
		name: 'phone',
		display: 'Phone Number',
		rules: 'required|numeric'
	}], function(errors, event) {
			event.preventDefault();
			console.log(errors);
			if (errors.length > 0) {
			//Form validation Failled
			// Do nothing
			var form = $('[name=form_bill]');
			form.find('.is-invalid').removeClass('is-invalid');
			form.find('.errors').remove();
			$(errors[0].element).addClass('is-invalid').after('<small class="errors text-danger">'+errors[0].message+'</small>');
			$(errors[0].element)[0].focus();
			console.log('error');
		}else{
			//Form validation Passed
			var form = $('[name=form_bill]');
			form.find('.is-invalid').removeClass('is-invalid');
			form.find('.errors').remove();
			console.log('worked');
			$('#payBillsModal').modal('toggle');
		}
	});
$(function(){
$('.pay_bill').click(function(event) {
		event.preventDefault();
		//FINDS THE FORM AND SUBMIT IT 
		$(this).closest('form').submit();
		// $(this).closest('form').find('.submit_af').submit();
	});

	$('.screen-1-billers').click(function(event) {
		event.preventDefault();
		var biller = $(this).attr('href');
		console.log(biller);
		$('.biller-name').val(biller).trigger('change');
		$('.screen-1').hide();
		$('.screen-2').show();
	});
	$('.btn-screen-3').click(function(event) {
		$('.screen-2').hide();
                // Sending to payBill
                var url = "payBill";
                var data = $('[name=form_bill]').serialize();
                $.post(url, data, function(result){
                    console.log(result);
                });
		$('.screen-3').show();
	});
	$('.bills_back').click(function(event){
		var btnParent = $(this).closest('.col-md-6');
		btnParent.parent().children().hide();
		console.log(btnParent.parent().first())
		btnParent.parent().children().first().show();
	})

});
</script>
