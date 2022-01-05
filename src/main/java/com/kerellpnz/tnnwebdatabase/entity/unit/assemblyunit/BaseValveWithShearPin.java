package com.kerellpnz.tnnwebdatabase.entity.unit.assemblyunit;

import com.kerellpnz.tnnwebdatabase.entity.BaseEntity;
import com.kerellpnz.tnnwebdatabase.entity.unit.detail.ShearPin;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "basevalvewithshearpins")
public class BaseValveWithShearPin extends BaseEntity {

    @Column(name = "ShearPinAmount")
    private Integer shearPinAmount;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "BaseValveId")
    private SheetGateValve baseValve;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "ShearPinId")
    private ShearPin shearPin;

    public BaseValveWithShearPin() {}

    public BaseValveWithShearPin(Integer shearPinAmount, ShearPin shearPin) {
        this.shearPinAmount = shearPinAmount;
        this.shearPin = shearPin;
    }

    public BaseValveWithShearPin(Integer shearPinAmount, SheetGateValve baseValve, ShearPin shearPin) {
        this.shearPinAmount = shearPinAmount;
        this.baseValve = baseValve;
        this.shearPin = shearPin;
    }

    public Integer getShearPinAmount() {
        return shearPinAmount;
    }

    public void setShearPinAmount(Integer shearPinAmount) {
        this.shearPinAmount = shearPinAmount;
    }

    public SheetGateValve getBaseValve() {
        return baseValve;
    }

    public void setBaseValve(SheetGateValve valve) {
        this.baseValve = valve;
    }

    public ShearPin getShearPin() {
        return shearPin;
    }

    public void setShearPin(ShearPin shearPin) {
        this.shearPin = shearPin;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BaseValveWithShearPin that = (BaseValveWithShearPin) o;
        return Objects.equals(baseValve, that.baseValve) && Objects.equals(shearPin, that.shearPin);
    }

    @Override
    public int hashCode() {
        return Objects.hash(baseValve, shearPin);
    }
}
