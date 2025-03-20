package entity;
public class Enquiry {
    private String enquiryId;
    private String content;
    private String response;
    private Applicant applicant;
    private Project project;

    public Enquiry(String enquiryId, String content,Applicant applicant, Project project) {
        this.enquiryId = enquiryId;
        this.content = content;
        this.response = null;
        this.project = project;
        this.applicant = applicant;
    }

    public String getEnquiryId() {
        return enquiryId;
    }

    public String getContent() {
        return content;
    }

    public void updateContent(String newContent) {
        this.content = newContent;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public String getResponse() {
        return response;
    }
}
