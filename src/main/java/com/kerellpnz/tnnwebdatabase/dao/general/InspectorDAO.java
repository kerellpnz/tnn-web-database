package com.kerellpnz.tnnwebdatabase.dao.general;

import com.kerellpnz.tnnwebdatabase.dao.BaseDAO;
import com.kerellpnz.tnnwebdatabase.entity.general.Inspector;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Repository
public class InspectorDAO extends BaseDAO<Inspector> {

    public Inspector getByLogin(String login) {
        String HQLRequest = "FROM Inspector i WHERE i.login = :login";
        try {
            return sessionFactory.getCurrentSession()
                    .createQuery(HQLRequest, Inspector.class)
                    .setParameter("login", login)
                    .getSingleResult();
        }
        catch (Exception e) {
            //e.printStackTrace();
            return null;
        }
    }

    public Inspector findByInspectorLogin(String searchLogin) {
        String login = searchLogin.toLowerCase();
        Query<Inspector> theQuery = sessionFactory.getCurrentSession()
                .createQuery("from Inspector i where i.login = :login", Inspector.class)
                        .setParameter("login", login);
        Inspector inspector = null;
        try {
            inspector = theQuery.getSingleResult();
        } catch (Exception e) {
            inspector = null;
        }
        return inspector;
    }
}
