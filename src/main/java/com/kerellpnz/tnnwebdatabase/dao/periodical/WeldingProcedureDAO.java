package com.kerellpnz.tnnwebdatabase.dao.periodical;

import com.kerellpnz.tnnwebdatabase.dao.BaseDAO;
import com.kerellpnz.tnnwebdatabase.entity.unit.periodical.WeldingProcedure;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Repository
public class WeldingProcedureDAO extends BaseDAO<WeldingProcedure> {

    public WeldingProcedure get(int id) {
        WeldingProcedure wp = sessionFactory.getCurrentSession().get(WeldingProcedure.class, id);
        Hibernate.initialize(wp.getPeriodicalJournals());
        return wp;
    }
}
