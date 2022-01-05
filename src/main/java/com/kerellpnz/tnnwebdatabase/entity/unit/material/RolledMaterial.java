package com.kerellpnz.tnnwebdatabase.entity.unit.material;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kerellpnz.tnnwebdatabase.entity.journal.material.RolledMaterialJournal;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import java.util.List;

@Entity
public class RolledMaterial extends MetalMaterial {

    @OneToMany(fetch= FetchType.LAZY, mappedBy="detailId", cascade= CascadeType.ALL)
    @JsonIgnore
    private List<RolledMaterialJournal> entityJournals;

    public RolledMaterial() {
        this.setName("Прокат");
    }

    public RolledMaterial(MetalMaterial metalMaterial, String number) {
        super(metalMaterial, metalMaterial.getNumber());
        this.setCertificate(number);
    }

    public List<RolledMaterialJournal> getEntityJournals() {
        return entityJournals;
    }

    public void setEntityJournals(List<RolledMaterialJournal> entityJournals) {
        this.entityJournals = entityJournals;
    }

    @Override
    public String toString() {
        return String.format("пл.%s (%s), Ф%s - %s", getMelt(), getMaterial(), getThirdSize(), getStatus());
    }

    public String forReport() {
        return String.format("сертификат: №%s, пл.%s, Ф%s, %s", getCertificate(), getMelt(), getThirdSize(), getMaterial());
    }
}
