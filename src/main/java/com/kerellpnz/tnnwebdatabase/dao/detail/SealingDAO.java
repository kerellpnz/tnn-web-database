package com.kerellpnz.tnnwebdatabase.dao.detail;

import com.kerellpnz.tnnwebdatabase.dao.BaseDAO;
import com.kerellpnz.tnnwebdatabase.entity.unit.detail.BaseSealing;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public class SealingDAO<T extends BaseSealing> extends BaseDAO<T> {

    public List<String> getDistinctMaterial(Class<T> type) {
        Session currentSession =sessionFactory.getCurrentSession();
        String HQLRequest = "select distinct o.material from " + type.getSimpleName() + " o";
        Query<String> theQuery = currentSession.createQuery(HQLRequest, String.class);
        return theQuery.getResultList();
    }

    public List<String> getDistinctDrawing(Class<T> type) {
        Session currentSession =sessionFactory.getCurrentSession();
        String HQLRequest = "select distinct o.drawing from " + type.getSimpleName() + " o";
        Query<String> theQuery = currentSession.createQuery(HQLRequest, String.class);
        return theQuery.getResultList();
    }

    public List<String> getDistinctName(Class<T> type) {
        Session currentSession =sessionFactory.getCurrentSession();
        String HQLRequest = "select distinct o.name from " + type.getSimpleName() + " o";
        Query<String> theQuery = currentSession.createQuery(HQLRequest, String.class);
        return theQuery.getResultList();
    }
}
