package com.kerellpnz.tnnwebdatabase.dao.assemblyunit;

import com.kerellpnz.tnnwebdatabase.dao.BaseDAO;
import com.kerellpnz.tnnwebdatabase.entity.unit.assemblyunit.SheetGateValve;
import com.kerellpnz.tnnwebdatabase.entity.unit.detail.Saddle;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public class GateValveDAO extends BaseDAO<SheetGateValve> {

    public SheetGateValve get(int id) {
        SheetGateValve entity = sessionFactory.getCurrentSession().get(SheetGateValve.class, id);
        if (entity.getPid() != null)
            Hibernate.initialize(entity.getPid().getSpecification());
        Hibernate.initialize(entity.getValveCase());
        Hibernate.initialize(entity.getValveCover());
        Hibernate.initialize(entity.getGate());
        Hibernate.initialize(entity.getEntityJournals());
        Hibernate.initialize(entity.getValveJournals());
        Hibernate.initialize(entity.getSaddles());
        Hibernate.initialize(entity.getNozzles());
        Hibernate.initialize(entity.getMainFlangeSeals());
        Hibernate.initialize(entity.getBaseAnticorrosiveCoatings());
        Hibernate.initialize(entity.getBaseValveWithShearPins());
        Hibernate.initialize(entity.getBaseValveWithScrewNuts());
        Hibernate.initialize(entity.getBaseValveWithScrewStuds());
        Hibernate.initialize(entity.getBaseValveWithSprings());
        return entity;
    }

    public SheetGateValve getForCheck(int id) {
        SheetGateValve tempValve = sessionFactory.getCurrentSession().get(SheetGateValve.class, id);
        if (tempValve.getPid() != null)
            Hibernate.initialize(tempValve.getPid().getSpecification());
        return tempValve;
    }

    public SheetGateValve getForBlank(int id) {
        SheetGateValve tempValve = sessionFactory.getCurrentSession().get(SheetGateValve.class, id);
        Hibernate.initialize(tempValve.getPid().getSpecification());
        if (tempValve.getValveCover() != null) {
            Hibernate.initialize(tempValve.getValveCover().getCaseBottom());
            Hibernate.initialize(tempValve.getValveCover().getFlange());
            Hibernate.initialize(tempValve.getValveCover().getCoverSleeve());
            Hibernate.initialize(tempValve.getValveCover().getCoverSleeve008());
            Hibernate.initialize(tempValve.getValveCover().getSpindle());
            Hibernate.initialize(tempValve.getValveCover().getColumn());
            Hibernate.initialize(tempValve.getValveCover().getFrontalSaddleSeals());
        }
        if (tempValve.getValveCase() != null) {
            Hibernate.initialize(tempValve.getValveCase().getCaseBottom());
            Hibernate.initialize(tempValve.getValveCase().getFlange());
            Hibernate.initialize(tempValve.getValveCase().getRings());
        }
        Hibernate.initialize(tempValve.getEntityJournals());
        Hibernate.initialize(tempValve.getValveJournals());
        Hibernate.initialize(tempValve.getGate());
        Hibernate.initialize(tempValve.getNozzles());
        Hibernate.initialize(tempValve.getBaseValveWithShearPins());
        Hibernate.initialize(tempValve.getBaseValveWithScrewStuds());
        Hibernate.initialize(tempValve.getBaseValveWithScrewNuts());
        Hibernate.initialize(tempValve.getBaseAnticorrosiveCoatings());
        Hibernate.initialize(tempValve.getMainFlangeSeals());
        Hibernate.initialize(tempValve.getSaddles());
        if(tempValve.getSaddles() != null) {
            for(Saddle saddle : tempValve.getSaddles()) {
                Hibernate.initialize(saddle.getFrontalSaddleSeals());
            }
        }
        return tempValve;
    }

    public List<String> getDistinctDrawing() {
        Session currentSession =sessionFactory.getCurrentSession();
        String HQLRequest = "select distinct o.drawing from SheetGateValve o";
        Query<String> theQuery = currentSession.createQuery(HQLRequest, String.class);
        return theQuery.getResultList();
    }
}
