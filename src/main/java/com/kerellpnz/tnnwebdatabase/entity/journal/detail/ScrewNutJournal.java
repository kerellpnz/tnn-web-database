package com.kerellpnz.tnnwebdatabase.entity.journal.detail;

import com.kerellpnz.tnnwebdatabase.entity.journal.BaseJournal;
import com.kerellpnz.tnnwebdatabase.entity.technicalcontrolplans.BaseTCP;
import com.kerellpnz.tnnwebdatabase.entity.unit.detail.ScrewNut;

import javax.persistence.*;

@Entity
@Table(name = "screwnutjournals")
public class ScrewNutJournal extends BaseJournal {

    @ManyToOne(cascade= {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name="DetailId")
    private ScrewNut detailId;

    public ScrewNutJournal() {

    }

    public ScrewNutJournal(BaseTCP tcp) {
        super(tcp);
    }

    public ScrewNutJournal(BaseJournal journal, Integer inspectorId, String journalNumber, ScrewNut detailId) {
        super(journal, inspectorId, journalNumber);
        this.detailId = detailId;
    }

    public ScrewNutJournal(BaseJournal journal, ScrewNut detailId) {
        super(journal);
        this.detailId = detailId;
    }

    public ScrewNut getDetailId() {
        return detailId;
    }

    public void setDetailId(ScrewNut detailId) {
        this.detailId = detailId;
    }
}
