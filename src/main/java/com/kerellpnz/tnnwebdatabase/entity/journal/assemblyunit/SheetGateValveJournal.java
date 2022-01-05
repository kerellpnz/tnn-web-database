package com.kerellpnz.tnnwebdatabase.entity.journal.assemblyunit;

import com.kerellpnz.tnnwebdatabase.entity.journal.BaseJournal;
import com.kerellpnz.tnnwebdatabase.entity.technicalcontrolplans.BaseTCP;
import com.kerellpnz.tnnwebdatabase.entity.unit.assemblyunit.SheetGateValve;

import javax.persistence.*;

@Entity
@Table(name = "sheetgatevalvejournals")
public class SheetGateValveJournal extends BaseJournal {

    @ManyToOne(cascade= {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name="DetailId")
    private SheetGateValve detailId;

    public SheetGateValveJournal() {

    }

    public SheetGateValveJournal(BaseTCP tcp) {
        super(tcp);
    }

    public SheetGateValveJournal(BaseJournal journal, Integer inspectorId, String journalNumber, SheetGateValve detailId) {
        super(journal, inspectorId, journalNumber);
        this.detailId = detailId;
    }

    public SheetGateValveJournal(BaseJournal journal, SheetGateValve detailId) {
        super(journal);
        this.detailId = detailId;
    }

    public SheetGateValve getDetailId() {
        return detailId;
    }

    public void setDetailId(SheetGateValve detailId) {
        this.detailId = detailId;
    }
}
