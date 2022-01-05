package com.kerellpnz.tnnwebdatabase.entity.unit.assemblyunit;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kerellpnz.tnnwebdatabase.entity.journal.assemblyunit.SheetGateValveCoverJournal;
import com.kerellpnz.tnnwebdatabase.entity.unit.detail.CoverSleeve;
import com.kerellpnz.tnnwebdatabase.entity.unit.detail.FrontalSaddleSealing;
import com.kerellpnz.tnnwebdatabase.entity.unit.detail.Spindle;
import com.kerellpnz.tnnwebdatabase.entity.unit.material.MetalMaterial;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
public class SheetGateValveCover extends BaseWeldValveDetail {

    @javax.persistence.Column(name = "BallValveDrainage")
    private String ballValveDrainage;

    @javax.persistence.Column(name = "BallValveDraining")
    private String ballValveDraining;

    @OneToOne(mappedBy="valveCover",
            cascade={CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST,
                    CascadeType.REFRESH})
    @JsonIgnore
    private SheetGateValve sheetGateValve;

    @ManyToMany(fetch=FetchType.LAZY,
            cascade= {CascadeType.PERSIST, CascadeType.MERGE,
                    CascadeType.DETACH, CascadeType.REFRESH})
    @JoinTable(
            name="coverwithseals",
            joinColumns=@JoinColumn(name="BaseWeldValveId"),
            inverseJoinColumns=@JoinColumn(name="SealingId")
    )
    @JsonIgnore
    private List<FrontalSaddleSealing> frontalSaddleSeals;

    @OneToOne(fetch= FetchType.LAZY, cascade= {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name="CoverSleeveId")
    @JsonIgnore
    private CoverSleeve coverSleeve;

    @OneToOne(fetch= FetchType.LAZY, cascade= {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name="SpindleId")
    @JsonIgnore
    private Spindle spindle;

    @OneToOne(fetch= FetchType.LAZY, cascade= {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name="ColumnId")
    @JsonIgnore
    private com.kerellpnz.tnnwebdatabase.entity.unit.detail.Column column;

    @OneToOne(fetch= FetchType.LAZY, cascade= {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name="MetalMaterialId")
    @JsonIgnore
    private MetalMaterial metalMaterial;

    @OneToMany(fetch= FetchType.LAZY, mappedBy="detailId", cascade=CascadeType.ALL)
    @JsonIgnore
    private List<SheetGateValveCoverJournal> entityJournals;

    @Transient
    private List<SheetGateValveCoverJournal> inputControlJournals = new ArrayList<>();
    @Transient
    private List<SheetGateValveCoverJournal> weldJournals = new ArrayList<>();
    @Transient
    private List<SheetGateValveCoverJournal> mechanicalJournals = new ArrayList<>();
    @Transient
    private List<SheetGateValveCoverJournal> documentJournals = new ArrayList<>();
    @Transient
    private List<SheetGateValveCoverJournal> assemblyJournals = new ArrayList<>();
    @Transient
    private Integer tempSealId;

    public SheetGateValveCover() {
        this.setName("Крышка ЗШ");
        this.setMaterial("-");
        this.setCertificate("-");
    }

    public SheetGateValveCover(SheetGateValveCover unit, String number) {
        super(unit, number);
        this.setPn(unit.getPn());
    }

    public CoverSleeve getCoverSleeve() {
        return coverSleeve;
    }

    public void setCoverSleeve(CoverSleeve coverSleeve) {
        this.coverSleeve = coverSleeve;
    }

    public Spindle getSpindle() {
        return spindle;
    }

    public void setSpindle(Spindle spindle) {
        this.spindle = spindle;
    }

    public com.kerellpnz.tnnwebdatabase.entity.unit.detail.Column getColumn() {
        return column;
    }

    public void setColumn(com.kerellpnz.tnnwebdatabase.entity.unit.detail.Column column) {
        this.column = column;
    }

    public String getBallValveDrainage() {
        return ballValveDrainage;
    }

    public void setBallValveDrainage(String ballValveDrainage) {
        this.ballValveDrainage = ballValveDrainage;
    }

    public String getBallValveDraining() {
        return ballValveDraining;
    }

    public void setBallValveDraining(String ballValveDraining) {
        this.ballValveDraining = ballValveDraining;
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

    public List<SheetGateValveCoverJournal> getEntityJournals() {
        return entityJournals;
    }

    public void setEntityJournals(List<SheetGateValveCoverJournal> entityJournals) {
        this.entityJournals = entityJournals;
    }

    public List<SheetGateValveCoverJournal> getInputControlJournals() {
        return inputControlJournals;
    }

    public void setInputControlJournals(List<SheetGateValveCoverJournal> inputControlJournals) {
        this.inputControlJournals = inputControlJournals;
    }

    public List<SheetGateValveCoverJournal> getWeldJournals() {
        return weldJournals;
    }

    public void setWeldJournals(List<SheetGateValveCoverJournal> weldJournals) {
        this.weldJournals = weldJournals;
    }

    public List<SheetGateValveCoverJournal> getMechanicalJournals() {
        return mechanicalJournals;
    }

    public void setMechanicalJournals(List<SheetGateValveCoverJournal> mechanicalJournals) {
        this.mechanicalJournals = mechanicalJournals;
    }

    public List<SheetGateValveCoverJournal> getDocumentJournals() {
        return documentJournals;
    }

    public void setDocumentJournals(List<SheetGateValveCoverJournal> documentJournals) {
        this.documentJournals = documentJournals;
    }

    public List<SheetGateValveCoverJournal> getAssemblyJournals() {
        return assemblyJournals;
    }

    public void setAssemblyJournals(List<SheetGateValveCoverJournal> assemblyJournals) {
        this.assemblyJournals = assemblyJournals;
    }

    public Integer getTempSealId() {
        return tempSealId;
    }

    public void setTempSealId(Integer tempSealId) {
        this.tempSealId = tempSealId;
    }

    public SheetGateValve getSheetGateValve() {
        return sheetGateValve;
    }

    public void setSheetGateValve(SheetGateValve sheetGateValve) {
        this.sheetGateValve = sheetGateValve;
    }
}
