package entity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Applicant extends User {

    private Application activeApplication;
    private boolean hasRequestWithdrawal;
    private List<Enquiry> enquiries;

    public Applicant(String name,String nric, int age, boolean isMarried, String password){
        super(name, nric, age, isMarried, password);
        this.activeApplication = null;
        this.enquiries=new ArrayList<>();
    }
}

