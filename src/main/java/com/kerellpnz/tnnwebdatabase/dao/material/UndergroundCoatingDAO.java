package com.kerellpnz.tnnwebdatabase.dao.material;

import com.kerellpnz.tnnwebdatabase.entity.unit.material.UndergroundCoating;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class UndergroundCoatingDAO extends CoatingDAO<UndergroundCoating>{

    public UndergroundCoating get(int id) {
        UndergroundCoating entity = sessionFactory.getCurrentSession().get(UndergroundCoating.class, id);
        Hibernate.initialize(entity.getEntityJournals());
        Hibernate.initialize(entity.getSheetGateValves());
        return entity;
    }

    public UndergroundCoating getForCopy(int id) {
        UndergroundCoating entity = sessionFactory.getCurrentSession().get(UndergroundCoating.class, id);
        Hibernate.initialize(entity.getEntityJournals());
        return entity;
    }
}
