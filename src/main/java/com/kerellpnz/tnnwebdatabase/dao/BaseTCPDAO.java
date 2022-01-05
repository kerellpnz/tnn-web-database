package com.kerellpnz.tnnwebdatabase.dao;

import com.kerellpnz.tnnwebdatabase.entity.technicalcontrolplans.BaseTCP;
import com.kerellpnz.tnnwebdatabase.entity.unit.detail.Ring;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Repository
public class BaseTCPDAO<T extends BaseTCP> extends BaseDAO<T>{

    public String getOperationTypeById(Class<T> type, int id) {
        String HQLRequest = "select o.operationType from " + type.getSimpleName() + " o WHERE o.id = :id";
        try {
            return sessionFactory.getCurrentSession()
                    .createQuery(HQLRequest, String.class)
                    .setParameter("id", id)
                    .getSingleResult();
        }
        catch (Exception e) {
            return null;
        }
    }

    public List<T> getTCPbyOperationType(Class<T> type, String operationType) {
        Session currentSession = sessionFactory.getCurrentSession();
        String HQLRequest = "select o from " + type.getSimpleName() + " o WHERE o.operationType = :operationType";
        Query<T> theQuery = currentSession.createQuery(HQLRequest, type).setParameter("operationType", operationType);
        return theQuery.getResultList();
    }
}
