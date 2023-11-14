package sample.hibernate;

import sample.hibernate.entities.*;


public class ReflectiveAccess {

    public Class<Dbinfo> fetchDbinfo() throws ClassNotFoundException {
        return (Class<Dbinfo>) Class.forName("sample.hibernate.entities.Dbinfo");
    }

}
