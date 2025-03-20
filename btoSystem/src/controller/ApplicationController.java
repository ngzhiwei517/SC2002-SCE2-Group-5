package controller;

import entity.Applicant;
import entity.Application;
import entity.HDBManager;
import entity.Project;

import java.util.ArrayList;
import java.util.List;

public class ApplicationController {
    private final List<Application> applications;
    private HDBManager manager;

    public ApplicationController(HDBManager manager) {
        this.applications = new ArrayList<>();
        this.manager = manager;
    }

    public boolean submitApplication(Applicant applicant, Project project, String flatType) {
        // Check if applicant already has an active application
        for (Application app : applications) {
            if (app.getApplicant().equals(applicant) && (app.getStatus() == Application.Status.PENDING || app.getStatus() == Application.Status.SUCCESSFUL)) {
                System.out.println("Applicant already has an active application. Cannot submit a new one.");
                return false;
            }
        }

        // Create a new application
        Application newApplication = new Application(applicant, project, flatType);
        applications.add(newApplication);
        System.out.println("Application submitted successfully: " + newApplication.getApplicationId());
        return true;
    }

    public void processApplication(String applicationId, boolean isApproved) {
        for (Application app : applications) {
            if (app.getApplicationId().equals(applicationId)) {
                app.setStatus(isApproved ? Application.Status.SUCCESSFUL : Application.Status.UNSUCCESSFUL);
                System.out.println("Application " + applicationId + " has been " + (isApproved ? "approved" : "rejected") + ".");
                return;
            }
        }
        System.out.println("Application not found.");
    }


    public void viewApplications() {
        //deal with csv
        }
}
