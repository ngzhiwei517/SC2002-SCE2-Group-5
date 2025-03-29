import boundary.ApplicantBoundary;
import boundary.MainBoundary;
import entity.*;
import controller.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {


    public static void main(String[] args) {
        UserController userController = new UserController();
        ProjectController projectController = new ProjectController();
        projectController.setUserController(userController);

        userController.init();
        projectController.init();

        userController.printUsersContent();
        projectController.printProjectContents();

        MainBoundary.setUserController(userController);
        ApplicantBoundary.setControllers(userController, projectController);

        MainBoundary.welcome();

        //temp login code

/*
        Scanner sc = new Scanner(System.in);
        System.out.println("Login ID: ");
        String loginID = sc.nextLine();
        System.out.println("Password: ");
        String password = sc.nextLine();

        if(userController.login(loginID, password))
        {
            System.out.println("Login Successful");
        }
        else
        {
            System.out.println("Login Failed");
        }

 */

        //projectController.viewProjectList(userController.getLoggedUser());


        //display dashboard for respective
    }

    //load all csv files into pseudo database first.

    //read all users into a list of users, downcasted into list of users.

    //read all applications, read all

    //on login, greeted with type of user, and options.
}