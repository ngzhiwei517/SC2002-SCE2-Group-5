package boundary;

import controller.*;
import entity.*;
import utils.utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
                    exit = true;
                    break;
                case 5:
                    exit = true;
                    break;
                case 6:
                    viewApplicantWithdrawal();
                    waitForContinue(true);
                    exit = true;
                    break;
                case 7:
                    exit = true;
                    waitForContinue(true);
                    break;
                case 8:
                    viewAllEnquiries();
                    exit = true;
                    waitForContinue(true);
                    break;
                case 9:
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
        System.out.println("2. View My Projects");
        System.out.println("3. Create BTO Project Listing");
        System.out.println("4. View HDB Officer Registration");
        System.out.println("5. View Applicant Application");
        System.out.println("6. View Applicant Withdrawal Request");
        System.out.println("7. Generate Report");
        System.out.println("8. Enquiry Menu");
        System.out.println("9. Log Out");
    }

    private static void viewBTOListings()
    {
        List<Project.Status> filter = List.of(Project.Status.VISIBLE);
        int exitcode = 0;
        while(exitcode == 0) {
            List<Project> projects = ProjectController.getProjects(filter, false);

            if(projects.isEmpty()) {
                System.out.println("No projects found, q to quit, h to view hidden projects");
                Scanner sc = new Scanner(System.in);
                while(true) {
                    String input = sc.nextLine();
                    if (input.equalsIgnoreCase("h")) {
                        filter = List.of(Project.Status.VISIBLE, Project.Status.INVISIBLE);
                        projects = ProjectController.getProjects(filter, false);
                        break;
                    } else if (input.equalsIgnoreCase("q")) {
                        return;
                    } else {
                        System.out.println("Invalid Input");
                    }
                }
            }

            for (Project project : projects) {
                Project.print(project, true);
            }

            Map<String, Integer> options = new HashMap<>();
            options.put("q", -2);
            options.put("h", -3);
            Scanner sc = new Scanner(System.in);
            if(filter.contains(Project.Status.INVISIBLE)) {
                System.out.print("Select Project (number to select, q to quit): ");
            }
            else {
                System.out.print("Select Project (number to select, q to quit, h to view hidden projects): ");
            }
            while (true) {
                String input = sc.nextLine();
                int choice = utils.getRange(options, 1, projects.size(), input);
                if (choice == -2) {
                    return;
                } else if (choice == -3) {
                    filter = List.of(Project.Status.VISIBLE, Project.Status.INVISIBLE);
                    break;
                } else if (choice == -1) {
                    System.out.println("Invalid Option");
                } else {
                    ProjectController.selectProject(projects.get(choice - 1));
                    exitcode = optionsUpdateDelete();
                    break;
                }
            }
        }
    }

    private static int optionsUpdateDelete()
    {

        int exitcode = 1;
        while(exitcode == 1) {
            System.out.println("1. Update Selected Project");
            System.out.println("2. Delete Selected Project");
            Map<String, Integer> options = new HashMap<>();
            Scanner sc = new Scanner(System.in);
            options.put("b", -2);
            options.put("q", -3);
            while (true) {
                String input = sc.nextLine();
                int choice = utils.getRange(options, 1, 2, input);
                if (choice == -2) {
                    return 0;
                } else if (choice == 1) {
                    exitcode = updateListing(ProjectController.getSelectedProject());
                    break;
                } else if (choice == 2) {
                    exitcode = deleteListing(ProjectController.getSelectedProject());
                    break;
                } else {
                    System.out.println("Invalid Option");
                }
            }
        }
        return exitcode; //go back to main menu
    }

    private static int updateListing(Project project){
        Project.print(project, true);

        int exitcode = 2;

        while(exitcode == 2) {
            System.out.println("1. Edit Project Name");
            System.out.println("2. Change Neighbourhood");
            System.out.println("3. Change Opening Date");
            System.out.println("4. Change Closing Date");
            System.out.println("5. Toggle Visibility"); // TODO: add current visbility
            System.out.println("6. Flat Options");
            System.out.println("7. Officer Options");

            System.out.println("Input (b to back): ");

            Scanner sc = new Scanner(System.in);
            Map<String, Integer> options = new HashMap<>();
            options.put("b", -2);
            int choice = -1;
            while (true) {
                String input = sc.nextLine();
                choice = utils.getRange(options, 1, 7, input);
                if (choice == -2) {
                    return 1;
                } else if (choice == -1) {
                    System.out.println("Invalid Option");
                } else {
                    break;
                }
            }

            switch (choice) {
                case 1: {
                    System.out.println("Previous Name: " + project.getProjectName());
                    System.out.println("New Name: ");
                    String input = sc.nextLine();
                    //data validation here
                    project.setProjectName(input);
                    exitcode = 1;
                }
                break;
                case 2: {
                    System.out.println("Previous Neighbourhood: " + project.getNeighborhood());
                    System.out.println("New Neighbourhood: ");
                    String input = sc.nextLine();
                    //data validation here
                    project.setNeighborhood(input);
                    exitcode = 1;

                }
                break;
                case 3: {
                    System.out.println("Previous Opening Date: " + project.getOpeningDate().toString());
                    System.out.println("New Opening Date (M/D/YYYY): ");
                    String input = sc.nextLine();
                    //data validation here
                    LocalDate date = LocalDate.parse(input, DateTimeFormatter.ofPattern("M/d/yyyy"));
                    project.setOpeningDate(date);
                    exitcode = 1;
                }
                break;
                case 4: {
                    System.out.println("Previous Closing Date: " + project.getClosingDate().toString());
                    System.out.println("New Closing Date (M/D/YYYY): ");
                    String input = sc.nextLine();
                    //data validation here
                    LocalDate date = LocalDate.parse(input, DateTimeFormatter.ofPattern("M/d/yyyy"));
                    project.setClosingDate(date);
                    exitcode = 1;
                }
                break;
                case 5: {
                    //just toggle visibility from here
                    project.toggleVisibility();
                    System.out.println("Project is now " + project.getStatus().toString());
                    exitcode = 1;
                }
                break;
                case 6: {
                    //print list of flats
                    exitcode = selectFlats(project);
                    //get flat selection here
                }
                break;
                case 7: {
                    exitcode = editOfficers(project);
                    //print list of officers
                }
                break;
            }
        }
        return exitcode;
    }

    private static int selectFlats(Project project){
        while(true) {
            Scanner sc = new Scanner(System.in);
            System.out.println("Select Flat (a to add new flat):");
            List<Flat> flats = project.getFlats();
            int index = 1;
            for (Flat flat : flats) {
                System.out.print(index + ". ");
                flat.print();
                index++;
            }

            Map<String, Integer> options = new HashMap<>();
            options.put("b", -2);
            options.put("a", -3);
            int choice;
            while (true) {
                String input = sc.nextLine();
                choice = utils.getRange(options, 1, flats.size(), input);
                if (choice == -2) {
                    return 2; //go back to options.
                } else if (choice == -1) {
                    System.out.println("Invalid Option");
                } else if (choice == -3) {
                    addFlats(project);
                    return 2;
                }
                else {
                    break;
                }
            }

            Flat selectedFlat = flats.get(choice - 1);
            System.out.println("1. Edit Flat");
            System.out.println("2. Delete Flat");

            while (true) {
                String input = sc.nextLine();
                choice = utils.getRange(options, 1, 2, input);
                if (choice == -2) {
                    return 2;
                } else if (choice == -1) {
                    System.out.println("Invalid Option");
                } else {
                    break;
                }
            }

            switch (choice) {
                case 1: {
                    editFlat(selectedFlat);
                    return 2;
                }
                case 2: {
                    deleteFlat(selectedFlat);
                    return 2;
                }
            }
        }
    }

    private static void addFlats(Project project)
    {
        List<Flat> flats = requestFlats();

        if(flats.isEmpty())
        {
            return;
        }

        System.out.println("Flats being Added:");
        for (Flat flat : flats) {
            flat.print();
        }
        System.out.println("Confirm? (y/n)");
        Scanner sc = new Scanner(System.in);
        while(true) {
            String input = sc.nextLine();
            if (input.equals("y")) {
                for (Flat flat : flats) {
                    project.addFlat(flat);
                }
            } else if (input.equals("n")) {
                return;
            } else {
                System.out.println("Invalid Option");
            }
        }
    }

    private static void deleteFlat(Flat flat){
        //double check
        flat.getProject().printBasicInformation();
        flat.print();
        System.out.println("Are you sure to delete the flat? (y/n)");
        Scanner sc = new Scanner(System.in);
        while(true)
        {
            String input = sc.nextLine();
            if(input.equals("y")) {
                flat.deleteSelf();
                break;
            }
            else if(input.equals("n")) {
                break;
            }
            else {
                System.out.println("Invalid Option");
            }
        }
    }

    private static void editFlat(Flat flat)
    {
        System.out.println("1. Edit Flat Type");
        System.out.println("2. Edit Flat Price");
        System.out.println("3. Edit Flat Units");

        Scanner sc = new Scanner(System.in);
        Map<String, Integer> options = new HashMap<>();
        options.put("b", -2);
        int choice = -1;
        while(true) {
            String input = sc.nextLine();
            choice = utils.getRange(options, 1,  3, input);
            if (choice == -2) {
                return;
            }
            else if (choice == -1) {
                System.out.println("Invalid Option");
            }
            else {
                break;
            }
        }

        switch(choice) {
            case 1: {
                System.out.println("Previous Flat Type: ");
                System.out.println("New Flat Type: ");

                System.out.println("1. 2-Room: ");
                System.out.println("2. 3-Room: ");

                while(true) {
                    String input = sc.nextLine();
                    int flatType = utils.getRange(options, 1, 2, input);
                    if (flatType == -1) {
                        System.out.println("Invalid Option");
                    } else if (flatType == -2) {
                        return;
                    } else if (flatType == 1) {
                        flat.setType(Flat.Type.TwoRoom);
                        break;
                    } else if (flatType == 2) {
                        flat.setType(Flat.Type.ThreeRoom);
                        break;
                    }
                }

            }
            break;
            case 2: {
                System.out.println("Previous Flat Price: ");
                System.out.println("New Flat Price: ");

                while (true)
                {
                    String input = sc.nextLine();
                    if(input.equals("b"))
                    {
                        return;
                    }
                    if(utils.assertFloat(input))
                    {
                        flat.setPrice(Float.parseFloat(input));
                        break;
                    }
                    else {
                        System.out.println("Invalid Option");
                    }
                }
            }
            break;
            case 3: {
                System.out.println("Previous Flat Units: ");
                System.out.println("New Flat Units: ");

                while (true)
                {
                    String input = sc.nextLine();
                    if(input.equals("b"))
                    {
                        return;
                    }
                    if(utils.assertInt(input))
                    {
                        flat.setUnits(Integer.parseInt(input));
                        break;
                    }
                    else {
                        System.out.println("Invalid Option");
                    }
                }
            }
            break;
            default: {

            }
            break;
        }
    }

    private static int editOfficers(Project project){

        while(true) {
            System.out.println("Officers: ");
            List<Officer> officers = project.getOfficers();
            int index = 1;
            for(Officer officer : officers) {
                System.out.println(index + ". " + officer.getName());
                index++;
            }
            Map<String, Integer> options = new HashMap<>();
            options.put("b", -2);
            Scanner sc = new Scanner(System.in);
            System.out.println("Select Officer to Remove (b to back): ");
            Officer selectedOfficer = null;
            int choice;
            while (true) {
                String input = sc.nextLine();
                choice = utils.getRange(options, 1, officers.size(), input);
                if (choice == -2) {
                    return 2; //go back to options.
                } else if (choice == -1) {
                    System.out.println("Invalid Option");
                } else {
                    break;
                }
            }

            selectedOfficer = officers.get(choice-1);

            if(selectedOfficer == null)
            {
                return -1;
            }

            System.out.println("Are you sure to remove the officer? (y/n)");
            {
                while(true) {
                    if (sc.nextLine().equals("y")) {
                        project.removeOfficer(selectedOfficer);
                        break;
                    } else if (sc.nextLine().equals("n")) {
                        break;
                    } else {
                        System.out.println("Invalid Option");
                    }
                }
            }

        }
    }

    private static int deleteListing(Project project){
        Project.print(project, true);
        System.out.println("Confirm Delete?");

        Scanner sc = new Scanner(System.in);
        while(true)
        {
            String input = sc.nextLine();
            if(input.equalsIgnoreCase("y"))
            {
                project.delete();
                return-1;
            }
            else if(input.equalsIgnoreCase("n"))
            {
                return 1;
            }
            else {
                System.out.println("Invalid Option");
            }
        }
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

        List<Flat> flats = requestFlats();

        //manager will be passed in as user.loggeduser.
        Project project = projectController.addProject(projectName,neighbourhood, applicationOpeningDate, applicationClosingDate, (Manager) userController.getLoggedUser(), officerSlots);
        for(Flat flat : flats) {
            project.addFlat(flat);
        }

        System.out.println("enter any key to continue");
        sc.nextLine();
    }

    private static List<Flat> requestFlats()
    {
        Scanner sc = new Scanner(System.in);
        List<Flat> flats = new ArrayList<Flat>();
        while(true) {
            System.out.println("Flat Type: ");
            String flatType = sc.nextLine();

            System.out.println("Flat Price: ");
            float flatPrice = -1;
            while(true) {
                String strFlatPrice = sc.nextLine();
                if (utils.assertFloat(strFlatPrice)) {
                    flatPrice = Float.parseFloat(strFlatPrice);
                    break;
                }
                else {
                    System.out.println("Invalid Option");
                }
            }

            int units = -1;
            System.out.println("No of Units: ");
            while(true) {
                String strUnits = sc.nextLine();
                if(utils.assertInt(strUnits))
                {
                    units = Integer.parseInt(strUnits);
                    break;
                }
                else {
                    System.out.println("Invalid Option");
                }
            }

            if(flatPrice >= 0 && units >= 0) { //assert that both are above 0
                Flat flat = new Flat(flatType, flatPrice, units);
                flats.add(flat);
            }

            System.out.println("Enter another flat? (\"N\" to exit): ");
            String choice = sc.nextLine();
            if(choice.equalsIgnoreCase("n"))
                break;
        }
        return flats;
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
        List<Application> applications = applicationController.getApplications(List.of(Application.Status.REQUESTED_WITHDRAW), Application.Type.Applicant);
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
                    selected_app.withdraw();
                }
                break;
                default: //default case should never happen as its asserted beforehand.
                    break;
            }
        }
    }

    private static void viewOfficerRegistrations()
    {
        List<Application> applications = applicationController.getApplications(List.of(Application.Status.PENDING), Application.Type.Officer);
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
                default: //default case should never happen as its asserted beforehand.
                break;
            }
        }
    }

    private static void viewAllEnquiries(){
        List<Project> projects = ProjectController.getProjects();

        if(projects.isEmpty()) {
            return;
        }

        List<Project> filtered_projects = new ArrayList<>();
        for(Project project : projects) {
            if(!project.getEnquiries().isEmpty()) {
                filtered_projects.add(project);
            }
        }

        int index = 1;
        for(Project project : filtered_projects) {
            System.out.print(index + ": ");
            System.out.print(project.getProjectName() + ", " + project.getManager().getName());
            System.out.print(", Enquiries: " + project.getEnquiries().size() + "\n");
            index++;
        }

        System.out.println("View Enquiries of Project (number to select, q to quit): ");
        Scanner sc = new Scanner(System.in);

        Map<String, Integer> options = new HashMap<>();
        options.put("q", -2);

        Project selectedProject = null;
        while(true)
        {
            String input = sc.nextLine();
            int choice = utils.getRange(options, 1, filtered_projects.size(), input);
            if (choice == -2) {
                return;
            } else if (choice == -1) {
                System.out.println("Invalid Option");
            } else {
                selectedProject = projects.get(choice - 1);
                break;
            }
        }

        if(selectedProject != null) { //sanity check, should never happen
            for(Enquiry enquiry : selectedProject.getEnquiries()) {
                enquiry.print();
            }
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
