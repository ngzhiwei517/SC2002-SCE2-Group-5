package entity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Applicant extends User {

    private List<Application> applications = new ArrayList<>();
    private List<Enquiry> enquiries = new ArrayList<>();

    public Applicant(int id, String name, String nric, int age, boolean isMarried, String password){
        super(id, name, nric, age, isMarried, password);
    }

    public boolean addEnquiry(Enquiry enquiry) {
        //System.out.println("Adding Enquiry: " + enquiry);
        return enquiries.add(enquiry);
    }

    public List<Enquiry> getEnquiries() { return enquiries; }

    public boolean removeEnquiry(Enquiry enquiry) {
        return enquiries.remove(enquiry);
    }

    public boolean addApplication(Application app) {
        return applications.add(app);
    }

    public List<Application> getApplications() { return applications; }

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

    public boolean removeApplication(Application app) {
        return applications.remove(app);
    }

    public boolean canApply(Project project, boolean asOfficer)
    {
        if(project == null)
            return false;

        if(!project.isEligible(this)) //check age here.
        {
            return false;
        }

        //check if today's date within application period.
        LocalDate today = LocalDate.now();
        if (today.isBefore(project.getOpeningDate()) || today.isAfter(project.getClosingDate())) {
            // Today is NOT within the project period
            return false;
        }


        //if theres an application that has a pending/approved/booked status, return false
        for(Application app : applications)
        {
            if(app.getType() != Application.Type.Officer)
            {
                if(app.getStatus() == Application.Status.BOOKED || app.getStatus() == Application.Status.PENDING || app.getStatus() == Application.Status.REQUESTED_WITHDRAW || app.getStatus() == Application.Status.SUCCESSFUL)
                {
                    return false;
                }
            }
        }
        return true;
}
    public String getAccountType(){
        return "Applicant";
    }
}

