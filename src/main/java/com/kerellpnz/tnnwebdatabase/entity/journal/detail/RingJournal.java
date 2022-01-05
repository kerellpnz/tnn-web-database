package com.kerellpnz.tnnwebdatabase.entity.journal.detail;

import com.kerellpnz.tnnwebdatabase.entity.journal.BaseJournal;
import com.kerellpnz.tnnwebdatabase.entity.technicalcontrolplans.BaseTCP;
import com.kerellpnz.tnnwebdatabase.entity.unit.detail.Ring;

import javax.persistence.*;

@Entity
@Table(name = "ring043journals")
public class RingJournal extends BaseJournal {

    @ManyToOne(cascade= {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name="DetailId")
    private Ring detailId;

    public RingJournal() {

    }

    public RingJournal(BaseTCP tcp) {
        super(tcp);
    }

    public RingJournal(BaseJournal journal, Integer inspectorId, String journalNumber, Ring detailId) {
        super(journal, inspectorId, journalNumber);
        this.detailId = detailId;
    }

    public RingJournal(BaseJournal journal, Ring detailId) {
        super(journal);
        this.detailId = detailId;
    }

    public Ring getDetailId() {
        return detailId;
    }

    public void setDetailId(Ring detailId) {
        this.detailId = detailId;
    }
}
