<%@page contentType="text/html; charset=UTF-8" %>


<head>
    <meta charset="utf-8" />
    <meta name="viewport" content="width=1700">
    <meta name="_csrf" content="${_csrf.token}">
    <meta name="_csrf_header" content="${_csrf.headerName}">
    <title>${title}</title>
    <link rel="icon" href="${contextRoot}/resources/img/transneft-icon.png" type="image/x-icon">
    <link href="${contextRoot}/resources/css/bootstrap.min.css" rel="stylesheet">
    <link href="${contextRoot}/resources/css/bootstrap-theme.min.css" rel="stylesheet">
    <link href="${contextRoot}/resources/css/selectize.bootstrap3.css" rel="stylesheet">
    <link href="${contextRoot}/resources/css/main-style.css" rel="stylesheet">
    <link href="${contextRoot}/resources/css/edit-page-style.css" rel="stylesheet">
    <link href="https://code.jquery.com/ui/1.13.0/themes/smoothness/jquery-ui.css" rel="stylesheet" />
<%--    <script>--%>
<%--        function preback() {window.history.forward();}--%>
<%--        setTimeout("preback()",0);--%>
<%--        window.onunload=function () {null};--%>
<%--    </script>--%>
</head>
<div class="topTN">
<%--    <span class="top_logo">Tnn web DataBase</span>--%>
    <span class="top_logo">транснефть надзор</span>
    <div class="top_menu">
        <button class="top_button"
                onclick="window.location.href='${contextRoot}/menu'; return false;">Главное меню</button>
        <div class="top_div" style="color: #ccc">Изделие</div>
        <div class="top_div" style="color: #ccc">Заказ</div>
        <div class="top_div" style="color: #ccc">Отчетность</div>
        <div class="top_div" style="color: #ccc">Периодика</div>
        <div class="top_div" style="color: #ccc">Материалы</div>
        <button class="top_button" style="color: #ccc">Журналы</button>
        <security:authorize access="isAuthenticated()">
            <div class="top_div"><span style="text-transform: uppercase; color: #ccc;">${userModel.name}</span></div>
        </security:authorize>
    </div>
</div>