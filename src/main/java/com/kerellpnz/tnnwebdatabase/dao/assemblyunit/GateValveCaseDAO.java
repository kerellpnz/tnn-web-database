package com.kerellpnz.tnnwebdatabase.dao.assemblyunit;

import com.kerellpnz.tnnwebdatabase.dao.BaseDAO;
import com.kerellpnz.tnnwebdatabase.entity.unit.assemblyunit.SheetGateValveCase;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public class GateValveCaseDAO extends BaseDAO<SheetGateValveCase> {

    public SheetGateValveCase get(int id) {
        SheetGateValveCase entity = sessionFactory.getCurrentSession().get(SheetGateValveCase.class, id);
        Hibernate.initialize(entity.getEntityJournals());
        Hibernate.initialize(entity.getRings());
        Hibernate.initialize(entity.getCaseBottom());
        Hibernate.initialize(entity.getFlange());
        Hibernate.initialize(entity.getCoverSleeve008());
        return entity;
    }

    public SheetGateValveCase getForCopy(int id) {
        SheetGateValveCase entity = sessionFactory.getCurrentSession().get(SheetGateValveCase.class, id);
        Hibernate.initialize(entity.getEntityJournals());
        return entity;
    }

    public List<String> getDistinctDrawing() {
        Session currentSession =sessionFactory.getCurrentSession();
        String HQLRequest = "select distinct o.drawing from SheetGateValveCase o";
        Query<String> theQuery = currentSession.createQuery(HQLRequest, String.class);
        return theQuery.getResultList();
    }
}
