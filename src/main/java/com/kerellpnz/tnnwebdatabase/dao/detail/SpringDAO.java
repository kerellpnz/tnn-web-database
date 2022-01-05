package com.kerellpnz.tnnwebdatabase.dao.detail;

import com.kerellpnz.tnnwebdatabase.dao.BaseDAO;
import com.kerellpnz.tnnwebdatabase.entity.unit.detail.Spring;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public class SpringDAO extends BaseDAO<Spring> {

    public Spring get(int id) {
        Spring entity = sessionFactory.getCurrentSession().get(Spring.class, id);
        Hibernate.initialize(entity.getEntityJournals());
        Hibernate.initialize(entity.getBaseValveWithSprings());
        return entity;
    }

    public Spring getForAdd(int id) {
        Spring entity = sessionFactory.getCurrentSession().get(Spring.class, id);
        Hibernate.initialize(entity.getBaseValveWithSprings());
        return entity;
    }

    public Spring getForCopy(int id) {
        Spring entity = sessionFactory.getCurrentSession().get(Spring.class, id);
        Hibernate.initialize(entity.getEntityJournals());
        return entity;
    }

    public List<String> getDistinctDrawing() {
        Session currentSession =sessionFactory.getCurrentSession();
        String HQLRequest = "select distinct o.drawing from Spring o";
        Query<String> theQuery = currentSession.createQuery(HQLRequest, String.class);
        return theQuery.getResultList();
    }
}
