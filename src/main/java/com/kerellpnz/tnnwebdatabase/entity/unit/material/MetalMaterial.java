package com.kerellpnz.tnnwebdatabase.entity.unit.material;

import com.kerellpnz.tnnwebdatabase.entity.BaseEntity;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "metalmaterials")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="Discriminator")
public class MetalMaterial extends BaseEntity {

    @Column(name="Name")
    private String name;

    @Column(name="Number")
    @NotBlank(message = "Введите номер!")
    private String number;

    @Column(name="Material")
    @NotBlank(message = "Введите материал!")
    private String material;

    @Column(name="Melt")
    @NotBlank(message = "Введите плавку!")
    private String melt;

    @Column(name="Certificate")
    @NotBlank(message = "Введите сертификат!")
    private String certificate;

    @Column(name="Batch")
    @NotBlank(message = "Введите партию!")
    private String batch;

    @Column(name="Status")
    private String status;

    @Column(name="FirstSize")
    private String firstSize;

    @Column(name="SecondSize")
    private String secondSize;

    @Column(name="ThirdSize")
    @NotBlank(message = "Введите размер!")
    private String thirdSize;

    @Column(name="Comment")
    private String comment;

    @Transient
    private Integer tempTCPId;

    public MetalMaterial() {

    }

    public MetalMaterial(MetalMaterial metalMaterial, String number) {
        this.name = metalMaterial.getName();
        this.number = number;
        this.material = metalMaterial.getMaterial();
        this.melt = metalMaterial.getMelt();
        this.certificate = metalMaterial.getCertificate();
        this.batch = metalMaterial.getBatch();
        this.status = metalMaterial.getStatus();
        this.firstSize = metalMaterial.getFirstSize();
        this.secondSize = metalMaterial.getSecondSize();
        this.thirdSize = metalMaterial.getThirdSize();
        this.comment = metalMaterial.getComment();
    }

    public Integer getTempTCPId() {
        return tempTCPId;
    }

    public void setTempTCPId(Integer tempTCPId) {
        this.tempTCPId = tempTCPId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public String getMelt() {
        return melt;
    }

    public void setMelt(String melt) {
        this.melt = melt;
    }

    public String getCertificate() {
        return certificate;
    }

    public void setCertificate(String certificate) {
        this.certificate = certificate;
    }

    public String getBatch() {
        return batch;
    }

    public void setBatch(String batch) {
        this.batch = batch;
    }

    public String getStatus() {
        return status == null ? "" : status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getFirstSize() {
        return firstSize;
    }

    public void setFirstSize(String firstSize) {
        this.firstSize = firstSize;
    }

    public String getSecondSize() {
        return secondSize;
    }

    public void setSecondSize(String secondSize) {
        this.secondSize = secondSize;
    }

    public String getThirdSize() {
        return thirdSize;
    }

    public void setThirdSize(String thirdSize) {
        this.thirdSize = thirdSize;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
