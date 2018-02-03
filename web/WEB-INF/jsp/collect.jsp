<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div class="card card-default">
    <div class="card-header">Collect</div>
    <div class="card-body">
        <div class="row">
            <div class="col-md-7">
                <form action="">
                    <div class="form-group">
                        <label>Source Account Number</label>
                        <c:set var="userAccts" value="${userAccounts}" />
                        <select class="form-control" name="" required>
                            <option value="">Select Source Account</option>
                            <c:forEach items="${userAccts}" var = "accts">
                                <option value="${accts.accountNumber}">${accts.accountName} ${accts.accountNumber} Balance</option>
                             </c:forEach>
                        </select>
                    </div>
                    <div class="form-group">
                        <label>Beneficiary Account Number</label>
                        <input class="form-control" type="text" name="acctNumber">
                    </div>

                    <div class="form-group">
                        <label>Amount</label>
                        <input class="form-control" type="text" name="amount">
                    </div>
                    <div class="form-group">
                        <input class="btn btn-primary btn-block" type="submit" value="Continue">
                    </div>
                </form>
            </div>
            <div class="col-md-5">
                <img src="<c:url value="/resources/imgs/collect_img.jpg"/>" alt="OSPoly Logo" style ="width: 100%; margin-bottom: 10px" />
            </div>
        </div>
    </div>
</div>