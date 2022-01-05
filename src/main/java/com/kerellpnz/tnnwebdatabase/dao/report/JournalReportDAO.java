package com.kerellpnz.tnnwebdatabase.dao.report;

import com.kerellpnz.tnnwebdatabase.dao.general.InspectorDAO;
import com.kerellpnz.tnnwebdatabase.entity.general.Inspector;
import com.kerellpnz.tnnwebdatabase.entity.report.JournalReport;
import com.kerellpnz.tnnwebdatabase.entity.journal.assemblyunit.CoatingJournal;
import com.kerellpnz.tnnwebdatabase.entity.journal.assemblyunit.SheetGateValveCaseJournal;
import com.kerellpnz.tnnwebdatabase.entity.journal.assemblyunit.SheetGateValveCoverJournal;
import com.kerellpnz.tnnwebdatabase.entity.journal.assemblyunit.SheetGateValveJournal;
import com.kerellpnz.tnnwebdatabase.entity.journal.detail.*;
import com.kerellpnz.tnnwebdatabase.entity.journal.general.PIDJournal;
import com.kerellpnz.tnnwebdatabase.entity.journal.material.*;
import com.kerellpnz.tnnwebdatabase.entity.journal.periodical.FactoryInspectionJournal;
import com.kerellpnz.tnnwebdatabase.entity.journal.periodical.StoreControlJournal;
import org.hibernate.Hibernate;
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
public class JournalReportDAO {

    private final SessionFactory sessionFactory;
    private final InspectorDAO inspectorDAO;
    private String engineer;
    private Date date;
    private int inspId;

    @Autowired
    public JournalReportDAO(SessionFactory sessionFactory, InspectorDAO inspectorDAO) {
        this.sessionFactory = sessionFactory;
        this.inspectorDAO = inspectorDAO;
    }

    public List<JournalReport> getJournalReportByInspectorAndDate(Date date, int inspId) {
        engineer = inspectorDAO.get(Inspector.class, inspId).getName();
        this.date = date;
        this.inspId = inspId;
        List<JournalReport> report = new ArrayList<>();
        getCoatingJournals(report);
        getSheetGateValveJournals(report);
        getCaseBottomJournals(report);
        getFlangeJournals(report);
        getCoverSleeveJournals(report);
        getCoverSleeve008Journals(report);
        getRingJournals(report);
        getSheetGateValveCaseJournals(report);
        getSheetGateValveCoverJournals(report);
        getMainFlangeSealingJournals(report);
        getFrontalSaddleSealingJournals(report);
        getGateJournals(report);
        getNozzleJournals(report);
        getRunningSleeveJournals(report);
        getSaddleJournals(report);
        getScrewNutJournals(report);
        getScrewStudJournals(report);
        getShearPinJournals(report);
        getSpindleJournals(report);
        getColumnJournals(report);
        getSpringJournals(report);
        getAbovegroundCoatingJournals(report);
        getAbrasiveMaterialJournals(report);
        getUndercoatJournals(report);
        getUndergroundCoatingJournals(report);
        getControlWeldJournals(report);
        getSheetMaterialJournals(report);
        getRolledMaterialJournals(report);
        getStoreControlJournals(report);
        getWeldingMaterialJournals(report);
        getFactoryInspectionJournals(report);
        getPIDJournals(report);
        report.sort(Comparator.comparing(JournalReport::getPoint));
        return report;
    }

    private void getCoatingJournals(List<JournalReport> report) {
        Session currentSession = sessionFactory.getCurrentSession();
        String HQLRequest = "select o from CoatingJournal o WHERE o.detailId is not null and o.date = :date and o.inspectorId = :inspId";
        List<CoatingJournal> journals = currentSession.createQuery(HQLRequest, CoatingJournal.class)
                .setParameter("date", date)
                .setParameter("inspId", inspId).getResultList();
        if (journals != null) {
            for(CoatingJournal journal: journals) {
                report.add(new JournalReport(journal, journal.getDetailId().getName(), journal.getDetailId().toString(), engineer));
            }
        }
    }

    private void getSheetGateValveJournals(List<JournalReport> report) {
        Session currentSession = sessionFactory.getCurrentSession();
        String HQLRequest = "select o from SheetGateValveJournal o WHERE o.detailId is not null and o.date = :date and o.inspectorId = :inspId";
        List<SheetGateValveJournal> journals = currentSession.createQuery(HQLRequest, SheetGateValveJournal.class)
                .setParameter("date", date)
                .setParameter("inspId", inspId).getResultList();
        if (journals != null) {
            for(SheetGateValveJournal journal: journals) {
                report.add(new JournalReport(journal, journal.getDetailId().getName(), journal.getDetailId().toString(), engineer));
            }
        }
    }

    private void getCaseBottomJournals(List<JournalReport> report) {
        Session currentSession = sessionFactory.getCurrentSession();
        String HQLRequest = "select o from CaseBottomJournal o WHERE o.detailId is not null and o.date = :date and o.inspectorId = :inspId";
        List<CaseBottomJournal> journals = currentSession.createQuery(HQLRequest, CaseBottomJournal.class)
                .setParameter("date", date)
                .setParameter("inspId", inspId).getResultList();
        if(journals != null) {
            for(CaseBottomJournal journal: journals) {
                report.add(new JournalReport(journal, journal.getDetailId().getName(), journal.getDetailId().forReport(), engineer));
            }
        }
    }

    private void getFlangeJournals(List<JournalReport> report) {
        Session currentSession = sessionFactory.getCurrentSession();
        String HQLRequest = "select o from FlangeJournal o WHERE o.detailId is not null and o.date = :date and o.inspectorId = :inspId";
        List<FlangeJournal> journals = currentSession.createQuery(HQLRequest, FlangeJournal.class)
                .setParameter("date", date)
                .setParameter("inspId", inspId).getResultList();
        if(journals != null) {
            for(FlangeJournal journal: journals) {
                report.add(new JournalReport(journal, journal.getDetailId().getName(), journal.getDetailId().forReport(), engineer));
            }
        }
    }

    private void getCoverSleeveJournals(List<JournalReport> report) {
        Session currentSession = sessionFactory.getCurrentSession();
        String HQLRequest = "select o from CoverSleeveJournal o WHERE o.detailId is not null and o.date = :date and o.inspectorId = :inspId";
        List<CoverSleeveJournal> journals = currentSession.createQuery(HQLRequest, CoverSleeveJournal.class)
                .setParameter("date", date)
                .setParameter("inspId", inspId).getResultList();
        if(journals != null) {
            for(CoverSleeveJournal journal: journals) {
                report.add(new JournalReport(journal, journal.getDetailId().getName(), journal.getDetailId().forReport(), engineer));
            }
        }
    }

    private void getCoverSleeve008Journals(List<JournalReport> report) {
        Session currentSession = sessionFactory.getCurrentSession();
        String HQLRequest = "select o from CoverSleeve008Journal o WHERE o.detailId is not null and o.date = :date and o.inspectorId = :inspId";
        List<CoverSleeve008Journal> journals = currentSession.createQuery(HQLRequest, CoverSleeve008Journal.class)
                .setParameter("date", date)
                .setParameter("inspId", inspId).getResultList();
        if(journals != null) {
            for(CoverSleeve008Journal journal: journals) {
                report.add(new JournalReport(journal, journal.getDetailId().getName(), journal.getDetailId().forReport(), engineer));
            }
        }
    }

    private void getRingJournals(List<JournalReport> report) {
        Session currentSession = sessionFactory.getCurrentSession();
        String HQLRequest = "select o from RingJournal o WHERE o.detailId is not null and o.date = :date and o.inspectorId = :inspId";
        List<RingJournal> journals = currentSession.createQuery(HQLRequest, RingJournal.class)
                .setParameter("date", date)
                .setParameter("inspId", inspId).getResultList();
        if(journals != null) {
            for(RingJournal journal: journals) {
                report.add(new JournalReport(journal, journal.getDetailId().getName(), journal.getDetailId().forReport(), engineer));
            }
        }
    }

    private void getSheetGateValveCaseJournals(List<JournalReport> report) {
        Session currentSession = sessionFactory.getCurrentSession();
        String HQLRequest = "select o from SheetGateValveCaseJournal o WHERE o.detailId is not null and o.date = :date and o.inspectorId = :inspId";
        List<SheetGateValveCaseJournal> journals = currentSession.createQuery(HQLRequest, SheetGateValveCaseJournal.class)
                .setParameter("date", date)
                .setParameter("inspId", inspId).getResultList();
        if(journals != null) {
            for(SheetGateValveCaseJournal journal: journals) {
                report.add(new JournalReport(journal, journal.getDetailId().getName(), journal.getDetailId().forReport(), engineer));
            }
        }
    }

    private void getSheetGateValveCoverJournals(List<JournalReport> report) {
        Session currentSession = sessionFactory.getCurrentSession();
        String HQLRequest = "select o from SheetGateValveCoverJournal o WHERE o.detailId is not null and o.date = :date and o.inspectorId = :inspId";
        List<SheetGateValveCoverJournal> journals = currentSession.createQuery(HQLRequest, SheetGateValveCoverJournal.class)
                .setParameter("date", date)
                .setParameter("inspId", inspId).getResultList();
        if(journals != null) {
            for(SheetGateValveCoverJournal journal: journals) {
                report.add(new JournalReport(journal, journal.getDetailId().getName(), journal.getDetailId().forReport(), engineer));
            }
        }
    }

    private void getMainFlangeSealingJournals(List<JournalReport> report) {
        Session currentSession = sessionFactory.getCurrentSession();
        String HQLRequest = "select o from MainFlangeSealingJournal o WHERE o.detailId is not null and o.date = :date and o.inspectorId = :inspId";
        List<MainFlangeSealingJournal> journals = currentSession.createQuery(HQLRequest, MainFlangeSealingJournal.class)
                .setParameter("date", date)
                .setParameter("inspId", inspId).getResultList();
        if(journals != null) {
            for(MainFlangeSealingJournal journal: journals) {
                report.add(new JournalReport(journal, journal.getDetailId().getName(), journal.getDetailId().forReport(), engineer));
            }
        }
    }

    private void getFrontalSaddleSealingJournals(List<JournalReport> report) {
        Session currentSession = sessionFactory.getCurrentSession();
        String HQLRequest = "select o from FrontalSaddleSealingJournal o WHERE o.detailId is not null and o.date = :date and o.inspectorId = :inspId";
        List<FrontalSaddleSealingJournal> journals = currentSession.createQuery(HQLRequest, FrontalSaddleSealingJournal.class)
                .setParameter("date", date)
                .setParameter("inspId", inspId).getResultList();
        if(journals != null) {
            for(FrontalSaddleSealingJournal journal: journals) {
                report.add(new JournalReport(journal, journal.getDetailId().getName(), journal.getDetailId().forReport(), engineer));
            }
        }
    }

    private void getGateJournals(List<JournalReport> report) {
        Session currentSession = sessionFactory.getCurrentSession();
        String HQLRequest = "select o from GateJournal o WHERE o.detailId is not null and o.date = :date and o.inspectorId = :inspId";
        List<GateJournal> journals = currentSession.createQuery(HQLRequest, GateJournal.class)
                .setParameter("date", date)
                .setParameter("inspId", inspId).getResultList();
        if(journals != null) {
            for(GateJournal journal: journals) {
                report.add(new JournalReport(journal, journal.getDetailId().getName(), journal.getDetailId().forReport(), engineer));
            }
        }
    }

    private void getNozzleJournals(List<JournalReport> report) {
        Session currentSession = sessionFactory.getCurrentSession();
        String HQLRequest = "select o from NozzleJournal o WHERE o.detailId is not null and o.date = :date and o.inspectorId = :inspId";
        List<NozzleJournal> journals = currentSession.createQuery(HQLRequest, NozzleJournal.class)
                .setParameter("date", date)
                .setParameter("inspId", inspId).getResultList();
        if(journals != null) {
            for(NozzleJournal journal: journals) {
                report.add(new JournalReport(journal, journal.getDetailId().getName(), journal.getDetailId().forReport(), engineer));
            }
        }
    }

    private void getRunningSleeveJournals(List<JournalReport> report) {
        Session currentSession = sessionFactory.getCurrentSession();
        String HQLRequest = "select o from RunningSleeveJournal o WHERE o.detailId is not null and o.date = :date and o.inspectorId = :inspId";
        List<RunningSleeveJournal> journals = currentSession.createQuery(HQLRequest, RunningSleeveJournal.class)
                .setParameter("date", date)
                .setParameter("inspId", inspId).getResultList();
        if(journals != null) {
            for(RunningSleeveJournal journal: journals) {
                report.add(new JournalReport(journal, journal.getDetailId().getName(), journal.getDetailId().forReport(), engineer));
            }
        }
    }

    private void getSaddleJournals(List<JournalReport> report) {
        Session currentSession = sessionFactory.getCurrentSession();
        String HQLRequest = "select o from SaddleJournal o WHERE o.detailId is not null and o.date = :date and o.inspectorId = :inspId";
        List<SaddleJournal> journals = currentSession.createQuery(HQLRequest, SaddleJournal.class)
                .setParameter("date", date)
                .setParameter("inspId", inspId).getResultList();
        if(journals != null) {
            for(SaddleJournal journal: journals) {
                report.add(new JournalReport(journal, journal.getDetailId().getName(), journal.getDetailId().forReport(), engineer));
            }
        }
    }

    private void getScrewNutJournals(List<JournalReport> report) {
        Session currentSession = sessionFactory.getCurrentSession();
        String HQLRequest = "select o from ScrewNutJournal o WHERE o.detailId is not null and o.date = :date and o.inspectorId = :inspId";
        List<ScrewNutJournal> journals = currentSession.createQuery(HQLRequest, ScrewNutJournal.class)
                .setParameter("date", date)
                .setParameter("inspId", inspId).getResultList();
        if(journals != null) {
            for(ScrewNutJournal journal: journals) {
                report.add(new JournalReport(journal, journal.getDetailId().getName(), journal.getDetailId().forReport(), engineer));
            }
        }
    }

    private void getScrewStudJournals(List<JournalReport> report) {
        Session currentSession = sessionFactory.getCurrentSession();
        String HQLRequest = "select o from ScrewStudJournal o WHERE o.detailId is not null and o.date = :date and o.inspectorId = :inspId";
        List<ScrewStudJournal> journals = currentSession.createQuery(HQLRequest, ScrewStudJournal.class)
                .setParameter("date", date)
                .setParameter("inspId", inspId).getResultList();
        if(journals != null) {
            for(ScrewStudJournal journal: journals) {
                report.add(new JournalReport(journal, journal.getDetailId().getName(), journal.getDetailId().forReport(), engineer));
            }
        }
    }

    private void getShearPinJournals(List<JournalReport> report) {
        Session currentSession = sessionFactory.getCurrentSession();
        String HQLRequest = "select o from ShearPinJournal o WHERE o.detailId is not null and o.date = :date and o.inspectorId = :inspId";
        List<ShearPinJournal> journals = currentSession.createQuery(HQLRequest, ShearPinJournal.class)
                .setParameter("date", date)
                .setParameter("inspId", inspId).getResultList();
        if(journals != null) {
            for(ShearPinJournal journal: journals) {
                report.add(new JournalReport(journal, journal.getDetailId().getName(), journal.getDetailId().forReport(), engineer));
            }
        }
    }

    private void getSpindleJournals(List<JournalReport> report) {
        Session currentSession = sessionFactory.getCurrentSession();
        String HQLRequest = "select o from SpindleJournal o WHERE o.detailId is not null and o.date = :date and o.inspectorId = :inspId";
        List<SpindleJournal> journals = currentSession.createQuery(HQLRequest, SpindleJournal.class)
                .setParameter("date", date)
                .setParameter("inspId", inspId).getResultList();
        if(journals != null) {
            for(SpindleJournal journal: journals) {
                report.add(new JournalReport(journal, journal.getDetailId().getName(), journal.getDetailId().forReport(), engineer));
            }
        }
    }

    private void getColumnJournals(List<JournalReport> report) {
        Session currentSession = sessionFactory.getCurrentSession();
        String HQLRequest = "select o from ColumnJournal o WHERE o.detailId is not null and o.date = :date and o.inspectorId = :inspId";
        List<ColumnJournal> journals = currentSession.createQuery(HQLRequest, ColumnJournal.class)
                .setParameter("date", date)
                .setParameter("inspId", inspId).getResultList();
        if(journals != null) {
            for(ColumnJournal journal: journals) {
                report.add(new JournalReport(journal, journal.getDetailId().getName(), journal.getDetailId().forReport(), engineer));
            }
        }
    }

    private void getSpringJournals(List<JournalReport> report) {
        Session currentSession = sessionFactory.getCurrentSession();
        String HQLRequest = "select o from SpringJournal o WHERE o.detailId is not null and o.date = :date and o.inspectorId = :inspId";
        List<SpringJournal> journals = currentSession.createQuery(HQLRequest, SpringJournal.class)
                .setParameter("date", date)
                .setParameter("inspId", inspId).getResultList();
        if(journals != null) {
            for(SpringJournal journal: journals) {
                report.add(new JournalReport(journal, journal.getDetailId().getName(), journal.getDetailId().forReport(), engineer));
            }
        }
    }

    private void getAbovegroundCoatingJournals(List<JournalReport> report) {
        Session currentSession = sessionFactory.getCurrentSession();
        String HQLRequest = "select o from AbovegroundCoatingJournal o WHERE o.detailId is not null and o.date = :date and o.inspectorId = :inspId";
        List<AbovegroundCoatingJournal> journals = currentSession.createQuery(HQLRequest, AbovegroundCoatingJournal.class)
                .setParameter("date", date)
                .setParameter("inspId", inspId).getResultList();
        if(journals != null) {
            for(AbovegroundCoatingJournal journal: journals) {
                report.add(new JournalReport(journal, journal.getDetailId().getName(), journal.getDetailId().forReport(), engineer));
            }
        }
    }

    private void getAbrasiveMaterialJournals(List<JournalReport> report) {
        Session currentSession = sessionFactory.getCurrentSession();
        String HQLRequest = "select o from AbrasiveMaterialJournal o WHERE o.detailId is not null and o.date = :date and o.inspectorId = :inspId";
        List<AbrasiveMaterialJournal> journals = currentSession.createQuery(HQLRequest, AbrasiveMaterialJournal.class)
                .setParameter("date", date)
                .setParameter("inspId", inspId).getResultList();
        if(journals != null) {
            for(AbrasiveMaterialJournal journal: journals) {
                report.add(new JournalReport(journal, journal.getDetailId().getName(), journal.getDetailId().forReport(), engineer));
            }
        }
    }

    private void getUndercoatJournals(List<JournalReport> report) {
        Session currentSession = sessionFactory.getCurrentSession();
        String HQLRequest = "select o from UndercoatJournal o WHERE o.detailId is not null and o.date = :date and o.inspectorId = :inspId";
        List<UndercoatJournal> journals = currentSession.createQuery(HQLRequest, UndercoatJournal.class)
                .setParameter("date", date)
                .setParameter("inspId", inspId).getResultList();
        if(journals != null) {
            for(UndercoatJournal journal: journals) {
                report.add(new JournalReport(journal, journal.getDetailId().getName(), journal.getDetailId().forReport(), engineer));
            }
        }
    }

    private void getUndergroundCoatingJournals(List<JournalReport> report) {
        Session currentSession = sessionFactory.getCurrentSession();
        String HQLRequest = "select o from UndergroundCoatingJournal o WHERE o.detailId is not null and o.date = :date and o.inspectorId = :inspId";
        List<UndergroundCoatingJournal> journals = currentSession.createQuery(HQLRequest, UndergroundCoatingJournal.class)
                .setParameter("date", date)
                .setParameter("inspId", inspId).getResultList();
        if(journals != null) {
            for(UndergroundCoatingJournal journal: journals) {
                report.add(new JournalReport(journal, journal.getDetailId().getName(), journal.getDetailId().forReport(), engineer));
            }
        }
    }

    private void getControlWeldJournals(List<JournalReport> report) {
        Session currentSession = sessionFactory.getCurrentSession();
        String HQLRequest = "select o from ControlWeldJournal o WHERE o.detailId is not null and o.date = :date and o.inspectorId = :inspId";
        List<ControlWeldJournal> journals = currentSession.createQuery(HQLRequest, ControlWeldJournal.class)
                .setParameter("date", date)
                .setParameter("inspId", inspId).getResultList();
        if(journals != null) {
            for(ControlWeldJournal journal: journals) {
                report.add(new JournalReport(journal, journal.getDetailId().getName(), journal.getDetailId().forReport(), engineer));
            }
        }
    }

    private void getSheetMaterialJournals(List<JournalReport> report) {
        Session currentSession = sessionFactory.getCurrentSession();
        String HQLRequest = "select o from SheetMaterialJournal o WHERE o.detailId is not null and o.date = :date and o.inspectorId = :inspId";
        List<SheetMaterialJournal> journals = currentSession.createQuery(HQLRequest, SheetMaterialJournal.class)
                .setParameter("date", date)
                .setParameter("inspId", inspId).getResultList();
        if(journals != null) {
            for(SheetMaterialJournal journal: journals) {
                report.add(new JournalReport(journal, journal.getDetailId().getName(), journal.getDetailId().forReport(), engineer));
            }
        }
    }

    private void getRolledMaterialJournals(List<JournalReport> report) {
        Session currentSession = sessionFactory.getCurrentSession();
        String HQLRequest = "select o from RolledMaterialJournal o WHERE o.detailId is not null and o.date = :date and o.inspectorId = :inspId";
        List<RolledMaterialJournal> journals = currentSession.createQuery(HQLRequest, RolledMaterialJournal.class)
                .setParameter("date", date)
                .setParameter("inspId", inspId).getResultList();
        if(journals != null) {
            for(RolledMaterialJournal journal: journals) {
                report.add(new JournalReport(journal, journal.getDetailId().getName(), journal.getDetailId().forReport(), engineer));
            }
        }
    }

    private void getStoreControlJournals(List<JournalReport> report) {
        Session currentSession = sessionFactory.getCurrentSession();
        String HQLRequest = "select o from StoreControlJournal o WHERE o.date = :date and o.inspectorId = :inspId";
        List<StoreControlJournal> journals = currentSession.createQuery(HQLRequest, StoreControlJournal.class)
                .setParameter("date", date)
                .setParameter("inspId", inspId).getResultList();
        if(journals != null) {
            for(StoreControlJournal journal: journals) {
                report.add(new JournalReport(journal, "Хранение и складирование материалов", "", engineer));
            }
        }
    }

    private void getWeldingMaterialJournals(List<JournalReport> report) {
        Session currentSession = sessionFactory.getCurrentSession();
        String HQLRequest = "select o from WeldingMaterialJournal o WHERE o.detailId is not null and o.date = :date and o.inspectorId = :inspId";
        List<WeldingMaterialJournal> journals = currentSession.createQuery(HQLRequest, WeldingMaterialJournal.class)
                .setParameter("date", date)
                .setParameter("inspId", inspId).getResultList();
        if(journals != null) {
            for(WeldingMaterialJournal journal: journals) {
                report.add(new JournalReport(journal, journal.getDetailId().getName(), journal.getDetailId().forReport(), engineer));
            }
        }
    }

    private void getFactoryInspectionJournals(List<JournalReport> report) {
        Session currentSession = sessionFactory.getCurrentSession();
        String HQLRequest = "select o from FactoryInspectionJournal o WHERE o.date = :date and o.inspectorId = :inspId";
        List<FactoryInspectionJournal> journals = currentSession.createQuery(HQLRequest, FactoryInspectionJournal.class)
                .setParameter("date", date)
                .setParameter("inspId", inspId).getResultList();
        if(journals != null) {
            for(FactoryInspectionJournal journal: journals) {
                report.add(new JournalReport(journal, "Контроль разрешительной документации", "", engineer));
            }
        }
    }

    private void getPIDJournals(List<JournalReport> report) {
        Session currentSession = sessionFactory.getCurrentSession();
        String HQLRequest = "select o from PIDJournal o WHERE o.detailId is not null and o.date = :date and o.inspectorId = :inspId";
        List<PIDJournal> journals = currentSession.createQuery(HQLRequest, PIDJournal.class)
                .setParameter("date", date)
                .setParameter("inspId", inspId).getResultList();
        if(journals != null) {
            for(PIDJournal journal: journals) {
                Hibernate.initialize(journal.getDetailId().getSpecification());
                report.add(new JournalReport(journal, journal.getDetailId().getSpecification().getNumber(), journal.getDetailId().forReport(), engineer));
            }
        }
    }
}
