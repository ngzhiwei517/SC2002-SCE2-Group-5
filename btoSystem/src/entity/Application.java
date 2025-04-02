package entity;
import java.util.Date;
public class Application  {
    public enum Status {
        PENDING,        // Initial state when application is submitted
        SUCCESSFUL,     // Approved and eligible to book a flat
        UNSUCCESSFUL,   // Rejected, applicant may apply for another project
        REQUESTED_WITHDRAW,
        WITHDRAWN,
        BOOKED          // Successfully booked a unit
    }

    //can store applicant id within the csv, as an index, this will never be wiped, and is sequential.
    private static int nextid;
    private int id;
    private final Applicant applicant;
    private final Project project;
    private final Flat flat;
    private Status status;

    public Application(int id, User user, Project project, Flat flat, Status status) {
        this.id = id;
        if(id >= nextid)
        {
            nextid = ++id;
        }
        this.applicant = user instanceof Applicant ? (Applicant) user : null;
        this.project = project;
        this.flat = flat;
        this.status=status;
    }

    public Application(User user, Project project, Flat flat, Status status) {         //new applicant
        this.id = nextid++;
        this.applicant = user instanceof Applicant ? (Applicant) user : null;
        this.project = project;
        this.flat = flat;
        this.status=status;
    }

    public static void print(Application app)
    {
        System.out.print(app.getApplicant().getName());
        System.out.print(" | ");
        System.out.print(app.getProject().getProjectName());
        System.out.print(" | ");
        System.out.print(app.getFlat().getType());
        System.out.print(" | ");
        System.out.print(app.getStatus().toString());
        System.out.println();
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

    public Flat getFlat() { return flat; }

    public void setStatus(Status status) {
        this.status = status;
    }



}
