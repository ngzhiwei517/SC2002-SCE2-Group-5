package controller;

import entity.*;
import interfaces.CSVReader;
import interfaces.CSVWriter;
import interfaces.ExitRequired;
import interfaces.InitRequired;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ReceiptController implements CSVReader, CSVWriter, InitRequired, ExitRequired {
    private final static String receiptPath = "receiptList.csv";
    private static HashMap<Integer, Receipt> receipts = new HashMap<>();

    private static UserController userController;
    private static ProjectController projectController;
    private static ApplicationController applicationController;
    private static EnquiryController enquiryController;
    private static ReceiptController receiptController;

    public void init()
    {
        userController = SessionController.getUserController();
        projectController = SessionController.getProjectController();
        applicationController = SessionController.getApplicationController();
        enquiryController = SessionController.getEnquiryController();
        receiptController = SessionController.getReceiptController();

        try {
            readData();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void exit(){
        writeData();
    }

    public boolean readData() throws IOException
    {
        try (BufferedReader br = new BufferedReader(new FileReader(receiptPath))) {
            String line;
            br.readLine(); // skip header
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
                int receipt_id = Integer.parseInt(data[0]);
                int application_id = Integer.parseInt(data[1]);
                int applicant_id = Integer.parseInt(data[2]);
                String applicantName = data[3];
                String nric = data[4];
                int age = Integer.parseInt(data[5]);
                boolean isMarried = Boolean.parseBoolean(data[6]);
                String flatType = data[7];
                String projectName = data[8];
                LocalDateTime bookingDate = LocalDateTime.parse(data[9], DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm:ss"));
                receipts.put(receipt_id ,new Receipt(receipt_id, application_id, applicant_id, applicantName, nric, age, isMarried, flatType, projectName, bookingDate));
            }
        }
        return false;
    }

    public boolean writeData() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(receiptPath))) {
            bw.write("r.id,a.id,u.id,applicantname,nric,age,isMarried,flattype,projectname,bookingdate");
            bw.newLine();
            for(int key : receipts.keySet()) {
                Receipt receipt = receipts.get(key);
                String receiptString = receipt.toString();
                receiptString += receipt.getReceipt_id() + ",";
                receiptString += receipt.getApplicationID() + ",";
                receiptString += receipt.getApplicantID() + ",";
                receiptString += receipt.getApplicantName() + ",";
                receiptString += receipt.getNric() + ",";
                receiptString += receipt.getAge() + ",";
                receiptString += receipt.isMarried() + ",";
                receiptString += receipt.getFlatType();
                receiptString += receipt.getProjectName();
                receiptString += receipt.getBookingDate().format(DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm:ss"));
                bw.write(receiptString);
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean generateReceipt(Application application)
    {
        Receipt receipt = new Receipt(application, application.getUser());
        receipts.put(receipt.getReceipt_id(), receipt);
        writeData();
        return true;
    }

    public Receipt getReceipt(int receipt_id) {
        if(receipts.containsKey(receipt_id)) {
            return receipts.get(receipt_id);
        }
        return null;
    }

    public static List<Receipt> getReceipt(Applicant applicant) {
        List<Receipt> receiptList = new ArrayList<>();
        for(int key : receipts.keySet()) {
            if(applicant.getID() == receipts.get(key).getApplicantID()) {
                receiptList.add(receipts.get(key));
            }
        }
        return receiptList;
    }
}
