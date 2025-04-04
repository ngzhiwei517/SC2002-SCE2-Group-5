package entity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Applicant extends User {

    List<Application> applications;

    public Applicant(int id, String name, String nric, int age, boolean isMarried, String password){
        super(id, name, nric, age, isMarried, password);
    }

    public boolean hasFlat()
    {
        return false;
    }

    public boolean hasPendingApplication()
    {
        return false;
    }
}

