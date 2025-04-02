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

        projectController.setUserController(userController);
        applicationController.setControllers(userController, projectController);

        userController.init();
        projectController.init();
        applicationController.init();

        userController.printUsersContent();
        projectController.printProjectContents();

        MainBoundary.setUserController(userController);
        ApplicantBoundary.setControllers(userController, projectController, applicationController);
        ManagerBoundary.setControllers(userController, projectController, applicationController);

        MainBoundary.welcome();


    }
}