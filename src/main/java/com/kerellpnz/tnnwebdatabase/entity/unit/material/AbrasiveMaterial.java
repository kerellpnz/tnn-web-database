package com.kerellpnz.tnnwebdatabase.entity.unit.material;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kerellpnz.tnnwebdatabase.entity.journal.material.AbrasiveMaterialJournal;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import java.util.List;

@Entity
public class AbrasiveMaterial extends BaseAnticorrosiveCoating {

    @OneToMany(fetch= FetchType.LAZY, mappedBy="detailId", cascade= CascadeType.ALL)
    @JsonIgnore
    private List<AbrasiveMaterialJournal> entityJournals;

    public AbrasiveMaterial() {

    }

    public AbrasiveMaterial(BaseAnticorrosiveCoating unit, String number) {
        super(unit, unit.getName());
        this.setBatch(number);
    }

    public List<AbrasiveMaterialJournal> getEntityJournals() {
        return entityJournals;
    }

    public void setEntityJournals(List<AbrasiveMaterialJournal> entityJournals) {
        this.entityJournals = entityJournals;
    }
}
