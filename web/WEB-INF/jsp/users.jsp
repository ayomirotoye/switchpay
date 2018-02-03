
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<div class="card card-default">
    <div class="card-header"> <h4 class="text-left display-4">Users</h4></div>
    <div class="card-body">
        <div class="row">
            <div class="col-md-4">
                <button class="btn btn-primary" data-toggle="modal" data-target="#addNewUser">+ Add New Users</button>
                <button class="btn btn-danger deleteUsers">Delete</button>
            </div>
            <div class="col-md-8">
                <form action="searchUsers" method="post">
                    <div class="input-group mb-3">
                        <div class="input-group-prepend">
                            <label class="input-group-text" for="inputGroupSelect01">Search by</label>
                        </div>
                        <select class="custom-select" id="search_by" name="search_by">
                            <option selected>Choose...</option>
                            <option value="username">username</option>
                            <option value="firstname">firstname</option>
                            <option value="surname">surname</option>
                            <option value="phoneNumber">phone</option>
                            <option value="creationdate">date of creation</option>
                            <option value="lastlogindate">date of last login</option>
                        </select>
                        <input type="text" class="form-control" placeholder="Enter search details" name="search_string" id="search_string">
                        <input type="hidden" name="table_name" value="tbl_user_profile">
                        <div class="input-group-append">
                            <button class="btn btn-outline-secondary" id = "searchForUsers" type="submit">Search</button>
                        </div>
                    </div>

                </form>
            </div>
            <div class="col-md-12" style="margin-top: 10px;">

                ${pagination}

            </div>
        </div>

        <div class="table-responsive">
            <table class="table table-striped userProfiles">
                <tr>
                    <th><input type="checkbox" class="masterCheck"></th>
                    <th>Username</th>
                    <th>First Name</th>
                    <th>Surname</th>
                    <th>Email</th>
                    <th>Phone Number</th>
                    <th>Status</th>
                    <th>Date of Creation</th>
                    <th>Date of Last Login</th>
                    <th>!</th>
                </tr>
                
                <c:choose>
                    <c:when test="${fn:length(userAllProfiles)>0}">
                        <c:set var="customerAccounts" value="${allUserAccounts}"></c:set>
                        <c:forEach items="${userAllProfiles}" var = "profiles">
                            <tr>
                                <td>
                                    <input type="checkbox" class="userBox">
                                    <input type="hidden" value="${profiles.username}"/>
                                </td>
                                <td>${profiles.username}</td>
                                <td>${profiles.firstName}</td>
                                <td>${profiles.surname}</td>
                                <td>${profiles.email}</td>
                                <td>${profiles.phoneNumber}</td>
                                <td>${profiles.status}</td>
                                <td>${profiles.creationdate}</td>
                                <td>${profiles.lastlogindate}</td>
                                <td>
                                    <button class="btn btn-primary" data-toggle="modal" data-target="#editUser"
                                            data-username="${profiles.username}"
                                            data-firstname="${profiles.firstName}"
                                            data-surname="${profiles.surname}"
                                            data-pass="${profiles.password}"
                                            data-phonenumber="${profiles.phoneNumber}"
                                            data-accountnumber="${profiles.accountNumber}"
                                            data-accountname="${profiles.accountName}"
                                            data-kyc="${profiles.kycLevel}"
                                            data-bvn="${profiles.bvn}">
                                        Edit
                                    </button>
                                </td>
                            </tr>
                        </c:forEach>
                    </c:when>
                    <c:otherwise>
                         ${message}
                    </c:otherwise>
                </c:choose>

            </table>
        </div>

    </div>
</div>
<div class="col-md-12 text-center" style="margin-top: 10px;">

    ${pagination}

</div>
<div id="addNewUser" class="modal fade" tabindex="-1" role="dialog" aria-labelledby="addNewUserLabel" aria-hidden="true">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title">Add User</h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <form action="${pageContext.request.contextPath}/adduser" method="post">
                <div class="modal-body">
                    <div class="form-group">
                        <label>Username</label>
                        <input class="form-control" type="text" name="username" value="">
                    </div>
                    <div class="form-row">
                        <div class="form-group col-md-6">
                            <label>Firstname</label>
                            <input class="form-control" type="text" name="firstame" value="">
                        </div>
                        <div class="form-group col-md-6">
                            <label>Surname</label>
                            <input class="form-control" type="text" name="surname" value="">
                        </div>
                    </div>

                    <div class="form-group">
                        <label>Phone #</label>
                        <input class="form-control" type="text" name="phonenumber" value="">
                    </div>
                    <div class="form-group">
                        <label>Password</label>
                        <input class="form-control" type="password" name="password" value="">
                    </div>
                    <div class="form-group">
                        <label>Confirm Password</label>
                        <input class="form-control" type="password" name="password2" value="">
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
</div>

<div id="editUser" class="modal" tabindex="-1" role="dialog">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title">Edit User</h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div id="edit_display"></div>
            <form action="${pageContext.request.contextPath}/edituser" method="post">
                <div class="modal-body">
                    <div class="form-group">
                        <input class="form-control" type="hidden" name="username">
                    </div>
                    
                    <div class="form-row">
                        <div class="form-group col-md-6">
                            <label>Firstname</label>
                            <input class="form-control" type="text" name="firstName">
                        </div>
                        <div class="form-group col-md-6">
                            <label>Surname</label>
                            <input class="form-control" type="text" name="surname">
                        </div>
                    </div>
                    <div class="form-group">
                        <label>Phone #</label>
                        <input class="form-control" type="text" name="phoneNumber">
                    </div>
                     <div class="form-group">
                        <label>Password</label>
                        <input class="form-control" type="password" name="pass">
                    </div>
                    <div class="form-group">
                        <label>Confirm Password</label>
                        <input class="form-control" type="text" name="pass2">
                    </div> 
                    <div class="form-group">
                        <label>Account Number</label>
                        <input class="form-control" type="text" name="accountNumber">
                    </div>
                    <div class="form-group">
                        <label>Account Name</label>
                        <input class="form-control" type="text" name="accountName">
                    </div>
                    <div class="form-row">
                        <div class="form-group col-md-6">
                            <label>KYC Level</label>
                            <input class="form-control" type="text" name="kyclevel">
                        </div>
                        <div class="form-group col-md-6">
                            <label>BVN</label>
                            <input class="form-control" type="text" name="bvn">
                        </div>
                    </div>
                
                <div class="modal-footer">
                    
                    <button type="button" class="btn btn-default closeModal" data-dismiss="modal">Close</button>
                    <button class="btn btn-primary edituser" type="submit">Edit User</button>
                </div>
            </form>
            </div>
        </div>
    </div>
</div>
<script type="text/javascript">
    $('#editUser').on('show.bs.modal', function (event) {
     var button = $(event.relatedTarget);
     var username = button.data('username');
     var firstname = button.data('firstname');
     var surname = button.data('surname');
     var phonenumber = button.data('phonenumber');
     var pass = button.data('pass');
     var accountnumber = button.data('accountnumber');
     var accountname = button.data('accountname');
     var kyc = button.data('kyc');
     var bvn = button.data('bvn');
     var balance = button.data('balance');
     
     var modal = $(this);
     
     modal.find('[name=firstName]').val(firstname);
     modal.find('[name=surname]').val(surname);
     modal.find('[name=phoneNumber]').val(phonenumber);
     
     modal.find('[name=accountNumber]').val(accountnumber);
     modal.find('[name=accountName]').val(accountname);
     modal.find('[name=pass]').val(pass);
     modal.find('[name=kyclevel]').val(kyc);
     modal.find('[name=bvn]').val(bvn);
     modal.find('[name=balance]').val(balance);
     
     modal.find('[name=username]').val(username);

     
     });
</script>