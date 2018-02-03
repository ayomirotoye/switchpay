<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div class="card card-default">
    <div class="card-header">Buy Airtime</div>
    <div class="card-body">
        <div class="row">
            <div class="col-md-7">
                <form action="${pageContext.request.contextPath}/vend/1">                    
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
                        <label>Network</label>
                        <select class="form-control" name="network">
                            <option value="">Select an option</option>
                            <option value="mtn">MTN</option>
                            <option value="airtel">Airtel</option>
                            <option value="etisalat">Etisalat</option>
                            <option value="glo">Glo</option>
                        </select>
                    </div>
                    <div class="form-group">
                        <label>Phone number</label>
                        <input class="form-control" type="text" name="phone">
                    </div>
                    <div class="form-group">
                        <label>Amount</label>
                        <input class="form-control" type="text" name="amount">
                    </div>
                    <div class="form-group">
                        <input class="btn btn-primary btn-block" id="continuevend" type="submit" value="Continue">
                    </div>
                </form>
            </div>

            <div class="col-md-5">
                <img <img src="<c:url value="/resources/imgs/airtime.jpg"/>" alt="OSPoly Logo" style ="width: 100%; margin-bottom: 10px" />
            </div>
        </div>
    </div>
</div>