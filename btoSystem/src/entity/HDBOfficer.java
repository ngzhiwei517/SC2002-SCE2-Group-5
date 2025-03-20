package entity;

import java.util.Iterator;
import java.util.List;

public class HDBOfficer extends User implements IEnquiryHandler{
    private List<Enquiry> allEnquiries;
    private List<Project> allProjects;
    private Applicant applicantProfile; // Composition for applying as an Applicant

    public HDBOfficer(String name,String nric,String password,int age,boolean isMarried){
        super(name, nric, password, age, isMarried);
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

    public Applicant getApplicantProfile() {
        return applicantProfile;
    }

    @Override
    public void viewProjects() {
        //deal with CSV where he can view the project he handles and apply
    }


    @Override
    public boolean canApplyForProject(Project project) {
        return true; //need to be revised
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
}

class enquiryfactory {
    public static Enquiry createEnquiry(String enquiryText, User user, Project project) {
        return new Enquiry("E" + System.currentTimeMillis(), enquiryText, (Applicant) user, project);
    }
}
