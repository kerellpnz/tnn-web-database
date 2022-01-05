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
                <form id="journalReportForm" class="form-horizontal"
                           action="getReport"
                           method="GET">
                    <div class="characteristics__body" style="margin-left: 100px">
                        <table class="characteristics__table">
                            <tbody>
                            <tr>
                                <td class="label-column">
                                    <div style="padding-bottom: 30px"><label class="control-label col-md-4 text-nowrap" for="start">Дата начала отчета:</label></div>
                                </td>
                                <td style="max-width: 110px">
                                    <input name="start" id="start" class="form-control date-picker"/>
                                </td>
                                <td class="label-column">
                                    <div style="padding-bottom: 30px"><label class="control-label col-md-4 text-nowrap" for="end">Конечная дата:</label></div>
                                </td>
                                <td style="max-width: 110px">
                                    <input name="end" id="end" class="form-control date-picker"/>
                                </td>
                                <td class="label-column">
                                    <div style="padding-bottom: 30px"><label class="control-label col-md-4 text-nowrap" for="newPer">Дата нового периода:</label></div>
                                </td>
                                <td style="max-width: 110px">
                                    <input name="newPer" id="newPer" class="form-control date-picker"/>
                                </td>
                                <td>
                                    <input type="submit" name="submit" value="Загрузить отчет" class="btn btn-primary" style="margin-left: 40px; margin-bottom: 5px"/>
                                </td>
                                <c:if test="${showButton == true}">
                                    <td>
                                        <a href="${contextRoot}/FOMReport/createExcelReport" class="btn btn-primary" style="margin-left: 20px; margin-bottom: 5px">Скачать Excel</a>
                                    </td>
                                </c:if>
                            </tr>
                            </tbody>
                        </table>
                    </div>
                </form>
            </div>
            <div class="content-edit-page" style="min-height: 500px">
                <div class="tab-control">
                    <table class="table table-striped table-bordered">
                        <thead>
                        <tr>
                            <th style="text-align: center">Заказчик</th>
                            <th style="text-align: center">Номер транспортного средства</th>
                            <th style="text-align: center">Дата погрузки</th>
                            <th style="text-align: center; min-width: 400px">Грузополучатель</th>
                            <th style="text-align: center">Тип продукции</th>
                            <th style="text-align: center">Наименование продукции</th>
                            <th style="text-align: center">Номер спецификации</th>
                            <th style="text-align: center">PID</th>
                            <th style="text-align: center">Монтажная маркировка</th>
                            <th style="text-align: center">ТУ, ГОСТ</th>
                            <th style="text-align: center">№ паспорта</th>
                            <th style="text-align: center">Кол-во, шт.</th>
                            <th style="text-align: center">Общий вес, кг.</th>
                        </tr>
                        </thead>
                        <c:if test="${not empty report}">
                            <tbody>
                            <c:forEach var="tempObject" items="${report}" varStatus="s">
                                <tr>
                                    <td style="text-align: center">${tempObject.customerName}</td>
                                    <td style="text-align: center">${tempObject.autoNumber}</td>
                                    <td style="text-align: center"><fmt:formatDate value="${tempObject.shippingDate}" pattern="dd.MM.yyyy" /></td>
                                    <td style="text-align: center">${tempObject.consignee}</td>
                                    <td style="text-align: center">${tempObject.productType}</td>
                                    <td style="text-align: center">${tempObject.productDescription}</td>
                                    <td style="text-align: center">${tempObject.specificationNumber}</td>
                                    <td style="text-align: center">${tempObject.pidNumber}</td>
                                    <td style="text-align: center">${tempObject.designationNumber}</td>
                                    <td style="text-align: center">${tempObject.std}</td>
                                    <td style="text-align: center">${tempObject.certificateNumber}</td>
                                    <td style="text-align: center">${tempObject.amount}</td>
                                    <td style="text-align: center">${tempObject.weight}</td>
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