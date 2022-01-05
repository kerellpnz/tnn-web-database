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
        <form:form id="sheetGateValveForm" class="form-horizontal" modelAttribute="entity" autocomplete="off"
                   action="${contextRoot}/entity/SheetGateValves/action"
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
                    <div class="characteristics__header header">Характеристики ЗШ</div>
                    <div class="characteristics__row">
                        <div class="characteristics__body">
                            <table class="table">
                                <tbody>
                                <tr>
                                    <td class="label-column"><label class="control-label col-md-4" for="number">Номер:</label></td>
                                    <td style="min-width: 200px">
                                        <form:input path="number" id="number" class="form-control" tabindex="1" type="text" />
                                        <form:errors path="number" cssClass="help-block" element="em"/>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="label-column"><label class="control-label col-md-4" for="drawing">Чертеж:</label></td>
                                    <td style="min-width: 250px">
                                        <form:input path="drawing" id="drawing" class="form-control" placeholder="ПТ 19005-500М1" tabindex="2" type="text" list="drawings"/>
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
                                    <td class="label-column"><label class="control-label col-md-4 text-nowrap" for="gatePlace">Положение шибера О/З:</label></td>
                                    <td style="min-width: 200px">
                                        <form:input path="gatePlace" id="gatePlace" class="form-control" tabindex="3" type="text" />
                                        <form:errors path="gatePlace" cssClass="help-block" element="em"/>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="label-column"><label class="control-label col-md-4 text-nowrap" for="time">Время хода,с:</label></td>
                                    <td style="min-width: 200px">
                                        <form:input path="time" id="time" class="form-control" tabindex="4" type="text" />
                                        <form:errors path="time" cssClass="help-block" element="em"/>
                                    </td>
                                </tr>
                                </tbody>
                            </table>
                        </div>
                        <div class="characteristics__body">
                            <table class="table">
                                <tbody>
                                <tr>
                                    <td class="label-column"><label class="control-label col-md-4" for="pid">PID:</label></td>
                                    <td style="min-width: 150px">
                                        <form:select path="pid" id="pid" class="form-control selectize" placeholder="Поиск PID...">
                                            <form:option style="display:none" value="" label="Поиск PID..."/>
                                            <form:options var="index" items="${pids}" itemValue="id" />
                                        </form:select>
                                    </td>
                                    <td>
                                        <input class="btn btn-primary equipment-row__button" name="openPID" type="submit" value="Открыть"
                                               onclick="if (!(confirm('После перехода текущий прогресс будет потерян. Открыть PID?'))) return false"/>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="label-column"><label class="col-md-4">Спецификация:</label></td>
                                    <td class="label-column ">
                                        <span>${entity.specNumber}</span>
                                        <form:hidden path="specNumber"/>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="label-column"><label class="control-label col-md-4 text-nowrap" for="moment">Момент Л/П:</label></td>
                                    <td style="min-width: 150px">
                                        <form:input path="moment" id="moment" class="form-control" tabindex="5" type="text" />
                                        <form:errors path="moment" cssClass="help-block" element="em"/>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="label-column"><label class="control-label col-md-4 text-nowrap" for="automaticReset">Автосброс О/З:</label></td>
                                    <td style="min-width: 150px">
                                        <form:input path="automaticReset" id="automaticReset" class="form-control" tabindex="6" type="text" />
                                        <form:errors path="automaticReset" cssClass="help-block" element="em"/>
                                    </td>
                                </tr>
                                </tbody>
                            </table>
                        </div>
                        <div class="characteristics__body">
                            <table class="table">
                                <tbody>
                                <tr>
                                    <td class="label-column"><label class="col-md-4">Обозначение:</label></td>
                                    <td class="label-column ">
                                        <span>${entity.pid.designation}</span>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="label-column"><label class="control-label col-md-4" for="earTest">Испытание ушей:</label></td>
                                    <td>
                                        <form:input path="earTest" class="form-control date-picker" />
                                        <form:errors path="earTest" cssClass="help-block" element="em"/>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="label-column"><label class="control-label col-md-4 text-nowrap" for="zip">ЗИП:</label></td>
                                    <td>
                                        <form:input path="zip" class="form-control date-picker" />
                                        <form:errors path="zip" cssClass="help-block" element="em"/>
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
                        <ul class="nav nav-tabs" id="operations">
                            <li class="active">
                                <a href="#equipment" class="tab-style">КОМПЛЕКТАЦИЯ</a>
                            </li>
                            <li>
                                <a href="#assembly" class="tab-style">СБОРКА</a>
                            </li>
                            <li>
                                <a href="#mechanical" class="tab-style">ПСИ</a>
                            </li>
                            <li>
                                <a href="#inputControl" class="tab-style">ВИК после ПСИ</a>
                            </li>
                            <li>
                                <a href="#weld" class="tab-style">СВАРКА</a>
                            </li>
                            <li>
                                <a href="#coating" class="tab-style">АКП</a>
                            </li>
                            <li>
                                <a href="#document" class="tab-style">ОТГРУЗКА</a>
                            </li>
                        </ul>
                        <div class="tab-content">
                            <div class="tab-pane active" id="equipment">
                                <div class="equipment-valve">
                                    <div class="equipment-valve__row">
                                        <table style="width: 33%">
                                            <tr>
                                                <td>
                                                    <table>
                                                        <tr>
                                                            <td><label class="col-md-1" for="valveCase">Корпус:</label></td>
                                                            <td style="width: 100%">
                                                                <form:select path="valveCase" id="valveCase" class="form-control selectize assembly-select" placeholder="Поиск копуса...">
                                                                    <form:option style="display:none" value="" label="Поиск копуса..."/>
                                                                    <form:options var="index" items="${cases}" itemValue="id" />
                                                                </form:select>
                                                            </td>
                                                            <td>
                                                                <input class="btn btn-primary equipment-row__button" name="openCase" type="submit" value="Открыть"
                                                                       onclick="if (!(confirm('После перехода весь несохраненный прогресс будет потерян. Открыть деталь?'))) return false"/>
                                                            </td>
                                                        </tr>
                                                        <tr>
                                                            <td><label class="col-md-1" for="tempSaddleId">Обойма:</label></td>
                                                            <td style="width: 100%">
                                                                <form:select path="tempSaddleId" id="tempSaddleId" class="form-control selectize assembly-select" placeholder="Поиск обойм...">
                                                                    <form:option style="display:none" value="" label="Поиск обойм..."/>
                                                                    <form:options var="index" items="${saddles}" itemValue="id" />
                                                                </form:select>
                                                            </td>
                                                            <td>
                                                                <input class="btn btn-primary equipment-row__button" name="addSaddle" type="submit" value="Добавить"/>
                                                            </td>
                                                        </tr>
                                                    </table>
                                                </td>
                                            </tr>
                                            <tr>
                                                <td>
                                                    <table class="table table-striped table-bordered" style="min-height: 80px">
                                                        <c:forEach var="tempObject" items="${entity.saddles}" varStatus="s">
                                                            <c:url var="openLink" value="/entity/SheetGateValves/openSaddle">
                                                                <c:param name="saddleId" value="${tempObject.id}" />
                                                                <c:param name="valveId" value="${entity.id}" />
                                                            </c:url>
                                                            <tr>
                                                                <td style="font-size: 12px">
                                                                        ${tempObject}
                                                                    <form:hidden path="saddles[${s.index}].id"/>
                                                                    <form:hidden path="saddles[${s.index}].number"/>
                                                                    <form:hidden path="saddles[${s.index}].zk"/>
                                                                    <form:hidden path="saddles[${s.index}].melt"/>
                                                                    <form:hidden path="saddles[${s.index}].dn"/>
                                                                    <form:hidden path="saddles[${s.index}].drawing"/>
                                                                    <form:hidden path="saddles[${s.index}].status"/>
                                                                </td>
                                                                <td style="text-align: center"><a href="${openLink}"
                                                                    onclick="if (!(confirm('После перехода весь несохраненный прогресс будет потерян. Открыть деталь?'))) return false">
                                                                    <span class="glyphicon glyphicon-open"></span></a></td>
                                                                <td style="text-align: center"><input class="btn btn-danger" name="deleteSaddle" type="submit" value="${s.index}"/></td>
                                                            </tr>
                                                        </c:forEach>
                                                    </table>
                                                </td>
                                            </tr>
                                        </table>
                                        <table style="width: 34%">
                                            <tr>
                                                <td>
                                                    <table>
                                                        <tr>
                                                            <td><label class="col-md-1" for="valveCover">Крышка:</label></td>
                                                            <td style="width: 100%">
                                                                <form:select path="valveCover" id="valveCover" class="form-control selectize assembly-select" placeholder="Поиск крышки...">
                                                                    <form:option style="display:none" value="" label="Поиск крышки..."/>
                                                                    <form:options var="index" items="${covers}" itemValue="id" />
                                                                </form:select>
                                                            </td>
                                                            <td>
                                                                <input class="btn btn-primary equipment-row__button" name="openCover" type="submit" value="Открыть"
                                                                       onclick="if (!(confirm('После перехода весь несохраненный прогресс будет потерян. Открыть деталь?'))) return false"/>
                                                            </td>
                                                        </tr>
                                                        <tr>
                                                            <td><label class="col-md-1" for="tempSpringId">Пружина:</label></td>
                                                            <td style="width: 100%">
                                                                <form:select path="tempSpringId" id="tempSpringId" class="form-control selectize assembly-select" placeholder="Поиск пружин...">
                                                                    <form:option style="display:none" value="" label="Поиск пружин..."/>
                                                                    <form:options var="index" items="${springs}" itemValue="id" />
                                                                </form:select>
                                                            </td>
                                                            <td>
                                                                <button type="button" class="btn btn-primary equipment-row__button" data-toggle="modal" data-target="#addSpring">Добавить</button>
                                                            </td>
                                                        </tr>
                                                    </table>
                                                </td>
                                            </tr>
                                            <tr>
                                                <td style="padding-left: 5px">
                                                    <table class="table table-striped table-bordered" style="min-height: 80px">
                                                        <c:forEach var="tempObject" items="${entity.baseValveWithSprings}" varStatus="s">
                                                            <c:url var="openLink" value="/entity/SheetGateValves/openSpring">
                                                                <c:param name="springId" value="${tempObject.spring.id}" />
                                                            </c:url>
                                                            <tr>
                                                                <td style="font-size: 12px">
                                                                        ${tempObject.spring}
                                                                    <form:hidden path="baseValveWithSprings[${s.index}].spring.id"/>
                                                                    <form:hidden path="baseValveWithSprings[${s.index}].spring.certificate"/>
                                                                    <form:hidden path="baseValveWithSprings[${s.index}].spring.batch"/>
                                                                    <form:hidden path="baseValveWithSprings[${s.index}].spring.dn"/>
                                                                    <form:hidden path="baseValveWithSprings[${s.index}].spring.status"/>
                                                                </td>
                                                                <td class="text-nowrap" style="font-size: 12px">
                                                                        ${tempObject.springAmount} шт.
                                                                    <form:hidden path="baseValveWithSprings[${s.index}].springAmount"/>
                                                                </td>
                                                                <td style="text-align: center"><a href="${openLink}"
                                                                    onclick="if (!(confirm('После перехода весь несохраненный прогресс будет потерян. Открыть деталь?'))) return false">
                                                                    <span class="glyphicon glyphicon-open"></span></a></td>
                                                                <td style="text-align: center"><input class="btn btn-danger" name="deleteSpring" type="submit" value="${s.index}"/></td>
                                                                <form:hidden path="baseValveWithSprings[${s.index}].id"/>
                                                            </tr>
                                                        </c:forEach>
                                                    </table>
                                                </td>
                                            </tr>
                                        </table>
                                        <table style="width: 33%">
                                            <tr>
                                                <td>
                                                    <table>
                                                        <tr>
                                                            <td><label class="col-md-1" for="gate">Шибер:</label></td>
                                                            <td style="width: 100%">
                                                                <form:select path="gate" id="gate" class="form-control selectize assembly-select" placeholder="Поиск шибера...">
                                                                    <form:option style="display:none" value="" label="Поиск шибера..."/>
                                                                    <form:options var="index" items="${gates}" itemValue="id" />
                                                                </form:select>
                                                            </td>
                                                            <td>
                                                                <input class="btn btn-primary equipment-row__button" name="openGate" type="submit" value="Открыть"
                                                                       onclick="if (!(confirm('После перехода весь несохраненный прогресс будет потерян. Открыть деталь?'))) return false"/>
                                                            </td>
                                                        </tr>
                                                        <tr>
                                                            <td><label class="col-md-1" for="tempShearPinId">Штифты:</label></td>
                                                            <td style="width: 100%">
                                                                <form:select path="tempShearPinId" id="tempShearPinId" class="form-control selectize assembly-select" placeholder="Поиск штифтов...">
                                                                    <form:option style="display:none" value="" label="Поиск штифтов..."/>
                                                                    <form:options var="index" items="${shearPins}" itemValue="id" />
                                                                </form:select>
                                                            </td>
                                                            <td>
                                                                <button type="button" class="btn btn-primary equipment-row__button" data-toggle="modal" data-target="#addShearPin">Добавить</button>
                                                            </td>
                                                        </tr>
                                                    </table>
                                                </td>
                                            </tr>
                                            <tr>
                                                <td style="padding-left: 5px">
                                                    <table class="table table-striped table-bordered" style="min-height: 80px">
                                                        <c:forEach var="tempObject" items="${entity.baseValveWithShearPins}" varStatus="s">
                                                            <c:url var="openLink" value="/entity/SheetGateValves/openShearPin">
                                                                <c:param name="shearPinId" value="${tempObject.shearPin.id}" />
                                                            </c:url>
                                                            <tr>
                                                                <td style="font-size: 12px">
                                                                        ${tempObject.shearPin}
                                                                    <form:hidden path="baseValveWithShearPins[${s.index}].shearPin.id"/>
                                                                    <form:hidden path="baseValveWithShearPins[${s.index}].shearPin.number"/>
                                                                    <form:hidden path="baseValveWithShearPins[${s.index}].shearPin.diameter"/>
                                                                    <form:hidden path="baseValveWithShearPins[${s.index}].shearPin.pull"/>
                                                                    <form:hidden path="baseValveWithShearPins[${s.index}].shearPin.tensileStrength"/>
                                                                    <form:hidden path="baseValveWithShearPins[${s.index}].shearPin.dn"/>
                                                                    <form:hidden path="baseValveWithShearPins[${s.index}].shearPin.status"/>
                                                                </td>
                                                                <td class="text-nowrap" style="font-size: 12px">
                                                                        ${tempObject.shearPinAmount} шт.
                                                                    <form:hidden path="baseValveWithShearPins[${s.index}].shearPinAmount"/>
                                                                </td>
                                                                <td style="text-align: center"><a href="${openLink}"
                                                                    onclick="if (!(confirm('После перехода весь несохраненный прогресс будет потерян. Открыть деталь?'))) return false">
                                                                    <span class="glyphicon glyphicon-open"></span></a></td>
                                                                <td style="text-align: center"><input class="btn btn-danger" name="deleteShearPin" type="submit" value="${s.index}"/></td>
                                                                <form:hidden path="baseValveWithShearPins[${s.index}].id"/>
                                                            </tr>
                                                        </c:forEach>
                                                    </table>
                                                </td>
                                            </tr>
                                        </table>
                                    </div>
                                </div>
                                <div class="equipment-valve">
                                    <div class="equipment-valve__row">
                                        <table style="width: 33%">
                                            <tr>
                                                <td>
                                                    <table>
                                                        <tr>
                                                            <td><label class="col-md-1" for="tempScrewStudId">Шпилька:</label></td>
                                                            <td style="width: 100%">
                                                                <form:select path="tempScrewStudId" id="tempScrewStudId" class="form-control selectize assembly-select" placeholder="Поиск шпилек...">
                                                                    <form:option style="display:none" value="" label="Поиск шпилек..."/>
                                                                    <form:options var="index" items="${screwStuds}" itemValue="id" />
                                                                </form:select>
                                                            </td>
                                                            <td>
                                                                <button type="button" class="btn btn-primary equipment-row__button" data-toggle="modal" data-target="#addScrewStud">Добавить</button>
                                                            </td>
                                                        </tr>
                                                    </table>
                                                </td>
                                            </tr>
                                            <tr>
                                                <td>
                                                    <table class="table table-striped table-bordered" style="min-height: 80px">
                                                        <c:forEach var="tempObject" items="${entity.baseValveWithScrewStuds}" varStatus="s">
                                                            <c:url var="openLink" value="/entity/SheetGateValves/openScrewStud">
                                                                <c:param name="screwStudId" value="${tempObject.screwStud.id}" />
                                                            </c:url>
                                                            <tr>
                                                                <td style="font-size: 12px">
                                                                        ${tempObject.screwStud}
                                                                    <form:hidden path="baseValveWithScrewStuds[${s.index}].screwStud.id"/>
                                                                    <form:hidden path="baseValveWithScrewStuds[${s.index}].screwStud.certificate"/>
                                                                    <form:hidden path="baseValveWithScrewStuds[${s.index}].screwStud.batch"/>
                                                                    <form:hidden path="baseValveWithScrewStuds[${s.index}].screwStud.dn"/>
                                                                    <form:hidden path="baseValveWithScrewStuds[${s.index}].screwStud.status"/>
                                                                </td>
                                                                <td class="text-nowrap" style="font-size: 12px">
                                                                        ${tempObject.screwStudAmount} шт.
                                                                    <form:hidden path="baseValveWithScrewStuds[${s.index}].screwStudAmount"/>
                                                                </td>
                                                                <td style="text-align: center"><a href="${openLink}"
                                                                    onclick="if (!(confirm('После перехода весь несохраненный прогресс будет потерян. Открыть деталь?'))) return false">
                                                                    <span class="glyphicon glyphicon-open"></span></a></td>
                                                                <td style="text-align: center"><input class="btn btn-danger" name="deleteScrewStud" type="submit" value="${s.index}"/></td>
                                                                <form:hidden path="baseValveWithScrewStuds[${s.index}].id"/>
                                                            </tr>
                                                        </c:forEach>
                                                    </table>
                                                </td>
                                            </tr>
                                        </table>
                                        <table style="width: 34%">
                                            <tr>
                                                <td>
                                                    <table>
                                                        <tr>
                                                            <td><label class="col-md-1" for="tempScrewNutId">Гайка:</label></td>
                                                            <td style="width: 100%">
                                                                <form:select path="tempScrewNutId" id="tempScrewNutId" class="form-control selectize assembly-select" placeholder="Поиск гаек...">
                                                                    <form:option style="display:none" value="" label="Поиск гаек..."/>
                                                                    <form:options var="index" items="${screwNuts}" itemValue="id" />
                                                                </form:select>
                                                            </td>
                                                            <td>
                                                                <button type="button" class="btn btn-primary equipment-row__button" data-toggle="modal" data-target="#addScrewNut">Добавить</button>
                                                            </td>
                                                        </tr>
                                                    </table>
                                                </td>
                                            </tr>
                                            <tr>
                                                <td style="padding-left: 5px">
                                                    <table class="table table-striped table-bordered" style="min-height: 80px">
                                                        <c:forEach var="tempObject" items="${entity.baseValveWithScrewNuts}" varStatus="s">
                                                            <c:url var="openLink" value="/entity/SheetGateValves/openScrewNut">
                                                                <c:param name="screwNutId" value="${tempObject.screwNut.id}" />
                                                            </c:url>
                                                            <tr>
                                                                <td style="font-size: 12px">
                                                                        ${tempObject.screwNut}
                                                                    <form:hidden path="baseValveWithScrewNuts[${s.index}].screwNut.id"/>
                                                                    <form:hidden path="baseValveWithScrewNuts[${s.index}].screwNut.certificate"/>
                                                                    <form:hidden path="baseValveWithScrewNuts[${s.index}].screwNut.batch"/>
                                                                    <form:hidden path="baseValveWithScrewNuts[${s.index}].screwNut.dn"/>
                                                                    <form:hidden path="baseValveWithScrewNuts[${s.index}].screwNut.status"/>
                                                                </td>
                                                                <td class="text-nowrap" style="font-size: 12px">
                                                                        ${tempObject.screwNutAmount} шт.
                                                                    <form:hidden path="baseValveWithScrewNuts[${s.index}].screwNutAmount"/>
                                                                </td>
                                                                <td style="text-align: center"><a href="${openLink}"
                                                                    onclick="if (!(confirm('После перехода весь несохраненный прогресс будет потерян. Открыть деталь?'))) return false">
                                                                    <span class="glyphicon glyphicon-open"></span></a></td>
                                                                <td style="text-align: center"><input class="btn btn-danger" name="deleteScrewNut" type="submit" value="${s.index}"/></td>
                                                                <form:hidden path="baseValveWithScrewNuts[${s.index}].id"/>
                                                            </tr>
                                                        </c:forEach>
                                                    </table>
                                                </td>
                                            </tr>
                                        </table>
                                        <table style="width: 33%">
                                            <tr>
                                                <td>
                                                    <table style="width: 100%">
                                                        <tr>
                                                            <td style="text-align: center">
                                                                <input class="btn btn-primary" name="checkBeforeCreateExcel" type="submit" value="ЗАГРУЗИТЬ БЛАНК ПРОВЕРКИ ПАСПОРТА"/>
                                                            </td>
                                                        </tr>
                                                    </table>
                                                </td>
                                            </tr>
                                            <tr>
                                                <td style="padding-left: 5px">
                                                    <table class="table table-striped table-bordered" style="min-height: 80px">

                                                    </table>
                                                </td>
                                            </tr>
                                        </table>
                                    </div>
                                </div>
                                <div class="equipment-valve">
                                    <div class="equipment-valve__row">
                                        <table style="width: 33%">
                                            <tr>
                                                <td>
                                                    <table>
                                                        <tr>
                                                            <td><label class="col-md-1 text-nowrap" for="tempSealId">Уплотнения О.Р.:</label></td>
                                                            <td style="width: 100%">
                                                                <form:select path="tempSealId" id="tempSealId" class="form-control selectize assembly-select" placeholder="Поиск уплотнений...">
                                                                    <form:option style="display:none" value="" label="Поиск уплотнений..."/>
                                                                    <form:options var="index" items="${mainFlangeSeals}" itemValue="id" />
                                                                </form:select>
                                                            </td>
                                                            <td>
                                                                <input class="btn btn-primary equipment-row__button" name="addSeal" type="submit" value="Добавить"/>
                                                            </td>
                                                        </tr>
                                                    </table>
                                                </td>
                                            </tr>
                                            <tr>
                                                <td>
                                                    <table class="table table-striped table-bordered" style="min-height: 80px">
                                                        <c:forEach var="tempObject" items="${entity.mainFlangeSeals}" varStatus="s">
                                                            <c:url var="openLink" value="/entity/SheetGateValves/openSeal">
                                                                <c:param name="sealId" value="${tempObject.id}" />
                                                            </c:url>
                                                            <tr>
                                                                <td style="font-size: 12px">
                                                                        ${tempObject}
                                                                    <form:hidden path="mainFlangeSeals[${s.index}].id"/>
                                                                    <form:hidden path="mainFlangeSeals[${s.index}].name"/>
                                                                    <form:hidden path="mainFlangeSeals[${s.index}].certificate"/>
                                                                    <form:hidden path="mainFlangeSeals[${s.index}].batch"/>
                                                                    <form:hidden path="mainFlangeSeals[${s.index}].drawing"/>
                                                                    <form:hidden path="mainFlangeSeals[${s.index}].status"/>
                                                                </td>
                                                                <td style="text-align: center"><a href="${openLink}"
                                                                    onclick="if (!(confirm('После перехода весь несохраненный прогресс будет потерян. Открыть деталь?'))) return false">
                                                                    <span class="glyphicon glyphicon-open"></span></a></td>
                                                                <td style="text-align: center"><input class="btn btn-danger" name="deleteSeal" type="submit" value="${s.index}"/></td>
                                                            </tr>
                                                        </c:forEach>
                                                    </table>
                                                </td>
                                            </tr>
                                        </table>
                                        <table style="width: 34%">
                                            <tr>
                                                <td>
                                                    <table>
                                                        <tr>
                                                            <td><label class="col-md-1" for="tempNozzleId">Катушка:</label></td>
                                                            <td style="width: 100%">
                                                                <form:select path="tempNozzleId" id="tempNozzleId" class="form-control selectize assembly-select" placeholder="Поиск катушки...">
                                                                    <form:option style="display:none" value="" label="Поиск катушки..."/>
                                                                    <form:options var="index" items="${nozzles}" itemValue="id" />
                                                                </form:select>
                                                            </td>
                                                            <td>
                                                                <input class="btn btn-primary equipment-row__button" name="addNozzle" type="submit" value="Добавить"/>
                                                            </td>
                                                        </tr>
                                                    </table>
                                                </td>
                                            </tr>
                                            <tr>
                                                <td style="padding-left: 5px">
                                                    <table class="table table-striped table-bordered" style="min-height: 80px">
                                                        <c:forEach var="tempObject" items="${entity.nozzles}" varStatus="s">
                                                            <c:url var="openLink" value="/entity/SheetGateValves/openNozzle">
                                                                <c:param name="nozzleId" value="${tempObject.id}" />
                                                                <c:param name="valveId" value="${entity.id}" />
                                                            </c:url>
                                                            <tr>
                                                                <td style="font-size: 12px">
                                                                        ${tempObject}
                                                                    <form:hidden path="nozzles[${s.index}].id"/>
                                                                    <form:hidden path="nozzles[${s.index}].number"/>
                                                                    <form:hidden path="nozzles[${s.index}].zk"/>
                                                                    <form:hidden path="nozzles[${s.index}].melt"/>
                                                                    <form:hidden path="nozzles[${s.index}].dn"/>
                                                                    <form:hidden path="nozzles[${s.index}].drawing"/>
                                                                    <form:hidden path="nozzles[${s.index}].status"/>
                                                                </td>
                                                                <td style="text-align: center"><a href="${openLink}"
                                                                    onclick="if (!(confirm('После перехода весь несохраненный прогресс будет потерян. Открыть деталь?'))) return false">
                                                                    <span class="glyphicon glyphicon-open"></span></a></td>
                                                                <td style="text-align: center"><input class="btn btn-danger" name="deleteNozzle" type="submit" value="${s.index}"/></td>
                                                            </tr>
                                                        </c:forEach>
                                                    </table>
                                                </td>
                                            </tr>
                                        </table>
                                        <table style="width: 33%">
                                            <tr>
                                                <td>
                                                    <table>

                                                    </table>
                                                </td>
                                            </tr>
                                            <tr>
                                                <td style="padding-left: 5px">
                                                    <table class="table table-striped table-bordered" style="min-height: 80px">

                                                    </table>
                                                </td>
                                            </tr>
                                        </table>
                                    </div>
                                </div>
                                <div class="equipment-valve">
                                    <div class="equipment-valve__row">
                                        <table style="width: 33%">
                                            <tr>
                                                <td>
                                                    <table>
                                                        <tr>
                                                            <td><label class="col-md-1 text-nowrap" for="tempCoatingId">АКП:</label></td>
                                                            <td style="width: 100%">
                                                                <form:select path="tempCoatingId" id="tempCoatingId" class="form-control selectize assembly-select" placeholder="Поиск АКП...">
                                                                    <form:option style="display:none" value="" label="Поиск АКП..."/>
                                                                    <form:options var="index" items="${baseAnticorrosiveCoatings}" itemValue="id" />
                                                                </form:select>
                                                            </td>
                                                            <td>
                                                                <input class="btn btn-primary equipment-row__button" name="addCoating" type="submit" value="Добавить"/>
                                                            </td>
                                                        </tr>
                                                    </table>
                                                </td>
                                            </tr>
                                            <tr>
                                                <td>
                                                    <table class="table table-striped table-bordered" style="min-height: 80px">
                                                        <c:forEach var="tempObject" items="${entity.baseAnticorrosiveCoatings}" varStatus="s">
                                                            <c:url var="openLink" value="/entity/SheetGateValves/openCoating">
                                                                <c:param name="coatingId" value="${tempObject.id}" />
                                                            </c:url>
                                                            <tr>
                                                                <td style="font-size: 12px">
                                                                        ${tempObject}
                                                                    <form:hidden path="baseAnticorrosiveCoatings[${s.index}].id"/>
                                                                    <form:hidden path="baseAnticorrosiveCoatings[${s.index}].name"/>
                                                                    <form:hidden path="baseAnticorrosiveCoatings[${s.index}].batch"/>
                                                                    <form:hidden path="baseAnticorrosiveCoatings[${s.index}].status"/>
                                                                </td>
                                                                <td style="text-align: center"><a href="${openLink}"
                                                                    onclick="if (!(confirm('После перехода весь несохраненный прогресс будет потерян. Открыть деталь?'))) return false">
                                                                    <span class="glyphicon glyphicon-open"></span></a></td>
                                                                <td style="text-align: center"><input class="btn btn-danger" name="deleteCoating" type="submit" value="${s.index}"/></td>
                                                            </tr>
                                                        </c:forEach>
                                                    </table>
                                                </td>
                                            </tr>
                                        </table>
                                        <table style="width: 34%">
                                            <tr>
                                                <td>
                                                    <table>

                                                    </table>
                                                </td>
                                            </tr>
                                            <tr>
                                                <td style="padding-left: 5px">
                                                    <table class="table table-striped table-bordered" style="min-height: 80px">

                                                    </table>
                                                </td>
                                            </tr>
                                        </table>
                                        <table style="width: 33%">
                                            <tr>
                                                <td>
                                                    <table>

                                                    </table>
                                                </td>
                                            </tr>
                                            <tr>
                                                <td style="padding-left: 5px">
                                                    <table class="table table-striped table-bordered" style="min-height: 80px">

                                                    </table>
                                                </td>
                                            </tr>
                                        </table>
                                    </div>
                                </div>
                            </div>
                            <div class="tab-pane" id="inputControl">
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
                                <div class="tcp-row">
                                    <div class="tcp-row__body">
                                        <table>
                                            <tr>
                                                <td class="label-column"><label class="text-nowrap" for="autoNumber">Номер авто/вагона:</label></td>
                                                <td style="padding-left: 20px; min-width: 415px">
                                                    <form:input path="autoNumber" id="autoNumber" class="form-control" placeholder="\"С 199 АТ 58\" или \"Ж/д вагон №54550181\"" type="text" />
                                                    <form:errors path="autoNumber" cssClass="help-block" element="em"/>
                                                </td>
                                            </tr>
                                        </table>
                                    </div>
                                </div>
                            </div>
                            <div class="tab-pane" id="assembly">
                                <%@include file="../journal/assembly-journal-view.jsp"%>
                            </div>
                            <div class="tab-pane" id="coating">
                                <%@include file="../journal/base-journal-view.jsp"%>
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
                        <button class="bottom-bar__button back-button" onclick="window.location.href='${contextRoot}/entity/SheetGateValves/showAll'; return false;">Назад</button>
                        <input id="SaveButton" type="submit" name="save" value="Сохранить" class="bottom-bar__button" />
                        <form:hidden path="id"/>
                        <form:hidden path="name"/>
                        <form:hidden path="status"/>
                    </div>
                </div>
            </div>
            <div class="modal fade" id="addShearPin" role="dialog" tabindex="-1">
                <div class="modal-dialog" role="document">
                    <div class="modal-content">
                        <div class="modal-header">
                            <button type="button" class="close" data-dismiss="modal">
                                <span>&times;</span>
                            </button>
                            <h4 class="modal-title"><label>Введите количество штифтов</label></h4>
                        </div>
                        <div class="modal-body">
                            <div class="form-group">
                                <div style="padding: 0px 15px">
                                    <input name="amountShear" id="amountShear" class="form-control" type="text"/>
                                </div>
                            </div>
                            <div class="form-group">
                                <div style="padding: 0px 15px">
                                    <input type="submit" name="addShearPin" value="Добавить" tabindex="-99" class="btn btn-primary"/>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="modal fade" id="addScrewStud" role="dialog" tabindex="-1">
                <div class="modal-dialog" role="document">
                    <div class="modal-content">
                        <div class="modal-header">
                            <button type="button" class="close" data-dismiss="modal">
                                <span>&times;</span>
                            </button>
                            <h4 class="modal-title"><label>Введите количество шпилек</label></h4>
                        </div>
                        <div class="modal-body">
                            <div class="form-group">
                                <div style="padding: 0px 15px">
                                    <input name="amountStud" id="amountStud" class="form-control" type="text"/>
                                </div>
                            </div>
                            <div class="form-group">
                                <div style="padding: 0px 15px">
                                    <input type="submit" name="addScrewStud" value="Добавить" class="btn btn-primary"/>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="modal fade" id="addScrewNut" role="dialog" tabindex="-1">
                <div class="modal-dialog" role="document">
                    <div class="modal-content">
                        <div class="modal-header">
                            <button type="button" class="close" data-dismiss="modal">
                                <span>&times;</span>
                            </button>
                            <h4 class="modal-title"><label>Введите количество гаек</label></h4>
                        </div>
                        <div class="modal-body">
                            <div class="form-group">
                                <div style="padding: 0px 15px">
                                    <input name="amountNut" id="amountNut" class="form-control" type="text"/>
                                </div>
                            </div>
                            <div class="form-group">
                                <div style="padding: 0px 15px">
                                    <input type="submit" name="addScrewNut" value="Добавить" class="btn btn-primary"/>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="modal fade" id="addSpring" role="dialog" tabindex="-1">
                <div class="modal-dialog" role="document">
                    <div class="modal-content">
                        <div class="modal-header">
                            <button type="button" class="close" data-dismiss="modal">
                                <span>&times;</span>
                            </button>
                            <h4 class="modal-title"><label>Введите количество пружин</label></h4>
                        </div>
                        <div class="modal-body">
                            <div class="form-group">
                                <div style="padding: 0px 15px">
                                    <input name="amountSpring" id="amountSpring" class="form-control" type="text"/>
                                </div>
                            </div>
                            <div class="form-group">
                                <div style="padding: 0px 15px">
                                    <input type="submit" name="addSpring" value="Добавить" class="btn btn-primary"/>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </form:form>

        <%@include file="../01-construct/script-suit-ediView.jsp"%>
    </div>
</div>
</body>
</html>