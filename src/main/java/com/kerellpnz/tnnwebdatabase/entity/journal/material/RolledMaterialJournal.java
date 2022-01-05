package com.kerellpnz.tnnwebdatabase.entity.journal.material;

import com.kerellpnz.tnnwebdatabase.entity.journal.BaseJournal;
import com.kerellpnz.tnnwebdatabase.entity.technicalcontrolplans.BaseTCP;
import com.kerellpnz.tnnwebdatabase.entity.unit.material.RolledMaterial;

import javax.persistence.*;

@Entity
@Table(name = "rolledmaterialjournals")
public class RolledMaterialJournal extends BaseJournal  {

    @ManyToOne(cascade= {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name="DetailId")
    private  RolledMaterial detailId;

    public RolledMaterialJournal() {

    }

    public RolledMaterialJournal(BaseTCP tcp) {
        super(tcp);
    }

    public RolledMaterialJournal(BaseJournal journal, Integer inspectorId, String journalNumber, RolledMaterial detailId) {
        super(journal, inspectorId, journalNumber);
        this.detailId = detailId;
    }

    public RolledMaterialJournal(BaseJournal journal, RolledMaterial detailId) {
        super(journal);
        this.detailId = detailId;
    }

    public RolledMaterial getDetailId() {
        return detailId;
    }

    public void setDetailId(RolledMaterial detailId) {
        this.detailId = detailId;
    }
}
