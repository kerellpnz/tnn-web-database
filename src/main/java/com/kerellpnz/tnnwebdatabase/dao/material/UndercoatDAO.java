package com.kerellpnz.tnnwebdatabase.dao.material;

import com.kerellpnz.tnnwebdatabase.entity.unit.material.Undercoat;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class UndercoatDAO extends CoatingDAO<Undercoat>{

    public Undercoat get(int id) {
        Undercoat entity = sessionFactory.getCurrentSession().get(Undercoat.class, id);
        Hibernate.initialize(entity.getEntityJournals());
        Hibernate.initialize(entity.getSheetGateValves());
        return entity;
    }

    public Undercoat getForCopy(int id) {
        Undercoat entity = sessionFactory.getCurrentSession().get(Undercoat.class, id);
        Hibernate.initialize(entity.getEntityJournals());
        return entity;
    }
}
