package com.kerellpnz.tnnwebdatabase.entity.unit.assemblyunit;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kerellpnz.tnnwebdatabase.entity.BaseEntity;
import com.kerellpnz.tnnwebdatabase.entity.general.PID;
import com.kerellpnz.tnnwebdatabase.entity.journal.assemblyunit.CoatingJournal;
import com.kerellpnz.tnnwebdatabase.entity.journal.assemblyunit.SheetGateValveJournal;
import com.kerellpnz.tnnwebdatabase.entity.unit.detail.*;
import com.kerellpnz.tnnwebdatabase.entity.unit.material.BaseAnticorrosiveCoating;
import com.kerellpnz.tnnwebdatabase.validation.ValveDrawing;
import com.kerellpnz.tnnwebdatabase.validation.ValveNumber;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.persistence.Column;
import javax.validation.constraints.NotBlank;
import java.util.*;

@Entity
@Table(name = "baseassemblyunit")
public class SheetGateValve extends BaseEntity {

    @Column(name="Status")
    private String status;

    @Column(name="Comment")
    private String comment;

    @Column(name="Name")
    private String name;

    @Column(name="Number")
    @NotBlank(message = "Введите номер!")
    @ValveNumber
    private String number;

    @Column(name="Drawing")
    @NotBlank(message = "Введите чертеж!")
    @ValveDrawing
    private String drawing;

    @Column(name = "GatePlace")
    private String gatePlace;

    @Column(name = "Moment")
    private String moment;

    @Column(name = "Time")
    private String time;

    @Column(name = "AutomaticReset")
    private String automaticReset;

    @Column(name = "EarTest")
    @DateTimeFormat(pattern = "dd.MM.yyyy")
    private Date earTest;

    @Column(name = "ZIP")
    @DateTimeFormat(pattern = "dd.MM.yyyy")
    private Date zip;

    @Column(name = "AutoNumber")
    private String autoNumber;

    @OneToOne(fetch= FetchType.LAZY, cascade= {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name="GateId")
    @JsonIgnore
    private Gate gate;

    @OneToOne(fetch= FetchType.LAZY, cascade= {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name="WeldGateValveCaseId")
    @JsonIgnore
    private SheetGateValveCase valveCase;

    @OneToOne(fetch= FetchType.LAZY, cascade= {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name="WeldGateValveCoverId")
    @JsonIgnore
    private SheetGateValveCover valveCover;

    @OneToOne(fetch= FetchType.EAGER, cascade= {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name="PIDId")
    private PID pid;

    //TODO Saddles, Nozzles, CounterFlanges!

    @OneToMany(fetch= FetchType.LAZY, mappedBy="sheetGateValve", cascade= CascadeType.ALL)
    @JsonIgnore
    private List<Saddle> saddles;

    @OneToMany(fetch= FetchType.LAZY, mappedBy="sheetGateValve", cascade= CascadeType.ALL)
    @JsonIgnore
    private List<Nozzle> nozzles;

    @ManyToMany(fetch=FetchType.LAZY,
            cascade= {CascadeType.PERSIST, CascadeType.MERGE,
                    CascadeType.DETACH, CascadeType.REFRESH})
    @JoinTable(
            name="basevalvewithcoatings",
            joinColumns=@JoinColumn(name="BaseValveId"),
            inverseJoinColumns=@JoinColumn(name="BaseCoatingId")
    )
    @JsonIgnore
    private List<BaseAnticorrosiveCoating> baseAnticorrosiveCoatings;

    @ManyToMany(fetch=FetchType.LAZY,
            cascade= {CascadeType.PERSIST, CascadeType.MERGE,
                    CascadeType.DETACH, CascadeType.REFRESH})
    @JoinTable(
            name="basevalvewithseals",
            joinColumns=@JoinColumn(name="BaseValveId"),
            inverseJoinColumns=@JoinColumn(name="MainFlangeSealingId")
    )
    @JsonIgnore
    private List<MainFlangeSealing> mainFlangeSeals;

    @OneToMany(fetch= FetchType.LAZY, mappedBy = "baseValve", cascade=CascadeType.ALL)
    @JsonIgnore
    private List<BaseValveWithShearPin> baseValveWithShearPins = new ArrayList<>();

    @OneToMany(fetch= FetchType.LAZY, mappedBy = "baseValve", cascade=CascadeType.ALL)
    @JsonIgnore
    private List<BaseValveWithScrewNut> baseValveWithScrewNuts = new ArrayList<>();

    @OneToMany(fetch= FetchType.LAZY, mappedBy = "baseValve", cascade=CascadeType.ALL)
    @JsonIgnore
    private List<BaseValveWithScrewStud> baseValveWithScrewStuds = new ArrayList<>();

    @OneToMany(fetch= FetchType.LAZY, mappedBy = "baseValve", cascade=CascadeType.ALL)
    @JsonIgnore
    private List<BaseValveWithSpring> baseValveWithSprings = new ArrayList<>();

    @OneToMany(fetch= FetchType.LAZY, mappedBy="detailId", cascade= CascadeType.ALL)
    @JsonIgnore
    private List<SheetGateValveJournal> valveJournals;

    @OneToMany(fetch= FetchType.LAZY, mappedBy="detailId", cascade=CascadeType.ALL)
    @JsonIgnore
    private List<CoatingJournal> entityJournals;

    @Transient
    private List<SheetGateValveJournal> inputControlJournals = new ArrayList<>();
    @Transient
    private List<SheetGateValveJournal> weldJournals = new ArrayList<>();
    @Transient
    private List<SheetGateValveJournal> mechanicalJournals = new ArrayList<>();
    @Transient
    private List<SheetGateValveJournal> documentJournals = new ArrayList<>();
    @Transient
    private List<SheetGateValveJournal> assemblyJournals = new ArrayList<>();
    @Transient
    private String specNumber;
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
    private Integer tempTCPId;
    @Transient
    private Integer tempShearPinId;
    @Transient
    private Integer tempScrewNutId;
    @Transient
    private Integer tempScrewStudId;
    @Transient
    private Integer tempSpringId;
    @Transient
    private Integer tempSaddleId;
    @Transient
    private Integer tempNozzleId;
    @Transient
    private Integer tempSealId;
    @Transient
    private Integer tempCoatingId;

    public SheetGateValve() {
        this.setName("ЗШ");
    }

    public String getGatePlace() {
        return gatePlace == null ? "" : gatePlace;
    }

    public void setGatePlace(String gatePlace) {
        this.gatePlace = gatePlace;
    }

    public String getMoment() {
        return moment == null ? "" : moment;
    }

    public void setMoment(String moment) {
        this.moment = moment;
    }

    public String getTime() {
        return time == null ? "" : time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getAutomaticReset() {
        return automaticReset == null ? "" : automaticReset;
    }

    public void setAutomaticReset(String automaticReset) {
        this.automaticReset = automaticReset;
    }

    public Date getEarTest() {
        return earTest;
    }

    public void setEarTest(Date earTest) {
        this.earTest = earTest;
    }

    public Date getZip() {
        return zip;
    }

    public void setZip(Date zip) {
        this.zip = zip;
    }

    public String getAutoNumber() {
        return autoNumber;
    }

    public void setAutoNumber(String autoNumber) {
        this.autoNumber = autoNumber;
    }

    public Gate getGate() {
        return gate;
    }

    public void setGate(Gate gate) {
        this.gate = gate;
    }

    public SheetGateValveCase getValveCase() {
        return valveCase;
    }

    public void setValveCase(SheetGateValveCase valveCase) {
        this.valveCase = valveCase;
    }

    public SheetGateValveCover getValveCover() {
        return valveCover;
    }

    public void setValveCover(SheetGateValveCover valveCover) {
        this.valveCover = valveCover;
    }

    public PID getPid() {
        return pid;
    }

    public void setPid(PID pid) {
        this.pid = pid;
    }

    public List<SheetGateValveJournal> getValveJournals() {
        return valveJournals;
    }

    public void setValveJournals(List<SheetGateValveJournal> valveJournals) {
        this.valveJournals = valveJournals;
    }

    public List<CoatingJournal> getEntityJournals() {
        return entityJournals;
    }

    public void setEntityJournals(List<CoatingJournal> entityJournals) {
        this.entityJournals = entityJournals;
    }

    public List<SheetGateValveJournal> getInputControlJournals() {
        return inputControlJournals;
    }

    public void setInputControlJournals(List<SheetGateValveJournal> inputControlJournals) {
        this.inputControlJournals = inputControlJournals;
    }

    public List<SheetGateValveJournal> getWeldJournals() {
        return weldJournals;
    }

    public void setWeldJournals(List<SheetGateValveJournal> weldJournals) {
        this.weldJournals = weldJournals;
    }

    public List<SheetGateValveJournal> getMechanicalJournals() {
        return mechanicalJournals;
    }

    public void setMechanicalJournals(List<SheetGateValveJournal> mechanicalJournals) {
        this.mechanicalJournals = mechanicalJournals;
    }

    public List<SheetGateValveJournal> getDocumentJournals() {
        return documentJournals;
    }

    public void setDocumentJournals(List<SheetGateValveJournal> documentJournals) {
        this.documentJournals = documentJournals;
    }

    public List<SheetGateValveJournal> getAssemblyJournals() {
        return assemblyJournals;
    }

    public void setAssemblyJournals(List<SheetGateValveJournal> assemblyJournals) {
        this.assemblyJournals = assemblyJournals;
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

    public String getDrawing() {
        return drawing;
    }

    public void setDrawing(String drawing) {
        this.drawing = drawing;
    }

    public Integer getTempTCPId() {
        return tempTCPId;
    }

    public void setTempTCPId(Integer tempTCPId) {
        this.tempTCPId = tempTCPId;
    }

    public Integer getTempShearPinId() {
        return tempShearPinId;
    }

    public void setTempShearPinId(Integer tempShearPinId) {
        this.tempShearPinId = tempShearPinId;
    }

    public Integer getTempScrewNutId() {
        return tempScrewNutId;
    }

    public void setTempScrewNutId(Integer tempScrewNutId) {
        this.tempScrewNutId = tempScrewNutId;
    }

    public Integer getTempScrewStudId() {
        return tempScrewStudId;
    }

    public void setTempScrewStudId(Integer tempScrewStudId) {
        this.tempScrewStudId = tempScrewStudId;
    }

    public Integer getTempSpringId() {
        return tempSpringId;
    }

    public void setTempSpringId(Integer tempSpringId) {
        this.tempSpringId = tempSpringId;
    }

    public List<Saddle> getSaddles() {
        return saddles;
    }

    public void setSaddles(List<Saddle> saddles) {
        this.saddles = saddles;
    }

    public List<Nozzle> getNozzles() {
        return nozzles;
    }

    public void setNozzles(List<Nozzle> nozzles) {
        this.nozzles = nozzles;
    }

    public Integer getTempSaddleId() {
        return tempSaddleId;
    }

    public void setTempSaddleId(Integer tempSaddleId) {
        this.tempSaddleId = tempSaddleId;
    }

    public Integer getTempNozzleId() {
        return tempNozzleId;
    }

    public void setTempNozzleId(Integer tempNozzleId) {
        this.tempNozzleId = tempNozzleId;
    }

    public List<BaseValveWithShearPin> getBaseValveWithShearPins() {
        return baseValveWithShearPins;
    }

    public void setBaseValveWithShearPins(List<BaseValveWithShearPin> baseValveWithShearPins) {
        this.baseValveWithShearPins = baseValveWithShearPins;
    }

    public List<BaseValveWithScrewNut> getBaseValveWithScrewNuts() {
        return baseValveWithScrewNuts;
    }

    public void setBaseValveWithScrewNuts(List<BaseValveWithScrewNut> baseValveWithScrewNuts) {
        this.baseValveWithScrewNuts = baseValveWithScrewNuts;
    }

    public List<BaseValveWithScrewStud> getBaseValveWithScrewStuds() {
        return baseValveWithScrewStuds;
    }

    public void setBaseValveWithScrewStuds(List<BaseValveWithScrewStud> baseValveWithScrewStuds) {
        this.baseValveWithScrewStuds = baseValveWithScrewStuds;
    }

    public List<BaseValveWithSpring> getBaseValveWithSprings() {
        return baseValveWithSprings;
    }

    public void setBaseValveWithSprings(List<BaseValveWithSpring> baseValveWithSprings) {
        this.baseValveWithSprings = baseValveWithSprings;
    }

    public List<MainFlangeSealing> getMainFlangeSeals() {
        return mainFlangeSeals;
    }

    public void setMainFlangeSeals(List<MainFlangeSealing> mainFlangeSeals) {
        this.mainFlangeSeals = mainFlangeSeals;
    }

    public Integer getTempSealId() {
        return tempSealId;
    }

    public void setTempSealId(Integer tempSealId) {
        this.tempSealId = tempSealId;
    }

    public List<BaseAnticorrosiveCoating> getBaseAnticorrosiveCoatings() {
        return baseAnticorrosiveCoatings;
    }

    public void setBaseAnticorrosiveCoatings(List<BaseAnticorrosiveCoating> baseAnticorrosiveCoatings) {
        this.baseAnticorrosiveCoatings = baseAnticorrosiveCoatings;
    }

    public Integer getTempCoatingId() {
        return tempCoatingId;
    }

    public void setTempCoatingId(Integer tempCoatingId) {
        this.tempCoatingId = tempCoatingId;
    }

    public String getSpecNumber() {
        return specNumber;
    }

    public void setSpecNumber(String specNumber) {
        this.specNumber = specNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SheetGateValve that)) return false;
        return Objects.equals(number, that.getNumber());
    }

    @Override
    public int hashCode() {
        return Objects.hash(number);
    }

    @Override
    public String toString() {
        return "О" + getNumber();
    }
}
