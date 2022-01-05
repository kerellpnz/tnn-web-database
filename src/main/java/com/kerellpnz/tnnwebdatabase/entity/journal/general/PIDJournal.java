package com.kerellpnz.tnnwebdatabase.entity.journal.general;

import com.kerellpnz.tnnwebdatabase.entity.general.PID;
import com.kerellpnz.tnnwebdatabase.entity.journal.BaseJournal;
import com.kerellpnz.tnnwebdatabase.entity.technicalcontrolplans.BaseTCP;

import javax.persistence.*;

@Entity
@Table(name = "pidjournals")
public class PIDJournal extends BaseJournal {

    @ManyToOne(cascade= {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name="DetailId")
    private PID detailId;

    public PIDJournal() {

    }

    public PIDJournal(BaseTCP tcp) {
        super(tcp);
    }

    public PIDJournal(BaseJournal journal, Integer inspectorId, String journalNumber, PID detailId) {
        super(journal, inspectorId, journalNumber);
        this.detailId = detailId;
    }

    public PIDJournal(BaseJournal journal, PID detailId) {
        super(journal);
        this.detailId = detailId;
    }

    public PID getDetailId() {
        return detailId;
    }

    public void setDetailId(PID detailId) {
        this.detailId = detailId;
    }
}
