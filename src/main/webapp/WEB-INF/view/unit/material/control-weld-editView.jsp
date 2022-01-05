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
                   action="${contextRoot}/entity/ControlWelds/action"
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
                    <div class="characteristics__header header">Характеристики КСС</div>
                    <div class="characteristics__row">
                        <div class="characteristics__body">
                            <table class="characteristics__table table">
                                <tbody>
                                <tr>
                                    <td class="label-column"><label class="control-label col-md-4" for="number">Номер:</label></td>
                                    <td>
                                        <form:input path="number" id="number" class="form-control" tabindex="1" type="text" />
                                        <form:errors path="number" cssClass="help-block" element="em"/>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="label-column"><label class="control-label col-md-4" for="weldingMethod">Способ сварки:</label></td>
                                    <td>
                                        <form:input path="weldingMethod" id="weldingMethod" class="form-control" tabindex="2" type="text" list="weldingMethods"/>
                                        <c:if test = "${not empty weldingMethods}">
                                            <datalist id="weldingMethods">
                                                <c:forEach var="index" items="${weldingMethods}">
                                                    <option value="${index}"/>
                                                </c:forEach>
                                            </datalist>
                                        </c:if>
                                        <form:errors path="weldingMethod" cssClass="help-block" element="em"/>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="label-column"><label class="control-label col-md-4" for="welder">Сварщик:</label></td>
                                    <td>
                                        <form:input path="welder" id="welder" class="form-control" tabindex="2" type="text" list="welders"/>
                                        <c:if test = "${not empty welders}">
                                            <datalist id="welders">
                                                <c:forEach var="index" items="${welders}">
                                                    <option value="${index}"/>
                                                </c:forEach>
                                            </datalist>
                                        </c:if>
                                        <form:errors path="welder" cssClass="help-block" element="em"/>
                                    </td>
                                </tr>
                                </tbody>
                            </table>
                        </div>
                        <div class="characteristics__body">
                            <table class="characteristics__table table">
                                <tbody>
                                <tr>
                                    <td class="label-column"><label class="control-label col-md-4 text-nowrap" for="firstMaterial">Материал 1:</label></td>
                                    <td>
                                        <form:input path="firstMaterial" id="firstMaterial" class="form-control" tabindex="2" type="text" list="firstMaterials"/>
                                        <c:if test = "${not empty firstMaterials}">
                                            <datalist id="firstMaterials">
                                                <c:forEach var="materialIndex" items="${firstMaterials}">
                                                    <option value="${materialIndex}"/>
                                                </c:forEach>
                                            </datalist>
                                        </c:if>
                                        <form:errors path="firstMaterial" cssClass="help-block" element="em"/>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="label-column"><label class="control-label col-md-4 text-nowrap" for="secondMaterial">Материал 2:</label></td>
                                    <td>
                                        <form:input path="secondMaterial" id="secondMaterial" class="form-control" tabindex="2" type="text" list="secondMaterials"/>
                                        <c:if test = "${not empty secondMaterials}">
                                            <datalist id="secondMaterials">
                                                <c:forEach var="materialIndex" items="${secondMaterials}">
                                                    <option value="${materialIndex}"/>
                                                </c:forEach>
                                            </datalist>
                                        </c:if>
                                        <form:errors path="secondMaterial" cssClass="help-block" element="em"/>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="label-column"><label class="control-label col-md-4" for="stamp">Клеймо:</label></td>
                                    <td>
                                        <form:input path="stamp" id="stamp" class="form-control" tabindex="2" type="text" list="stamps"/>
                                        <c:if test = "${not empty stamps}">
                                            <datalist id="stamps">
                                                <c:forEach var="index" items="${stamps}">
                                                    <option value="${index}"/>
                                                </c:forEach>
                                            </datalist>
                                        </c:if>
                                        <form:errors path="stamp" cssClass="help-block" element="em"/>
                                    </td>
                                </tr>
                                </tbody>
                            </table>
                        </div>
                        <div class="characteristics__body">
                            <table class="characteristics__table table">
                                <tbody>
                                <tr>
                                    <td class="label-column"><label class="control-label col-md-4" for="mechanicalPropertiesReport">Протокол (механика):</label></td>
                                    <td>
                                        <form:input path="mechanicalPropertiesReport" id="mechanicalPropertiesReport" class="form-control" tabindex="5" type="text" />
                                        <form:errors path="mechanicalPropertiesReport" cssClass="help-block" element="em"/>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="label-column"><label class="control-label col-md-4" for="metallographicPropertiesReport">Протокол (металлография):</label></td>
                                    <td>
                                        <form:input path="metallographicPropertiesReport" id="metallographicPropertiesReport" class="form-control" tabindex="5" type="text" />
                                        <form:errors path="metallographicPropertiesReport" cssClass="help-block" element="em"/>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="label-column"><label class="control-label col-md-4" for="size">Диаметр/толщина:</label></td>
                                    <td>
                                        <form:input path="size" id="size" class="form-control" tabindex="5" type="text" />
                                        <form:errors path="size" cssClass="help-block" element="em"/>
                                    </td>
                                </tr>
                                </tbody>
                            </table>
                        </div>
                        <div class="characteristics__body">
                            <table class="characteristics__table table">
                                <tbody>
                                <tr>
                                    <td class="label-column"><label class="control-label col-md-4 text-nowrap" for="beginingDate">Дата протокола:</label></td>
                                    <td>
                                        <form:input path="beginingDate" class="form-control date-picker" />
                                        <form:errors path="beginingDate" cssClass="help-block" element="em"/>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="label-column"><label class="control-label col-md-4 text-nowrap" for="expiryDate">Срок действия:</label></td>
                                    <td>
                                        <form:input path="expiryDate" class="form-control date-picker" />
                                        <form:errors path="expiryDate" cssClass="help-block" element="em"/>
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
                        <button class="bottom-bar__button back-button" onclick="window.location.href='${contextRoot}/entity/ControlWelds/showAll'; return false;">Назад</button>
                        <input id="SaveButton" type="submit" name="save" value="Сохранить" class="bottom-bar__button" />
                        <form:hidden path="id"/>
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