<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div class="card card-default">
    <div class="card-header">Change Token</div>
    <div class="card-body">
        <div class="row">
            <div class="col-md-7">
                <form action="${pageContext.request.contextPath}/ctoken1">
                    <div class="form-group">
                        <label>Current Token</label>
                        <input class="form-control" type="password" type="text" name="oToken" value="">
                    </div>
                    <div class="form-group">
                        <label>New Token</label>
                        <input class="form-control" type="password" type="text" name="nToken" value="">
                    </div>
                    <div class="form-group">
                        <label>Confirm Token</label>
                        <input class="form-control" type="password" type="text" name="cToken" value="">
                    </div>
                    <div class="form-group">
                        <input class="btn btn-primary btn-block changeToken" type="submit" value="Change Token">
                    </div>
                </form>
            </div>
            <div class="col-md-5">
                <img src="<c:url value="/resources/imgs/password_logo.jpg"/>" alt="OSPoly Logo" style ="width: 100%; margin-bottom: 10px" />
            </div>
        </div>
    </div>
</div>