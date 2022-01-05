package com.kerellpnz.tnnwebdatabase.entity.unit.assemblyunit;

import com.kerellpnz.tnnwebdatabase.entity.BaseEntity;
import com.kerellpnz.tnnwebdatabase.entity.unit.detail.ScrewNut;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "basevalvewithscrewnuts")
public class BaseValveWithScrewNut extends BaseEntity {

    @Column(name = "screwNutAmount")
    private Integer screwNutAmount;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "BaseValveId")
    private SheetGateValve baseValve;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "ScrewNutId")
    private ScrewNut screwNut;

    public BaseValveWithScrewNut() {}

    public BaseValveWithScrewNut(Integer screwNutAmount, ScrewNut screwNut) {
        this.screwNutAmount = screwNutAmount;
        this.screwNut = screwNut;
    }

    public Integer getScrewNutAmount() {
        return screwNutAmount;
    }

    public void setScrewNutAmount(Integer screwNutAmount) {
        this.screwNutAmount = screwNutAmount;
    }

    public SheetGateValve getBaseValve() {
        return baseValve;
    }

    public void setBaseValve(SheetGateValve baseValve) {
        this.baseValve = baseValve;
    }

    public ScrewNut getScrewNut() {
        return screwNut;
    }

    public void setScrewNut(ScrewNut screwNut) {
        this.screwNut = screwNut;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BaseValveWithScrewNut that = (BaseValveWithScrewNut) o;
        return Objects.equals(baseValve, that.baseValve) && Objects.equals(screwNut, that.screwNut);
    }

    @Override
    public int hashCode() {
        return Objects.hash(baseValve, screwNut);
    }
}
