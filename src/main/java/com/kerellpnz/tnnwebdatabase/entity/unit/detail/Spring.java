package com.kerellpnz.tnnwebdatabase.entity.unit.detail;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kerellpnz.tnnwebdatabase.entity.journal.detail.SpringJournal;
import com.kerellpnz.tnnwebdatabase.entity.unit.BaseUnit;
import com.kerellpnz.tnnwebdatabase.entity.unit.assemblyunit.BaseValveWithSpring;

import javax.persistence.*;
import javax.persistence.Column;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "springs")
public class Spring extends BaseUnit {

    @Column(name="Name")
    @NotBlank(message = "Введите наименование!")
    private String name;

    @Column(name = "Batch")
    @NotBlank(message = "Введите партию!")
    private String batch ;

    @Column(name = "Amount")
    @NotNull(message = "Введите количество!")
    @Min(value=0, message="Значение не может быть отрицательным!")
    private Integer amount ;

    @Column(name = "AmountRemaining")
    private Integer amountRemaining ;

    @OneToMany(fetch= FetchType.LAZY, mappedBy="detailId", cascade=CascadeType.ALL)
    @JsonIgnore
    private List<SpringJournal> entityJournals;

    @OneToMany(mappedBy = "spring")
    @JsonIgnore
    private List<BaseValveWithSpring> baseValveWithSprings = new ArrayList<>();

    public Spring() {
        this.setNumber("Пружина");
        this.setMaterial("51ХФА");
        this.setDn("-");
    }

    public Spring(Spring unit, String number) {
        super(unit, unit.getNumber());
        this.setCertificate(unit.getCertificate());
        this.batch = number;
        this.name = unit.getName();
        this.amount = 0;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    public String getBatch() {
        return batch;
    }

    public void setBatch(String batch) {
        this.batch = batch;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public Integer getAmountRemaining() {
        return amountRemaining;
    }

    public void setAmountRemaining(Integer amountRemaining) {
        this.amountRemaining = amountRemaining;
    }

    public List<SpringJournal> getEntityJournals() {
        return entityJournals;
    }

    public void setEntityJournals(List<SpringJournal> entityJournals) {
        this.entityJournals = entityJournals;
    }

    public List<BaseValveWithSpring> getBaseValveWithSprings() {
        return baseValveWithSprings;
    }

    public void setBaseValveWithSprings(List<BaseValveWithSpring> baseValveWithSprings) {
        this.baseValveWithSprings = baseValveWithSprings;
    }

    @Override
    public String toString() {
        return String.format("Сертификат: №%s/партия: %s - %s", getCertificate(), getBatch(), getStatus());
    }

    @Override
    public String forReport() {
        return String.format("партия: %s, серт.: №%s, кол-во: %d шт.", getBatch(), getCertificate(), getAmount());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Spring that)) return false;
        return Objects.equals(getDrawing(), that.getDrawing()) &&
                Objects.equals(getCertificate(), that.getCertificate()) &&
                Objects.equals(getBatch(), that.getBatch());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getBatch(), getCertificate());
    }
}
