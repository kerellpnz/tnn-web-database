package com.kerellpnz.tnnwebdatabase.entity.journal.assemblyunit;

import com.kerellpnz.tnnwebdatabase.entity.journal.BaseJournal;
import com.kerellpnz.tnnwebdatabase.entity.technicalcontrolplans.BaseTCP;
import com.kerellpnz.tnnwebdatabase.entity.unit.assemblyunit.SheetGateValveCover;

import javax.persistence.*;

@Entity
@Table(name = "sheetgatevalvecoverjournals")
public class SheetGateValveCoverJournal extends BaseJournal {

    @ManyToOne(cascade= {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name="DetailId")
    private SheetGateValveCover detailId;

    public SheetGateValveCoverJournal() {

    }

    public SheetGateValveCoverJournal(BaseTCP tcp) {
        super(tcp);
    }

    public SheetGateValveCoverJournal(BaseJournal journal, Integer inspectorId, String journalNumber, SheetGateValveCover detailId) {
        super(journal, inspectorId, journalNumber);
        this.detailId = detailId;
    }

    public SheetGateValveCoverJournal(BaseJournal journal, SheetGateValveCover detailId) {
        super(journal);
        this.detailId = detailId;
    }

    public SheetGateValveCover getDetailId() {
        return detailId;
    }

    public void setDetailId(SheetGateValveCover detailId) {
        this.detailId = detailId;
    }
}
