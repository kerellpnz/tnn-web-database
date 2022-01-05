<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<%@page contentType="text/html; charset=UTF-8" %>
<c:set var="contextRoot" value="${pageContext.request.contextPath}"/>

<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="utf-8" />
    <meta name="viewport" content="width=1700">
    <meta name="_csrf" content="${_csrf.token}">
    <meta name="_csrf_header" content="${_csrf.headerName}">
    <title>${title}</title>
    <link rel="icon" href="${contextRoot}/resources/img/transneft-icon.png" type="image/x-icon">
    <link href="${contextRoot}/resources/css/bootstrap.min.css" rel="stylesheet">
    <link href="${contextRoot}/resources/css/bootstrap-theme.min.css" rel="stylesheet">
    <link href="${contextRoot}/resources/css/main-style.css" rel="stylesheet">
    <script>
        window.menu = '${title}';
        window.contextRoot = '${contextRoot}';
        window.userRole = '${userModel.role}';
        function preback() {window.history.forward();}
        setTimeout("preback()",0);
        window.onunload=function () {null};
    </script>
</head>
<body>
    <%@include file="./menu/top-bar-main.jsp"%>
<div class="contentTN">
    <div class="containerTN">
        <%@include file="./menu/side-bar.jsp"%>
        <div id="workspace">
            <div class="jumbotron">
                <h1>${errorTitle}</h1>
                <hr/>
                <blockquote class="blockquote">
                    ${errorDescription}
                </blockquote>
                <c:if test="${not empty errorMessage}">
                    <hr/>
                    <div style="padding: 10px; font-size: 20px; font-weight: 700">Сообщение об ошибке:</div>
                    <blockquote class="blockquote">
                            ${errorMessage}
                    </blockquote>
                </c:if>
            </div>
        </div>
        <script src="${contextRoot}/resources/js/jquery.js"></script>
        <script src="${contextRoot}/resources/js/bootstrap.min.js"></script>
        <script src="${contextRoot}/resources/js/tnn-web-database.js"></script>
    </div>
</div>
</body>
</html>