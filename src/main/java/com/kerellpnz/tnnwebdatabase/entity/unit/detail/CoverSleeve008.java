package com.kerellpnz.tnnwebdatabase.entity.unit.detail;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kerellpnz.tnnwebdatabase.entity.journal.detail.CoverSleeve008Journal;
import com.kerellpnz.tnnwebdatabase.entity.unit.BaseUnitWithZk;
import com.kerellpnz.tnnwebdatabase.entity.unit.assemblyunit.BaseWeldValveDetail;
import com.kerellpnz.tnnwebdatabase.entity.unit.material.MetalMaterial;

import javax.persistence.*;
import javax.persistence.Column;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Entity
@Table(name = "coversleeves008")
public class CoverSleeve008 extends BaseUnitWithZk {

    @Column(name="ZK")
    @NotBlank(message = "Введите ЗК!")
    private String zk;

    @OneToOne(fetch= FetchType.LAZY, cascade= {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name="MetalMaterialId")
    @JsonIgnore
    private MetalMaterial metalMaterial;

    @OneToMany(fetch= FetchType.LAZY, mappedBy="detailId", cascade=CascadeType.ALL)
    @JsonIgnore
    private List<CoverSleeve008Journal> entityJournals;

    @OneToOne(mappedBy="coverSleeve008",
            cascade={CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST,
                    CascadeType.REFRESH})
    @JsonIgnore
    private BaseWeldValveDetail baseWeldValveDetail;

    @Transient
    private Integer reqId;
    @Transient
    private String reqName;

    public CoverSleeve008() {
        this.setName("Втулка дренажная");
        this.setMaterial("09Г2С");
        this.setCertificate("-");
    }

    public CoverSleeve008(CoverSleeve008 unit, String number) {
        super(unit, number);
        this.metalMaterial = unit.getMetalMaterial();
        this.zk = unit.getZk();
    }

    @Override
    public String getZk() {
        return zk;
    }

    @Override
    public void setZk(String zk) {
        this.zk = zk;
    }

    public MetalMaterial getMetalMaterial() {
        return metalMaterial;
    }

    public void setMetalMaterial(MetalMaterial metalMaterial) {
        this.metalMaterial = metalMaterial;
    }

    public List<CoverSleeve008Journal> getEntityJournals() {
        return entityJournals;
    }

    public void setEntityJournals(List<CoverSleeve008Journal> entityJournals) {
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

    public void setReqId(Integer reqId) {
        this.reqId = reqId;
    }

    public String getReqName() {
        return reqName;
    }

    public void setReqName(String reqName) {
        this.reqName = reqName;
    }
}
