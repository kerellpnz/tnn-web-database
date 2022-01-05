package com.kerellpnz.tnnwebdatabase.entity.journal.material;

import com.kerellpnz.tnnwebdatabase.entity.journal.BaseJournal;
import com.kerellpnz.tnnwebdatabase.entity.technicalcontrolplans.BaseTCP;
import com.kerellpnz.tnnwebdatabase.entity.unit.material.Undercoat;

import javax.persistence.*;

@Entity
@Table(name = "undercoatjournals")
public class UndercoatJournal extends BaseJournal {

    @ManyToOne(cascade= {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name="DetailId")
    private Undercoat detailId;

    public UndercoatJournal() {

    }

    public UndercoatJournal(BaseTCP tcp) {
        super(tcp);
    }

    public UndercoatJournal(BaseJournal journal, Integer inspectorId, String journalNumber, Undercoat detailId) {
        super(journal, inspectorId, journalNumber);
        this.detailId = detailId;
    }

    public UndercoatJournal(BaseJournal journal, Undercoat detailId) {
        super(journal);
        this.detailId = detailId;
    }

    public UndercoatJournal(BaseJournal journal) {
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

    public Undercoat getDetailId() {
        return detailId;
    }

    public void setDetailId(Undercoat detailId) {
        this.detailId = detailId;
    }
}
