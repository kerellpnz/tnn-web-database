<%@page contentType="text/html; charset=UTF-8" %>

<div>
    <div id="loginbox" style="margin-top: 35px;"
         class="mainbox col-md-3 col-md-offset-2 col-sm-6 col-sm-offset-2">
        <div class="panel panel-primary">
            <div class="panel-heading">
                <div class="panel-title">Смена пароля</div>
            </div>
            <div style="padding-top: 30px" class="panel-body">
                <!-- Registration Form -->
                <form:form class="form-horizontal" action="${contextRoot}/processChangePassword"
                           modelAttribute="changePassword"
                           method="POST">
                    <div class="input-group">
                        <span class="input-group-addon"><i class="glyphicon glyphicon-lock"></i></span>
                        <form:input type="password" path="password" placeholder="Введите новый пароль" class="form-control" />
                    </div>
                    <div style="margin-bottom: 25px">
                        <form:errors path="password" cssClass="help-block" element="em"/>
                    </div>
                    <div class="input-group">
                        <span class="input-group-addon"><i class="glyphicon glyphicon-lock"></i></span>
                        <form:input type="password" path="matchingPassword" placeholder="Повторите новый пароль" class="form-control" />
                    </div>
                    <div style="margin-bottom: 25px">
                        <form:errors path="matchingPassword" cssClass="help-block" element="em"/>
                    </div>
                    <div style="margin-top: 10px" class="form-group">
                        <div class="col-sm-6 controls">
                            <button type="submit" class="btn btn-primary">Сохранить</button>
                        </div>
                    </div>
                    <form:hidden path="id"/>
                </form:form>
            </div>
        </div>
    </div>
</div>
