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
		<form:form id="entityForm" class="form-horizontal" modelAttribute="entity" autocomplete="off"
				   action="${contextRoot}/entity/${materialClass}/action"
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
					<div class="characteristics__header header">${title}</div>
					<div class="characteristics__row">
						<div class="characteristics__body">
							<table class="characteristics__table table">
								<tbody>
								<tr>
									<td class="label-column"><label class="control-label col-md-4" for="certificate">Сертификат:</label></td>
									<td>
										<form:input path="certificate" id="certificate" class="form-control" tabindex="5" type="text" />
										<form:errors path="certificate" cssClass="help-block" element="em"/>
									</td>
								</tr>
								<tr>
									<td class="label-column"><label class="control-label col-md-4" for="material">Материал:</label></td>
									<td>
										<form:input path="material" id="material" class="form-control" tabindex="2" type="text" list="materials"/>
										<c:if test = "${not empty materials}">
											<datalist id="materials">
												<c:forEach var="materialIndex" items="${materials}">
													<option value="${materialIndex}"/>
												</c:forEach>
											</datalist>
										</c:if>
										<form:errors path="material" cssClass="help-block" element="em"/>
									</td>
								</tr>
								<tr>
									<td class="label-column"><label class="control-label col-md-4" for="melt">Плавка:</label></td>
									<td>
										<form:input path="melt" id="melt" class="form-control" tabindex="3" type="text" />
										<form:errors path="melt" cssClass="help-block" element="em"/>
									</td>
								</tr>
								</tbody>
							</table>
						</div>
						<div class="characteristics__body">
							<table class="characteristics__table table">
								<tbody>
								<tr>
									<c:choose>
										<c:when test="${materialClass == 'SheetMaterials'}">
											<td class="label-column"><label class="control-label col-md-4 text-nowrap" for="number">Номер листа:</label></td>
										</c:when>
										<c:otherwise>
											<td class="label-column"><label class="control-label col-md-4 text-nowrap" for="number">Количество:</label></td>
										</c:otherwise>
									</c:choose>
									<td>
										<form:input path="number" id="number" class="form-control" tabindex="1" type="text" />
										<form:errors path="number" cssClass="help-block" element="em"/>
									</td>
								</tr>
								<tr>
									<td class="label-column"><label class="control-label col-md-4" for="batch">Партия:</label></td>
									<td>
										<form:input path="batch" id="batch" class="form-control" tabindex="4" type="text" />
										<form:errors path="batch" cssClass="help-block" element="em"/>
									</td>
								</tr>
								<tr>
									<td class="label-column"><label class="col-md-4">СТАТУС:</label></td>
									<td class="label-column">
										<c:choose>
										<c:when test="${entity.status == 'НЕ СООТВ.'}">
										<span style="color: red; font-weight: 700;">
								</c:when>
								<c:otherwise>
									<span style="color: green; font-weight: 700;">
								</c:otherwise>
							</c:choose>
											${entity.status}</span>
									</td>
								</tr>
								</tbody>
							</table>
						</div>
						<div class="characteristics__body">
							<table class="characteristics__table table">
								<tbody>
								<tr>
									<c:choose>
										<c:when test="${materialClass == 'SheetMaterials'}">
											<td class="label-column"><label class="control-label col-md-4 text-nowrap" for="number">Толщина:</label></td>
										</c:when>
										<c:otherwise>
											<td class="label-column"><label class="control-label col-md-4 text-nowrap" for="number">Диаметр:</label></td>
										</c:otherwise>
									</c:choose>
									<td>
										<form:input path="thirdSize" id="thirdSize" class="form-control" tabindex="9" type="text" list="thirdSizes"/>
										<c:if test = "${not empty thirdSizes}">
											<datalist id="thirdSizes">
												<c:forEach var="thirdSizeIndex" items="${thirdSizes}">
													<option value="${thirdSizeIndex}"/>
												</c:forEach>
											</datalist>
										</c:if>
										<form:errors path="thirdSize" cssClass="help-block" element="em"/>
									</td>
								</tr>
								<tr>
									<td class="label-column"><label class="control-label col-md-4" for="secondSize">Длина:</label></td>
									<td>
										<form:input path="secondSize" id="secondSize" class="form-control" tabindex="8" type="text" list="secondSizes"/>
										<c:if test = "${not empty secondSizes}">
											<datalist id="secondSizes">
												<c:forEach var="secondSizeIndex" items="${secondSizes}">
													<option value="${secondSizeIndex}"/>
												</c:forEach>
											</datalist>
										</c:if>
										<form:errors path="secondSize" cssClass="help-block" element="em"/>
									</td>
								</tr>
								<tr>
									<c:choose>
										<c:when test="${materialClass == 'SheetMaterials'}">
											<td class="label-column"><label class="control-label col-md-4 text-nowrap" for="number">Ширина:</label></td>
										</c:when>
										<c:otherwise>
											<td class="label-column"><label class="control-label col-md-4 text-nowrap" for="number">Толщина стенки:</label></td>
										</c:otherwise>
									</c:choose>
									<td>
										<form:input path="firstSize" id="firstSize" class="form-control" tabindex="7" type="text" list="firstSizes"/>
										<c:if test = "${not empty firstSizes}">
											<datalist id="firstSizes">
												<c:forEach var="firstSizeIndex" items="${firstSizes}">
													<option value="${firstSizeIndex}"/>
												</c:forEach>
											</datalist>
										</c:if>
										<form:errors path="firstSize" cssClass="help-block" element="em"/>
									</td>
								</tr>
								</tbody>
							</table>
						</div>
					</div>
				</div>
				<div class="content-edit-page">
					<div class="tab-control">
						<%@include file="../journal/base-journal-view.jsp"%>
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
						<button class="bottom-bar__button back-button" onclick="window.location.href='${contextRoot}/entity/${materialClass}/showAll'; return false;">Назад</button>
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