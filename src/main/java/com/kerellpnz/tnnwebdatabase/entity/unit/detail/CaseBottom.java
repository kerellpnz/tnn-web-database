package com.kerellpnz.tnnwebdatabase.entity.unit.detail;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kerellpnz.tnnwebdatabase.entity.journal.detail.CaseBottomJournal;
import com.kerellpnz.tnnwebdatabase.entity.unit.BaseUnitWithZk;
import com.kerellpnz.tnnwebdatabase.entity.unit.assemblyunit.BaseWeldValveDetail;
import com.kerellpnz.tnnwebdatabase.entity.unit.assemblyunit.SheetGateValveCase;
import com.kerellpnz.tnnwebdatabase.entity.unit.material.MetalMaterial;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "casebottoms")
public class CaseBottom extends BaseUnitWithZk {

//    @Column(name="ZK")
//    private String zk;

    @OneToOne(fetch= FetchType.LAZY, cascade= {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name="MetalMaterialId")
    @JsonIgnore
    private MetalMaterial metalMaterial;

    @OneToMany(fetch= FetchType.LAZY, mappedBy="detailId", cascade=CascadeType.ALL)
    @JsonIgnore
    private List<CaseBottomJournal> entityJournals;

    @OneToOne(mappedBy="caseBottom",
            cascade={CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST,
                    CascadeType.REFRESH})
    @JsonIgnore
    private BaseWeldValveDetail baseWeldValveDetail;

    @Transient
    private Integer reqId;
    @Transient
    private String reqName;

    public CaseBottom() {
        this.setName("Днище");
    }

    public CaseBottom(CaseBottom unit, String number) {
        super(unit, number);
        //this.zk = unit.getZk();
        this.metalMaterial = unit.getMetalMaterial();
    }

//    public String getZk() {
//        return zk;
//    }
//
//    public void setZk(String zk) {
//        this.zk = zk;
//    }

    public MetalMaterial getMetalMaterial() {
        return metalMaterial;
    }

    public void setMetalMaterial(MetalMaterial metalMaterial) {
        this.metalMaterial = metalMaterial;
    }

    public List<CaseBottomJournal> getEntityJournals() {
        return entityJournals;
    }

    public void setEntityJournals(List<CaseBottomJournal> entityJournals) {
        this.entityJournals = entityJournals;
    }

    public BaseWeldValveDetail getBaseWeldValveDetail() {
        return baseWeldValveDetail;
    }

    public void setBaseWeldValveDetail(BaseWeldValveDetail baseWeldValveDetail) {
        this.baseWeldValveDetail = baseWeldValveDetail;
    }

    public Integer getReqId() {
        return reqId;
    }

    public String getReqName() {
        return reqName;
    }

    public void setReqName(String reqName) {
        this.reqName = reqName;
    }

    public void setReqId(Integer reqId) {
        this.reqId = reqId;
    }

    @Override
    public String toString() {
        return String.format("№%s/пл.%s/DN%s - %s", getNumber(), getMelt(), getDn(), getStatus());
    }

    @Override
    public String forReport() {
        return String.format("DN%s, №%s, пл.%s", getDn(), getNumber(), getMelt());
    }
}
