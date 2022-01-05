package com.kerellpnz.tnnwebdatabase.entity.journal.detail;

import com.kerellpnz.tnnwebdatabase.entity.journal.BaseJournal;
import com.kerellpnz.tnnwebdatabase.entity.technicalcontrolplans.BaseTCP;
import com.kerellpnz.tnnwebdatabase.entity.unit.detail.MainFlangeSealing;

import javax.persistence.*;

@Entity
@Table(name = "mainflangesealingjournals")
public class MainFlangeSealingJournal extends BaseJournal {


    @ManyToOne(cascade= {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name="DetailId")
    private MainFlangeSealing detailId;

    public MainFlangeSealingJournal() {

    }

    public MainFlangeSealingJournal(BaseTCP tcp) {
        super(tcp);
    }

    public MainFlangeSealingJournal(BaseJournal journal, Integer inspectorId, String journalNumber, MainFlangeSealing detailId) {
        super(journal, inspectorId, journalNumber);
        this.detailId = detailId;
    }

    public MainFlangeSealingJournal(BaseJournal journal, MainFlangeSealing detailId) {
        super(journal);
        this.detailId = detailId;
    }

    public MainFlangeSealing getDetailId() {
        return detailId;
    }

    public void setDetailId(MainFlangeSealing detailId) {
        this.detailId = detailId;
    }
}
