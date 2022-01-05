package com.kerellpnz.tnnwebdatabase.entity.journal.detail;

import com.kerellpnz.tnnwebdatabase.entity.journal.BaseJournal;
import com.kerellpnz.tnnwebdatabase.entity.technicalcontrolplans.BaseTCP;
import com.kerellpnz.tnnwebdatabase.entity.unit.detail.ShearPin;

import javax.persistence.*;

@Entity
@Table(name = "shearpinjournals")
public class ShearPinJournal extends BaseJournal {

    @ManyToOne(cascade= {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name="DetailId")
    private ShearPin detailId;

    public ShearPinJournal() {

    }

    public ShearPinJournal(BaseTCP tcp) {
        super(tcp);
    }

    public ShearPinJournal(BaseJournal journal, Integer inspectorId, String journalNumber, ShearPin detailId) {
        super(journal, inspectorId, journalNumber);
        this.detailId = detailId;
    }

    public ShearPinJournal(BaseJournal journal, ShearPin detailId) {
        super(journal);
        this.detailId = detailId;
    }

    public ShearPin getDetailId() {
        return detailId;
    }

    public void setDetailId(ShearPin detailId) {
        this.detailId = detailId;
    }
}
