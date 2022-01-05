<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<%@page contentType="text/html; charset=UTF-8" %>
<c:set var="contextRoot" value="${pageContext.request.contextPath}"/>

<!DOCTYPE html>
<html lang="ru">
<head>
    <meta http-equiv="Content-type" content="text/html;charset=UTF-8">
    <meta name="viewport" content="width=1200">
    <title>${title}</title>
    <link href="${contextRoot}/resources/css/bootstrap.min.css" rel="stylesheet">
    <link href="${contextRoot}/resources/css/bootstrap-theme.min.css" rel="stylesheet">
    <link href="${contextRoot}/resources/css/main-style.css" rel="stylesheet">
    <script>
        window.menu = '${title}';
        window.contextRoot = '${contextRoot}';
        function preback() {window.history.forward();}
        setTimeout("preback()",0);
        window.onunload=function () {null};
    </script>
</head>
<body>
    <%@include file="./menu/top-bar-main.jsp"%>
<div class="content">
    <div class="container">
        <div id="loginbox" style="margin-top: 150px;"
             class="col-md-offset-3 col-md-6">
            <c:if test="${not empty message}">
                <div class="row">
                    <div class="col-md-12">
                        <div class="alert alert-danger">${message}</div>
                    </div>
                </div>
            </c:if>
            <c:if test="${not empty logout}">
                <div class="row">
                    <div class="col-md-12">
                        <div class="alert alert-success">${logout}</div>
                    </div>
                </div>
            </c:if>
            <div class="panel panel-primary">
                <div class="panel-heading">
                    <div class="panel-title">Вход</div>
                </div>
                <div style="padding-top: 30px" class="panel-body">
                    <form:form action="${contextRoot}/login"
                               method="POST" class="form-horizontal" id="loginForm" autocomplete="off">
                        <div style="margin-bottom: 25px" class="input-group">
                            <span class="input-group-addon"><i class="glyphicon glyphicon-user"></i></span>
                            <input type="search" name="username" placeholder="Логин" class="form-control"/>
                        </div>
                        <div style="margin-bottom: 25px" class="input-group">
                            <span class="input-group-addon"><i class="glyphicon glyphicon-lock"></i></span>
                            <input type="password" name="password" placeholder="Пароль" class="form-control" >
                        </div>
                        <div style="margin-top: 10px" class="form-group">
                            <div class="col-sm-6 controls">
                                <button type="submit" class="btn btn-primary">ОК</button>
                            </div>
                        </div>
                    </form:form>
                </div>
            </div>
        </div>
    </div>
</div>
<script src="${contextRoot}/resources/js/jquery.js"></script>
<script src="${contextRoot}/resources/js/jquery.validate.js"></script>
<script src="${contextRoot}/resources/js/bootstrap.min.js"></script>
</body>
</html>