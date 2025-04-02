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

    public void displayAllApplications()
    {
        for(Application app : applications)
        {
            System.out.println(app.getApplicant().getName() + " | " + app.getStatus() + " | " + app.getFlat().getType() + " | " + app.getProject().getProjectName());
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

                User user = userController.getUser(applicant_id);
                Project project = projectController.getProject(project_id);
                Flat flat = project.getFlat(flat_id);
                Application.Status status = Application.Status.valueOf(data[5]);
                Application application = new Application(id, user, project,flat,status);
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
                String applicationString = app.getApplicant().getNric() + "," +
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

    public List<Application> getApplications(List<Application.Status> filter)
    {
        List<Application> filteredApplications = new ArrayList<>();
        for(Application app: applications) {
            if(filter.contains(app.getStatus()))
                filteredApplications.add(app);
        }
        return filteredApplications;
    }

    public boolean tryApply(User user, Project project, Flat flat)
    {
        //if() //assertion check to check if user can actually apply for project, by default this should be true, as it is asserted previously within the boundary.

        //assertion check that user does not already have a application for this flat.
        Application application = new Application(user, project, flat, Application.Status.PENDING);
        applications.add(application);
        return true;
        //return false;
    }

    public List<Application> getUserApplications(User user)
    {
        List<Application> retList = new ArrayList<>();
        for(Application app : applications)
        {
            if(app.getApplicant() == user)
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
