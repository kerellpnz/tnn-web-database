<%@page contentType="text/html; charset=UTF-8" %>

<div>
    <div id="loginbox" style="margin-top: 35px;"
         class="mainbox col-md-3 col-md-offset-5 col-sm-6 col-md-offset-2">
        <div class="panel panel-primary">
            <div class="panel-heading">
                <div class="panel-title">Профиль пользователя</div>
            </div>
            <div style="padding-top: 30px" class="panel-body">
                <c:if test="${not empty registrationError}">
                    <div class="row">
                        <div class="col-md-12">
                            <div class="alert alert-danger">${registrationError}</div>
                        </div>
                    </div>
                </c:if>
                <form:form class="form-horizontal" action="${contextRoot}/processProfileForm"
                           modelAttribute="userProfile"
                           method="POST">
                    <div style="margin-bottom: 25px" class="input-group">
                        <span class="input-group-addon"><i class="glyphicon glyphicon-user"></i></span>
                        <form:input type="text" path="login" placeholder="Логин" class="form-control" />
                        <form:errors path="login" cssClass="help-block" element="em"/>
                    </div>
                    <div style="margin-bottom: 25px" class="input-group">
                        <span class="input-group-addon"><i class="glyphicon glyphicon-user"></i></span>
                        <form:input type="text" path="name" placeholder="Имя" class="form-control" />
                        <form:errors path="name" cssClass="help-block" element="em"/>
                    </div>
                    <div style="margin-bottom: 25px" class="input-group">
                        <span class="input-group-addon"><i class="glyphicon glyphicon-user"></i></span>
                        <form:input type="text" path="apointment" placeholder="Должность" class="form-control" />
                        <form:errors path="apointment" cssClass="help-block" element="em"/>
                    </div>
                    <div style="margin-top: 10px" class="form-group">
                        <div class="col-sm-6 controls">
                            <a href="${contextRoot}/changePassword" class="btn btn-success">Изменить пароль</a>
                        </div>
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
