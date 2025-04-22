package dao;

import controller.UserController;
import entity.Applicant;
import entity.Enquiry;
import entity.Project;
import entity.User;

import java.io.*;
import java.util.HashMap;

public class EnquiryCSVDAO implements EnquiryDAO {
    private ProjectDAO projectDAO;
    private UserDAO userDAO;
    private HashMap<Integer, Enquiry> enquiries = new HashMap<>();
    private final String path = "EnquiryList.csv";

    @Override
    public void injectDAO(UserDAO userDAO, ProjectDAO projectDAO) {
        this.userDAO = userDAO;
        this.projectDAO = projectDAO;
    }

    @Override
    public HashMap<Integer, Enquiry> get() {
        return enquiries;
    }

    @Override
    public boolean read() {
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
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

                Project project = projectDAO.getProject(p_id);
                Applicant user = (Applicant) userDAO.getUser(u_id);

                User responder = null;
                if(!str_responder_id.equalsIgnoreCase("null"))
                {
                    responder = userDAO.getUser(Integer.parseInt(str_responder_id));
                }

                //sanity check to check if e_id already in hashmap
                if(enquiries.containsKey(e_id))
                {
                    System.out.println("Duplicate Key Detected: " + e_id);
                }

                Enquiry enquiry = new Enquiry(e_id,project,user,status,title,body,responder, response);
                enquiries.put(e_id, enquiry);

                System.out.println("Reading Enq"); //TODO: REMOVE DEBUG IDENTIFIER.
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public boolean write() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(path))){
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
                String response = enq.getResponse();
                enquiryString += (response != null && !response.equals("null") ? response : "null") + ",";
                bw.write(enquiryString);
                bw.newLine();
                System.out.println("Writing Enq"); //TODO: REMOVE DEBUG IDENTIFIER.
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean add(Enquiry enquiry) {
        if(!enquiries.containsKey(enquiry.getEnquiryId()))
        {
            enquiries.put(enquiry.getEnquiryId(), enquiry);
            return true;
        }
        return false;
    }

    @Override
    public boolean remove(Enquiry enquiry) {
        if(enquiries.containsKey(enquiry.getEnquiryId()))
        {
            enquiries.remove(enquiry.getEnquiryId());
            return true;
        }
        return false;
    }
}
