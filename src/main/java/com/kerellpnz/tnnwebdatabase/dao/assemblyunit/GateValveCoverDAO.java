package com.kerellpnz.tnnwebdatabase.dao.assemblyunit;

import com.kerellpnz.tnnwebdatabase.dao.BaseDAO;
import com.kerellpnz.tnnwebdatabase.entity.unit.assemblyunit.SheetGateValveCover;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public class GateValveCoverDAO extends BaseDAO<SheetGateValveCover> {

    public SheetGateValveCover get(int id) {
        SheetGateValveCover entity = sessionFactory.getCurrentSession().get(SheetGateValveCover.class, id);
        Hibernate.initialize(entity.getEntityJournals());
        Hibernate.initialize(entity.getFrontalSaddleSeals());
        Hibernate.initialize(entity.getCaseBottom());
        Hibernate.initialize(entity.getFlange());
        Hibernate.initialize(entity.getCoverSleeve008());
        Hibernate.initialize(entity.getCoverSleeve());
        Hibernate.initialize(entity.getSpindle());
        Hibernate.initialize(entity.getColumn());
        Hibernate.initialize(entity.getMetalMaterial());
        return entity;
    }

    public SheetGateValveCover getForCopy(int id) {
        SheetGateValveCover entity = sessionFactory.getCurrentSession().get(SheetGateValveCover.class, id);
        Hibernate.initialize(entity.getEntityJournals());
        return entity;
    }

    public List<String> getDistinctDrawing() {
        Session currentSession =sessionFactory.getCurrentSession();
        String HQLRequest = "select distinct o.drawing from SheetGateValveCover o";
        Query<String> theQuery = currentSession.createQuery(HQLRequest, String.class);
        return theQuery.getResultList();
    }
}
