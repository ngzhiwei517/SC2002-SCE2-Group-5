package entity;

import interfaces.ProjectContainer;

import java.time.LocalDate;
import java.util.*;


public class Manager extends User implements ProjectContainer {

    private List<Project> projects = new ArrayList<>();

    public Manager(int id, String name, String nric, int age, boolean isMarried, String password) {
        super(id, name, nric, age, isMarried, password);
    }

    public List<Project> getProjects(){
        return projects;
    }

    public List<Project> getProjects(List<Project.Status> filter){
        List<Project> projects = new ArrayList<>();
        for(Project project : this.projects){
            if(filter.contains(project.getStatus())){
                projects.add(project);
            }
        }
        return projects;
    }

    public boolean addProject(Project project){
        //System.out.println("Adding project " + project);
        return projects.add(project);
    }

    public boolean removeProject(Project project) {
        return projects.remove(project);
    }

    public boolean assertDateClash(LocalDate date, Project excluded){
        for(Project project : projects){
            if(project == excluded)
            {
                continue;
            }
            //System.out.println("Checking if project " + project.getProjectName() + " is a date clash");
            if(!project.assertDateClash(date)){ //fail to assert date clash.
                System.out.println("Clashed");
                return false;
            }
        }
        return true;
    }

    public String getAccountType(){
        return "Manager";
    }
}