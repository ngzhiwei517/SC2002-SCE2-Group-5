package entity;
import java.util.Date;
public class Application  {
    public enum Status {
        PENDING,        // Initial state when application is submitted
        SUCCESSFUL,     // Approved and eligible to book a flat
        UNSUCCESSFUL,   // Rejected, applicant may apply for another project
        BOOKED          // Successfully booked a unit
    }
    private static int applicationCounter = 0;
    private final String applicationId;
    private final Applicant applicant;
    private final Project project;
    private Status status;
    private final Date applicationDate;
    private final String flatType;

    public Application(Applicant applicant, Project project, String flatType) {
        this.applicationId = "APP" + (++applicationCounter);
        this.applicant = applicant;
        this.project = project;
        this.status=Status.PENDING;
        this.applicationDate = new Date();
        this.flatType = flatType;
    }

    public String getApplicationId() {
        return applicationId;
    }

    public Applicant getApplicant() {
        return applicant;
    }

    public Project getProject() {
        return project;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Date getApplicationDate() {
        return applicationDate;
    }

    public String getFlatType() {
        return flatType;
    }
}
