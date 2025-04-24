package controller;

import dao.AuditDAO;
import dao.ProjectDAO;
import entity.*;
import interfaces.Reader;
import interfaces.Writer;
import interfaces.ExitRequired;
import interfaces.InitRequired;

import java.io.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class ProjectController  implements InitRequired, ExitRequired {
    //private Map<Integer, Project> projects = new HashMap<Integer, Project>();
    //private final String projectPath = "ProjectList.csv";
    //private final String flatPath = "FlatList.csv";
    //private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M/d/yyyy");

    private ProjectDAO projectDAO;
    private AuditDAO auditDAO;

    private static UserController userController;
    private static ProjectController projectController;
    private static ApplicationController applicationController;
    private static EnquiryController enquiryController;
    private static ReceiptController receiptController;

    public void init()
    {
        userController = SessionController.getUserController();
        projectController = SessionController.getProjectController();
        applicationController = SessionController.getApplicationController();
        enquiryController = SessionController.getEnquiryController();
        receiptController = SessionController.getReceiptController();

        projectDAO = SessionController.getProjectDAO();
        projectDAO.injectDAO(SessionController.getUserDAO());
        auditDAO = SessionController.getAuditDAO();

        try {
            projectDAO.read();
            //readData();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void exit() {
        projectDAO.write();
        //writeData();
    }


    public Project addProject(String projectName, String neighbourhood, LocalDate OpeningDate, LocalDate ClosingDate, Manager manager, int officerSlots, Project.Status visiblity)
    {
        HashMap<Integer, Project> projects = projectDAO.get();

        Project new_project = new Project(projectName, neighbourhood,
                OpeningDate, ClosingDate, manager,
                officerSlots, new ArrayList<Officer>() {
        }, visiblity) ;

        if(!projects.containsKey(new_project.getProjectID())) {

            AuditLog aud = new AuditLog(manager.getName(), "Created new project" + new_project.getProjectName());
            auditDAO.append(aud);
            auditDAO.write();


            projectDAO.add(new_project);
            projectDAO.write();
        }
        return new_project;
    }

    public Project getProject(int id)
    {
        HashMap<Integer, Project> projects = projectDAO.get();
        if(projects.containsKey(id))
        {
            return projects.get(id);
        }
        else
            return null;
    }

    public List<Project> getProjects(List<Project.Status> filter)
    {
        HashMap<Integer, Project> projects = projectDAO.get();
        List<Project> filteredList = new ArrayList<>();
            for (int key : projects.keySet()) {
                if(filter.contains(projects.get(key).getStatus())) {
                    filteredList.add(projects.get(key));
                }
        }
        return filteredList;
    }

    public List<Project> getProjects(Officer officer)
    {
        HashMap<Integer, Project> projects = projectDAO.get();
        List<Project> filtered = new ArrayList<Project>();
        for(int key : projects.keySet()) {
            if(projects.get(key).getOfficers().contains(officer)) {
                filtered.add(projects.get(key));
            }
        }
        return filtered;
    }

    public List<Project> getProjects()
    {
        HashMap<Integer, Project> projects = projectDAO.get();
        List<Project> ret = new ArrayList<Project>();
        for(int key : projects.keySet()) {
            ret.add(projects.get(key));
        }
        return ret;
    }

    public List<Project> getEligibleProjects(Applicant user, boolean asOfficer) {

        HashMap<Integer, Project> projects = projectDAO.get();
        List<Project> remapped = new ArrayList<Project>();
        for (int key : projects.keySet()) {
            if(user.canApply(projects.get(key), asOfficer) && projects.get(key).getStatus() == Project.Status.VISIBLE)
            {
                remapped.add(projects.get(key));
            }
        }
        return remapped;
    }

    public static boolean crossAssignFlat(Project project, List<Flat> flats)
    {
        for (Flat flat : flats) {
            project.addFlat(flat);
            flat.setProject(project);
        }
        return true;
    }

    public void setProjectName(Manager manager, Project project, String name)
    {
        String prevProjectName = project.getProjectName();
        project.setProjectName(name);

        AuditLog aud = new AuditLog(manager.getName(), "project " + prevProjectName + " name set to " + project.getProjectName());
        auditDAO.append(aud);
        auditDAO.write();

        projectDAO.write();
    }

    public void setNeighborhood(Manager manager, Project project, String neighbourhood)
    {
        project.setNeighborhood(neighbourhood);

        AuditLog aud = new AuditLog(manager.getName(), "project " + project.getProjectName() + " neighbourhood set to " + project.getNeighborhood());
        auditDAO.append(aud);
        auditDAO.write();

        projectDAO.write();
    }

    public boolean setOpeningDate(Manager manager, Project project, LocalDate date) {
        if (manager.assertDateClash(date, project) && project.getClosingDate().isAfter(date)) {
            project.setOpeningDate(date);
            projectDAO.write();

            AuditLog aud = new AuditLog(manager.getName(), "project " + project.getProjectName() + " opening date set to " + project.getOpeningDate());
            auditDAO.append(aud);
            auditDAO.write();

            return true;
        }
        return false;
    }

    public boolean setClosingDate(Manager manager, Project project, LocalDate date) {
        if(manager.assertDateClash(date, project) && project.getClosingDate().isBefore(date)) {
            project.setClosingDate(date);
            projectDAO.write();

            AuditLog aud = new AuditLog(manager.getName(), "project " + project.getProjectName() + " closing date set to " + project.getClosingDate());
            auditDAO.append(aud);
            auditDAO.write();

            return true;
        }
        return false;
    }

    public boolean toggleVisibility(Manager manager, Project project) {

        AuditLog aud = new AuditLog(manager.getName(), "project " + project.getProjectName() + " visibility toggled to " + project.getStatus().toString());
        auditDAO.append(aud);
        auditDAO.write();

        project.toggleVisibility();
        projectDAO.write();
        return true;
    }

    public boolean deleteFlat(Manager manager, Flat flat)
    {
        List<Application> applications = applicationController.getApplications(flat); //get applications
        for(Application application : applications) {
            if(!applicationController.deleteApplication(application)){ //fixes dependency issues on next import.
                System.out.println("tried to delete non-existent application");
            }
        }

        AuditLog aud = new AuditLog(manager.getName(), "flat removed " + flat.getId() + " from " + flat.getProject().getProjectName());
        auditDAO.append(aud);
        auditDAO.write();

        flat.deleteSelf(); //once removed from project, GC will handle.
        projectDAO.write();
        return true;
    }

    public boolean deleteProject(Manager manager, Project project)
    {
        //find all applications related to project and delete them.
        List<Application> applications = applicationController.getApplications(project);
        for(Application application : applications) {
            if(!applicationController.deleteApplication(application)){ //fixes dependency issues on next import.
                System.out.println("tried to delete non-existent application");
            }
        }

        //this should find all enquiries related to project and delete them.
        List<Enquiry> enquiries = enquiryController.getEnquiries(project);
        for(Enquiry enquiry : enquiries) {
            if (!enquiryController.deleteEnquiry(enquiry)){
                System.out.println("tried to delete non-existent application");
            }
        }

        //project.delete();
        manager.removeProject(project); //remove project from manager, then remove project from projectDAO

        if(projectDAO.remove(project))
        {
            AuditLog aud = new AuditLog(manager.getName(), "project removed" + project.getProjectID() + "," + project.getProjectName());
            auditDAO.append(aud);
            auditDAO.write();

            System.out.println("Project deleted successfully");
        }
        else {
            System.out.println("Failed to delete project");
        }
        return true;
    }

}
