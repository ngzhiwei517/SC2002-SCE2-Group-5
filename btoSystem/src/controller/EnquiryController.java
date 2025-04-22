package controller;


import dao.EnquiryCSVDAO;
import dao.EnquiryDAO;
import entity.*;
import interfaces.Reader;
import interfaces.Writer;
import interfaces.ExitRequired;
import interfaces.InitRequired;

import javax.swing.*;
import java.io.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EnquiryController implements InitRequired, ExitRequired {
    private static Map<Integer, Enquiry> enquiries = new HashMap<Integer, Enquiry>();
    private final String enquiryPath = "EnquiryList.csv";
    private static UserController userController;
    private static ProjectController projectController;
    private static ApplicationController applicationController;
    private static EnquiryController enquiryController;
    private static ReceiptController receiptController;

    private EnquiryDAO enquiryDAO;

    public void init()
    {
        userController = SessionController.getUserController();
        projectController = SessionController.getProjectController();
        applicationController = SessionController.getApplicationController();
        enquiryController = SessionController.getEnquiryController();
        receiptController = SessionController.getReceiptController();

        enquiryDAO = SessionController.getEnquiryDAO();
        enquiryDAO.injectDAO(SessionController.getUserDAO(), SessionController.getProjectDAO());

        try {
            enquiryDAO.read();
            //readData();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void exit()
    {
        enquiryDAO.write();
        //writeData();
    }


    public boolean newEnquiry(Applicant user, Project project, String title, String body)
    {
        Enquiry enquiry = new Enquiry(project, user, Enquiry.Status.PENDING, title, body);
        enquiryDAO.add(enquiry);
        enquiryDAO.write();
        return true;
    }

    public List<Enquiry> getEnquiries(Project project)
    {
        List<Enquiry> retList = new ArrayList<>();
        HashMap<Integer, Enquiry> enquiries = enquiryDAO.get();
        for(Enquiry enq : enquiries.values()) {
            if(enq.getProject().equals(project))
            {
                retList.add(enq);
            }
        }
        return retList;
    }

    public boolean deleteEnquiry(Enquiry enquiry)
    {
        if(enquiryDAO.remove(enquiry))
        {
            return true;
        }
        return false;
    }

}
