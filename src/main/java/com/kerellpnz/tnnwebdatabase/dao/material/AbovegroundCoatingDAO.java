package com.kerellpnz.tnnwebdatabase.dao.material;

import com.kerellpnz.tnnwebdatabase.entity.unit.material.AbovegroundCoating;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class AbovegroundCoatingDAO extends CoatingDAO<AbovegroundCoating>{

    public AbovegroundCoating get(int id) {
        AbovegroundCoating entity = sessionFactory.getCurrentSession().get(AbovegroundCoating.class, id);
        Hibernate.initialize(entity.getEntityJournals());
        Hibernate.initialize(entity.getSheetGateValves());
        return entity;
    }

    public AbovegroundCoating getForCopy(int id) {
        AbovegroundCoating entity = sessionFactory.getCurrentSession().get(AbovegroundCoating.class, id);
        Hibernate.initialize(entity.getEntityJournals());
        return entity;
    }
}
