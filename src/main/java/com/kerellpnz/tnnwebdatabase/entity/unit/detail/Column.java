package com.kerellpnz.tnnwebdatabase.entity.unit.detail;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kerellpnz.tnnwebdatabase.entity.journal.detail.ColumnJournal;
import com.kerellpnz.tnnwebdatabase.entity.unit.BaseUnitWithZk;
import com.kerellpnz.tnnwebdatabase.entity.unit.assemblyunit.SheetGateValveCover;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "columns")
public class Column extends BaseUnitWithZk {

    @OneToOne(fetch= FetchType.LAZY, cascade= {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name="RunningSleeveId")
    @JsonIgnore
    private RunningSleeve runningSleeve;

    @OneToMany(fetch= FetchType.LAZY, mappedBy="detailId", cascade=CascadeType.ALL)
    @JsonIgnore
    private List<ColumnJournal> entityJournals;

    @OneToOne(mappedBy="column",
            cascade={CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST,
                    CascadeType.REFRESH})
    @JsonIgnore
    private SheetGateValveCover sheetGateValveCover;

    @Transient
    private Integer reqId;

    public Column() {
        this.setName("Стойка");
        this.setCertificate("-");
    }

    public Column(Column unit, String number) {
        super(unit, number);
    }

    public RunningSleeve getRunningSleeve() {
        return runningSleeve;
    }

    public void setRunningSleeve(RunningSleeve runningSleeve) {
        this.runningSleeve = runningSleeve;
    }

    public List<ColumnJournal> getEntityJournals() {
        return entityJournals;
    }

    public void setEntityJournals(List<ColumnJournal> entityJournals) {
        this.entityJournals = entityJournals;
    }

    public SheetGateValveCover getSheetGateValveCover() {
        return sheetGateValveCover;
    }

    public void setSheetGateValveCover(SheetGateValveCover sheetGateValveCover) {
        this.sheetGateValveCover = sheetGateValveCover;
    }

    public Integer getReqId() {
        return reqId;
    }

    public void setReqId(Integer reqId) {
        this.reqId = reqId;
    }

    @Override
    public String toString() {
        return String.format("№%s/DN%s - %s", getNumber(), getDn(), getStatus());
    }

    @Override
    public String forReport() {
        return String.format("DN%s, №%s", getDn(), getNumber());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Column that)) return false;
        return getNumber().equalsIgnoreCase(that.getNumber()) &&
                Objects.equals(getDn(), that.getDn());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getNumber(), getDn());
    }
}
