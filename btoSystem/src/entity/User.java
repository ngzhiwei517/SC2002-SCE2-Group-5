package entity;

public abstract class User {
    int id;
    private String name;
    private String nric;
    private int age;
    private boolean isMarried;
    private String password;
    //private List<Enquiry> enquiries;


    // Constructor
    public User(int id, String name, String nric, int age, boolean isMarried, String password) {
        this.id = id;
        this.name = name;
        this.nric = nric;
        this.age = age;
        this.isMarried = isMarried;
        this.password = password;
    }


    // Getters
    public int getID() { return id; }

    public String getName() {
        return name;
    }

    public String getNric() {
        return nric;
    }

    public int getAge() {
        return age;
    }

    public boolean isMarried() {
        return isMarried;
    }

    // Setters
    public void setName(String name) {
        this.name = name;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setMarried(boolean isMarried) {
        this.isMarried = isMarried;
    }

    // Common methods
    public boolean verifyPassword(String inputPassword) {
        return this.password.equals(inputPassword);
    }

    public boolean changePassword(String oldPassword, String newPassword) {
        if (verifyPassword(oldPassword)) {
            this.password = newPassword;
            return true;
        }
        return false;
    }

    // Abstract methods
}
