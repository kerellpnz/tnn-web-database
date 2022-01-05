package com.kerellpnz.tnnwebdatabase.entity.journal.detail;

import com.kerellpnz.tnnwebdatabase.entity.journal.BaseJournal;
import com.kerellpnz.tnnwebdatabase.entity.technicalcontrolplans.BaseTCP;
import com.kerellpnz.tnnwebdatabase.entity.unit.detail.CoverSleeve;

import javax.persistence.*;

@Entity
@Table(name = "coversleevejournals")
public class CoverSleeveJournal extends BaseJournal {

    @ManyToOne(cascade= {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name="DetailId")
    private CoverSleeve detailId;

    public CoverSleeveJournal() {

    }

    public CoverSleeveJournal(BaseTCP tcp) {
        super(tcp);
    }

    public CoverSleeveJournal(BaseJournal journal, Integer inspectorId, String journalNumber, CoverSleeve detailId) {
        super(journal, inspectorId, journalNumber);
        this.detailId = detailId;
    }

    public CoverSleeveJournal(BaseJournal journal, CoverSleeve detailId) {
        super(journal);
        this.detailId = detailId;
    }

    public CoverSleeve getDetailId() {
        return detailId;
    }

    public void setDetailId(CoverSleeve detailId) {
        this.detailId = detailId;
    }
}
