package com.kerellpnz.tnnwebdatabase.entity.unit;

import com.kerellpnz.tnnwebdatabase.entity.unit.detail.CaseBottom;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import java.util.Objects;

@MappedSuperclass
public class BaseUnitWithZk extends BaseUnit {

    @Column(name="ZK")
    private String zk;

    public String getZk() {
        return zk == null ? "" : zk;
    }

    public void setZk(String zk) {
        this.zk = zk;
    }

    public BaseUnitWithZk() {
    }

    public BaseUnitWithZk(BaseUnitWithZk unit, String number) {
        super(unit, number);
        this.zk = unit.getZk();
    }

    @Override
    public String toString() {
        return String.format("ЗК №%s-%s/пл.%s/чер.%s/DN%s - %s", getZk(), getNumber(), getMelt(), getDrawing(), getDn(), getStatus());
    }

    @Override
    public String forReport() {
        return String.format("DN%s, №%s-%s, пл.%s", getDn(), getZk(), getNumber(), getMelt());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BaseUnitWithZk that)) return false;
        return Objects.equals(getNumber(), that.getNumber()) &&
                Objects.equals(getZk(), that.getZk()) &&
                Objects.equals(getDn(), that.getDn()) &&
                Objects.equals(getMelt(), that.getMelt()) &&
                Objects.equals(getCertificate(), that.getCertificate()) &&
                Objects.equals(getDrawing(), that.getDrawing());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getMelt(), getNumber(), getDrawing(), getDn());
    }
}
