package entity;

public abstract class User {
    private String name;
    private String nric;
    private String password;
    private int age;
    private boolean isMarried;
    //private List<Enquiry> enquiries;


    // Constructor
    public User(String name, String nric, String password, int age, boolean isMarried) {
        this.name = name;
        this.nric = nric;
        this.password = password;
        this.age = age;
        this.isMarried = isMarried;
    }



    // Getters
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
    public abstract void viewProjects();
    public abstract boolean canApplyForProject(Project project);
}
