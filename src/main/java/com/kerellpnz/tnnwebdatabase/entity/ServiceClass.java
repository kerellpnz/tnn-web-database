package com.kerellpnz.tnnwebdatabase.entity;

import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class ServiceClass {

    private List<String> documentsOption = Arrays.asList(
            "НТД",
            "Сертификат",
            "ПМИ",
            "Заключения",
            "КД",
            "ТП",
            "КД, ТП",
            "ТП, Диаграммы",
            "КД, Заключения",
            "КД, Сертификат",
            "Сертификат, Заключения",
            "Паспорт",
            "Паспорт, КД",
            "Схема увязки, ТН",
            "Спецификация");

    private List<String> placesOfControlOption = Arrays.asList(
            "Цех",
            "Склад материалов",
            "Малярный участок",
            "Испытательный стенд",
            "ЦЗЛ",
            "Офис ТН",
            "Участок отгрузки",
            "Цех, Офис ТН");

    private List<String> productTypes = Arrays.asList(
            "Общие",
            "АКП",
            "Задвижка шиберная");

    private List<String> operationTypes = Arrays.asList(
            "Общие",
            "Входной контроль",
            "Обработка",
            "Сварка",
            "Сборка",
            "ПСИ",
            "ВИК после ПСИ",
            "АКП",
            "Документация",
            "Отгрузка");

    private List<String> dns = Arrays.asList(
            "150",
            "200",
            "250",
            "300",
            "350",
            "400",
            "500",
            "600",
            "700",
            "800",
            "1000",
            "1050",
            "1200");

    private List<String> pns = Arrays.asList(
            "1,6",
            "2,5",
            "4,0",
            "6,3",
            "8,0",
            "10,0",
            "12,5");

    public List<String> getDocumentsOption() {
        return documentsOption;
    }

    public List<String> getPlacesOfControlOption() {
        return placesOfControlOption;
    }

    public List<String> getProductTypes() {
        return productTypes;
    }

    public List<String> getOperationTypes() {
        return operationTypes;
    }

    public List<String> getDns() {
        return dns;
    }

    public List<String> getPns() {
        return pns;
    }
}
