package com.kerellpnz.tnnwebdatabase.dao.detail;

import com.kerellpnz.tnnwebdatabase.entity.unit.detail.MainFlangeSealing;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class MainFlangeSealingDAO extends SealingDAO<MainFlangeSealing> {

    public MainFlangeSealing get(int id) {
        MainFlangeSealing entity = sessionFactory.getCurrentSession().get(MainFlangeSealing.class, id);
        Hibernate.initialize(entity.getEntityJournals());
        Hibernate.initialize(entity.getSheetGateValves());
        return entity;
    }

    public MainFlangeSealing getForCopy(int id) {
        MainFlangeSealing entity = sessionFactory.getCurrentSession().get(MainFlangeSealing.class, id);
        Hibernate.initialize(entity.getEntityJournals());
        return entity;
    }
}
