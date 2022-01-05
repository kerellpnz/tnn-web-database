package com.kerellpnz.tnnwebdatabase.entity.unit.material;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kerellpnz.tnnwebdatabase.entity.journal.material.SheetMaterialJournal;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import java.util.List;

@Entity
public class SheetMaterial extends MetalMaterial {

    @OneToMany(fetch= FetchType.LAZY, mappedBy="detailId", cascade=CascadeType.ALL)
    @JsonIgnore
    private List<SheetMaterialJournal> entityJournals;

    public SheetMaterial() {
        this.setName("Лист");
    }

    public SheetMaterial(MetalMaterial metalMaterial, String number) {
        super(metalMaterial, number);
    }

    public List<SheetMaterialJournal> getEntityJournals() {
        return entityJournals;
    }

    public void setEntityJournals(List<SheetMaterialJournal> entityJournals) {
        this.entityJournals = entityJournals;
    }

    @Override
    public String toString() {
        return String.format("пл.%s (%s), Лист №%s, толщина: %s - %s", getMelt(), getMaterial(), getNumber(), getThirdSize(), getStatus());
    }

    public String forReport() {
        return String.format("сертификат: №%s, пл.%s, №%s, толщина: %s, %s", getCertificate(), getMelt(), getNumber(), getThirdSize(), getMaterial());
    }
}
