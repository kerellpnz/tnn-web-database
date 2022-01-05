package com.kerellpnz.tnnwebdatabase.dao.material;

import com.kerellpnz.tnnwebdatabase.dao.BaseDAO;
import com.kerellpnz.tnnwebdatabase.entity.unit.material.WeldingMaterial;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Repository
public class WeldingMaterialDAO extends BaseDAO<WeldingMaterial> {

    public WeldingMaterial get(int id) {
        WeldingMaterial entity = sessionFactory.getCurrentSession().get(WeldingMaterial.class, id);
        Hibernate.initialize(entity.getEntityJournals());
        return entity;
    }

    public List<String> getDistinctName() {
        Session currentSession =sessionFactory.getCurrentSession();
        String HQLRequest = "select distinct o.name from WeldingMaterial o";
        Query<String> theQuery = currentSession.createQuery(HQLRequest, String.class);
        return theQuery.getResultList();
    }
}
