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

    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M/d/yyyy");
    private String projectName;
    private String neighborhood;
    private LocalDate applicationOpeningDate;
    private LocalDate applicationClosingDate;
    private Manager managerInCharge;
    private int OfficerSlots;
    private List<Officer> Officers; //list that store num of officers that handle this project
    private List<Flat> Flats = new ArrayList<>();

    public Project(String projectName, String neighborhood, String Type1, int Type1Units, int Type1Price, String Type2, int Type2Units, int Type2Price, String OpeningDate, String ClosingDate, Manager manager, int officerSlots, List<Officer> officers) {
        this.projectName = projectName;
        this.neighborhood = neighborhood;
        this.managerInCharge = manager;
        this.applicationOpeningDate = LocalDate.parse(OpeningDate, formatter);
        this.applicationClosingDate = LocalDate.parse(ClosingDate, formatter);
        this.OfficerSlots = officerSlots;
        this.Officers = officers;

        Flats.add(new Flat(Type1, Type1Units, Type1Price));
        Flats.add(new Flat(Type2, Type2Units, Type2Price));
    }

    public List<Flat> getFlats() {
        return Flats;
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

    public Manager getManagerInCharge() {
        return managerInCharge;
    }

    public void setManagerInCharge(Manager managerInCharge) {
        this.managerInCharge = managerInCharge;
    }




}
