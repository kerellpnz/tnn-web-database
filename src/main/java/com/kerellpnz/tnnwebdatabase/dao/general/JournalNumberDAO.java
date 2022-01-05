package com.kerellpnz.tnnwebdatabase.dao.general;

import com.kerellpnz.tnnwebdatabase.dao.BaseDAO;
import com.kerellpnz.tnnwebdatabase.entity.journal.general.JournalNumber;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Transactional
@Repository
public class JournalNumberDAO extends BaseDAO<JournalNumber> {

    public String getLastOpenJournalNumber() {
        String HQLRequest = "select j1.number from JournalNumber as j1 where j1.isClosed = :isClosed order by j1.id desc";
        List<String> numbers = sessionFactory
                        .getCurrentSession()
                        .createQuery(HQLRequest, String.class)
                        .setParameter("isClosed", false)
                        .getResultList();
        return numbers.get(0);
    }

    public List<String> getOpenJournalNumbers() {
        String HQLRequest = "select j1.number from JournalNumber as j1 where j1.isClosed = :isClosed order by j1.id desc";
        return sessionFactory
                .getCurrentSession()
                .createQuery(HQLRequest, String.class)
                .setParameter("isClosed", false)
                .getResultList();
    }
}
