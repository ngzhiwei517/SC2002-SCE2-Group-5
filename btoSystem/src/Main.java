import boundary.ApplicantBoundary;
import boundary.MainBoundary;
import boundary.ManagerBoundary;
import boundary.OfficerBoundary;

import entity.*;
import controller.*;
import dao.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {


    public static void main(String[] args) {


        UserController userController = new UserController();
        ProjectController projectController = new ProjectController();
        ApplicationController applicationController = new ApplicationController();
        EnquiryController enquiryController = new EnquiryController();
        ReceiptController receiptController = new ReceiptController();

        SessionController.setUserController(userController);
        SessionController.setApplicationController(applicationController);
        SessionController.setEnquiryController(enquiryController);
        SessionController.setReceiptController(receiptController);
        SessionController.setProjectController(projectController);

        UserDAO userDAO = new UserCSVDAO();
        ProjectDAO projectDAO = new ProjectCSVDAO();
        EnquiryDAO enquiryDAO = new EnquiryCSVDAO();
        ApplicationDAO applicationDAO = new ApplicationCSVDAO();
        AuditDAO auditDAO = new AuditCSVDAO();
        ReceiptDAO receiptDAO = new ReceiptCSVDAO();

        SessionController.setUserDAO(userDAO);
        SessionController.setProjectDAO(projectDAO);
        SessionController.setEnquiryDAO(enquiryDAO);
        SessionController.setApplicationDAO(applicationDAO);
        SessionController.setAuditDAO(auditDAO);
        SessionController.setReceiptDAO(receiptDAO);

        MainBoundary.init();
        ApplicantBoundary.init();
        OfficerBoundary.init();
        ManagerBoundary.init();

        userController.init();
        projectController.init();
        applicationController.init();
        enquiryController.init();
        receiptController.init();

        boolean exit = true;
        while (exit){
            exit = MainBoundary.welcome();
        }

        applicationController.exit();
        projectController.exit();
        enquiryController.exit();
        userController.exit();
        receiptController.exit();

    }
}