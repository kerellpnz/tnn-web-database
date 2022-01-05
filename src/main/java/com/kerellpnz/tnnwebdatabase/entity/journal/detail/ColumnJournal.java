package com.kerellpnz.tnnwebdatabase.entity.journal.detail;

import com.kerellpnz.tnnwebdatabase.entity.journal.BaseJournal;
import com.kerellpnz.tnnwebdatabase.entity.technicalcontrolplans.BaseTCP;
import com.kerellpnz.tnnwebdatabase.entity.unit.detail.Column;

import javax.persistence.*;

@Entity
@Table(name = "columnjournals")
public class ColumnJournal extends BaseJournal {

    @ManyToOne(cascade= {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name="DetailId")
    private Column detailId;

    public ColumnJournal() {

    }

    public ColumnJournal(BaseTCP tcp) {
        super(tcp);
    }

    public ColumnJournal(BaseJournal journal, Integer inspectorId, String journalNumber, Column detailId) {
        super(journal, inspectorId, journalNumber);
        this.detailId = detailId;
    }

    public ColumnJournal(BaseJournal journal, Column detailId) {
        super(journal);
        this.detailId = detailId;
    }

    public Column getDetailId() {
        return detailId;
    }

    public void setDetailId(Column detailId) {
        this.detailId = detailId;
    }
}
