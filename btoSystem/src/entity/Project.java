package entity;

import controller.UserController;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;


enum FlatType {
    TWO_ROOM("2-Room"),
    THREE_ROOM("3-Room");

    private final String displayName;

    FlatType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}


public class Project {
    public enum Status {
        VISIBLE,
        INVISIBLE
    }

    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M/d/yyyy");
    private static int nextid = 0;
    private int p_id;
    private String projectName;
    private String neighborhood;
    private LocalDate openingDate;
    private LocalDate closingDate;
    private Manager managerInCharge;
    private Status status;
    private int OfficerSlots;

    private List<Officer> Officers; //list that store num of officers that handle this project
    private List<Flat> flats = new ArrayList<>();

    public Project(int p_id, String projectName, String neighborhood, String OpeningDate, String ClosingDate, Manager manager, int officerSlots, List<Officer> officers, Status status) {
        this.p_id = p_id;
        if(p_id >= nextid)
        {
            nextid = ++p_id;
        }
        this.projectName = projectName;
        this.neighborhood = neighborhood;
        this.managerInCharge = manager;
        this.openingDate = LocalDate.parse(OpeningDate, formatter);
        this.closingDate = LocalDate.parse(ClosingDate, formatter);
        this.OfficerSlots = officerSlots;
        this.Officers = officers;
        this.status = status;
    }

    public Project(String projectName, String neighborhood, String OpeningDate, String ClosingDate, Manager manager, int officerSlots, List<Officer> officers, Status status) {
        this.p_id = nextid++;
        this.projectName = projectName;
        this.neighborhood = neighborhood;
        this.managerInCharge = manager;
        this.openingDate = LocalDate.parse(OpeningDate, formatter);
        this.closingDate = LocalDate.parse(ClosingDate, formatter);
        this.OfficerSlots = officerSlots;
        this.Officers = officers;
        this.status = status;
    }

    public int getProjectID() {
        return p_id;
    }

    public List<Flat> getFlats() {
        return flats;
    }

    public boolean addFlat(Flat flat) {
        flats.add(flat);
        return true;
    }

    public boolean addOfficer(Officer officer)
    {
        Officers.add(officer);
        return true;
    }

    public Flat getFlat(int id){
        for(Flat flat : flats)
        {
            if(flat.getId() == id)
            {
                return flat;
            }
        }
        return null;
    }

    public static void print(Project project, boolean by_manager)
    {
        System.out.print("Project: ");
        System.out.print(project.getProjectName());
        System.out.print(", ");
        System.out.println(project.getNeighborhood());

        //separated for easier reading.
        System.out.print("Application Window: ");
        System.out.println(project.getOpeningDate() + " to " + project.getClosingDate());

        System.out.println("Manager: " + project.getManagerInCharge().getName());

        if(by_manager)
        {
            System.out.println("Officer Slots: " + project.getOfficerSlots());

            System.out.print("Officers: ");
            for (Officer officer : project.getOfficers()) {
                System.out.print(officer.getName());
                if(officer != project.getOfficers().get(project.getOfficers().lastIndexOf(officer)))
                {
                    System.out.print(", ");
                }
            }
            System.out.print("\n");
        }

        System.out.println("Flats: ");
        for(Flat flat : project.getFlats())
        {
            flat.print();
        }

        System.out.println("=====================================");
    }

    public void printBasicInformation()
    {
        System.out.print("Project: ");
        System.out.print(this.getProjectName());
        System.out.print(", ");
        System.out.println(this.getNeighborhood());

        //separated for easier reading.
        System.out.print("Application Window: ");
        System.out.println(this.getOpeningDate() + " to " + this.getClosingDate());

        System.out.println("Manager: " + this.getManagerInCharge().getName());
    }

    public void printName()
    {
        System.out.print("Project: ");
        System.out.print(this.getProjectName());
    }

    public boolean isEligible(User user)
    {
        if(user instanceof Officer)
        {
            if(Officers.contains((Officer)user))
            {
                return false;
            }
        }
        for(Flat f : flats) {
            if (user.isMarried()) {
                return true;
            } else if (user.getAge() >= 35)
            {
                if(f.getType() == Flat.Type.TwoRoom)
                    return true;
            }
        }
        return false;
    }

    public List<Flat> getEligibleFlats(Applicant applicant)
    {
        List<Flat> filtered = new ArrayList<>();
        for(Flat f : flats) {
            if (applicant.isMarried()) {
                filtered.add(f);
            } else if (applicant.getAge() >= 35)
            {
                if(f.getType() == Flat.Type.TwoRoom)
                    filtered.add(f);
            }
        }
        return filtered;
    }

    public Status getStatus() { return status; }
    public String getProjectName() { return projectName; }
    public String getNeighborhood() { return neighborhood; }
    public LocalDate getOpeningDate() { return openingDate; }
    public LocalDate getClosingDate() { return closingDate; }
    public Manager getManagerInCharge() {  return managerInCharge; }
    public int getOfficerSlots() { return OfficerSlots; }
    public List<Officer> getOfficers() { return Officers; }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public void setNeighborhood(String neighborhood) {
        this.neighborhood = neighborhood;
    }
    
    public void setOpeningDate(LocalDate applicationOpeningDate) {
        this.openingDate = applicationOpeningDate;
    }

    public void setClosingDate(LocalDate applicationClosingDate) {
        this.closingDate = applicationClosingDate;
    }

    public void setManagerInCharge(Manager managerInCharge) {
        this.managerInCharge = managerInCharge;
    }


    public boolean isDateClash(Project project)
    {
        if(project != this)
        {
            return (project.getOpeningDate().isAfter(this.getOpeningDate()) && project.getOpeningDate().isBefore(this.getClosingDate())) || (project.getClosingDate().isAfter(this.getOpeningDate()) && project.getClosingDate().isBefore(this.getClosingDate()));
        }
        return false;
    }
}
