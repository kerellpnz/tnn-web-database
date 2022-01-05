package com.kerellpnz.tnnwebdatabase.entity.unit.material;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kerellpnz.tnnwebdatabase.entity.BaseEntity;
import com.kerellpnz.tnnwebdatabase.entity.journal.material.WeldingMaterialJournal;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "weldingmaterials")
public class WeldingMaterial extends BaseEntity {

    @Column(name = "Name")
    @NotBlank(message = "Введите наименование!")
    private String name;
    
    @Column(name = "Certificate")
    @NotBlank(message = "Введите сертификат!")
    private String certificate;
    
    @Column(name = "Batch")
    @NotBlank(message = "Введите партию!")
    private String batch;
    
    @Column(name = "Amount")
    @NotBlank(message = "Введите количество!")
    private String amount;
    
    @Column(name = "Status")
    private String status;
    
    @Column(name = "Comment")
    private String comment;

    @OneToMany(fetch= FetchType.LAZY, mappedBy="detailId", cascade=CascadeType.ALL)
    @JsonIgnore
    private List<WeldingMaterialJournal> entityJournals;

    @Transient
    private Integer tempTCPId;

    public WeldingMaterial() {}

    public WeldingMaterial(WeldingMaterial unit, String number) {
        this.name = unit.getName();
        this.amount = unit.getAmount();
        this.batch = number;
        this.certificate = unit.getCertificate();
        this.status = unit.getStatus();
        this.comment = unit.getComment();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getStatus() {
        return status;
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

    public List<WeldingMaterialJournal> getEntityJournals() {
        return entityJournals;
    }

    public void setEntityJournals(List<WeldingMaterialJournal> entityJournals) {
        this.entityJournals = entityJournals;
    }

    public Integer getTempTCPId() {
        return tempTCPId;
    }

    public void setTempTCPId(Integer tempTCPId) {
        this.tempTCPId = tempTCPId;
    }

    public String forReport() {
        return "партия: " + getBatch();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof WeldingMaterial coating)) return false;
        return Objects.equals(getName(), coating.getName()) &&
                Objects.equals(getCertificate(), coating.getCertificate()) &&
                Objects.equals(getBatch(), coating.getBatch());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCertificate(), getBatch());
    }
}
