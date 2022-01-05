package com.kerellpnz.tnnwebdatabase.entity.report;


import com.kerellpnz.tnnwebdatabase.entity.journal.BaseJournal;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class JournalReport {

    private String date;
    private String point;
    private String name;
    private String number;
    private String description;
    private String status;
    private String engineer;
    private String remark;
    private String remarkClosed;
    private String placeOfControl;
    private String documents;
    private String comment;
    private String journalNumber;
    public record RowIdentity(String date, String point, String name, String description, String status,
                       String remark, String remarkClosed, String placeOfControl, String documents,
                       String comment) {}

    public JournalReport(BaseJournal journal, String name, String number, String engineer) {
        DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        this.date = dateFormat.format(journal.getDate());
        this.point = journal.getPoint();
        this.name = name;
        this.number = number;
        this.description = journal.getDescription();
        this.status = journal.getStatus();
        this.engineer = engineer;
        this.remark = !journal.getRemarkIssued().isBlank() ? journal.getRemarkIssued().contains("/") ?
                "Предписание №" + journal.getRemarkIssued() + "\nот " + dateFormat.format(journal.getDateOfRemark()) + "\n" + journal.getRemarkInspector() :
                "Замечание №" + journal.getRemarkIssued() + "\nот " + dateFormat.format(journal.getDateOfRemark()) + "\n" + journal.getRemarkInspector() : "-";
        this.remarkClosed = !journal.getRemarkClosed().isBlank() ? journal.getRemarkIssued().contains("/") ?
                "Предписание №" + journal.getRemarkIssued() + "\nснято от\n" + dateFormat.format(journal.getClosingDate()) :
                "Замечание №" + journal.getRemarkIssued() + "\nснято от\n" + dateFormat.format(journal.getClosingDate()) : "-";
        this.placeOfControl = journal.getPlaceOfControl();
        this.documents = journal.getDocuments();
        this.comment = !journal.getComment().isBlank() ? journal.getComment() : "-";
        this.journalNumber = journal.getJournalNumber();
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPoint() {
        return point;
    }

    public void setPoint(String point) {
        this.point = point;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getEngineer() {
        return engineer;
    }

    public void setEngineer(String engineer) {
        this.engineer = engineer;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getRemarkClosed() {
        return remarkClosed;
    }

    public void setRemarkClosed(String remarkClosed) {
        this.remarkClosed = remarkClosed;
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

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getJournalNumber() {
        return journalNumber;
    }

    public void setJournalNumber(String journalNumber) {
        this.journalNumber = journalNumber;
    }
}


