package com.kerellpnz.tnnwebdatabase.entity.unit.assemblyunit;

import com.kerellpnz.tnnwebdatabase.entity.BaseEntity;
import com.kerellpnz.tnnwebdatabase.entity.unit.detail.Spring;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "basevalvewithsprings")
public class BaseValveWithSpring extends BaseEntity {

    @Column(name = "springAmount")
    private Integer springAmount;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "BaseValveId")
    private SheetGateValve baseValve;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "SpringId")
    private Spring spring;

    public BaseValveWithSpring() {}

    public BaseValveWithSpring(Integer springAmount, Spring spring) {
        this.springAmount = springAmount;
        this.spring = spring;
    }

    public Integer getSpringAmount() {
        return springAmount;
    }

    public void setSpringAmount(Integer springAmount) {
        this.springAmount = springAmount;
    }

    public SheetGateValve getBaseValve() {
        return baseValve;
    }

    public void setBaseValve(SheetGateValve baseValve) {
        this.baseValve = baseValve;
    }

    public Spring getSpring() {
        return spring;
    }

    public void setSpring(Spring spring) {
        this.spring = spring;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BaseValveWithSpring that = (BaseValveWithSpring) o;
        return Objects.equals(baseValve, that.baseValve) && Objects.equals(spring, that.spring);
    }

    @Override
    public int hashCode() {
        return Objects.hash(baseValve, spring);
    }
}
