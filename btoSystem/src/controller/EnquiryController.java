package controller;


import entity.*;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
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

    public boolean writeData()
    {
        return false;
    }

    public List<Enquiry> getEnquiries(User user, Project project)
    {
        List<Enquiry> filtered = new ArrayList<Enquiry>();
        for(int key : enquiries.keySet())
        {
            if(enquiries.get(key).getUser().equals(user))
            {
                if(project == enquiries.get(key).getProject() || project == null) {
                    filtered.add(enquiries.get(key));
                }
            }
        }
        return filtered;
    }

    public static List<Enquiry> getEnquiries(List<Enquiry.Status> filter ,Project project)
    {
        List<Enquiry> filtered = new ArrayList<Enquiry>();
        for(int key : enquiries.keySet())
        {
            if(project == enquiries.get(key).getProject() && filter.contains(enquiries.get(key).getStatus()))
            {
                filtered.add(enquiries.get(key));
            }
        }
        return filtered;
    }


    public boolean newEnquiry(Applicant user, Project project, String title, String body)
    {
        Enquiry enquiry = new Enquiry(project, user, Enquiry.Status.PENDING, title, body);
        enquiries.put(enquiry.getEnquiryId(), enquiry);
        return true;
    }

}
