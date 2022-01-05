package com.kerellpnz.tnnwebdatabase.entity.unit.detail;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kerellpnz.tnnwebdatabase.entity.journal.detail.FlangeJournal;
import com.kerellpnz.tnnwebdatabase.entity.unit.BaseUnitWithZk;
import com.kerellpnz.tnnwebdatabase.entity.unit.assemblyunit.BaseWeldValveDetail;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "coverflanges")
public class Flange extends BaseUnitWithZk {

    @OneToMany(fetch= FetchType.LAZY, mappedBy="detailId", cascade= CascadeType.ALL)
    @JsonIgnore
    private List<FlangeJournal> entityJournals;

    @OneToOne(mappedBy="flange",
            cascade={CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST,
                    CascadeType.REFRESH})
    @JsonIgnore
    private BaseWeldValveDetail baseWeldValveDetail;

    @Transient
    private Integer reqId;
    @Transient
    private String reqName;

    public Flange() {
        this.setName("Фланец");
        this.setMaterial("09Г2С");
    }

    public Flange(Flange unit, String number) {
        super(unit, number);
    }

    public List<FlangeJournal> getEntityJournals() {
        return entityJournals;
    }

    public void setEntityJournals(List<FlangeJournal> entityJournals) {
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

    @Override
    public String toString() {
        return String.format("№%s/пл.%s/чер.%s/DN%s - %s", getNumber(), getMelt(), getDrawing(), getDn(), getStatus());
    }

    @Override
    public String forReport() {
        return String.format("DN%s, №%s, пл.%s, чертеж: %s", getDn(), getNumber(), getMelt(), getDrawing());
    }
}
