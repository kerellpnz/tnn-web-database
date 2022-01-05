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
        <form:form id="WeldingProcedureForm" class="form-horizontal" modelAttribute="periodicalControl" autocomplete="off"
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
                                    <c:choose>
                                        <c:when test="${periodicalClass == 'WeldingProcedures'}">
                                            <td class="label-column"><label class="col-md-4 text-nowrap">Вид сварки:</label></td>
                                        </c:when>
                                        <c:otherwise>
                                            <td class="label-column"><label class="col-md-4 text-nowrap">Тип периодики:</label></td>
                                        </c:otherwise>
                                    </c:choose>
                                    <td style="min-width: 150px">
                                            ${periodicalControl.name}
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
                                        <fmt:formatDate value="${periodicalControl.lastControl}" pattern="dd.MM.yyyy" />
                                        <form:hidden path="lastControl"/>
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
                                        <fmt:formatDate value="${periodicalControl.nextControl}" pattern="dd.MM.yyyy" />
                                        <form:hidden path="nextControl"/>
                                    </td>
                                </tr>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
                <div class="content-edit-page">
                    <div class="tab-control">
                        <table class="table table-striped table-border">
                            <thead>
                            <tr>
                                <th>Дата</th>
                                <th>Пункт</th>
                                <th>Операция</th>
                                <th>Место контроля</th>
                                <th>Предъявленные документы</th>
                                <th>Инженер</th>
                                <th>Статус</th>
                                <th>Замечание выдано</th>
                                <th>Замечание снято</th>
                                <th>Номер журнала</th>
                                <th>Примечание</th>
                                <th></th>
                            </tr>
                            </thead>
                            <c:forEach var="tempJournal" items="${periodicalControl.periodicalJournals}" varStatus="s">
                                <c:set var="value" value="${tempJournal.remarkIssued}"/>
                                <c:choose>
                                    <c:when test="${not empty tempJournal.date}">
                                        <form:hidden path="periodicalJournals[${s.index}].id"/>
                                        <form:hidden path="periodicalJournals[${s.index}].date"/>
                                        <form:hidden path="periodicalJournals[${s.index}].point"/>
                                        <form:hidden path="periodicalJournals[${s.index}].pointId"/>
                                        <form:hidden path="periodicalJournals[${s.index}].description"/>
                                        <form:hidden path="periodicalJournals[${s.index}].placeOfControl"/>
                                        <form:hidden path="periodicalJournals[${s.index}].documents"/>
                                        <form:hidden path="periodicalJournals[${s.index}].inspectorId"/>
                                        <form:hidden path="periodicalJournals[${s.index}].inspectorName"/>
                                        <form:hidden path="periodicalJournals[${s.index}].status"/>
                                        <form:hidden path="periodicalJournals[${s.index}].journalNumber"/>
                                        <form:hidden path="periodicalJournals[${s.index}].remarkInspector"/>
                                        <c:choose>
                                            <c:when test="${tempJournal.status == 'Не соответствует'}">
                                                <tr style="background-color: #f5bfbf">
                                            </c:when>
                                            <c:otherwise>
                                                <tr>
                                            </c:otherwise>
                                        </c:choose>
                                        <td class="date-column">
                                            <fmt:formatDate value="${tempJournal.date}" pattern="dd.MM.yyyy" />
                                            <c:choose>
                                                <c:when test="${tempJournal.status == 'Не соответствует' and empty tempJournal.closingDate}">
                                                    <form:input path="periodicalJournals[${s.index}].closingDate" class="form-control date-picker date-column" />
                                                </c:when>
                                                <c:otherwise>
                                                    <fmt:formatDate value="${tempJournal.closingDate}" pattern="dd.MM.yyyy" />
                                                    <form:hidden path="periodicalJournals[${s.index}].closingDate"/>
                                                </c:otherwise>
                                            </c:choose>
                                        </td>
                                        <td class="point-column">${tempJournal.point}</td>
                                        <td class="description-column">${tempJournal.description}</td>
                                        <td class="placeOfControl-column">${tempJournal.placeOfControl}</td>
                                        <td class="documents-column">${tempJournal.documents}</td>
                                        <td class="inspector-column">${tempJournal.inspectorName}</td>
                                        <td class="status-column">${tempJournal.status}</td>
                                        <c:choose>
                                            <c:when test="${not empty tempJournal.remarkIssued}">
                                                <td class="remark-input">
                                                    <c:choose>
                                                        <c:when test="${fn:contains(value,'/')}">
                                                            <span class="remark-span">Предписание №</span>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <span class="remark-span">Замечание №</span>
                                                        </c:otherwise>
                                                    </c:choose>
                                                        ${tempJournal.remarkIssued}
                                                    <span class="remark-span">от <fmt:formatDate value="${tempJournal.date}" pattern="dd.MM.yyyy" /> ${tempJournal.remarkInspector}</span>
                                                </td>
                                                <form:hidden path="periodicalJournals[${s.index}].remarkIssued"/>
                                                <c:choose>
                                                    <c:when test="${not empty tempJournal.remarkClosed}">
                                                        <td class="remark-input">
                                                            <c:choose>
                                                                <c:when test="${fn:contains(value,'/')}">
                                                                    <span class="remark-span">Предписание №</span>
                                                                </c:when>
                                                                <c:otherwise>
                                                                    <span class="remark-span">Замечание №</span>
                                                                </c:otherwise>
                                                            </c:choose>
                                                                ${tempJournal.remarkClosed}
                                                            <span class="remark-span">cнято от <fmt:formatDate value="${tempJournal.closingDate}" pattern="dd.MM.yyyy" /></span>
                                                        </td>
                                                        <form:hidden path="periodicalJournals[${s.index}].remarkClosed"/>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <td>
                                                            <c:if test="${not empty tempJournal.status}">
                                                                <form:input path="periodicalJournals[${s.index}].remarkClosed" class="form-control remark-input"/>
                                                            </c:if>
                                                        </td>
                                                    </c:otherwise>
                                                </c:choose>
                                            </c:when>
                                            <c:otherwise>
                                                <td class="remark-column">-</td>
                                                <td class="remark-column">-</td>
                                            </c:otherwise>
                                        </c:choose>
                                        <td class="journalNumber-column">${tempJournal.journalNumber}</td>
                                        <c:choose>
                                            <c:when test="${tempJournal.status == 'Не соответствует'}">
                                                <td><form:textarea path="periodicalJournals[${s.index}].comment" class="form-control form-textarea comment-column"/></td>
                                            </c:when>
                                            <c:otherwise>
                                                <td class="comment-column">${tempJournal.comment}</td>
                                                <form:hidden path="periodicalJournals[${s.index}].comment"/>
                                            </c:otherwise>
                                        </c:choose>
                                        <td>
                                            <security:authorize access="hasAnyAuthority('ADMIN', 'MANAGER')">
                                                <input class="btn btn-danger" name="deleteOperation" type="submit" value="${s.index}" onclick="if (!(confirm('Удалить эту запись?'))) return false"/>
                                            </security:authorize>
                                        </td>
                                        </tr>
                                    </c:when>
                                    <c:otherwise>
                                        <form:hidden path="periodicalJournals[${s.index}].id"/>
                                        <form:hidden path="periodicalJournals[${s.index}].pointId"/>
                                        <tr>
                                            <td><form:input path="periodicalJournals[${s.index}].date" class="form-control date-picker date-column" /></td>
                                            <td><form:textarea path="periodicalJournals[${s.index}].point" class="form-control form-textarea point-column"/></td>
                                            <td><form:textarea path="periodicalJournals[${s.index}].description" class="form-control form-textarea description-textarea"/></td>
                                            <td><form:textarea path="periodicalJournals[${s.index}].placeOfControl" class="form-control form-textarea placeOfControl-column"/></td>
                                            <td style="width: 120px;"><form:textarea path="periodicalJournals[${s.index}].documents" class="form-control form-textarea documents-column"/></td>
                                            <td class="inspector-column empty">Не завершено</td>
                                            <td class="status-column empty">Не завершено</td>
                                            <td><form:input path="periodicalJournals[${s.index}].remarkIssued" class="form-control remark-input"/></td>
                                            <td class="remark-column">-</td>
                                            <td class="journalNumber-column empty">Не завершено</td>
                                            <td><form:textarea path="periodicalJournals[${s.index}].comment" class="form-control form-textarea comment-column"/></td>
                                            <td>
                                                <input class="btn btn-danger" name="deleteOperation" type="submit" value="${s.index}" onclick="if (!(confirm('Удалить эту запись?'))) return false"/>
                                            </td>
                                        </tr>
                                    </c:otherwise>
                                </c:choose>
                            </c:forEach>
                        </table>
                        <div class="tcp-row">
                            <div class="tcp-row__body">
                                <div class="tcp-row__label"><label class="control-label text-nowrap" for="tempTCPId">Выбор пункта:</label></div>
                                <c:choose>
                                    <c:when test="${periodicalClass == 'WeldingProcedures'}">
                                        <form:select path="tempTCPId" id="tempTCPId" class="form-control">
                                            <form:option style="display:none" value="" label="Выбор/поиск пункта..."/>
                                            <form:options items="${weldingProcedureTCPs}" itemLabel="description" itemValue="id" />
                                        </form:select>
                                    </c:when>
                                    <c:otherwise>
                                        <form:select path="tempTCPId" id="tempTCPId" class="form-control">
                                            <form:option style="display:none" value="" label="Выбор/поиск пункта..."/>
                                            <form:options items="${heatTreatmentTCPs}" itemLabel="description" itemValue="id" />
                                        </form:select>
                                    </c:otherwise>
                                </c:choose>
                                <input class="btn btn-primary tcp-row__button" name="addOperation" type="submit" value="Добавить операцию"/>
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
                            <c:when test="${periodicalClass == 'WeldingProcedures'}">
                                <button class="bottom-bar__button back-button" onclick="window.location.href='${contextRoot}/entity/${periodicalClass}/showAll'; return false;">Назад</button>
                            </c:when>
                            <c:otherwise>
                                <button class="bottom-bar__button back-button" onclick="window.location.href='${contextRoot}/'; return false;">Назад</button>
                            </c:otherwise>
                        </c:choose>
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