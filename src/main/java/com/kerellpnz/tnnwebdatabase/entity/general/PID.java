package com.kerellpnz.tnnwebdatabase.entity.general;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kerellpnz.tnnwebdatabase.entity.BaseEntity;
import com.kerellpnz.tnnwebdatabase.entity.journal.general.PIDJournal;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "pids")
public class PID extends BaseEntity {

    @Column(name = "Number")
    @NotBlank(message = "Введите номер!")
    private String number ;

    @Column(name = "Amount")
    @NotNull(message = "Введите количество ЗШ")
    private Integer amount ;

    @Column(name = "AmountShipped")
    private Integer amountShipped ;

    @Column(name = "Consignee")
    @NotBlank(message = "Введите грузополучателя!")
    private String consignee ;

    @Column(name = "ShippingDate")
    @DateTimeFormat(pattern = "dd.MM.yyyy")
    @NotNull(message = "Введите дату отгрузки!")
    private Date shippingDate ;

    @Column(name = "Designation")
    @NotBlank(message = "Введите обозначение!")
    private String designation ;

    @Column(name = "Weight")
    @NotNull(message = "Введите массу!")
    @Min(value = 0, message = "Масса не может быть отрицательной!")
    private Integer weight ;

    @Column(name = "Description")
    private String description ;

    @Column(name = "AACType")
    @NotBlank(message = "Выберите тип АКП")
    private String aacType;

    @Column(name = "Comment")
    private String comment ;

    @ManyToOne(fetch= FetchType.LAZY, cascade= {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name="SpecificationId")
    @JsonIgnore
    private Specification specification;

    @OneToMany(fetch= FetchType.LAZY, mappedBy="detailId", cascade=CascadeType.ALL)
    @JsonIgnore
    private List<PIDJournal> entityJournals ;

    @Column(name = "ProductType")
    private String productType ;

    @Transient
    private Integer tempTCPId;
    @Transient
    private Integer reqId;
    @Transient
    private String reqName;

    //TODO:
    //private List<BaseAssemblyUnit> BaseAssemblyUnits ;

    public PID() {

    }

    public PID(Specification specification) {
        this.specification = specification;
    }

    public String getAacType() {
        return aacType;
    }

    public void setAacType(String aacType) {
        this.aacType = aacType;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public Integer getAmountShipped() {
        return amountShipped;
    }

    public void setAmountShipped(Integer amountShipped) {
        this.amountShipped = amountShipped;
    }

    public String getConsignee() {
        return consignee;
    }

    public void setConsignee(String consignee) {
        this.consignee = consignee;
    }

    public Date getShippingDate() {
        return shippingDate;
    }

    public void setShippingDate(Date shippingDate) {
        this.shippingDate = shippingDate;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Specification getSpecification() {
        return specification;
    }

    public void setSpecification(Specification specification) {
        this.specification = specification;
    }

    public List<PIDJournal> getEntityJournals() {
        return entityJournals;
    }

    public void setEntityJournals(List<PIDJournal> entityJournals) {
        this.entityJournals = entityJournals;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public Integer getTempTCPId() {
        return tempTCPId;
    }

    public void setTempTCPId(Integer tempTCPId) {
        this.tempTCPId = tempTCPId;
    }

    public Integer getReqId() {
        return reqId;
    }

    public void setReqId(Integer reqId) {
        this.reqId = reqId;
    }

    public String getReqName() {
        return reqName;
    }

    public void setReqName(String reqName) {
        this.reqName = reqName;
    }

    @Override
    public String toString() {
        return "" + getNumber();
    }

    public String forReport() {
        return "код позиции " + getNumber();
    }
}
