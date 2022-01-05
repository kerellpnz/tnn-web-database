package com.kerellpnz.tnnwebdatabase.entity.journal.assemblyunit;

import com.kerellpnz.tnnwebdatabase.entity.journal.BaseJournal;
import com.kerellpnz.tnnwebdatabase.entity.technicalcontrolplans.BaseTCP;
import com.kerellpnz.tnnwebdatabase.entity.unit.assemblyunit.SheetGateValveCase;

import javax.persistence.*;

@Entity
@Table(name = "sheetgatevalvecasejournals")
public class SheetGateValveCaseJournal extends BaseJournal {

    @ManyToOne(cascade= {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name="DetailId")
    private SheetGateValveCase detailId;

    public SheetGateValveCaseJournal() {

    }

    public SheetGateValveCaseJournal(BaseTCP tcp) {
        super(tcp);
    }

    public SheetGateValveCaseJournal(BaseJournal journal, Integer inspectorId, String journalNumber, SheetGateValveCase detailId) {
        super(journal, inspectorId, journalNumber);
        this.detailId = detailId;
    }

    public SheetGateValveCaseJournal(BaseJournal journal, SheetGateValveCase detailId) {
        super(journal);
        this.detailId = detailId;
    }

    public SheetGateValveCase getDetailId() {
        return detailId;
    }

    public void setDetailId(SheetGateValveCase detailId) {
        this.detailId = detailId;
    }
}
