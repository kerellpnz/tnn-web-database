package com.kerellpnz.tnnwebdatabase.dao.detail;

import com.kerellpnz.tnnwebdatabase.entity.unit.detail.FrontalSaddleSealing;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class FrontalSaddleSealingDAO extends SealingDAO<FrontalSaddleSealing> {

    public FrontalSaddleSealing get(int id) {
        FrontalSaddleSealing entity = sessionFactory.getCurrentSession().get(FrontalSaddleSealing.class, id);
        Hibernate.initialize(entity.getEntityJournals());
        Hibernate.initialize(entity.getSheetGateValveCovers());
        Hibernate.initialize(entity.getSaddles());
        return entity;
    }

    public FrontalSaddleSealing getForCopy(int id) {
        FrontalSaddleSealing entity = sessionFactory.getCurrentSession().get(FrontalSaddleSealing.class, id);
        Hibernate.initialize(entity.getEntityJournals());
        return entity;
    }

    public FrontalSaddleSealing getForCover(int id) {
        FrontalSaddleSealing entity = sessionFactory.getCurrentSession().get(FrontalSaddleSealing.class, id);
        Hibernate.initialize(entity.getSheetGateValveCovers());
        return entity;
    }

    public FrontalSaddleSealing getForSaddle(int id) {
        FrontalSaddleSealing entity = sessionFactory.getCurrentSession().get(FrontalSaddleSealing.class, id);
        Hibernate.initialize(entity.getSaddles());
        return entity;
    }
}
