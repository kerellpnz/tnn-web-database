package com.kerellpnz.tnnwebdatabase.entity.journal.detail;

import com.kerellpnz.tnnwebdatabase.entity.journal.BaseJournal;
import com.kerellpnz.tnnwebdatabase.entity.technicalcontrolplans.BaseTCP;
import com.kerellpnz.tnnwebdatabase.entity.unit.detail.ScrewStud;

import javax.persistence.*;

@Entity
@Table(name = "screwstudjournals")
public class ScrewStudJournal extends BaseJournal {

    @ManyToOne(cascade= {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name="DetailId")
    private ScrewStud detailId;

    public ScrewStudJournal() {

    }

    public ScrewStudJournal(BaseTCP tcp) {
        super(tcp);
    }

    public ScrewStudJournal(BaseJournal journal, Integer inspectorId, String journalNumber, ScrewStud detailId) {
        super(journal, inspectorId, journalNumber);
        this.detailId = detailId;
    }

    public ScrewStudJournal(BaseJournal journal, ScrewStud detailId) {
        super(journal);
        this.detailId = detailId;
    }

    public ScrewStud getDetailId() {
        return detailId;
    }

    public void setDetailId(ScrewStud detailId) {
        this.detailId = detailId;
    }
}
