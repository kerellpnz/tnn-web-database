package com.kerellpnz.tnnwebdatabase.entity.unit.assemblyunit;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.Column;
import com.kerellpnz.tnnwebdatabase.entity.journal.assemblyunit.SheetGateValveCaseJournal;
import com.kerellpnz.tnnwebdatabase.entity.unit.detail.Ring;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Entity
public class SheetGateValveCase extends BaseWeldValveDetail {

    @Column(name = "DateOfWashing")
    @DateTimeFormat(pattern = "dd.MM.yyyy")
    private Date dateOfWashing;

    @Column(name="Melt")
    @NotBlank(message = "Введите плавку!")
    private String melt;

    @OneToOne(mappedBy="valveCase",
            cascade={CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST,
                    CascadeType.REFRESH})
    @JsonIgnore
    private SheetGateValve sheetGateValve;

    @OneToMany(fetch= FetchType.LAZY, mappedBy="sheetGateValveCase", cascade= CascadeType.ALL)
    @JsonIgnore
    private List<Ring> rings;

    @Transient
    private Integer tempRingId;

    @OneToMany(fetch= FetchType.LAZY, mappedBy="detailId", cascade=CascadeType.ALL)
    @JsonIgnore
    private List<SheetGateValveCaseJournal> entityJournals;

    @Transient
    private List<SheetGateValveCaseJournal> inputControlJournals = new ArrayList<>();
    @Transient
    private List<SheetGateValveCaseJournal> weldJournals = new ArrayList<>();
    @Transient
    private List<SheetGateValveCaseJournal> mechanicalJournals = new ArrayList<>();
    @Transient
    private List<SheetGateValveCaseJournal> documentJournals = new ArrayList<>();

    public SheetGateValveCase() {
        this.setName("Корпус ЗШ");
        this.setMaterial("20ГМЛ");
        this.setCertificate("-");
    }

    public SheetGateValveCase(SheetGateValveCase unit, String number) {
        super(unit, number);
        this.setPn(unit.getPn());
        this.melt = unit.getMelt();
    }

    @Override
    public String getMelt() {
        return melt == null ? "" : melt;
    }

    @Override
    public void setMelt(String melt) {
        this.melt = melt;
    }

    public Date getDateOfWashing() {
        return dateOfWashing;
    }

    public void setDateOfWashing(Date dateOfWashing) {
        this.dateOfWashing = dateOfWashing;
    }

    public List<Ring> getRings() {
        return rings;
    }

    public void setRings(List<Ring> rings) {
        this.rings = rings;
    }

    public List<SheetGateValveCaseJournal> getEntityJournals() {
        return entityJournals;
    }

    public void setEntityJournals(List<SheetGateValveCaseJournal> entityJournals) {
        this.entityJournals = entityJournals;
    }

    public List<SheetGateValveCaseJournal> getInputControlJournals() {
        return inputControlJournals;
    }

    public void setInputControlJournals(List<SheetGateValveCaseJournal> inputControlJournals) {
        this.inputControlJournals = inputControlJournals;
    }

    public List<SheetGateValveCaseJournal> getWeldJournals() {
        return weldJournals;
    }

    public void setWeldJournals(List<SheetGateValveCaseJournal> weldJournals) {
        this.weldJournals = weldJournals;
    }

    public List<SheetGateValveCaseJournal> getMechanicalJournals() {
        return mechanicalJournals;
    }

    public void setMechanicalJournals(List<SheetGateValveCaseJournal> mechanicalJournals) {
        this.mechanicalJournals = mechanicalJournals;
    }

    public List<SheetGateValveCaseJournal> getDocumentJournals() {
        return documentJournals;
    }

    public void setDocumentJournals(List<SheetGateValveCaseJournal> documentJournals) {
        this.documentJournals = documentJournals;
    }

    public Integer getTempRingId() {
        return tempRingId;
    }

    public void setTempRingId(Integer tempRingId) {
        this.tempRingId = tempRingId;
    }

    public SheetGateValve getSheetGateValve() {
        return sheetGateValve;
    }

    public void setSheetGateValve(SheetGateValve sheetGateValve) {
        this.sheetGateValve = sheetGateValve;
    }
}
