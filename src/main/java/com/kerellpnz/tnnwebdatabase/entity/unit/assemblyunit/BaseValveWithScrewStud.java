package com.kerellpnz.tnnwebdatabase.entity.unit.assemblyunit;

import com.kerellpnz.tnnwebdatabase.entity.BaseEntity;
import com.kerellpnz.tnnwebdatabase.entity.unit.detail.ScrewStud;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "basevalvewithscrewstuds")
public class BaseValveWithScrewStud extends BaseEntity {

    @Column(name = "screwStudAmount")
    private Integer screwStudAmount;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "BaseValveId")
    private SheetGateValve baseValve;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "ScrewStudId")
    private ScrewStud screwStud;

    public BaseValveWithScrewStud() {}

    public BaseValveWithScrewStud(Integer screwStudAmount, ScrewStud screwStud) {
        this.screwStudAmount = screwStudAmount;
        this.screwStud = screwStud;
    }

    public Integer getScrewStudAmount() {
        return screwStudAmount;
    }

    public void setScrewStudAmount(Integer screwStudAmount) {
        this.screwStudAmount = screwStudAmount;
    }

    public SheetGateValve getBaseValve() {
        return baseValve;
    }

    public void setBaseValve(SheetGateValve baseValve) {
        this.baseValve = baseValve;
    }

    public ScrewStud getScrewStud() {
        return screwStud;
    }

    public void setScrewStud(ScrewStud screwStud) {
        this.screwStud = screwStud;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BaseValveWithScrewStud that = (BaseValveWithScrewStud) o;
        return Objects.equals(baseValve, that.baseValve) && Objects.equals(screwStud, that.screwStud);
    }

    @Override
    public int hashCode() {
        return Objects.hash(baseValve, screwStud);
    }
}
