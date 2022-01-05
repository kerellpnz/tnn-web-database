package com.kerellpnz.tnnwebdatabase.entity.unit.material;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kerellpnz.tnnwebdatabase.entity.journal.material.AbovegroundCoatingJournal;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Entity
public class AbovegroundCoating extends BaseAnticorrosiveCoating {

    @OneToMany(fetch= FetchType.LAZY, mappedBy="detailId", cascade= CascadeType.ALL)
    @JsonIgnore
    private List<AbovegroundCoatingJournal> entityJournals;

    @Column(name="Color")
    @NotBlank(message = "Введите цвет!")
    private String color;

    public AbovegroundCoating() {

    }

    public AbovegroundCoating(AbovegroundCoating unit, String number) {
        super(unit, unit.getName());
        this.setColor(unit.getColor());
        this.setBatch(number);
    }

    public List<AbovegroundCoatingJournal> getEntityJournals() {
        return entityJournals;
    }

    public void setEntityJournals(List<AbovegroundCoatingJournal> entityJournals) {
        this.entityJournals = entityJournals;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    @Override
    public String toString() {
        return String.format("Партия: %s| АКП: %s| %s - %s", getBatch(), getName(), getColor(), getStatus());
    }
}
