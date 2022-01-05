package com.kerellpnz.tnnwebdatabase.entity.unit.detail;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kerellpnz.tnnwebdatabase.entity.journal.detail.RunningSleeveJournal;
import com.kerellpnz.tnnwebdatabase.entity.unit.BaseUnitWithZk;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Entity
@Table(name = "runningSleeves")
public class RunningSleeve extends BaseUnitWithZk {

    @javax.persistence.Column
    @NotBlank(message = "Введите ЗК!")
    private String zk;

    @OneToOne(mappedBy="runningSleeve",
            cascade={CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST,
                    CascadeType.REFRESH})
    @JsonIgnore
    private Column column;

    @OneToMany(fetch= FetchType.LAZY, mappedBy="detailId", cascade=CascadeType.ALL)
    @JsonIgnore
    private List<RunningSleeveJournal> entityJournals;

    @Transient
    private Integer reqId;

    public RunningSleeve() {
        this.setName("Ходовая втулка");
        this.setCertificate("-");
    }

    public RunningSleeve(RunningSleeve unit, String number) {
        super(unit, number);
        this.setZk(unit.getZk());
    }

    public List<RunningSleeveJournal> getEntityJournals() {
        return entityJournals;
    }

    public void setEntityJournals(List<RunningSleeveJournal> entityJournals) {
        this.entityJournals = entityJournals;
    }

    public Integer getReqId() {
        return reqId;
    }

    public void setReqId(Integer reqId) {
        this.reqId = reqId;
    }

    @Override
    public String getZk() {
        return zk;
    }

    @Override
    public void setZk(String zk) {
        this.zk = zk;
    }

    public Column getColumn() {
        return column;
    }

    public void setColumn(Column column) {
        this.column = column;
    }

    @Override
    public String toString() {
        return String.format("ЗК №%s-%s/чер.%s/DN%s - %s", getZk(), getNumber(), getDrawing(), getDn(), getStatus());
    }

    @Override
    public String forReport() {
        return String.format("DN%s, №%s-%s", getDn(), getZk(), getNumber());
    }
}
