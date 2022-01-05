package com.kerellpnz.tnnwebdatabase.entity.unit.material;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kerellpnz.tnnwebdatabase.entity.journal.material.UndercoatJournal;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import java.util.List;

@Entity
public class Undercoat extends BaseAnticorrosiveCoating {

    @OneToMany(fetch= FetchType.LAZY, mappedBy="detailId", cascade= CascadeType.ALL)
    @JsonIgnore
    private List<UndercoatJournal> entityJournals;

    public Undercoat() {

    }

    public Undercoat(BaseAnticorrosiveCoating unit, String number) {
        super(unit, unit.getName());
        this.setBatch(number);
    }

    public List<UndercoatJournal> getEntityJournals() {
        return entityJournals;
    }

    public void setEntityJournals(List<UndercoatJournal> entityJournals) {
        this.entityJournals = entityJournals;
    }


}
