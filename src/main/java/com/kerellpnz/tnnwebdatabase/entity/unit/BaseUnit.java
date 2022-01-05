package com.kerellpnz.tnnwebdatabase.entity.unit;

import com.kerellpnz.tnnwebdatabase.entity.BaseEntity;
import com.kerellpnz.tnnwebdatabase.entity.unit.detail.CaseBottom;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;
import javax.validation.constraints.NotBlank;
import java.util.Objects;

@MappedSuperclass
public class BaseUnit extends BaseEntity {

    @Column(name="Name")
    private String name;

    @Column(name="DN")
    @NotBlank(message = "Выберите диаметр!")
    private String dn;

    @Column(name="Number")
    @NotBlank(message = "Введите номер!")
    private String number;

    @Column(name="Drawing")
    @NotBlank(message = "Введите чертеж!")
    private String drawing;

    @Column(name="Material")
    private String material;

    @Column(name="Melt")
    private String melt;

    @Column(name="Certificate")
    @NotBlank(message = "Введите сертификат!")
    private String certificate;

    @Column(name="Status")
    private String status;

    @Column(name="Comment")
    private String comment;

    @Transient
    private Integer tempTCPId;

    public BaseUnit() {

    }

    public BaseUnit(BaseUnit unit, String number) {
        this.name = unit.getName();
        this.dn = unit.getDn();
        this.number = number;
        this.drawing = unit.getDrawing();
        this.material = unit.getMaterial();
        this.melt = unit.getMelt();
        this.certificate = unit.getCertificate();
        this.status = unit.getStatus();
        this.comment = unit.getComment();
    }

    public String getDn() {
        return dn == null ? "" : dn;
    }

    public void setDn(String dn) {
        this.dn = dn;
    }



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number == null ? "" : number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getDrawing() {
        return drawing == null ? "" : drawing;
    }

    public void setDrawing(String drawing) {
        this.drawing = drawing;
    }

    public String getMaterial() {
        return material == null ? "" : material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public String getMelt() {
        return melt == null ? "" : melt;
    }

    public void setMelt(String melt) {
        this.melt = melt;
    }

    public String getCertificate() {
        return certificate == null ? "" : certificate;
    }

    public void setCertificate(String certificate) {
        this.certificate = certificate;
    }

    public String getStatus() {
        return status == null ? "" : status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Integer getTempTCPId() {
        return tempTCPId;
    }

    public void setTempTCPId(Integer tempTCPId) {
        this.tempTCPId = tempTCPId;
    }

    public String forReport() {
        return String.format("DN%s, №%s, пл.%s", getDn(), getNumber(), getMelt());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BaseUnit that)) return false;
        return Objects.equals(getNumber(), that.getNumber()) &&
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
