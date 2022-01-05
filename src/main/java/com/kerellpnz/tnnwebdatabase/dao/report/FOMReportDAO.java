package com.kerellpnz.tnnwebdatabase.dao.report;

import com.kerellpnz.tnnwebdatabase.entity.report.FOMReport;
import com.kerellpnz.tnnwebdatabase.entity.unit.assemblyunit.SheetGateValve;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

@Transactional
@Repository
public class FOMReportDAO {

    private final SessionFactory sessionFactory;

    @Autowired
    public FOMReportDAO(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public List<FOMReport> getShippingValves(Date start, Date end) {
        Session currentSession = sessionFactory.getCurrentSession();
        String HQLRequest = "select e from SheetGateValve e join SheetGateValveJournal j on (e = j.detailId) " +
                "where e.status = 'Отгружен' and e.pid is not null and j.pointId = 114 and j.date >= :start and j.date <= :end";
        List<SheetGateValve> valves = currentSession.createQuery(HQLRequest, SheetGateValve.class)
                .setParameter("start", start)
                .setParameter("end", end).getResultList();
        List<FOMReport> report = new ArrayList<>();
        for(SheetGateValve valve: valves) {
            report.add(new FOMReport(valve));
        }
        report.sort(Comparator.comparing(FOMReport::getShippingDate));
        return report;
    }
}
