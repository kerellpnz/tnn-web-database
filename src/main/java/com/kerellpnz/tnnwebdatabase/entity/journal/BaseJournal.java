package com.kerellpnz.tnnwebdatabase.entity.journal;

import com.kerellpnz.tnnwebdatabase.entity.BaseEntity;
import com.kerellpnz.tnnwebdatabase.entity.technicalcontrolplans.BaseTCP;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;

@MappedSuperclass
public class BaseJournal extends BaseEntity {

    @Column(name="PointId")
    private Integer pointId;

    @Column(name="Point")
    private String point;

    @Column(name="Description")
    private String description;

    @Column(name="JournalNumber")
    private String journalNumber;

    @Column(name="Date")
    @DateTimeFormat(pattern = "dd.MM.yyyy")
    private Date date;

    @Column(name="Status")
    private String status;

    @Column(name="RemarkClosed")
    private String remarkClosed;

    @Column(name="RemarkIssued")
    private String remarkIssued;

    @Column(name="Comment")
    private String comment;

    @Column(name="InspectorId")
    private Integer inspectorId;

    @Transient
    private String inspectorName;

    @Column(name="PlaceOfControl")
    private String placeOfControl;

    @Column(name="Documents")
    private String documents;

    @Column(name="DateOfRemark")
    @DateTimeFormat(pattern = "dd.MM.yyyy")
    private Date dateOfRemark;

    @Column(name="ClosingDate")
    @DateTimeFormat(pattern = "dd.MM.yyyy")
    private Date closingDate;

    @Column(name="RemarkInspector")
    private String remarkInspector;

    public BaseJournal() {

    }

    public BaseJournal(BaseTCP tCP)
    {
        pointId = tCP.getId();
        point = tCP.getPoint();
        description = tCP.getDescription();
        placeOfControl = tCP.getPlaceOfControl();
        documents = tCP.getDocument();
    }

    public BaseJournal(BaseJournal journal, Integer inspectorId, String journalNumber)
    {
        this.pointId = journal.getPointId();
        this.point = journal.getPoint();
        this.description = journal.getDescription();
        this.journalNumber = journalNumber;
        this.date = journal.getDate();
        this.status = journal.getStatus();
        this.remarkClosed = journal.getRemarkClosed();
        this.remarkIssued = journal.getRemarkIssued();
        this.comment = journal.getComment();
        this.inspectorId = inspectorId;
        this.placeOfControl = journal.getPlaceOfControl();
        this.documents = journal.getDocuments();
        this.dateOfRemark = journal.getDateOfRemark();
        this.closingDate = journal.getClosingDate();
        this.remarkInspector = journal.getRemarkInspector();
    }

    public BaseJournal(BaseJournal journal)
    {
        this.pointId = journal.getPointId();
        this.point = journal.getPoint();
        this.description = journal.getDescription();
        this.placeOfControl = journal.getPlaceOfControl();
        this.documents = journal.getDocuments();
    }

    public Integer getPointId() {
        return pointId;
    }

    public void setPointId(Integer pointId) {
        this.pointId = pointId;
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

    public String getJournalNumber() {
        return journalNumber;
    }

    public void setJournalNumber(String journalNumber) {
        this.journalNumber = journalNumber;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getStatus() {
        return status == null ? "" : status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRemarkClosed() {
        return remarkClosed == null ? "" : remarkClosed;
    }

    public void setRemarkClosed(String remarkClosed) {
        this.remarkClosed = remarkClosed;
    }

    public String getRemarkIssued() {
        return remarkIssued == null ? "" : remarkIssued;
    }

    public void setRemarkIssued(String remarkIssued) {
        this.remarkIssued = remarkIssued;
    }

    public String getComment() {
        return comment == null ? "" : comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getPlaceOfControl() {
        return placeOfControl;
    }

    public void setPlaceOfControl(String placeOfControl) {
        this.placeOfControl = placeOfControl;
    }

    public String getDocuments() {
        return documents;
    }

    public void setDocuments(String documents) {
        this.documents = documents;
    }

    public Date getDateOfRemark() {
        return dateOfRemark;
    }

    public void setDateOfRemark(Date dateOfRemark) {
        this.dateOfRemark = dateOfRemark;
    }

    public String getRemarkInspector() {
        return remarkInspector;
    }

    public void setRemarkInspector(String remarkInspector) {
        this.remarkInspector = remarkInspector;
    }

    public Integer getInspectorId() {
        return inspectorId;
    }

    public void setInspectorId(Integer inspectorId) {
        this.inspectorId = inspectorId;
    }

    public String getInspectorName() {
        return inspectorName;
    }

    public void setInspectorName(String inspectorName) {
        this.inspectorName = inspectorName;
    }

    public Date getClosingDate() {
        return closingDate;
    }

    public void setClosingDate(Date closingDate) {
        this.closingDate = closingDate;
    }
}
