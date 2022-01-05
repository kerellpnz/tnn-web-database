package com.kerellpnz.tnnwebdatabase.dao.detail;

import com.kerellpnz.tnnwebdatabase.dao.BaseDAO;
import com.kerellpnz.tnnwebdatabase.entity.unit.detail.ScrewNut;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public class ScrewNutDAO extends BaseDAO<ScrewNut> {

    public ScrewNut get(int id) {
        ScrewNut entity = sessionFactory.getCurrentSession().get(ScrewNut.class, id);
        Hibernate.initialize(entity.getEntityJournals());
        Hibernate.initialize(entity.getBaseValveWithScrewNuts());
        return entity;
    }

    public ScrewNut getForAdd(int id) {
        ScrewNut entity = sessionFactory.getCurrentSession().get(ScrewNut.class, id);
        Hibernate.initialize(entity.getBaseValveWithScrewNuts());
        return entity;
    }

    public ScrewNut getForCopy(int id) {
        ScrewNut entity = sessionFactory.getCurrentSession().get(ScrewNut.class, id);
        Hibernate.initialize(entity.getEntityJournals());
        return entity;
    }

    public List<String> getDistinctDrawing() {
        Session currentSession =sessionFactory.getCurrentSession();
        String HQLRequest = "select distinct o.drawing from ScrewNut o";
        Query<String> theQuery = currentSession.createQuery(HQLRequest, String.class);
        return theQuery.getResultList();
    }
}
