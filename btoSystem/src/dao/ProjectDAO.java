package dao;

import entity.Project;

import java.util.HashMap;
import interfaces.Reader;
import interfaces.Writer;

public interface ProjectDAO extends Reader, Writer{
    void injectDAO(UserDAO userDAO);
    HashMap<Integer, Project> get();
    boolean read();
    boolean write();
    boolean add(Project project);
    boolean remove(Project project);
    Project getProject(int id);
}
