package controller;

import entity.*;
import controller.*;

import java.io.*;
import java.util.List;

public class ApplicationController {
    private final String applicantPath = "ApplicationList.csv";
    private List<Application> applications;
    private UserController userController;
    private ProjectController projectController;

    public void setControllers(UserController uController, ProjectController pController)
    {
        userController = uController;
        projectController = pController;
    }

    public void init()
    {

    }

    public boolean readData() throws IOException
    {
        try (BufferedReader br = new BufferedReader(new FileReader(applicantPath))) {
            String line;
            br.readLine(); // skip header
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                String applicantID = data[0];
                User user = userController.getUser(applicantID);
                Project project = projectController.getProject(data[1]);
                Flat flat = project.getFlatByString(data[2]);
                Application.Status status = Application.Status.valueOf(data[3]);
                Application application = new Application(user, project,flat,status);
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

    public boolean tryApply(User user, Project project, Flat flat)
    {
        //if() //assertion check to check if user can actually apply for project, by default this should be true, as it is asserted previously within the boundary.

        //assertion check that user does not already have a application for this flat.


        Application application = new Application(user, project, flat, Application.Status.PENDING);
        return true;
        //return false;
    }
}
