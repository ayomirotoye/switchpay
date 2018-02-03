<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> 

<div class="card card-default">
    <div class="card-header">${whichPay}</div>
    <div class="card-body">

        <div class="row">
            <div class="col-md-7">
                <c:set var="checkVal" value=" "/>
                <c:choose>
                    <c:when test="${alertMessage != null}">
                        <div id="pay-alert" class='alert alert-danger'>${alertMessage}</div>
                    </c:when>
                </c:choose>

                <form action="${pageContext.request.contextPath}/pay/1">
                    <div class="form-group">
                        <label>Source Account Number</label>
                        <c:set var="userAccts" value="${userAccounts}" />
                        <select class="form-control" name="srcAccount" required>
                            <option value="">Select Source Account</option>
                            <c:forEach items="${userAccts}" var = "accts">
                                <option value="${accts.accountNumber}#${accts.accountName}">${accts.accountName} ${accts.accountNumber} ${accts.balance}</option>
                             </c:forEach>
                        </select>
                    </div>
                    <div class="form-group">
                        <label>Beneficiary Account Number</label>
                        <input class="form-control" type="text" name="acctNumber" required>
                    </div>

                    <div class="form-group">
                        <label>Amount</label>
                        <input class="form-control" type="text" name="amount" required>
                    </div>
                    <div class="form-group">
                        <input class="btn btn-primary btn-block" id="continuepay" type="submit" value="Continue">
                    </div>
                </form>
            </div>
            <div class="col-md-5">
                <img <img src="<c:url value="/resources/imgs/pay_logo.jpg"/>" alt="OSPoly Logo" style ="width: 100%; margin-bottom: 10px" />
            </div>
        </div>
    </div>
</div>