package com.kerellpnz.tnnwebdatabase.entity.journal.material;

import com.kerellpnz.tnnwebdatabase.entity.journal.BaseJournal;
import com.kerellpnz.tnnwebdatabase.entity.technicalcontrolplans.BaseTCP;
import com.kerellpnz.tnnwebdatabase.entity.unit.material.SheetMaterial;

import javax.persistence.*;

@Entity
@Table(name = "sheetmaterialjournals")
public class SheetMaterialJournal extends BaseJournal {

    @ManyToOne(cascade= {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name="DetailId")
    private SheetMaterial detailId;

    public SheetMaterialJournal() {

    }

    public SheetMaterialJournal(BaseTCP tcp) {
        super(tcp);
    }

    public SheetMaterialJournal(BaseJournal journal, Integer inspectorId, String journalNumber, SheetMaterial detailId) {
        super(journal, inspectorId, journalNumber);
        this.detailId = detailId;
    }

    public SheetMaterialJournal(BaseJournal journal, SheetMaterial detailId) {
        super(journal);
        this.detailId = detailId;
    }

    public SheetMaterial getDetailId() {
        return detailId;
    }

    public void setDetailId(SheetMaterial detailId) {
        this.detailId = detailId;
    }
}
