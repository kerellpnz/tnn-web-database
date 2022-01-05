package com.kerellpnz.tnnwebdatabase.dao.material;

import com.kerellpnz.tnnwebdatabase.entity.unit.material.AbrasiveMaterial;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class AbrasiveMaterialDAO extends CoatingDAO<AbrasiveMaterial>{

    public AbrasiveMaterial get(int id) {
        AbrasiveMaterial entity = sessionFactory.getCurrentSession().get(AbrasiveMaterial.class, id);
        Hibernate.initialize(entity.getEntityJournals());
        Hibernate.initialize(entity.getSheetGateValves());
        return entity;
    }

    public AbrasiveMaterial getForCopy(int id) {
        AbrasiveMaterial entity = sessionFactory.getCurrentSession().get(AbrasiveMaterial.class, id);
        Hibernate.initialize(entity.getEntityJournals());
        return entity;
    }
}
