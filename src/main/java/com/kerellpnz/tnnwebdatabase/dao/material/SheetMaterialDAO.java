package com.kerellpnz.tnnwebdatabase.dao.material;

import com.kerellpnz.tnnwebdatabase.entity.unit.material.SheetMaterial;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class SheetMaterialDAO extends MetalMaterialDAO<SheetMaterial> {

    public SheetMaterial get(int id) {
        SheetMaterial entity = sessionFactory.getCurrentSession().get(SheetMaterial.class, id);
        Hibernate.initialize(entity.getEntityJournals());
        return entity;
    }
}
