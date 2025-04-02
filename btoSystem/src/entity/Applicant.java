package entity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Applicant extends User {

    private Application activeApplication;
    private List<Enquiry> enquiries;

    public Applicant(int id, String name, String nric, int age, boolean isMarried, String password){
        super(id, name, nric, age, isMarried, password);
        this.activeApplication = null;
        this.enquiries=new ArrayList<>();
    }
}

