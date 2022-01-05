<%@page contentType="text/html; charset=UTF-8" %>

<div class="column">
    <button class="column__button"><span>входной контроль</span></button>
    <div class="column__toggle_block">
        <button class="column__link" onclick="window.location.href='${contextRoot}/entity/RolledMaterials/showAll'; return false;">Круг/Труба</button>
        <button class="column__link" onclick="window.location.href='${contextRoot}/entity/SheetMaterials/showAll'; return false;">Лист</button>
        <button class="column__link" onclick="window.location.href='${contextRoot}/entity/WeldingMaterials/showAll'; return false;">Сварочные материалы</button>
        <button class="column__link" onclick="window.location.href='${contextRoot}/entity/Springs/showAll'; return false;">Пружины</button>
        <button class="column__link" onclick="window.location.href='${contextRoot}/entity/FrontalSaddleSealings/showAll'; return false;">Уплотнения ЗШ</button>
        <button class="column__link" onclick="window.location.href='${contextRoot}/entity/MainFlangeSealings/showAll'; return false;">Уплотнения основного разъема</button>
        <button class="column__link" onclick="window.location.href='${contextRoot}/entity/Undercoats/showAll'; return false;">АКП Грунт</button>
        <button class="column__link" onclick="window.location.href='${contextRoot}/entity/AbrasiveMaterials/showAll'; return false;">АКП Дробь</button>
        <button class="column__link" onclick="window.location.href='${contextRoot}/entity/AbovegroundCoatings/showAll'; return false;">АКП Надземное</button>
        <button class="column__link" onclick="window.location.href='${contextRoot}/entity/UndergroundCoatings/showAll'; return false;">АКП Подземное</button>
    </div>
    <button class="column__button"><span>задвижка шиберная</span></button>
    <div class="column__toggle_block">
        <button class="column__link main-link" onclick="window.location.href='${contextRoot}/entity/SheetGateValves/showAll'; return false;">Задвижка шиберная</button>
        <button class="column__link main-link" onclick="window.location.href='${contextRoot}/entity/SheetGateValveCases/showAll'; return false;">Корпус ЗШ</button>
        <button class="column__link main-link" onclick="window.location.href='${contextRoot}/entity/SheetGateValveCovers/showAll'; return false;">Крышка ЗШ</button>
        <button class="column__link" onclick="window.location.href='${contextRoot}/entity/CoverSleeves008/showAll'; return false;">Втулка дренажная</button>
        <button class="column__link" onclick="window.location.href='${contextRoot}/entity/RunningSleeves/showAll'; return false;">Втулка резьбовая</button>
        <button class="column__link" onclick="window.location.href='${contextRoot}/entity/CoverSleeves/showAll'; return false;">Втулка центральная</button>
        <button class="column__link" onclick="window.location.href='${contextRoot}/entity/ScrewNuts/showAll'; return false;">Гайка</button>
        <button class="column__link" onclick="window.location.href='${contextRoot}/entity/CaseBottoms/showAll'; return false;">Днище</button>
        <button class="column__link" onclick="window.location.href='${contextRoot}/entity/Nozzles/showAll'; return false;">Катушка</button>
        <button class="column__link" onclick="window.location.href='${contextRoot}/entity/Rings/showAll'; return false;">Кольца</button>
        <button class="column__link" onclick="window.location.href='${contextRoot}/entity/Saddles/showAll'; return false;">Обойма</button>
        <button class="column__link" onclick="window.location.href='${contextRoot}/entity/Columns/showAll'; return false;">Стойка</button>
        <button class="column__link" onclick="window.location.href='${contextRoot}/entity/Flanges/showAll'; return false;">Фланец</button>
        <button class="column__link">Фланец ответный</button>
        <button class="column__link" onclick="window.location.href='${contextRoot}/entity/Gates/showAll'; return false;">Шибер</button>
        <button class="column__link" onclick="window.location.href='${contextRoot}/entity/ScrewStuds/showAll'; return false;">Шпилька</button>
        <button class="column__link" onclick="window.location.href='${contextRoot}/entity/Spindles/showAll'; return false;">Шпиндель</button>
        <button class="column__link" onclick="window.location.href='${contextRoot}/entity/ShearPins/showAll'; return false;">Штифты</button>
    </div>
    <button class="column__button"><span>кран шаровой</span></button>
    <div class="column__toggle_block">
        <img src="${contextRoot}/resources/img/InDev.png" alt="">
    </div>
    <security:authorize access="hasAnyAuthority('ADMIN', 'MANAGER')">
        <button class="column__button"><span>птк</span></button>
        <div class="column__toggle_block">
            <button class="column__link main-link" onclick="window.location.href='${contextRoot}/tcp/SheetGateValveTCPs/showAll'; return false;">Задвижка шиберная</button>
            <button class="column__link" onclick="window.location.href='${contextRoot}/tcp/PIDTCPs/showAll'; return false;">PID</button>
            <button class="column__link" onclick="window.location.href='${contextRoot}/tcp/CoverSleeve008TCPs/showAll'; return false;">Втулка дренажная</button>
            <button class="column__link" onclick="window.location.href='${contextRoot}/tcp/RunningSleeveTCPs/showAll'; return false;">Втулка резьбовая</button>
            <button class="column__link" onclick="window.location.href='${contextRoot}/tcp/CoverSleeveTCPs/showAll'; return false;">Втулка центральная</button>
            <button class="column__link" onclick="window.location.href='${contextRoot}/tcp/ScrewNutTCPs/showAll'; return false;">Гайка</button>
            <button class="column__link" onclick="window.location.href='${contextRoot}/tcp/CaseBottomTCPs/showAll'; return false;">Днище</button>
            <button class="column__link" onclick="window.location.href='${contextRoot}/tcp/NozzleTCPs/showAll'; return false;">Катушка</button>
            <button class="column__link" onclick="window.location.href='${contextRoot}/tcp/RingTCPs/showAll'; return false;">Кольца</button>
            <button class="column__link" onclick="window.location.href='${contextRoot}/tcp/SheetGateValveCaseTCPs/showAll'; return false;">Корпус</button>
            <button class="column__link" onclick="window.location.href='${contextRoot}/tcp/SheetGateValveCoverTCPs/showAll'; return false;">Крышка</button>
            <button class="column__link" onclick="window.location.href='${contextRoot}/tcp/SaddleTCPs/showAll'; return false;">Обойма</button>
            <button class="column__link" onclick="window.location.href='${contextRoot}/tcp/SpringTCPs/showAll'; return false;">Пружины</button>
            <button class="column__link" onclick="window.location.href='${contextRoot}/tcp/ColumnTCPs/showAll'; return false;">Стойка</button>
            <button class="column__link" onclick="window.location.href='${contextRoot}/tcp/FlangeTCPs/showAll'; return false;">Фланец</button>
            <button class="column__link">Фланец ответный</button>
            <button class="column__link" onclick="window.location.href='${contextRoot}/tcp/GateTCPs/showAll'; return false;">Шибер</button>
            <button class="column__link" onclick="window.location.href='${contextRoot}/tcp/ScrewStudTCPs/showAll'; return false;">Шпилька</button>
            <button class="column__link" onclick="window.location.href='${contextRoot}/tcp/SpindleTCPs/showAll'; return false;">Шпиндель</button>
            <button class="column__link" onclick="window.location.href='${contextRoot}/tcp/ShearPinTCPs/showAll'; return false;">Штифты</button>
            <button class="column__link" onclick="window.location.href='${contextRoot}/tcp/CoatingTCPs/showAll'; return false;">АКП ЗА</button>
            <button class="column__link" onclick="window.location.href='${contextRoot}/tcp/AnticorrosiveCoatingTCPs/showAll'; return false;">АКП Входной</button>
            <button class="column__link" onclick="window.location.href='${contextRoot}/tcp/MetalMaterialTCPs/showAll'; return false;">Металл</button>
            <button class="column__link" onclick="window.location.href='${contextRoot}/tcp/WeldingMaterialTCPs/showAll'; return false;">Сварочные материалы</button>
            <button class="column__link" onclick="window.location.href='${contextRoot}/tcp/ControlWeldTCPs/showAll'; return false;">КСС</button>
            <button class="column__link" onclick="window.location.href='${contextRoot}/tcp/FrontalSaddleSealingTCPs/showAll'; return false;">Уплотнения ЗШ</button>
            <button class="column__link" onclick="window.location.href='${contextRoot}/tcp/MainFlangeSealingTCPs/showAll'; return false;">Уплотнения основного разъема</button>
            <button class="column__link" onclick="window.location.href='${contextRoot}/tcp/WeldingProcedureTCPs/showAll'; return false;">Режимы сварки</button>
            <button class="column__link" onclick="window.location.href='${contextRoot}/tcp/HeatTreatmentTCPs/showAll'; return false;">Контроль ТО</button>
            <button class="column__link" onclick="window.location.href='${contextRoot}/tcp/StoresControlTCPs/showAll'; return false;">Контроль складирования</button>
            <button class="column__link" onclick="window.location.href='${contextRoot}/tcp/FactoryInspectionTCPs/showAll'; return false;">Контроль документации</button>
        </div>
    </security:authorize>
</div>
<script src="${contextRoot}/resources/js/accordion.js"></script>
