package com.kerellpnz.tnnwebdatabase.entity.unit.detail;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kerellpnz.tnnwebdatabase.entity.journal.detail.FrontalSaddleSealingJournal;
import com.kerellpnz.tnnwebdatabase.entity.unit.assemblyunit.SheetGateValveCover;

import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "frontalsaddleseals")
public class FrontalSaddleSealing extends BaseSealing  implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @OneToMany(fetch= FetchType.LAZY, mappedBy="detailId", cascade= CascadeType.ALL)
    @JsonIgnore
    private List<FrontalSaddleSealingJournal> entityJournals;

    @ManyToMany(fetch=FetchType.LAZY,
            cascade= {CascadeType.PERSIST, CascadeType.MERGE,
                    CascadeType.DETACH, CascadeType.REFRESH})
    @JoinTable(
            name="saddlewithseals",
            joinColumns=@JoinColumn(name="FrontalSaddleSealingId"),
            inverseJoinColumns=@JoinColumn(name="SaddleId")
    )
    @JsonIgnore
    private List<Saddle> saddles;

    @ManyToMany(fetch=FetchType.LAZY,
            cascade= {CascadeType.PERSIST, CascadeType.MERGE,
                    CascadeType.DETACH, CascadeType.REFRESH})
    @JoinTable(
            name="coverwithseals",
            joinColumns=@JoinColumn(name="SealingId"),
            inverseJoinColumns=@JoinColumn(name="BaseWeldValveId")
    )
    @JsonIgnore
    private List<SheetGateValveCover> sheetGateValveCovers;

    public FrontalSaddleSealing() {
    }

    public FrontalSaddleSealing(FrontalSaddleSealing sealing, String number) {
        super(sealing, number);
    }

    public List<FrontalSaddleSealingJournal> getEntityJournals() {
        return entityJournals;
    }

    public void setEntityJournals(List<FrontalSaddleSealingJournal> entityJournals) {
        this.entityJournals = entityJournals;
    }

    public List<Saddle> getSaddles() {
        return saddles;
    }

    public void setSaddles(List<Saddle> saddles) {
        this.saddles = saddles;
    }

    public List<SheetGateValveCover> getSheetGateValveCovers() {
        return sheetGateValveCovers;
    }

    public void setSheetGateValveCovers(List<SheetGateValveCover> sheetGateValveCovers) {
        this.sheetGateValveCovers = sheetGateValveCovers;
    }
}
