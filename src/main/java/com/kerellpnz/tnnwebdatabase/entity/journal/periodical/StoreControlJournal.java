package com.kerellpnz.tnnwebdatabase.entity.journal.periodical;

import com.kerellpnz.tnnwebdatabase.entity.journal.BaseJournal;
import com.kerellpnz.tnnwebdatabase.entity.technicalcontrolplans.BaseTCP;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "storescontroljournals")
public class StoreControlJournal extends BaseJournal {

    public StoreControlJournal(BaseTCP tCP) {
        super(tCP);
    }

    public StoreControlJournal(BaseJournal journal) {
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

    public StoreControlJournal() {

    }
}
