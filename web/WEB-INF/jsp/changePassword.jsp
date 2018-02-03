<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div class="card card-default">
    <div class="card-header">Change Password</div>
    <div class="card-body">
<!--        <div class="form-group">
            <label>${alertMessage}</label>
        </div>-->

        <div class="row">
            <div class="col-md-7">
                <form action="${pageContext.request.contextPath}/cpassword1">
                    <div class="form-group">
                        <label>Current Password</label>
                        <input class="form-control" type="password" name="oPassword" value="">
                    </div>
                    <div class="form-group">
                        <label>New Password</label>
                        <input class="form-control" type="password" name="nPassword" value="">
                    </div>
                    <div class="form-group">
                        <label>Confirm Password</label>
                        <input class="form-control" type="password" name="cPassword" value="">
                    </div>
                    <div class="form-group">
                        <input class="btn btn-primary btn-block changePassword" type="submit" value="Change">
                    </div>
                </form>
            </div>
            <div class="col-md-5">
                <img src="<c:url value="/resources/imgs/password_logo.jpg"/>" alt="OSPoly Logo" style ="width: 100%; margin-bottom: 10px" />
            </div>
        </div>
    </div>
</div>