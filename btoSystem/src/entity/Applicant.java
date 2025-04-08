package entity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Applicant extends User {

    private List<Application> applications = new ArrayList<>();
    private List<Enquiry> enquiries = new ArrayList<>();

    public Applicant(int id, String name, String nric, int age, boolean isMarried, String password){
        super(id, name, nric, age, isMarried, password);
    }

    public boolean addEnquiry(Enquiry enquiry) {
        System.out.println("Adding Enquiry: " + enquiry);
        return enquiries.add(enquiry);
    }

    public List<Enquiry> getEnquiries() { return enquiries; }

    public boolean removeEnquiry(Enquiry enquiry) {
        return enquiries.remove(enquiry);
    }

    public boolean addApplication(Application app) {
        System.out.println("Adding Application: " + app);
        return applications.add(app);
    }

    public List<Application> getApplications() { return applications; }

    public boolean removeApplication(Application app) {
        return applications.remove(app);
    }

    public boolean hasFlat()
    {
        //if theres an application that has a pending/approved/booked status, return false
        return false;
    }

    public boolean hasPendingApplication()
    {
        //if theres an application that has a pending/approved/booked status, return false
        return false;
    }
}

