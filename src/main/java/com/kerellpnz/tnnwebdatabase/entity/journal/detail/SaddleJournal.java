package com.kerellpnz.tnnwebdatabase.entity.journal.detail;

import com.kerellpnz.tnnwebdatabase.entity.journal.BaseJournal;
import com.kerellpnz.tnnwebdatabase.entity.technicalcontrolplans.BaseTCP;
import com.kerellpnz.tnnwebdatabase.entity.unit.detail.Saddle;

import javax.persistence.*;

@Entity
@Table(name = "saddlejournals")
public class SaddleJournal extends BaseJournal {

    @ManyToOne(cascade= {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name="DetailId")
    private Saddle detailId;

    public SaddleJournal() {

    }

    public SaddleJournal(BaseTCP tcp) {
        super(tcp);
    }

    public SaddleJournal(BaseJournal journal, Integer inspectorId, String journalNumber, Saddle detailId) {
        super(journal, inspectorId, journalNumber);
        this.detailId = detailId;
    }

    public SaddleJournal(BaseJournal journal, Saddle detailId) {
        super(journal);
        this.detailId = detailId;
    }

    public Saddle getDetailId() {
        return detailId;
    }

    public void setDetailId(Saddle detailId) {
        this.detailId = detailId;
    }
}
