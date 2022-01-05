package com.kerellpnz.tnnwebdatabase.entity.unit.material;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kerellpnz.tnnwebdatabase.entity.BaseEntity;
import com.kerellpnz.tnnwebdatabase.entity.journal.BaseJournal;
import com.kerellpnz.tnnwebdatabase.entity.unit.BaseUnit;
import com.kerellpnz.tnnwebdatabase.entity.unit.assemblyunit.SheetGateValve;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "baseanticorrosivecoatings")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="Discriminator")
public class BaseAnticorrosiveCoating extends BaseEntity {

    @Column(name="Name")
    @NotBlank(message = "Введите название АКП")
    private String name;

    @Column(name="Factory")
    private String factory;

    @Column(name="Certificate")
    @NotBlank(message = "Введите сертификат!")
    private String certificate;

    @Column(name="Batch")
    @NotBlank(message = "Введите партию!")
    private String batch;

    @Column(name="Amount")
    @NotBlank(message = "Введите количество!")
    private String amount;

    @Column(name="Status")
    private String status;

    @Column(name="Comment")
    private String comment;

    @Column(name="ExpirationDate")
    @DateTimeFormat(pattern = "dd.MM.yyyy")
    @NotNull(message = "Введите дату срока годности!")
    private Date expirationDate;

    @Column(name="InputControlDate")
    @DateTimeFormat(pattern = "dd.MM.yyyy")
    private Date inputControlDate;

    @ManyToMany(fetch=FetchType.LAZY,
            cascade= {CascadeType.PERSIST, CascadeType.MERGE,
                    CascadeType.DETACH, CascadeType.REFRESH})
    @JoinTable(
            name="basevalvewithcoatings",
            joinColumns=@JoinColumn(name="BaseCoatingId"),
            inverseJoinColumns=@JoinColumn(name="BaseValveId")
    )
    @JsonIgnore
    private List<SheetGateValve> sheetGateValves;

    @Transient
    private Integer tempTCPId;

    public BaseAnticorrosiveCoating() {
    }

    public BaseAnticorrosiveCoating(BaseAnticorrosiveCoating unit, String number) {
        this.name = number;
        this.factory = unit.getFactory();
        this.amount = unit.getAmount();
        this.batch = unit.getBatch();
        this.expirationDate = unit.getExpirationDate();
        this.inputControlDate = unit.getInputControlDate();
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

    public String getFactory() {
        return factory;
    }

    public void setFactory(String factory) {
        this.factory = factory;
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

    public Date getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }

    public Date getInputControlDate() {
        return inputControlDate;
    }

    public void setInputControlDate(Date inputControlDate) {
        this.inputControlDate = inputControlDate;
    }

    public List<SheetGateValve> getSheetGateValves() {
        return sheetGateValves;
    }

    public void setSheetGateValves(List<SheetGateValve> sheetGateValves) {
        this.sheetGateValves = sheetGateValves;
    }

    public Integer getTempTCPId() {
        return tempTCPId;
    }

    public void setTempTCPId(Integer tempTCPId) {
        this.tempTCPId = tempTCPId;
    }

    @Override
    public String toString() {
        return String.format("Партия: %s| АКП: %s - %s", getBatch(), getName(), getStatus());
    }

    public String forReport() {
        return String.format("партия: %s, кол-во: %s", getBatch(), getAmount());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BaseAnticorrosiveCoating coating)) return false;
        return Objects.equals(getName(), coating.getName()) &&
                Objects.equals(getCertificate(), coating.getCertificate()) &&
                Objects.equals(getBatch(), coating.getBatch());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCertificate(), getBatch());
    }
}
