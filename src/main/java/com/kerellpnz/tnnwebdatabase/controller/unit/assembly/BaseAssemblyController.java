package com.kerellpnz.tnnwebdatabase.controller.unit.assembly;

import com.kerellpnz.tnnwebdatabase.controller.unit.BaseEntityController;
import com.kerellpnz.tnnwebdatabase.dao.general.JournalNumberDAO;
import com.kerellpnz.tnnwebdatabase.entity.unit.BaseUnit;
import com.kerellpnz.tnnwebdatabase.entity.unit.assemblyunit.BaseWeldValveDetail;
import com.kerellpnz.tnnwebdatabase.entity.unit.assemblyunit.SheetGateValveCover;
import com.kerellpnz.tnnwebdatabase.entity.unit.material.MetalMaterial;

import javax.servlet.http.HttpSession;

public class BaseAssemblyController extends BaseEntityController {

    public BaseAssemblyController(HttpSession session, JournalNumberDAO journalNumberDAO) {
        super(session, journalNumberDAO);
    }

    public boolean checkMaterial(BaseUnit unit, MetalMaterial metal) {
        unit.setMaterial(metal.getMaterial());
        unit.setMelt(metal.getMelt());
        unit.setCertificate(metal.getCertificate());
        return !metal.getStatus().equals("НЕ СООТВ.");
    }

    public String checkCaseBottom(BaseWeldValveDetail detail) {
        if(detail instanceof SheetGateValveCover) {
            detail.setMaterial(detail.getCaseBottom().getMaterial());
            detail.setMelt(detail.getCaseBottom().getMelt());
            detail.setCertificate(detail.getCaseBottom().getCertificate());
        }
        if (detail.getCaseBottom().getBaseWeldValveDetail() != null) {
            if (detail.getCaseBottom().getBaseWeldValveDetail().getId() != detail.getId()) {
                return "ОШИБКА: Выбранное днище уже применено в " +
                        detail.getCaseBottom().getBaseWeldValveDetail().getName() + " №" +
                        detail.getCaseBottom().getBaseWeldValveDetail().getNumber();
            }
            return checkCaseBottomStatus(detail);
        }
        return checkCaseBottomStatus(detail);
    }
    private String checkCaseBottomStatus(BaseWeldValveDetail detail) {
        if (detail.getCaseBottom().getStatus().equals("НЕ СООТВ."))
            return "badBottom";
        else
            return "success";
    }

    public String checkFlange(BaseWeldValveDetail detail) {
        if (detail.getFlange().getBaseWeldValveDetail() != null) {
            if (detail.getFlange().getBaseWeldValveDetail().getId() != detail.getId()) {
                return "ОШИБКА: Выбранный фланец уже применен в " +
                        detail.getFlange().getBaseWeldValveDetail().getName() + " №" +
                        detail.getFlange().getBaseWeldValveDetail().getNumber();
            }
            return checkFlangeStatus(detail);
        }
        return checkFlangeStatus(detail);
    }
    private String checkFlangeStatus(BaseWeldValveDetail detail) {
        if (detail.getFlange().getStatus().equals("НЕ СООТВ."))
            return "badFlange";
        else
            return "success";
    }

    public String checkCoverSleeve008(BaseWeldValveDetail detail) {
        if (detail.getCoverSleeve008().getBaseWeldValveDetail() != null) {
            if (detail.getCoverSleeve008().getBaseWeldValveDetail().getId() != detail.getId()) {
                return "ОШИБКА: Выбранная дренажная втулка уже применена в " +
                        detail.getCoverSleeve008().getBaseWeldValveDetail().getName() + " №" +
                        detail.getCoverSleeve008().getBaseWeldValveDetail().getNumber();
            }
            return checkCoverSleeve008Status(detail);
        }
        return checkCoverSleeve008Status(detail);
    }
    private String checkCoverSleeve008Status(BaseWeldValveDetail detail) {
        if (detail.getCoverSleeve008().getStatus().equals("НЕ СООТВ."))
            return "badCoverSleeve008";
        else
            return "success";
    }
}
