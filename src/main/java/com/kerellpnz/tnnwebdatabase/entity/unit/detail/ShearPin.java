package com.kerellpnz.tnnwebdatabase.entity.unit.detail;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kerellpnz.tnnwebdatabase.entity.journal.detail.ShearPinJournal;
import com.kerellpnz.tnnwebdatabase.entity.unit.BaseUnit;
import com.kerellpnz.tnnwebdatabase.entity.unit.assemblyunit.BaseValveWithShearPin;

import javax.persistence.*;
import javax.persistence.Column;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "shearpins")
public class ShearPin extends BaseUnit {

    @Column(name = "Pull")
    @NotBlank(message = "Введите тягу!")
    private String pull;

    @Column(name = "Diameter")
    @NotBlank(message = "Введите диаметр!")
    private String diameter;

    @Column(name = "TensileStrength")
    @NotBlank(message = "Введите предел прочности!")
    private String tensileStrength;

    @Column(name = "Melt")
    @NotBlank(message = "Введите плавку!")
    private String melt;

    @Column(name = "Amount")
    @NotNull(message = "Введите количество!")
    private Integer amount;

    @Column(name = "AmountRemaining")
    private Integer amountRemaining;

    @OneToMany(fetch= FetchType.LAZY, mappedBy="detailId", cascade=CascadeType.ALL)
    @JsonIgnore
    private List<ShearPinJournal> entityJournals;

    @OneToMany(mappedBy = "shearPin")
    @JsonIgnore
    private List<BaseValveWithShearPin> baseValveWithShearPins = new ArrayList<>();

    public ShearPin() {
        this.setName("Штифт");
        this.setMaterial("14Х17Н2");
        this.setCertificate("-");
    }

    public ShearPin(ShearPin unit, String number, String diameter,
                    String tensileStrength, String pull, Integer amount) {
        super(unit, number);
        this.setMelt(unit.getMelt());
        this.setDiameter(diameter);
        this.setTensileStrength(tensileStrength);
        this.setPull(pull);
        this.setAmount(amount);
    }

    public List<ShearPinJournal> getEntityJournals() {
        return entityJournals;
    }

    public void setEntityJournals(List<ShearPinJournal> entityJournals) {
        this.entityJournals = entityJournals;
    }

    @Override
    public String getMelt() {
        return melt;
    }

    @Override
    public void setMelt(String melt) {
        this.melt = melt;
    }

    public String getPull() {
        return pull == null ? "" : pull;
    }

    public void setPull(String pull) {
        this.pull = pull;
    }

    public String getDiameter() {
        return diameter == null ? "" : diameter;
    }

    public void setDiameter(String diameter) {
        this.diameter = diameter;
    }

    public String getTensileStrength() {
        return tensileStrength == null ? "" : tensileStrength;
    }

    public void setTensileStrength(String tensileStrength) {
        this.tensileStrength = tensileStrength;
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

    public List<BaseValveWithShearPin> getBaseValveWithShearPins() {
        return baseValveWithShearPins;
    }

    public void setBaseValveWithShearPins(List<BaseValveWithShearPin> baseValveWithShearPins) {
        this.baseValveWithShearPins = baseValveWithShearPins;
    }

    @Override
    public String toString() {
        return String.format("ЗК №%s/Ф%s/Тяга: %s/%sМПа/DN%s - %s", getNumber(), getDiameter(), getPull(), getTensileStrength(), getDn(), getStatus());
    }

    @Override
    public String forReport() {
        return String.format("DN%s, ЗК: %s, Ф%s, тяга: %s, кол-во: %d", getDn(), getNumber(), getDiameter(), getPull(), getAmount());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ShearPin that)) return false;
        return Objects.equals(getNumber(), that.getNumber()) &&
                Objects.equals(getPull(), that.getPull());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getNumber(), getPull());
    }
}
