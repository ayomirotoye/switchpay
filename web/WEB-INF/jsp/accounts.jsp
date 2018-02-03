<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div class="card card-default">
    <div class="card-header">Accounts</div>
    <div class="card-body">
        <div class="" style="margin: 10px 0;">
            <button class="btn btn-primary">+ Add New Account</button>
            <button class="btn btn-danger">Delete</button> 
        </div>

        <table class="table table-striped">
            <tr>
                <th><input type="checkbox" name=""></th>
                <th>id</th>
                <th>Username</th>
                <th>Account Number</th>
                <th>Account Name</th>
                <th>KYC Level</th>
                <th>BVN</th>
            </tr>

            <c:set var="userAllAccounts" value="${userAllAccounts}" />
            <c:forEach items="${userAllAccounts}" var = "accounts">
                <tr>
                    <td><input type="checkbox" name=""></td>
                    <td>${accounts.id}</td>
                    <td>${accounts.username}</td>
                    <td>${accounts.accountNumber}</td>
                    <td>${accounts.accountName}</td>
                    <td>${accounts.kycLevel}</td>
                    <td>${accounts.bvn}</td>
                </tr>
             </c:forEach>
        </table>

        <div>
            <ul class="pagination">
              <li><a href="#">1</a></li>
              <li><a href="#">2</a></li>
              <li><a href="#">3</a></li>
              <li><a href="#">4</a></li>
              <li><a href="#">5</a></li>
            </ul>
        </div>
    </div>
</div>