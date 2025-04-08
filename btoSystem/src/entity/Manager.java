package entity;

import java.util.*;


public class Manager extends User {

    private List<Project> projects = new ArrayList<>();

    public Manager(int id, String name, String nric, int age, boolean isMarried, String password) {
        super(id, name, nric, age, isMarried, password);
    }

    public List<Project> getProject(){
        return projects;
    }

    public boolean addProject(Project project){
        return projects.add(project);
    }

    public boolean removeProject(Project project) {
        return projects.remove(project);
    }


}