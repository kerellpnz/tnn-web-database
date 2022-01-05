package com.kerellpnz.tnnwebdatabase.entity.journal.periodical;

import com.kerellpnz.tnnwebdatabase.entity.journal.BaseJournal;
import com.kerellpnz.tnnwebdatabase.entity.technicalcontrolplans.BaseTCP;
import com.kerellpnz.tnnwebdatabase.entity.unit.periodical.HeatTreatment;

import javax.persistence.*;

@Entity
@Table(name = "ndtcontroljournals")
public class HeatTreatmentJournal extends BaseJournal {

    @ManyToOne(cascade= {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name="DetailId")
    private HeatTreatment detailId;

    public HeatTreatmentJournal() {

    }

    public HeatTreatmentJournal(BaseTCP tcp) {
        super(tcp);
    }

    public HeatTreatmentJournal(BaseJournal journal, Integer inspectorId, String journalNumber, HeatTreatment detailId) {
        super(journal, inspectorId, journalNumber);
        this.detailId = detailId;
    }

    public HeatTreatmentJournal(BaseJournal journal, HeatTreatment detailId) {
        super(journal);
        this.detailId = detailId;
    }

    public HeatTreatment getDetailId() {
        return detailId;
    }

    public void setDetailId(HeatTreatment detailId) {
        this.detailId = detailId;
    }
}
