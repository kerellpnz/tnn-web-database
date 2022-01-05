package com.kerellpnz.tnnwebdatabase.dao.detail;

import com.kerellpnz.tnnwebdatabase.dao.BaseDAO;
import com.kerellpnz.tnnwebdatabase.entity.unit.detail.ShearPin;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Repository
public class ShearPinDAO extends BaseDAO<ShearPin> {

    public ShearPin get(int id) {
        ShearPin entity = sessionFactory.getCurrentSession().get(ShearPin.class, id);
        Hibernate.initialize(entity.getEntityJournals());
        Hibernate.initialize(entity.getBaseValveWithShearPins());
        return entity;
    }

    public List<String> getDistinctDrawing() {
        Session currentSession =sessionFactory.getCurrentSession();
        String HQLRequest = "select distinct o.drawing from ShearPin o";
        Query<String> theQuery = currentSession.createQuery(HQLRequest, String.class);
        return theQuery.getResultList();
    }

    public List<String> getDistinctDiameter() {
        Session currentSession =sessionFactory.getCurrentSession();
        String HQLRequest = "select distinct o.diameter from ShearPin o";
        Query<String> theQuery = currentSession.createQuery(HQLRequest, String.class);
        return theQuery.getResultList();
    }

    public List<String> getDistinctTensileStrength() {
        Session currentSession =sessionFactory.getCurrentSession();
        String HQLRequest = "select distinct o.tensileStrength from ShearPin o";
        Query<String> theQuery = currentSession.createQuery(HQLRequest, String.class);
        return theQuery.getResultList();
    }

    public ShearPin getForAdd(int id) {
        ShearPin entity = sessionFactory.getCurrentSession().get(ShearPin.class, id);
        Hibernate.initialize(entity.getBaseValveWithShearPins());
        return entity;
    }

    public ShearPin getForCopy(int id) {
        ShearPin entity = sessionFactory.getCurrentSession().get(ShearPin.class, id);
        Hibernate.initialize(entity.getEntityJournals());
        return entity;
    }
}
