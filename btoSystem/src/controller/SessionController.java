package controller;

import entity.User;

public class SessionController {

    private static UserController userController;
    private static ApplicationController applicationController;
    private static EnquiryController enquiryController;
    private static ProjectController projectController;
    private static ReceiptController receiptController;

    private static User loggedUser = null;
    public static User getLoggedUser()
    {
        return loggedUser;
    }
    public static void clearLoggedUser() { loggedUser = null; }

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
        User user = UserController.getUser(username);
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
