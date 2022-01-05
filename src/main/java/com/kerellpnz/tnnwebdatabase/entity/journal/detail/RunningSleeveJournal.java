package com.kerellpnz.tnnwebdatabase.entity.journal.detail;

import com.kerellpnz.tnnwebdatabase.entity.journal.BaseJournal;
import com.kerellpnz.tnnwebdatabase.entity.technicalcontrolplans.BaseTCP;
import com.kerellpnz.tnnwebdatabase.entity.unit.detail.RunningSleeve;

import javax.persistence.*;

@Entity
@Table(name = "RunningSleevejournals")
public class RunningSleeveJournal extends BaseJournal {

    @ManyToOne(cascade= {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name="DetailId")
    private RunningSleeve detailId;

    public RunningSleeveJournal() {

    }

    public RunningSleeveJournal(BaseTCP tcp) {
        super(tcp);
    }

    public RunningSleeveJournal(BaseJournal journal, Integer inspectorId, String journalNumber, RunningSleeve detailId) {
        super(journal, inspectorId, journalNumber);
        this.detailId = detailId;
    }

    public RunningSleeveJournal(BaseJournal journal, RunningSleeve detailId) {
        super(journal);
        this.detailId = detailId;
    }

    public RunningSleeve getDetailId() {
        return detailId;
    }

    public void setDetailId(RunningSleeve detailId) {
        this.detailId = detailId;
    }
}
