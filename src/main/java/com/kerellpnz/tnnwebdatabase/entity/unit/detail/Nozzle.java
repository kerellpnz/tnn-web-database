package com.kerellpnz.tnnwebdatabase.entity.unit.detail;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kerellpnz.tnnwebdatabase.entity.general.PID;
import com.kerellpnz.tnnwebdatabase.entity.journal.detail.NozzleJournal;
import com.kerellpnz.tnnwebdatabase.entity.unit.BaseUnitWithZk;
import com.kerellpnz.tnnwebdatabase.entity.unit.assemblyunit.SheetGateValve;
import com.kerellpnz.tnnwebdatabase.entity.unit.material.MetalMaterial;

import javax.persistence.*;
import javax.persistence.Column;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "nozzles")
public class Nozzle extends BaseUnitWithZk {

    @Column(name = "PN")
    private String pn;

    @Column(name = "TensileStrength")
    private String tensileStrength;

    @Column(name = "Grooving")
    private String grooving;

    @OneToOne(fetch= FetchType.EAGER, cascade= {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name="PIDId")
    private PID pid;

    @ManyToOne(cascade= {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name="BaseValveId")
    @JsonIgnore
    private SheetGateValve sheetGateValve;

    @OneToOne(fetch= FetchType.LAZY, cascade= {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name="MetalMaterialId")
    @JsonIgnore
    private MetalMaterial metalMaterial;

    @OneToMany(fetch= FetchType.LAZY, mappedBy="detailId", cascade=CascadeType.ALL)
    @JsonIgnore
    private List<NozzleJournal> entityJournals;

    @Transient
    private List<NozzleJournal> inputControlJournals = new ArrayList<>();
    @Transient
    private List<NozzleJournal> mechanicalJournals = new ArrayList<>();
    @Transient
    private Integer inputTCPId;
    @Transient
    private Integer mechTCPId;
    @Transient
    private Integer reqId;

    public Nozzle() {
        this.setName("Катушка");
        this.setMaterial("09Г2С");
        this.setMaterial("-");
    }

    public Nozzle(Nozzle unit, String number) {
        super(unit, number);
        this.tensileStrength = unit.getTensileStrength();
        this.grooving = unit.getGrooving();
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

    public String getTensileStrength() {
        return tensileStrength == null ? "" : tensileStrength;
    }

    public void setTensileStrength(String tensileStrength) {
        this.tensileStrength = tensileStrength;
    }

    public String getGrooving() {
        return grooving == null ? "" : grooving;
    }

    public void setGrooving(String grooving) {
        this.grooving = grooving;
    }

    public PID getPid() {
        return pid;
    }

    public void setPid(PID pid) {
        this.pid = pid;
    }

    public SheetGateValve getSheetGateValve() {
        return sheetGateValve;
    }

    public void setSheetGateValve(SheetGateValve sheetGateValve) {
        this.sheetGateValve = sheetGateValve;
    }

    public MetalMaterial getMetalMaterial() {
        return metalMaterial;
    }

    public void setMetalMaterial(MetalMaterial metalMaterial) {
        this.metalMaterial = metalMaterial;
    }

    public List<NozzleJournal> getEntityJournals() {
        return entityJournals;
    }

    public void setEntityJournals(List<NozzleJournal> entityJournals) {
        this.entityJournals = entityJournals;
    }

    public List<NozzleJournal> getInputControlJournals() {
        return inputControlJournals;
    }

    public void setInputControlJournals(List<NozzleJournal> inputControlJournals) {
        this.inputControlJournals = inputControlJournals;
    }

    public List<NozzleJournal> getMechanicalJournals() {
        return mechanicalJournals;
    }

    public void setMechanicalJournals(List<NozzleJournal> mechanicalJournals) {
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
}
