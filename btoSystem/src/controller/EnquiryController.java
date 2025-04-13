package controller;


import entity.*;

import javax.swing.*;
import java.io.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EnquiryController {
    private static Map<Integer, Enquiry> enquiries = new HashMap<Integer, Enquiry>();
    private final String enquiryPath = "EnquiryList.csv";
    private UserController userController;
    private ProjectController projectController;

    public void setControllers(UserController uController, ProjectController pController)
    {
        userController = uController;
        projectController = pController;
    }

    public void init()
    {
        try{
            readData();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

    }

    public void exit()
    {
        writeData();
    }

    public boolean readData() throws IOException
    {
        try (BufferedReader br = new BufferedReader(new FileReader(enquiryPath))) {
            String line;
            br.readLine(); // skip header
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
                int e_id = Integer.parseInt(data[0]);
                int p_id = Integer.parseInt(data[1]);
                int u_id = Integer.parseInt(data[2]);
                String status = data[3];
                String title = data[4];
                String body = data[5];
                String str_responder_id = data[6];
                String response = data[7];

                Project project = ProjectController.getProject(p_id);
                Applicant user = (Applicant) UserController.getUser(u_id);

                User responder = null;
                if(!str_responder_id.equalsIgnoreCase("null"))
                {
                    responder = UserController.getUser(Integer.parseInt(str_responder_id));
                }

                //sanity check to check if e_id already in hashmap
                if(enquiries.containsKey(e_id))
                {
                    System.out.println("Duplicate Key Detected: " + e_id);
                }

                Enquiry enquiry = new Enquiry(e_id,project,user,status,title,body,responder, response);
                enquiries.put(e_id, enquiry);
            }
        }
        return false;
    }

    public boolean writeData() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(enquiryPath))){
            bw.write("e.id,p.id,u.id,status,title,body,responder_id,response");
            bw.newLine();
            for(int key : enquiries.keySet()) {
                Enquiry enq = enquiries.get(key);
                String enquiryString = "";
                enquiryString += enq.getEnquiryId() + ",";
                enquiryString += enq.getProject().getProjectID() + ",";
                enquiryString += enq.getUser().getID() + ",";
                enquiryString += enq.getStatus().toString() + ",";
                enquiryString += enq.getTitle() + ",";
                enquiryString += enq.getText() + ",";
                enquiryString += (enq.getResponder() != null ? enq.getResponder().getID() : "null") + ",";
                enquiryString += (enq.getResponse() != null || !enq.getResponse().equals("null") ? enq.getResponse() : "null") + ",";
                bw.write(enquiryString);
                bw.newLine();
                System.out.println("Writing"); //TODO: REMOVE DEBUG IDENTIFIER.
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }


    public boolean newEnquiry(Applicant user, Project project, String title, String body)
    {
        Enquiry enquiry = new Enquiry(project, user, Enquiry.Status.PENDING, title, body);
        enquiries.put(enquiry.getEnquiryId(), enquiry);
        return true;
    }

}
