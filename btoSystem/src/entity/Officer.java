package entity;

import java.util.List;

public class Officer extends Applicant {

    public Officer(String name, String nric, int age, boolean isMarried, String password) {
        super(name, nric,  age, isMarried, password);
    }
}