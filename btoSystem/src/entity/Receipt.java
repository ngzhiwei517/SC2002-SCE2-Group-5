package entity;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Receipt {
    private String applicantName;
    private String nric;
    private int age;
    private boolean isMarried;
    private String flatType;
    private String projectName;
    private LocalDateTime bookingDate;

    public Receipt(Applicant applicant, String flatType, Project project) {
        this.applicantName = applicant.getName();
        this.nric = applicant.getNric();
        this.age = applicant.getAge();
        this.isMarried = applicant.isMarried();
        this.flatType = flatType;
        this.projectName = project.getProjectName();
        this.bookingDate = LocalDateTime.now();
    }


    public void printReceipt() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        System.out.println("=== Booking Receipt ===");
        System.out.println("Date: " + bookingDate.format(formatter));
        System.out.println("Applicant: " + applicantName);
        System.out.println("NRIC: " + nric);
        System.out.println("Age: " + age);
        System.out.println("Marital Status: " + (isMarried ? "Married" : "Single"));
        System.out.println("Flat Type: " + flatType);
        System.out.println("Project: " + projectName);
        System.out.println("=======================");
    }

    // Save receipt as a text file (optional)
    public void saveReceiptToFile() {

        // Implementation can be added to save as a .txt file
    }

}
