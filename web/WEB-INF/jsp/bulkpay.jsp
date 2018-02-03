<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@page session="true"%>


<div class="card card-default">
    <div class="card-header"><h4 class="text-left display-4">Recently Uploaded Files</h4>
    </div>
    <div class="card-body">
        <div class="row">
            <div class="col-md-4">
                
                <button class="btn btn-primary bkdelete" type="submit"> Delete File</button>
            </div>
            <div class="col-md-8">
                <form action="searchFiles" method="post">
                    <div class="input-group mb-3">
                        <div class="input-group-prepend">
                            <label class="input-group-text" for="inputGroupSelect01">Select by</label>
                        </div>
                        <select class="custom-select" id="search_by" name="search_by">
                            <option selected>Choose...</option>
                            <option value="file_name">file name</option>
                            <option value="date_uploaded">date uploaded</option>
                        </select>
                        <input type="text" class="form-control" placeholder="Enter search details" name="search_string" id="search_string">
                        <input type="hidden" name="table_name" value="tbl_filenames">
                        <div class="input-group-append">
                            <button class="btn btn-outline-secondary" id = "searchForFiles" type="submit">Search</button>
                        </div>
                    </div>

                </form>
            </div>
            <div class="col-md-12" style="margin-top: 10px;">

                ${pagination}

            </div>

        </div>
        <div class="table-responsive">
            <table class="table table-striped bulk_files">
                <thead>
                    <tr>
                        <th><input type="checkbox" class="masterCheck"></th>
                        <th>S/N</th>
                        <th>File</th>
                        <th>Date Uploaded</th>
                        <th>Uploaded By</th>
                            <th></th>
                    </tr>
                </thead>
                <tbody>
                    <c:choose>
                        <c:when test="${fn:length(filenames)>0}">
                            <c:forEach var="filename" items="${filenames}" varStatus="status">
                                <tr>
                                    <td>
                                        <input type="checkbox" class="bkbox">
                                        <input type="hidden" name="file_id" value="${filename.id}">
                                    </td>     
                                    <td>${status.count}</td> 
                                    <td>${filename.file_name}</td>
                                    <td>${filename.date_uploaded}</td>
                                    <td>${filename.uploaded_by}</td>
                                    <td>
                                        <a href="viewPayments/${filename.id}" class="btn btn-secondary link">View</a>
                                    </td>
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

                <script>
//            $(window).scroll(function (event) {
//                if ($(this).scrollTop() > 50 && !$('header').hasClass('bg-white')) {
//                    $('header').addClass('bg-white').addClass('text-dark');
//                } else if ($(this).scrollTop() < 50 && $('header').hasClass('bg-white')) {
//                    $('header').removeClass('bg-white').removeClass('text-dark');
//                }
//            });
    $(document).ready(function () {
        $(function () {
            $('.tab-all').click(function (event) {
                // event.preventDefault();
                // var th = $(this);
                // setTimeout(function(){
                //     th.removeClass('active');
                // }, 20);
            });
            $('[data-bind-link]').click(function () {
                var dataval = $(this).attr('data-bind-link');
                $(dataval).trigger('click');
            });
        });

        $('#view_file').click(function (event) {
            event.preventDefault();

        });
    });

            </script>

