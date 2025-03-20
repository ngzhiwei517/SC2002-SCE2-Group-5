package entity;

//For all the applicants
public interface IEnquiryCreator {
    void createEnquiry(String enquiryText, Project project);
    void viewEnquiries();
    void editEnquiry(String enquiryId, String newContent);
    void deleteEnquiry(String enquiryId);
}
