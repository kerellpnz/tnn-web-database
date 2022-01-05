<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@page contentType="text/html; charset=UTF-8" %>
<c:set var="contextRoot" value="${pageContext.request.contextPath}"/>

<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="utf-8" />
    <link href="${contextRoot}/resources/css/edit-page-style.css" rel="stylesheet">
</head>
<body>
<div class="contentTN">
    <div class="containerTN">
        <form:form id="messageForm" class="form-horizontal" modelAttribute="errorMessage" autocomplete="off"
                   action="${contextRoot}/errorMessage/send"
                   method="POST">
            <div class="wrapper">
                <div class="characteristics">
                    <div class="comment__header header">Краткое описание</div>
                    <div class="comment__form">
                        <form:input path="shortDescription" id="shortDescription" class="form-control"/>
                        <form:errors path="shortDescription" cssClass="help-block" element="em"/>
                    </div>
                </div>
                <div class="comment">
                    <div class="comment__row">
                        <div class="comment__body">
                            <div class="comment__header header">Описание</div>
                            <div class="comment__form">
                                <form:textarea path="description" id="description" rows="10" placeholder="Опишите ваши действия!
Скопируйте сюда сообщение об ошибке..." class="form-control"/>
                                <form:errors path="description" cssClass="help-block" element="em"/>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="bottom-bar">
                    <div class="bottom-bar__row">
                        <button class="bottom-bar__button back-button" onclick="window.location.href='${contextRoot}/'; return false;">Назад</button>
                        <input id="SaveButton" type="submit" name="save" value="Отправить" class="bottom-bar__button" />
                        <form:hidden path="id"/>
                    </div>
                </div>
            </div>
        </form:form>
    </div>
</div>
</body>
</html>