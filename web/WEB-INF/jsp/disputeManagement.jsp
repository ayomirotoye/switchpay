<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/functions" prefix = "fn" %>

<div class="card card-default">
    <div class="card-header">
        <h4 class="text-left display-4">Disputes</h4>
    </div>
    <div class="card-body">
        <div class="row">
            <div class="col-md-4">
                <button class="btn btn-primary" data-toggle="modal" data-target="#exampleModal">Log Dispute</button>
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
                            <button class="btn btn-outline-secondary" id = "searchForSettlements" type="submit">Search</button>
                        </div>
                    </div>

                </form>
            </div>
            <div class="col-md-12" style="margin-top: 10px;">

                ${pagination}

            </div>

        </div>
        <div class="table-responsive">
            <table class="table table-hover">
                <thead>
                    <tr>
                        <th>Dispute Id</th>
                        <th>Session Id</th>
                        <th>Logged Against</th>
                        <th>Reason</th>
                        <th>Logged By</th>
                        <th>Date logged</th>
                        <th>Type</th>
                        <th>Status</th>

                    </tr>
                </thead>
                <tbody>
                    <c:set var="bankcode" value="${bankcode}"></c:set>
                    <c:choose>
                        <c:when test="${fn:length(disputes)>0}">
                            <c:forEach var="myDisputes" items="${disputes}" varStatus="status">

                                <c:choose>
                                    <c:when test="${myDisputes.logged_by eq bankcode}">
                                        <tr class="table-success" data-toggle="modal" data-target="#moreInfo">
                                            <th scope="row">${status.count}</th>
                                            <th>${myDisputes.session_id}</th>
                                            <td>${myDisputes.logged_against}</td> 
                                            <td>${myDisputes.description}</td> 
                                            <td>${myDisputes.logged_by}</td> 
                                            <td>${myDisputes.date_logged}</td> 
                                            <td class="font-weight-bold">Outgoing</td> 

                                        </tr>
                                    </c:when>
                                    <c:otherwise>
                                        <tr class="table-danger" data-toggle="modal" data-target="#moreInfo">
                                            <th scope="row">${status.count}</th>
                                            <th>${myDisputes.session_id}</th>
                                            <td>${myDisputes.logged_against}</td>
                                            <td>${myDisputes.description}</td> 
                                            <td>${myDisputes.logged_by}</td> 
                                            <td>${myDisputes.date_logged}</td> 
                                            <td class="font-weight-bold">Incoming</td>
                                           
                                            <td><c:choose>
                                                    <c:when test="${myDisputes.status=='0'}">
                                                        <button type="button" class="btn btn-danger btn-sm stop-btn" data-toggle="modal" data-target="#updateModal" 
                                           data-session_id="${myDisputes.session_id}" 
                                           data-description="${myDisputes.description}"
                                           data-logged_by="${myDisputes.logged_by}"
                                           data-date_logged="${myDisputes.date_logged}" data-id="${myDisputes.id}" data-target="#stopModal" data-status="start">Accept</button> 
                                                        <br />
                                                    </c:when>    
                                                    <c:otherwise>
                                                        <button type="button" class="btn btn-primary btn-sm stop-btn" data-toggle="modal" data-id="${myDisputes.id}" data-target="#startModal" data-status="stop">Accepted</button> 
                                                        <br />
                                                    </c:otherwise>
                                                </c:choose></td>
                                    </c:otherwise>
                                </c:choose>

                            </c:forEach>  
                        </c:when>
                        <c:otherwise>
                        <div class="alert alert-info display-4 text-center">No Outgoing dispute logged yet</div>
                    </c:otherwise>
                </c:choose>

                </tbody>
            </table>
        </div>
    </div>
  
    <div id="updateModal" class="modal" tabindex="-1" role="dialog">
        <div class="modal-dialog" role="document">
            <div class="modal-content">
                <div class="modal-header border-0">
                    <h5>Accept this dispute?</h5>
                </div>
                <div class="modal-body">
                    <div id="update_display"></div>
                    <form class="reg" action="updateDispute" method="POST" modelAttribute="dispute_update" name="updateDispute">
                        <input type="hidden" name="session_id">
                        <div class="form-group">
                            <label>Reason</label>
                            <input type="text" id="description" name="description" class="form-control" disabled>
                        </div>
                        <div class="text-center">
                            <button type="submit" id="submit_dispute" class="btn btn-secondary mb-3"> Yes</button>
                            <button type="button" name="close_dialog" class="btn btn-primary mb-3" data-dismiss="modal"> No</button>

                        </div>   
                    </form>
                </div>
            </div>
        </div>

    </div>
    <!-- Modal -->
    <div class="modal fade" id="exampleModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
        <div class="modal-dialog" role="document">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="exampleModalLabel">Modal title</h5>
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                        <span aria-hidden="true">&times;</span>
                    </button>
                </div>
                <div id="log_result"></div>
                <form action="/logDispute" method="POST">
                    <div class="modal-body">
                        <div class="form-group">
                            <label>Session Id</label>
                            <input type="text" id="session_id" name="session_id" maxlength="30" class="form-control">
                        </div>
                        <div class="form-group">
                            <label>Reason</label>
                            <input type="text" id="description" name="description" maxlength="100" class="form-control">
                        </div>
                        <input type="hidden" name="logged_by" id="logged_by" value="${bankcode}">
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-danger" data-dismiss="modal">Close</button>
                        <button type="button" class="btn btn-primary" id="subDispute">Log Dispute</button>
                    </div>
                </form>
                <span id="display"></span>
            </div>
        </div>
    </div>
    <script type="text/javascript">
        $(document).ready(function () {
            $('#subDispute').click(function (e) {
                e.preventDefault();
                var th = $(this);
                var form = th.closest('form');
                form.hide('fast');
                var dataVal = form.serialize();
                $.post("logDispute", dataVal, function (result) {
                    console.log(result);
                    if (result === 'Successful') {
                        $('#log_result').html('<div class="alert alert-success mx-auto">' + result + '</div>');
                        form[0].reset();
                        setTimeout(function () {

                        }, 2000);
                        window.location.reload(true);
                    } else {

                        $('#log_result').html('<div class="alert alert-danger mx-auto">' + result + '</div>');
                        form.show();
                        setTimeout(function () {

                        }, 2000);
                    }
                    // $('#display').remove();
                });

            });

            $('#submit_dispute').click(function (e) {
                e.preventDefault();
                var th = $(this);
                var form = th.closest('form');
                form.hide('fast');
                var dataVal = form.serialize();
                console.log(dataVal);
                $.post("updateDispute", dataVal, function (result) {
                    console.log(result);
                    if (result === 'Successful') {
                        $('#update_display').html('<div class="alert alert-success mx-auto">' + result + '</div>');
                        form[0].reset();
                        setTimeout(function () {

                        }, 2000);
                        window.location.reload(true);
                    } else {

                        $('#update_display').html('<div class="alert alert-danger mx-auto">' + result + '</div>');
                        form.show();
                        setTimeout(function () {

                        }, 2000);
                    }
                    // $('#display').remove();
                });

            });
            $('#updateModal').on('show.bs.modal', function (event) {
                button = $(event.relatedTarget);
                var session_id = button.data('session_id');
                var description = button.data('description');
                var modal = $(this);
                modal.find('[name=session_id]').val(session_id);
                modal.find('[name=description]').val(description);
            });
        });

    </script>
</div>