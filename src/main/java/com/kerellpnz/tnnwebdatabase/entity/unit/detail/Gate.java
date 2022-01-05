package com.kerellpnz.tnnwebdatabase.entity.unit.detail;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kerellpnz.tnnwebdatabase.entity.journal.detail.GateJournal;
import com.kerellpnz.tnnwebdatabase.entity.unit.BaseUnitWithZk;
import com.kerellpnz.tnnwebdatabase.entity.unit.assemblyunit.SheetGateValve;
import com.kerellpnz.tnnwebdatabase.entity.unit.material.MetalMaterial;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.persistence.Column;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "gates")
public class Gate extends BaseUnitWithZk {

    @Column(name = "ProtocolControl")
    @DateTimeFormat(pattern = "dd.MM.yyyy")
    private Date protocolControl;

    @Column(name = "ProtocolControlStatus")
    private String protocolControlStatus;

    @OneToOne(mappedBy="gate",
            cascade={CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST,
                    CascadeType.REFRESH})
    @JsonIgnore
    private SheetGateValve sheetGateValve;

    @OneToMany(fetch= FetchType.LAZY, mappedBy="detailId", cascade=CascadeType.ALL)
    @JsonIgnore
    private List<GateJournal> entityJournals;

    @OneToOne(fetch= FetchType.LAZY, cascade= {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name="MetalMaterialId")
    @JsonIgnore
    private MetalMaterial metalMaterial;

    @Transient
    private Integer reqId;

    public Gate() {
        this.setName("Шибер");
        this.setCertificate("-");
    }

    public Gate(Gate unit, String number) {
        super(unit, number);
        this.metalMaterial = unit.getMetalMaterial();
    }

    public Date getProtocolControl() {
        return protocolControl;
    }

    public void setProtocolControl(Date protocolControl) {
        this.protocolControl = protocolControl;
    }

    public String getProtocolControlStatus() {
        return protocolControlStatus;
    }

    public void setProtocolControlStatus(String protocolControlStatus) {
        this.protocolControlStatus = protocolControlStatus;
    }

    public List<GateJournal> getEntityJournals() {
        return entityJournals;
    }

    public void setEntityJournals(List<GateJournal> entityJournals) {
        this.entityJournals = entityJournals;
    }

    public MetalMaterial getMetalMaterial() {
        return metalMaterial;
    }

    public void setMetalMaterial(MetalMaterial metalMaterial) {
        this.metalMaterial = metalMaterial;
    }

    public SheetGateValve getSheetGateValve() {
        return sheetGateValve;
    }

    public void setSheetGateValve(SheetGateValve sheetGateValve) {
        this.sheetGateValve = sheetGateValve;
    }

    public Integer getReqId() {
        return reqId;
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
