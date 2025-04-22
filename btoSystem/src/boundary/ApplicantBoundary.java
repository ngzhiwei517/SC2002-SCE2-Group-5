package boundary;
import controller.*;
import entity.*;
import utils.utils;

import java.util.*;

public class ApplicantBoundary {

    private static UserController userController;
    private static ProjectController projectController;
    private static ApplicationController applicationController;
    private static EnquiryController enquiryController;

    public static void setControllers(UserController uController, ProjectController pController, ApplicationController aController, EnquiryController eController)
    {
        userController = uController;
        projectController = pController;
        applicationController = aController;
        enquiryController = eController;
    }

    public static void welcome(){
        int choice = -1;
        boolean exit = false;
        while(!exit){
            if(choice == -1 || (choice >= 1 && choice <= 5)){
                displayDashboard();
            }
            Scanner sc = new Scanner(System.in);
            choice = sc.nextInt();
            switch(choice){
                case 1:
                    utils.clear();
                    viewProjectOptions();
                    break;
                case 2:
                    utils.clear();
                    viewApplication();
                    break;
                case 3:
                    utils.clear();
                    viewAllEnquiries();
                    break;
                case 4:
                    utils.clear();
                    showReceipt();
                    break;
                case 5:
                    utils.clear();
                    if(!(SessionController.getLoggedUser() instanceof Officer))
                    {
                        SessionController.logOut();
                        ProjectController.clearSelectedProject();
                        System.out.println("Logging Out");
                    }
                    exit = true;
                    break;
                default:
                    break;
            }
        }

    }

    public static void showReceipt()
    {
        if(!(SessionController.getLoggedUser() instanceof Applicant))
        {
            System.out.println("Something Went Wrong, Session User Data is not of Class Applicant");
            return;
        }
        List<Receipt> receipts = ReceiptController.getReceipt((Applicant) SessionController.getLoggedUser());

        if(receipts.isEmpty())
        {
            System.out.println("No receipts found");
            return;
        }

        for(Receipt receipt : receipts){
            receipt.printReceipt();
        }

    }

    public static void displayDashboard() {
        System.out.println("Welcome "); //add applicant name here
        System.out.println("1. View & Apply for Projects");
        System.out.println("2. View All Applications");
        System.out.println("3. View All Enquiries");
        System.out.println("4. Show Receipts");

        if(SessionController.getLoggedUser() instanceof Officer)
        {
            System.out.println("5. Return to Officer View");
        }
        else {
            System.out.println("5. Log Out");
        }
    }

    private static void viewProjectOptions()
    {
        //boolean early_exit = false;
        int state = 0;
        Flat selectedFlat = null;
        //get project list, based on variables & visibility
        List<Project> projectList = projectController.getEligibleProjects(SessionController.getLoggedUser());
        if(projectList.isEmpty())
        {
            System.out.println("You are not Eligible any ongoing projects");
            return;
        }
        boolean exit = false;
        while(!exit) {
            switch (state) {
                case 0: {
                    int index = 1;
                    for (Project p : projectList) {
                        System.out.print(index + ". ");
                        p.printBasicInformation();
                        System.out.println("------------------------------");
                        index++;
                    }
                    state = 1;
                }
                break;
                case 1: {
                    //give user option to choose between objects, using numbering 1-x, b to go back, q to exit.
                    Scanner sc = new Scanner(System.in);
                    String input = sc.nextLine();
                    if (!utils.isNumeric(input)) {
                        if (input.equals("b")) {
                            exit = true;
                        } else if (input.equals("q")) {
                            exit = true;
                        }
                        else {
                            System.out.println("Invalid Input.");
                        }
                    }
                    else {
                        if(Integer.parseInt(input) > 0 && Integer.parseInt(input) <= projectList.size())
                        {
                            projectController.selectProject(projectList.get(Integer.parseInt(input)-1));
                            utils.clear();
                            state = 2;
                        }
                        else
                        {
                            System.out.println("Invalid Input.");
                        }
                    }
                }
                break;
                //after entering project allow users to view all flat options
                case 2: {
                    ProjectController.getSelectedProject().printBasicInformation();
                    List<Flat> flats = ProjectController.getSelectedProject().getEligibleFlats((Applicant) SessionController.getLoggedUser());
                    if(!flats.isEmpty()) {
                        System.out.println("Flat Options: (number to select, b to back, q to exit, e to write enquiry)");
                        int index = 1;
                        for (Flat f : flats) {
                            System.out.print(index + ". ");
                            f.print();
                            index++;
                        }
                        state = 3;
                    }
                    else {
                        System.out.println("You are not eligible for any flats in this project."); //this should never show, but acts as fallback mechanic
                    }
                }
                case 3: {
                    if (((Applicant) SessionController.getLoggedUser()).canApply(ProjectController.getSelectedProject())) {
                        System.out.print("Input: number to select, b to back, q to exit, e to write enquiry");
                        Scanner sc = new Scanner(System.in);
                        String input = sc.nextLine();
                        if (!utils.isNumeric(input)) {
                            if (input.equals("b")) {
                                state = 0;
                            } else if (input.equals("q")) {
                                exit = true;
                            }
                            else if (input.equals("e")) {
                                state = 5;
                            } else {
                                System.out.println("Invalid Input.");
                            }
                        } else {
                            if (Integer.parseInt(input) > 0 && Integer.parseInt(input) <= projectList.size()) //SUCCESS CONDITION
                            {
                                selectedFlat = ProjectController.getSelectedProject().getEligibleFlats( (Applicant) SessionController.getLoggedUser()).get(Integer.parseInt(input) - 1);
                                state = 4;
                                utils.clear();
                            } else {
                                System.out.println("Invalid Input.");
                            }
                        }
                    }
                    else {
                        System.out.println("You already have a flat or pending application. \n b: view other projects \n q: back to main menu");
                        Scanner sc = new Scanner(System.in);
                        while(true) {
                            String input = sc.nextLine();
                            if (input.equals("b")) {
                                state = 0;
                                break;
                            } else if (input.equals("q")) {
                                exit = true;
                                break;
                            } else {
                                System.out.println("Invalid Input.");
                            }
                        }
                    }
                }
                break;
                case 4: {
                    projectController.getSelectedProject().printBasicInformation();
                    selectedFlat.print();
                    System.out.println("Apply for Project? (Y/N)");
                    Scanner sc = new Scanner(System.in);
                    while(true) {
                        String input = sc.nextLine();
                        if(input.equalsIgnoreCase("y"))
                        {
                            applicationController.tryApply(SessionController.getLoggedUser(), selectedFlat);
                            exit = true;
                            break;
                        }
                        else if(input.equalsIgnoreCase("n")) {
                            System.out.println("exiting");
                            state = 2;
                            break;
                        }
                        else{
                            System.out.println("Invalid Input.");
                        }
                    }
                }
                break;
                case 5: //enquiry state
                    System.out.print("Title: ");
                    Scanner sc = new Scanner(System.in);
                    String title = sc.nextLine();
                    if(title.equals("b") || title.equals("q")) {
                        exit = true;
                        break;
                    }
                    System.out.println("Text Body: ");
                    String body = sc.nextLine();
                    if(body.equals("b") || body.equals("q")) {
                        exit = true;
                        break;
                    }
                    enquiryController.newEnquiry((Applicant) SessionController.getLoggedUser(), ProjectController.getSelectedProject(), title, body);
                    exit = true;
                break;
            }
        }
    }


    private static void viewApplication()
    {
        List<Application> applications = ((Applicant) SessionController.getLoggedUser()).getApplications(List.of(Application.Status.PENDING, Application.Status.SUCCESSFUL, Application.Status.BOOKED, Application.Status.REQUESTED_WITHDRAW, Application.Status.UNSUCCESSFUL, Application.Status.REQUESTED_WITHDRAW_BOOKED, Application.Status.WITHDRAWN), Application.Type.Applicant);

        if(!applications.isEmpty()) {
            int count = 1;
            for(Application app : applications) {
                System.out.println(count + ". " + app.getProject().getProjectName() + " | " + app.getFlat().getType() + " | " + app.getStatus());
                count++;
            }
            Scanner sc = new Scanner(System.in);
            System.out.println("Withdraw Application (number to withdraw, b to back): ");


            Application selectedApplication;
            int choice;
            Map<String, Integer> options = new HashMap<>();
            options.put("b", -2);
            while (true) {
                String input = sc.nextLine();
                choice = utils.getRange(options, 1, applications.size(), input);
                if (choice == -2) {
                    return; //go back to options.
                } else if (choice == -1) {
                    System.out.println("Invalid Option");
                } else {

                    break;
                }
            }

            selectedApplication = applications.get(choice-1);

            selectedApplication.print();
            System.out.println("Confirm Delete? (y/n)");
            while(true){
                String input = sc.nextLine();
                if(input.equals("y")) {
                    applicationController.tryWithdrawApplication(selectedApplication);
                    break;
                }
                else if(input.equals("n")) {
                    break;
                }
                else{
                    System.out.println("Invalid Input.");
                }
            }

        }
        else {
            System.out.println("You have no pending applications");
        }
    }

    private static void viewAllEnquiries() {
        int index = 1;
        List<Enquiry> enquiries = ((Applicant) SessionController.getLoggedUser()).getEnquiries();
        if (enquiries.isEmpty()) {
            System.out.println("No Enquiries Found");
            return;
        }

        for (Enquiry e : enquiries) {
            System.out.print(index + ". ");
            e.print();
        }

        Scanner sc = new Scanner(System.in);
        int choice;
        Map<String, Integer> options = new HashMap<>();
        options.put("b", -2);
        while (true) {
            String input = sc.nextLine();
            choice = utils.getRange(options, 1, enquiries.size(), input);
            if (choice == -2) {
                return; //go back to options.
            } else if (choice == -1) {
                System.out.println("Invalid Option");
            } else {
                break;
            }
        }

        Enquiry selectedEnquiry = enquiries.get(choice-1);


        System.out.print("1. Edit Enquiry\n2. Delete Enquiry\n");
        System.out.print("Input: ");
        while(true)
        {
            String input = sc.nextLine();
            if(input.equalsIgnoreCase("1")) {
                editEnquiry(selectedEnquiry);
                break;
            }else if(input.equalsIgnoreCase("2")) {
                deleteEnquiry(selectedEnquiry);
                break;
            }
            else if(input.equalsIgnoreCase("b")) {
                return;
            }
            else{
                System.out.println("Invalid Input.");
            }
        }
    }

    private static void editEnquiry(Enquiry enquiry) {
        while(true) {
            System.out.print("1. Edit Title");
            System.out.print("2. Edit Body");
            System.out.print("Input (b to back): ");

            Scanner sc = new Scanner(System.in);
            while (true) {
                String input = sc.nextLine();
                switch (input) {
                    case "1":
                        System.out.print("Previous Title: " + enquiry.getTitle());
                        System.out.print("New Title: ");
                        enquiry.setTitle(sc.nextLine());
                        break;
                    case "2":
                        System.out.print("Previous Body: " + enquiry.getText());
                        System.out.print("New Body: ");
                        enquiry.setText(sc.nextLine());
                        break;
                    case "b":
                        return;
                    default:
                        System.out.println("Invalid Input.");
                        break;
                }
            }
        }
    }

    private static void deleteEnquiry(Enquiry enquiry){
        System.out.println("Confirm Delete Enquiry? (y/n)");
        Scanner sc = new Scanner(System.in);
        while(true) {
            String input = sc.nextLine();
            if (input.equalsIgnoreCase("y")) {
                enquiry.delete();
                return;
            } else if (input.equalsIgnoreCase("n")) {
                return;
            } else {
                System.out.println("Invalid Input.");
            }
        }
    }
}
