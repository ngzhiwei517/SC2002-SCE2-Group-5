package entity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class HDBManager extends User implements IEnquiryManager {
    private List<Enquiry>allEnquiries;
    private List<Project> allProjects;

    public HDBManager(String name, String nric, String password,int age,boolean isMarried, List<Enquiry> allEnquiries) {
        super(name, nric, password, age, isMarried);
        this.allEnquiries = new ArrayList<>();
        this.allProjects =new ArrayList<>();
    }

    public void createEnquiry(String enquiryText, Project project) {
        Enquiry enquiry = EnquiryFactory.createEnquiry(enquiryText, this, project);
        allEnquiries.add(enquiry);
        System.out.println("Enquiry created: " + enquiryText);
    }

    @Override
    public void viewEnquiries() {
        if (allEnquiries.isEmpty()) {
            System.out.println("No enquiries found.");
            return;
        }
        System.out.println("Enquiries for " + getName() + ":");
        for (Enquiry e : allEnquiries) {
            System.out.println(e.getContent());
        }
    }

    @Override
    public void editEnquiry(String enquiryId, String newContent) {
        for (Enquiry e : allEnquiries) {
            if (e.getEnquiryId().equals(enquiryId)) {
                e.updateContent(newContent);
                System.out.println("Enquiry " + enquiryId + " updated.");
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

    //can save to txt file
    public void viewAllEnquiries(List<Enquiry> enquiries) {
        System.out.println("HDB Manager viewing all enquiries:");
        for (Enquiry e : enquiries) {
            System.out.println(e.getContent());
        }
    }

    //manager cannot apply for projects
    @Override
    public boolean canApplyForProject(Project project) {
        return false;
    }


    public void processApplication(Application application, boolean isApproved) {
        if (isApproved) {
            application.setStatus(Application.Status.SUCCESSFUL);
            System.out.println("Application " + application.getApplicationId() + " approved.");
        } else {
            application.setStatus(Application.Status.UNSUCCESSFUL);
            System.out.println("Application " + application.getApplicationId() + " rejected.");
        }
    }

    public void processWithdrawal(Application application, boolean isApproved) {
        if (isApproved) {
            System.out.println("Withdrawal request for " + application.getApplicationId() + " approved.");
            // Application is withdrawn, so it is no longer valid
        } else {
            System.out.println("Withdrawal request for " + application.getApplicationId() + " rejected.");
        }
    }
}

class EnquiryFactory {
    public static Enquiry createEnquiry(String enquiryText, User user, Project project) {
        return new Enquiry("E" + System.currentTimeMillis(), enquiryText, (Applicant) user, project);
    }
}