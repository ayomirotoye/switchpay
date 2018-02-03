<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div class="card card-default">
    <div class="card-header">Mini-Statement</div>

    <div class="card-body">
        <table class="table table-striped">
            <tr>
                <th>Amount(N)</th>
                <th>Source Name</th>
                <th>Source Account Number</th>
                <th>Beneficiary</th>
                <th>Withdrawal or Deposit</th>
                <th>Status</th>
                <th>Date and Time</th>
            </tr>
            <c:set var="stmt" value="${ministatement}" />
            <c:forEach items="${ministatement}" var = "stmt">
                <tr>
                    <td>${stmt.amount}</td>
                    <td>${stmt.source}</td>
                    <td>${stmt.fromAccount}</td>
                    <td>${stmt.beneficiary}</td>
                    <td>${stmt.creditOrDebit}</td>
                    <td>${stmt.transactionStatus}</td>
                    <td>${stmt.transactionDateAndTime}</td>
                </tr>
             </c:forEach>
        </table>
    </div>
</div>