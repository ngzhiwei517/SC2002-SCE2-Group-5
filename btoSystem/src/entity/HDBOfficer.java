package entity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class HDBOfficer extends User implements IEnquiryHandler, IApplicationOfficerCapabilities {
    private List<Enquiry> handledEnquiries;
    private Project assignedProject; // Officer handles applications for one project
    private Applicant applicantProfile; // Composition for applying as an Applicant


    public HDBOfficer(String name, String nric, String password, int age, boolean isMarried, Project project) {
        super(name, nric, password, age, isMarried);
        this.assignedProject = project;
        this.handledEnquiries = new ArrayList<>();
        this.applicantProfile = null;
    }

    public void enableApplicantMode() {
        if (this.applicantProfile == null) {
            this.applicantProfile = new Applicant(this.getName(), this.getNric(), "Password", this.getAge(), this.isMarried());
        }
    }

    public boolean isApplicant() {
        return this.applicantProfile != null;
    }


    public void applyForProject(Project project, String flatType) {
        if (isApplicant()) {
            applicantProfile.applyForProject(project, flatType);
        } else {
            System.out.println("Error: Officer is not in applicant mode.");
        }
    }


    public void assignToProject(Project project) {
        if (this.assignedProject != null) {
            System.out.println("Error: Officer is already assigned to a project.");
            return;
        }
        if (project.addOfficer(this)) {
            this.assignedProject = project;
            System.out.println("Officer " + getName() + " assigned to project: " + project.getProjectName());
        }
    }


    public Project getAssignedProject() {
        return assignedProject;
    }

    public Applicant getApplicantProfile() {
        return applicantProfile;
    }

    //need to be revised
    @Override
    public boolean canApplyForProject(Project project) {
        return true;
    }

    // Checks if an application belongs to the assigned project
    public boolean canHandleApplication(Application app) {
        return app.getProject().equals(this.assignedProject);
    }

    public void viewHandledApplications() {
        if (assignedProject == null) {
            System.out.println("No project assigned.");
            return;
        }
        System.out.println("Applications for project " + assignedProject.getProjectName() + ":");
        for (Application app : assignedProject.getApplications()) {
            System.out.println("Application ID: " + app.getApplicationId() + " | Status: " + app.getStatus());
        }
    }

    @Override
    public void addApplication(Application application) {
        if (canHandleApplication(application)) {
            assignedProject.addApplication(application);
            application.setStatus(Application.Status.PENDING); // Ensure proper status
            System.out.println("Officer " + getName() + " submitted application " + application.getApplicationId());
        } else {
            System.out.println("Error: Officer cannot submit application for project " + application.getProject().getProjectName());
        }
    }


    @Override
    public void createEnquiry(String enquiryText, Project project) {
        // Check if the enquiry is for the officer's assigned project
        if (assignedProject == null || !assignedProject.equals(project)) {
            System.out.println("Error: Cannot create enquiry for a project not assigned to this officer.");
            return;
        }

        Enquiry enquiry = enquiryfactory.createEnquiry(enquiryText, this, project);
        handledEnquiries.add(enquiry);
        project.getEnquiries().add(enquiry); // Also add to project's enquiries list
        System.out.println("Enquiry created: " + enquiryText);
    }

    @Override
    public void viewEnquiries() {
        if (assignedProject == null) {
            System.out.println("No project assigned to this officer.");
            return;
        }

        System.out.println("Enquiries for project " + assignedProject.getProjectName() + ":");
        for (Enquiry e : handledEnquiries) {
            System.out.println("Enquiry ID: " + e.getEnquiryId() +
                    " | Applicant: " + e.getApplicant().getName() +
                    " | Content: " + e.getContent() +
                    " | Response: " + (e.getResponse() != null ? e.getResponse() : "No response yet"));
        }
    }


    @Override
    public void editEnquiry(String enquiryId, String newContent) {
        for (Enquiry e : handledEnquiries) {
            if (e.getEnquiryId().equals(enquiryId)) {
                e.updateContent(newContent);
                System.out.println("Edited enquiry " + enquiryId);
                return;
            }
        }
        System.out.println("Enquiry not found.");
    }

    @Override
    public void deleteEnquiry(String enquiryId) {
        Iterator<Enquiry> iterator =handledEnquiries.iterator();
        while (iterator.hasNext()) {
            Enquiry enquiry = iterator.next();
            if (enquiry.getEnquiryId().equals(enquiryId)) {
                iterator.remove();
                System.out.println("Enquiry " + enquiryId + " deleted successfully.");
                return;
            }
        }
        System.out.println("Enquiry " + enquiryId + " not found.");
    }

    public void handleProjectEnquiry(Enquiry enquiry) {
        if (assignedProject == null || !assignedProject.equals(enquiry.getProject())) {
            System.out.println("Error: Cannot handle enquiry for a project not assigned to this officer.");
            return;
        }

        if (!handledEnquiries.contains(enquiry)) {
            handledEnquiries.add(enquiry);
            System.out.println("Enquiry " + enquiry.getEnquiryId() + " assigned to officer " + getName());
        } else {
            System.out.println("Enquiry already handled by this officer.");
        }
    }



















    @Override
    public void replyToEnquiry(String enquiryId, String response) {
        for (Enquiry e : handledEnquiries) {
            if (e.getEnquiryId().equals(enquiryId)) {
                e.setResponse(response);
                System.out.println("Replied to enquiry " + enquiryId + ": " + response);
                return;
            }
        }
        System.out.println("Enquiry not found.");
    }


}

class enquiryfactory {
    public static Enquiry createEnquiry(String enquiryText, User user, Project project) {
        return new Enquiry("E" + System.currentTimeMillis(), enquiryText, (Applicant) user, project);
    }
}
