package com.kerellpnz.tnnwebdatabase.entity.journal.periodical;

import com.kerellpnz.tnnwebdatabase.entity.journal.BaseJournal;
import com.kerellpnz.tnnwebdatabase.entity.technicalcontrolplans.BaseTCP;
import com.kerellpnz.tnnwebdatabase.entity.unit.periodical.WeldingProcedure;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "weldingproceduresjournals")
public class WeldingProcedureJournal extends BaseJournal {

    @ManyToOne(cascade= {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name="DetailId")
    private WeldingProcedure detailId;

    public WeldingProcedureJournal() {

    }

    public WeldingProcedureJournal(BaseTCP tcp) {
        super(tcp);
    }

    public WeldingProcedureJournal(BaseJournal journal, Integer inspectorId, String journalNumber, WeldingProcedure detailId) {
        super(journal, inspectorId, journalNumber);
        this.detailId = detailId;
    }

    public WeldingProcedureJournal(BaseJournal journal, WeldingProcedure detailId) {
        super(journal);
        this.detailId = detailId;
    }

    public WeldingProcedureJournal(WeldingProcedure wp, BaseJournal journal) {
        this.setDetailId(wp);
        this.setPointId(120);
        this.setPoint(journal.getPoint());
        this.setDescription(journal.getDescription());
        this.setJournalNumber(journal.getJournalNumber());
        this.setDate(journal.getDate());
        this.setStatus(journal.getStatus());
        this.setRemarkClosed(journal.getRemarkClosed());
        this.setRemarkIssued(journal.getRemarkIssued());
        this.setComment(journal.getComment());
        this.setInspectorId(journal.getInspectorId());
        this.setPlaceOfControl(journal.getPlaceOfControl());
        this.setDocuments(journal.getDocuments());
        this.setDateOfRemark(journal.getDateOfRemark());
        this.setRemarkInspector(journal.getRemarkInspector());
    }

    public WeldingProcedure getDetailId() {
        return detailId;
    }

    public void setDetailId(WeldingProcedure detailId) {
        this.detailId = detailId;
    }
}
