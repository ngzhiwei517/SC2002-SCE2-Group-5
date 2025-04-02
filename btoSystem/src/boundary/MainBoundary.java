package boundary;

import controller.UserController;
import entity.Manager;
import utils.utils;

import java.util.Scanner;

public class MainBoundary {

    private static UserController userController;

    public static void setUserController(UserController controller)
    {
        userController = controller;
    }

    //login will be here, dashboards will be here
    public static void welcome() {
        displayDashboard();

        int choice = 0;
        while (choice != 4) {
            Scanner sc = new Scanner(System.in); //garbage collection should handle this once i close the obejct
            choice = sc.nextInt();
            //sc.close();
            switch (choice) {
                case 1:
                    login();
                    displayDashboard();
                    // go to login ui.
                    break;
                case 2:
                    displayDashboard();
                    // go to register user ui.
                    break;
                case 3:
                    displayDashboard();
                    // go to forgot password ui
                    break;
                case 4:
                    displayDashboard();
                    // break out of loop.
                    break;
                default:
                    System.out.println("Invalid choice");
                    break;
            }

        }
    }

    public static void displayDashboard() //displayed on logout and on login.
    {
        //clear screen here
        utils.clear();
        System.out.println("1. Login");
        System.out.println("3. Register User");
        System.out.println("2. Reset Password");
        System.out.println("4. Exit");
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

            if(userController.login(username, password))
            {
                System.out.println("Login Successful");
                login = true;
                if(userController.getLoggedUser() instanceof Manager)
                {
                    ManagerBoundary.welcome();
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
