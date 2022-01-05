package com.kerellpnz.tnnwebdatabase.dao.material;

import com.kerellpnz.tnnwebdatabase.dao.BaseDAO;
import com.kerellpnz.tnnwebdatabase.entity.unit.material.BaseAnticorrosiveCoating;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public class CoatingDAO<T extends BaseAnticorrosiveCoating> extends BaseDAO<T> {

    public List<String> getDistinctName(Class<T> type) {
        Session currentSession =sessionFactory.getCurrentSession();
        String HQLRequest = "select distinct o.name from " + type.getSimpleName() + " o";
        Query<String> theQuery = currentSession.createQuery(HQLRequest, String.class);
        return theQuery.getResultList();
    }

    public List<String> getDistinctFactory(Class<T> type) {
        Session currentSession =sessionFactory.getCurrentSession();
        String HQLRequest = "select distinct o.factory from " + type.getSimpleName() + " o";
        Query<String> theQuery = currentSession.createQuery(HQLRequest, String.class);
        return theQuery.getResultList();
    }

    public List<String> getDistinctColor(Class<T> type) {
        Session currentSession =sessionFactory.getCurrentSession();
        String HQLRequest = "select distinct o.color from " + type.getSimpleName() + " o";
        Query<String> theQuery = currentSession.createQuery(HQLRequest, String.class);
        return theQuery.getResultList();
    }
}
