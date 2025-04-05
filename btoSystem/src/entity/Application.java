package entity;
import java.util.Date;
public class Application  {
    public enum Type {
        Officer,
        Applicant
    }

    public enum Status {
        PENDING,        // Initial state when application is submitted
        SUCCESSFUL,     // Approved and eligible to book a flat
        UNSUCCESSFUL,   // Rejected, applicant may apply for another project
        REQUESTED_WITHDRAW,
        WITHDRAWN,
        BOOKED,          // Successfully booked a unit
        APPROVED,
        REJECTED,
    }

    //can store applicant id within the csv, as an index, this will never be wiped, and is sequential.
    private static int nextid;
    private int id;
    private User user;
    private final Project project;
    private final Flat flat;
    private Status status;
    private Type type;

    public Application(int id, User user, Project project, Flat flat, Status status, Type type) {
        this.id = id;
        if(id >= nextid)
        {
            nextid = ++id;
        }
        this.user = user;
        this.project = project;
        this.flat = flat;
        this.status=status;
        this.type = type;
    }

    public Application(Applicant applicant, Project project, Flat flat, Status status, Type type) {         //new applicant
        this.id = nextid++;
        this.user = (User) applicant;
        this.project = project;
        this.flat = flat;
        this.status=status;
        this.type = type;
    }

    public Application(Officer officer, Project project, Status status, Type type)
    {
        this.id = nextid++;
        this.user = (User) officer;
        this.project = project;
        this.status = status;
        this.type = type;
        this.flat = null;
    }

    public void print()
    {
        if(this.type == Type.Applicant) {
            System.out.print(this.getUser().getName());
            System.out.print(" | ");
            System.out.print(this.getProject().getProjectName());
            System.out.print(" | ");
            System.out.print(this.getFlat().getType());
            System.out.print(" | ");
            System.out.print(this.getStatus().toString());
            System.out.println();
        }
        else {
            System.out.print(this.getUser().getName());
            System.out.print(" | ");
            System.out.print(this.getProject().getProjectName());
            System.out.print(" | ");
            System.out.print(this.getStatus().toString());
            System.out.println();
        }
    }

    public User getUser() { return user; }

    public Project getProject() { return project; }

    public Status getStatus() { return status; }

    public Type getType() { return type;}

    public Flat getFlat() { return flat; }

    public void setStatus(Status status) {
        this.status = status;
    }

    public void approve() { this.status = Status.APPROVED; }

    public void reject() { this.status = Status.REJECTED; }



}
