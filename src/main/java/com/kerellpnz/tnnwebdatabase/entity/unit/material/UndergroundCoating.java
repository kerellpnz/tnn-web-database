package com.kerellpnz.tnnwebdatabase.entity.unit.material;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kerellpnz.tnnwebdatabase.entity.journal.material.UndergroundCoatingJournal;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import java.util.List;

@Entity
public class UndergroundCoating extends BaseAnticorrosiveCoating {

    @OneToMany(fetch= FetchType.LAZY, mappedBy="detailId", cascade= CascadeType.ALL)
    @JsonIgnore
    private List<UndergroundCoatingJournal> entityJournals;

    public UndergroundCoating() {

    }

    public UndergroundCoating(BaseAnticorrosiveCoating unit, String number) {
        super(unit, number);
    }

    public List<UndergroundCoatingJournal> getEntityJournals() {
        return entityJournals;
    }

    public void setEntityJournals(List<UndergroundCoatingJournal> entityJournals) {
        this.entityJournals = entityJournals;
    }
}
