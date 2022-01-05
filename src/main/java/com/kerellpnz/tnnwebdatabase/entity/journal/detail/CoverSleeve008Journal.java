package com.kerellpnz.tnnwebdatabase.entity.journal.detail;

import com.kerellpnz.tnnwebdatabase.entity.journal.BaseJournal;
import com.kerellpnz.tnnwebdatabase.entity.technicalcontrolplans.BaseTCP;
import com.kerellpnz.tnnwebdatabase.entity.unit.detail.CoverSleeve008;

import javax.persistence.*;

@Entity
@Table(name = "coversleeve008journals")
public class CoverSleeve008Journal extends BaseJournal {

    @ManyToOne(cascade= {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name="DetailId")
    private CoverSleeve008 detailId;

    public CoverSleeve008Journal() {

    }

    public CoverSleeve008Journal(BaseTCP tcp) {
        super(tcp);
    }

    public CoverSleeve008Journal(BaseJournal journal, Integer inspectorId, String journalNumber, CoverSleeve008 detailId) {
        super(journal, inspectorId, journalNumber);
        this.detailId = detailId;
    }

    public CoverSleeve008Journal(BaseJournal journal, CoverSleeve008 detailId) {
        super(journal);
        this.detailId = detailId;
    }

    public CoverSleeve008 getDetailId() {
        return detailId;
    }

    public void setDetailId(CoverSleeve008 detailId) {
        this.detailId = detailId;
    }
}
