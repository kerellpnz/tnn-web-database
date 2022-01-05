<%@page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<!DOCTYPE html>
<html lang="ru">
<head>
    <link rel="stylesheet" href="${contextRoot}/resources/css/page-style.css">
</head>
<body>
<div class="wrapper">
    <div class="header"></div>
    <div id="container">
        <div id="content">
            <c:if test="${not empty message}">
                <div class="col-xs-12">
                    <div class="alert alert-info alert-dismissable">
                        <button type="button" class="close" data-dismiss="alert">&times;</button>
                            ${message}
                    </div>
                </div>
            </c:if>
            <table id="specification-table" class="table table-border">
                <thead>
                <tr>
                    <th style="width: 60px"></th>
                    <th style="text-align: center">ID</th>
                    <th style="text-align: center; width: 160px;">Номер</th>
                    <th style="text-align: center; width: 60px">Программа поставки</th>
                    <th style="text-align: center; width: 60px">Заказчик</th>
                    <th style="text-align: center">Объект</th>
                    <th style="text-align: center; width: 300px"></th>
                    <th></th>
                </tr>
                </thead>
            </table>
        </div>
        <div class="modal fade" id="newSpecificationModal" role="dialog" tabindex="-1">
            <div class="modal-dialog" role="document">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal">
                            <span>&times;</span>
                        </button>
                        <h4 class="modal-title">Спецификация</h4>
                    </div>
                    <div class="modal-body">
                        <form:form id="specificationForm" class="form-horizontal" modelAttribute="specification"
                                   action="${contextRoot}/Specifications/save"
                                   method="POST">
                            <div class="form-group">
                                <label class="control-label col-md-4" for="number">Номер спецификации</label>
                                <div class="col-md-8">
                                    <form:input type="text" path="number" id="number" class="form-control"/>
                                    <form:errors path="number" cssClass="help-block" element="em"/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="control-label col-md-4" for="program">Программа</label>
                                <div class="col-md-8">
                                    <form:input type="text" path="program" id="program" class="form-control"/>
                                    <form:errors path="program" cssClass="help-block" element="em"/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="control-label col-md-4" for="customer">Заказчик</label>
                                <div class="col-md-8">
                                    <form:select path="customer" id="customer" class="form-control">
                                        <form:options items="${customers}" itemLabel="shortName" itemValue="id" />
                                    </form:select>
                                    <form:errors path="customer" cssClass="help-block" element="em"/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="control-label col-md-4" for="facility">Объект</label>
                                <div class="col-md-8">
                                    <form:textarea path="facility" id="facility" rows="6" class="form-control"/>
                                    <form:errors path="facility" cssClass="help-block" element="em"/>
                                </div>
                            </div>
                            <div class="form-group">
                                <div class="col-md-offset-4 col-md-8">
                                    <input type="submit" name="submit" id="submit" value="Сохранить" class="btn btn-primary"/>
                                    <form:hidden path="id"/>
                                </div>
                            </div>
                        </form:form>
                    </div>
                </div>
            </div>
        </div>
        <div class="modal fade" id="findPID" role="dialog" tabindex="-1">
            <div class="modal-dialog" role="document">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal">
                            <span>&times;</span>
                        </button>
                        <h4 class="modal-title">Найти PID</h4>
                    </div>
                    <div class="modal-body">
                        <form id="findPIDForm" class="form-horizontal"
                                   action="findPID"
                                   method="GET">
                            <div class="form-group">
                                <label class="control-label col-md-4">Номер PID</label>
                                <div class="col-md-8">
                                    <input name="PIDNumber" id="PIDNumber" class="form-control" type="text" list="pids"/>
                                    <c:if test = "${not empty pids}">
                                        <datalist id="pids">
                                            <c:forEach var="pidIndex" items="${pids}">
                                                <option value="${pidIndex}"></option>
                                            </c:forEach>
                                        </datalist>
                                    </c:if>
                                </div>
                            </div>
                            <div class="form-group">
                                <div class="col-md-offset-4 col-md-8">
                                    <input type="submit" name="submit" value="Открыть" class="btn btn-primary"/>
                                </div>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>
