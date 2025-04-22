package dao;

import entity.Application;

import java.io.IOException;
import java.util.HashMap;

public interface ApplicationDAO {
    void injectDAO(UserDAO userDAO, ProjectDAO projectDAO);
    HashMap<Integer, Application> get();
    boolean read();
    boolean write();
    boolean add(Application application);
    boolean remove(Application application);
}
