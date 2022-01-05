package com.kerellpnz.tnnwebdatabase.dao.detail;

import com.kerellpnz.tnnwebdatabase.dao.BaseDAO;
import com.kerellpnz.tnnwebdatabase.entity.unit.detail.Saddle;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Repository
public class SaddleDAO extends BaseDAO<Saddle> {

    public Saddle get(int id) {
        Saddle entity = sessionFactory.getCurrentSession().get(Saddle.class, id);
        Hibernate.initialize(entity.getEntityJournals());
        Hibernate.initialize(entity.getMetalMaterial());
        Hibernate.initialize(entity.getEntityJournals());
        Hibernate.initialize(entity.getFrontalSaddleSeals());
        return entity;
    }

    public Saddle getForCopy(int id) {
        Saddle entity = sessionFactory.getCurrentSession().get(Saddle.class, id);
        Hibernate.initialize(entity.getEntityJournals());
        return entity;
    }

    public List<String> getDistinctDrawing() {
        Session currentSession =sessionFactory.getCurrentSession();
        String HQLRequest = "select distinct o.drawing from Saddle o";
        Query<String> theQuery = currentSession.createQuery(HQLRequest, String.class);
        return theQuery.getResultList();
    }
    
    public List<Saddle> getUnusedSaddles() {
        Session currentSession = sessionFactory.getCurrentSession();
        String HQLRequest = "select o from Saddle o WHERE o.sheetGateValve is null";
        Query<Saddle> theQuery = currentSession.createQuery(HQLRequest, Saddle.class);
        return theQuery.getResultList();
    }
}
