package entity;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Receipt {
    private int application_id;
    private String applicantName;
    private String nric;
    private int age;
    private boolean isMarried;
    private String flatType;
    private String projectName;
    private LocalDateTime bookingDate;

    public Receipt(Application application, Applicant applicant, String flatType, Project project) {
        this.application_id = application.getId();
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
        String fileName = this.applicantName + "_" + this.bookingDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + "_" + this.application_id + ".txt";
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(fileName))){
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            bw.write("=== Booking Receipt ===");
            bw.newLine();
            bw.write("Date: " + bookingDate.format(formatter));
            bw.newLine();
            bw.write("Applicant: " + applicantName);
            bw.newLine();
            bw.write("NRIC: " + nric);
            bw.newLine();
            bw.write("Age: " + age);
            bw.newLine();
            bw.write("Marital Status: " + (isMarried ? "Married" : "Single"));
            bw.newLine();
            bw.write("Flat Type: " + flatType);
            bw.newLine();
            bw.write("Project: " + projectName);
            bw.newLine();
            bw.write("=======================");
            bw.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
