package com.kerellpnz.tnnwebdatabase.entity.journal.detail;

import com.kerellpnz.tnnwebdatabase.entity.journal.BaseJournal;
import com.kerellpnz.tnnwebdatabase.entity.technicalcontrolplans.BaseTCP;
import com.kerellpnz.tnnwebdatabase.entity.unit.detail.Flange;

import javax.persistence.*;

@Entity
@Table(name = "coverflangejournals")
public class FlangeJournal extends BaseJournal {

    @ManyToOne(cascade= {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name="DetailId")
    private Flange detailId;

    public FlangeJournal() {

    }

    public FlangeJournal(BaseTCP tcp) {
        super(tcp);
    }

    public FlangeJournal(BaseJournal journal, Integer inspectorId, String journalNumber, Flange detailId) {
        super(journal, inspectorId, journalNumber);
        this.detailId = detailId;
    }

    public FlangeJournal(BaseJournal journal, Flange detailId) {
        super(journal);
        this.detailId = detailId;
    }

    public Flange getDetailId() {
        return detailId;
    }

    public void setDetailId(Flange detailId) {
        this.detailId = detailId;
    }
}
