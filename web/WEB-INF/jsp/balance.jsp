<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div class="card card-default">
    <div class="card-header">Account Balances</div>

    <div class="card-body">
        <c:choose>
            <c:when test="${alertMessage != null}">
                <div id="pay-alert" class='alert alert-danger'>${alertMessage}</div>
            </c:when>
        </c:choose>
        <table class="table table-striped">
            <tr>
                <th>Account Number</th>
                <th>Account Name</th>
                <th>KYC level</th>
                <th>BVN</th>
                <th>Balance</th>
            </tr>
            <c:set var="userAccts" value="${userAccounts}" />
            <c:forEach items="${userAccts}" var = "accts">
                <tr>
                    <td>${accts.accountNumber}</td>
                    <td>${accts.accountName}</td>
                    <td>${accts.kycLevel}</td>
                    <td>${accts.bvn}</td>
                    <td>${accts.balance}</td>
                </tr>
             </c:forEach>
        </table>
    </div>
</div>