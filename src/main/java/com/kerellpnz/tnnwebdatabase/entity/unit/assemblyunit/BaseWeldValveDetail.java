package com.kerellpnz.tnnwebdatabase.entity.unit.assemblyunit;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.Column;
import com.kerellpnz.tnnwebdatabase.entity.general.PID;
import com.kerellpnz.tnnwebdatabase.entity.unit.BaseUnit;
import com.kerellpnz.tnnwebdatabase.entity.unit.detail.CaseBottom;
import com.kerellpnz.tnnwebdatabase.entity.unit.detail.CoverSleeve008;
import com.kerellpnz.tnnwebdatabase.entity.unit.detail.Flange;

import javax.persistence.*;

@Entity
@Table(name = "baseweldvalvedetail")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="Discriminator")
public class BaseWeldValveDetail extends BaseUnit {

    @Column(name = "PN")
    private String pn;

    @OneToOne(fetch= FetchType.LAZY, cascade= {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name="CaseBottomId")
    @JsonIgnore
    private CaseBottom caseBottom;

    @OneToOne(fetch= FetchType.LAZY, cascade= {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name="CoverFlangeId")
    @JsonIgnore
    private Flange flange;

    @OneToOne(fetch= FetchType.LAZY, cascade= {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name="CoverSleeve008Id")
    @JsonIgnore
    private CoverSleeve008 coverSleeve008;

    @OneToOne(fetch= FetchType.EAGER, cascade= {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name="PIDId")
    private PID pid;

    @Transient
    private Integer inputTCPId;
    @Transient
    private Integer weldTCPId;
    @Transient
    private Integer mechTCPId;
    @Transient
    private Integer docTCPId;
    @Transient
    private Integer assemblyTCPId;
    @Transient
    private Integer reqId;

    public BaseWeldValveDetail() {
    }

    public BaseWeldValveDetail(BaseUnit unit, String number) {
        super(unit, number);
    }

    public String getPn() {
        return pn == null ? "" : pn;
    }

    public void setPn(String pn) {
        this.pn = pn;
    }

    public CaseBottom getCaseBottom() {
        return caseBottom;
    }

    public void setCaseBottom(CaseBottom caseBottom) {
        this.caseBottom = caseBottom;
    }

    public Flange getFlange() {
        return flange;
    }

    public void setFlange(Flange flange) {
        this.flange = flange;
    }

    public PID getPid() {
        return pid;
    }

    public void setPid(PID pid) {
        this.pid = pid;
    }

    public CoverSleeve008 getCoverSleeve008() {
        return coverSleeve008;
    }

    public void setCoverSleeve008(CoverSleeve008 coverSleeve008) {
        this.coverSleeve008 = coverSleeve008;
    }

    public Integer getInputTCPId() {
        return inputTCPId;
    }

    public void setInputTCPId(Integer inputTCPId) {
        this.inputTCPId = inputTCPId;
    }

    public Integer getWeldTCPId() {
        return weldTCPId;
    }

    public void setWeldTCPId(Integer weldTCPId) {
        this.weldTCPId = weldTCPId;
    }

    public Integer getMechTCPId() {
        return mechTCPId;
    }

    public void setMechTCPId(Integer mechTCPId) {
        this.mechTCPId = mechTCPId;
    }

    public Integer getDocTCPId() {
        return docTCPId;
    }

    public void setDocTCPId(Integer docTCPId) {
        this.docTCPId = docTCPId;
    }

    public Integer getAssemblyTCPId() {
        return assemblyTCPId;
    }

    public void setAssemblyTCPId(Integer assemblyTCPId) {
        this.assemblyTCPId = assemblyTCPId;
    }

    public Integer getReqId() {
        return reqId;
    }

    public void setReqId(Integer reqId) {
        this.reqId = reqId;
    }

    @Override
    public String toString() {
        return String.format("№%s/пл.%s/DN%s - %s", getNumber(), getMelt(), getDn(), getStatus());
    }
}
