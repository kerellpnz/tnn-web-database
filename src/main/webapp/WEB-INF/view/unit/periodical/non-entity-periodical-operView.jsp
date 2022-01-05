<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<%@page contentType="text/html; charset=UTF-8" %>
<c:set var="contextRoot" value="${pageContext.request.contextPath}"/>

<!DOCTYPE html>
<html lang="ru">
<%@include file="../01-construct/top-bar-editView.jsp"%>
<body>
<div class="contentTN">
    <div class="containerTN">
        <form:form id="entityForm" class="form-horizontal" modelAttribute="entity" autocomplete="off"
                   action="${contextRoot}/entity/${periodicalClass}/action"
                   method="POST">
            <div class="wrapper">
                <div class="characteristics">
                    <c:if test="${not empty message}">
                        <div class="col-xs-12">
                            <div class="alert alert-danger alert-dismissable">
                                <button type="button" class="close" data-dismiss="alert">&times;</button>
                                    ${message}
                            </div>
                        </div>
                    </c:if>
                    <div class="characteristics__header header">Характеристики</div>
                    <div class="characteristics__row">
                        <div class="characteristics__body">
                            <table class="table" style="min-width: 300px">
                                <tbody>
                                <tr>
                                    <td class="label-column"><label class="col-md-4 text-nowrap">Тип периодики:</label></td>
                                    <td style="min-width: 150px">
                                            ${type}
                                    </td>
                                </tr>
                                </tbody>
                            </table>
                        </div>
                        <div class="characteristics__body">
                            <table class="table" style="min-width: 300px">
                                <tbody>
                                <tr>
                                    <td class="label-column"><label class="col-md-4 text-nowrap">Последняя дата контроля:</label></td>
                                    <td style="min-width: 150px">
                                        <fmt:formatDate value="${lastDate}" pattern="dd.MM.yyyy" />
                                    </td>
                                </tr>
                                </tbody>
                            </table>
                        </div>
                        <div class="characteristics__body">
                            <table class="table" style="min-width: 300px">
                                <tbody>
                                <tr>
                                    <td class="label-column"><label class="col-md-4 text-nowrap">Следующая дата контроля:</label></td>
                                    <td style="min-width: 150px">
                                        <fmt:formatDate value="${nextDate}" pattern="dd.MM.yyyy" />
                                    </td>
                                </tr>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
                <div class="content-edit-page">
                    <div class="tab-control">
                        <%@include file="../journal/base-journal-view.jsp"%>
                    </div>
                </div>
                <div class="bottom-bar">
                    <div class="bottom-bar__row">
                        <button class="bottom-bar__button back-button" onclick="window.location.href='${contextRoot}/'; return false;">Назад</button>
                        <input id="SaveButton" type="submit" name="save" value="Сохранить" class="bottom-bar__button" />
                    </div>
                </div>
            </div>
        </form:form>

        <%@include file="../01-construct/script-suit-ediView.jsp"%>
    </div>
</div>
</body>
</html>