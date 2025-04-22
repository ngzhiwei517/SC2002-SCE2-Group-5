package boundary;

import controller.SessionController;
import controller.UserController;
import entity.*;
import utils.utils;

import java.util.Scanner;

public class MainBoundary {

    private static UserController userController;

    public static void setUserController(UserController controller)
    {
        userController = controller;
    }

    //login will be here, dashboards will be here
    public static boolean welcome() {
        displayDashboard();

        String choice = "0";
        while (!choice.equalsIgnoreCase("3")) {
            Scanner sc = new Scanner(System.in); //garbage collection should handle this once i close the obejct
            choice = sc.nextLine();
            //sc.close();
            switch (choice) {
                case "1":
                    login();
                    displayDashboard();
                    // go to login ui.
                    break;
                case "2":
                    resetPassword();
                    displayDashboard();
                    break;
                case "3":
                    break;
                default:
                    System.out.println("Invalid choice");
                    break;
            }
        }
        return false;
    }

    public static void displayDashboard() //displayed on logout and on login.
    {
        //clear screen here
        utils.clear();
        System.out.println("1. Login");
        System.out.println("2. Reset Password");
        System.out.println("3. Exit");
    }

    public static void resetPassword()
    {
        System.out.println("Resetting Password");
        Scanner sc = new Scanner(System.in);
        System.out.println("Username: ");
        String username = sc.nextLine();
        System.out.println("Current Password: ");
        String password = sc.nextLine();
        System.out.println("New Password: ");
        String newpassword = sc.nextLine();

        if(userController.resetPassword(username, password, newpassword)) {
            System.out.println("Password Reset Successful");
        }
    }

    public static void login()
    {
        boolean login = false;
        while(!login) {
            Scanner sc = new Scanner(System.in);
            System.out.println("Username: ");
            String username = sc.nextLine();
            System.out.println("Password: ");
            String password = sc.nextLine();

            //do assertion check.
            //assert with this pattern ^[TS]\d{7}[A-Z]$

            if(userController.login(username, password))
            {
                System.out.println("Login Successful");
                login = true;
                if(SessionController.getLoggedUser() instanceof Manager)
                {
                    ManagerBoundary.welcome();
                }
                else if(SessionController.getLoggedUser() instanceof Officer) {
                    OfficerBoundary.welcome();
                }
                else {
                    ApplicantBoundary.welcome();
                }
            }
            else {
                System.out.println("Login Failed");
            }
            //if login successful, set login to true for this instance of welcome.
        }
        //try login here
    }

}
