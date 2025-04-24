package controller;

import dao.AuditDAO;
import dao.UserDAO;
import entity.*;
import interfaces.Reader;
import interfaces.Writer;
import interfaces.ExitRequired;
import interfaces.InitRequired;

import java.io.*;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserController implements InitRequired, ExitRequired {
    //private static Map<String, User> Users = new HashMap<>();;
    //private final String userPath = "ApplicantList.csv";

    private UserDAO userDAO;
    private AuditDAO auditDAO;

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

        userDAO = SessionController.getUserDAO();
        auditDAO = SessionController.getAuditDAO();

        try {
            //System.out.println("READ DAO");
            userDAO.read();
            //readData();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void exit()
    {
        userDAO.write();
        //writeData();
    }

    public User getUser(int id)
    {
        HashMap<String, User> users = userDAO.get();
        for(User user: users.values())
        {
            if(user.getID() == id)
                return user;
        }
        return null;
    }

    public User getUser(String username)
    {
        HashMap<String, User> users = userDAO.get();
        if(users.containsKey(username))
            return users.get(username);
        return null;
    }

    public boolean resetPassword(String username, String currentPassword, String newPassword) {
        HashMap<String, User> users = userDAO.get();
        if(!users.containsKey(username)) {
            return false;
        }
        User user = users.get(username);
        if(user.changePassword(currentPassword, newPassword)){

            userDAO.write();

            AuditLog aud = new AuditLog(username, "password changed");
            auditDAO.append(aud);
            auditDAO.write();

            return true;
        }
        return false;
    }
}
