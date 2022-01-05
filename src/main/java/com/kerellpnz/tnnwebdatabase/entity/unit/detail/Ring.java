package com.kerellpnz.tnnwebdatabase.entity.unit.detail;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kerellpnz.tnnwebdatabase.entity.journal.detail.RingJournal;
import com.kerellpnz.tnnwebdatabase.entity.unit.BaseUnitWithZk;
import com.kerellpnz.tnnwebdatabase.entity.unit.assemblyunit.SheetGateValveCase;
import com.kerellpnz.tnnwebdatabase.entity.unit.material.MetalMaterial;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "rings043")
public class Ring extends BaseUnitWithZk {

    @OneToOne(fetch= FetchType.LAZY, cascade= {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name="MetalMaterialId")
    @JsonIgnore
    private MetalMaterial metalMaterial;

    @ManyToOne(cascade= {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name="BaseWeldValveId")
    @JsonIgnore
    private SheetGateValveCase sheetGateValveCase;

    @OneToMany(fetch= FetchType.LAZY, mappedBy="detailId", cascade=CascadeType.ALL)
    @JsonIgnore
    private List<RingJournal> entityJournals;

    @Transient
    private Integer reqId;

    public Ring() {
        this.setName("Кольцо");
        this.setMaterial("09Г2С");
        this.setCertificate("-");
    }

    public Ring(Ring unit, String number) {
        super(unit, number);
        this.metalMaterial = unit.getMetalMaterial();
    }

    public MetalMaterial getMetalMaterial() {
        return metalMaterial;
    }

    public void setMetalMaterial(MetalMaterial metalMaterial) {
        this.metalMaterial = metalMaterial;
    }

    public SheetGateValveCase getSheetGateValveCase() {
        return sheetGateValveCase;
    }

    public void setSheetGateValveCase(SheetGateValveCase sheetGateValveCase) {
        this.sheetGateValveCase = sheetGateValveCase;
    }

    public List<RingJournal> getEntityJournals() {
        return entityJournals;
    }

    public void setEntityJournals(List<RingJournal> entityJournals) {
        this.entityJournals = entityJournals;
    }

    public Integer getReqId() {
        return reqId;
    }

    public void setReqId(Integer reqId) {
        this.reqId = reqId;
    }
}
