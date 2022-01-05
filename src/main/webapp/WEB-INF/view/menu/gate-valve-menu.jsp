<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@page contentType="text/html; charset=UTF-8" %>

<!DOCTYPE html>
<html lang="ru">
<head>
	<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/gate-valve-menu-style.css">
</head>
	<body>
		<div class="wrapper">
			<div class="container">
				<div class="header">
					<div class="header__title title">ЗАДВИЖКА ШИБЕРНАЯ</div>
					<div class="header__buttons">
						<button class="header__button button"
								onclick="window.location.href='${contextRoot}/entity/SheetGateValves/showAll'; return false;">
								Задвижки</button>
					</div>
				</div>
				<div class="main-details">
					<div class="main-details__row">
						<div class="main-details__body">
							<div class="main-details__title title">Детали корпуса</div>
							<div class="main-details__buttons">
								<button class="main-details__button button" onclick="window.location.href='${contextRoot}/entity/SheetGateValveCases/showAll'; return false;">Корпус</button>
								<button class="main-details__button button" onclick="window.location.href='${contextRoot}/entity/Rings/showAll'; return false;">Кольца</button>
							</div>
						</div>
						<div class="main-details__body">
							<div class="main-details__title title">Детали крышки</div>
							<div class="main-details__buttons-body">
								<div class="main-details__buttons">
									<button class="main-details__button button" onclick="window.location.href='${contextRoot}/entity/SheetGateValveCovers/showAll'; return false;">Крышка</button>
									<button class="main-details__button button" onclick="window.location.href='${contextRoot}/entity/CoverSleeves008/showAll'; return false;">Втулка дренажная</button>
								</div>
								<div class="main-details__buttons">
									<button class="main-details__button button" onclick="window.location.href='${contextRoot}/entity/Spindles/showAll'; return false;">Шпиндель</button>
									<button class="main-details__button button" onclick="window.location.href='${contextRoot}/entity/CoverSleeves/showAll'; return false;">Втулка центральная</button>
								</div>
							</div>
						</div>
					</div>
				</div>
				<div class="common-details">
					<div class="common-details__buttons">
						<button class="common-details__button button" onclick="window.location.href='${contextRoot}/entity/CaseBottoms/showAll'; return false;">Днище</button>
						<button class="common-details__button button" onclick="window.location.href='${contextRoot}/entity/Flanges/showAll'; return false;">Фланец</button>
					</div>
				</div>
				<div class="details-block">
					<div class="details-block__row">
						<div class="details-block__body">
							<div class="details-block__title title">Детали затвора</div>
							<div class="details-block__buttons">
								<button class="details-block__button button" onclick="window.location.href='${contextRoot}/entity/Gates/showAll'; return false;">Шибер</button>
								<button class="details-block__button button" onclick="window.location.href='${contextRoot}/entity/Saddles/showAll'; return false;">Обойма</button>
							</div>
						</div>
						<div class="details-block__body">
							<div class="details-block__title title">Бугельный узел</div>
							<div class="details-block__buttons">
								<button class="details-block__button button" onclick="window.location.href='${contextRoot}/entity/Columns/showAll'; return false;">Стойка</button>
								<button class="details-block__button button" onclick="window.location.href='${contextRoot}/entity/RunningSleeves/showAll'; return false;">Втулка резьбовая</button>
								<button class="details-block__button button" onclick="window.location.href='${contextRoot}/entity/ShearPins/showAll'; return false;">Штифты</button>
							</div>
						</div>
					</div>
				</div>
				<div class="details-block">
					<div class="details-block__row">
						<div class="details-block__body">
							<div class="details-block__title title">Крепежные детали</div>
							<div class="details-block__buttons">
								<button class="details-block__button button" onclick="window.location.href='${contextRoot}/entity/ScrewStuds/showAll'; return false;">Шпилька</button>
								<button class="details-block__button button" onclick="window.location.href='${contextRoot}/entity/ScrewNuts/showAll'; return false;">Гайка</button>
							</div>
						</div>
						<div class="details-block__body">
							<div class="details-block__title title">Присоединение к трубопроводу</div>
							<div class="details-block__buttons">
								<button class="details-block__button button" onclick="window.location.href='${contextRoot}/entity/Nozzles/showAll'; return false;">Катушка</button>
								<button class="details-block__button button">Ответный фланец</button>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</body>
</html>