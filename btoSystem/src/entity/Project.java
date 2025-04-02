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
    private LocalDate applicationOpeningDate;
    private LocalDate applicationClosingDate;
    private Manager managerInCharge;
    private Status status;
    private int OfficerSlots;

    private List<Officer> Officers; //list that store num of officers that handle this project
    private List<Flat> Flats = new ArrayList<>();

    public Project(int p_id, String projectName, String neighborhood, String OpeningDate, String ClosingDate, Manager manager, int officerSlots, List<Officer> officers, Status status) {
        this.p_id = p_id;
        if(p_id >= nextid)
        {
            nextid = ++p_id;
        }
        this.projectName = projectName;
        this.neighborhood = neighborhood;
        this.managerInCharge = manager;
        this.applicationOpeningDate = LocalDate.parse(OpeningDate, formatter);
        this.applicationClosingDate = LocalDate.parse(ClosingDate, formatter);
        this.OfficerSlots = officerSlots;
        this.Officers = officers;
        this.status = status;
    }

    public Project(String projectName, String neighborhood, String OpeningDate, String ClosingDate, Manager manager, int officerSlots, List<Officer> officers, Status status) {
        this.p_id = nextid++;
        this.projectName = projectName;
        this.neighborhood = neighborhood;
        this.managerInCharge = manager;
        this.applicationOpeningDate = LocalDate.parse(OpeningDate, formatter);
        this.applicationClosingDate = LocalDate.parse(ClosingDate, formatter);
        this.OfficerSlots = officerSlots;
        this.Officers = officers;
        this.status = status;
    }

    public int getProjectID() {
        return p_id;
    }

    public List<Flat> getFlats() {
        return Flats;
    }

    public boolean addFlat(Flat flat) {
        Flats.add(flat);
        return true;
    }

    public boolean addOfficer(Officer officer)
    {
        Officers.add(officer);
        return true;
    }

    public Flat getFlat(int id){
        for(Flat flat : Flats)
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
        System.out.println(project.getApplicationOpeningDate() + " to " + project.getApplicationClosingDate());

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
            Flat.print(flat);
        }

        System.out.println("=====================================");
        /*
        System.out.println("Project ID: " + project.getProjectID());
        System.out.println("Project Name: " + project.getProjectName());
        System.out.println("Neighbourhood: " + project.getNeighborhood());
        System.out.println("Application Opening Date: " + project.getApplicationOpeningDate());
        System.out.println("Application Closing Date: " + project.getApplicationClosingDate());
        System.out.println("Manager: " + project.getManagerInCharge().getName());
        System.out.println("Officer Slots: " + project.getOfficerSlots());

        for (Officer officer : project.getOfficers()) {
            System.out.print("Officer: " + officer.getName());
        }
        System.out.println("----------------------------");
        for (Flat flat : project.getFlats()) {
            System.out.println("Flat Type: " + flat.getType());
            System.out.println("Flat Price: " + flat.getPrice());
            System.out.println("No. Units: " + flat.getUnits());
            System.out.println("----------------------------");
        }
        System.out.println("================================");
        */
    }

    public Status getStatus() {
        return status;
    }

    public String getProjectName() {
        return projectName;
    }

    public String getNeighborhood() {
        return neighborhood;
    }

    public LocalDate getApplicationOpeningDate() {
        return applicationOpeningDate;
    }

    public LocalDate getApplicationClosingDate() {
        return applicationClosingDate;
    }

    public Manager getManagerInCharge() {
        return managerInCharge;
    }

    public int getOfficerSlots() { return OfficerSlots; }

    public List<Officer> getOfficers() { return Officers; }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public void setNeighborhood(String neighborhood) {
        this.neighborhood = neighborhood;
    }
    
    public void setApplicationOpeningDate(LocalDate applicationOpeningDate) {
        this.applicationOpeningDate = applicationOpeningDate;
    }


    public void setApplicationClosingDate(LocalDate applicationClosingDate) {
        this.applicationClosingDate = applicationClosingDate;
    }

    public void setManagerInCharge(Manager managerInCharge) {
        this.managerInCharge = managerInCharge;
    }




}
