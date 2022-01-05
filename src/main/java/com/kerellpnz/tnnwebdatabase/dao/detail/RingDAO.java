package com.kerellpnz.tnnwebdatabase.dao.detail;

import com.kerellpnz.tnnwebdatabase.dao.BaseDAO;
import com.kerellpnz.tnnwebdatabase.entity.unit.detail.Ring;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Repository
public class RingDAO extends BaseDAO<Ring> {

    public Ring get(int id) {
        Ring entity = sessionFactory.getCurrentSession().get(Ring.class, id);
        Hibernate.initialize(entity.getEntityJournals());
        Hibernate.initialize(entity.getMetalMaterial());
        return entity;
    }

    public Ring getForCopy(int id) {
        Ring entity = sessionFactory.getCurrentSession().get(Ring.class, id);
        Hibernate.initialize(entity.getEntityJournals());
        return entity;
    }

    public List<String> getDistinctDrawing() {
        Session currentSession =sessionFactory.getCurrentSession();
        String HQLRequest = "select distinct o.drawing from Ring o";
        Query<String> theQuery = currentSession.createQuery(HQLRequest, String.class);
        return theQuery.getResultList();
    }
    
    public List<Ring> getUnusedRings() {
        Session currentSession = sessionFactory.getCurrentSession();
        String HQLRequest = "select o from Ring o WHERE o.sheetGateValveCase is null";
        Query<Ring> theQuery = currentSession.createQuery(HQLRequest, Ring.class);
        return theQuery.getResultList();
    }
}
