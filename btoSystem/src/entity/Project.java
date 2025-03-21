package entity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

enum ToggleStatus {
    ON, OFF;


    //turn on or off the status alternatively/ if current is on then turn off
    public ToggleStatus toggle() {
        return this == ON ? OFF : ON;
    }
}

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
    private String projectName;
    private String neighborhood;
    private LocalDate applicationOpeningDate;
    private LocalDate applicationClosingDate;
    private HDBManager managerInCharge;
    private final int MAX_OFFICER_SLOTS=10;
    private int remainingOfficerSlots;
    private List<HDBOfficer> assignedOfficers; //list that store num of officers that handle this project
    private List<Application> applications;
    private List<Enquiry> enquiries;
    private ToggleStatus status;

    public Project(String projectName, String neighborhood, HDBManager manager) {
        this.projectName = projectName;
        this.neighborhood = neighborhood;
        this.applicationOpeningDate = LocalDate.now(); // Default to today
        this.applicationClosingDate = LocalDate.now().plusMonths(1); // Default closing in 1 month
        this.managerInCharge = manager;
        this.remainingOfficerSlots = MAX_OFFICER_SLOTS;
        this.assignedOfficers = new ArrayList<>();
        this.applications = new ArrayList<>();
        this.enquiries = new ArrayList<>();
        this.status = ToggleStatus.OFF; // Default status is OFF
    }


    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getNeighborhood() {
        return neighborhood;
    }

    public void setNeighborhood(String neighborhood) {
        this.neighborhood = neighborhood;
    }

    public LocalDate getApplicationOpeningDate() {
        return applicationOpeningDate;
    }

    public void setApplicationOpeningDate(LocalDate applicationOpeningDate) {
        this.applicationOpeningDate = applicationOpeningDate;
    }

    public LocalDate getApplicationClosingDate() {
        return applicationClosingDate;
    }

    public void setApplicationClosingDate(LocalDate applicationClosingDate) {
        this.applicationClosingDate = applicationClosingDate;
    }

    public HDBManager getManagerInCharge() {
        return managerInCharge;
    }

    public void setManagerInCharge(HDBManager managerInCharge) {
        this.managerInCharge = managerInCharge;
    }

    public int getRemainingOfficerSlots(){
        return this.remainingOfficerSlots;
    }

    public List<HDBOfficer> getAssignedOfficers() {
        return assignedOfficers;
    }

    public void setAssignedOfficers(List<HDBOfficer> assignedOfficers) {
        this.assignedOfficers = assignedOfficers;
    }

    public List<Application> getApplications() {
        return applications;
    }

    public void setApplications(List<Application> applications) {
        this.applications = applications;
    }

    public List<Enquiry> getEnquiries() {
        return enquiries;
    }

    public void setEnquiries(List<Enquiry> enquiries) {
        this.enquiries = enquiries;
    }

    public ToggleStatus getStatus() {
        return status;
    }

    public void setStatus(ToggleStatus status) {
        this.status = status;
    }

    public boolean addOfficer(HDBOfficer officer) {
        if (assignedOfficers.size() >= MAX_OFFICER_SLOTS) {
            System.out.println("Error: Cannot assign more officers. Maximum reached.");
            return false;
        }
        assignedOfficers.add(officer);
        return true;
    }

    public boolean addApplication(Application application) {
        if (applications.contains(application)) {
            System.out.println("Error: Application already exists for this project.");
            return false;
        }
        applications.add(application);
        return true;
    }




}
