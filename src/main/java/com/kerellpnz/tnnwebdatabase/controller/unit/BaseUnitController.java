package com.kerellpnz.tnnwebdatabase.controller.unit;

import com.kerellpnz.tnnwebdatabase.dao.general.JournalNumberDAO;
import com.kerellpnz.tnnwebdatabase.entity.unit.BaseUnit;
import com.kerellpnz.tnnwebdatabase.entity.unit.material.MetalMaterial;

import javax.servlet.http.HttpSession;

public class BaseUnitController extends BaseEntityController {

    public BaseUnitController(HttpSession session, JournalNumberDAO journalNumberDAO) {
        super(session, journalNumberDAO);
    }

    public boolean checkMaterial(BaseUnit unit, MetalMaterial metal) {
        unit.setMaterial(metal.getMaterial());
        unit.setMelt(metal.getMelt());
        return !metal.getStatus().equals("НЕ СООТВ.");
    }
}
