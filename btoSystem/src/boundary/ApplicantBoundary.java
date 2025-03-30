package boundary;
import controller.*;
import entity.*;

import java.util.List;
import java.util.Scanner;

public class ApplicantBoundary {

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
                    viewListOfProjects();
                    exit = true;
                    break;
                case 2:
                    applyForProject();
                    exit = true;
                    break;
                case 3:
                    viewAppliedProjects();
                    exit = true;
                    break;
                case 4:
                    withdrawApplication();
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
        System.out.println("2. Apply for Project");
        System.out.println("3. View Applied Project");
        System.out.println("4. Request for Application Withdrawal");
        System.out.println("5. Enquiry Options");
        System.out.println("6. Log Out");
    }

    private static void viewListOfProjects()
    {
        List<Project> projectList = projectController.getEligibleProjects(userController.getLoggedUser());
        int index = 0;
        for(Project p : projectList)
        {
            System.out.println(String.valueOf(index) + ". " + p.getProjectName());
            index++;
        }
    }

    private static void applyForProject()
    {
        //get list of eligible projects, in format List<Projects>
        List<Project> projectList = projectController.getEligibleProjects(userController.getLoggedUser());
        List<Flat> flatList = null;
        int index = 0;
        for(Project p : projectList)
        {
            System.out.println(String.valueOf(index) + ". " + p.getProjectName());
            index++;
        }

        int state = 0;
        while(state != 3) {
            switch(state) {
            case 0: {
                Scanner sc = new Scanner(System.in);
                int choice = 0;
                System.out.println("Select Project: "); //TODO: use b to back
                choice = sc.nextInt();         //allow user to select a project using the list,
                projectController.selectProject(projectList.get(choice)); //value is stored inside selectedproject within projectcontroller
                state = 1;
            }
            break;
            case 1: {
                flatList = projectController.getSelectedProject().getFlats();
                for (Flat f : flatList) {
                    //TODO: assert flatlist here
                    System.out.println(f.getType() + ", " + f.getUnits() + ", " + f.getPrice());
                }
                System.out.println("Select Flat: "); //use b to back
                state = 2;
            }
                break;

                //select flat based on eligibility
            case 2: {

                Scanner sc = new Scanner(System.in);
                int choice = 0;
                choice = sc.nextInt(); //TODO: check whether b is inserted. if inserted, go back to state 0;
                if(choice < flatList.size()) {
                    applicationController.tryApply(userController.getLoggedUser(), projectController.getSelectedProject(), flatList.get(choice));
                    applicationController.displayAllApplications();
                    state = 3;
                }
            }
            break;
            default:{


            }
            break;
            }
            System.out.println("newloop"); //use b to back
        }


        //projectController.applyProject();
        //when user confirms selection, use tryApply, where within that it will assert all cnds



    }

    private static void viewAppliedProjects()
    {
        List<Application> appList = applicationController.getUserApplications(userController.getLoggedUser());
        for(Application app : appList) {
            System.out.println(app.getApplicant() + " | " + app.getProject() + " | " + app.getFlat() + " | " + app.getStatus());
        }

    }

    private static void withdrawApplication()
    {
        List<Application> appList = applicationController.getUserApplications(userController.getLoggedUser());
        int count = 0;
        for(Application app : appList) {
            System.out.println(count + ". " + app.getApplicant() + " | " + app.getProject() + " | " + app.getFlat() + " | " + app.getStatus());
            count++;
        }

        boolean exit = false;
        while(!exit) {
            Scanner sc = new Scanner(System.in);
            System.out.println("Selection: ");
            int choice = sc.nextInt();

            if (choice < appList.size()) {
                applicationController.tryWithdrawApplication(appList.get(choice));
                System.out.println("Withdraw Successful");
                exit = true;
            }
            else{
                System.out.println("invalid option");
            }
        }
    }

    private static void enquiryOptions()
    {

    }


}
