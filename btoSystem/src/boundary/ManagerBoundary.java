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
        System.out.println("Welcome " + UserController.getLoggedUser().getName()); //add applicant name here
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
                    viewAllEnquiries();
                    exit = true;
                    waitForContinue(true);
                    break;
                case 4:
                    exit = true;
                    System.out.println("Logging Out");
                    break;
                default:
                    break;
            }
        }
        //log out of everything.
        UserController.clearLoggedUser();
        ProjectController.clearSelectedProject();
    }

    public static void displayDashboard()
    {
        //within dashboard just show outstanding stuff.

        System.out.println("1. View BTO Project Listings"); //contains, RUD functions. should be able to filter visibility
        System.out.println("2. Create BTO Project Listing");
        System.out.println("3. View All Enquiries");
        System.out.println("4. Log Out");
    }

    private static void viewBTOListings()
    {
        if(!(UserController.getLoggedUser() instanceof Manager)) //sanity check.
        {
            return;
        }

        Scanner sc = new Scanner(System.in);
        List<Project.Status> filter = new ArrayList<>(List.of(Project.Status.VISIBLE));
        int exitcode = 0;
        while(exitcode == 0) {
            //view only manager's projects.
            List<Project> projects = ((Manager) UserController.getLoggedUser()).getProjects(filter);

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
                    projects = ProjectController.getProjects(filter);
                    for(Project project : projects) {
                        project.print(true);
                    }
                    //wait for next key;
                    utils.waitKey();
                    break;
                }else {
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
            ProjectController.getSelectedProject().print(true);
            Project selected_project = ProjectController.getSelectedProject();
            System.out.println("1. Update Selected Project");
            System.out.println("2. Delete Selected Project");
            System.out.println("3. View Applicant Applications");
            System.out.println("4. View Applicant Withdrawal Requests");
            System.out.println("5. View Officer Applications");
            System.out.println("6. View Enquiries");
            System.out.println("7. Generate Report");
            System.out.println("q or to quit, b to back.");
            Map<String, Integer> options = new HashMap<>();
            Scanner sc = new Scanner(System.in);
            options.put("b", -2);
            options.put("q", -3);
            while (true) {
                String input = sc.nextLine();
                int choice = utils.getRange(options, 1, 6, input);
                if (choice == -2) {
                    return 0;
                }
                else if (choice == -3)
                {
                    return -1;
                } else if (choice == 1) {
                    exitcode = updateListing(selected_project);
                    break;
                } else if (choice == 2) {
                    exitcode = deleteListing(selected_project);
                    break;
                } else if(choice == 3) {
                    exitcode = viewProjectApplications(selected_project);
                    break;
                } else if(choice == 4) {
                    exitcode = viewProjectWithdrawals(selected_project);
                    break;
                } else if(choice == 5) {
                    exitcode = viewProjectOfficerApplications(selected_project);
                    break;
                } else if(choice == 6) {
                    exitcode = viewProjectEnquiries(selected_project);
                    break;
                } else if(choice == 7) {
                    exitcode = generateReport(selected_project);
                    break;
                } else {
                    System.out.println("Invalid Option");
                }

            }
        }
        return exitcode; //go back to main menu
    }

    private static int updateListing(Project project){
        project.print(true);

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
                    System.out.println("New Name (b to back, q to quit): ");
                    String input = sc.nextLine();
                    if(input.equalsIgnoreCase("q")) {
                        return -1; //-1 brings back to main menu.
                    }
                    else if(input.equalsIgnoreCase("b")) {
                        return 0; //0 brings back to view page.
                    }
                    //data validation here
                    project.setProjectName(input);
                    exitcode = 1;
                }
                break;
                case 2: {
                    System.out.println("Previous Neighbourhood: " + project.getNeighborhood());
                    System.out.println("New Neighbourhood (b to back, q to quit): ");
                    String input = sc.nextLine();
                    if(input.equalsIgnoreCase("q")) {
                        return -1; //-1 brings back to main menu.
                    }
                    else if(input.equalsIgnoreCase("b")) {
                        return 0; //0 brings back to view page.
                    }
                    //data validation here
                    project.setNeighborhood(input);
                    exitcode = 1;

                }
                break;
                case 3: {
                    System.out.println("Previous Opening Date: " + project.getOpeningDate().toString());
                    System.out.println("New Opening Date (M/D/YYYY) (b to back, q to quit): ");

                    //data validation here
                    while(true) {
                        String input = sc.nextLine();
                        if(input.equalsIgnoreCase("q")) {
                            return -1; //-1 brings back to main menu.
                        }
                        else if(input.equalsIgnoreCase("b")) {
                            return 0; //0 brings back to view page.
                        }
                        try {
                            LocalDate date = LocalDate.parse(input, DateTimeFormatter.ofPattern("M/d/yyyy"));
                            if(UserController.getLoggedUser().assertDateClash(date, project) && project.getClosingDate().isAfter(date))
                            {
                                project.setOpeningDate(date);
                                exitcode = 1;
                                break;
                            }
                            else {
                                System.out.println("Date Assertion Failed");
                            }
                        } catch (Exception e) {
                            System.out.println("Invalid Date");
                        }
                    }

                }
                break;
                case 4: {
                    System.out.println("Previous Closing Date: " + project.getClosingDate().toString());
                    System.out.println("New Closing Date (M/D/YYYY) (b to back, q to quit): ");
                    //data validation here
                    while(true) {
                        String input = sc.nextLine();
                        if(input.equalsIgnoreCase("q")) {
                            return -1; //-1 brings back to main menu.
                        }
                        else if(input.equalsIgnoreCase("b")) {
                            return 0; //0 brings back to view page.
                        }
                        try {
                            LocalDate date = LocalDate.parse(input, DateTimeFormatter.ofPattern("M/d/yyyy"));
                            if(UserController.getLoggedUser().assertDateClash(date, project) && project.getClosingDate().isBefore(date))
                            {
                                project.setClosingDate(date);
                                exitcode = 1;
                                break;
                            }
                            else {
                                System.out.println("Date Assertion Failed, Enter new Date");
                            }
                        } catch (Exception e) {
                            System.out.println("Invalid Date");
                        }
                    }
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
                flat.deleteSelf(); //TODO:: SHIFT INTO CONTROLLER.
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

                System.out.println("1. 2-Room");
                System.out.println("2. 3-Room");

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
                        flat.setPrice(Float.parseFloat(input)); //TODO:: SHIFT INTO CONTROLLER
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
                        project.removeOfficer(selectedOfficer); //TODO:: SHIFT INTO CONTROLLER
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
        project.print(true);
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
        String projectName;
        String neighbourhood;
        LocalDate openingDate = null;
        LocalDate closingDate = null;
        int officerSlots = -1;
        Project.Status visibility;

        reqName:
        System.out.println("Project Name: ");  //do assertion checks here
        while(true)
        {
            String input = sc.nextLine();
            if(input.equalsIgnoreCase("q")) {
                return;
            } else if (input.equalsIgnoreCase("b")) {
                return;
            }
            else {
                projectName = input;
                break;
            }
        }

        System.out.println("Neighbourhood: ");
        while(true)
        {
            String input = sc.nextLine();
            if(input.equalsIgnoreCase("q")) {
                return;
            } else if (input.equalsIgnoreCase("b")) {
                return;
            }
            else{
                neighbourhood = input;
                break;
            }
        }

        System.out.println("Application Opening Date (MM/DD/YYYY): ");
        while(true)
        {
            String input = sc.nextLine();
            if(input.equalsIgnoreCase("q")) {
                return;
            } else if (input.equalsIgnoreCase("b")) {
                return;
            }

            try{ openingDate = LocalDate.parse(input, DateTimeFormatter.ofPattern("M/d/yyyy")); } //assert date format
            catch (Exception e){ System.out.println("Invalid Format"); }

            if(UserController.getLoggedUser().assertDateClash(openingDate, null) && openingDate != null){ //assert date clashing
                break;
            }
            else { System.out.println("Date Clashed with Managed Project"); }
        }

        System.out.println("Application Closing Date (MM/DD/YYYY): ");
        while(true)
        {
            String input = sc.nextLine();
            if(input.equalsIgnoreCase("q")) {
                return;
            } else if (input.equalsIgnoreCase("b")) {
                return;
            }

            try{ closingDate = LocalDate.parse(input, DateTimeFormatter.ofPattern("M/d/yyyy")); } //assert date format
            catch (Exception e){ System.out.println("Invalid Format"); }

            if(UserController.getLoggedUser().assertDateClash(closingDate, null) && closingDate != null){ //assert date clashing
                break;
            }
            else { System.out.println("Date Clashed with Managed Project"); }
        }

        System.out.println("Officer Slots: ");
        while(true)
        {
            String input = sc.nextLine();
            if(input.equalsIgnoreCase("q")) {
                return;
            } else if (input.equalsIgnoreCase("b")) {
                return;
            }

            try{ officerSlots = Integer.parseInt(input); } //assert date format
            catch (Exception e){ System.out.println("Invalid Format"); }

            if(officerSlots >= 0) {
                break;
            }
        }

        System.out.println("Visibility (1: Visible, 2: Invisible): ");
        while(true)
        {
            String input = sc.nextLine();
            if(input.equalsIgnoreCase("q")) {
                return;
            } else if (input.equalsIgnoreCase("b")) {
                return;
            }
            if(input.equalsIgnoreCase("1")) {
                visibility = Project.Status.VISIBLE;
                break;
            }
            else if(input.equalsIgnoreCase("2")) {
                visibility = Project.Status.INVISIBLE;
                break;
            }
            else {
                System.out.println("Invalid Option");
            }
        }

        //request type1 details
        //skip line

        List<Flat> flats = requestFlats();

        //manager will be passed in as user.loggeduser.
        Project project = ProjectController.addProject(projectName,neighbourhood, openingDate, closingDate, (Manager) userController.getLoggedUser(), officerSlots, visibility);
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
            System.out.println("1. 2-Room");
            System.out.println("2. 3-Room");
            System.out.print("Flat Type: ");
            Flat.Type type = null;
            while(true)
            {

                String input = sc.nextLine();
                if(input.equalsIgnoreCase("q")) {
                    return flats;
                }
                else if(input.equalsIgnoreCase("1") || input.equalsIgnoreCase("2")) {
                    if(input.equalsIgnoreCase("1")) {
                        type = Flat.Type.TwoRoom;
                    }
                    if(input.equalsIgnoreCase("2")) {
                        type = Flat.Type.ThreeRoom;
                    }
                    break;
                }
                else {
                    System.out.println("Invalid Option");
                }

            }

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
                Flat flat = new Flat(type, flatPrice, units);
                flats.add(flat);
            }

            System.out.println("Enter another flat? (\"N\" to exit): ");
            String choice = sc.nextLine();
            if(choice.equalsIgnoreCase("n"))
                break;
        }
        return flats;
    }

    private static int viewProjectApplications(Project project) {
        while(true) {
            project.printBasicInformation();
            List<Application> applications = project.getApplications(List.of(Application.Status.PENDING), Application.Type.Applicant);
            int index = 1;
            for (Application app : applications) {
                System.out.println(index++ + ": ");
                app.print();
            }

            if(!applications.isEmpty()) {
                System.out.println("Select Application (number to select, q to quit, b to back, a to view all):");
            }
            else {
                System.out.println("(q to quit, b to back, a to view all):");
            }
            Map<String, Integer> options = new HashMap<>();
            options.put("b", -2);
            options.put("q", -3);
            options.put("a", -4);
            Application selected_application = null;
            //selection code here. a to view all, number to select, b to back, q to quit.
            Scanner sc = new Scanner(System.in);
            int choice;
            while (true) {
                String input = sc.nextLine();
                choice = utils.getRange(options, 1, applications.size(), input);
                if (choice == -2) {
                    return 1; //go back to options.
                } else if (choice == -3) {
                    return -1;
                } else if (choice == -4) {
                    applications = project.getApplications(List.of(Application.Status.PENDING, Application.Status.SUCCESSFUL, Application.Status.BOOKED), Application.Type.Applicant);
                    for (Application app : applications) {
                        app.print();
                    }
                    utils.waitKey();
                    break;
                } else if (choice == -1) {
                    System.out.println("Invalid Option");
                } else {
                    selected_application = applications.get(choice - 1);
                    break;
                }

            }

            if (selected_application != null) {
                //ask for approve/reject
                System.out.println("Approve/Reject Application (y/n, b to back) : ");
                while (true) {
                    String input = sc.nextLine();
                    if (input.equals("y")) {
                        selected_application.approve();
                        return 1;
                    } else if (input.equals("n")) {
                        selected_application.reject();
                        return 1;
                    } else if (input.equals("b")) {
                        return 1;
                    } else {
                        System.out.println("Invalid Option");
                    }
                }
            }
        }
    }

    private static int viewProjectWithdrawals(Project project) {
        while(true) {
            project.printBasicInformation();
            List<Application> applications = project.getApplications(List.of(Application.Status.REQUESTED_WITHDRAW, Application.Status.REQUESTED_WITHDRAW_BOOKED), Application.Type.Applicant);
            int index = 1;
            for (Application app : applications) {
                System.out.println(index++ + ": ");
                app.print();
            }

            if(!applications.isEmpty()) {
                System.out.println("Select Application (number to select, q to quit, b to back, a to view all):");
            }
            else {
                System.out.println("(q to quit, b to back, a to view all):");
            }
            Map<String, Integer> options = new HashMap<>();
            options.put("b", -2);
            options.put("q", -3);
            options.put("a", -4);
            Application selected_application = null;
            //selection code here. a to view all, number to select, b to back, q to quit.
            Scanner sc = new Scanner(System.in);
            int choice;
            while (true) {
                String input = sc.nextLine();
                choice = utils.getRange(options, 1, applications.size(), input);
                if (choice == -2) {
                    return 1; //go back to options.
                } else if (choice == -3) {
                    return -1;
                } else if (choice == -4) {
                    applications = project.getApplications(List.of(Application.Status.REQUESTED_WITHDRAW, Application.Status.REQUESTED_WITHDRAW_BOOKED, Application.Status.WITHDRAWN), Application.Type.Applicant);
                    for (Application app : applications) {
                        app.print();
                    }
                    utils.waitKey();
                    break;
                } else if (choice == -1) {
                    System.out.println("Invalid Option");
                } else {
                    selected_application = applications.get(choice - 1);
                    break;
                }
            }

            if (selected_application != null) {
                //ask for approve/reject
                System.out.println("Approve/Reject Withdrawal (y/n, b to back) : ");
                while(true) {
                    String input = sc.nextLine();
                    if (input.equals("y")) {
                        selected_application.approve();
                        return 1;
                    } else if (input.equals("n")) {
                        selected_application.reject();
                        return 1;
                    } else if (input.equals("b")) {
                        return 1;
                    } else {
                        System.out.println("Invalid Option");
                    }
                }
            }
        }
    }

    private static int viewProjectEnquiries(Project project)
    {
        List<Enquiry.Status> filter = new ArrayList<>(List.of(Enquiry.Status.PENDING));
        while(true) {
            List<Enquiry> enquiries = project.getEnquiries(filter);

            int index = 1;
            for (Enquiry enq : enquiries) {
                System.out.println(index++ + ": ");
                enq.print();
            }

            if (!enquiries.isEmpty()) { //add visibility option here.
                if(filter.contains(Enquiry.Status.CLOSED)) {
                    System.out.println("Select Enquiry to Respond (number to select, q to quit, b to back, c: toggle closed enquiries (ON) ): ");
                }
                else {
                    System.out.println("Select Enquiry to Respond (number to select, q to quit, b to back, c: toggle closed enquiries (OFF) ): ");
                }
            } else {
                if(filter.contains(Enquiry.Status.CLOSED)) {
                    System.out.println("No Enquiries (q to quit, b to back, c: toggle closed enquiries (ON))");
                }
                else {
                    System.out.println("No Enquiries (q to quit, b to back, c: toggle closed enquiries (OFF))");
                }
            }

            Map<String, Integer> options = new HashMap<>();
            options.put("b", -2);
            options.put("q", -3);
            options.put("c", -4);
            Enquiry selected_enquiry = null;
            //selection code here. a to view all, number to select, b to back, q to quit.
            Scanner sc = new Scanner(System.in);
            int choice;
            while (true) {
                String input = sc.nextLine();
                choice = utils.getRange(options, 1, enquiries.size(), input);
                if (choice == -2) {
                    return 1; //go back to options.
                } else if (choice == -3) {
                    return -1;
                } else if (choice == -4) { //allow for editing of all things.
                    if (filter.contains(Enquiry.Status.CLOSED)) {
                        filter.remove(Enquiry.Status.CLOSED);
                    }
                    else {
                        filter.add(Enquiry.Status.CLOSED);
                    }
                    break;
                } else if (choice == -1) {
                    System.out.println("Invalid Option");
                } else {
                    selected_enquiry = enquiries.get(choice - 1);
                    break;
                }
            }

            if (selected_enquiry != null) { //user has selected an enquiry.
                System.out.print("Response: ");
                String response = sc.nextLine();
                if(response.equalsIgnoreCase("q") || response.equalsIgnoreCase("b") )
                {
                    return 0;
                }
                if(!selected_enquiry.respond(userController.getLoggedUser(), response))
                {
                    System.out.println("Response Failed");
                }
            }
        }
    }

    private static int viewProjectOfficerApplications(Project project)
    {
        while(true) {
            project.printBasicInformation();
            List<Application.Status> filter = List.of(Application.Status.PENDING);
            List<Application> applications = project.getApplications(filter, Application.Type.Officer);
            int index = 1;
            for (Application app : applications) {
                System.out.println(index++ + ": ");
                app.print();
            }

            if (!applications.isEmpty()) {
                System.out.println("Select Application (number to select, q to quit, b to back, a to view all): ");
            } else {
                System.out.println(" q to quit, a to view all: ");
            }

            Map<String, Integer> options = new HashMap<>();
            options.put("b", -2);
            options.put("q", -3);
            options.put("a", -4);
            Application selected_application = null;
            //selection code here. a to view all, number to select, b to back, q to quit.
            Scanner sc = new Scanner(System.in);
            int choice;
            while (true) {
                String input = sc.nextLine();
                choice = utils.getRange(options, 1, applications.size(), input);
                if (choice == -2) {
                    return 1; //go back to options.
                } else if (choice == -3) {
                    return -1;
                } else if (choice == -4) {
                    applications = project.getApplications(Application.Type.Officer);
                    for (Application app : applications) {
                        app.print();
                    }
                    utils.waitKey();
                    break;
                } else if (choice == -1) {
                    System.out.println("Invalid Option");
                } else {
                    selected_application = applications.get(choice - 1);
                    break;
                }
            }

            if (selected_application != null) {
                //ask for approve/reject
                System.out.println("Approve/Reject Application (y/n, b to back) : ");
                while (true) {
                    String input = sc.nextLine();
                    if (input.equals("y")) {
                        selected_application.approve();
                        return 1;
                    } else if (input.equals("n")) {
                        selected_application.reject();
                        return 1;
                    } else if (input.equals("b")) {
                        return 1;
                    } else {
                        System.out.println("Invalid Option");
                    }
                }
            }
        }
    }

    private static int generateReport(Project project)
    {
        return -1;
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
