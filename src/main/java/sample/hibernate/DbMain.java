package sample.hibernate;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import sample.hibernate.entities.*;
import sample.hibernate.service.IDbDao;
import sample.hibernate.service.DbDao;

import java.util.Properties;

public class DbMain {
    private SessionFactory factoryVdb;

    private Properties databasePropsVdb;
    private IDbDao daoVdb;

    private void start() {

//        // Only a TRY to make graalVM happy
//        ReflectiveAccess access = new ReflectiveAccess();
//
//        try {
//            access.fetchDbinfo();
//
//        } catch (ClassNotFoundException e) {
//            throw new RuntimeException(e);
//        }


        databasePropsVdb = new Properties();
        databasePropsVdb.setProperty("hibernate.connection.driver_class", "org.h2.Driver");

        // Some dummy data
        databasePropsVdb.setProperty("hibernate.hbm2ddl.auto", "create-drop");
        databasePropsVdb.setProperty("hibernate.hbm2ddl.import_files", "sql/data.sql");

        //databasePropsVdb.setProperty("hibernate.bytecode.use_reflection_optimizer", "false");

        databasePropsVdb.setProperty("hibernate.connection.url", "jdbc:h2:file:~\\db-h2");

        factoryVdb = new Configuration()
                .setProperties(databasePropsVdb)
                .addAnnotatedClass(Dbinfo.class)
                .buildSessionFactory();

        daoVdb = new DbDao();

        daoVdb.setSessionFactory(factoryVdb);

        System.out.println("\n===================================================================");
        String versionInfo = daoVdb.getDbVersionText();
        System.out.println("\n===================================================================");
        System.out.println(" !!! DB VERSION !!! " + versionInfo);
        System.out.println("\n===================================================================");

    }

    public static void main(String[] args) {
        new DbMain().start();
    }
}
