import boundary.ApplicantBoundary;
import boundary.MainBoundary;
import boundary.ManagerBoundary;
import entity.*;
import controller.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {


    public static void main(String[] args) {
        UserController userController = new UserController();
        ProjectController projectController = new ProjectController();
        ApplicationController applicationController = new ApplicationController();
        EnquiryController enquiryController = new EnquiryController();

        projectController.setUserController(userController);
        applicationController.setControllers(userController, projectController);

        MainBoundary.setUserController(userController);
        ApplicantBoundary.setControllers(userController, projectController, applicationController, enquiryController);
        enquiryController.setControllers(userController, projectController);
        ManagerBoundary.setControllers(userController, projectController, applicationController);

        userController.init();
        projectController.init();
        applicationController.init();
        enquiryController.init();

        boolean exit = true;
        while (exit){
            exit = MainBoundary.welcome();
        }

        applicationController.exit();
        projectController.exit();
        enquiryController.exit();

    }
}