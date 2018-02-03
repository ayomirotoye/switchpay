<%@ page isErrorPage="true" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
    <head></head>
    <body>
        <section>
            <div class="">
                    <div class="text-center d-flex align-items-center" style="height: 100vh;">
                            <div class="w-100">
                                    <h2 class="display-1 text-muted">Oops...</h2>
                                    <p class="lead">something went wrong.</p>
                                    <small class="text-muted">The exception is: <%= exception %></small>
                            </div>
                    </div>
            </div>
        </section>
    </body>
</html>