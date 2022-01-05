package com.kerellpnz.tnnwebdatabase.dao.detail;

import com.kerellpnz.tnnwebdatabase.dao.BaseDAO;
import com.kerellpnz.tnnwebdatabase.entity.unit.detail.CoverSleeve;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public class CoverSleeveDAO extends BaseDAO<CoverSleeve> {

    public CoverSleeve get(int id) {
        CoverSleeve entity = sessionFactory.getCurrentSession().get(CoverSleeve.class, id);
        Hibernate.initialize(entity.getEntityJournals());
        Hibernate.initialize(entity.getMetalMaterial());
        return entity;
    }

    public CoverSleeve getForCopy(int id) {
        CoverSleeve entity = sessionFactory.getCurrentSession().get(CoverSleeve.class, id);
        Hibernate.initialize(entity.getEntityJournals());
        return entity;
    }

    public List<String> getDistinctDrawing() {
        Session currentSession =sessionFactory.getCurrentSession();
        String HQLRequest = "select distinct o.drawing from CoverSleeve o";
        Query<String> theQuery = currentSession.createQuery(HQLRequest, String.class);
        return theQuery.getResultList();
    }
}
