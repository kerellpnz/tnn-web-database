package com.kerellpnz.tnnwebdatabase.entity.unit.material;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kerellpnz.tnnwebdatabase.entity.BaseEntity;
import com.kerellpnz.tnnwebdatabase.entity.journal.material.ControlWeldJournal;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "controlwelds")
public class ControlWeld extends BaseEntity {

    @Column(name = "Name")
    private String name;

    @Column(name = "Number")
    @NotBlank(message = "Введите номер!")
    private String number;

    @Column(name = "MechanicalPropertiesReport")
    @NotBlank(message = "Введите номер протокола или поставьте \"-\"!")
    private String mechanicalPropertiesReport;

    @Column(name = "MetallographicPropertiesReport")
    @NotBlank(message = "Введите номер протокола или поставьте \"-\"!")
    private String metallographicPropertiesReport;

    @Column(name = "WeldingMethod")
    @NotBlank(message = "Введите способ сварки")
    private String weldingMethod;

    @Column(name = "Welder")
    @NotBlank(message = "Введите сварщика")
    private String welder;

    @Column(name = "Stamp")
    @NotBlank(message = "Введите клеймо!")
    private String stamp;

    @Column(name = "FirstMaterial")
    @NotBlank(message = "Введите первый материал!")
    private String firstMaterial;

    @Column(name = "SecondMaterial")
    @NotBlank(message = "Введите второй материал!")
    private String secondMaterial;

    @Column(name = "Size")
    @NotBlank(message = "Введите размер!")
    private String size;

    @Column(name = "Status")
    private String status;

    @Column(name = "BeginingDate")
    @NotNull(message = "Введите дату!")
    @DateTimeFormat(pattern = "dd.MM.yyyy")
    private Date beginingDate;

    @Column(name = "ExpiryDate")
    @NotNull(message = "Введите дату!")
    @DateTimeFormat(pattern = "dd.MM.yyyy")
    private Date expiryDate;

    @Column(name = "Comment")
    private String comment;

    @Transient
    private Integer tempTCPId;

    @OneToMany(fetch= FetchType.LAZY, mappedBy="detailId", cascade=CascadeType.ALL)
    @JsonIgnore
    private List<ControlWeldJournal> entityJournals;

    public ControlWeld() {
        this.setName("КСС");
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

    public String getMechanicalPropertiesReport() {
        return mechanicalPropertiesReport;
    }

    public void setMechanicalPropertiesReport(String mechanicalPropertiesReport) {
        this.mechanicalPropertiesReport = mechanicalPropertiesReport;
    }

    public String getMetallographicPropertiesReport() {
        return metallographicPropertiesReport;
    }

    public void setMetallographicPropertiesReport(String metallographicPropertiesReport) {
        this.metallographicPropertiesReport = metallographicPropertiesReport;
    }

    public String getWeldingMethod() {
        return weldingMethod;
    }

    public void setWeldingMethod(String weldingMethod) {
        this.weldingMethod = weldingMethod;
    }

    public String getWelder() {
        return welder;
    }

    public void setWelder(String welder) {
        this.welder = welder;
    }

    public String getStamp() {
        return stamp;
    }

    public void setStamp(String stamp) {
        this.stamp = stamp;
    }

    public String getFirstMaterial() {
        return firstMaterial;
    }

    public void setFirstMaterial(String firstMaterial) {
        this.firstMaterial = firstMaterial;
    }

    public String getSecondMaterial() {
        return secondMaterial;
    }

    public void setSecondMaterial(String secondMaterial) {
        this.secondMaterial = secondMaterial;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getBeginingDate() {
        return beginingDate;
    }

    public void setBeginingDate(Date beginingDate) {
        this.beginingDate = beginingDate;
    }

    public Date getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public List<ControlWeldJournal> getEntityJournals() {
        return entityJournals;
    }

    public void setEntityJournals(List<ControlWeldJournal> entityJournals) {
        this.entityJournals = entityJournals;
    }

    public Integer getTempTCPId() {
        return tempTCPId;
    }

    public void setTempTCPId(Integer tempTCPId) {
        this.tempTCPId = tempTCPId;
    }

    public String forReport() {
        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
        return "№ " + getNumber() + ", " + getWeldingMethod() + ", до " + formatter.format(getExpiryDate());
    }
}
