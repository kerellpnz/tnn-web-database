package com.kerellpnz.tnnwebdatabase.dao.periodical;

import com.kerellpnz.tnnwebdatabase.dao.BaseDAO;
import com.kerellpnz.tnnwebdatabase.entity.unit.periodical.HeatTreatment;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Repository
public class HeatTreatmentDAO extends BaseDAO<HeatTreatment> {

    public HeatTreatment get(int id) {
        HeatTreatment wp = sessionFactory.getCurrentSession().get(HeatTreatment.class, id);
        Hibernate.initialize(wp.getPeriodicalJournals());
        return wp;
    }
}
