package com.kerellpnz.tnnwebdatabase.dao.material;

import com.kerellpnz.tnnwebdatabase.entity.unit.material.RolledMaterial;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class RolledMaterialDAO extends MetalMaterialDAO<RolledMaterial> {

    public RolledMaterial get(int id) {
        RolledMaterial entity = sessionFactory.getCurrentSession().get(RolledMaterial.class, id);
        Hibernate.initialize(entity.getEntityJournals());
        return entity;
    }
}
