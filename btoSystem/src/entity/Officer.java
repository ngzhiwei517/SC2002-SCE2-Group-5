package entity;

import java.util.List;

public class Officer extends Applicant {

    public Officer(int id, String name, String nric, int age, boolean isMarried, String password) {
        super(id, name, nric,  age, isMarried, password);
    }
}