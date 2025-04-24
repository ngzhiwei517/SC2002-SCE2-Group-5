package entity;

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
        REQUESTED_WITHDRAW_BOOKED,
        WITHDRAWN,
        BOOKED,          // Successfully booked a unit
    }

    //can store applicant id within the csv, as an index, this will never be wiped, and is sequential.
    private static int nextid;
    private int id;
    private Applicant user;
    private Project project;
    private Flat flat;
    private Status status;
    private Type type;

    public Application(int id, Applicant user, Project project, Flat flat, Status status, Type type) {
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

        project.addApplication(this);
        user.addApplication(this);
    }

    public Application(Applicant applicant, Project project, Flat flat, Status status, Type type) {         //new applicant
        this.id = nextid++;
        this.user = applicant;
        this.project = project;
        this.flat = flat;
        this.status=status;
        this.type = type;

        this.project.addApplication(this);
        this.user.addApplication(this);
    }

    public Application(Officer officer, Project project, Status status, Type type)
    {
        this.id = nextid++;
        this.user = officer;
        this.project = project;
        this.status = status;
        this.type = type;
        this.flat = null;

        this.project.addApplication(this);
        this.user.addApplication(this);
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
    public int getId() { return id; }

    public Applicant getUser() { return user; }

    public Project getProject() { return project; }

    public Status getStatus() { return status; }

    public Type getType() { return type;}

    public Flat getFlat() { return flat; }

    public void setStatus(Status status) {
        this.status = status;
    }

    public boolean approve() {
        if(this.status == Status.REQUESTED_WITHDRAW || this.status == Status.REQUESTED_WITHDRAW_BOOKED)
        {
            if(this.status == Status.REQUESTED_WITHDRAW_BOOKED) //TODO:CHECK WHETHER AFTER APPLYING - SLOTS OR AFTER BOOKING THEN - SLOTS.
            {
                this.flat.withdraw();
            }
            this.status = Status.WITHDRAWN;
        }
        else {

            if (this.type == Type.Officer) {
                if(project.getOfficerSlots() <= 0)
                {
                    return false;
                }
                //TODO: assert officer correct slots.
                this.project.addOfficer((Officer) user);
            }
            this.status = Status.SUCCESSFUL;
            return true;
        }
        return false;
    }

    public void reject() { this.status = Status.UNSUCCESSFUL; }

    public boolean book() {
        if(this.flat.book())
        {
            this.status = Status.BOOKED;
            return true;
        }
        return false;
    }



}
