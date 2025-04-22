package controller;

import dao.ApplicationDAO;
import entity.*;
import controller.*;
import interfaces.Reader;
import interfaces.Writer;
import interfaces.ExitRequired;
import interfaces.InitRequired;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ApplicationController implements InitRequired, ExitRequired {
    //private final String applicationPath = "ApplicationList.csv";
    //private static Map<Integer, Application> applications = new HashMap<Integer, Application>();

    private static UserController userController;
    private static ProjectController projectController;
    private static ApplicationController applicationController;
    private static EnquiryController enquiryController;
    private static ReceiptController receiptController;

    private ApplicationDAO applicationDAO;

    public void init()
    {
        userController = SessionController.getUserController();
        projectController = SessionController.getProjectController();
        applicationController = SessionController.getApplicationController();
        enquiryController = SessionController.getEnquiryController();
        receiptController = SessionController.getReceiptController();

        applicationDAO = SessionController.getApplicationDAO();
        applicationDAO.injectDAO(SessionController.getUserDAO(), SessionController.getProjectDAO());


        try {
            applicationDAO.read();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void exit()
    {
        applicationDAO.write();
       //writeData();
    }

    /*
    public boolean readData() throws IOException
    {
        try (BufferedReader br = new BufferedReader(new FileReader(applicationPath))) {
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

                Applicant user = (Applicant) userController.getUser(applicant_id);
                Project project = projectController.getProject(project_id);

                Flat flat = flat_id != -1 ? project.getFlat(flat_id) : null;

                Application application = new Application(id, user, project,flat,status,type);
                applications.put(id, application);
                System.out.println("Reading");
            }
        }
        return false;
    }



    public boolean writeData()
    {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(applicationPath))){
            bw.write("a.id,u.id,p.id,f.id,type,status");
            bw.newLine();
            //form the line application string here
            for(int key : applications.keySet()) {
                Application app = applications.get(key);
                String applicationString = "";
                applicationString += app.getId() + ",";
                applicationString += app.getUser().getID() + ",";
                applicationString += app.getProject().getProjectID() + ",";
                applicationString += (app.getFlat() != null ? app.getFlat().getId() : "-1") + ",";
                applicationString += app.getType().toString() + ",";
                applicationString += app.getStatus().toString() + ",";
                bw.write(applicationString);
                bw.newLine();
                System.out.println("Writing");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }*/

    public List<Application> getApplications(List<Application.Status> filter, Application.Type type)
    {
        HashMap<Integer, Application> applications = applicationDAO.get();
        List<Application> filteredApplications = new ArrayList<>();
        for(int key: applications.keySet()) {
            Application app = applications.get(key);
            if(filter.contains(app.getStatus()) && app.getType().equals(type))
                filteredApplications.add(app);
        }
        return filteredApplications;
    }

    public Application tryApply(User user, Flat flat)
    {
        //TODO: if() //assertion check to check if user can actually apply for project, by default this should be true, as it is asserted previously within the boundary.

        //assertion check that user does not already have an application for this flat.

        Application application = new Application((Applicant) user, flat.getProject(), flat, Application.Status.PENDING, Application.Type.Applicant);

        if(applicationDAO.add(application))
        {
            applicationDAO.write();
            return application;
        }
        return null;
        //applications.put(application.getId(), application);
        //writeData();

    }

    public Application tryApplyOfficer(Officer officer, Project project)
    {
        //get all projects officer is applied to.
        //TODO: CHECK AGAINST SELF (APPLICANT)

        List<Project> projects = officer.getProjects();
        for(Project p: projects) {
            if(!p.isDateClash(project)) {  //check stuff here.
                return null;
            }
        }
        //TODO:: CHECK AGAINST OFFICER'S APPLICATIONS TOO.

        //CHECK AGAINST PROJECT'S OFFICERSLOTS
        if(project.getOfficerSlots() == 0)
        {
            return null;
        }
        Application app = new Application(officer, project, Application.Status.PENDING, Application.Type.Officer);
        if(applicationDAO.add(app))
        {
            applicationDAO.write();
            return app;
        }
        return null;
    }

    public boolean tryWithdrawApplication(Application application)
    {
        HashMap<Integer, Application> applications = applicationDAO.get();
        if(applications.containsValue(application)) {
            application.setStatus(Application.Status.REQUESTED_WITHDRAW);
            applicationDAO.write();
            return true;
        }
        return false;
    }

    public boolean tryBookApplication(Application application)
    {
        application.book();
        receiptController.generateReceipt(application);
        applicationDAO.write();
        return true;
    }
}
