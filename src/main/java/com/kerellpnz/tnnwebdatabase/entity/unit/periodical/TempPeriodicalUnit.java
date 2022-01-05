package com.kerellpnz.tnnwebdatabase.entity.unit.periodical;

import com.kerellpnz.tnnwebdatabase.entity.journal.BaseJournal;

import java.util.List;

public class TempPeriodicalUnit<T extends BaseJournal> {

    private Integer tempTCPId;
    private List<T> entityJournals;

    public TempPeriodicalUnit(List<T> entityJournals) {
        this.entityJournals = entityJournals;
    }

    public Integer getTempTCPId() {
        return tempTCPId;
    }

    public void setTempTCPId(Integer tempTCPId) {
        this.tempTCPId = tempTCPId;
    }

    public List<T> getEntityJournals() {
        return entityJournals;
    }

    public void setEntityJournals(List<T> entityJournals) {
        this.entityJournals = entityJournals;
    }
}
