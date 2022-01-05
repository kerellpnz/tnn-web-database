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
                        <div class="alert alert-success alert-dismissable">
                            <button type="button" class="close" data-dismiss="alert">&times;</button>
                                ${message}
                        </div>
                    </div>
                </c:if>
                <table id="tcp-table" class="table table-striped table-border">
                    <thead>
                    <tr>
                        <th style="text-align: center">ID</th>
                        <th style="text-align: center">Тип изделия</th>
                        <th style="text-align: center">Тип операции</th>
                        <th style="text-align: center">Пункт</th>
                        <th style="text-align: center">Краткое описание</th>
                        <th style="text-align: center">Описание</th>
                        <th style="text-align: center">Место контроля</th>
                        <th style="text-align: center">Документы</th>
                        <th style="min-width: 100px"></th>
                    </tr>
                    </thead>
                </table>
            </div>
            <div class="modal fade" id="newTCPModal" role="dialog" tabindex="-1">
                <div class="modal-dialog" role="document">
                    <div class="modal-content">
                        <div class="modal-header">
                            <button type="button" class="close" data-dismiss="modal">
                                <span>&times;</span>
                            </button>
                            <h4 class="modal-title">Пункт</h4>
                        </div>
                        <div class="modal-body">
                            <form:form id="tcpForm" class="form-horizontal" modelAttribute="tcp"
                                     action="${contextRoot}/tcp/${tcpClass}/save"
                                     method="POST">
                                <div class="form-group">
                                    <label class="control-label col-md-4" for="productType">Тип продукции</label>
                                    <div class="col-md-8">
                                        <form:select path="productType" id="productType" class="form-control"
                                                   items="${productTypes}"
                                        />
                                        <form:errors path="productType" cssClass="help-block" element="em"/>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="control-label col-md-4" for="operationType">Тип операции</label>
                                    <div class="col-md-8">
                                        <form:select path="operationType" id="operationType" class="form-control"
                                                   items="${operationTypes}"
                                        />
                                        <form:errors path="operationType" cssClass="help-block" element="em"/>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="control-label col-md-4" for="point">Пункт</label>
                                    <div class="col-md-8">
                                        <form:input type="text" path="point" id="point" class="form-control"/>
                                        <form:errors path="point" cssClass="help-block" element="em"/>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="control-label col-md-4" for="shortDescription">Краткое описание</label>
                                    <div class="col-md-8">
                                        <form:input type="text" path="shortDescription" id="shortDescription" class="form-control"/>
                                        <form:errors path="shortDescription" cssClass="help-block" element="em"/>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="control-label col-md-4" for="description">Описание</label>
                                    <div class="col-md-8">
                                        <form:textarea path="description" id="description" rows="7" class="form-control"/>
                                        <form:errors path="description" cssClass="help-block" element="em"/>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="control-label col-md-4" for="placeOfControl">Место контроля</label>
                                    <div class="col-md-8">
                                        <form:input path="placeOfControl" id="placeOfControl" class="form-control" tabindex="2" type="text" list="placesOfControl"/>
                                        <c:if test = "${not empty placesOfControl}">
                                            <datalist id="placesOfControl">
                                                <c:forEach var="index" items="${placesOfControl}">
                                                    <option value="${index}"/>
                                                </c:forEach>
                                            </datalist>
                                        </c:if>
                                        <form:errors path="placeOfControl" cssClass="help-block" element="em"/>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="control-label col-md-4" for="document">Документы</label>
                                    <div class="col-md-8">
                                        <form:input path="document" id="document" class="form-control" tabindex="2" type="text" list="documents"/>
                                        <c:if test = "${not empty documents}">
                                            <datalist id="documents">
                                                <c:forEach var="index" items="${documents}">
                                                    <option value="${index}"/>
                                                </c:forEach>
                                            </datalist>
                                        </c:if>
                                        <form:errors path="document" cssClass="help-block" element="em"/>
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
        </div>
    </div>
</body>
</html>
