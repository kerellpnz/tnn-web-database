package com.kerellpnz.tnnwebdatabase.entity.journal.material;

import com.kerellpnz.tnnwebdatabase.entity.journal.BaseJournal;
import com.kerellpnz.tnnwebdatabase.entity.technicalcontrolplans.BaseTCP;
import com.kerellpnz.tnnwebdatabase.entity.unit.material.WeldingMaterial;

import javax.persistence.*;

@Entity
@Table(name = "weldingmaterialjournals")
public class WeldingMaterialJournal extends BaseJournal {

    @ManyToOne(cascade= {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name="DetailId")
    private WeldingMaterial detailId;

    public WeldingMaterialJournal() {

    }

    public WeldingMaterialJournal(BaseTCP tcp) {
        super(tcp);
    }

    public WeldingMaterialJournal(BaseJournal journal, Integer inspectorId, String journalNumber, WeldingMaterial detailId) {
        super(journal, inspectorId, journalNumber);
        this.detailId = detailId;
    }

    public WeldingMaterialJournal(BaseJournal journal, WeldingMaterial detailId) {
        super(journal);
        this.detailId = detailId;
    }

    public WeldingMaterial getDetailId() {
        return detailId;
    }

    public void setDetailId(WeldingMaterial detailId) {
        this.detailId = detailId;
    }
}
