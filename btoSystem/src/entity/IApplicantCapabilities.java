package entity;

public interface IApplicantCapabilities {
    void viewEligibleProjects();
    void applyForProject(Project project,String flatType);
    void viewApplication();
    void requestWithdrawal();
    void viewEnquiry();
    void addEnquiry(Enquiry enquiry);

    //void editEnquiry(Enquiry enquiry, String newText);
    //void deleteEnquiry(Enquiry enquiry);
}
