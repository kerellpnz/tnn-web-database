package com.kerellpnz.tnnwebdatabase.entity.unit.periodical;

import com.kerellpnz.tnnwebdatabase.entity.BaseEntity;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;
import java.util.Date;

@MappedSuperclass
public class PeriodicalControl extends BaseEntity {

    @Column(name = "Name")
    private String name ;

    @Column(name = "LastControl")
    @DateTimeFormat(pattern = "dd.MM.yyyy")
    private Date lastControl ;

    @Column(name = "NextControl")
    @DateTimeFormat(pattern = "dd.MM.yyyy")
    private Date nextControl ;

    @Column(name = "Status")
    private String status ;

    @Column(name = "Comment")
    private String comment ;

    @Transient
    private Integer tempTCPId;

    public PeriodicalControl() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getLastControl() {
        return lastControl;
    }

    public void setLastControl(Date lastControl) {
        this.lastControl = lastControl;
    }

    public Date getNextControl() {
        return nextControl;
    }

    public void setNextControl(Date nextControl) {
        this.nextControl = nextControl;
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

    public Integer getTempTCPId() {
        return tempTCPId;
    }

    public void setTempTCPId(Integer tempTCPId) {
        this.tempTCPId = tempTCPId;
    }
}
