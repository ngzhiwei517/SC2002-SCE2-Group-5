package dao;

import entity.Receipt;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

public class ReceiptCSVDAO implements ReceiptDAO {

    private HashMap<Integer, Receipt> receipts = new HashMap<>();
    private final String path = "users.csv";

    @Override
    public HashMap<Integer, Receipt> get() {
        return receipts;
    }

    @Override
    public boolean read() {
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
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
        catch (IOException ex)
        {
            System.out.println(ex.getMessage());
            return false;
        }
        return true;
    }

    @Override
    public boolean write() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(path))) {
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
            return false;
        }
        return true;
    }

    @Override
    public boolean add(Receipt receipt) {
        if(!receipts.containsKey(receipt.getReceipt_id()))
        {
            receipts.put(receipt.getReceipt_id(), receipt);
            return true;
        }
        return false;
    }

    @Override
    public boolean remove(Receipt receipt) {
        if(receipts.containsKey(receipt.getReceipt_id())) {
            receipts.remove(receipt.getReceipt_id());
            return true;
        }
        return false;
    }
}
