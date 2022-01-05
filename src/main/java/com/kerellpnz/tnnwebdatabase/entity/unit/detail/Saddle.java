package com.kerellpnz.tnnwebdatabase.entity.unit.detail;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.Column;
import com.kerellpnz.tnnwebdatabase.entity.general.PID;
import com.kerellpnz.tnnwebdatabase.entity.journal.detail.SaddleJournal;
import com.kerellpnz.tnnwebdatabase.entity.unit.BaseUnitWithZk;
import com.kerellpnz.tnnwebdatabase.entity.unit.assemblyunit.SheetGateValve;
import com.kerellpnz.tnnwebdatabase.entity.unit.material.MetalMaterial;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "saddles")
public class Saddle extends BaseUnitWithZk {

    @Column(name = "PN")
    private String pn;

    @OneToMany(fetch= FetchType.LAZY, mappedBy="detailId", cascade=CascadeType.ALL)
    @JsonIgnore
    private List<SaddleJournal> entityJournals;

    @OneToOne(fetch= FetchType.LAZY, cascade= {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name="MetalMaterialId")
    @JsonIgnore
    private MetalMaterial metalMaterial;

    @OneToOne(fetch= FetchType.EAGER, cascade= {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name="PIDId")
    private PID pid;

    @ManyToOne(cascade= {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name="BaseValveId")
    @JsonIgnore
    private SheetGateValve sheetGateValve;

    @ManyToMany(fetch=FetchType.LAZY,
            cascade= {CascadeType.PERSIST, CascadeType.MERGE,
                    CascadeType.DETACH, CascadeType.REFRESH})
    @JoinTable(
            name="saddlewithseals",
            joinColumns=@JoinColumn(name="SaddleId"),
            inverseJoinColumns=@JoinColumn(name="FrontalSaddleSealingId")
    )
    @JsonIgnore
    private List<FrontalSaddleSealing> frontalSaddleSeals;

    @Column(name = "TensileStrength")
    private String tensileStrength;

    @Column(name = "Grooving")
    private String grooving;

    @Transient
    private Integer tempSealId;
    @Transient
    private Integer reqId;

    @Transient
    private List<SaddleJournal> inputControlJournals = new ArrayList<>();
    @Transient
    private List<SaddleJournal> mechanicalJournals = new ArrayList<>();
    @Transient
    private Integer inputTCPId;
    @Transient
    private Integer mechTCPId;

    public Saddle() {
        this.setName("Обойма");
        this.setMaterial("09Г2С");
        this.setCertificate("-");
    }

    public Saddle(Saddle unit, String number) {
        super(unit, number);
        this.pn = unit.getPn();
        this.metalMaterial = unit.getMetalMaterial();
    }

    public Integer getReqId() {
        return reqId;
    }

    public void setReqId(Integer reqId) {
        this.reqId = reqId;
    }

    public String getPn() {
        return pn == null ? "" : pn;
    }

    public void setPn(String pn) {
        this.pn = pn;
    }

    public List<SaddleJournal> getEntityJournals() {
        return entityJournals;
    }

    public void setEntityJournals(List<SaddleJournal> entityJournals) {
        this.entityJournals = entityJournals;
    }

    public List<FrontalSaddleSealing> getFrontalSaddleSeals() {
        return frontalSaddleSeals;
    }

    public void setFrontalSaddleSeals(List<FrontalSaddleSealing> frontalSaddleSeals) {
        this.frontalSaddleSeals = frontalSaddleSeals;
    }

    public MetalMaterial getMetalMaterial() {
        return metalMaterial;
    }

    public void setMetalMaterial(MetalMaterial metalMaterial) {
        this.metalMaterial = metalMaterial;
    }

    public PID getPid() {
        return pid;
    }

    public void setPid(PID pid) {
        this.pid = pid;
    }

    public Integer getTempSealId() {
        return tempSealId;
    }

    public void setTempSealId(Integer tempSealId) {
        this.tempSealId = tempSealId;
    }

    public List<SaddleJournal> getInputControlJournals() {
        return inputControlJournals;
    }

    public void setInputControlJournals(List<SaddleJournal> inputControlJournals) {
        this.inputControlJournals = inputControlJournals;
    }

    public List<SaddleJournal> getMechanicalJournals() {
        return mechanicalJournals;
    }

    public void setMechanicalJournals(List<SaddleJournal> mechanicalJournals) {
        this.mechanicalJournals = mechanicalJournals;
    }

    public Integer getInputTCPId() {
        return inputTCPId;
    }

    public void setInputTCPId(Integer inputTCPId) {
        this.inputTCPId = inputTCPId;
    }

    public Integer getMechTCPId() {
        return mechTCPId;
    }

    public void setMechTCPId(Integer mechTCPId) {
        this.mechTCPId = mechTCPId;
    }

    public SheetGateValve getSheetGateValve() {
        return sheetGateValve;
    }

    public void setSheetGateValve(SheetGateValve sheetGateValve) {
        this.sheetGateValve = sheetGateValve;
    }

    public String getTensileStrength() {
        return tensileStrength;
    }

    public void setTensileStrength(String tensileStrength) {
        this.tensileStrength = tensileStrength;
    }

    public String getGrooving() {
        return grooving;
    }

    public void setGrooving(String grooving) {
        this.grooving = grooving;
    }
}
