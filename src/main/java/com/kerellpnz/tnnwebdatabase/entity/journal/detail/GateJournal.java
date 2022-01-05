package com.kerellpnz.tnnwebdatabase.entity.journal.detail;

import com.kerellpnz.tnnwebdatabase.entity.journal.BaseJournal;
import com.kerellpnz.tnnwebdatabase.entity.technicalcontrolplans.BaseTCP;
import com.kerellpnz.tnnwebdatabase.entity.unit.detail.Gate;

import javax.persistence.*;

@Entity
@Table(name = "gatejournals")
public class GateJournal extends BaseJournal {

    @ManyToOne(cascade= {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name="DetailId")
    private Gate detailId;

    public GateJournal() {

    }

    public GateJournal(BaseTCP tcp) {
        super(tcp);
    }

    public GateJournal(BaseJournal journal, Integer inspectorId, String journalNumber, Gate detailId) {
        super(journal, inspectorId, journalNumber);
        this.detailId = detailId;
    }

    public GateJournal(BaseJournal journal, Gate detailId) {
        super(journal);
        this.detailId = detailId;
    }

    public Gate getDetailId() {
        return detailId;
    }

    public void setDetailId(Gate detailId) {
        this.detailId = detailId;
    }
}
