<%@page contentType="text/html; charset=UTF-8" %>

<table class="table table-striped table-border">
    <thead>
    <tr>
        <th>Дата</th>
        <th>Пункт</th>
        <th>Операция</th>
        <th>Место контроля</th>
        <th>Предъявленные документы</th>
        <th>Инженер</th>
        <th>Статус</th>
        <th>Замечание выдано</th>
        <th>Замечание снято</th>
        <th>Номер журнала</th>
        <th>Примечание</th>
        <th></th>
    </tr>
    </thead>
    <c:forEach var="tempJournal" items="${entity.inputControlJournals}" varStatus="s">
        <c:set var="value" value="${tempJournal.remarkIssued}"/>
        <c:choose>
            <c:when test="${not empty tempJournal.date}">
                <form:hidden path="inputControlJournals[${s.index}].id"/>
                <form:hidden path="inputControlJournals[${s.index}].date"/>
                <form:hidden path="inputControlJournals[${s.index}].dateOfRemark"/>
                <form:hidden path="inputControlJournals[${s.index}].point"/>
                <form:hidden path="inputControlJournals[${s.index}].pointId"/>
                <form:hidden path="inputControlJournals[${s.index}].description"/>
                <form:hidden path="inputControlJournals[${s.index}].placeOfControl"/>
                <form:hidden path="inputControlJournals[${s.index}].documents"/>
                <form:hidden path="inputControlJournals[${s.index}].inspectorId"/>
                <form:hidden path="inputControlJournals[${s.index}].inspectorName"/>
                <form:hidden path="inputControlJournals[${s.index}].status"/>
                <form:hidden path="inputControlJournals[${s.index}].journalNumber"/>
                <form:hidden path="inputControlJournals[${s.index}].remarkInspector"/>
                <c:choose>
                    <c:when test="${tempJournal.status == 'Не соответствует'}">
                        <tr style="background-color: #f5bfbf">
                    </c:when>
                    <c:otherwise>
                        <tr>
                    </c:otherwise>
                </c:choose>
                <td class="date-column">
                    <fmt:formatDate value="${tempJournal.date}" pattern="dd.MM.yyyy" />
                    <c:choose>
                        <c:when test="${tempJournal.status == 'Не соответствует' and empty tempJournal.closingDate}">
                            <form:input path="inputControlJournals[${s.index}].closingDate" class="form-control date-picker date-column" />
                        </c:when>
                        <c:otherwise>
<%--                            <fmt:formatDate value="${tempJournal.closingDate}" pattern="dd.MM.yyyy" />--%>
                            <form:hidden path="inputControlJournals[${s.index}].closingDate"/>
                        </c:otherwise>
                    </c:choose>
                </td>
                <td class="point-column">${tempJournal.point}</td>
                <td class="description-column">${tempJournal.description}</td>
                <td class="placeOfControl-column">${tempJournal.placeOfControl}</td>
                <td class="documents-column">${tempJournal.documents}</td>
                <td class="inspector-column">${tempJournal.inspectorName}</td>
                <td class="status-column">${tempJournal.status}</td>
                <c:choose>
                    <c:when test="${not empty tempJournal.remarkIssued}">
                        <td class="remark-input">
                            <c:choose>
                                <c:when test="${fn:contains(value,'/')}">
                                    <span class="remark-span">Предписание №</span>
                                </c:when>
                                <c:otherwise>
                                    <span class="remark-span">Замечание №</span>
                                </c:otherwise>
                            </c:choose>
                                ${tempJournal.remarkIssued}
                            <span class="remark-span">от <fmt:formatDate value="${tempJournal.dateOfRemark}" pattern="dd.MM.yyyy" /> ${tempJournal.remarkInspector}</span>
                        </td>
                        <form:hidden path="inputControlJournals[${s.index}].remarkIssued"/>
                        <c:choose>
                            <c:when test="${not empty tempJournal.remarkClosed}">
                                <td class="remark-input">
                                    <c:choose>
                                        <c:when test="${fn:contains(value,'/')}">
                                            <span class="remark-span">Предписание №</span>
                                        </c:when>
                                        <c:otherwise>
                                            <span class="remark-span">Замечание №</span>
                                        </c:otherwise>
                                    </c:choose>
                                        ${tempJournal.remarkClosed}
                                    <span class="remark-span">cнято от <fmt:formatDate value="${tempJournal.closingDate}" pattern="dd.MM.yyyy" /></span>
                                </td>
                                <form:hidden path="inputControlJournals[${s.index}].remarkClosed"/>
                            </c:when>
                            <c:otherwise>
                                <td>
                                    <c:if test="${not empty tempJournal.status}">
                                        <form:input path="inputControlJournals[${s.index}].remarkClosed" class="form-control remark-input"/>
                                    </c:if>
                                </td>
                            </c:otherwise>
                        </c:choose>
                    </c:when>
                    <c:otherwise>
                        <td class="remark-column">-</td>
                        <td class="remark-column">-</td>
                    </c:otherwise>
                </c:choose>
                <td class="journalNumber-column">${tempJournal.journalNumber}</td>
                <c:choose>
                    <c:when test="${tempJournal.status == 'Не соответствует'}">
                        <td><form:textarea path="inputControlJournals[${s.index}].comment" class="form-control form-textarea comment-column"/></td>
                    </c:when>
                    <c:otherwise>
                        <td class="comment-column">${tempJournal.comment}</td>
                        <form:hidden path="inputControlJournals[${s.index}].comment"/>
                    </c:otherwise>
                </c:choose>
                <td>
                    <c:choose>
                        <c:when test="${empty tempJournal.inspectorName}">
                            <input class="btn btn-danger" name="deleteOperationInput" type="submit" value="${s.index}" />
                        </c:when>
                        <c:otherwise>
                            <security:authorize access="hasAnyAuthority('ADMIN', 'MANAGER')">
                                <input class="btn btn-danger" name="deleteOperationInput" type="submit" value="${s.index}" onclick="if (!(confirm('Удалить эту запись?'))) return false"/>
                            </security:authorize>
                        </c:otherwise>
                    </c:choose>
                </td>
                </tr>
            </c:when>
            <c:otherwise>
                <form:hidden path="inputControlJournals[${s.index}].id"/>
                <form:hidden path="inputControlJournals[${s.index}].pointId"/>
                <tr>
                    <td><form:input path="inputControlJournals[${s.index}].date" class="form-control date-picker date-column" /></td>
                    <td><form:textarea path="inputControlJournals[${s.index}].point" class="form-control form-textarea point-column"/></td>
                    <td><form:textarea path="inputControlJournals[${s.index}].description" class="form-control form-textarea description-textarea"/></td>
                    <td><form:textarea path="inputControlJournals[${s.index}].placeOfControl" class="form-control form-textarea placeOfControl-column"/></td>
                    <td style="width: 120px;"><form:textarea path="inputControlJournals[${s.index}].documents" class="form-control form-textarea documents-column"/></td>
                    <td class="inspector-column empty">Не завершено</td>
                    <td class="status-column empty">Не завершено</td>
                    <td><form:input path="inputControlJournals[${s.index}].remarkIssued" class="form-control remark-input"/></td>
                    <td class="remark-column">-</td>
                    <td class="journalNumber-column empty">Не завершено</td>
                    <td><form:textarea path="inputControlJournals[${s.index}].comment" class="form-control form-textarea comment-column"/></td>
                    <td>
                        <input class="btn btn-danger" name="deleteOperationInput" type="submit" value="${s.index}" onclick="if (!(confirm('Удалить эту запись?'))) return false"/>
                    </td>
                </tr>
            </c:otherwise>
        </c:choose>
    </c:forEach>
</table>
<div class="tcp-row">
    <div class="tcp-row__body">
        <div class="tcp-row__label"><label class="control-label text-nowrap" for="inputTCPId">Выбор пункта:</label></div>
        <form:select path="inputTCPId" id="inputTCPId" class="form-control">
            <form:option style="display:none" value="" label="Выбор/поиск пункта..."/>
            <form:options items="${inputControlTCP}" itemLabel="shortDescription" itemValue="id" />
        </form:select>
        <input class="btn btn-primary tcp-row__button" name="addOperationInput" type="submit" value="Добавить операцию"/>
    </div>
</div>