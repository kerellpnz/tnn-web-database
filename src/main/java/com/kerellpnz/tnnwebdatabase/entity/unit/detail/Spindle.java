package com.kerellpnz.tnnwebdatabase.entity.unit.detail;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kerellpnz.tnnwebdatabase.entity.journal.detail.SpindleJournal;
import com.kerellpnz.tnnwebdatabase.entity.unit.BaseUnitWithZk;
import com.kerellpnz.tnnwebdatabase.entity.unit.assemblyunit.SheetGateValveCover;
import com.kerellpnz.tnnwebdatabase.entity.unit.material.MetalMaterial;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "spindles")
public class Spindle extends BaseUnitWithZk {

    @OneToOne(cascade= {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name="MetalMaterialId")
    @JsonIgnore
    private MetalMaterial metalMaterial;

    @OneToMany(fetch= FetchType.LAZY, mappedBy="detailId", cascade=CascadeType.ALL)
    @JsonIgnore
    private List<SpindleJournal> entityJournals;

    @OneToOne(mappedBy="spindle",
            cascade={CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST,
                    CascadeType.REFRESH})
    @JsonIgnore
    private SheetGateValveCover sheetGateValveCover;

    @Transient
    private Integer reqId;

    public Spindle() {
        this.setName("Шпиндель");
        this.setCertificate("-");
    }

    public Spindle(Spindle unit, String number) {
        super(unit, number);
        this.metalMaterial = unit.getMetalMaterial();
    }

    public MetalMaterial getMetalMaterial() {
        return metalMaterial;
    }

    public void setMetalMaterial(MetalMaterial metalMaterial) {
        this.metalMaterial = metalMaterial;
    }

    public List<SpindleJournal> getEntityJournals() {
        return entityJournals;
    }

    public void setEntityJournals(List<SpindleJournal> entityJournals) {
        this.entityJournals = entityJournals;
    }

    public SheetGateValveCover getSheetGateValveCover() {
        return sheetGateValveCover;
    }

    public void setSheetGateValveCover(SheetGateValveCover sheetGateValveCover) {
        this.sheetGateValveCover = sheetGateValveCover;
    }

    public Integer getReqId() {
        return reqId;
    }

    public void setReqId(Integer reqId) {
        this.reqId = reqId;
    }
}
