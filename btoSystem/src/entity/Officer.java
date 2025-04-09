package entity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Officer extends Applicant {

    private List<Project> projects = new ArrayList<Project>();

    public Officer(int id, String name, String nric, int age, boolean isMarried, String password) {
        super(id, name, nric,  age, isMarried, password);
    }

    public boolean addProject(Project project){
        return projects.add(project);
    }

    public boolean removeProject(Project project)
    {
        return projects.remove(project);
    }

    public boolean assertDateClash(LocalDate date){
        return true;
    }

    public boolean canApply(){
        return true;
    }
}