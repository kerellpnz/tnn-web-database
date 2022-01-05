package com.kerellpnz.tnnwebdatabase.entity.unit.detail;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kerellpnz.tnnwebdatabase.entity.journal.detail.MainFlangeSealingJournal;
import com.kerellpnz.tnnwebdatabase.entity.unit.assemblyunit.SheetGateValve;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "mainflangeseals")
public class MainFlangeSealing extends BaseSealing {

    @OneToMany(fetch= FetchType.LAZY, mappedBy="detailId", cascade= CascadeType.ALL)
    @JsonIgnore
    private List<MainFlangeSealingJournal> entityJournals;

    @ManyToMany(fetch=FetchType.LAZY,
            cascade= {CascadeType.PERSIST, CascadeType.MERGE,
                    CascadeType.DETACH, CascadeType.REFRESH})
    @JoinTable(
            name="basevalvewithseals",
            joinColumns=@JoinColumn(name="MainFlangeSealingId"),
            inverseJoinColumns=@JoinColumn(name="BaseValveId")
    )
    @JsonIgnore
    private List<SheetGateValve> sheetGateValves;

    public MainFlangeSealing() {
    }

    public MainFlangeSealing(MainFlangeSealing sealing, String number) {
        super(sealing, number);
    }

    public List<MainFlangeSealingJournal> getEntityJournals() {
        return entityJournals;
    }

    public void setEntityJournals(List<MainFlangeSealingJournal> entityJournals) {
        this.entityJournals = entityJournals;
    }

    public List<SheetGateValve> getSheetGateValves() {
        return sheetGateValves;
    }

    public void setSheetGateValves(List<SheetGateValve> sheetGateValves) {
        this.sheetGateValves = sheetGateValves;
    }
}
