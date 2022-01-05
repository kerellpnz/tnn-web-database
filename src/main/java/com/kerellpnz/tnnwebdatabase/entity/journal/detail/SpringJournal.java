package com.kerellpnz.tnnwebdatabase.entity.journal.detail;

import com.kerellpnz.tnnwebdatabase.entity.journal.BaseJournal;
import com.kerellpnz.tnnwebdatabase.entity.technicalcontrolplans.BaseTCP;
import com.kerellpnz.tnnwebdatabase.entity.unit.detail.Spring;

import javax.persistence.*;

@Entity
@Table(name = "springjournals")
public class SpringJournal extends BaseJournal {

    @ManyToOne(cascade= {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name="DetailId")
    private Spring detailId;

    public SpringJournal() {

    }

    public SpringJournal(BaseTCP tcp) {
        super(tcp);
    }

    public SpringJournal(BaseJournal journal, Integer inspectorId, String journalNumber, Spring detailId) {
        super(journal, inspectorId, journalNumber);
        this.detailId = detailId;
    }

    public SpringJournal(BaseJournal journal, Spring detailId) {
        super(journal);
        this.detailId = detailId;
    }

    public Spring getDetailId() {
        return detailId;
    }

    public void setDetailId(Spring detailId) {
        this.detailId = detailId;
    }
}
