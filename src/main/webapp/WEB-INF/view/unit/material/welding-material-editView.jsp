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
                   action="${contextRoot}/entity/WeldingMaterials/action"
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
                    <div class="characteristics__header header">Характеристики сварочного материала</div>
                    <div class="characteristics__row">
                        <div class="characteristics__body">
                            <table class="characteristics__table table">
                                <tbody>
                                <tr>
                                    <td class="label-column"><label class="control-label col-md-4" for="name">Наименование:</label></td>
                                    <td style="min-width: 400px">
                                        <form:input path="name" id="name" class="form-control" tabindex="2" type="text" list="names"/>
                                        <c:if test = "${not empty names}">
                                            <datalist id="names">
                                                <c:forEach var="index" items="${names}">
                                                    <option value="${index}"/>
                                                </c:forEach>
                                            </datalist>
                                        </c:if>
                                        <form:errors path="name" cssClass="help-block" element="em"/>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="label-column"><label class="control-label col-md-4" for="certificate">Сертификат:</label></td>
                                    <td>
                                        <form:input path="certificate" id="certificate" class="form-control" tabindex="2" type="text" />
                                        <form:errors path="certificate" cssClass="help-block" element="em"/>
                                    </td>
                                </tr>
                                </tbody>
                            </table>
                        </div>
                        <div class="characteristics__body">
                            <table class="characteristics__table table">
                                <tbody>
                                <tr>
                                    <td class="label-column"><label class="control-label col-md-4" for="batch">Партия:</label></td>
                                    <td>
                                        <form:input path="batch" id="batch" class="form-control" tabindex="3" type="text" />
                                        <form:errors path="batch" cssClass="help-block" element="em"/>
                                    </td>
                                </tr>
                                </tbody>
                            </table>
                        </div>
                        <div class="characteristics__body">
                            <table class="characteristics__table table">
                                <tbody>
                                <tr>
                                    <td class="label-column"><label class="control-label col-md-4" for="amount">Количество:</label></td>
                                    <td>
                                        <form:input path="amount" id="amount" class="form-control" tabindex="4" type="text" />
                                        <form:errors path="amount" cssClass="help-block" element="em"/>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="label-column"><label class="col-md-4">СТАТУС:</label></td>
                                    <td class="label-column">
                                        <c:choose>
                                        <c:when test="${entity.status == 'НЕ СООТВ.'}">
                                        <span style="color: red; font-weight: 700;">
								</c:when>
								<c:otherwise>
									<span style="color: green; font-weight: 700;">
								</c:otherwise>
							</c:choose>
                                            ${entity.status}</span>
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
                <div class="comment">
                    <div class="comment__row">
                        <div class="comment__body">
                            <div class="comment__header header">Примечание</div>
                            <div class="comment__form">
                                <form:textarea path="comment" id="comment" rows="3" class="form-control"/>
                                <form:errors path="comment" cssClass="help-block" element="em"/>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="bottom-bar">
                    <div class="bottom-bar__row">
                        <button class="bottom-bar__button back-button" onclick="window.location.href='${contextRoot}/entity/WeldingMaterials/showAll'; return false;">Назад</button>
                        <input id="SaveButton" type="submit" name="save" value="Сохранить" class="bottom-bar__button" />
                        <form:hidden path="id"/>
                        <form:hidden path="status"/>
                    </div>
                </div>
            </div>
        </form:form>

        <%@include file="../01-construct/script-suit-ediView.jsp"%>
    </div>
</div>
</body>
</html>