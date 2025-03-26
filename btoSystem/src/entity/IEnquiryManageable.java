package entity;

import java.util.List;

public interface IEnquiryManageable {
    public List<Enquiry> getEnquiryFromProject();
    public void replyToEnquiry(Enquiry enquiry, String replyText);
    public Project getProjectDetails(Project project);
}
