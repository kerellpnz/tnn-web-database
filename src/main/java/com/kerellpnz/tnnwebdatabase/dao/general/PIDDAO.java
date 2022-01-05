package com.kerellpnz.tnnwebdatabase.dao.general;

import com.kerellpnz.tnnwebdatabase.dao.BaseDAO;
import com.kerellpnz.tnnwebdatabase.entity.general.PID;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Repository
public class PIDDAO extends BaseDAO<PID> {

    public PID get(int id) {
        PID entity = sessionFactory.getCurrentSession().get(PID.class, id);
        Hibernate.initialize(entity.getEntityJournals());
        Hibernate.initialize(entity.getSpecification());
        return entity;
    }

    public List<String> getDistinctDesignations() {
        Session currentSession = sessionFactory.getCurrentSession();
        String HQLRequest = "select distinct o.designation from PID o";
        Query<String> theQuery = currentSession.createQuery(HQLRequest, String.class);
        return theQuery.getResultList();
    }

    public List<String> getPIDNumbers() {
        Session currentSession = sessionFactory.getCurrentSession();
        String HQLRequest = "select o.number from PID o";
        Query<String> theQuery = currentSession.createQuery(HQLRequest, String.class);
        return theQuery.getResultList();
    }

    public Integer getIdByNumber(String number) {
        String HQLRequest = "select o.id from PID o WHERE o.number = :number";
        try {
            return sessionFactory.getCurrentSession()
                    .createQuery(HQLRequest, Integer.class)
                    .setParameter("number", number)
                    .getSingleResult();
        }
        catch (Exception e) {
            return null;
        }
    }
}
