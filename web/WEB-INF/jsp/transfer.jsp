<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div class="card card-default">
    <div class="card-header">Transfer</div>
    <div class="card-body">
        <div class="row">
            <div class="col-md-7">
                <c:choose>
                    <c:when test="${alertMessage != null}">
                        <div id="pay-alert" class='alert alert-danger'>${alertMessage}</div>
                    </c:when>
                </c:choose>
                <form action="${pageContext.request.contextPath}/transfer/1">
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
                        <input class="form-control" type="text" name="acctNumber">
                    </div>

                    <div class="form-group">
                        <label>Beneficiary Bank Number</label>
                        <c:set var="finInst" value="${financialInstitutions}" />
                        <select class="form-control" name="finInst" required>
                            <option value="">Select Beneficiary Bank</option>
                            <c:forEach items="${finInst}" var = "finInsts">
                                <option value="${finInsts.code}#${finInsts.name}">${finInsts.name}</option>
                             </c:forEach>
                        </select>
                    </div>

                    <div class="form-group">
                        <label>Purpose</label>
                        <input class="form-control" type="text" name="purpose">
                    </div>

                    <div class="form-group">
                        <label>Amount</label>
                        <input class="form-control" type="text" name="amount">
                    </div>

                    <div class="form-group">
                        <input class="btn btn-primary btn-block" id="continuetransfer" type="submit" value="Continue">
                    </div>
                </form>
            </div>
            <div class="col-md-5">
                <img src="<c:url value="/resources/imgs/transfer_logo.jpg"/>" alt="OSPoly Logo" style ="width: 100%; margin-bottom: 10px" />
            </div>
        </div>
    </div>
</div>