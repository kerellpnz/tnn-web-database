package com.kerellpnz.tnnwebdatabase.entity.unit.detail;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kerellpnz.tnnwebdatabase.entity.journal.detail.ScrewNutJournal;
import com.kerellpnz.tnnwebdatabase.entity.unit.BaseUnit;
import com.kerellpnz.tnnwebdatabase.entity.unit.assemblyunit.BaseValveWithScrewNut;

import javax.persistence.*;
import javax.persistence.Column;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "screwnuts")
public class ScrewNut extends BaseUnit {

    @Column(name="Batch")
    @NotBlank(message = "Введите партию!")
    private String batch;

    @Column(name = "Amount")
    @NotNull(message = "Введите количество!")
    @Min(value=0, message="Значение не может быть отрицательным!")
    private Integer amount ;

    @Column(name = "AmountRemaining")
    private Integer amountRemaining ;

    @OneToMany(fetch= FetchType.LAZY, mappedBy="detailId", cascade=CascadeType.ALL)
    @JsonIgnore
    private List<ScrewNutJournal> entityJournals;

    @OneToMany(mappedBy = "screwNut")
    @JsonIgnore
    private List<BaseValveWithScrewNut> baseValveWithScrewNuts = new ArrayList<>();

    public ScrewNut() {
        this.setName("Гайка");
        this.setNumber("-");
        this.setMaterial("40Х");
    }

    public ScrewNut(ScrewNut unit, String number) {
        super(unit, number);
        this.setBatch(number);
        this.setCertificate(unit.getCertificate());
        this.setAmount(0);
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

    public List<ScrewNutJournal> getEntityJournals() {
        return entityJournals;
    }

    public void setEntityJournals(List<ScrewNutJournal> entityJournals) {
        this.entityJournals = entityJournals;
    }

    public List<BaseValveWithScrewNut> getBaseValveWithScrewNuts() {
        return baseValveWithScrewNuts;
    }

    public void setBaseValveWithScrewNuts(List<BaseValveWithScrewNut> baseValveWithScrewNuts) {
        this.baseValveWithScrewNuts = baseValveWithScrewNuts;
    }

    @Override
    public String toString() {
        return String.format("Сертификат: №%s/партия: %s/DN%s - %s", getCertificate(), getBatch(), getDn(), getStatus());
    }

    @Override
    public String forReport() {
        return String.format("DN%s, партия: %s, серт.: №%s", getDn(), getBatch(), getCertificate());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ScrewNut that)) return false;
        return Objects.equals(getBatch(), that.getBatch()) &&
                Objects.equals(getDrawing(), that.getDrawing()) &&
                Objects.equals(getCertificate(), that.getCertificate());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getBatch(), getCertificate());
    }
}
