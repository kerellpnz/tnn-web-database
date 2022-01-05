package com.kerellpnz.tnnwebdatabase.entity.journal.detail;

import com.kerellpnz.tnnwebdatabase.entity.journal.BaseJournal;
import com.kerellpnz.tnnwebdatabase.entity.technicalcontrolplans.BaseTCP;
import com.kerellpnz.tnnwebdatabase.entity.unit.detail.Nozzle;

import javax.persistence.*;

@Entity
@Table(name = "nozzlejournals")
public class NozzleJournal extends BaseJournal {

    @ManyToOne(cascade= {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name="DetailId")
    private Nozzle detailId;

    public NozzleJournal() {

    }

    public NozzleJournal(BaseTCP tcp) {
        super(tcp);
    }

    public NozzleJournal(BaseJournal journal, Integer inspectorId, String journalNumber, Nozzle detailId) {
        super(journal, inspectorId, journalNumber);
        this.detailId = detailId;
    }

    public NozzleJournal(BaseJournal journal, Nozzle detailId) {
        super(journal);
        this.detailId = detailId;
    }

    public Nozzle getDetailId() {
        return detailId;
    }

    public void setDetailId(Nozzle detailId) {
        this.detailId = detailId;
    }
}
