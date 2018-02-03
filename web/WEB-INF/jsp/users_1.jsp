<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div class="card card-default">
    <div class="card-header">Users</div>
    <div class="card-body">
        <div class="" style="margin: 10px 0;">
            <button class="btn btn-primary" data-toggle="modal" data-target="#addNewUser">+ Add New Users</button>
            
            <button class="btn btn-danger">Delete</button> 
        </div>
        <table class="table table-striped">
            <tr>
                <th><input type="checkbox" name=""></th>
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
                <td><input type="checkbox" name=""></td>
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

<div id="addNewUser" class="modal fade" tabindex="-1" role="dialog" aria-labelledby="addNewUserLabel" aria-hidden="true">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <h4 class="modal-title">Modal title</h4>
            </div>
            <form action="${pageContext.request.contextPath}/adduser" method="">
                <div class="modal-body">
                    <div class="form-group">
                        <label>Username</label>
                        <input class="form-control" type="text" name="username" value="">
                    </div>
                    <div class="form-group">
                        <label>Password</label>
                        <input class="form-control" type="password" name="password" value="">
                    </div>
                    <div class="row">
                        <div class="form-group col-xs-6">
                            <label>Firstname</label>
                            <input class="form-control" type="text" name="firstname" value="">
                        </div>
                        <div class="form-group col-xs-6">
                            <label>Surname</label>
                            <input class="form-control" type="text" name="surname" value="">
                        </div>
                    </div>

                    <div class="form-group">
                        <label>Phone #</label>
                        <input class="form-control" type="text" name="phonenumber" value="">
                    </div>
                    <div class="form-group">
                        <label>Account Number</label>
                        <input class="form-control" type="text" name="accountnumber" value="">
                    </div>
                    <div class="form-group">
                        <label>Account Name</label>
                        <input class="form-control" type="text" name="accountname" value="">
                    </div>
                    <div class="row">
                        <div class="form-group col-xs-6">
                            <label>KYC Level</label>
                            <input class="form-control" type="text" name="kyclevel" value="">

                        </div>
                        <div class="form-group col-xs-6">
                            <label>BVN</label>
                            <input class="form-control" type="text" name="bvn" value="">
                        </div>
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default closeModal" data-dismiss="modal">Close</button>
                    <button class="btn btn-primary adduser" type="submit">Add New User</button>
                </div>
            </form>
        </div>
    </div>
</div>