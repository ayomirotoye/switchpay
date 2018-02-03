<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div class="card card-default">
    <div class="card-header">
        <h3 class = "card-title">Buy Airtime</h3>
    </div>

    <div class="card-body">
        <div class="row">
            <div class="col-md-6">
                <div id="vend-alert"></div>

                <div style="padding:10px 0;">
                    <dt>Source Account Name</dt>
                    <dd>${srcAccountName}</dd>
                </div>
                <div style="padding:10px 0;">
                    <dt>Source Account Number</dt>
                    <dd>${srcAccountNumber}</dd>
                </div>
                <div style="padding:10px 0;">
                    <dt>Network</dt>
                    <dd>${network}</dd>
                </div>
                <div style="padding:10px 0;">
                    <dt>Phone number</dt>
                    <dd>${phone}</dd>
                </div>

                <div style="padding:10px 0;">
                    <dt>Amount:</dt>
                    <dd>${formattedAmount}</dd>
                </div>
                <form action="${pageContext.request.contextPath}/vend/0">
                    <div class="form-group">
                        <label>Please enter Token </label>
                        <input class="form-control" type="password" name="token" value="">
                    </div>

                    <div class="form-group">
                        <input class="btn btn-danger cancelvend" data-bindLink="#vend" type="button" value="Cancel">
                        <input class="btn btn-success completevend" type="submit" value="Complete Airtime Purchase">
                    </div>

                    <input type="hidden" value="${srcAccountName}" name="srcAccountName">
                    <input type="hidden" value="${srcAccountNumber}" name="srcAccountNumber">
                    <input type="hidden" value="${network}" name="network">
                    <input type="hidden" value="${phone}" name="phone">
                    <input type="hidden" value="${amount}" name="amount">
                </form>
            </div>
            <div class="col-md-6">
                <img src="<c:url value="/resources/imgs/pay_logo.jpg"/>" alt="OSPoly Logo" style ="width: 100%; margin-bottom: 10px" />
            </div>
        </div>
    </div>
</div>