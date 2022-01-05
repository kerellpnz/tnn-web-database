package com.kerellpnz.tnnwebdatabase.entity.unit.periodical;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kerellpnz.tnnwebdatabase.entity.journal.periodical.HeatTreatmentJournal;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "ndtcontrols")
public class HeatTreatment extends PeriodicalControl {

    @OneToMany(fetch= FetchType.LAZY, mappedBy="detailId", cascade= CascadeType.ALL)
    @JsonIgnore
    private List<HeatTreatmentJournal> periodicalJournals;

    public List<HeatTreatmentJournal> getPeriodicalJournals() {
        return periodicalJournals;
    }

    public void setPeriodicalJournals(List<HeatTreatmentJournal> periodicalJournals) {
        this.periodicalJournals = periodicalJournals;
    }
}
