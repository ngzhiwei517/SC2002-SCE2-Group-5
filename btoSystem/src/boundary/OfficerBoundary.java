package boundary;

import controller.*;
import entity.*;
import utils.utils;

import java.util.*;

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

    public static void displayDashboard() {
        System.out.println("Welcome "); //add applicant name here
        System.out.println("1. View & Register Projects");
        System.out.println("2. View Managed Projects");
        System.out.println("4. Login as Applicant");
        System.out.println("5. Log Out");
    }

    private static void viewBTOListings()
    {
        if(!(UserController.getLoggedUser() instanceof Officer)) //sanity check.
        {
            return;
        }

        Scanner sc = new Scanner(System.in);
        List<Project.Status> filter = new ArrayList<>(List.of(Project.Status.VISIBLE));
        int exitcode = 0;
        while(exitcode == 0) {
            //view only manager's projects.
            List<Project> projects = ((Officer) UserController.getLoggedUser()).getProjects(filter);

            //print out projects.
            int index = 1;
            for(Project project : projects){
                System.out.println(index + ".");
                project.print(true);
                index++;
            }

            Map<String, Integer> options = new HashMap<>(); //allow user to choose project number, or a to view all projects.
            options.put("q", -2);
            options.put("v", -3);
            options.put("a", -4);

            if(filter.contains(Project.Status.INVISIBLE)) {
                if(projects.isEmpty()) {
                    System.out.print("q to quit, v to toggle visibility (ON), a to view ALL projects: ");
                }
                else {
                    System.out.print("Select Project (number to select, q to quit, v to toggle visibility (ON), a to view ALL projects): ");
                }
            }
            else if(projects.isEmpty()) {
                System.out.print("q to quit, v to toggle visibility (OFF), a to view ALL projects: ");
            }
            else {
                System.out.print("Select Project (number to select, q to quit, v to toggle visibility (OFF), a to view ALL projects): ");
            }


            while (true) {
                String input = sc.nextLine();
                int choice = utils.getRange(options, 1, projects.size(), input);
                if (choice == -2) {
                    return;
                } else if (choice == -3) {
                    if (filter.contains(Project.Status.INVISIBLE)) {
                        filter.remove(Project.Status.INVISIBLE);
                    } else {
                        filter.add(Project.Status.INVISIBLE);
                    }
                    break;
                } else if (choice == -1) {
                    System.out.println("Invalid Option");
                } else if (choice == -4) {
                    projects = ProjectController.getProjects(filter, false);
                    for(Project project : projects) {
                        project.print(true);
                    }
                    //wait for next key;
                    utils.waitForKey();
                    break;
                }else {
                    ProjectController.selectProject(projects.get(choice - 1));

                    //register code here.
                    registerForProject(ProjectController.getSelectedProject());
                    break;
                }
            }
        }
    }

    public static void registerForProject(Project project) {
        //sanity check if can register for project to begin with

        Scanner sc = new Scanner(System.in);
        project.printBasicInformation();
        System.out.println("Apply For This Project? (Y/N)");
        String input = sc.nextLine();

        while(true) {
            if (input.equalsIgnoreCase("y")) {
                break;
            } else if (input.equalsIgnoreCase("n")) {
                return;
            } else {
                System.out.println("Invalid Option");
            }
        }

        System.out.println("Officer Application Request Sent.");
    }

    public static void viewProjects()
    {
        //get all the projects first
        List<Project.Status> filter = List.of(Project.Status.VISIBLE, Project.Status.INVISIBLE);
        List<Project> projects =  projectController.getProjects(filter, false);
        for (Project project : projects) {
            project.print(true);
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

        ApplicationController.tryApplyOfficer((Officer) UserController.getLoggedUser(), ProjectController.getSelectedProject());
    }




    public static void viewManagedProjects()
    {
        List<Project.Status> filter = new ArrayList<>(List.of(Project.Status.VISIBLE));
        while(true) {

            List<Project> projects = ((Officer) UserController.getLoggedUser()).getProjects(filter);
            int index = 1;
            for(Project project : projects) {
                System.out.println(index++ + ".");
                project.printBasicInformation();

            }

            if (filter.contains(Project.Status.INVISIBLE)) {
                if (projects.isEmpty()) {
                    System.out.print("q to quit, v to toggle visibility (ON), a to view ALL projects: ");
                } else {
                    System.out.print("Select Project (number to select, q to quit, v to toggle visibility (ON), a to view ALL projects): ");
                }
            } else if (projects.isEmpty()) {
                System.out.print("q to quit, v to toggle visibility (OFF), a to view ALL projects: ");
            } else {
                System.out.print("Select Project (number to select, q to quit, v to toggle visibility (OFF), a to view ALL projects): ");
            }

            Map<String, Integer> options = new HashMap<>(); //allow user to choose project number, or a to view all projects.
            options.put("q", -2);
            options.put("v", -3);

            Scanner sc = new Scanner(System.in);
            while (true) {
                String input = sc.nextLine();
                int choice = utils.getRange(options, 1, projects.size(), input);
                if (choice == -2) {
                    return;
                } else if (choice == -3) {
                    if (filter.contains(Project.Status.INVISIBLE)) {
                        filter.remove(Project.Status.INVISIBLE);
                    } else {
                        filter.add(Project.Status.INVISIBLE);
                    }
                    break;
                } else if (choice == -1) {
                    System.out.println("Invalid Option");
                }else {
                    ProjectController.selectProject(projects.get(choice - 1));
                    //register code here.
                    manageProject(ProjectController.getSelectedProject());
                    break;
                }
            }
        }
    }

    public static void manageProject(Project project)
    {
        int remainingApplications = project.getApplications(List.of(Application.Status.SUCCESSFUL), Application.Type.Applicant).size();
        int remainingEnquiries = project.getEnquiries(List.of(Enquiry.Status.PENDING)).size();
        //get enquiries

        System.out.println("1. View Applications (Remaining: " + remainingApplications + ")" );
        //get applications
        System.out.println("2. View Enquiries (Remaining: " + remainingEnquiries + ")" );

        Scanner sc = new Scanner(System.in);
        while(true)
        {
            String input = sc.nextLine();
            if(input.equalsIgnoreCase("b")) {
                return;
            }
            else if(input.equalsIgnoreCase("1")) {
                manageApplications(project);
                break;
            }
            else if(input.equalsIgnoreCase("2")) {
                manageEnquiries(project);
                break;
            }
        }
    }

    public static void manageApplications(Project project){
        List<Application> applications = project.getApplications(List.of(Application.Status.SUCCESSFUL), Application.Type.Applicant);

        int index = 1;
        for(Application application : applications) {
            System.out.println(index++ + ".");
            application.print();
        }

        Map<String, Integer> options = new HashMap<>();
        options.put("q", -2);
        options.put("v", -3);
        Scanner sc = new Scanner(System.in);
        int choice = -1;
        while(true) {
            choice = utils.getRange(options, 1, applications.size(), sc.nextLine());
            if (choice == -2) {
                return;
            }
            else if (choice == -3) {
                //print out all applications to the project, get back.
            }
            else if (choice == -1) {
                System.out.println("Invalid Option");
            }
            else {
                break;
            }
        }

        Application selectedApplication = applications.get(choice - 1);

        System.out.println("Book for Applicant? (y/n)");
        String input = sc.nextLine();

        while(true) {
            if (input.equalsIgnoreCase("y")) {
                selectedApplication.book();
            }
            else if (input.equalsIgnoreCase("n")) {
                break;
            }
        }
    }

    public static void manageEnquiries(Project project) {
        List<Enquiry> enquiries = project.getEnquiries(List.of(Enquiry.Status.PENDING));

        int index = 1;
        for(Enquiry enquiry : enquiries) {
            System.out.println(index++ + ".");
            enquiry.print();
        }

        Map<String, Integer> options = new HashMap<>();
        options.put("q", -2);
        options.put("v", -3);
        Scanner sc = new Scanner(System.in);
        int choice = -1;
        while(true) {
            choice = utils.getRange(options, 1, enquiries.size(), sc.nextLine());
            if (choice == -2) {
                return;
            }
            else if (choice == -3) {
                //print out all applications to the project, get back.
            }
            else if (choice == -1) {
                System.out.println("Invalid Option");
            }
            else {
                break;
            }
        }

        Enquiry selectedEnquiry = enquiries.get(choice - 1);

        System.out.println("Reply (b to back): ");
        String response = sc.nextLine();

        while(true) {
            if (response.equalsIgnoreCase("b")) {
               break;
            }
            else {
                selectedEnquiry.respond(UserController.getLoggedUser(), response);
                break;
            }
        }
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
    }

}
