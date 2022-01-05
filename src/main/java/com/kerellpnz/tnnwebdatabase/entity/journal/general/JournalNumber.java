package com.kerellpnz.tnnwebdatabase.entity.journal.general;

import com.kerellpnz.tnnwebdatabase.entity.BaseEntity;

import javax.persistence.*;

@Entity
@Table(name = "journalnumbers")
public class JournalNumber extends BaseEntity {

    @Column(name="Number")
    private String number;

    @Column(name="isClosed")
    private boolean isClosed;

    public JournalNumber() {
        this.isClosed = true;
    }

    public JournalNumber(String number, boolean isClosed) {
        this.number = number;
        this.isClosed = isClosed;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public boolean isClosed() {
        return isClosed;
    }

    public void setClosed(boolean closed) {
        isClosed = closed;
    }
}
