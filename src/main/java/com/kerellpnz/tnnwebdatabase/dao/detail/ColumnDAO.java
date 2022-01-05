package com.kerellpnz.tnnwebdatabase.dao.detail;

import com.kerellpnz.tnnwebdatabase.dao.BaseDAO;
import com.kerellpnz.tnnwebdatabase.entity.unit.detail.Column;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public class ColumnDAO extends BaseDAO<Column> {

    public Column get(int id) {
        Column entity = sessionFactory.getCurrentSession().get(Column.class, id);
        Hibernate.initialize(entity.getEntityJournals());
        Hibernate.initialize(entity.getRunningSleeve());
        return entity;
    }

    public Column getForCopy(int id) {
        Column entity = sessionFactory.getCurrentSession().get(Column.class, id);
        Hibernate.initialize(entity.getEntityJournals());
        return entity;
    }

    public List<String> getDistinctDrawing() {
        Session currentSession =sessionFactory.getCurrentSession();
        String HQLRequest = "select distinct o.drawing from Column o";
        Query<String> theQuery = currentSession.createQuery(HQLRequest, String.class);
        return theQuery.getResultList();
    }
}
