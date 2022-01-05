package com.kerellpnz.tnnwebdatabase.entity.report;

import com.kerellpnz.tnnwebdatabase.entity.unit.assemblyunit.SheetGateValve;

import java.util.Date;
import java.util.Objects;

public class FOMReport {

    private final String std = "ТУ 3741-007-05749375-2005";
    private final String productType = "запорная арматура с антикоррозионным покрытием";
    private final int amount = 1;
    private String customerFullName;
    private String customerName;
    private String autoNumber;
    private Date shippingDate;
    private String consignee;
    private String productDescription;
    private String specificationNumber;
    private String pidNumber;
    private String designationNumber;
    private String certificateNumber;
    private int weight;
    private String program;
    public record SheetIdentity(String customerName, String program) {}

    public FOMReport(SheetGateValve valve) {
        this.customerFullName = valve.getPid().getSpecification().getCustomer().getName();
        this.customerName = valve.getPid().getSpecification().getCustomer().getShortName();
        this.autoNumber = valve.getAutoNumber();
        this.shippingDate = Objects.requireNonNull(valve.getValveJournals().stream()
                .filter(journal -> Integer.valueOf(114).equals(journal.getPointId())).findAny().orElse(null)).getDate();
        this.consignee = valve.getPid().getConsignee();
        String[] desSplit = valve.getPid().getDesignation().split("-");
        if (desSplit.length != 8) {
            this.productDescription = "Ошибка в обозначении ЗШ!";
            this.certificateNumber = "Ошибка в обозначении ЗШ!";
        }
        else {
            String aacType = valve.getPid().getAacType().substring(0,valve.getPid().getAacType().length() - 1);
            this.productDescription = "Задвижка шиберная DN " + desSplit[1] + " PN " + desSplit[2] + " МПа - ΔР "
                    + desSplit[3] + " МПа - " + desSplit[4] + " - " + desSplit[5] + " -" + desSplit[6] + " - "
                    + desSplit[7] + ", " + aacType + "й установки. С АКП.";
            this.certificateNumber = "задвижка шиберная DN " + desSplit[1] + " PN " + desSplit[2] + "\nзав. № 0"
                    + valve.getNumber();
        }
        this.specificationNumber = valve.getPid().getSpecification().getNumber();
        this.pidNumber = valve.getPid().getNumber();
        this.designationNumber = valve.getDrawing();
        this.weight = valve.getPid().getWeight();
        this.program = valve.getPid().getSpecification().getProgram();
    }

    public String getStd() {
        return std;
    }

    public String getProductType() {
        return productType;
    }

    public int getAmount() {
        return amount;
    }

    public String getCustomerFullName() {
        return customerFullName;
    }

    public void setCustomerFullName(String customerFullName) {
        this.customerFullName = customerFullName;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getAutoNumber() {
        return autoNumber;
    }

    public void setAutoNumber(String autoNumber) {
        this.autoNumber = autoNumber;
    }

    public Date getShippingDate() {
        return shippingDate;
    }

    public void setShippingDate(Date shippingDate) {
        this.shippingDate = shippingDate;
    }

    public String getConsignee() {
        return consignee;
    }

    public void setConsignee(String consignee) {
        this.consignee = consignee;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public String getSpecificationNumber() {
        return specificationNumber;
    }

    public void setSpecificationNumber(String specificationNumber) {
        this.specificationNumber = specificationNumber;
    }

    public String getPidNumber() {
        return pidNumber;
    }

    public void setPidNumber(String pidNumber) {
        this.pidNumber = pidNumber;
    }

    public String getDesignationNumber() {
        return designationNumber;
    }

    public void setDesignationNumber(String designationNumber) {
        this.designationNumber = designationNumber;
    }

    public String getCertificateNumber() {
        return certificateNumber;
    }

    public void setCertificateNumber(String certificateNumber) {
        this.certificateNumber = certificateNumber;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public String getProgram() {
        return program;
    }

    public void setProgram(String program) {
        this.program = program;
    }
}
