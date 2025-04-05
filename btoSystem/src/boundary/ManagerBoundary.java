package boundary;

import controller.*;
import entity.*;
import utils.utils;

import java.util.*;

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

        Map<String, Integer> options = new HashMap<>();
        options.put("q", -2);

        Scanner sc = new Scanner(System.in);
        System.out.print("Select Project (number to select, q to quit): ");
        while(true)
        {
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

        //implement UD functions here
        //display UD options here/
    }

    private static void optionsUpdateDelete()
    {
        System.out.println("1. Update Selected Project");
        System.out.println("2. Delete Selected Project");

        //update code here
        //update options
        // change name?
        // change location?
        // add flat?
        // remove flat?
        // edit flat?
        // -- within flat menu
        // update room type
        // update room value

        //delete code here

        //confirm delete?
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
        //get all applications where applications are pending
        List<Application> applications = applicationController.getApplications(List.of(Application.Status.PENDING), Application.Type.Applicant);
        Map<Project, List<Application>> map = new HashMap<>();

        //add all filtered applications to a map
        for(Application app :applications) {
            if(map.containsKey(app.getProject())) {
                map.get(app.getProject()).add(app);
            }
            else {
                map.put(app.getProject(), new ArrayList<>());
                map.get(app.getProject()).add(app);
            }
        }

        int index = 1;
        List<Project> projects =  new ArrayList<>();
        //assign index to each project item.
        for(Project key : map.keySet() ) {
            System.out.println(index + ": ");
            projects.add(key);
            key.printBasicInformation();
            System.out.println("Pending Applications: " + map.get(key).size());
            index++;
        }

        //allow for selection here
        Map<String, Integer> options = new HashMap<>();
        options.put("q", -2);

        Scanner sc = new Scanner(System.in);
        System.out.print("Select Project (number to select, q to quit): ");
        while(true)
        {
            String input = sc.nextLine();
            int choice = utils.getRange(options, 1, projects.size(), input);
            if (choice == -2) {
                return;
            } else if (choice == -1) {
                System.out.println("Invalid Option");
            } else {
                ProjectController.selectProject(projects.get(choice - 1)); //saves selected project inside the project controller.
                break;
            }
        }

        //after selecting project, display all project applications.
        List<Application> project_applications = map.get(ProjectController.getSelectedProject());

        index = 1;
        for(Application app : project_applications) {
            System.out.println(index + ": ");
            app.print();
            index++;
        }

        Application selected_app = null;
        //allow user to select application here
        System.out.print("Select application (number to select, q to quit): ");
        while(true)
        {
            String input = sc.nextLine();
            int choice = utils.getRange(options, 1, applications.size(), input);
            if (choice == -2) {
                return;
            } else if (choice == -1) {
                System.out.println("Invalid Option");
            } else {
                selected_app = project_applications.get(choice-1); //saves selected project inside the project controller.
                break;
            }
        }

        //let manager approve, reject or back/quit here
        int status = -1;
        if(selected_app != null) { //null case should never happen, there is just a sanity check here.

            selected_app.print(); //print the application details itself

            options.put("a", 1);
            options.put("r", 2);

            //request user if want to approve or reject or back
            while(true) {
                String input = sc.nextLine();
                int choice = utils.getRange(options, 0, 0, input);
                if (choice == -2) {
                    return;
                } else if (choice == -1) {
                    System.out.println("Invalid Option");
                } else {
                    status = choice;
                    break;
                }
            }
        }

        if(status != -1) //another sanity check here
        {
            switch(status) {
                case 1:{
                    selected_app.approve();
                }
                break;
                case 2: {
                    selected_app.reject();
                }
                break;
                default: //default case should never happen as its asserted beforehand.
                    break;
            }

        }




    }

    private static void viewApplicantWithdrawal()
    {
        List<Application.Status> filter = List.of(Application.Status.REQUESTED_WITHDRAW);
        List<Application> applications = applicationController.getApplications(filter, Application.Type.Applicant);
        for(Application app : applications) {
            app.print();
        }

        //copy whatever from on top down to here.
    }

    private static void viewOfficerRegistrations()
    {
        List<Application.Status> filter = List.of(Application.Status.PENDING, Application.Status.APPROVED, Application.Status.REJECTED);
        List<Application> applications = applicationController.getApplications(filter, Application.Type.Officer);
        for(Application app : applications) {
            app.print();
        }

        //copy whatever from on top down to here.
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
