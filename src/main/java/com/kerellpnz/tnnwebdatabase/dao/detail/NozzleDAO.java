package com.kerellpnz.tnnwebdatabase.dao.detail;

import com.kerellpnz.tnnwebdatabase.dao.BaseDAO;
import com.kerellpnz.tnnwebdatabase.entity.unit.detail.Nozzle;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Repository
public class NozzleDAO extends BaseDAO<Nozzle> {

    public Nozzle get(int id) {
        Nozzle entity = sessionFactory.getCurrentSession().get(Nozzle.class, id);
        Hibernate.initialize(entity.getEntityJournals());
        Hibernate.initialize(entity.getMetalMaterial());
        return entity;
    }

    public Nozzle getForCopy(int id) {
        Nozzle entity = sessionFactory.getCurrentSession().get(Nozzle.class, id);
        Hibernate.initialize(entity.getEntityJournals());
        return entity;
    }
    
    public List<String> getDistinctDrawing() {
        Session currentSession =sessionFactory.getCurrentSession();
        String HQLRequest = "select distinct o.drawing from Nozzle o";
        Query<String> theQuery = currentSession.createQuery(HQLRequest, String.class);
        return theQuery.getResultList();
    }

    public List<String> getDistinctTensileStrength() {
        Session currentSession =sessionFactory.getCurrentSession();
        String HQLRequest = "select distinct o.tensileStrength from Nozzle o";
        Query<String> theQuery = currentSession.createQuery(HQLRequest, String.class);
        return theQuery.getResultList();
    }

    public List<String> getDistinctGrooving() {
        Session currentSession =sessionFactory.getCurrentSession();
        String HQLRequest = "select distinct o.grooving from Nozzle o";
        Query<String> theQuery = currentSession.createQuery(HQLRequest, String.class);
        return theQuery.getResultList();
    }

    public List<Nozzle> getUnusedNozzles() {
        Session currentSession = sessionFactory.getCurrentSession();
        String HQLRequest = "select o from Nozzle o WHERE o.sheetGateValve is null";
        Query<Nozzle> theQuery = currentSession.createQuery(HQLRequest, Nozzle.class);
        return theQuery.getResultList();
    }
}
