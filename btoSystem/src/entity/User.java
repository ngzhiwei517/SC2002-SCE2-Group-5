package entity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class User {
    int id;
    private String name;
    private String nric;
    private int age;
    private boolean isMarried;
    private String password;

    private static Map<Integer, User> users = new HashMap<Integer, User>();

    // Constructor
    public User(int id, String name, String nric, int age, boolean isMarried, String password) {
        this.id = id;
        this.name = name;
        this.nric = nric;
        this.age = age;
        this.isMarried = isMarried;
        this.password = password;

        if(!users.containsKey(id)){
            users.put(id, this);
        }
        else {
            System.out.println("duplicate user id: " + id);
        }
    }

    // Getters
    public int getID() { return id; }

    public String getName() {return name;}

    public String getNric() { return nric; }

    public int getAge() { return age; }

    public boolean isMarried() { return isMarried; }

    // Setters
    public void setName(String name) { this.name = name; }

    public void setAge(int age) { this.age = age; }

    public void setMarried(boolean isMarried) { this.isMarried = isMarried; }

    // static methods
    public static User getUser(int id) { return users.get(id); }


    // Common methods
    public boolean verifyPassword(String inputPassword) { return this.password.equals(inputPassword); }

    public boolean changePassword(String oldPassword, String newPassword) {
        if (verifyPassword(oldPassword)) {
            this.password = newPassword;
            return true;
        }
        return false;
    }

}
