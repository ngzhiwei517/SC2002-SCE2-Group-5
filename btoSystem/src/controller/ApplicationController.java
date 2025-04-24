package controller;

import dao.ApplicationDAO;
import dao.AuditDAO;
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
    private AuditDAO auditDAO;

    public void init()
    {
        userController = SessionController.getUserController();
        projectController = SessionController.getProjectController();
        applicationController = SessionController.getApplicationController();
        enquiryController = SessionController.getEnquiryController();
        receiptController = SessionController.getReceiptController();

        applicationDAO = SessionController.getApplicationDAO();
        applicationDAO.injectDAO(SessionController.getUserDAO(), SessionController.getProjectDAO());
        auditDAO = SessionController.getAuditDAO();


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

            AuditLog aud = new AuditLog(user.getName(), "application created id " + application.getId());
            auditDAO.append(aud);
            auditDAO.write();

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

            AuditLog aud = new AuditLog(officer.getName(), "application created id " + app.getId());
            auditDAO.append(aud);
            auditDAO.write();

            return app;
        }
        return null;
    }

    public boolean tryWithdrawApplication(Application application)
    {
        if(application.getStatus().equals(Application.Status.REQUESTED_WITHDRAW) || application.getStatus().equals(Application.Status.REQUESTED_WITHDRAW_BOOKED) || application.getStatus().equals(Application.Status.WITHDRAWN))
        {
            return false;
        }

        HashMap<Integer, Application> applications = applicationDAO.get();
        if(applications.containsValue(application)) {
            application.setStatus(Application.Status.REQUESTED_WITHDRAW);
            applicationDAO.write();

            AuditLog aud = new AuditLog(application.getUser().getName(), "application withdrew " + application.getId());
            auditDAO.append(aud);
            auditDAO.write();

            return true;
        }
        return false;
    }

    public boolean tryBookApplication(Application application)
    {
        if(!application.book())
        {
            return false;
        }
        receiptController.generateReceipt(application);
        applicationDAO.write();

        AuditLog aud = new AuditLog(application.getUser().getName(), "application booked " + application.getId());
        auditDAO.append(aud);
        auditDAO.write();

        return true;
    }

    public List<Application> getApplications(Flat flat)
    {
        List<Application> retList = new ArrayList<>();
        HashMap<Integer, Application> applications = applicationDAO.get();
        for(Application app : applications.values()) {
            if(app.getFlat().equals(flat))
            {
                retList.add(app);
            }
        }
        return retList;
    }

    public List<Application> getApplications(Project project)
    {
        List<Application> retList = new ArrayList<>();
        HashMap<Integer, Application> applications = applicationDAO.get();
        for(Application app : applications.values()) {
            if(app.getProject().equals(project))
            {
                retList.add(app);
            }
        }
        return retList;
    }

    public boolean deleteApplication(Application application)
    {
        if(applicationDAO.remove(application)) {

            AuditLog aud = new AuditLog(application.getUser().getName(), "application deleted " + application.getId());
            auditDAO.append(aud);
            auditDAO.write();

            return true;
        }
        return false;
    }
}
