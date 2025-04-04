package boundary;

import controller.*;
import entity.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ManagerBoundary {

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
                    viewBTOListings();
                    waitForContinue(true);
                    exit = true;
                    break;
                case 2:
                    createBTOListing();
                    waitForContinue(true);
                    exit = true;
                    break;
                case 3:
                    viewOfficerRegistrations();
                    waitForContinue(true);
                    exit = true;
                    break;
                case 4:
                    viewApplicantApplications();
                    waitForContinue(true);
                    exit = true;
                    break;
                case 5:
                    viewApplicantWithdrawal();
                    waitForContinue(true);
                    exit = true;
                    break;
                case 6:
                    exit = true;
                    waitForContinue(true);
                    break;
                case 7:
                    exit = true;
                    waitForContinue(true);
                    break;
                case 8:
                    exit = true;
                    System.out.println("Logging Out");
                    break;
                default:
                    break;
            }
        }
        //log out of everything.
        userController.clearLoggedUser();
        projectController.clearSelectedProject();
    }

    public static void displayDashboard()
    {
        System.out.println("1. View BTO Project Listings"); //contains, RUD functions. should be able to filter visibility
        System.out.println("2. Create BTO Project Listing");
        System.out.println("3. View HDB Officer Registration");
        System.out.println("4. View Applicant Application");
        System.out.println("5. View Applicant Withdrawal Request");
        System.out.println("6. Generate Report");
        System.out.println("7. Enquiry Menu");
        System.out.println("8. Log Out");
    }

    private static void viewBTOListings()
    {
        List<Project.Status> filter = List.of(Project.Status.VISIBLE);
        List<Project> projects =  projectController.getProjects(filter, false);
        for (Project project : projects) {
            Project.print(project, true);
        }

        //implement RUD functions here
    }

    private static void createBTOListing()
    {
        Scanner sc = new Scanner(System.in);
        //request projname
        System.out.println("Project Name: ");
        String projectName = sc.nextLine();
        //request neighbourhood
        System.out.println("Neighbourhood: ");
        String neighbourhood = sc.nextLine();
        //rqeuest openingdate
        System.out.println("Application Opening Date (MM/DD/YYYY): ");
        String applicationOpeningDate = sc.nextLine();
        //request closingdate
        System.out.println("Application Closing Date (MM/DD/YYYY): ");
        String applicationClosingDate = sc.nextLine();
        //request officerslots
        System.out.println("Officer Slots: ");
        int officerSlots = sc.nextInt();
        sc.nextLine();
        //request type1 details
        //skip line

        List<Flat> flats = new ArrayList<Flat>();
        while(true) {
            System.out.println("Flat Type: ");
            String flatType = sc.nextLine();
            System.out.println("Flat Price: ");
            int flatPrice = sc.nextInt();
            sc.nextLine();
            System.out.println("No of Units: ");
            int units = sc.nextInt();
            sc.nextLine();
            Flat flat = new Flat(flatType, flatPrice, units);
            //request type2 details
            flats.add(flat);

            System.out.println("Enter another flat? (\"N\" to exit): ");
            String choice = sc.nextLine();
            if(choice.equalsIgnoreCase("n"))
                break;
        }

        //manager will be passed in as user.loggeduser.
        Project project = projectController.addProject(projectName,neighbourhood, applicationOpeningDate, applicationClosingDate, (Manager) userController.getLoggedUser(), officerSlots);
        for(Flat flat : flats) {
            project.addFlat(flat);
        }


        System.out.println("enter any key to continue");
        sc.nextLine();
        welcome();
    }

    private static void viewApplicantApplications()
    {
        List<Application.Status> filter = List.of(Application.Status.PENDING, Application.Status.SUCCESSFUL, Application.Status.BOOKED);
        List<Application> applications = applicationController.getApplications(filter, Application.Type.Applicant);
        for(Application app : applications) {
            app.print();
        }

        //add editing here.
    }

    private static void viewApplicantWithdrawal()
    {
        List<Application.Status> filter = List.of(Application.Status.REQUESTED_WITHDRAW);
        List<Application> applications = applicationController.getApplications(filter, Application.Type.Applicant);
        for(Application app : applications) {
            app.print();
        }
    }

    private static void viewOfficerRegistrations()
    {
        List<Application.Status> filter = List.of(Application.Status.PENDING, Application.Status.APPROVED, Application.Status.REJECTED);
        List<Application> applications = applicationController.getApplications(filter, Application.Type.Officer);
        for(Application app : applications) {
            app.print();
        }
    }

    private static void waitForContinue(boolean refresh)
    {
        System.out.println("Press any key to continue");
        Scanner sc = new Scanner(System.in);
        sc.nextLine();
        if(refresh)
        {
            welcome();
        }
    }
}
