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
                   action="${contextRoot}/entity/${sealingClass}/action"
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
                    <div class="characteristics__header header">${title}</div>
                    <div class="characteristics__row">
                        <div class="characteristics__body">
                            <table class="characteristics__table table">
                                <tbody>
                                <tr>
                                    <td class="label-column"><label class="control-label col-md-4" for="name">Наименование:</label></td>
                                    <td style="min-width: 300px">
                                        <form:input path="name" id="name" class="form-control" tabindex="1" type="text" list="names"/>
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
                                    <td class="label-column"><label class="control-label col-md-4" for="drawing">Чертеж:</label></td>
                                    <td style="min-width: 150px">
                                        <form:input path="drawing" id="drawing" class="form-control" tabindex="7" type="text" list="drawings"/>
                                        <c:if test = "${not empty drawings}">
                                            <datalist id="drawings">
                                                <c:forEach var="drawingIndex" items="${drawings}">
                                                    <option value="${drawingIndex}"/>
                                                </c:forEach>
                                            </datalist>
                                        </c:if>
                                        <form:errors path="drawing" cssClass="help-block" element="em"/>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="label-column"><label class="control-label col-md-4" for="certificate">Сертификат:</label></td>
                                    <td>
                                        <form:input path="certificate" id="certificate" class="form-control" tabindex="5" type="text" />
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
                                    <td class="label-column"><label class="control-label col-md-4" for="material">Материал:</label></td>
                                    <td>
                                        <form:input path="material" id="material" class="form-control" tabindex="2" type="text" list="materials"/>
                                        <c:if test = "${not empty materials}">
                                            <datalist id="materials">
                                                <c:forEach var="materialIndex" items="${materials}">
                                                    <option value="${materialIndex}"/>
                                                </c:forEach>
                                            </datalist>
                                        </c:if>
                                        <form:errors path="material" cssClass="help-block" element="em"/>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="label-column"><label class="control-label col-md-4" for="batch">Партия:</label></td>
                                    <td>
                                        <form:input path="batch" id="batch" class="form-control" tabindex="4" type="text" />
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
                                <c:if test="${sealingClass == 'FrontalSaddleSealings' or sealingClass == 'Springs'}">
                                    <tr>
                                        <td class="label-column"><label class="col-md-4">Остаток:</label></td>
                                        <td class="label-column">
                                            <span>${entity.amountRemaining}</span>
                                        </td>
                                    </tr>
                                </c:if>
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
                        <ul class="nav nav-tabs" id="operations">
                            <li class="active">
                                <a href="#journals" class="tab-style">Операции</a>
                            </li>
                            <li>
                                <a href="#usage" class="tab-style">Применение</a>
                            </li>
                        </ul>
                        <div class="tab-content">
                            <div class="tab-pane active" id="journals">
                                <%@include file="../journal/base-journal-view.jsp"%>
                            </div>
                            <div class="tab-pane" id="usage">
                                <table class="table table-striped table-bordered" style="min-width: 1500px">
                                    <c:choose>
                                        <c:when test="${sealingClass == 'Springs'}">
                                            <c:forEach var="tempObject" items="${entity.baseValveWithSprings}" varStatus="s">
                                                <c:url var="openLink" value="/entity/Springs/openSheetGateValve">
                                                    <c:param name="valveId" value="${tempObject.baseValve.id}" />
                                                </c:url>
                                                <tr>
                                                    <td style="font-size: 12px">
                                                            ${tempObject.baseValve}
                                                        <form:hidden path="baseValveWithSprings[${s.index}].baseValve.id"/>
                                                        <form:hidden path="baseValveWithSprings[${s.index}].baseValve.number"/>
                                                    </td>
                                                    <td class="text-nowrap" style="font-size: 12px">
                                                            ${tempObject.springAmount} шт.
                                                        <form:hidden path="baseValveWithSprings[${s.index}].springAmount"/>
                                                    </td>
                                                    <td style="text-align: center"><a href="${openLink}"><span class="glyphicon glyphicon-open"></span></a></td>
                                                        <%--                                    <td style="text-align: center"><input class="btn btn-danger" name="deleteSpring" type="submit" value="${s.index}"/></td>--%>
                                                        <%--                                    <form:hidden path="baseValveWithSprings[${s.index}].id"/>--%>
                                                </tr>
                                            </c:forEach>
                                        </c:when>
                                        <c:when test="${sealingClass == 'MainFlangeSealings'}">
                                            <c:forEach var="tempObject" items="${entity.sheetGateValves}" varStatus="s">
                                                <c:url var="openLink" value="/entity/${sealingClass}/openSheetGateValve">
                                                    <c:param name="valveId" value="${tempObject.id}" />
                                                </c:url>
                                                <tr>
                                                    <td>
                                                            ${tempObject}
                                                        <form:hidden path="sheetGateValves[${s.index}].id"/>
                                                        <form:hidden path="sheetGateValves[${s.index}].number"/>
                                                    </td>
                                                    <td style="text-align: center"><a href="${openLink}"><span class="glyphicon glyphicon-open"></span></a></td>
                                                </tr>
                                            </c:forEach>
                                        </c:when>
                                        <c:otherwise>
                                            <c:forEach var="tempObject" items="${entity.saddles}" varStatus="s">
                                                <c:url var="openLink" value="/entity/${sealingClass}/openSaddle">
                                                    <c:param name="saddleId" value="${tempObject.id}" />
                                                </c:url>
                                                <tr>
                                                    <td>${tempObject.name}</td>
                                                    <td>
                                                            ${tempObject}
                                                        <form:hidden path="saddles[${s.index}].id"/>
                                                        <form:hidden path="saddles[${s.index}].zk"/>
                                                        <form:hidden path="saddles[${s.index}].number"/>
                                                        <form:hidden path="saddles[${s.index}].melt"/>
                                                        <form:hidden path="saddles[${s.index}].drawing"/>
                                                        <form:hidden path="saddles[${s.index}].dn"/>
                                                        <form:hidden path="saddles[${s.index}].status"/>
                                                        <form:hidden path="saddles[${s.index}].name"/>
                                                    </td>
                                                    <td style="text-align: center"><a href="${openLink}"><span class="glyphicon glyphicon-open"></span></a></td>
                                                </tr>
                                            </c:forEach>
                                            <br>
                                            <c:forEach var="tempObject" items="${entity.sheetGateValveCovers}" varStatus="s">
                                                <c:url var="openLink" value="/entity/${sealingClass}/openValveCover">
                                                    <c:param name="coverId" value="${tempObject.id}" />
                                                </c:url>
                                                <tr>
                                                    <td>${tempObject.name}</td>
                                                    <td>
                                                            ${tempObject}
                                                        <form:hidden path="sheetGateValveCovers[${s.index}].id"/>
                                                        <form:hidden path="sheetGateValveCovers[${s.index}].dn"/>
                                                        <form:hidden path="sheetGateValveCovers[${s.index}].number"/>
                                                        <form:hidden path="sheetGateValveCovers[${s.index}].melt"/>
                                                        <form:hidden path="sheetGateValveCovers[${s.index}].status"/>
                                                    </td>
                                                    <td style="text-align: center"><a href="${openLink}"><span class="glyphicon glyphicon-open"></span></a></td>
                                                </tr>
                                            </c:forEach>
                                        </c:otherwise>
                                    </c:choose>
                                </table>
                            </div>
                        </div>
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
                        <button class="bottom-bar__button back-button" onclick="window.location.href='${contextRoot}/entity/${sealingClass}/showAll'; return false;">Назад</button>
                        <input id="SaveButton" type="submit" name="save" value="Сохранить" class="bottom-bar__button" />
                        <form:hidden path="id"/>
                        <form:hidden path="number"/>
                        <form:hidden path="amountRemaining"/>
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