package com.kerellpnz.tnnwebdatabase.entity.journal.detail;

import com.kerellpnz.tnnwebdatabase.entity.journal.BaseJournal;
import com.kerellpnz.tnnwebdatabase.entity.technicalcontrolplans.BaseTCP;
import com.kerellpnz.tnnwebdatabase.entity.unit.detail.Spindle;

import javax.persistence.*;

@Entity
@Table(name = "spindlejournals")
public class SpindleJournal extends BaseJournal {

    @ManyToOne(cascade= {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name="DetailId")
    private Spindle detailId;

    public SpindleJournal() {

    }

    public SpindleJournal(BaseTCP tcp) {
        super(tcp);
    }

    public SpindleJournal(BaseJournal journal, Integer inspectorId, String journalNumber, Spindle detailId) {
        super(journal, inspectorId, journalNumber);
        this.detailId = detailId;
    }

    public SpindleJournal(BaseJournal journal, Spindle detailId) {
        super(journal);
        this.detailId = detailId;
    }

    public Spindle getDetailId() {
        return detailId;
    }

    public void setDetailId(Spindle detailId) {
        this.detailId = detailId;
    }
}
