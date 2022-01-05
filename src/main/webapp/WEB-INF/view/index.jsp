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
	<link href="${contextRoot}/resources/css/dataTables.bootstrap.css" rel="stylesheet">
	<link href="${contextRoot}/resources/css/dataTables.buttons.min.css" rel="stylesheet">
	<link href="${contextRoot}/resources/css/selectize.bootstrap3.css" rel="stylesheet">
	<link href="${contextRoot}/resources/css/main-style.css" rel="stylesheet">
	<script>
		window.menu = '${title}';
		window.contextRoot = '${contextRoot}';
		window.userRole = '${userModel.role}';
		// function preback() {window.history.forward();}
		// setTimeout("preback()",0);
		// window.onunload=function () {null};
	</script>
</head>
<body>
	<%@include file="./menu/top-bar-main.jsp"%>
	<div class="contentTN">
		<div class="containerTN">
			<%@include file="./menu/side-bar.jsp"%>
			<div id="workspace">
				<c:if test="${userClickMainMenu == true}">
					<%@include file="./menu/main-menu.jsp"%>
				</c:if>
				<c:if test="${userClickProfile == true}">
					<%@include file="./menu/profile-menu.jsp"%>
				</c:if>
				<c:if test="${userClickChangePassword == true}">
					<%@include file="./menu/change-password-menu.jsp"%>
				</c:if>
				<c:if test="${userClickGateValveMenu == true}">
					<%@include file="./menu/gate-valve-menu.jsp"%>
				</c:if>
				<c:if test="${userClickTCP == true}">
					<script>window.tcpClass='${tcpClass}';</script>
					<%@include file="./tcp/tcp-view.jsp"%>
				</c:if>
				<c:if test="${userClickJournalNumbers == true}">
					<%@include file="unit/general/journal-number-view.jsp"%>
				</c:if>
				<c:if test="${userClickCustomers == true}">
					<%@include file="unit/general/customer-view.jsp"%>
				</c:if>
				<c:if test="${userClickSpecifications == true}">
					<%@include file="unit/general/specification-view.jsp"%>
				</c:if>
				<c:if test="${userClickEntityView == true}">
					<%@include file="unit/entity-view.jsp"%>
				</c:if>
				<c:if test="${userClickErrorMessage == true}">
					<%@include file="error-message.jsp"%>
				</c:if>
			</div>
			<script src="${contextRoot}/resources/js/jquery.js"></script>
			<script src="${contextRoot}/resources/js/jquery.validate.js"></script>
			<script src="${contextRoot}/resources/js/bootstrap.min.js"></script>
			<script src="${contextRoot}/resources/js/jquery.dataTables.js"></script>
			<script src="${contextRoot}/resources/js/jquery-ui.js"></script>
			<script src="${contextRoot}/resources/js/moment.js"></script>
			<script src="${contextRoot}/resources/js/dataTables.bootstrap.js"></script>
			<script src="${contextRoot}/resources/js/dataTables.buttons.min.js"></script>
			<script src="${contextRoot}/resources/js/dataTables.dateRender.js"></script>
			<script src="${contextRoot}/resources/js/bootbox.all.min.js"></script>
			<script src="${contextRoot}/resources/js/selectize.min.js"></script>
			<script src="${contextRoot}/resources/js/tnn-web-database.js"></script>
		</div>
	</div>
</body>
</html>