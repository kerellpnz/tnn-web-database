package com.kerellpnz.tnnwebdatabase.entity.journal.detail;

import com.kerellpnz.tnnwebdatabase.entity.journal.BaseJournal;
import com.kerellpnz.tnnwebdatabase.entity.technicalcontrolplans.BaseTCP;
import com.kerellpnz.tnnwebdatabase.entity.unit.detail.CaseBottom;
import com.kerellpnz.tnnwebdatabase.entity.unit.detail.FrontalSaddleSealing;

import javax.persistence.*;

@Entity
@Table(name = "frontalsaddlesealingjournals")
public class FrontalSaddleSealingJournal extends BaseJournal {

    @ManyToOne(cascade= {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name="DetailId")
    private FrontalSaddleSealing detailId;

    public FrontalSaddleSealingJournal() {

    }

    public FrontalSaddleSealingJournal(BaseTCP tcp) {
        super(tcp);
    }

    public FrontalSaddleSealingJournal(BaseJournal journal, Integer inspectorId, String journalNumber, FrontalSaddleSealing detailId) {
        super(journal, inspectorId, journalNumber);
        this.detailId = detailId;
    }

    public FrontalSaddleSealingJournal(BaseJournal journal, FrontalSaddleSealing detailId) {
        super(journal);
        this.detailId = detailId;
    }

    public FrontalSaddleSealing getDetailId() {
        return detailId;
    }

    public void setDetailId(FrontalSaddleSealing detailId) {
        this.detailId = detailId;
    }
}
