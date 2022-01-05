package com.kerellpnz.tnnwebdatabase.dao.detail;

import com.kerellpnz.tnnwebdatabase.dao.BaseDAO;
import com.kerellpnz.tnnwebdatabase.entity.unit.detail.CoverSleeve008;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public class CoverSleeve008DAO extends BaseDAO<CoverSleeve008> {

    public CoverSleeve008 get(int id) {
        CoverSleeve008 entity = sessionFactory.getCurrentSession().get(CoverSleeve008.class, id);
        Hibernate.initialize(entity.getEntityJournals());
        Hibernate.initialize(entity.getMetalMaterial());
        return entity;
    }

    public CoverSleeve008 getForCopy(int id) {
        CoverSleeve008 entity = sessionFactory.getCurrentSession().get(CoverSleeve008.class, id);
        Hibernate.initialize(entity.getEntityJournals());
        return entity;
    }

    public List<String> getDistinctDrawing() {
        Session currentSession =sessionFactory.getCurrentSession();
        String HQLRequest = "select distinct o.drawing from CoverSleeve008 o";
        Query<String> theQuery = currentSession.createQuery(HQLRequest, String.class);
        return theQuery.getResultList();
    }
}
