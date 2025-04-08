package controller;

import entity.*;
import controller.*;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ApplicationController {
    private final String applicantPath = "ApplicationList.csv";
    private List<Application> applications = new ArrayList<Application>();
    private UserController userController;
    private ProjectController projectController;

    public void setControllers(UserController uController, ProjectController pController)
    {
        userController = uController;
        projectController = pController;
    }

    public void init()
    {
        try {
            readData();
        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }

    public boolean readData() throws IOException
    {
        try (BufferedReader br = new BufferedReader(new FileReader(applicantPath))) {
            String line;
            br.readLine(); // skip header
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                int id = Integer.parseInt(data[0]);
                int applicant_id = Integer.parseInt(data[1]);
                int project_id = Integer.parseInt(data[2]);
                int flat_id = Integer.parseInt(data[3]);
                Application.Type type = Application.Type.valueOf(data[4]);
                Application.Status status = Application.Status.valueOf(data[5]);

                Applicant user = (Applicant) UserController.getUser(applicant_id);
                Project project = ProjectController.getProject(project_id);

                Flat flat = flat_id != -1 ? project.getFlat(flat_id) : null;

                Application application = new Application(id, user, project,flat,status,type);
                applications.add(application);
            }
        }
        return false;
    }

    public boolean writeData()
    {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(applicantPath))){
            //form the line application string here
            for(Application app: applications) {
                String applicationString = app.getUser().getNric() + "," +
                                        app.getProject().getProjectName() + "," +
                                        app.getFlat().getType() + "," +
                                        app.getStatus();
                bw.write(applicationString);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<Application> getApplications(List<Application.Status> filter, Application.Type type)
    {
        List<Application> filteredApplications = new ArrayList<>();
        for(Application app: applications) {
            if(filter.contains(app.getStatus()) && app.getType().equals(type))
                filteredApplications.add(app);
        }
        return filteredApplications;
    }

    public Application tryApply(User user, Flat flat)
    {
        //if() //assertion check to check if user can actually apply for project, by default this should be true, as it is asserted previously within the boundary.

        //assertion check that user does not already have an application for this flat.

        Application application = new Application((Applicant) user, flat.getProject(), flat, Application.Status.PENDING, Application.Type.Applicant);
        applications.add(application);
        return application;
        //return false;
    }

    public Application tryApplyOfficer(Officer officer, Project project)
    {
        //get all projects officer is applied to.
        //TODO: CHECK AGAINST SELF (APPLICANT)

        List<Project> projects = ProjectController.getProjects(officer);
        for(Project p: projects) {
            if(!p.isDateClash(project)) {  //check stuff here.
                return null;
            }
        }
        return new Application(officer, project, Application.Status.PENDING, Application.Type.Officer);
    }

    public List<Application> getUserApplications(User user)
    {
        List<Application> retList = new ArrayList<>();
        for(Application app : applications)
        {
            if(app.getUser() == user)
            {
                retList.add(app);
            }
            //System.out.println(app.getApplicant().getName() + " | " + app.getStatus() + " | " + app.getFlat().getType() + " | " + app.getProject().getProjectName());
        }
        return retList;
    }

    public boolean tryWithdrawApplication(Application application)
    {
        if(applications.contains(application)) {
            application.setStatus(Application.Status.WITHDRAWN);
            return true;
        }
        return false;
    }
}
