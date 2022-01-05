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
        <form:form id="pidForm" class="form-horizontal" modelAttribute="entity" autocomplete="off"
                   action="${contextRoot}/Specifications/action"
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
                    <div class="characteristics__header header">Характеристики PID</div>
                    <div class="characteristics__row">
                        <div class="characteristics__body">
                            <table class="characteristics__table table">
                                <tbody>
                                <tr>
                                    <td class="label-column"><label class="control-label col-md-4 text-nowrap" for="number">Номер PID:</label></td>
                                    <td>
                                        <form:input path="number" id="number" class="form-control" tabindex="1" type="text" />
                                        <form:errors path="number" cssClass="help-block" element="em"/>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="label-column"><label class="col-md-4">Спецификация:</label></td>
                                    <td class="label-column ">
                                        <span>${entity.specification.number}</span>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="label-column"><label class="col-md-4">Заказчик:</label></td>
                                    <td class="label-column">
                                        <span>${entity.specification.customer.name}</span>
                                    </td>
                                </tr>
                                </tbody>
                            </table>
                        </div>
                        <div class="characteristics__body">
                            <table class="characteristics__table table">
                                <tbody>
                                <tr>
                                    <td class="label-column"><label class="control-label col-md-4" for="designation">Обозначение:</label></td>
                                    <td>
                                        <form:input path="designation" id="designation" class="form-control" tabindex="2" type="text" list="designations"/>
                                        <c:if test = "${not empty designations}">
                                            <datalist id="designations">
                                                <c:forEach var="designationIndex" items="${designations}">
                                                    <option value="${designationIndex}"/>
                                                </c:forEach>
                                            </datalist>
                                        </c:if>
                                        <form:errors path="designation" cssClass="help-block" element="em"/>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="label-column"><label class="control-label col-md-4 text-nowrap" for="shippingDate">Срок поставки:</label></td>
                                    <td>
                                        <form:input path="shippingDate" class="form-control date-picker" />
                                        <form:errors path="shippingDate" cssClass="help-block" element="em"/>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="label-column"><label class="control-label col-md-4" for="weight">Масса:</label></td>
                                    <td>
                                        <form:input path="weight" id="weight" class="form-control" tabindex="3" type="text" />
                                        <form:errors path="weight" cssClass="help-block" element="em"/>
                                    </td>
                                </tr>
                                </tbody>
                            </table>
                        </div>
                        <div class="characteristics__body">
                            <table class="characteristics__table table">
                                <tbody>
                                <tr>
                                    <td class="label-column"><label class="control-label col-md-4 text-nowrap" for="aacType">Тип АКП:</label></td>
                                    <td style="min-width: 150px">
                                        <form:select path="aacType" id="aacType" class="form-control selectize" placeholder="Выберите тип АКП...">
                                            <form:option style="display:none" value="" label="Выберите тип АКП..."/>
                                            <form:option value="Подземное" />
                                            <form:option value="Надземное" />
                                        </form:select>
                                        <form:errors path="aacType" cssClass="help-block" element="em"/>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="label-column"><label class="control-label col-md-4" for="amount">Количество:</label></td>
                                    <td>
                                        <form:input path="amount" id="amount" class="form-control" tabindex="4" type="text" />
                                        <form:errors path="amount" cssClass="help-block" element="em"/>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="label-column"><label class="col-md-4">Отгружено:</label></td>
                                    <td class="label-column">
                                        <span>${entity.amountShipped}</span>
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
                        <table class="characteristics__table table">
                            <tbody>
                            <tr>
                                <td>
                                    <div class="comment__header header">Описание</div>
                                    <div class="comment__form">
                                        <form:textarea path="description" id="description" rows="10" class="form-control form-textarea"/>
                                        <form:errors path="description" cssClass="help-block" element="em"/>
                                    </div>
                                </td>
                                <td>
                                    <div class="comment__header header">Грузополучатель</div>
                                    <div class="comment__form">
                                        <form:textarea path="consignee" id="consignee" rows="10"
                                                       placeholder="УКАЗЫВАТЬ ГРУЗОПОЛУЧАТЕЛЯ СЛЕДУЮЩИМ ОБРАЗОМ:

Если авто: АО \"Транснефть-Прикамье\", Республика Татарстан, г. Бугульма, ул. Монтажная, 1;
                   АО \"Транснефть - Дружба\" Орловская обл., Орловский р-он, п. Стальной Конь, ЛПДС \"Стальной Конь\"
Если жд:   АО \"Транснефть - Урал\" ст. Черниковка КБШ ж.д.;
                   АО \"Транснефть - Урал\" ст. Юргамыш Южно-Уральская ж.д."
                                                       class="form-control form-textarea"/>
                                        <form:errors path="consignee" cssClass="help-block" element="em"/>
                                    </div>
                                </td>
                            </tr>
                            </tbody>
                        </table>
                    </div>
                    <div class="comment__body">
                        <div class="comment__header header">Примечание</div>
                        <div class="comment__form">
                            <form:textarea path="comment" id="comment" rows="2" class="form-control form-textarea"/>
                            <form:errors path="comment" cssClass="help-block" element="em"/>
                        </div>
                    </div>
                </div>
                <div class="bottom-bar">
                    <div class="bottom-bar__row">
                        <c:choose>
                            <c:when test="${not empty entity.reqId}">
                                <c:choose>
                                    <c:when test="${entity.reqName == 'valve'}">
                                        <button class="bottom-bar__button back-button" onclick="window.location.href='${contextRoot}/entity/SheetGateValves/showFormForUpdate/${entity.reqId}'; return false;">Назад</button>
                                    </c:when>
                                    <c:when test="${entity.reqName == 'nozzle'}">
                                        <button class="bottom-bar__button back-button" onclick="window.location.href='${contextRoot}/entity/Nozzles/showFormForUpdate/${entity.reqId}'; return false;">Назад</button>
                                    </c:when>
                                    <c:otherwise>
                                        <button class="bottom-bar__button back-button" onclick="window.location.href='${contextRoot}/entity/Saddles/showFormForUpdate/${entity.reqId}'; return false;">Назад</button>
                                    </c:otherwise>
                                </c:choose>
                            </c:when>
                            <c:otherwise>
                                <button class="bottom-bar__button back-button" onclick="window.location.href='${contextRoot}/Specifications/showAll'; return false;">Назад</button>
                            </c:otherwise>
                        </c:choose>
                        <input id="SaveButton" type="submit" name="save" value="Сохранить" class="bottom-bar__button" />
                        <form:hidden path="id"/>
                        <form:hidden path="reqId"/>
                        <form:hidden path="reqName"/>
                        <form:hidden path="specification.id"/>
                        <form:hidden path="specification.number"/>
                        <form:hidden path="specification.customer.name"/>
                        <form:hidden path="amountShipped"/>
                    </div>
                </div>
            </div>
        </form:form>

        <%@include file="../01-construct/script-suit-ediView.jsp"%>
    </div>
</div>
</body>
</html>
