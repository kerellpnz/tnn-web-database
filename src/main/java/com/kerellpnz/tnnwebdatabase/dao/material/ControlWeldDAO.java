package com.kerellpnz.tnnwebdatabase.dao.material;

import com.kerellpnz.tnnwebdatabase.dao.BaseDAO;
import com.kerellpnz.tnnwebdatabase.entity.unit.material.ControlWeld;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Repository
public class ControlWeldDAO extends BaseDAO<ControlWeld> {

    public ControlWeld get(int id) {
        ControlWeld entity = sessionFactory.getCurrentSession().get(ControlWeld.class, id);
        Hibernate.initialize(entity.getEntityJournals());
        return entity;
    }

    public List<String> getDistinctWeldingMethod() {
        Session currentSession =sessionFactory.getCurrentSession();
        String HQLRequest = "select distinct o.weldingMethod from  ControlWeld o";
        Query<String> theQuery = currentSession.createQuery(HQLRequest, String.class);
        return theQuery.getResultList();
    }

    public List<String> getDistinctWelder() {
        Session currentSession =sessionFactory.getCurrentSession();
        String HQLRequest = "select distinct o.welder from ControlWeld o";
        Query<String> theQuery = currentSession.createQuery(HQLRequest, String.class);
        return theQuery.getResultList();
    }

    public List<String> getDistinctFirstMaterial() {
        Session currentSession =sessionFactory.getCurrentSession();
        String HQLRequest = "select distinct o.firstMaterial from ControlWeld o";
        Query<String> theQuery = currentSession.createQuery(HQLRequest, String.class);
        return theQuery.getResultList();
    }

    public List<String> getDistinctSecondMaterial() {
        Session currentSession =sessionFactory.getCurrentSession();
        String HQLRequest = "select distinct o.secondMaterial from ControlWeld o";
        Query<String> theQuery = currentSession.createQuery(HQLRequest, String.class);
        return theQuery.getResultList();
    }

    public List<String> getDistinctStamp() {
        Session currentSession =sessionFactory.getCurrentSession();
        String HQLRequest = "select distinct o.stamp from ControlWeld o";
        Query<String> theQuery = currentSession.createQuery(HQLRequest, String.class);
        return theQuery.getResultList();
    }
}
