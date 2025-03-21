package entity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Applicant extends User implements IApplicantCapabilities,IEnquiryCreator {

    private Application activeApplication;
    private boolean hasRequestWithdrawal;
    private List<Enquiry> enquiries;


    public Applicant(String name,String nric, String password, int age, boolean isMarried){
        super(name, nric, password, age, isMarried);
        this.activeApplication = null;
        this.enquiries=new ArrayList<>();
    }

    @Override
    public boolean canApplyForProject(Project project) {
        if (activeApplication == null || activeApplication.getStatus() == Application.Status.UNSUCCESSFUL) {
            return true;
        }
        System.out.println("Cannot apply. You already have an active application for: " +
                activeApplication.getProject().getProjectName() + " (Status: " + activeApplication.getStatus() + ")");
        return false;
    }

    public void viewEligibleProjects(){
        //deal with csv
    }

    @Override
    public void applyForProject(Project project, String flatType) {
        if (!canApplyForProject(project)) return;

        activeApplication = new Application(this, project, flatType);
        System.out.println("Application submitted for project: " + project.getProjectName());
    }

    @Override
    public void viewApplication() {
        if (activeApplication == null) {
            System.out.println("No active application.");
            return;
        }
        System.out.println("Application for project " + activeApplication.getProject().getProjectName() +
                " - Status: " + activeApplication.getStatus());
    }

    @Override
    public void requestWithdrawal() {
        if (activeApplication == null) {
            System.out.println("No active application to withdraw.");
            return;
        }
        hasRequestWithdrawal = true;
        System.out.println("Withdrawal request submitted for project: " + activeApplication.getProject().getProjectName());
    }


    @Override
    public void createEnquiry(String enquiryText, Project project) {
        Enquiry enquiry = enquiryFact.createEnquiry(enquiryText, this, project);
        enquiries.add(enquiry);
        System.out.println("Enquiry created: " + enquiryText);
    }

    @Override
    public void viewEnquiries() {
        if (enquiries.isEmpty()) {
            System.out.println("No enquiries found.");
            return;
        }
        System.out.println("Enquiries for " + getName() + ":");
        for (Enquiry e : enquiries) {
            System.out.println(e.getContent());
        }
    }


    @Override
    public void editEnquiry(String enquiryId, String newContent) {
        for (Enquiry e : enquiries) {
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
        Iterator<Enquiry> iterator = enquiries.iterator();
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
}

class enquiryFact {
    public static Enquiry createEnquiry(String enquiryText, User user, Project project) {
        return new Enquiry("E" + System.currentTimeMillis(), enquiryText, (Applicant) user, project);
    }
}
