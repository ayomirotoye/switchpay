<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div class="row">
    <div class="col-md-12">
        <h4 class="text-left display-4">Dashboard</h4>
    </div>
</div>
<section>
    <div class="row">
        <div class="col-md-12">
            <div class="card mb-3">
                <div class="card-header">Live Transactions</div>
                <div class="card-body" style="overflow-x: auto;">
                    <div class="table-responsive">
                        <table class="table table-striped">
                            <thead class="sticky-top">
                                <tr>
                                    <th>Session Id</th>
                                    <th>From Bank</th>
                                    <th>To Bank</th>
                                    <th>Amount</th>
                                    <th>Transaction Date</th>
                                    <th>Status</th>
                                </tr>
                            </thead>
                            <div class="col-md-12 text-center" style="margin-top: 10px;">

                                ${pagination}

                            </div>
                            <tbody>
                                <c:forEach var="report" items="${transactions}" varStatus="status">
                                    <c:choose>
                                        <c:when test="${report.status eq '00'}">
                                            <tr class="table-success" data-toggle="modal" data-target="#moreInfo">
                                                <th scope="row">${report.sessionID}</th>
                                                <td>${report.fromBank}</td>
                                                <td>${report.toBank}</td>
                                                <td>${report.amount}</td>
                                                <td>${report.transactionDate}</td>
                                                <td>${report.status }</td>
                                            </tr>
                                        </c:when>
                                        <c:otherwise>
                                            <tr class="table-danger" data-toggle="modal" data-target="#moreInfo">
                                                <th scope="row">${report.sessionID}</th> 
                                                <td>${report.fromBank}</td>
                                                <td>${report.toBank}</td>
                                                <td>${report.amount}</td>
                                                <td>${report.transactionDate}</td>
                                                <td>${report.status }</td>
                                            </tr>
                                        </c:otherwise>
                                    </c:choose>
                                </c:forEach>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </div>
                                <div class="col-md-12 text-center" style="margin-top: 10px;">

                                    ${pagination}

                                </div>
    </div>
</section>
<div class="modal fade" id="moreInfo" tabindex="-1" role="dialog" aria-labelledby="imformationModal" aria-hidden="true">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="imformationModal">More Info</h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="modal-body">
                <div>
                    <h3 class="display-4">Mark Huston</h3>
                    <p>Session ID: 1001</p>

                </div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-outline-primary" data-dismiss="modal">Close</button>
            </div>
        </div>
    </div>
</div>