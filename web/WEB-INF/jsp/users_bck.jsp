<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div class="card card-default">
    <div class="card-header">Users</div>
    <div class="card-body">
        <table class="table table-striped">
            <tr>
                <th>Username</th>
                <th>First Name</th>
                <th>Surname</th>
                <th>Email</th>
                <th>Phone Number</th>
                <th>Status</th>
                <th>Date of Creation</th>
                <th>Date of Last Login</th>
            </tr>
            <c:set var="userAllProfiles" value="${userAllProfiles}" />
            <c:forEach items="${userAllProfiles}" var = "profiles">
                <tr>
                <td>${profiles.username}</td>
                <td>${profiles.firstName}</td>
                <td>${profiles.surname}</td>
                <td>${profiles.email}</td>
                <td>${profiles.phoneNumber}</td>
                <td>${profiles.status}</td>
                <td>${profiles.creationdate}</td>
                <td>${profiles.lastlogindate}</td>
                </tr>
             </c:forEach>
        </table>
    </div>
</div>