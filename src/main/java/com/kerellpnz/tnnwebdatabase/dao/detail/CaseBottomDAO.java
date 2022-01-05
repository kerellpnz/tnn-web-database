package com.kerellpnz.tnnwebdatabase.dao.detail;

import com.kerellpnz.tnnwebdatabase.dao.BaseDAO;
import com.kerellpnz.tnnwebdatabase.entity.unit.detail.CaseBottom;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public class CaseBottomDAO extends BaseDAO<CaseBottom> {

    public CaseBottom get(int id) {
        CaseBottom entity = sessionFactory.getCurrentSession().get(CaseBottom.class, id);
        Hibernate.initialize(entity.getEntityJournals());
        Hibernate.initialize(entity.getMetalMaterial());
        return entity;
    }

    public CaseBottom getForCopy(int id) {
        CaseBottom entity = sessionFactory.getCurrentSession().get(CaseBottom.class, id);
        Hibernate.initialize(entity.getEntityJournals());
        return entity;
    }

    public List<String> getDistinctDrawing() {
        Session currentSession =sessionFactory.getCurrentSession();
        String HQLRequest = "select distinct o.drawing from CaseBottom o";
        Query<String> theQuery = currentSession.createQuery(HQLRequest, String.class);
        return theQuery.getResultList();
    }
}
