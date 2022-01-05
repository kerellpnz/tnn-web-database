package com.kerellpnz.tnnwebdatabase.entity.technicalcontrolplans;


import com.kerellpnz.tnnwebdatabase.entity.BaseEntity;

import javax.persistence.*;

@Entity
@Table(name = "basetcp")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="Discriminator")
public class BaseTCP extends BaseEntity {

    @Column(name="Point")
    private String point;

    @Column(name="Description")
    private String description;

    @Column(name="ShortDescription")
    private String shortDescription;

    @Column(name="PlaceOfControl")
    private String placeOfControl;

    @Column(name="Documents")
    private String document;

    @Column(name="OperationType")
    private String operationType;

    @Column(name="ProductType")
    private String productType;

    public BaseTCP() {

    }

    public BaseTCP(String point, String description, String placeOfControl, String document, String operationType, String productType) {
        this.point = point;
        this.description = description;
        this.placeOfControl = placeOfControl;
        this.document = document;
        this.operationType = operationType;
        this.productType = productType;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getPoint() {
        return point;
    }

    public void setPoint(String point) {
        this.point = point;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPlaceOfControl() {
        return placeOfControl;
    }

    public void setPlaceOfControl(String placeOfControl) {
        this.placeOfControl = placeOfControl;
    }

    public String getDocument() {
        return document;
    }

    public void setDocument(String documents) {
        this.document = documents;
    }

    public String getOperationType() {
        return operationType;
    }

    public void setOperationType(String operationType) {
        this.operationType = operationType;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }
}
