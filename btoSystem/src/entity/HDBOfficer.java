package entity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class HDBOfficer extends User implements IEnquiryHandler,IApplicationOfficerCapabilities{
    private List<Enquiry> allEnquiries;
    private Project assignedProject; // Officer handles applications for one project
    private Applicant applicantProfile; // Composition for applying as an Applicant
    private List<Application> handledApplications; //store a list of all applications to one project

    public HDBOfficer(String name,String nric,String password,int age,boolean isMarried,Project project){
        super(name, nric, password, age, isMarried);
        this.assignedProject = project;
        this.allEnquiries = new ArrayList<>();
        this.handledApplications = new ArrayList<>()
        this.applicantProfile=null;
    }

    public void enableApplicantMode() {
        if (this.applicantProfile == null) {
            this.applicantProfile = new Applicant(this.getName(), this.getNric(), "tempPassword", this.getAge(), this.isMarried());
        }
    }

    public boolean isApplicant() {
        return this.applicantProfile != null;
    }


    public Project getAssignedProject() {
        return assignedProject;
    }

    public Applicant getApplicantProfile() {
        return applicantProfile;
    }


    @Override
    public boolean canApplyForProject(Project project) {
        return true; //need to be revised
    }

    public boolean canHandleApplication(Application app) {
        return app.getProject().equals(this.assignedProject);
    }

    public void viewHandledApplications() {
        System.out.println("Applications handled by " + getName() + ":");
        for (Application app : handledApplications) {
            System.out.println("Application ID: " + app.getApplicationId() + " | Status: " + app.getStatus());
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

    @Override

}

class enquiryfactory {
    public static Enquiry createEnquiry(String enquiryText, User user, Project project) {
        return new Enquiry("E" + System.currentTimeMillis(), enquiryText, (Applicant) user, project);
    }
}
