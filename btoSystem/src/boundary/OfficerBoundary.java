package boundary;

import controller.ApplicationController;
import controller.ProjectController;
import controller.UserController;

import java.util.Scanner;

public class OfficerBoundary {

    private static UserController userController;
    private static ProjectController projectController;
    private static ApplicationController applicationController;

    public static void setControllers(UserController uController, ProjectController pController, ApplicationController aController)
    {
        userController = uController;
        projectController = pController;
        applicationController = aController;
    }

    public static void welcome(){
        System.out.println("Welcome "); //add applicant name here

        displayDashboard();
        int choice = 0;
        boolean exit = false;
        while(!exit){
            Scanner sc = new Scanner(System.in);
            choice = sc.nextInt();
            switch(choice){
                case 1:
                    exit = true;
                    break;
                case 2:
                    exit = true;
                    break;
                case 3:
                    exit = true;
                    break;
                case 4:
                    exit = true;
                    break;
                case 5:
                    exit = true;
                    break;
                case 6:
                    userController.clearLoggedUser();
                    projectController.clearSelectedProject();
                    System.out.println("Logging Out");
                    exit = true;
                    break;
                default:
                    break;
            }
        }

    }

    public static void displayDashboard() {
        System.out.println("1. View List of Projects");
        System.out.println("2. Register for Project Team");
        System.out.println("3. Select Project To View");
        System.out.println("4. View & Reply Enquiries");
        System.out.println("6. Log Out");
    }

    public static void displayProjectDashboard()
    {

    }

}
