package com.kerellpnz.tnnwebdatabase.entity.unit.periodical;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kerellpnz.tnnwebdatabase.entity.journal.periodical.WeldingProcedureJournal;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "weldingprocedures")
public class WeldingProcedure extends PeriodicalControl {

    @OneToMany(fetch= FetchType.LAZY, mappedBy="detailId", cascade= CascadeType.ALL)
    @JsonIgnore
    private List<WeldingProcedureJournal> periodicalJournals;

    public List<WeldingProcedureJournal> getPeriodicalJournals() {
        return periodicalJournals;
    }

    public void setPeriodicalJournals(List<WeldingProcedureJournal> periodicalJournals) {
        this.periodicalJournals = periodicalJournals;
    }
}
