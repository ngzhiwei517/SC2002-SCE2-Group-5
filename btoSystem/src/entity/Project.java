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
        INVISIBLE,
        DELETED
    }

    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M/d/yyyy");
    private static int nextid = 0;
    private int p_id;
    private String projectName;
    private String neighborhood;
    private LocalDate openingDate;
    private LocalDate closingDate;
    private Manager manager;
    private Status status;
    private int OfficerSlots;
    private List<Application> applications = new ArrayList<>();
    private List<Enquiry> enquiries = new ArrayList<>();

    private List<Officer> Officers; //list that store num of officers that handle this project
    private List<Flat> flats = new ArrayList<>();

    public Project(int p_id, String projectName, String neighborhood, LocalDate OpeningDate, LocalDate ClosingDate, Manager manager, int officerSlots, List<Officer> officers, Status status) {
        this.p_id = p_id;
        if(p_id >= nextid)
        {
            nextid = ++p_id;
        }
        this.projectName = projectName;
        this.neighborhood = neighborhood;
        this.manager = manager;
        this.openingDate = OpeningDate;
        this.closingDate = ClosingDate;
        this.OfficerSlots = officerSlots;
        this.Officers = officers;
        this.status = status;

        for(Officer officer : Officers) //give officer project.
        {
            officer.addProject(this);
        }

        manager.addProjects(this);
    }

    public Project(String projectName, String neighborhood, LocalDate OpeningDate, LocalDate ClosingDate, Manager manager, int officerSlots, List<Officer> officers, Status status) {
        this.p_id = nextid++;
        this.projectName = projectName;
        this.neighborhood = neighborhood;
        this.manager = manager;
        this.openingDate = OpeningDate;
        this.closingDate = ClosingDate;
        this.OfficerSlots = officerSlots;
        this.Officers = officers;
        this.status = status;

        for(Officer officer : Officers) //give officer project.
        {
            officer.addProject(this);
        }

        manager.addProjects(this);
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

    public void print(boolean by_manager)
    {
        System.out.print("Project: ");
        System.out.print(this.getProjectName());
        System.out.print(", ");
        System.out.println(this.getNeighborhood());

        //separated for easier reading.
        System.out.print("Application Window: ");
        System.out.println(this.getOpeningDate() + " to " + this.getClosingDate());

        System.out.println("Manager: " + this.getManager().getName());

        if(by_manager)
        {
            System.out.println("Officer Slots: " + this.getOfficerSlots());

            System.out.print("Officers: ");
            for (Officer officer : this.getOfficers()) {
                System.out.print(officer.getName());
                if(officer != this.getOfficers().get(this.getOfficers().size() - 1))
                    System.out.print(", ");
            }
            System.out.print("\n");
        }

        System.out.println("Flats: ");
        for(Flat flat : this.getFlats())
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

        System.out.println("Manager: " + this.getManager().getName());
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
    public Manager getManager() {  return manager; }
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

    public void setManager(Manager managerInCharge) {
        this.manager = managerInCharge;
    }

    public void toggleVisibility()
    {
        if(status == Status.INVISIBLE)
        {
            status = Status.VISIBLE;
        }
        else if(status == Status.VISIBLE)
        {
            status = Status.INVISIBLE;
        }
        return;
    }

    public boolean isDateClash(Project project)
    {
        LocalDate opdate = project.getOpeningDate();
        LocalDate cldate = project.getClosingDate();
        if(opdate == null || cldate == null)
        {
            return false;
        }
        //TODO: remove debug statements here
        System.out.println(getOpeningDate());
        System.out.println(getClosingDate());

        if(opdate.isEqual(getOpeningDate()) || opdate.isEqual(getClosingDate()))
        {
            System.out.println("equal");
            return false;
        }
        if(cldate.isEqual(getOpeningDate()) || cldate.isEqual(getClosingDate()))
        {
            System.out.println("equal");
            return false;
        }
        else if(opdate.isAfter(getOpeningDate()) && opdate.isBefore(getClosingDate()))
        {
            System.out.println("between");
            return false;
        }
        else if(cldate.isAfter(getOpeningDate()) && cldate.isBefore(getClosingDate()))
        {
            System.out.println("between");
            return false;
        }
        return true;
    }

    public boolean addOfficer(Officer officer)
    {
        if(!Officers.contains(officer))
        {
            Officers.add(officer);
            OfficerSlots--;
            return true;
        }
        return false;
    }

    public boolean delete()
    {
        if(status != Status.DELETED)
        {
            status = Status.DELETED;
            return true;
        }
        return false;
    }

    public boolean deleteFlat(Flat flat)
    {
        return flats.remove(flat);
    }

    public boolean removeOfficer(Officer officer)
    {
        officer.removeProject(this);
        return Officers.remove(officer);
    }



    public List<Application> getApplications() { return applications; }

    public List<Application> getApplications(Application.Type type) {
        List<Application> filtered = new ArrayList<>();
        for(Application application : applications) {
            if(type.equals(application.getType()))
            {
                filtered.add(application);
            }
        }
        return filtered;
    }

    public List<Application> getApplications(List<Application.Status> filter, Application.Type type) {
        List<Application> filtered = new ArrayList<>(); //filter here
        for(Application application : applications) {
            if(filter.contains(application.getStatus()) && type.equals(application.getType()))
            {
                filtered.add(application);
            }
        }
        return filtered;
    }

    public boolean removeApplication(Application application)
    {
        return applications.remove(application);
    }

    public boolean addApplication(Application application){
        return applications.add(application);
    }

    public List<Enquiry> getEnquiries() { return enquiries; }

    public List<Enquiry> getEnquiries(List<Enquiry.Status> filter) {

        List<Enquiry> filtered = new ArrayList<>();
        for(Enquiry enquiry : enquiries) {
            if (filter.contains(enquiry.getStatus()))
            {
                filtered.add(enquiry);
            }
        }
        return filtered;
    }

    public boolean removeEnquiry(Enquiry enquiry) {
        return enquiries.remove(enquiry);
    }

    public boolean addEnquiry(Enquiry enquiry) {
        return enquiries.add(enquiry);
    }

    public boolean assertDateClash(LocalDate date)
    {
        if(date == null)
        {
            return false;
        }

        System.out.println(date.toString());
        System.out.println(getOpeningDate());
        System.out.println(getClosingDate());

        if(date.isEqual(getOpeningDate()) || date.isEqual(getClosingDate()))
        {
            System.out.println("equal");
            return false;
        }
        else if(date.isAfter(getOpeningDate()) && date.isBefore(getClosingDate()))
        {
            System.out.println("between");
            return false;
        }
        return true;
    }

}
