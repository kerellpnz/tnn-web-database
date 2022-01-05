package com.kerellpnz.tnnwebdatabase.entity.unit.detail;

import com.kerellpnz.tnnwebdatabase.entity.BaseEntity;
import com.kerellpnz.tnnwebdatabase.entity.unit.BaseUnit;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@MappedSuperclass
public class BaseSealing extends BaseEntity {

    @Column(name = "Number")
    private String number ;

    @Column(name = "Name")
    @NotBlank(message = "Введите наименование!")
    private String name ;

    @Column(name = "Drawing")
    @NotBlank(message = "Введите чертеж или поставьте \"-\"!")
    private String drawing ;

    @Column(name = "Certificate")
    @NotBlank(message = "Введите сертификат!")
    private String certificate ;

    @Column(name = "Status")
    private String status ;

    @Column(name = "Comment")
    private String comment ;

    @Column(name = "Material")
    @NotBlank(message = "Введите материал!")
    private String material ;

    @Column(name = "Batch")
    @NotBlank(message = "Введите партию!")
    private String batch ;

    @Column(name = "Amount")
    @NotNull(message = "Введите количество!")
    @Min(value=0, message="Значение не может быть отрицательным!")
    private Integer amount ;

    @Column(name = "AmountRemaining")
    private Integer amountRemaining ;

    @Transient
    private Integer tempTCPId;
    
    public BaseSealing() {}

    public BaseSealing(BaseSealing sealing, String number) {
        this.name = sealing.getName();
        this.batch = number;
        this.drawing = sealing.getDrawing();
        this.material = sealing.getMaterial();
        this.certificate = sealing.getCertificate();
        this.status = sealing.getStatus();
        this.comment = sealing.getComment();
        this.amount = 0;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDrawing() {
        return drawing == null ? "" : drawing;
    }

    public void setDrawing(String drawing) {
        this.drawing = drawing;
    }

    public String getCertificate() {
        return certificate;
    }

    public void setCertificate(String certificate) {
        this.certificate = certificate;
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

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public String getBatch() {
        return batch;
    }

    public void setBatch(String batch) {
        this.batch = batch;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public Integer getAmountRemaining() {
        return amountRemaining;
    }

    public void setAmountRemaining(Integer amountRemaining) {
        this.amountRemaining = amountRemaining;
    }

    public Integer getTempTCPId() {
        return tempTCPId;
    }

    public void setTempTCPId(Integer tempTCPId) {
        this.tempTCPId = tempTCPId;
    }

    @Override
    public String toString() {
        return String.format("Сертификат: №%s, партия: %s, %s, чертеж: %s - %s", getCertificate(), getBatch(),
                getName(), getDrawing(), getStatus());
    }

    public String forReport() {
        return String.format("партия: %s, серт.: №%s, кол-во: %d шт.", getBatch(), getCertificate(), getAmount());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BaseSealing that)) return false;
        return Objects.equals(getName(), that.getName()) &&
                Objects.equals(getDrawing(), that.getDrawing()) &&
                Objects.equals(getCertificate(), that.getCertificate()) &&
                Objects.equals(getBatch(), that.getBatch());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getBatch(), getDrawing(), getCertificate());
    }
}
