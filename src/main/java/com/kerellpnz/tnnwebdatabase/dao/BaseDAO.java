package com.kerellpnz.tnnwebdatabase.dao;

import com.kerellpnz.tnnwebdatabase.entity.BaseEntity;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public class BaseDAO<T extends BaseEntity> {
    @Autowired
    protected SessionFactory sessionFactory;

    public List<T> getAll(Class<T> type) {
        Session currentSession =sessionFactory.getCurrentSession();
        String HQLRequest = "FROM " + type.getSimpleName();
        Query<T> theQuery = currentSession.createQuery(HQLRequest, type);
        return theQuery.getResultList();
    }

    public T get(Class<T> type, int id) {
        return sessionFactory.getCurrentSession().get(type, id);
    }

    public boolean saveOrUpdate(T t) {
        try  {
            sessionFactory.getCurrentSession().saveOrUpdate(t);
            return true;
        }
        catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean delete(Class<T> type, int id) {
        try {
            Session currentSession = sessionFactory.getCurrentSession();
            String HQLRequest = "DELETE FROM " + type.getSimpleName() +
                    " t WHERE t.id=:typeId";
            Query theQuery = currentSession.createQuery(HQLRequest);
            theQuery.setParameter("typeId", id);
            theQuery.executeUpdate();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
