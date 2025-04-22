package controller;

import dao.*;
import entity.User;

public class SessionController {

    private static UserController userController;
    private static ApplicationController applicationController;
    private static EnquiryController enquiryController;
    private static ProjectController projectController;
    private static ReceiptController receiptController;

    private static UserDAO userDAO;
    private static ProjectDAO projectDAO;
    private static EnquiryDAO enquiryDAO;
    private static ApplicationDAO applicationDAO;
    private static ReceiptDAO receiptDAO;
    private static AuditDAO auditDAO;


    private static User loggedUser = null;
    public static User getLoggedUser()
    {
        return loggedUser;
    }
    public static void clearLoggedUser() { loggedUser = null; }

    public static void setAuditDAO(AuditDAO auditDAO) {
        SessionController.auditDAO = auditDAO;
    }
    public static AuditDAO getAuditDAO() {
        return auditDAO;
    }

    public static void setReceiptDAO(ReceiptDAO receiptDAO) {
        SessionController.receiptDAO = receiptDAO;
    }

    public static ReceiptDAO getReceiptDAO() {
        return receiptDAO;
    }

    public static void setApplicationDAO(ApplicationDAO applicationDAO)
    {
        SessionController.applicationDAO = applicationDAO;
    }

    public static ApplicationDAO getApplicationDAO()
    {
        return applicationDAO;
    }

    public static void setEnquiryDAO(EnquiryDAO enquiryDAO) {
        SessionController.enquiryDAO = enquiryDAO;
    }

    public static EnquiryDAO getEnquiryDAO() {
        return enquiryDAO;
    }

    public static void setProjectDAO(ProjectDAO projectDAO)
    {
        SessionController.projectDAO = projectDAO;
    }

    public static ProjectDAO getProjectDAO()
    {
        return projectDAO;
    }

    public static void setUserDAO(UserDAO userDAO) {
        SessionController.userDAO = userDAO;
    }

    public static UserDAO getUserDAO() {
        return userDAO;
    }

    // Setter for UserController
    public static void setUserController(UserController controller) {
        userController = controller;
    }

    // Setter for ApplicationController
    public static void setApplicationController(ApplicationController controller) {
        applicationController = controller;
    }

    // Setter for EnquiryController
    public static void setEnquiryController(EnquiryController controller) {
        enquiryController = controller;
    }

    // Setter for ProjectController
    public static void setProjectController(ProjectController controller) {
        projectController = controller;
    }

    // Setter for ReceiptController
    public static void setReceiptController(ReceiptController controller) {
        receiptController = controller;
    }

    public static UserController getUserController() {
        return userController;
    }

    public static ApplicationController getApplicationController() {
        return applicationController;
    }

    public static EnquiryController getEnquiryController() {
        return enquiryController;
    }

    public static ProjectController getProjectController() {
        return projectController;
    }

    public static ReceiptController getReceiptController() {
        return receiptController;
    }

    public static void logOut(){ clearLoggedUser();}
    public static boolean login(String username, String password)
    {
        User user = userController.getUser(username);
        if (user != null) {
            if(user.verifyPassword(password))
            {
                loggedUser = user;
                return true;
            }
        }
        return false;
    }
}
