package com.kerellpnz.tnnwebdatabase.dao.material;

import com.kerellpnz.tnnwebdatabase.dao.BaseDAO;
import com.kerellpnz.tnnwebdatabase.entity.unit.material.MetalMaterial;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Transactional
public class MetalMaterialDAO<T extends MetalMaterial> extends BaseDAO<T> {

    public List<String> getDistinctMaterial(Class<T> type) {
        Session currentSession =sessionFactory.getCurrentSession();
        String HQLRequest = "select distinct o.material from " + type.getSimpleName() + " o";
        Query<String> theQuery = currentSession.createQuery(HQLRequest, String.class);
        return theQuery.getResultList();
    }

    public List<String> getDistinctFirstSize(Class<T> type) {
        Session currentSession =sessionFactory.getCurrentSession();
        String HQLRequest = "select distinct o.firstSize from " + type.getSimpleName() + " o";
        Query<String> theQuery = currentSession.createQuery(HQLRequest, String.class);
        return theQuery.getResultList();
    }

    public List<String> getDistinctSecondSize(Class<T> type) {
        Session currentSession =sessionFactory.getCurrentSession();
        String HQLRequest = "select distinct o.secondSize from " + type.getSimpleName() + " o";
        Query<String> theQuery = currentSession.createQuery(HQLRequest, String.class);
        return theQuery.getResultList();
    }

    public List<String> getDistinctThirdSize(Class<T> type) {
        Session currentSession =sessionFactory.getCurrentSession();
        String HQLRequest = "select distinct o.thirdSize from " + type.getSimpleName() + " o";
        Query<String> theQuery = currentSession.createQuery(HQLRequest, String.class);
        return theQuery.getResultList();
    }
}
