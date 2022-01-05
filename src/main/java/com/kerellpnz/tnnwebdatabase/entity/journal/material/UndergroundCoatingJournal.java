package com.kerellpnz.tnnwebdatabase.entity.journal.material;

import com.kerellpnz.tnnwebdatabase.entity.journal.BaseJournal;
import com.kerellpnz.tnnwebdatabase.entity.technicalcontrolplans.BaseTCP;
import com.kerellpnz.tnnwebdatabase.entity.unit.material.UndergroundCoating;

import javax.persistence.*;

@Entity
@Table(name = "undergroundcoatingjournals")
public class UndergroundCoatingJournal extends BaseJournal {

    @ManyToOne(cascade= {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name="DetailId")
    private UndergroundCoating detailId;

    public UndergroundCoatingJournal() {

    }

    public UndergroundCoatingJournal(BaseTCP tcp) {
        super(tcp);
    }

    public UndergroundCoatingJournal(BaseJournal journal, Integer inspectorId, String journalNumber, UndergroundCoating detailId) {
        super(journal, inspectorId, journalNumber);
        this.detailId = detailId;
    }

    public UndergroundCoatingJournal(BaseJournal journal, UndergroundCoating detailId) {
        super(journal);
        this.detailId = detailId;
    }

    public UndergroundCoatingJournal(BaseJournal journal) {
        super.setId(journal.getId());
        super.setPointId(journal.getPointId());
        super.setPoint(journal.getPoint());
        super.setDescription(journal.getDescription());
        super.setJournalNumber(journal.getJournalNumber());
        super.setDate(journal.getDate());
        super.setStatus(journal.getStatus());
        super.setRemarkClosed(journal.getRemarkClosed());
        super.setRemarkIssued(journal.getRemarkIssued());
        super.setComment(journal.getComment());
        super.setInspectorId(journal.getInspectorId());
        super.setPlaceOfControl(journal.getPlaceOfControl());
        super.setDocuments(journal.getDocuments());
        super.setClosingDate(journal.getClosingDate());
        super.setRemarkInspector(journal.getRemarkInspector());
    }

    public UndergroundCoating getDetailId() {
        return detailId;
    }

    public void setDetailId(UndergroundCoating detailId) {
        this.detailId = detailId;
    }
}
