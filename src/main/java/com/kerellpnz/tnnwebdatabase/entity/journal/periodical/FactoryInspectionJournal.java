package com.kerellpnz.tnnwebdatabase.entity.journal.periodical;

import com.kerellpnz.tnnwebdatabase.entity.journal.BaseJournal;
import com.kerellpnz.tnnwebdatabase.entity.technicalcontrolplans.BaseTCP;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "factoryinspectionjournals")
public class FactoryInspectionJournal extends BaseJournal {

    public FactoryInspectionJournal(BaseTCP tCP) {
        super(tCP);
    }

    public FactoryInspectionJournal() {

    }

    public FactoryInspectionJournal(BaseJournal journal) {
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
        super.setDateOfRemark(journal.getDateOfRemark());
        super.setRemarkInspector(journal.getRemarkInspector());
    }
}
