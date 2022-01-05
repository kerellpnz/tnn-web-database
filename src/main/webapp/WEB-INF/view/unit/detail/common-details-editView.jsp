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
        <form:form id="caseBottomForm" class="form-horizontal" modelAttribute="entity" autocomplete="off"
                   action="${contextRoot}/entity/${commonDetailClass}/action"
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
                            <table class="table">
                                <tbody>
                                <tr>
                                    <td class="label-column"><label class="control-label col-md-4" for="number">Номер:</label></td>
                                    <td style="min-width: 150px">
                                        <form:input path="number" id="number" class="form-control" tabindex="1" type="text" />
                                        <form:errors path="number" cssClass="help-block" element="em"/>
                                    </td>
                                </tr>
                                <c:if test="${commonDetailClass == 'CoverSleeves008'}">
                                    <tr>
                                        <td class="label-column"><label class="control-label col-md-4" for="zk">ЗК:</label></td>
                                        <td style="min-width: 150px">
                                            <form:input path="zk" id="zk" class="form-control" tabindex="6" type="text" />
                                            <form:errors path="zk" cssClass="help-block" element="em"/>
                                        </td>
                                    </tr>
                                </c:if>
                                <tr>
                                    <td class="label-column"><label class="control-label col-md-4" for="dn">DN:</label></td>
                                    <td style="min-width: 150px">
                                        <form:select path="dn" id="dn" class="form-control selectize" placeholder="Выберете DN...">
                                            <form:option style="display:none" value="" label="Выберете DN..."/>
                                            <form:options items="${dns}" />
                                        </form:select>
                                        <form:errors path="dn" cssClass="help-block" element="em"/>
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
                                </tbody>
                            </table>
                        </div>
                        <div class="characteristics__body">
                            <table class="table">
                                <tbody>
                                <tr>
                                    <td class="label-column"><label class="control-label col-md-4 text-nowrap" for="material">Марка материала:</label></td>
                                    <td style="min-width: 150px">
                                        <form:input path="material" id="material" class="form-control" tabindex="4" type="text" />
                                        <form:errors path="material" cssClass="help-block" element="em"/>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="label-column"><label class="control-label col-md-4" for="melt">Плавка:</label></td>
                                    <td style="min-width: 150px">
                                        <form:input path="melt" id="melt" class="form-control" tabindex="4" type="text" />
                                        <form:errors path="melt" cssClass="help-block" element="em"/>
                                    </td>
                                </tr>
                                </tbody>
                            </table>
                        </div>
                        <div class="characteristics__body">
                            <table class="table">
                                <tbody>
                                <tr>
                                    <td class="label-column"><label class="control-label col-md-4" for="certificate">Сертификат:</label></td>
                                    <td style="min-width: 150px">
                                        <form:input path="certificate" id="certificate" class="form-control" tabindex="5" type="text" />
                                        <form:errors path="certificate" cssClass="help-block" element="em"/>
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
                        <c:if test="${not empty entity.baseWeldValveDetail}">
                            <div class="characteristics__body">
                                <table>
                                    <tbody>
                                    <tr>
                                        <td style="text-align: center">
                                            <label class="text-nowrap">В СБОРКЕ:</label>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td style="text-align: center">
                                                ${entity.baseWeldValveDetail.name}: №${entity.baseWeldValveDetail.number}
                                            <form:hidden path="baseWeldValveDetail.name"/>
                                            <form:hidden path="baseWeldValveDetail.number"/>
                                        </td>
                                    </tr>
                                    </tbody>
                                </table>
                            </div>
                        </c:if>
                    </div>
                </div>
                <c:if test="${commonDetailClass == 'CaseBottoms' or commonDetailClass == 'CoverSleeves008'}">
                    <div class="equipment">
                        <div class="equipment-row">
                            <div class="equipment-row__body">
                                <div class="tcp-row__label"><label class="control-label text-nowrap" for="metalMaterial">Материал:</label></div>
                                <form:select path="metalMaterial" id="metalMaterial" class="form-control selectize" placeholder="Поиск материала...">
                                    <form:option style="display:none" value="" label="Поиск материала..."/>
                                    <form:options var="material" items="${metalMaterials}" itemValue="id" />
                                </form:select>
                                <input class="btn btn-primary equipment-row__button" name="openMaterial" type="submit" value="Открыть"/>
                            </div>
                        </div>
                    </div>
                </c:if>
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
                        <c:choose>
                            <c:when test="${not empty entity.reqId}">
                                <c:choose>
                                    <c:when test="${entity.reqName == 'case'}">
                                        <button class="bottom-bar__button back-button" onclick="window.location.href='${contextRoot}/entity/SheetGateValveCases/showFormForUpdate/${entity.reqId}'; return false;">Назад</button>
                                    </c:when>
                                    <c:otherwise>
                                        <button class="bottom-bar__button back-button" onclick="window.location.href='${contextRoot}/entity/SheetGateValveCovers/showFormForUpdate/${entity.reqId}'; return false;">Назад</button>
                                    </c:otherwise>
                                </c:choose>
                            </c:when>
                            <c:otherwise>
                                <button class="bottom-bar__button back-button" onclick="window.location.href='${contextRoot}/entity/${commonDetailClass}/showAll'; return false;">Назад</button>
                            </c:otherwise>
                        </c:choose>
                        <input id="SaveButton" type="submit" name="save" value="Сохранить" class="bottom-bar__button" />
                        <form:hidden path="id"/>
                        <form:hidden path="reqId"/>
                        <form:hidden path="reqName"/>
                        <form:hidden path="name"/>
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