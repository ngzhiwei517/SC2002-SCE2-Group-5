package boundary;
import controller.*;

import java.util.Scanner;

public class ApplicantBoundary {

    private static UserController userController;
    private static ProjectController projectController;

    public static void setControllers(UserController uController, ProjectController pController)
    {
        userController = uController;
        projectController = pController;
    }


    public static void welcome(){
        System.out.println("Welcome "); //add applicant name here

        displayDashboard();
        int choice = 0;
        while(choice != 6){
            Scanner sc = new Scanner(System.in);
            choice = sc.nextInt();
            switch(choice){
                case 1:
                    projectController.viewProjectList(userController.getLoggedUser());
                    break;
                case 2:
                    break;
                case 3:
                    break;
                case 4:
                    break;
                case 5:
                    break;
                case 6:
                    userController.clearLoggedUser();
                    System.out.println("Logging Out");
                    break;
                default:
                    break;
            }
        }

    }

    public static void displayDashboard() {
        System.out.println("1. View List of Projects");
        System.out.println("2. Apply for Project");
        System.out.println("3. View Applied Project");
        System.out.println("4. Request for Application Withdrawal");
        System.out.println("5. Enquiry Options");
        System.out.println("6. Log Out");
    }
}
