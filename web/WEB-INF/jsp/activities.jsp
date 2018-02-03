<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div class="card card-default">
    <div class="card-header">Activities</div>
    <div class="card-body">
        <div class="table-responsive">
            <table class="table table-striped userProfiles">
                <tr>
                    <th>Username</th>
                    <th>action</th>
                    <th>action date</th>
                </tr>
                <c:set var="userAllProfiles" value="${userAllProfiles}" />
                <c:forEach items="${userAllProfiles}" var = "profiles">
                <tr>
                    <td>${profiles.username}</td>
                    <td>${profiles.action}</td>
                    <td>${profiles.date}</td>
                </tr>
                 </c:forEach>
            </table>
        </div>
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
