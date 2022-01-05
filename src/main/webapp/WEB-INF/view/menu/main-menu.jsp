<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@page contentType="text/html; charset=UTF-8" %>
<head>
	<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/menu-style.css">
</head>
<div class="wrapper">
	<div class="container">
		<div class="header">
			<div class="header__title title">главное меню</div>
		</div>
		<div class="manufacturing">
			<div class="manufacturing__row">
				<div class="products">
					<div class="products__title title">изделия</div>
					<div class="products__buttons">
						<button class="products__button button"
									onclick="window.location.href='gate-valve-menu'; return false;">
									ЗАДВИЖКА ШИБЕРНАЯ</button>
						<button class="products__button button">КРАН ШАРОВОЙ</button>
					</div>
				</div>
				<div class="materials">
					<div class="metal-materials">
						<div class="metal-materials__row">
							<div class="metal-materials__title title">входной контроль материалов</div>
							<div class="metal-materials__buttons">
								<button class="metal-materials__button button" onclick="window.location.href='${contextRoot}/entity/FrontalSaddleSealings/showAll'; return false;">Уплотнения ЗШ</button>
								<button class="metal-materials__button button" onclick="window.location.href='${contextRoot}/entity/MainFlangeSealings/showAll'; return false;">Уплотнения основного<br>разъема</button>
								<button class="metal-materials__button button" onclick="window.location.href='${contextRoot}/entity/SheetMaterials/showAll'; return false;">Лист</button>
								<button class="metal-materials__button button" onclick="window.location.href='${contextRoot}/entity/Springs/showAll'; return false;">Пружины</button>
								<button class="metal-materials__button button" onclick="window.location.href='${contextRoot}/entity/RolledMaterials/showAll'; return false;">Круг/Труба</button>
							</div>
						</div>
					</div>
					<div class="cover-materials">
						<div class="cover-materials__title title">акп</div>
						<div class="cover-materials__row">
							<div class="cover-materials__body">
								<div class="cover-materials__subtitle title">надземное</div>
								<div class="cover-materials__buttons">
									<button class="cover-materials__button button" onclick="window.location.href='${contextRoot}/entity/Undercoats/showAll'; return false;">Грунт</button>
									<button class="cover-materials__button button" onclick="window.location.href='${contextRoot}/entity/AbovegroundCoatings/showAll'; return false;">Эмаль</button>
								</div>
							</div>
							<div class="cover-materials__body">
								<div class="cover-materials__subtitle title">подземное</div>
								<div class="cover-materials__buttons">
									<button class="cover-materials__button button" onclick="window.location.href='${contextRoot}/entity/UndergroundCoatings/showAll'; return false;">Компоненты</button>
								</div>
							</div>
						</div>
						<div class="cover-materials__buttons">
							<button class="cover-materials__button button" onclick="window.location.href='${contextRoot}/entity/AbrasiveMaterials/showAll'; return false;">Дробь</button>
						</div>
					</div>
					<div class="weld">
						<div class="weld__title title">сварка</div>
						<div class="weld__buttons">
							<button class="weld__button button" onclick="window.location.href='${contextRoot}/entity/WeldingMaterials/showAll'; return false;">Сварочные материалы</button>
							<button class="weld__button button" onclick="window.location.href='${contextRoot}/entity/ControlWelds/showAll'; return false;">КСС</button>
						</div>
					</div>
				</div>
			</div>
		</div>
		<div class="periodical">
			<div class="periodical__title title">периодический контроль</div>
			<div class="periodical__row">
				<div class="periodical__buttons">
					<button class="periodical__button button" onclick="window.location.href='${contextRoot}/entity/HeatTreatment/showFormForUpdate/1'; return false;">Контроль ТО</button>
					<button class="periodical__button button" onclick="window.location.href='${contextRoot}/entity/WeldingProcedures/showAll'; return false;">Режимы сварки</button>
				</div>
				<div class="periodical__buttons">
					<button class="periodical__button button" onclick="window.location.href='${contextRoot}/entity/StoreControl/showAll'; return false;">Складирование</button>
					<button class="periodical__button button" onclick="window.location.href='${contextRoot}/entity/FactoryInspection/showAll'; return false;">Контроль разрешительной документации</button>
				</div>
			</div>
		</div>
	</div>
</div>