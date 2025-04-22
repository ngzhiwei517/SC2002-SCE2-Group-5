package dao;

import entity.Application;

import java.io.IOException;
import java.util.HashMap;
import interfaces.Reader;
import interfaces.Writer;

public interface ApplicationDAO extends Reader, Writer{
    void injectDAO(UserDAO userDAO, ProjectDAO projectDAO);
    HashMap<Integer, Application> get();
    boolean read();
    boolean write();
    boolean add(Application application);
    boolean remove(Application application);
}
