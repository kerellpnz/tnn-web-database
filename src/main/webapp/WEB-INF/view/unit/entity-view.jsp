<%@page contentType="text/html; charset=UTF-8" %>

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
                <c:if test="${userClickWeldingMaterials == true}">
                    <table id="welding-materials-table" class="table table-striped table-border">
                        <thead>
                        <tr>
                            <th class="page-table-header">ID</th>
                            <th class="page-table-header">Наименование</th>
                            <th class="page-table-header">Партия</th>
                            <th class="page-table-header">Сертификат</th>
                            <th class="page-table-header">Статус</th>
                            <th class="page-table-header"></th>
                        </tr>
                        </thead>
                    </table>
                </c:if>
                <c:if test="${userClickControlWelds == true}">
                    <table id="control-weld-table" class="table table-striped table-border">
                        <thead>
                        <tr>
                            <th class="page-table-header">ID</th>
                            <th class="page-table-header">Номер</th>
                            <th class="page-table-header">Способ сварки</th>
                            <th class="page-table-header">Протокол(механика)</th>
                            <th class="page-table-header">Протокол(металлография)</th>
                            <th class="page-table-header">Срок действия</th>
                            <th class="page-table-header">Статус</th>
                            <th class="page-table-header"></th>
                        </tr>
                        </thead>
                    </table>
                </c:if>
                <c:if test="${userClickPeriodicalControl == true}">
                    <script>window.periodicalClass='${periodicalClass}';</script>
                    <table id="periodical-table" class="table table-striped table-border">
                        <thead>
                        <tr>
                            <th class="page-table-header">ID</th>
                            <th class="page-table-header">Наименование</th>
                            <th class="page-table-header">Последняя дата контроля</th>
                            <th class="page-table-header">Срок следующей проверки</th>
                        </tr>
                        </thead>
                    </table>
                </c:if>
                <c:if test="${userClickMetalMaterials == true}">
                    <script>window.materialClass='${materialClass}';window.title='${title}';</script>
                    <table id="metal-material-table" class="table table-striped table-border">
                        <thead>
                        <tr>
                            <th class="page-table-header">ID</th>
                            <th class="page-table-header">Сертификат</th>
                            <th class="page-table-header">Плавка</th>
                            <th class="page-table-header">Номер листа</th>
                            <th class="page-table-header">Партия</th>
                            <th class="page-table-header">Материал</th>
                            <c:choose>
                                <c:when test="${materialClass == 'SheetMaterials'}">
                                    <th class="page-table-header">Толщина</th>
                                </c:when>
                                <c:otherwise>
                                    <th class="page-table-header">Диаметр</th>
                                </c:otherwise>
                            </c:choose>
                            <th class="page-table-header">Статус</th>
                            <th class="page-table-header"></th>
                        </tr>
                        </thead>
                    </table>
                </c:if>
                <c:if test="${userClickShearPins == true}">
                    <script>window.title='${title}';</script>
                    <table id="shear-pin-table" class="table table-striped table-border">
                        <thead>
                        <tr>
                            <th class="page-table-header">ID</th>
                            <th class="page-table-header">ЗК</th>
                            <th class="page-table-header">Диаметр</th>
                            <th class="page-table-header">Предел прочности</th>
                            <th class="page-table-header">Тяга</th>
                            <th class="page-table-header">Чертеж</th>
                            <th class="page-table-header">Сертификат</th>
                            <th class="page-table-header">Плавка</th>
                            <th class="page-table-header">Материал</th>
                            <th class="page-table-header">Количество</th>
                            <th class="page-table-header">Статус</th>
                            <th class="page-table-header"></th>
                        </tr>
                        </thead>
                    </table>
                    <div class="modal fade" id="shearPinCopyModal" role="dialog" tabindex="-1">
                        <div class="modal-dialog" role="document">
                            <div class="modal-content">
                                <div class="modal-header">
                                    <button type="button" class="close" data-dismiss="modal">
                                        <span>&times;</span>
                                    </button>
                                    <h4 class="modal-title">Копировать запись</h4>
                                </div>
                                <div class="modal-body">
                                    <form:form id="shearPinCopyForm" class="form-horizontal" modelAttribute="entityId" autocomplete="false"
                                               action="single-copy"
                                               method="POST">
                                        <div class="form-group">
                                            <label class="control-label col-md-4">Новый ЗК:</label>
                                            <div class="col-md-8">
                                                <input autocomplete="off" type="search" name="number" id="number" class="form-control"/>
                                            </div>
                                        </div>
                                        <div class="form-group">
                                            <label class="control-label col-md-4">Диаметр:</label>
                                            <div class="col-md-8">
                                                <input autocomplete="off" type="search" name="diameter" id="diameter" class="form-control"/>
                                            </div>
                                        </div>
                                        <div class="form-group">
                                            <label class="control-label col-md-4">Предел прочности:</label>
                                            <div class="col-md-8">
                                                <input autocomplete="off" type="search" name="tensileStrength" id="tensileStrength" class="form-control"/>
                                            </div>
                                        </div>
                                        <div class="form-group">
                                            <label class="control-label col-md-4">Тяга №:</label>
                                            <div class="col-md-8">
                                                <input autocomplete="off" type="search" name="pull" id="pull" class="form-control"/>
                                            </div>
                                        </div>
                                        <div class="form-group">
                                            <label class="control-label col-md-4">Количество:</label>
                                            <div class="col-md-8">
                                                <input autocomplete="off" type="search" name="amount" id="amount" class="form-control"/>
                                            </div>
                                        </div>
                                        <div class="form-group">
                                            <div class="col-md-offset-4 col-md-8">
                                                <input type="submit" name="submit" id="submit" value="Ок" class="btn btn-primary"/>
                                                <form:hidden path="id"/>
                                            </div>
                                        </div>
                                    </form:form>
                                </div>
                            </div>
                        </div>
                    </div>
                </c:if>
                <c:if test="${userClickCoatings == true}">
                    <script>window.coatingClass='${coatingClass}';window.title='${title}';</script>
                    <table id="coating-table" class="table table-striped table-border">
                        <thead>
                        <tr>
                            <th class="page-table-header">ID</th>
                            <th class="page-table-header">Наименование</th>
                            <th class="page-table-header">Сертификат</th>
                            <th class="page-table-header">Партия</th>
                            <th class="page-table-header">Принято</th>
                            <th class="page-table-header">Использовать до</th>
                            <th class="page-table-header">Количество</th>
                            <th class="page-table-header">Статус</th>
                            <th class="page-table-header"></th>
                        </tr>
                        </thead>
                    </table>
                </c:if>
                <c:if test="${userClickCommonDetails == true}">
                    <script>window.commonDetailClass='${commonDetailClass}';window.title='${title}';</script>
                    <table id="common-detail-table" class="table table-striped table-border">
                        <thead>
                        <tr>
                            <th class="page-table-header">ID</th>
                            <th class="page-table-header">DN</th>
                            <th class="page-table-header">ЗК</th>
                            <th class="page-table-header">Номер</th>
                            <th class="page-table-header">Плавка</th>
                            <th class="page-table-header">Сертификат</th>
                            <th class="page-table-header">Чертеж</th>
                            <th class="page-table-header">Материал</th>
                            <th class="page-table-header">Статус</th>
                            <th class="page-table-header"></th>
                        </tr>
                        </thead>
                    </table>
                </c:if>
                <c:if test="${userClickDetails == true}">
                    <script>window.detailClass='${detailClass}';window.title='${title}';</script>
                    <table id="detail-table" class="table table-striped table-border">
                        <thead>
                        <tr>
                            <th class="page-table-header">ID</th>
                            <th class="page-table-header">DN</th>
                            <th class="page-table-header">ЗК</th>
                            <th class="page-table-header">Номер</th>
                            <th class="page-table-header">Плавка</th>
                            <th class="page-table-header">Чертеж</th>
                            <th class="page-table-header">Материал</th>
                            <th class="page-table-header">Сертификат</th>
                            <th class="page-table-header">Статус</th>
                            <th class="page-table-header"></th>
                        </tr>
                        </thead>
                    </table>
                </c:if>
                <c:if test="${userClickBugels == true}">
                    <script>window.bugelClass='${bugelClass}';window.title='${title}';</script>
                    <table id="bugel-table" class="table table-striped table-border">
                        <thead>
                        <tr>
                            <th class="page-table-header">ID</th>
                            <th class="page-table-header">DN</th>
                            <th class="page-table-header">ЗК</th>
                            <th class="page-table-header">Номер</th>
                            <th class="page-table-header">Чертеж</th>
                            <th class="page-table-header">Статус</th>
                            <th class="page-table-header"></th>
                        </tr>
                        </thead>
                    </table>
                </c:if>
                <c:if test="${userClickAssemblies == true}">
                    <script>window.assemblyClass='${assemblyClass}';window.title='${title}';</script>
                    <table id="assemblies-table" class="table table-striped table-border">
                        <thead>
                        <tr>
                            <th class="page-table-header">ID</th>
                            <th class="page-table-header">DN</th>
                            <th class="page-table-header">PN</th>
                            <th class="page-table-header">Номер</th>
                            <th class="page-table-header">Плавка</th>
                            <th class="page-table-header">Материал</th>
                            <th class="page-table-header">Чертеж</th>
                            <th class="page-table-header">PID</th>
                            <th class="page-table-header">Статус</th>
                            <th class="page-table-header"></th>
                        </tr>
                        </thead>
                    </table>
                </c:if>
                <c:if test="${userClickSheetGateValves == true}">
                    <table id="gate-valve-table" class="table table-striped table-border">
                        <thead>
                        <tr>
                            <th class="page-table-header">ID</th>
                            <th class="page-table-header">Номер</th>
                            <th class="page-table-header">Обозначение</th>
                            <th class="page-table-header">PID</th>
                            <th class="page-table-header">Статус</th>
                            <th class="page-table-header"></th>
                        </tr>
                        </thead>
                    </table>
                </c:if>
                <c:if test="${userClickAssemblyDetail == true}">
                    <script>window.assemblyDetail='${assemblyDetail}';window.title='${title}';</script>
                    <table id="assembly-detail-table" class="table table-striped table-border">
                        <thead>
                        <tr>
                            <th class="page-table-header">ID</th>
                            <th class="page-table-header">DN</th>
                            <th class="page-table-header">PN</th>
                            <th class="page-table-header">ЗК</th>
                            <th class="page-table-header">Номер</th>
                            <th class="page-table-header">Плавка</th>
                            <th class="page-table-header">К К/Т</th>
                            <th class="page-table-header">Разделка К/Т</th>
                            <th class="page-table-header">Материал</th>
                            <th class="page-table-header">Сертификат</th>
                            <th class="page-table-header">Чертеж</th>
                            <th class="page-table-header">PID</th>
                            <th class="page-table-header">Статус</th>
                            <th class="page-table-header"></th>
                        </tr>
                        </thead>
                    </table>
                </c:if>
                <c:if test="${userClickScrews == true}">
                    <script>window.screwClass='${screwClass}';window.title='${title}';</script>
                    <table id="screw-table" class="table table-striped table-border">
                        <thead>
                        <tr>
                            <th class="page-table-header">ID</th>
                            <th class="page-table-header">DN</th>
                            <th class="page-table-header">Сертификат</th>
                            <th class="page-table-header">Партия</th>
                            <th class="page-table-header">Плавка</th>
                            <th class="page-table-header">Чертеж</th>
                            <th class="page-table-header">Материал</th>
                            <th class="page-table-header">Количество</th>
                            <th class="page-table-header">Статус</th>
                            <th class="page-table-header"></th>
                        </tr>
                        </thead>
                    </table>
                </c:if>
                <c:if test="${userClickSealings == true}">
                    <script>window.sealingClass='${sealingClass}';window.title='${title}';</script>
                    <table id="sealing-table" class="table table-striped table-border">
                        <thead>
                        <tr>
                            <th class="page-table-header">ID</th>
                            <th class="page-table-header">Наименование</th>
                            <th class="page-table-header">Партия</th>
                            <th class="page-table-header">Сертификат</th>
                            <th class="page-table-header">Материал</th>
                            <th class="page-table-header">Чертеж</th>
                            <th class="page-table-header">Количество</th>
                            <th class="page-table-header">Статус</th>
                            <th class="page-table-header"></th>
                        </tr>
                        </thead>
                    </table>
                </c:if>
                <div class="modal fade" id="singleCopyModal" role="dialog" tabindex="-1">
                    <div class="modal-dialog" role="document">
                        <div class="modal-content">
                            <div class="modal-header">
                                <button type="button" class="close" data-dismiss="modal">
                                    <span>&times;</span>
                                </button>
                                <h4 class="modal-title">Копировать запись</h4>
                            </div>
                            <div class="modal-body">
                                <form:form id="entityIdForm" class="form-horizontal" modelAttribute="entityId" autocomplete="false"
                                           action="single-copy"
                                           method="POST">
                                    <div class="form-group">
                                        <label class="control-label col-md-4">Новый номер/партия:</label>
                                        <div class="col-md-8">
                                            <input autocomplete="off" type="search" name="number" id="number" placeholder="Введите новый номер..." class="form-control"/>
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <div class="col-md-offset-4 col-md-8">
                                            <input type="submit" name="submit" id="submit" value="Ок" class="btn btn-primary"/>
                                            <form:hidden path="id"/>
                                        </div>
                                    </div>
                                </form:form>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="modal fade" id="multiCopyModal" role="dialog" tabindex="-1">
                    <div class="modal-dialog" role="document">
                        <div class="modal-content">
                            <div class="modal-header">
                                <button type="button" class="close" data-dismiss="modal">
                                    <span>&times;</span>
                                </button>
                                <h4 class="modal-title">Копировать ЗК</h4>
                            </div>
                            <div class="modal-body">
                                <form:form id="entityIdForm2" class="form-horizontal" modelAttribute="entityId" autocomplete="false"
                                           action="multi-copy"
                                           method="POST">
                                    <div class="form-group">
                                        <label class="control-label col-md-4">Количество:</label>
                                        <div class="col-md-8">
                                            <input autocomplete="off" type="search" name="quantity" id="quantity" placeholder="Введите количество..." class="form-control"/>
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <div class="col-md-offset-4 col-md-8">
                                            <input type="submit" name="submit" id="submit" value="Ок" class="btn btn-primary"/>
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
    </div>
</body>
</html>
