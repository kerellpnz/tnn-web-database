package com.kerellpnz.tnnwebdatabase.entity.journal.material;

import com.kerellpnz.tnnwebdatabase.entity.journal.BaseJournal;
import com.kerellpnz.tnnwebdatabase.entity.technicalcontrolplans.BaseTCP;
import com.kerellpnz.tnnwebdatabase.entity.unit.material.ControlWeld;

import javax.persistence.*;

@Entity
@Table(name = "controlweldjournals")
public class ControlWeldJournal extends BaseJournal {

    @ManyToOne(cascade= {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name="DetailId")
    private ControlWeld detailId;

    public ControlWeldJournal() {

    }

    public ControlWeldJournal(BaseTCP tcp) {
        super(tcp);
    }

    public ControlWeldJournal(BaseJournal journal, Integer inspectorId, String journalNumber, ControlWeld detailId) {
        super(journal, inspectorId, journalNumber);
        this.detailId = detailId;
    }

    public ControlWeldJournal(BaseJournal journal, ControlWeld detailId) {
        super(journal);
        this.detailId = detailId;
    }

    public ControlWeld getDetailId() {
        return detailId;
    }

    public void setDetailId(ControlWeld detailId) {
        this.detailId = detailId;
    }
}
