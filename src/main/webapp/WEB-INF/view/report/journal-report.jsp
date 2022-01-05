<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<%@page contentType="text/html; charset=UTF-8" %>
<c:set var="contextRoot" value="${pageContext.request.contextPath}"/>

<!DOCTYPE html>
<html lang="ru">
<%@include file="../unit/01-construct/top-bar-editView.jsp"%>
<body>
<div class="contentTN">
    <div class="containerTN">
        <div class="wrapper">
            <div class="characteristics"  style="min-width: 1700px">
                <c:if test="${not empty message}">
                    <div class="col-xs-12">
                        <div class="alert alert-danger alert-dismissable">
                            <button type="button" class="close" data-dismiss="alert">&times;</button>
                                ${message}
                        </div>
                    </div>
                </c:if>
                <div class="characteristics__header header">${title}</div>
                <form:form id="journalReportForm" class="form-horizontal" modelAttribute="tempObject"
                           action="${contextRoot}/journalReport/getReport"
                           method="POST">
                    <div class="characteristics__body" style="margin-left: 100px">
                        <table class="characteristics__table">
                            <tbody>
                            <tr>
                                <td class="label-column">
                                    <div style="padding-bottom: 30px"><label class="control-label col-md-4" for="date">Дата:</label></div>
                                </td>
                                <td style="max-width: 110px">
                                    <form:input path="date" id="date" class="form-control date-picker"/>
                                    <form:errors path="date" cssClass="help-block" element="em"/>
                                </td>
                                <td class="label-column">
                                    <div style="padding-bottom: 30px"><label class="control-label col-md-4" for="inspId">Инженер:</label></div>
                                </td>
                                <td style="min-width: 200px">
                                    <form:select path="inspId" id="inspId" class="form-control selectize" placeholder="Выберите инженера...">
                                        <form:option style="display:none" value="" label="Выберите инженера..."/>
                                        <form:options items="${inspectors}" itemLabel="name" itemValue="id" />
                                    </form:select>
                                    <form:errors path="inspId" cssClass="help-block" element="em"/>
                                </td>
                                <td>
                                    <input type="submit" name="submit" value="Загрузить отчет" class="btn btn-primary" style="margin-left: 40px; margin-bottom: 5px"/>
                                </td>
                                <c:if test="${showButton == true}">
                                    <td>
                                        <a href="${contextRoot}/journalReport/createExcelReport" class="btn btn-primary" style="margin-left: 20px; margin-bottom: 5px">Скачать Excel</a>
                                    </td>
                                </c:if>
                            </tr>
                            </tbody>
                        </table>
                    </div>
                </form:form>
            </div>
            <div class="content-edit-page" style="min-height: 500px">
                <div class="tab-control">
                    <table class="table table-striped table-bordered">
                        <thead>
                        <tr>
                            <th style="text-align: center">Дата</th>
                            <th style="text-align: center">Пункт</th>
                            <th style="text-align: center">Изделие</th>
                            <th style="text-align: center; min-width: 400px">Контролируемые характеристики</th>
                            <th style="text-align: center">Место проведение контроля</th>
                            <th style="text-align: center">Предъявленные документы</th>
                            <th style="text-align: center">Оценка годности</th>
                            <th style="text-align: center">Замечание открыто</th>
                            <th style="text-align: center">Замечание снято</th>
                            <th style="text-align: center">Инженер</th>
                            <th style="text-align: center">Примечание</th>
                        </tr>
                        </thead>
                    <c:if test="${not empty report}">
                        <tbody>
                        <c:forEach var="tempObject" items="${report}" varStatus="s">
                            <tr>
                                <td>${tempObject.date}</td>
                                <td>${tempObject.point}</td>
                                <td>${tempObject.name}: ${tempObject.number}</td>
                                <td>${tempObject.description}</td>
                                <td>${tempObject.placeOfControl}</td>
                                <td>${tempObject.documents}</td>
                                <td>${tempObject.status}</td>
                                <td>${tempObject.remark}</td>
                                <td>${tempObject.remarkClosed}</td>
                                <td>${tempObject.engineer}</td>
                                <td>${tempObject.comment}</td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </c:if>
                    </table>
                </div>
            </div>
        </div>
        <%@include file="../unit/01-construct/script-suit-ediView.jsp"%>
    </div>
</div>
</body>
</html>