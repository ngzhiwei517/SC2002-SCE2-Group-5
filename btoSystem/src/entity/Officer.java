package entity;

import interfaces.ProjectContainer;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Officer extends Applicant implements ProjectContainer {

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

    public boolean assertDateClash(LocalDate date){
        return true;
    }

    /*
    public boolean canApplyProject(Project project) {
        //check against list of projects, then check against applicant side's applicants.

        //check if officer has already applied to this project
        List<Application> filtered = getApplications(List.of(Application.Status.PENDING, Application.Status.SUCCESSFUL, Application.Status.BOOKED), Application.Type.Applicant);
        for(Application application : filtered){ //this checks if officer has already applied for this as a project.
            if(application.getProject() == project){
                //System.out.println("CannotApplyProject AS APPLICANT");
                return false;
            }
        }

        //check if officer has already applied to this project as an officer
        filtered = getApplications(List.of(Application.Status.SUCCESSFUL, Application.Status.PENDING), Application.Type.Officer);
        for(Application application : filtered){
            if(application.getProject() == project){
                //System.out.println("CannotApplyProject AS OFFICER");
                return false;
            }
        }

        //check if officer applications date clash with any other applications.
        for(Application application : filtered){
            if(!application.getProject().assertDateClash(project.getOpeningDate()) && !application.getProject().assertDateClash(project.getClosingDate())){
                //System.out.println("CannotApplyProject DATE CLASH");
                return false;
            }
        }

        return true;
    }
    */

    @Override
    public boolean canApply(Project project, boolean asOfficer){
        //check against list of projects, then check against applicant side's applicants.

        //check if officer has already applied to this project as an applicant
        List<Application> filtered = getApplications(List.of(Application.Status.PENDING, Application.Status.SUCCESSFUL, Application.Status.BOOKED), Application.Type.Applicant);
        for(Application application : filtered){ //this checks if officer has already applied for this as a project.
            if(application.getProject() == project){
                //System.out.println("CannotApplyProject AS APPLICANT" + project.getProjectName());
                return false;
            }
        }

        //check if officer applications date clash with any other applications as applicant
        for(Application application : filtered){
            if(!(application.getProject().assertDateClash(project.getOpeningDate()) && application.getProject().assertDateClash(project.getClosingDate()))){
                //System.out.println("CannotApplyProject DATE CLASH APPLICANT" + project.getProjectName());
                return false;
            }
        }

        //check if officer has already applied to this project as an officer
        filtered = getApplications(List.of(Application.Status.SUCCESSFUL, Application.Status.PENDING), Application.Type.Officer);
        for(Application application : filtered){
            if(application.getProject() == project){
                //System.out.println("CannotApplyProject AS OFFICER" + project.getProjectName());
                return false;
            }
        }

        //check if officer applications date clash with any other applications as officer
        for(Application application : filtered){
            if(!(application.getProject().assertDateClash(project.getOpeningDate()) && application.getProject().assertDateClash(project.getClosingDate()))){
                //System.out.println("CannotApplyProject DATE CLASH OFFICER" + project.getProjectName());
                return false;
            }
        }

        //check if officer already HAS project.
        for(Project proj : projects)
        {
            if(project == proj){
                //System.out.println("CannotApplyProject HASPROJECT" + project.getProjectName());
                return false;
            }
        }

        if(!asOfficer){
            for(Application app : getApplications()) {
                if (app.getType() == Application.Type.Applicant) {
                    if (app.getStatus() == Application.Status.BOOKED || app.getStatus() == Application.Status.PENDING || app.getStatus() == Application.Status.REQUESTED_WITHDRAW || app.getStatus() == Application.Status.SUCCESSFUL) {
                        //System.out.println("Removing as Applicant");
                        return false;
                    }
                }
            }
        }

        return true;
    }

    public String getAccountType() {
        return "Officer";
    }
}