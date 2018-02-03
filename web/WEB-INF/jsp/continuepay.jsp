<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div class="card card-default">
    <div class="card-header">
        <h3 class = "card-title">${whichPay}</h3>
    </div>

    <div class="card-body">
        <div class="row">
            <div class="col-md-6">
                <div id="pay-alert"></div>

                <div style="padding:10px 0;">
                    <dt>Source Account Name</dt>
                    <dd>${srcAccountName}</dd>
                </div>
                <div style="padding:10px 0;">
                    <dt>Source Account Number</dt>
                    <dd>${srcAccountNumber}</dd>
                </div>
                <div style="padding:10px 0;">
                    <dt>Beneficiary Account Name</dt>
                    <dd>${accountName}</dd>
                </div>
                <div style="padding:10px 0;">
                    <dt>Beneficiary Account Number:</dt>
                    <dd>${acctNumber}</dd>
                </div>

                <div style="padding:10px 0;">
                    <dt>Amount:</dt>
                    <dd>${formattedAmount}</dd>
                </div>
                <form action="${pageContext.request.contextPath}/pay/0">
                    <div class="form-group">
                        <label>Please enter Token </label>
                        <input class="form-control" type="password" name="token" value="">
                    </div>
                    
                    <div class="form-group">
                        <input class="btn btn-danger cancelpay" data-bindLink="#pay" type="button" value="Cancel">
<!--                    </div>

                    <div class="form-group">-->
                        <input class="btn btn-success completepay" type="submit" value="Complete Payment">
                    </div>

                    <input type="hidden" value="${srcAccountName}" name="srcAccountName">
                    <input type="hidden" value="${srcAccountNumber}" name="srcAccountNumber">
                    <input type="hidden" value="${accountName}" name="accountName">
                    <input type="hidden" value="${acctNumber}" name="acctNumber">
                    <input type="hidden" value="${bvn}" name="bvn">
                    <input type="hidden" value="${kycLevel}" name="kycLevel">
                    <input type="hidden" value="${amount}" name="amount">
                </form>
            </div>
            <div class="col-md-6">
                <img src="<c:url value="/resources/imgs/pay_logo.jpg"/>" alt="OSPoly Logo" style ="width: 100%; margin-bottom: 10px" />
            </div>
        </div>
    </div>
</div>