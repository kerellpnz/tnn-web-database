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
            <table id="journal-number-table" class="table table-striped table-border">
                <thead>
                <tr>
                    <th>ID</th>
                    <th>Номер</th>
                    <th>Журнал закрыт</th>
                    <th></th>
                </tr>
                </thead>
            </table>
        </div>
        <div class="modal fade" id="newJournalNumberModal" role="dialog" tabindex="-1">
            <div class="modal-dialog" role="document">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal">
                            <span>&times;</span>
                        </button>
                        <h4 class="modal-title">Новый журнал</h4>
                    </div>
                    <div class="modal-body">
                        <form:form id="journalNumberForm" class="form-horizontal" modelAttribute="journalNumber"
                                   action="${contextRoot}/entity/JournalNumbers/save"
                                   method="POST">
                            <div class="form-group">
                                <label class="control-label col-md-4" for="number">Номер журнала</label>
                                <div class="col-md-8">
                                    <form:input type="text" path="number" id="number" placeholder="" class="form-control"/>
                                    <form:errors path="number" cssClass="help-block" element="em"/>
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
        <div class="modal fade" id="changeJournal" role="dialog" tabindex="-1">
            <div class="modal-dialog" role="document">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal">
                            <span>&times;</span>
                        </button>
                        <h4 class="modal-title">Сменить активный журнал</h4>
                    </div>
                    <div class="modal-body">
                        <form id="changeJournalForm" class="form-horizontal"
                              action="changeJournal"
                              method="GET">
                            <div class="form-group">
                                <label class="control-label col-md-4">Номер журнала</label>
                                <div class="col-md-8">
                                    <select name="number" id="number" class="form-control">
                                        <c:forEach items="${journals}" var="index">
                                            <option value="${index}">
                                                    ${index}
                                            </option>
                                        </c:forEach>
                                    </select>
                                </div>
                            </div>
                            <div class="form-group">
                                <div class="col-md-offset-4 col-md-8">
                                    <input type="submit" name="submit" value="Выбрать" class="btn btn-primary"/>
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
