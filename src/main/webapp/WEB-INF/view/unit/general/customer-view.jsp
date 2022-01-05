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
            <table id="customer-table" class="table table-striped table-border">
                <thead>
                <tr>
                    <th>ID</th>
                    <th>Наименование</th>
                    <th>Аббревиатура</th>
                    <th></th>
                </tr>
                </thead>
            </table>
        </div>
        <div class="modal fade" id="newCustomerModal" role="dialog" tabindex="-1">
            <div class="modal-dialog" role="document">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal">
                            <span>&times;</span>
                        </button>
                        <h4 class="modal-title">Заказчик</h4>
                    </div>
                    <div class="modal-body">
                        <form:form id="customerForm" class="form-horizontal" modelAttribute="customer"
                                   action="${contextRoot}/tcp/Customers/save"
                                   method="POST">
                            <div class="form-group">
                                <label class="control-label col-md-4" for="name">Наименование</label>
                                <div class="col-md-8">
                                    <form:input type="text" path="name" id="name" class="form-control"/>
                                    <form:errors path="name" cssClass="help-block" element="em"/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="control-label col-md-4" for="shortName">Аббревиатура</label>
                                <div class="col-md-8">
                                    <form:input type="text" path="shortName" id="shortName" class="form-control"/>
                                    <form:errors path="shortName" cssClass="help-block" element="em"/>
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
