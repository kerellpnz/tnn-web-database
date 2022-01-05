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
        <form:form id="SheetGateValveCasesForm" class="form-horizontal" modelAttribute="entity" autocomplete="off"
                   action="${contextRoot}/entity/SheetGateValveCases/action"
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
                                <tr>
                                    <td class="label-column"><label class="control-label col-md-4" for="dn">DN:</label></td>
                                    <td style="min-width: 150px">
                                        <form:select path="dn" id="dn" class="form-control selectize" placeholder="Выберите DN...">
                                            <form:option style="display:none" value="" label="Выберите DN..."/>
                                            <form:options items="${dns}" />
                                        </form:select>
                                        <form:errors path="dn" cssClass="help-block" element="em"/>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="label-column"><label class="control-label col-md-4" for="pn">PN:</label></td>
                                    <td style="min-width: 150px">
                                        <form:select path="pn" id="pn" class="form-control selectize" placeholder="Выберите PN...">
                                            <form:option style="display:none" value="" label="Выберите PN..."/>
                                            <form:options items="${pns}" />
                                        </form:select>
                                        <form:errors path="pn" cssClass="help-block" element="em"/>
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
                                <tr>
                                    <td class="label-column"><label class="control-label col-md-4" for="pid">PID:</label></td>
                                    <td style="min-width: 150px">
                                        <form:select path="pid" id="pid" class="form-control selectize" placeholder="Поиск PID...">
                                            <form:option style="display:none" value="" label="Поиск PID..."/>
                                            <form:options var="index" items="${pids}" itemValue="id" />
                                        </form:select>
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
                        <c:if test="${not empty entity.sheetGateValve}">
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
                                                ${entity.sheetGateValve.name}: №О${entity.sheetGateValve.number}
                                            <form:hidden path="sheetGateValve.id"/>
                                            <form:hidden path="sheetGateValve.name"/>
                                            <form:hidden path="sheetGateValve.number"/>
                                        </td>
                                    </tr>
                                    </tbody>
                                </table>
                            </div>
                        </c:if>
                    </div>
                </div>
                <div class="equipment">
                    <div class="equipment-row">
                        <table style="width: 50%">
                            <tr>
                                <td><label class="col-md-1" for="flange">Фланец:</label></td>
                                <td style="width: 100%">
                                    <form:select path="flange" id="flange" class="form-control selectize assembly-select" placeholder="Поиск фланца...">
                                        <form:option style="display:none" value="" label="Поиск фланца..."/>
                                        <form:options var="flangeIndex" items="${flanges}" itemValue="id" />
                                    </form:select>
                                </td>
                                <td>
                                    <input class="btn btn-primary equipment-row__button" name="openFlange" type="submit" value="Открыть"
                                           onclick="if (!(confirm('После перехода текущий прогресс будет потерян. Открыть деталь?'))) return false"/>
                                </td>
                            </tr>
                            <tr>
                                <td><label class="col-md-1" for="caseBottom">Днище:</label></td>
                                <td style="width: 100%">
                                    <form:select path="caseBottom" id="caseBottom" class="form-control selectize assembly-select" placeholder="Поиск днища...">
                                        <form:option style="display:none" value="" label="Поиск днища..."/>
                                        <form:options var="caseBottomIndex" items="${caseBottoms}" itemValue="id" />
                                    </form:select>
                                </td>
                                <td>
                                    <input class="btn btn-primary equipment-row__button" name="openCaseBottom" type="submit" value="Открыть"
                                           onclick="if (!(confirm('После перехода текущий прогресс будет потерян. Открыть деталь?'))) return false"/>
                                </td>
                            </tr>
                            <tr>
                                <td><label class="col-md-1" for="coverSleeve008">Втулка дренажная:</label></td>
                                <td style="width: 100%">
                                    <form:select path="coverSleeve008" id="coverSleeve008" class="form-control selectize assembly-select" placeholder="Поиск дренажной втулки...">
                                        <form:option style="display:none" value="" label="Поиск дренажной втулки..."/>
                                        <form:options var="index" items="${coverSleeves008}" itemValue="id" />
                                    </form:select>
                                </td>
                                <td>
                                    <input class="btn btn-primary equipment-row__button" name="openCoverSleeve008" type="submit" value="Открыть"
                                           onclick="if (!(confirm('После перехода текущий прогресс будет потерян. Открыть деталь?'))) return false"/>
                                </td>
                            </tr>
                        </table>
                        <table style="width: 50%">
                            <tr>
                                <td>
                                    <div class="equipment-row__body">
                                        <div class="tcp-row__label"><label class="control-label text-nowrap" for="tempRingId">Кольца:</label></div>
                                        <form:select path="tempRingId" id="tempRingId" class="form-control selectize" placeholder="Поиск колец...">
                                            <form:option style="display:none" value="" label="Поиск колец..."/>
                                            <form:options var="ringIndex" items="${rings}" itemValue="id" />
                                        </form:select>
                                        <input class="btn btn-primary equipment-row__button" name="addRing" type="submit" value="Добавить"/>
                                    </div>
                                    <div style="padding-left: 20px; padding-right: 20px">
                                        <table class="table table-striped table-bordered" style="min-height: 80px">
                                            <c:forEach var="tempObject" items="${entity.rings}" varStatus="s">
                                                <c:url var="openLink" value="/entity/SheetGateValveCases/openRing">
                                                    <c:param name="ringId" value="${tempObject.id}" />
                                                    <c:param name="caseId" value="${entity.id}" />
                                                </c:url>
                                                <tr>
                                                    <td>
                                                            ${tempObject}
                                                        <form:hidden path="rings[${s.index}].id"/>
                                                        <form:hidden path="rings[${s.index}].number"/>
                                                        <form:hidden path="rings[${s.index}].zk"/>
                                                        <form:hidden path="rings[${s.index}].melt"/>
                                                        <form:hidden path="rings[${s.index}].dn"/>
                                                        <form:hidden path="rings[${s.index}].drawing"/>
                                                        <form:hidden path="rings[${s.index}].status"/>
                                                    </td>
                                                    <td style="text-align: center"><a href="${openLink}"
                                                                                      onclick="if (!(confirm('После перехода текущий прогресс будет потерян. Открыть деталь?'))) return false">
                                                        <span class="glyphicon glyphicon-open"></span></a></td>
                                                    <td style="text-align: center"><input class="btn btn-danger" name="deleteRing" type="submit" value="${s.index}"/></td>
                                                </tr>
                                            </c:forEach>
                                        </table>
                                    </div>
                                </td>
                            </tr>
                        </table>
                    </div>
                </div>
                <div class="content-edit-page">
                    <div class="tab-control">
                        <ul class="nav nav-tabs" id="operations">
                            <li class="active">
                                <a href="#inputControl" class="tab-style">Крестовина:Вх/Толщина</a>
                            </li>
                            <li>
                                <a href="#weld" class="tab-style">СВАРКА</a>
                            </li>
                            <li>
                                <a href="#mechanical" class="tab-style">Корпус:ВИК/Толщина/Промывка</a>
                            </li>
                            <li>
                                <a href="#document" class="tab-style">ДОКУМЕНТЫ</a>
                            </li>
                        </ul>
                        <div class="tab-content">
                            <div class="tab-pane active" id="inputControl">
                                <%@include file="../journal/inputControl-journal-view.jsp"%>
                            </div>
                            <div class="tab-pane" id="weld">
                                <%@include file="../journal/weld-journal-view.jsp"%>
                            </div>
                            <div class="tab-pane" id="mechanical">
                                <%@include file="../journal/mechanical-journal-view.jsp"%>
                            </div>
                            <div class="tab-pane" id="document">
                                <%@include file="../journal/document-journal-view.jsp"%>
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
                        <c:choose>
                            <c:when test="${not empty entity.reqId}">
                                <button class="bottom-bar__button back-button" onclick="window.location.href='${contextRoot}/entity/SheetGateValves/showFormForUpdate/${entity.reqId}'; return false;">Назад</button>
                            </c:when>
                            <c:otherwise>
                                <button class="bottom-bar__button back-button" onclick="window.location.href='${contextRoot}/entity/SheetGateValveCases/showAll'; return false;">Назад</button>
                            </c:otherwise>
                        </c:choose>
                        <input id="SaveButton" type="submit" name="save" value="Сохранить" class="bottom-bar__button" />
                        <form:hidden path="id"/>
                        <form:hidden path="reqId"/>
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