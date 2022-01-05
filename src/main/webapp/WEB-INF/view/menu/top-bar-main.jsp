<%@page contentType="text/html; charset=UTF-8" %>

<div class="topTN">
<%--    <span class="top_logo">Tnn web DataBase</span>--%>
    <span class="top_logo">транснефть надзор</span>
    <div class="top_menu">
        <button class="top_button"
                onclick="window.location.href='${contextRoot}/menu'; return false;">Главное меню</button>
        <div class="top_div">Изделие
            <div class="dropdown-content">
                <button>Кран шаровой</button>
                <button onclick="window.location.href='${contextRoot}/gate-valve-menu'; return false;">Задвижка шиберная</button>
            </div>
        </div>
        <div class="top_div">Заказ
            <div class="dropdown-content">
                <security:authorize access="hasAnyAuthority('ADMIN', 'MANAGER')">
                    <button onclick="window.location.href='${contextRoot}/tcp/Customers/showAll'; return false;">Заказчик</button>
                </security:authorize>
                <button onclick="window.location.href='${contextRoot}/Specifications/showAll'; return false;">Спецификация</button>
            </div>
        </div>
        <div class="top_div">Отчетность
            <div class="dropdown-content">
                <button onclick="window.location.href='${contextRoot}/journalReport/showMenu'; return false;">Отчет ЖТН</button>
<%--                <button>Ежедневный отчет</button>--%>
                <button onclick="window.location.href='${contextRoot}/FOMReport/showMenu'; return false;">Отчет по отгрузке</button>
            </div>
        </div>
        <div class="top_div">Периодика
            <div class="dropdown-content">
                <button onclick="window.location.href='${contextRoot}/entity/HeatTreatment/showFormForUpdate/1'; return false;">Контроль ТО</button>
                <button onclick="window.location.href='${contextRoot}/entity/WeldingProcedures/showAll'; return false;">Режимы сварки</button>
                <button onclick="window.location.href='${contextRoot}/entity/StoreControl/showAll'; return false;">Складирование материалов</button>
                <button onclick="window.location.href='${contextRoot}/entity/FactoryInspection/showAll'; return false;">Контроль разрешительной документации</button>
            </div>
        </div>
        <div class="top_div">Материалы
            <div class="dropdown-content">
                <div>АКП
                    <div class="dropdown-content_2">
                        <button onclick="window.location.href='${contextRoot}/entity/AbrasiveMaterials/showAll'; return false;">Дробь</button>
                        <button onclick="window.location.href='${contextRoot}/entity/Undercoats/showAll'; return false;">Грунт</button>
                        <button onclick="window.location.href='${contextRoot}/entity/AbovegroundCoatings/showAll'; return false;">Эмаль</button>
                        <button onclick="window.location.href='${contextRoot}/entity/UndergroundCoatings/showAll'; return false;">Подземное</button>
                    </div>
                </div>
                <div>Металл
                    <div class="dropdown-content_2">
                        <button onclick="window.location.href='${contextRoot}/entity/SheetMaterials/showAll'; return false;">Лист</button>
                        <button onclick="window.location.href='${contextRoot}/entity/RolledMaterials/showAll'; return false;">Круг/Труба</button>
                    </div>
                </div>
                <div>Уплотнительные материалы
                    <div class="dropdown-content_2">
                        <button onclick="window.location.href='${contextRoot}/entity/FrontalSaddleSealings/showAll'; return false;">Уплотнения ЗШ</button>
                        <button onclick="window.location.href='${contextRoot}/entity/MainFlangeSealings/showAll'; return false;">Уплотнения основного разъема</button>
                        <div></div>
                        <div></div>
                    </div>
                </div>
                <button onclick="window.location.href='${contextRoot}/entity/ControlWelds/showAll'; return false;">КСС</button>
                <button onclick="window.location.href='${contextRoot}/entity/WeldingMaterials/showAll'; return false;">Сварочные материалы</button>
            </div>
        </div>
        <button class="top_button" onclick="window.location.href='${contextRoot}/entity/JournalNumbers/showAll'; return false;">Журналы</button>
        <security:authorize access="isAnonymous()">
            <button class="top_button_log" onclick="window.location.href='${contextRoot}/login'; return false;">Вход/Регистрация</button>
        </security:authorize>
        <security:authorize access="isAuthenticated()">
            <div class="top_div"><span style="text-transform: uppercase">${userModel.name}</span>
                <div class="dropdown-content">
                    <button onclick="window.location.href='${contextRoot}/showMyProfile'; return false;">Профиль</button>
                    <button onclick="window.location.href='${contextRoot}/errorMessage/showFormForAdd'; return false;">Сообщить об ошибке</button>
                    <button onclick="window.location.href='${contextRoot}/perform-logout'; return false;">Выйти</button>
                </div>
            </div>
        </security:authorize>
    </div>
</div>
