package dao;

import entity.Project;

import java.util.HashMap;

public interface ProjectDAO {
    void injectDAO(UserDAO userDAO);
    HashMap<Integer, Project> get();
    boolean read();
    boolean write();
    boolean add(Project project);
    boolean remove(Project project);
    Project getProject(int id);
}
