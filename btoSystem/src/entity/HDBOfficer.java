package entity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class HDBOfficer extends User implements IEnquiryHandler, IApplicationOfficerCapabilities {
    private List<Enquiry> allEnquiries;
    private Project assignedProject; // Officer handles applications for one project
    private Applicant applicantProfile; // Composition for applying as an Applicant


    public HDBOfficer(String name, String nric, String password, int age, boolean isMarried, Project project) {
        super(name, nric, password, age, isMarried);
        this.assignedProject = project;
        this.allEnquiries = new ArrayList<>();
        this.applicantProfile = null;
    }

    public void enableApplicantMode() {
        if (this.applicantProfile == null) {
            this.applicantProfile = new Applicant(this.getName(), this.getNric(), "tempPassword", this.getAge(), this.isMarried());
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
        this.assignedProject = project;
        project.getAssignedOfficers().add(this);
        System.out.println("Officer " + getName() + " assigned to project: " + project.getProjectName());
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
        Enquiry enquiry = enquiryfactory.createEnquiry(enquiryText, this, project);
        allEnquiries.add(enquiry);
        System.out.println("Enquiry created: " + enquiryText);
    }

    @Override
    public void viewEnquiries() {
        System.out.println("HDB officer viewing all enquiries:");
        for (Enquiry e : allEnquiries) {
            System.out.println(e.getContent());
        }
    }

    @Override
    public void editEnquiry(String enquiryId, String newContent) {
        for (Enquiry e : allEnquiries) {
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
        Iterator<Enquiry> iterator = allEnquiries.iterator();
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

    @Override
    public void replyToEnquiry(String enquiryId, String response) {
        for (Enquiry e : allEnquiries) {
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
