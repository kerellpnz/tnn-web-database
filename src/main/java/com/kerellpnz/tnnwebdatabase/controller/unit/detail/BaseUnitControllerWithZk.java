package com.kerellpnz.tnnwebdatabase.controller.unit.detail;

import com.kerellpnz.tnnwebdatabase.controller.unit.BaseUnitController;
import com.kerellpnz.tnnwebdatabase.dao.general.JournalNumberDAO;
import com.kerellpnz.tnnwebdatabase.entity.unit.BaseUnitWithZk;
import com.kerellpnz.tnnwebdatabase.entity.unit.detail.CaseBottom;

import javax.servlet.http.HttpSession;
import java.util.List;

public class BaseUnitControllerWithZk<T extends  BaseUnitWithZk> extends BaseUnitController {

    public BaseUnitControllerWithZk(HttpSession session, JournalNumberDAO journalNumberDAO) {
        super(session, journalNumberDAO);
    }

    public String checkDuplicatesWithZK(T entity, List<T> entities) {
        if (!entity.getZk().isBlank()) {
            for(BaseUnitWithZk tempEntity : entities) {
                if (entity.getNumber().equals(tempEntity.getNumber()) && entity.getZk().equals(tempEntity.getZk())) {
                    if (entity.getId() != tempEntity.getId())
                        return "ОШИБКА: Деталь с таким номером уже существует! Режим: по ЗК";
                }
            }
        }
        else {
            for(BaseUnitWithZk tempEntity : entities) {
                if (entity.equals(tempEntity)) {
                    if (entity.getId() != tempEntity.getId())
                        return "ОШИБКА: Деталь с таким данными уже существует! Режим: по Сертификату";
                }
            }
        }
        return "";
    }
}
