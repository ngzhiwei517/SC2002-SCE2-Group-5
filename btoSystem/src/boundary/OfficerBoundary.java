package boundary;

import controller.*;
import entity.*;
import utils.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
        int choice = -1;
        boolean exit = false;
        while(!exit){
            if(choice == -1 || (choice >= 1 && choice <= 4)){
                displayDashboard();
            }
            Scanner sc = new Scanner(System.in);
            choice = sc.nextInt();
            switch(choice){
                case 1:
                    viewProjects();
                    break;
                case 2:
                    viewManagedProjects();
                    break;
                case 3:
                    viewEnquiries();
                case 4:
                    ApplicantBoundary.welcome();
                    break;
                case 5:
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

    public static void viewProjects()
    {
        //get all the projects first
        List<Project.Status> filter = List.of(Project.Status.VISIBLE, Project.Status.INVISIBLE);
        List<Project> projects =  projectController.getProjects(filter, false);
        for (Project project : projects) {
            Project.print(project, true);
        }
        //get choice
        System.out.println("Select Project (number to select, q to quit)");
        Scanner sc = new Scanner(System.in);
        Map<String, Integer> options = new HashMap<>(); //automatically clamps between 1 (first option) & maximum length of projects.
        options.put("q", -2);
        while(true) {
            String input = sc.nextLine();
            int choice = utils.getRange(options, 1, projects.size(), input);
            if (choice == -2) {
                return;
            } else if (choice == -1) {
                System.out.println("Invalid Option");
            } else {
                ProjectController.selectProject(projects.get(choice - 1));
                break;
            }
        }

        //ask officer to double check if can apply for this project.
        utils.clear();
        projectController.getSelectedProject().printBasicInformation();
        System.out.println("Apply For This Project? (Y/N)");
        String input = sc.nextLine();

        while(true) {
            if (input.equalsIgnoreCase("y")) {
                break;
            } else if (input.equalsIgnoreCase("n")) {
                break;
            } else {
                System.out.println("Invalid Option");
            }
        }

        applicationController.tryApplyOfficer((Officer) UserController.getLoggedUser(), ProjectController.getSelectedProject());
    }

    public static void displayDashboard() {
        System.out.println("Welcome "); //add applicant name here
        System.out.println("1. View & Register Projects");
        System.out.println("2. View Managed Projects");
        System.out.println("3. View & Reply Enquiries"); //should be able to view enquiries of project registered.
        System.out.println("4. Login as Applicant");
        System.out.println("5. Log Out");
    }


    public static void viewManagedProjects()
    {

    }

    public static void viewEnquiries() //send in
    {

        //get all projects under this dude.
        List<Enquiry.Status> filter = List.of(Enquiry.Status.PENDING);
        List<Project> projects =  ProjectController.getProjects((Officer) UserController.getLoggedUser());
        int index = 1;
        for (Project project : projects) {
            System.out.print(index + ": ");
            project.printName();
            System.out.print(" Enquiries: ");
            System.out.print(EnquiryController.getEnquiries(filter, project).size());
            System.out.print("\n");
            index++;
        }

        Scanner sc = new Scanner(System.in);

        //declare option map.
        Map<String, Integer> options = new HashMap<>(); //automatically clamps between 1 (first option) & maximum length of projects.
        options.put("q", -2);

        System.out.print("Select Project (number to select, q to quit): ");
        while(true) {
            String input = sc.nextLine();
            int choice = utils.getRange(options, 1, projects.size(), input);
            if (choice == -2) {
                return;
            } else if (choice == -1) {
                System.out.println("Invalid Option");
            } else {
                ProjectController.selectProject(projects.get(choice - 1));
                break;
            }
        }

        List<Enquiry> enquiries = EnquiryController.getEnquiries(filter, ProjectController.getSelectedProject());
        for (Enquiry enquiry : enquiries) {
            enquiry.print();
        }

        Enquiry selectedEnquiry = null;
        options = new HashMap<>();
        options.put("q", -2);
        options.put("b", -3);
        System.out.print("Select an Enquiry to Respond To (number to select, b or q to quit): ");
        while(true) {
            String input = sc.nextLine();
            int choice = utils.getRange(options, 1, enquiries.size(), input);
            if (choice == -2 || choice == -3) {
                return;
            } else if(choice == -1) {
                System.out.println("Invalid Option");
            }
            else {
                selectedEnquiry = enquiries.get(choice - 1);
                break;
            }
        }


        System.out.print("Response: ");
        String response = sc.nextLine();
        if(response.equalsIgnoreCase("q") || response.equalsIgnoreCase("b") )
        {
            return;
        }
        if(!selectedEnquiry.respond(userController.getLoggedUser(), response))
        {
            System.out.println("Response Failed");
        }






        //ask for selection


        //for(ProjectController.getSelectedProject())
        /*
        List<Enquiry.Status> filter = List.of(Enquiry.Status.PENDING);
        for(Project project : projects) {

            List<Enquiry> enquiries = EnquiryController.getEnquiries(filter, project);
            if(!enquiries.isEmpty()) {
                project.printBasicInformation();
                for (Enquiry enquiry : enquiries) {
                    enquiry.print();
                }
            }
        }*/

        //allow user to select project, then enquiry id


    }

}
