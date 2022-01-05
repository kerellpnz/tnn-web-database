package com.kerellpnz.tnnwebdatabase.entity.journal.material;

import com.kerellpnz.tnnwebdatabase.entity.journal.BaseJournal;
import com.kerellpnz.tnnwebdatabase.entity.technicalcontrolplans.BaseTCP;
import com.kerellpnz.tnnwebdatabase.entity.unit.material.AbovegroundCoating;

import javax.persistence.*;

@Entity
@Table(name = "abovegroundcoatingjournals")
public class AbovegroundCoatingJournal extends BaseJournal {

    @ManyToOne(cascade= {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name="DetailId")
    private AbovegroundCoating detailId;

    public AbovegroundCoatingJournal() {

    }

    public AbovegroundCoatingJournal(BaseTCP tcp) {
        super(tcp);
    }

    public AbovegroundCoatingJournal(BaseJournal journal, Integer inspectorId, String journalNumber, AbovegroundCoating detailId) {
        super(journal, inspectorId, journalNumber);
        this.detailId = detailId;
    }

    public AbovegroundCoatingJournal(BaseJournal journal, AbovegroundCoating detailId) {
        super(journal);
        this.detailId = detailId;
    }

    public AbovegroundCoatingJournal(BaseJournal journal) {
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

    public AbovegroundCoating getDetailId() {
        return detailId;
    }

    public void setDetailId(AbovegroundCoating detailId) {
        this.detailId = detailId;
    }
}
