package controller;


import entity.*;

import java.util.List;

public class EnquiryController {
    private List<Enquiry> allEnquiries;

    public EnquiryController(List<Enquiry> allEnquiries) {
        this.allEnquiries = allEnquiries;
    }

    public void createEnquiry(IEnquiryCreator creator, String enquiryText, Project project) {
       creator.createEnquiry(enquiryText, project);
    }

    //applicant can only view their own enquiry
    public void viewApplicantEnquiries(IEnquiryCreator creator) {
       creator.viewEnquiries();
    }

    public void editEnquiry(IEnquiryCreator creator, String enquiryId, String newContent) {
        creator.editEnquiry(enquiryId, newContent);
    }

    public void deleteEnquiry(IEnquiryCreator creator, String enquiryId) {
        creator.deleteEnquiry(enquiryId);
    }

    // HDB Officer or Manager can reply to an enquiry
    public void replyToEnquiry(IEnquiryHandler handler, String enquiryId, String replyContent) {
        handler.replyToEnquiry(enquiryId, replyContent);
    }

    public void handleProjectEnquiry(HDBOfficer officer, Enquiry enquiry) {
        officer.handleProjectEnquiry(enquiry);
    }

    //HDB Manager can view all enquiries
    public void viewAllEnquiries(IEnquiryManager manager) {
        manager.viewAllEnquiries(allEnquiries);
    }


}
