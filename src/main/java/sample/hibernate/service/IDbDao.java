package sample.hibernate.service;

import org.hibernate.SessionFactory;

public interface IDbDao {

    void setSessionFactory(SessionFactory sessionFactory);

    String getDbVersionText();

}