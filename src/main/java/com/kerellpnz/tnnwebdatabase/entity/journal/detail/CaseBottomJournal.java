package com.kerellpnz.tnnwebdatabase.entity.journal.detail;

import com.kerellpnz.tnnwebdatabase.entity.journal.BaseJournal;
import com.kerellpnz.tnnwebdatabase.entity.technicalcontrolplans.BaseTCP;
import com.kerellpnz.tnnwebdatabase.entity.unit.detail.CaseBottom;

import javax.persistence.*;

@Entity
@Table(name = "casebottomjournals")
public class CaseBottomJournal extends BaseJournal {

    @ManyToOne(cascade= {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name="DetailId")
    private CaseBottom detailId;

    public CaseBottomJournal() {

    }

    public CaseBottomJournal(BaseTCP tcp) {
        super(tcp);
    }

    public CaseBottomJournal(BaseJournal journal, Integer inspectorId, String journalNumber, CaseBottom detailId) {
        super(journal, inspectorId, journalNumber);
        this.detailId = detailId;
    }

    public CaseBottomJournal(BaseJournal journal, CaseBottom detailId) {
        super(journal);
        this.detailId = detailId;
    }

    public CaseBottom getDetailId() {
        return detailId;
    }

    public void setDetailId(CaseBottom detailId) {
        this.detailId = detailId;
    }
}
