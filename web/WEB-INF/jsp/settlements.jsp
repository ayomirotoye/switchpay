<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>



<div class="card card-success">
    <div class="card-header">
        <h4 class="text-left display-4">Settlement</h4>
    </div>
    <div class="card-body">
        <div class="row">
            <div class="col-md-4">
                <form action="downloadZipped" method="get" id="files_form">
                    <button class="btn btn-primary mb-3">Download Report</button>
                    <input type="hidden" name="filename" class="clicked_file">
                </form>
                
            </div>
            <div class="col-md-8">
                <form action="searchSettlements" method="post">
                    <div class="input-group mb-3">
                        <div class="input-group-prepend">
                            <label class="input-group-text" for="inputGroupSelect01">Search by</label>
                        </div>
                        <select class="custom-select" id="search_by" name="search_by">
                            <option value="">Choose...</option>
                            <option value="filename">filename</option>
                            <option value="total_amount_received">total amount received</option>
                            <option value="total_amount_sent">total amount sent</option>
                            <option value="settlement_date">settlement date</option>
                        </select>
                        <input type="text" class="form-control" placeholder="Enter search details" name="search_string" id="search_string">
                        <input type="hidden" name="table_name" value="tbl_settlements">
                        <div class="input-group-append">
                            <button class="btn btn-outline-secondary search_btn" id = "searchForSettlements" type="submit">Search</button>
                        </div>
                    </div>

                </form>
            </div>
            <div class="col-md-12" style="margin-top: 10px;">

                ${pagination}

            </div>
        </div>
        <div class="table-responsive">
            <table class="table table-hover settlements_reports">
                <thead>
                    <tr>
<!--                        <th><input type="radio" name="masterCheck" class="masterCheck"></th>-->
                        <th></th>
                        <th>#</th>
                        <th>Filename</th>
                        <th>Total amount received</th>
                        <th>Total amount sent</th>
                        <th>Settlement date</th>
                        <th></th>

                    </tr>
                </thead>
                <tbody>
                    <c:choose>
                        <c:when test="${fn:length(allSettlements)>0}">
                            <c:forEach var="settlements" items="${allSettlements}" varStatus="status">
                                <tr>
                                    <td>
                                        <input type="radio" name="reportBox" class="reportBox">
                                        <input type="hidden" name="filename" value="${settlements.location}">
                                    </td>     
                                    <td>${status.count}</td> 
                                    <td>${settlements.filename}</td>
                                    <td>${settlements.total_amount_received}</td>
                                    <td>${settlements.total_amount_sent}</td>
                                    <td>${settlements.settlement_date}</td>
                                </tr>
                            </c:forEach>
                        </c:when>
                        <c:otherwise>
                         ${message}
                        </c:otherwise>
                    </c:choose>
                </tbody>
            </table>
        </div>
    </div>
</div>
<div class="col-md-12 text-center" style="margin-top: 10px;">

    ${pagination}

</div>

       