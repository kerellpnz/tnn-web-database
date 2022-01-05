package com.kerellpnz.tnnwebdatabase.dao.detail;

import com.kerellpnz.tnnwebdatabase.dao.BaseDAO;
import com.kerellpnz.tnnwebdatabase.entity.unit.detail.Spindle;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public class SpindleDAO extends BaseDAO<Spindle> {

    public Spindle get(int id) {
        Spindle entity = sessionFactory.getCurrentSession().get(Spindle.class, id);
        Hibernate.initialize(entity.getEntityJournals());
        Hibernate.initialize(entity.getMetalMaterial());
        return entity;
    }

    public Spindle getForCopy(int id) {
        Spindle entity = sessionFactory.getCurrentSession().get(Spindle.class, id);
        Hibernate.initialize(entity.getEntityJournals());
        return entity;
    }

    public List<String> getDistinctDrawing() {
        Session currentSession =sessionFactory.getCurrentSession();
        String HQLRequest = "select distinct o.drawing from Spindle o";
        Query<String> theQuery = currentSession.createQuery(HQLRequest, String.class);
        return theQuery.getResultList();
    }
}
